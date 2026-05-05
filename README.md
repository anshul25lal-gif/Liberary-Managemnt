# Library Management System

A complete, production-ready Library Management System built as a college project using **Spring Boot 3.2**, **MySQL**, **Thymeleaf**, and **Bootstrap 5**.

---

## Features

### Admin (Librarian)
- Dashboard with live statistics (books, members, issues today, overdue, fines)
- Full book management — Add / Edit / Delete with validation
- Member management — View, Block/Unblock, Delete
- Issue books to members with business rule enforcement
- Process returns with automatic fine calculation (₹5/day overdue)
- View and manage all transactions, mark fines as paid

### Member (Student)
- Self-registration and login
- Personal dashboard with borrowed books and due date alerts
- Full borrowing history with fine status
- Browse and search the book catalog (filter by category, availability, sort)
- Edit profile details

---

## Technology Stack

| Layer       | Technology                             |
|-------------|----------------------------------------|
| Language    | Java 17                                |
| Framework   | Spring Boot 3.2                        |
| Build       | Maven                                  |
| Database    | MySQL 8                                |
| ORM         | Spring Data JPA (Hibernate)            |
| Security    | Spring Security 6 + BCrypt             |
| Frontend    | Thymeleaf + Bootstrap 5                |
| Validation  | Jakarta Bean Validation                |
| Dev Tools   | Spring Boot DevTools, Lombok           |

---

## Prerequisites

- JDK 17+
- MySQL 8+
- Maven 3.8+

---

## Setup Instructions

### 1. Clone / download the project

```bash
cd "LIberary management systerm"
```

### 2. Create the MySQL database

```sql
CREATE DATABASE library_db;
```

### 3. Configure database credentials

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 4. Build and run

```bash
mvn clean install
mvn spring-boot:run
```

The application starts at **http://localhost:8080**

On first run, `data.sql` seeds the admin user and 15 sample books automatically.

---

## Default Credentials

| Role   | Username | Password  |
|--------|----------|-----------|
| Admin  | `admin`  | `admin123` |
| Member | Register at `/register` | — |

---

## Business Rules

| Rule | Detail |
|------|--------|
| Borrow limit | 3 books per member |
| Loan period | 14 days |
| Fine | ₹5 per overdue day |
| Auto-block | Member blocked if unpaid fines > ₹100 |
| Book delete | Blocked if active issues exist |

---

## Project Structure

```
src/main/java/com/college/library/
├── config/          # Security & Web configuration
├── controller/      # MVC controllers
├── dto/             # Data Transfer Objects
├── exception/       # Global exception handler
├── model/           # JPA entities + enums
├── repository/      # Spring Data JPA repositories
└── service/         # Business logic layer

src/main/resources/
├── templates/       # Thymeleaf HTML templates
│   ├── admin/       # Admin pages
│   └── member/      # Member pages
├── static/          # CSS, JS, images
├── application.properties
└── data.sql         # Seed data
```

---

## Screenshots

> _(Add screenshots here after running the application)_

---

## Future Enhancements

- Email notifications for overdue books
- PDF report generation for transaction history
- Chart.js analytics on admin dashboard
- Book cover image upload
- Dark mode toggle
- Export to Excel

---

## Author

**Student Name:** _(Your Name)_  
**College:** _(Your College Name)_  
**Course:** _(Your Course / Department)_  
**Year:** 2024–25
