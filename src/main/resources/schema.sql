CREATE TABLE IF NOT EXISTS documents (
    id SERIAL UNIQUE NOT NULL,
    
    title VARCHAR(50) NOT NULL,
    "language" VARCHAR(50) NOT NULL,
    image VARCHAR(700),
    price INTEGER NOT NULL,
    publisher_name VARCHAR(100) NOT NULL,
    document_type VARCHAR(50) NOT NULL CHECK (document_type IN ('book', 'magazine')),
    volume INTEGER,
    frequency VARCHAR(50) CHECK (frequency IN ('daily', 'weekly', 'monthly', 'yearly')),
    edition INTEGER,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genres (
    id SERIAL UNIQUE NOT NULL,
    
    name VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS document_genres (
    document_id INT NOT NULL,
	genre_id INT NOT NULL,
    
    CONSTRAINT fk_document 
    FOREIGN KEY (document_id) 
    REFERENCES documents (id) 
    ON DELETE CASCADE,
    			
    CONSTRAINT fk_genre 
    FOREIGN KEY (genre_id) 
   	REFERENCES genres (id) 
    ON DELETE CASCADE,
    			
    PRIMARY KEY (document_id, genre_id)
);

CREATE TABLE IF NOT EXISTS document_authors (
    document_id INT NOT NULL,
    author_name VARCHAR(70) NOT NULL,
    
    CONSTRAINT fk_document 
    FOREIGN KEY (document_id) 
    REFERENCES documents (id) 
    ON DELETE CASCADE,
    			
    PRIMARY KEY (document_id, author_name)
);

CREATE TABLE IF NOT EXISTS employees (
    id SERIAL UNIQUE NOT NULL,
    
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,

	address varchar(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    ward VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
	
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    salary INTEGER NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "libraries" (
    id SERIAL NOT NULL,
    
    name VARCHAR(50) NOT NULL,

	address VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    ward VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS librarians (
	employee_id INT UNIQUE NOT NULL,

    library_id INT NOT NULL,

    CONSTRAINT fk_employee 
    FOREIGN KEY (employee_id) 
    REFERENCES employees (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_library 
   	FOREIGN KEY (library_id) 
    REFERENCES libraries (id)
	ON DELETE CASCADE,
    		
    PRIMARY KEY (employee_id)
);

CREATE TABLE IF NOT EXISTS warehouses (
    id SERIAL NOT NULL,
    
    name VARCHAR(50) NOT NULL,

	address VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    ward VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS warehouse_staffs (
	employee_id INT UNIQUE NOT NULL,

    warehouse_id INT NOT NULL,
    
    CONSTRAINT fk_employee 
    FOREIGN KEY (employee_id) 
    REFERENCES employees (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_warehouse 
   	FOREIGN KEY (warehouse_id) 
    REFERENCES warehouses (id)
	ON DELETE CASCADE,
    		
    PRIMARY KEY (employee_id)
);

CREATE TABLE IF NOT EXISTS "orders" (
    id SERIAL UNIQUE NOT NULL,
    
    created_date TIMESTAMP NOT NULL,
    ship_start_date TIMESTAMP NOT NULL,
    ship_end_date TIMESTAMP NOT NULL,
    total_price INT NOT NULL,
    
    warehouse_id INT NOT NULL,
    warehouse_staff_id INT NOT NULL,
    
    CONSTRAINT fk_warehouse 
   	FOREIGN KEY (warehouse_id) 
    REFERENCES warehouses (id)
	ON DELETE SET NULL,
    		
    CONSTRAINT fk_warehouse_staff 
    FOREIGN KEY (warehouse_staff_id) 
    REFERENCES warehouse_staffs (employee_id)
	ON DELETE SET NULL,
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "copies" (
    id SERIAL NOT NULL,
    
    fee INTEGER NOT NULL,
--     retail_price INTEGER NOT NULL,
    status VARCHAR(50) CHECK (status IN ('borrowing', 'available', 'lost', 'transporting')),
    
    document_id INT NOT NULL,
    library_id INT,
    warehouse_id INT,
    order_id INT NOT NULL,
    
    CONSTRAINT fk_document 
    FOREIGN KEY (document_id) 
    REFERENCES documents (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_library 
    FOREIGN KEY (library_id) 
    REFERENCES libraries (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_warehouse 
    FOREIGN KEY (warehouse_id) 
    REFERENCES warehouses (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_order 
   	FOREIGN KEY (order_id) 
    REFERENCES "orders" (id)
	ON DELETE NO ACTION,
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS workshifts (
    id SERIAL NOT NULL,
    
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    
    employee_id INT NOT NULL,

    
    CONSTRAINT fk_employee
    FOREIGN KEY (employee_id)
    REFERENCES employees (id)
	ON DELETE CASCADE,
    
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS "members" (
    id SERIAL NOT NULL,
    
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,

	address VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    ward VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    
    is_banned boolean not null default false,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS transfers (
    id SERIAL UNIQUE NOT NULL,
    
    created_date TIMESTAMP NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    amount INTEGER NOT NULL,
    type VARCHAR(50) CHECK (type IN ('import', 'export')),
    
    library_id INT NOT NULL,
    warehouse_id INT NOT NULL,
    warehouse_staff_id INT NOT NULL,
    
    CONSTRAINT fk_library 
   	FOREIGN KEY (library_id) 
    REFERENCES libraries (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_warehouse 
    FOREIGN KEY (warehouse_id) 
    REFERENCES warehouses (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_warehouse_staff 
    FOREIGN KEY (warehouse_staff_id) 
    REFERENCES warehouse_staffs (employee_id)
	ON DELETE CASCADE,
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS copy_transfers (
    copy_id INT NOT NULL,
    transfer_id INT NOT NULL,
    
    CONSTRAINT fk_copy 
    FOREIGN KEY (copy_id) 
    REFERENCES copies (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_transfer 
   	FOREIGN KEY (transfer_id) 
    REFERENCES transfers (id)
	ON DELETE CASCADE,
    		
    PRIMARY KEY (copy_id, transfer_id)
);

CREATE TABLE IF NOT EXISTS borrow_tickets (
    id SERIAL UNIQUE NOT NULL,
    
    member_id INT NOT NULL,
    librarian_id INT NOT NULL,
    
    CONSTRAINT fk_member 
    FOREIGN KEY (member_id) 
    REFERENCES members (id)
	ON DELETE CASCADE,
    		
    CONSTRAINT fk_librarian 
    FOREIGN KEY (librarian_id) 
    REFERENCES librarians (employee_id)
	ON DELETE CASCADE,
    		
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS copy_borrow_tickets (	
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    fee INTEGER NOT NULL,
    fine INTEGER,
    status_on_return varchar(50) check (status_on_return in ('good', 'bad', 'lost')),

    copy_id INT NOT NULL,
	borrow_ticket_id INT NOT NULL,

    CONSTRAINT fk_copy 
    FOREIGN KEY (copy_id) 
    REFERENCES copies (id)
	ON DELETE CASCADE,

	CONSTRAINT fk_borrow_ticket
	FOREIGN KEY (borrow_ticket_id)
	REFERENCES borrow_tickets (id)
	ON DELETE CASCADE,

	PRIMARY KEY (copy_id, borrow_ticket_id)
);




