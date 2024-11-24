CREATE TABLE IF NOT EXISTS document (
    id SERIAL UNIQUE NOT NULL,
    
    title VARCHAR(50) NOT NULL,
    language VARCHAR(50) NOT NULL,
    price NUMERIC(20, 0) NOT NULL,
    publisher_name VARCHAR(100) NOT NULL,
    document_type VARCHAR(50) NOT NULL CHECK (document_type IN ('book', 'magazine')),
    volume INTEGER,
    frequency VARCHAR(50),
    edition INTEGER,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genre (
    id SERIAL UNIQUE NOT NULL,
    
    name VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS document_genre (
    document_id SERIAL NOT NULL,
	genre_id SERIAL NOT NULL,
    
    CONSTRAINT fk_document 
    	FOREIGN KEY (document_id) 
    		REFERENCES document (id) 
    			ON DELETE CASCADE,
    			
    CONSTRAINT fk_genre 
    	FOREIGN KEY (genre_id) 
    		REFERENCES genre (id) 
    			ON DELETE CASCADE,
    			
    PRIMARY KEY (document_id, genre_id)
);

CREATE TABLE IF NOT EXISTS document_author (
    document_id SERIAL NOT NULL,
    author_name VARCHAR(70) NOT NULL,
    
    CONSTRAINT fk_document 
    	FOREIGN KEY (document_id) 
    		REFERENCES document (id) 
    			ON DELETE CASCADE,
    			
    PRIMARY KEY (document_id, author_name)
);

CREATE TABLE IF NOT EXISTS employee (
    id SERIAL UNIQUE NOT NULL,
    
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    ward VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    salary NUMERIC(20, 2) NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS library (
    id SERIAL NOT NULL,
    
    name VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    ward VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS librarian (
    id SERIAL UNIQUE NOT NULL,
    library_id SERIAL NOT NULL,
    
    CONSTRAINT fk_id_employee 
    	FOREIGN KEY (id) 
    		REFERENCES employee (id),
    		
    CONSTRAINT fk_library 
    	FOREIGN KEY (id) 
    		REFERENCES library (id),
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS warehouse (
    id SERIAL UNIQUE NOT NULL,
    
    name VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    ward VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS warehouse_staff (
    id SERIAL UNIQUE NOT NULL,
    
    warehouse_id SERIAL NOT NULL,
    
    CONSTRAINT fk_id_employee 
    	FOREIGN KEY (id) 
    		REFERENCES employee (id),
    		
    CONSTRAINT fk_id_warehouse 
    	FOREIGN KEY (warehouse_id) 
    		REFERENCES warehouse (id),
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "order" (
    id SERIAL UNIQUE NOT NULL,
    
    created_date TIMESTAMP NOT NULL,
    ship_start_date TIMESTAMP NOT NULL,
    ship_end_date TIMESTAMP NOT NULL,
    total_price INT NOT NULL,
    
    warehouse_id SERIAL NOT NULL,
    warehouse_staff_id SERIAL NOT NULL,
    
    CONSTRAINT fk_warehouse 
    	FOREIGN KEY (warehouse_id) 
    		REFERENCES warehouse (id),
    		
    CONSTRAINT fk_warehouse_staff 
    	FOREIGN KEY (warehouse_staff_id) 
    		REFERENCES warehouse_staff (id),
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS copy (
    id SERIAL NOT NULL,
    
    fee NUMERIC(20, 0) NOT NULL,
    retail_price NUMERIC(20, 0) NOT NULL,
    status VARCHAR(50) CHECK (status IN ('borrowing', 'available', 'lost', 'transporting')),
    
    document_id SERIAL NOT NULL,
    library_id SERIAL,
    warehouse_id SERIAL,
    order_id SERIAL NOT NULL,
    
    CONSTRAINT fk_document 
    	FOREIGN KEY (document_id) 
    		REFERENCES document (id),
    		
    CONSTRAINT fk_library 
    	FOREIGN KEY (library_id) 
    		REFERENCES library (id),
    		
    CONSTRAINT fk_warehouse 
    	FOREIGN KEY (warehouse_id) 
    		REFERENCES warehouse (id),
    		
    CONSTRAINT fk_order 
    	FOREIGN KEY (order_id) 
    		REFERENCES "order" (id),
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS workshift (
    id SERIAL NOT NULL,
    
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    
    employee_id serial not null,

    
    constraint fk_employee
    foreign key (employee_id)
    references employee (id),
    
    PRIMARY KEY (id, employee_id, date)
);


CREATE TABLE IF NOT EXISTS member (
    id SERIAL NOT NULL,
    
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    ward VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    salary INTEGER NOT NULL,
    
    is_banned boolean not null default false,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS transfer (
    id SERIAL UNIQUE NOT NULL,
    
    created_date TIMESTAMP NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    amount INTEGER NOT NULL,
    type VARCHAR(50) CHECK (type IN ('import', 'export')),
    
    library_id SERIAL NOT NULL,
    warehouse_id SERIAL NOT NULL,
    warehouse_staff_id SERIAL NOT NULL,
    
    CONSTRAINT fk_library 
    	FOREIGN KEY (library_id) 
    		REFERENCES library (id),
    		
    CONSTRAINT fk_warehouse 
    	FOREIGN KEY (warehouse_id) 
    		REFERENCES warehouse (id),
    		
    CONSTRAINT fk_warehouse_staff 
    	FOREIGN KEY (warehouse_staff_id) 
    		REFERENCES warehouse_staff (id),
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS copy_transfer (
    copy_id SERIAL NOT NULL,
    transfer_id SERIAL NOT NULL,
    
    CONSTRAINT fk_copy 
    	FOREIGN KEY (copy_id) 
    		REFERENCES copy (id),
    		
    CONSTRAINT fk_transfer 
    	FOREIGN KEY (transfer_id) 
    		REFERENCES transfer (id),
    		
    PRIMARY KEY (copy_id, transfer_id)
);

CREATE TABLE IF NOT EXISTS borrow_ticket (
    id SERIAL UNIQUE NOT NULL,
    
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    fee INTEGER NOT NULL,
    fine INTEGER,
    status_on_return varchar(50) check (status_on_return in ('good', 'bad', 'lost')),
    
    copy_id SERIAL NOT NULL,
    member_id SERIAL NOT NULL,
    librarian_id SERIAL NOT NULL,
    
    CONSTRAINT fk_copy 
    	FOREIGN KEY (copy_id) 
    		REFERENCES copy (id),
    		
    CONSTRAINT fk_member 
    	FOREIGN KEY (member_id) 
    		REFERENCES member (id),
    		
    CONSTRAINT fk_librarian 
    	FOREIGN KEY (librarian_id) 
    		REFERENCES librarian (id),
    		
    PRIMARY KEY (id)
);




