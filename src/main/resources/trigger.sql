CREATE OR REPLACE FUNCTION update_order_total_price()
RETURNS TRIGGER AS $$
BEGIN
	-- Upadte the total price in the orders table whenever a new copy is added
	UPDATE "orders"
	SET total_price = total_price + (SELECT price FROM documents WHERE id = NEW.document_id)
	WHERE id = NEW.order_id;

	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_insert_copies
	AFTER INSERT ON "copies"
	FOR EACH ROW
	EXECUTE FUNCTION update_order_total_price();


CREATE OR REPLACE FUNCTION update_copy_status_on_return()
RETURNS TRIGGER AS $$
BEGIN
	-- If the return_date is not null, update the copy status to 'available'
	IF NEW.return_date IS NOT NULL THEN
		UPDATE "copies"
		SET status = 'available'
		WHERE id = NEW.copy_id;
	END IF;

	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_update_insert_return_date_borrow_tickets
	AFTER INSERT OR UPDATE OF return_date ON borrow_tickets
	FOR EACH ROW
	EXECUTE FUNCTION update_copy_status_on_return();

-- -- Insert a warehouse with id = 1
-- INSERT INTO warehouses (name, address, district, ward, street, city)
-- VALUES ('Warehouse 1', '123 Street', 'District A', 'Ward X', 'Main St.', 'City A');

-- -- Insert an employee into the employees table
-- INSERT INTO employees (first_name, last_name, address, district, ward, street, city, phone, email, salary)
-- VALUES ('John', 'Doe', '456 Another Street', 'District B', 'Ward Y', 'Elm St.', 'City B', '123-456-7890', 'johndoe@example.com', 3000);

-- -- DROP FUNCTION IF EXISTS check_total_participation_employee();
-- -- DROP TRIGGER IF EXISTS after_insert_update_ensure_specialization_librarian ON librarians;
-- -- DROP TRIGGER IF EXISTS after_insert_update_ensure_specialization_warehouse_staff ON warehouse_staffs;


-- -- Insert warehouse staff, referencing the employee and warehouse
-- INSERT INTO warehouse_staffs (warehouse_id, employee_id)
-- VALUES (1, 1);

-- -- Insert a sample order
-- INSERT INTO "orders" (created_date, ship_start_date, ship_end_date, total_price, warehouse_id, warehouse_staff_id)
-- VALUES ('2024-11-30 10:00:00', '2024-12-01 10:00:00', '2024-12-05 10:00:00', 0, 1, 1);

-- -- Insert a new genre
-- INSERT INTO genres (name)
-- VALUES ('Science Fiction'), ('Fantasy'), ('Non-fiction');


-- -- Insert a document (e.g., a book)
-- INSERT INTO documents (title, "language", image, price, publisher_name, document_type, volume, frequency, edition)
-- VALUES
-- ('The Galactic Odyssey', 'English', 'image_path_or_url_here', 25, 'Space Publishers', 'book', 1, NULL, 1);

-- -- Insert a record into document_genres to associate the document with a genre
-- INSERT INTO document_genres (document_id, genre_id)
-- VALUES
-- ((SELECT id FROM documents WHERE title = 'The Galactic Odyssey'), (SELECT id FROM genres WHERE name = 'Science Fiction'));

-- -- Insert an author for the document
-- INSERT INTO document_authors (document_id, author_name)
-- VALUES
-- ((SELECT id FROM documents WHERE title = 'The Galactic Odyssey'), 'John Doe');

-- -- Insert copies for this order
-- INSERT INTO "copies" (fee, retail_price, status, document_id, library_id, warehouse_id, order_id)
-- VALUES (5, 100, 'available', 1, NULL, 1, 2);

-- -- Check the total price of the order should now be 100
-- SELECT * FROM "orders" WHERE id = 2;


	