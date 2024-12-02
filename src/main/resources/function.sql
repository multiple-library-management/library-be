CREATE OR REPLACE FUNCTION get_documents_with_genre_count(
    IN doc_type VARCHAR(50)
)
RETURNS TABLE(d_id INT, title VARCHAR, genre_count INT, message TEXT)
AS $$
DECLARE
    genre_count INT;
BEGIN
    -- Validate the input parameter
    IF doc_type IS NULL OR doc_type NOT IN ('book', 'magazine') THEN
        RAISE EXCEPTION 'Invalid document type: %', doc_type;
    END IF;

    -- Loop through documents by type and count the associated genres
    FOR d_id, title IN 
        SELECT d.id, d.title
        FROM documents d
        WHERE d.document_type = doc_type
    LOOP
        -- Count the number of genres for the current document
        SELECT COUNT(*) INTO genre_count
        FROM document_genres dg
        WHERE dg.document_id = d_id;

        -- Set message based on the genre count
        IF genre_count = 0 THEN
            message := 'This document has no genres.';
        ELSE
            message := 'This document has ' || genre_count || ' genres.';
        END IF;

        -- Return the document ID, title, genre count, and message
        RETURN NEXT;
    END LOOP;
    
    -- Return empty result if no documents match the type
    RETURN;
END;
$$ LANGUAGE plpgsql;


-- Calling the function to get documents of type 'book'
-- SELECT * FROM get_documents_with_genre_count('book');

CREATE OR REPLACE FUNCTION get_member_borrow_ticket_status_count(
    IN m_id INT
)
RETURNS TABLE(ticket_status VARCHAR(50), ticket_count INT, message TEXT)
AS $$
DECLARE
    statuses TEXT[] := ARRAY['good', 'bad', 'lost'];  -- Array of statuses
BEGIN
    -- Validate the input member_id exists in the 'members' table
    IF NOT EXISTS (SELECT 1 FROM members WHERE id = m_id) THEN
        RAISE EXCEPTION 'Member with ID % does not exist.', m_id;
    END IF;

    -- Loop through each possible borrow ticket status from the array
    FOREACH ticket_status IN ARRAY statuses LOOP
        -- Debugging: print ticket_status in the loop
        RAISE NOTICE 'Processing status: %', ticket_status;

        -- Count the number of borrow tickets for this status
        SELECT COALESCE(COUNT(*), 0) INTO ticket_count  -- Ensure ticket_count is 0 if no tickets found
        FROM borrow_tickets bt
        WHERE bt.member_id = m_id
          AND bt.status_on_return = ticket_status;

        -- Debugging: check ticket_count
        RAISE NOTICE 'Ticket count for status %: %', ticket_status, ticket_count;

        -- Set a message based on the ticket count for this status
        IF ticket_count = 0 THEN
            message := 'No tickets with status ' || ticket_status || '.';
        ELSE
            message := 'Member has ' || ticket_count || ' tickets with status ' || ticket_status || '.';
        END IF;

        -- Return the status, ticket count, and message for the current status
        RETURN NEXT;
    END LOOP;

    -- Return empty result if no tickets found for any status
    RETURN;
END;
$$ LANGUAGE plpgsql;




-- -- Sample members data
-- INSERT INTO members (id, first_name, last_name, address, district, ward, street, city, phone, email, salary)
-- VALUES
-- (1, 'John', 'Doe', '123 Main St', 'District A', 'Ward 1', 'Street X', 'City Y', '123-456-7890', 'john.doe@example.com', 5000),
-- (2, 'Jane', 'Smith', '456 Oak St', 'District B', 'Ward 2', 'Street Y', 'City Z', '987-654-3210', 'jane.smith@example.com', 4500);

-- INSERT INTO libraries (
--     name,
--     address,
--     district,
--     ward,
--     street,
--     city
-- ) 
-- VALUES 
-- (
--     'Central Library', 
--     '123 Library St', 
--     'District A', 
--     'Ward 5', 
--     'Main Street', 
--     'City X'
-- );

-- -- Insert employee first into employees table
-- INSERT INTO employees (
--     first_name,
--     last_name,
--     address,
--     district,
--     ward,
--     street,
--     city,
--     phone,
--     email,
--     salary
-- ) 
-- VALUES 
-- (
--     'Alice', 
--     'Johnson', 
--     '456 Employee Rd', 
--     'District A', 
--     'Ward 5', 
--     'Second Street', 
--     'City X',
--     '123-111-2233', 
--     'alice.johnson@example.com',
--     4000
-- );

-- -- Suppose Alice's employee ID is 1 and we want to assign her as a librarian
-- -- INSERT INTO librarians (
-- --     library_id, 
-- --     employee_id
-- -- ) 
-- -- VALUES 
-- -- (
-- --     1, 
-- --     1
-- -- );



-- -- Sample borrow tickets data
-- INSERT INTO borrow_tickets (start_date, end_date, return_date, fee, fine, status_on_return, copy_id, member_id, librarian_id)
-- VALUES
-- ('2023-01-01', '2023-01-10', '2023-01-12', 10, 5, 'good', 4, 1, 1),
-- ('2023-02-01', '2023-02-10', '2023-02-12', 10, 5, 'bad', 4, 1, 1),
-- ('2023-03-01', '2023-03-10', '2023-03-15', 10, 5, 'lost', 4, 2, 1);


-- SELECT * FROM get_member_borrow_ticket_status_count(1);

