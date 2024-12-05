----------------------------------------------------- EITHER LIBRARY_ID OR WAREHOUSE_ID MUST BE NULL, THE OTHER ONE HAVE A VALUE  -----------------------------------------------------
CREATE OR REPLACE FUNCTION enforce_one_location_copy()
RETURNS TRIGGER AS $$
BEGIN
    -- Check that only one of library_id or warehouse_id is set (non-NULL)
    IF (NEW.library_id IS NOT NULL AND NEW.warehouse_id IS NOT NULL) THEN
        RAISE EXCEPTION 'Both library_id and warehouse_id cannot have values. One must be NULL.';
    ELSIF (NEW.library_id IS NULL AND NEW.warehouse_id IS NULL) THEN
        RAISE EXCEPTION 'Both library_id and warehouse_id cannot be NULL. One must have a value.';
    END IF;

    RETURN NEW; -- Allow the operation if the condition is satisfied
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER before_insert_update_check_one_location_copy
BEFORE INSERT OR UPDATE ON copies
FOR EACH ROW
EXECUTE FUNCTION enforce_one_location_copy();

----------------------------------------------------- MEMBER CANNOT BORROW LIST COPIES  -----------------------------------------------------
CREATE OR REPLACE FUNCTION prevent_borrowing_lost_copy()
    RETURNS TRIGGER AS $$
BEGIN
    -- Check if the copy has a 'lost' status
    IF EXISTS (
        SELECT 1
        FROM copies c
        WHERE c.id = NEW.copy_id
          AND (c.status <> 'available')
    ) THEN
        -- Raise an exception if the copy is marked as lost
        RAISE EXCEPTION 'The copy with ID % is lost and cannot be borrowed.', NEW.copy_id;
    END IF;

    -- Allow the operation to proceed if the copy is not lost
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER prevent_borrowing_lost_copy_trigger
BEFORE INSERT ON copy_borrow_tickets
FOR EACH ROW
EXECUTE FUNCTION prevent_borrowing_lost_copy();

----------------------------------------------------- EACH MEMBER IS ALLOWED TO BORROW 2 COPIES  -----------------------------------------------------
CREATE OR REPLACE FUNCTION prevent_exceeding_borrow_limit()
RETURNS TRIGGER AS $$
BEGIN
    -- Check how many copies are already associated with the borrow ticket
    IF (SELECT COUNT(*)
        FROM copy_borrow_tickets cbt
        WHERE cbt.borrow_ticket_id = NEW.borrow_ticket_id) >= 2 THEN
        -- Raise an exception if the member is trying to borrow more than 2 copies in this borrow ticket
        RAISE EXCEPTION 'Member % cannot borrow more than 2 copies in a single borrow ticket.', 
            (SELECT member_id FROM borrow_tickets WHERE id = NEW.borrow_ticket_id);
    END IF;

    -- Allow the operation to proceed if the member has borrowed fewer than 2 copies in this borrow ticket
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER prevent_exceeding_borrow_limit_trigger
BEFORE INSERT ON copy_borrow_tickets
FOR EACH ROW
EXECUTE FUNCTION prevent_exceeding_borrow_limit();

----------------------------------------------------- MEMBER MUST RETURN BEFORE BORROWING NEW DOCUMENT  -----------------------------------------------------
CREATE OR REPLACE FUNCTION prevent_multiple_active_tickets()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if there is any other borrow ticket with the same Member_ID and NULL Return_Date
    IF EXISTS (
        SELECT 1
        FROM copy_borrow_tickets cbt
        JOIN borrow_tickets bt ON cbt.borrow_ticket_id = bt.id
        WHERE bt.member_id = (SELECT member_id FROM borrow_tickets WHERE id = NEW.borrow_ticket_id)
          AND cbt.return_date IS NULL
          AND cbt.borrow_ticket_id != NEW.borrow_ticket_id -- Exclude the current ticket being inserted
    ) THEN
        -- Raise an exception if such a ticket is found
        RAISE EXCEPTION 'Member already has an active borrow ticket (Return_Date is NULL).';
    END IF;

    RETURN NEW; -- Allow the operation to proceed if the condition is not violated
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER before_insert_check_active_borrow_ticket
BEFORE INSERT ON copy_borrow_tickets
FOR EACH ROW
EXECUTE FUNCTION prevent_multiple_active_tickets();

