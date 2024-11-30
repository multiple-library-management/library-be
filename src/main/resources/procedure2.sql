CREATE OR REPLACE PROCEDURE get_document_count_by_type(
    IN p_document_type VARCHAR(50),
    OUT doc_count INT
)
LANGUAGE plpgsql
AS
$$
BEGIN
    -- Check if the document type is valid (enforcing the CHECK constraint)
    IF p_document_type NOT IN ('book', 'magazine') THEN
        RAISE EXCEPTION 'Invalid document type: %. Valid types are ''book'' or ''magazine''.', p_document_type;
    END IF;

    -- Query to return the count of documents filtered by document type
    BEGIN
        SELECT COUNT(*) INTO doc_count
        FROM documents d
        JOIN document_authors da ON d.id = da.document_id
        WHERE d.document_type = p_document_type;
    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE EXCEPTION 'Foreign key violation: Unable to find matching document for author.';
        WHEN others THEN
            RAISE EXCEPTION 'An unexpected error occurred while processing the request: %', SQLERRM;
    END;
END;
$$;


CREATE OR REPLACE PROCEDURE get_total_order_price(
    IN p_start_date TIMESTAMP,
    IN p_end_date TIMESTAMP,
    IN p_warehouse_id INT,
    OUT total_price INT
)
AS
$$
BEGIN
    -- Check if the start date is before the end date
    IF p_start_date > p_end_date THEN
        RAISE EXCEPTION 'Start date (%s) cannot be after end date (%s).', p_start_date, p_end_date;
    END IF;

    -- Query to return the total price of orders for a specific warehouse
    BEGIN
        SELECT SUM(o.total_price) INTO total_price
        FROM "orders" o
        JOIN copies c ON o.id = c.order_id
        WHERE o.created_date BETWEEN p_start_date AND p_end_date
          AND o.warehouse_id = p_warehouse_id;
        
        -- Check if total price is NULL (in case no records matched)
        IF total_price IS NULL THEN
            RAISE EXCEPTION 'No orders found for the specified warehouse (ID: %) within the given date range.', p_warehouse_id;
        END IF;
        
    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE EXCEPTION 'Foreign key violation: Unable to find matching orders for warehouse ID: %. Please verify the warehouse and order relationships.', p_warehouse_id;
        WHEN others THEN
            RAISE EXCEPTION 'An unexpected error occurred while processing the request: %', SQLERRM;
    END;
END;
$$ LANGUAGE plpgsql;


-- Declare a variable to hold the result
DO $$ 
DECLARE
    doc_count INT;
BEGIN
    -- Call the procedure and store the result in doc_count
    CALL get_document_count_by_type('book', doc_count);
    
    -- Output the result
    RAISE NOTICE 'Document count: %', doc_count;
END $$;

DO $$ 
DECLARE
    total_price INT;
BEGIN
    -- Call the procedure and store the result in total_price
    CALL get_total_order_price('2024-11-01 00:00:00', '2024-12-30 23:59:59', 1, total_price);
    
    -- Output the result
    RAISE NOTICE 'Total price of orders: %', total_price;
END $$;

