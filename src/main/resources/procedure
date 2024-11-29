 --  ____     ___     ____   _   _   __  __   _____   _   _   _____   ____  
 -- |  _ \   / _ \   / ___| | | | | |  \/  | | ____| | \ | | |_   _| / ___| 
 -- | | | | | | | | | |     | | | | | |\/| | |  _|   |  \| |   | |   \___ \ 
 -- | |_| | | |_| | | |___  | |_| | | |  | | | |___  | |\  |   | |    ___) |
 -- |____/   \___/   \____|  \___/  |_|  |_| |_____| |_| \_|   |_|   |____/ 
                                                                         
CREATE OR REPLACE PROCEDURE insert_document(
    IN title VARCHAR(50),
    IN "language" VARCHAR(50),
    IN image VARCHAR(700),
    IN price INTEGER,
    IN publisher_name VARCHAR(100),
    IN author_name VARCHAR(50),
    IN document_type VARCHAR(50),
    IN volume INTEGER,
    IN frequency VARCHAR(50),
    IN edition INTEGER
)
LANGUAGE plpgsql
AS $$
DECLARE
    new_document_id INTEGER;
BEGIN
    -- Check if title is provided
    IF title IS NULL OR title = '' THEN
        RAISE EXCEPTION 'Title is required and cannot be empty';
    END IF;

    -- Check if price is valid (greater than 0)
    IF price <= 0 THEN
        RAISE EXCEPTION 'Price must be a positive value';
    END IF;

    -- Check if publisher_name is provided
    IF publisher_name IS NULL OR publisher_name = '' THEN
        RAISE EXCEPTION 'Publisher name is required and cannot be empty';
    END IF;

    -- Check if document_type is valid (either 'book' or 'magazine')
    IF document_type NOT IN ('book', 'magazine') THEN
        RAISE EXCEPTION 'Invalid document type. Allowed values are ''book'' or ''magazine''';
    END IF;

    -- Conditional checks for 'magazine' and 'book' types
    IF document_type = 'magazine' THEN
        -- Magazine must have values for volume and frequency, and edition must be NULL
        IF volume IS NULL OR volume < 1 THEN
            RAISE EXCEPTION 'Magazine must have a valid positive volume';
        END IF;

        IF frequency IS NULL OR frequency NOT IN ('daily', 'weekly', 'monthly', 'yearly') THEN
            RAISE EXCEPTION 'Magazine must have a valid frequency value';
        END IF;

        IF edition IS NOT NULL THEN
            RAISE EXCEPTION 'Edition must be NULL for magazines';
        END IF;

    ELSIF document_type = 'book' THEN
        -- Book must have a valid edition, and volume and frequency must be NULL
        IF edition IS NULL OR edition < 1 THEN
            RAISE EXCEPTION 'Book must have a valid positive edition';
        END IF;

        IF volume IS NOT NULL THEN
            RAISE EXCEPTION 'Volume must be NULL for books';
        END IF;

        IF frequency IS NOT NULL THEN
            RAISE EXCEPTION 'Frequency must be NULL for books';
        END IF;

    END IF;

    -- Insert the document record and get the document ID
    INSERT INTO documents (
        title, 
        "language", 
        image, 
        price, 
        publisher_name, 
        document_type, 
        volume, 
        frequency, 
        edition
    )
    VALUES (
        title, 
        "language", 
        image, 
        price, 
        publisher_name, 
        document_type, 
        volume, 
        frequency, 
        edition
    )
    RETURNING id INTO new_document_id;  -- Store the new document ID

    -- Insert the author into the document_authors table, associating the author with the new document ID
    IF author_name IS NOT NULL AND author_name <> '' THEN
        INSERT INTO document_authors (
            document_id, 
            author_name
        )
        VALUES (
            new_document_id, 
            author_name
        );
    END IF;

    -- Optionally, return a success message
    RAISE NOTICE 'Document inserted successfully with ID: %, and author: %', new_document_id, author_name;

END;
$$;


