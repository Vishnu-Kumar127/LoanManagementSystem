USE Loan_Management_System;
CREATE TABLE Role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_Number VARCHAR(15),
    user_name VARCHAR(50) UNIQUE,
    random_salt VARCHAR(50),
    password VARCHAR(255),
    address_id INT UNIQUE,
    FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE CASCADE
);

CREATE TABLE Address (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    door_no INT,
    street VARCHAR(50),
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE Loan_list (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    loan_type VARCHAR(50),
    max_period INT, 
    interest_rate DECIMAL(5,2),
    max_amount DECIMAL(15,2),
    Description TEXT
);

CREATE TABLE LoanApplications (
    loanAppId INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    loan_id INT,
    interest_rate DECIMAL(5,2),
    start_date DATE,
    loan_Period INT,
    loan_amount DECIMAL(15,2),
    EMI_amount DECIMAL(15,2),
    total_Interest DECIMAL(15,2),
    remaining_Interest DECIMAL(15,2),
    remaining_principal DECIMAL(15,2),
    status VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (loan_id) REFERENCES Loan_list(loan_id) ON DELETE CASCADE
);

CREATE TABLE Loan_repayment (
    loan_payment_id INT AUTO_INCREMENT PRIMARY KEY,
    loanAppId INT,
    EMI_amount DECIMAL(15,2),
    interest_paid DECIMAL(15,2),
    principal_paid DECIMAL(15,2),
    penalty_paid DECIMAL(15,2),
    date DATE,
    status VARCHAR(20),
    FOREIGN KEY (loanAppId) REFERENCES LoanApplications(loanAppId) ON DELETE CASCADE
);

CREATE TABLE Payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(15,2),
    payment_date DATE,
    loan_payment_id INT,
    FOREIGN KEY (loan_payment_id) REFERENCES Loan_repayment(loan_payment_id) ON DELETE CASCADE
);