----------------------------------------------------- SET COPY STATUS TO BORROWING  -----------------------------------------------------
CREATE OR REPLACE FUNCTION set_copy_status_borrowing()
    RETURNS TRIGGER AS $$
BEGIN
    -- Set the status of the copy to 'borrowing' when it is borrowed
    UPDATE copies
    SET status = 'borrowing'
    WHERE id = NEW.copy_id
      AND EXISTS (SELECT 1 FROM copy_borrow_tickets WHERE copy_id = NEW.copy_id AND return_date IS NULL);

    -- Return the inserted row (standard behavior for a BEFORE INSERT trigger)
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER set_copy_status_borrowing_trigger
BEFORE INSERT ON copy_borrow_tickets
FOR EACH ROW
EXECUTE FUNCTION set_copy_status_borrowing();

----------------------------------------------------- IF MEMBER DO NOT RETURN AFTER 30 DAYS THEY WILL BE BANNED, AND THE BOOK IS MARKED TO LOST  -----------------------------------------------------

-- SELECT cron.schedule(
--     'check_overdue_borrowed_books',
--     '0 0 * * 0', -- Every Sunday at midnight
--     $$
--         -- Ban members who have overdue borrowed books
--         UPDATE members m
--         SET is_banned = true
--         FROM borrow_tickets bt
--         JOIN copy_borrow_tickets cbt ON bt.id = cbt.borrow_ticket_id
--         WHERE m.id = bt.member_id
--           AND cbt.return_date IS NULL  -- Book is not yet returned
--           AND current_date > (cbt.end_date + INTERVAL '30 days');

--         -- Mark copies as 'lost' for overdue books
--         UPDATE copies c
--         SET status = 'lost'
--         FROM copy_borrow_tickets cbt
--         WHERE c.id = cbt.copy_id
--           AND cbt.return_date IS NULL  -- Book is not yet returned
--           AND current_date > (cbt.end_date + INTERVAL '30 days');
--     $$
-- );

----------------------------------------------------- SET COPY STATUS TO BORROWING -----------------------------------------------------


CREATE OR REPLACE FUNCTION set_copy_status_to_borrowing()
RETURNS TRIGGER AS $$
BEGIN
    -- Update the copy status to 'borrowing' when a new copy_borrow_tickets entry is created
    UPDATE copies
    SET status = 'borrowing'
    WHERE id = NEW.copy_id;

    RETURN NEW; -- Return the new row to allow the insert to continue
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_insert_set_copy_status_borrowing
AFTER INSERT ON copy_borrow_tickets
FOR EACH ROW
EXECUTE FUNCTION set_copy_status_to_borrowing();



----------------------------------------------------- IF MEMBER IS BEING BANNED CANNOT CREATE TICKET TO BORROW -----------------------------------------------------

CREATE OR REPLACE FUNCTION prevent_banned_member_ticket()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if the member is banned
    IF EXISTS (
        SELECT 1
        FROM members
        WHERE id = NEW.member_id AND is_banned = TRUE
    ) THEN
        RAISE EXCEPTION 'Banned members cannot create a new borrow ticket';
    END IF;

    -- Allow the insertion if the member is not banned
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER before_insert_check_banned_member
BEFORE INSERT ON borrow_tickets
FOR EACH ROW
EXECUTE FUNCTION prevent_banned_member_ticket();


----------------------------------------------------- THE LIBRARY MUST HOLD AT LEST 1O COPY OF EACH DOCUMENT -----------------------------------------------------

CREATE OR REPLACE FUNCTION prevent_borrowing_if_low_stock()
RETURNS TRIGGER AS $$
DECLARE
    available_count INTEGER;
    l_id INTEGER;
    doc_id INTEGER;
