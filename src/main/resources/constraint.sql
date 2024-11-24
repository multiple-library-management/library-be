----------------------------------------------------- TOTAL PARTICIPATION ON LIBRARIAN AND WAREHOUSE STAFF -----------------------------------------------------
CREATE OR REPLACE FUNCTION check_total_participation_employee()
RETURNS TRIGGER AS $$
BEGIN
	IF NOT EXISTS (SELECT 1 FROM Employee WHERE id = NEW.ID) THEN
		RAISE EXCEPTion 'This new employee is not registered yet';
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER ensure_specialization_librarian
BEFORE INSERT OR UPDATE ON librarian
FOR EACH ROW
EXECUTE FUNCTION check_total_participation_employee();

CREATE OR REPLACE TRIGGER ensure_specialization_warehouse_staff
BEFORE INSERT OR UPDATE ON warehouse_staff
FOR EACH ROW
EXECUTE FUNCTION check_total_participation_employee();

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


create or replace TRIGGER check_one_location_copy
BEFORE INSERT OR UPDATE ON copy
FOR EACH ROW
EXECUTE FUNCTION enforce_one_location_copy();

----------------------------------------------------- MEMBER MUST RETURN BEFORE BORROWING NEW DOCUMENT  -----------------------------------------------------
CREATE OR REPLACE FUNCTION prevent_multiple_active_tickets()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if there is any other borrow ticket with the same Member_ID and NULL Return_Date
    IF EXISTS (
        SELECT 1
        FROM borrow_ticket
        WHERE Member_ID = NEW.Member_ID
          AND Return_Date IS NULL
          AND ID != NEW.ID -- Exclude the current ticket being inserted
    ) THEN
        -- Raise an exception if such a ticket is found
        RAISE EXCEPTION 'Member % already has an active borrow ticket (Return_Date is NULL).', NEW.Member_ID;
    END IF;

    RETURN NEW; -- Allow the operation to proceed if the condition is not violated
END;
$$ LANGUAGE plpgsql;


create or replace TRIGGER check_active_borrow_ticket
BEFORE INSERT ON borrow_ticket
FOR EACH ROW
EXECUTE FUNCTION prevent_multiple_active_tickets();

----------------------------------------------------- IF MEMBER DO NOT RETURN AFTER 30 DAYS THEY WILL BE BANNED, AND THE BOOK IS MARKED TO LOST  -----------------------------------------------------

CREATE OR REPLACE FUNCTION check_and_update_status()
RETURNS TRIGGER AS $$
BEGIN
    -- Update status_on_return if overdue
    IF NEW.return_date IS NULL AND CURRENT_DATE - NEW.end_date > 30 THEN
        UPDATE borrow_ticket
        SET status_on_return = 'lost'
        WHERE id = NEW.id;

		UPDATE copy
		SET status = 'lost'
		WHERE id = NEW.copy_id;

        UPDATE member
        SET is_banned = true
        WHERE id = NEW.member_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


create or REPLACE TRIGGER auto_update_status
AFTER INSERT OR UPDATE ON borrow_ticket
FOR EACH ROW
EXECUTE FUNCTION check_and_update_status();


----------------------------------------------------- IF MEMBER IS BEING BANNED CANNOT CREATE TICKET TO BORROW -----------------------------------------------------

CREATE OR REPLACE FUNCTION prevent_banned_member_ticket()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if the member is banned
    IF EXISTS (
        SELECT 1
        FROM Member
        WHERE ID = NEW.Member_ID AND Is_Banned = TRUE
    ) THEN
        RAISE EXCEPTION 'Banned members cannot create a new borrow ticket';
    END IF;

    -- Allow the insertion if the member is not banned
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_banned_member
BEFORE INSERT ON Borrow_Ticket
EXECUTE FUNCTION prevent_banned_member_ticket();


----------------------------------------------------- THE LIBRARY MUST HOLD AT LEST 1O COPY OF EACH DOCUMENT -----------------------------------------------------

CREATE OR REPLACE FUNCTION prevent_borrowing_if_low_stock()
RETURNS TRIGGER AS $$
DECLARE
    available_count INTEGER;
    library_id INTEGER;
BEGIN
    -- Get the library_id of the copy being borrowed
    SELECT library_id INTO library_id
    FROM copy
    WHERE id = NEW.copy_id;

    -- Count the number of available copies of the same document in the library
    SELECT COUNT(*) INTO available_count
    FROM copy
    WHERE document_id = (
            SELECT document_id
            FROM copy
            WHERE id = NEW.copy_id
        )
      AND library_id = library_id
      AND status = 'available';

    -- Raise an exception if available copies are less than 10
    IF available_count < 10 THEN
        RAISE EXCEPTION 'Cannot borrow this document. The number of available copies in the library is below 10.';
    END IF;

    -- Ensure the copy being borrowed is available
--    IF (
--        SELECT status
--        FROM copy
--        WHERE id = NEW.copy_id
--    ) <> 'available' THEN
--        RAISE EXCEPTION 'Cannot borrow this copy. It is not available for borrowing.';
--    END IF;

    -- Allow the borrowing operation
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_copy_availability
BEFORE INSERT ON borrow_ticket
FOR EACH ROW
EXECUTE FUNCTION prevent_borrowing_if_low_stock();




