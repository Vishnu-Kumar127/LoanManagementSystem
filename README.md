# 💸 Loan Management System

This is a Java Servlet-based Loan Management System that allows users and admins to manage loan applications, payments, and user information. The API can be tested using Postman for quick integration and validation.

---

## 📦 Tech Stack

- **Java Servlet**
- **MySQL**
- **JDBC**
- **Tomcat**
- **Postman** (for API testing)

---

## 🧪 Testing with Postman

All endpoints are prefixed with:  
```
http://localhost:8080/LoanManagementSystem/api
```

### 🔐 Authentication APIs

#### ✅ User Login
```http
POST /login
```
**Body (x-www-form-urlencoded):**
```
username: user1
password: password123
```

#### ✅ Admin Login
```http
POST /adminLogin
```
**Body (x-www-form-urlencoded):**
```
username: admin
password: adminpass
```

---

### 👤 User Management

#### 👥 Create User
```http
POST /createUser
```
**Body (x-www-form-urlencoded):**
- name
- username
- password
- address
- phone

#### 👨‍💼 Create Admin
```http
POST /createAdmin
```
**Body (x-www-form-urlencoded):**
- name
- username
- password

---

### 💰 Loan Application

#### 📝 Apply for Loan
```http
POST /applyLoan
```
**Body (x-www-form-urlencoded):**
- userId
- loanId
- amount

#### 📄 Get Loans by User
```http
GET /getLoansByUser
```
**Headers:**
- Cookie (must contain a session with userId)

---

### 💳 Payment APIs

#### 💵 Make a Payment
```http
POST /makePayment
```
**Body (x-www-form-urlencoded):**
- loanpaymentid
- amount

#### 📊 Get Payments by Loan
```http
GET /getPaymentsByLoan?loanAppId=1
```

#### 💸 Get Total Paid by Loan
```http
GET /getTotalPaidAmount?loanAppId=1
```

---

### 🧾 Repayment Details

#### 📆 Get Loan Repayment Schedule
```http
GET /getLoanRepayment?loanAppId=1
```

---

## 🚀 Running the Project

1. Import into IntelliJ or Eclipse.
2. Set up Tomcat server and MySQL database.
3. Update your DB credentials in `Db_Connector.java`.
4. Deploy the WAR and run `http://localhost:8080/LoanManagementSystem`.

---

## 📬 Postman Collection

You can create a Postman collection with all the above endpoints and test them using form-data or raw JSON.

---

## 🙋‍♂️ Author

**Vishnu Kumar M.J.**  
GitHub: [Vishnu-Kumar127](https://github.com/Vishnu-Kumar127)