BEGIN
    -- Get the library_id and document_id of the copy being borrowed
    SELECT library_id, document_id INTO l_id, doc_id
    FROM copies
    WHERE id = NEW.copy_id;

    -- Count the number of available copies of the same document in the library
    SELECT COUNT(*) INTO available_count
    FROM copies
    WHERE document_id = doc_id
      AND library_id = l_id
      AND status = 'available';

    -- Raise an exception if available copies are less than 10
    IF available_count < 10 THEN
        RAISE EXCEPTION 'Cannot borrow this document. The number of available copies in the library is below 10.';
    END IF;

    -- Ensure the specific copy being borrowed is available
    IF (SELECT status FROM copies WHERE id = NEW.copy_id) <> 'available' THEN
        RAISE EXCEPTION 'Cannot borrow this copy. It is not available for borrowing.';
    END IF;

    -- Allow the borrowing operation
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER before_insert_check_copy_availability
BEFORE INSERT ON copy_borrow_tickets
FOR EACH ROW
EXECUTE FUNCTION prevent_borrowing_if_low_stock();

----------------------------------------------------- AUTOMATICALLY UPDATE THE AMOUNT OF TRANSFER -----------------------------------------------------
CREATE OR REPLACE FUNCTION update_transfer_amount()
RETURNS TRIGGER AS $$
BEGIN
    -- Update the amount field in the transfers table based on the number of copies in the copy_transfers table
    UPDATE transfers
    SET amount = (
        SELECT COUNT(*)
        FROM copy_transfers
        WHERE transfer_id = NEW.transfer_id
    )
    WHERE id = NEW.transfer_id;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_insert_update_transfer_amount
AFTER INSERT ON copy_transfers
FOR EACH ROW
EXECUTE FUNCTION update_transfer_amount();

----------------------------------------------------- UPDATE THE COPY LIBRARY ID AND WAREHOUSE ID -----------------------------------------------------
CREATE OR REPLACE FUNCTION update_copy_library_warehouse()
RETURNS TRIGGER AS $$
BEGIN
    -- Get the type of the transfer by joining the transfers table
    DECLARE
        transfer_type VARCHAR(50);
    BEGIN
        -- Retrieve the transfer type from the transfers table
        SELECT type INTO transfer_type
        FROM transfers
        WHERE id = NEW.transfer_id;

        -- Handle the case when the transfer type is 'export'
        IF transfer_type = 'export' THEN
            -- Ensure the copy is currently in a library (i.e., library_id is not NULL and warehouse_id is NULL)
            IF EXISTS (
                SELECT 1
                FROM copies
                WHERE id = NEW.copy_id
                AND library_id IS NOT NULL
                AND warehouse_id IS NULL
            ) THEN
                -- Perform export: set library_id to NULL and warehouse_id to the transfer's warehouse_id
                UPDATE copies
                SET library_id = NULL,
                    warehouse_id = (SELECT warehouse_id FROM transfers WHERE id = NEW.transfer_id)
                WHERE id = NEW.copy_id;
            ELSE
                -- Raise an exception if the copy is not in a library or already in a warehouse
                RAISE EXCEPTION 'Cannot export copies that are not in a library or are already in a warehouse.';
            END IF;

        -- Handle the case when the transfer type is 'import'
        ELSIF transfer_type = 'import' THEN
            -- Ensure the copy is currently in a warehouse (i.e., warehouse_id is not NULL and library_id is NULL)
            IF EXISTS (
                SELECT 1
                FROM copies
                WHERE id = NEW.copy_id
                AND warehouse_id IS NOT NULL
                AND library_id IS NULL
            ) THEN
                -- Perform import: set warehouse_id to NULL and library_id to the transfer's library_id
                UPDATE copies
                SET warehouse_id = NULL,
                    library_id = (SELECT library_id FROM transfers WHERE id = NEW.transfer_id)
                WHERE id = NEW.copy_id;
            ELSE
                -- Raise an exception if the copy is not in a warehouse or already in a library
                RAISE EXCEPTION 'Cannot import copies that are not in a warehouse or are already in a library.';
            END IF;
        END IF;

        -- Return the new row for the trigger
        RETURN NEW;
    END;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER after_insert_update_copy_library_warehouse
AFTER INSERT ON copy_transfers
FOR EACH ROW
EXECUTE FUNCTION update_copy_library_warehouse();