CREATE OR REPLACE PROCEDURE update_document(
    IN doc_id INTEGER,  -- Document ID to identify the document to update
    IN title VARCHAR(50),
    IN "language" VARCHAR(50),
    IN image VARCHAR(700),
    IN price INTEGER,
    IN publisher_name VARCHAR(100),
    IN document_type VARCHAR(50),
    IN volume INTEGER,
    IN frequency VARCHAR(50),
    IN edition INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if doc_id is valid
    IF doc_id IS NULL OR doc_id <= 0 THEN
        RAISE EXCEPTION 'Invalid document ID';
    END IF;

    -- Check if title is provided
    IF title IS NULL OR title = '' THEN
        RAISE EXCEPTION 'Title is required and cannot be empty';
    END IF;

    -- Check if price is valid (greater than 0)
    IF price <= 0 THEN
        RAISE EXCEPTION 'Price must be a positive value';
    END IF;

    -- Check if publisher_name is provided
    IF publisher_name IS NULL OR publisher_name = '' THEN
        RAISE EXCEPTION 'Publisher name is required and cannot be empty';
    END IF;

    -- Check if document_type is valid (either 'book' or 'magazine')
    IF document_type NOT IN ('book', 'magazine') THEN
        RAISE EXCEPTION 'Invalid document type. Allowed values are ''book'' or ''magazine''';
    END IF;

    -- Conditional checks for 'magazine' and 'book' types
    IF document_type = 'magazine' THEN
        -- Magazine must have values for volume and frequency, and edition must be NULL
        IF volume IS NULL OR volume < 1 THEN
            RAISE EXCEPTION 'Magazine must have a valid positive volume';
        END IF;

        IF frequency IS NULL OR frequency NOT IN ('daily', 'weekly', 'monthly', 'yearly') THEN
            RAISE EXCEPTION 'Magazine must have a valid frequency value';
        END IF;

        IF edition IS NOT NULL THEN
            RAISE EXCEPTION 'Edition must be NULL for magazines';
        END IF;

    ELSIF document_type = 'book' THEN
        -- Book must have a valid edition, and volume and frequency must be NULL
        IF edition IS NULL OR edition < 1 THEN
            RAISE EXCEPTION 'Book must have a valid positive edition';
        END IF;

        IF volume IS NOT NULL THEN
            RAISE EXCEPTION 'Volume must be NULL for books';
        END IF;

        IF frequency IS NOT NULL THEN
            RAISE EXCEPTION 'Frequency must be NULL for books';
        END IF;

    END IF;

    -- Update the document record in the table
    UPDATE documents AS d
    SET 
        title = COALESCE($2, d.title),              -- Use the parameter $2 for title
        "language" = COALESCE($3, d."language"),     -- Use the parameter $3 for language
        image = COALESCE($4, d.image),              -- Use the parameter $4 for image
        price = COALESCE($5, d.price),              -- Use the parameter $5 for price
        publisher_name = COALESCE($6, d.publisher_name), -- Use the parameter $6 for publisher_name
        document_type = COALESCE($7, d.document_type), -- Use the parameter $7 for document_type
        volume = COALESCE($8, d.volume),            -- Use the parameter $8 for volume
        frequency = COALESCE($9, d.frequency),      -- Use the parameter $9 for frequency
        edition = COALESCE($10, d.edition)          -- Use the parameter $10 for edition
    WHERE d.id = doc_id;

    -- Optionally, you can return a message or value here if needed
    RAISE NOTICE 'Document with ID % updated successfully', doc_id;
    
END;
$$;

CREATE OR REPLACE PROCEDURE delete_document(
    IN doc_id INTEGER  -- Document ID to identify the document to delete
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if doc_id is valid
    IF doc_id IS NULL OR doc_id <= 0 THEN
        RAISE EXCEPTION 'Invalid document ID';
    END IF;

    -- Step 1: Delete related records from document_genres
    DELETE FROM document_genres
    WHERE document_id = doc_id;

    -- Step 2: Delete related records from document_authors
    DELETE FROM document_authors
    WHERE document_id = doc_id;

    -- Step 3: Delete related records from copies
    DELETE FROM copies
    WHERE document_id = doc_id;

    -- Step 4: Delete the document itself
    DELETE FROM documents
    WHERE id = doc_id;

    -- Optionally, you can return a message here if needed
    RAISE NOTICE 'Document with ID % and its references have been deleted successfully', doc_id;

END;
$$;

 --     _      _   _   _____   _   _    ___    ____    ____  
 --    / \    | | | | |_   _| | | | |  / _ \  |  _ \  / ___| 
 --   / _ \   | | | |   | |   | |_| | | | | | | |_) | \___ \ 
 --  / ___ \  | |_| |   | |   |  _  | | |_| | |  _ <   ___) |
 -- /_/   \_\  \___/    |_|   |_| |_|  \___/  |_| \_\ |____/ 
                                                          
