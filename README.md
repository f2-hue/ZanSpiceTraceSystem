# Zanzibar Spice Traceability System

## Developers

- TARKIA OTHMAN - Frontend Developer
- FAHAD MOHAMEDI IDRISA - Backend Developer

Zanzibar Spice is a Spring Boot web application designed to support spice supply-chain traceability for farmers, traders, and administrators in Zanzibar. The system allows farmers to register farms and harvest batches, traders to update batch status as goods move through the supply chain, and administrators to manage users, review batches, and flag suspicious records.

A public verification page also lets consumers and partners scan or search a batch reference code to view the history of a spice batch.

## Overview

This project is built as a role-based web application with:

- Farmer workflows for registering farms and creating harvest batches
- Trader workflows for searching batches and updating processing status
- Admin workflows for approving or suspending users and monitoring batches
- Public batch verification via reference code or QR code lookup
- QR code generation for each batch for traceability and sharing

## Main Features

### Farmer features
- Register a farm with district, region, land size, and spice types
- Create harvest batches linked to a registered farm
- Generate a unique reference code for each batch
- Generate and store a QR code for batch traceability
- View batch details and associated events

### Trader features
- View all available batches on the trader dashboard
- Search a batch by reference code
- Update batch status such as IN_PROCESSING, PACKAGED, or DISPATCHED
- View the full event history of a batch

### Admin features
- View dashboard statistics for users and batches
- Approve, suspend, activate, or delete user accounts
- Review all batches in the system
- Flag or unflag batches for review
- View user management and batch management screens

### Public verification
- Search a batch by reference code via the public verification page
- Open a direct verification URL using the generated QR code
- See the overall lifecycle history of the batch

## Technology Stack

- Java 17
- Spring Boot 4.1.0
- Spring MVC
- Thymeleaf templates
- Spring Security
- Spring Data JPA
- PostgreSQL
- Lombok
- ZXing (QR code generation)
- Maven

## Project Structure

```text
src/
  main/
    java/
      chain/trace/zanzibarspice/
        controller/
        entity/
        repository/
        security/
        service/
    resources/
      application.properties
      static/
      templates/
  test/
    java/
```

## Prerequisites

Make sure the following are installed:

- Java 17 or later
- Maven
- PostgreSQL database server

## Configuration

The application configuration is stored in:

- [src/main/resources/application.properties](src/main/resources/application.properties)

Default settings include:

- Database URL: `jdbc:postgresql://localhost:5433/zanzibarspice_db`
- Database username: `postgres`
- Database password: `postgres123`
- Server port: `8080`
- QR code upload directory: `uploads/qrcodes`

Before running the app, make sure the PostgreSQL database exists and the credentials are valid.

## Running the Application

From the project root, run:

```bash
./mvnw spring-boot:run
```

Then open the app in your browser:

- Home page: http://localhost:8080/
- Login: http://localhost:8080/login
- Register: http://localhost:8080/register
- Verification page: http://localhost:8080/verify

## Default Role-Based Routes

- `/` - Public landing page
- `/login` - Login page
- `/register` - User registration page
- `/verify` or `/verify/{referenceCode}` - Public batch verification
- `/farmer/dashboard` - Farmer dashboard
- `/trader/dashboard` - Trader dashboard
- `/admin/dashboard` - Admin dashboard

## Notes

- QR code images are written to the `uploads/qrcodes` folder.
- Static uploaded content is served from the configured upload path.
- The system uses Spring Security with role-based access control for FARMER, TRADER, and ADMIN users.

## Suggested Next Improvements

- Add unit and integration tests
- Add email notifications for batch status updates
- Add more detailed audit logs and history views
- Improve UI/UX for dashboards and forms
- Add export features for batch reports