CREATE OR REPLACE PROCEDURE add_author_to_document(
	IN document_id INTEGER,
	IN author_name VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if the document exists
    IF NOT EXISTS (SELECT 1 FROM documents WHERE id = document_id) THEN
        RAISE EXCEPTION 'Document with ID % does not exist', document_id;
    END IF;

    -- Check if the author_name is provided
    IF author_name IS NULL OR author_name = '' THEN
        RAISE EXCEPTION 'Author name is required and cannot be empty';
    END IF;

    -- Insert the author into the document_authors table
    INSERT INTO document_authors (
        document_id,
        author_name
    )
    VALUES (
        document_id,
        author_name
    );

    -- Optionally, return a success message
    RAISE NOTICE 'Author % added to document with ID %', author_name, document_id;

END;
$$;

 --   ____   _____   _   _   ____    _____   ____  
 --  / ___| | ____| | \ | | |  _ \  | ____| / ___| 
 -- | |  _  |  _|   |  \| | | |_) | |  _|   \___ \ 
 -- | |_| | | |___  | |\  | |  _ <  | |___   ___) |
 --  \____| |_____| |_| \_| |_| \_\ |_____| |____/ 

CREATE OR REPLACE PROCEDURE insert_genre(
	IN genre_name VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
	-- Check if genre name is provided
	IF genre_name IS NULL OR genre_name = '' THEN
		RAISE EXCEPTION 'Genre name is required and cannot be empty';
	END IF;

	-- Insert genre into genres table
	INSERT INTO genres (name)
	VALUES (genre_name);

	RAISE NOTICE 'Genre "%s" inserted successfully', genre_name;
END;
$$
;

CREATE OR REPLACE PROCEDURE update_genre(
	IN genre_id INTEGER,
	IN genre_name VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
	-- Check if genre exists
	IF NOT EXISTS (SELECT 1 FROM genres WHERE id = genre_id) THEN
		RAISE EXCEPTION 'Genre with ID % does not exist', genre_id;
	END IF;

	-- Check if genre_name is provided
	IF genre_name IS NULL OR genre_name = '' then
		RAISE EXCEPTION 'New genre name is required an cannot be empty';
	END IF;

	-- Update the genre name
	UPDATE genres
	SET name = genre_name
	WHERE id = genre_id;

    RAISE NOTICE 'Genre with ID % updated to "%"', genre_id, genre_name;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_genre(
    IN genre_id INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if the genre exists
    IF NOT EXISTS (SELECT 1 FROM genres WHERE id = genre_id) THEN
        RAISE EXCEPTION 'Genre with ID % does not exist', genre_id;
    END IF;

    -- Check if the genre is associated with any documents
    IF EXISTS (SELECT 1 FROM document_genres WHERE genre_id = genre_id) THEN
        RAISE EXCEPTION 'Cannot delete genre with ID % as it is associated with documents', genre_id;
    END IF;

    -- Delete the genre
    DELETE FROM genres WHERE id = genre_id;

    -- Optionally, return a success message
    RAISE NOTICE 'Genre with ID % deleted successfully', genre_id;
END;
$$;

CREATE OR REPLACE PROCEDURE add_genre_to_document (
	IN document_id INTEGER,
	IN genre_id INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
	-- Check if document exists
	IF NOT EXISTS (SELECT 1 FROM documents WHERE id = document_id) THEN
        RAISE EXCEPTION 'Document with ID % does not exist', document_id;
	END IF;

	-- Check if genre_id exists
	IF NOT EXISTS (SELECT 1 FROM genres WHERE id = genre_id) THEN 
		RAISE EXCEPTION 'Genre with ID % does not exist', genre_id;
	END IF;

	-- Insert the genre to document relationship
	INSERT INTO document_genres (document_id, genre_id)
	VALUES (document_id, genre_id);

	RAISE NOTICE 'Genre with ID % added to document with ID %', genre_id, document_id;
END;
$$;


-- CALL insert_document(
--     'The Great Book',          -- title
--     'English',                 -- language
--     'http://example.com/image.jpg',  -- image (URL)
--     250,                       -- price
--     'Publisher X',             -- publisher_name
--     'book',                    -- document_type ('book' or 'magazine')
--     NULL,                      -- volume (NULL for books)
--     NULL,                      -- frequency (NULL for books)
--     1                          -- edition (must be a positive integer for books)
-- );


-- CALL update_document(
--     3,                         -- doc_id (ID of the document to update)
--     'Updated Book Title',      -- title
--     NULL,                 -- language
--     'http://example.com/updated_image.jpg',  -- image (URL)
--     300,                       -- price
--     'Publisher Y',             -- publisher_name
--     'book',                    -- document_type ('book' or 'magazine')
--     NULL,                      -- volume (NULL for books)
--     NULL,                      -- frequency (NULL for books)
--     2                          -- edition (must be a positive integer for books)
-- );

-- CALL delete_document(3);


