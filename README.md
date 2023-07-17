
# Hotel Asset Reservation API

- [Hotel Reservation API](#hotel-asset-reservation-api)
    * [Overview](#overview)
- [Explore REST APIs](#explore-rest-apis)
- [Database](#database)
- [Testing](#testing)
- [Tech Stack](#tech-stack)

## Overview

- This project is a Hotel Asset Reservation `RESTful API` designed to **create reservations.**
- The user can create a reservation, search all reservations within the database, update, reservations and delete reservations.
- Necessary validation is incorporated within the API that prevents illogical operations from occurring such as making an overlapping reservation.

# Running the API server

- Build a Docker image with the following command:

```bash
docker build -t com.lfokazi/reservationapp .
```

- Run the dockerized application with

```bash
docker run -p 8080:8080 com.lfokazi/reservationapp
```

# Explore REST APIs

##  Reservation

| Method | Endpoint           | Description                                                               |
|--------|--------------------|---------------------------------------------------------------------------|
| POST   | /reservations      | Creates a reservation for a given restaurant asset                        |
| POST   | /reservations/{id} | Updates an existing reservation                                           |
| GET    | /reservations      | Get all exsiting reservations. This API supports filtering and pagination |
| GET    | /reservations/{id} | Get an exsiting user specified Hotel reservation                          |
| DELETE | /reservations/{id} | Delete an existing user specified reservation                             |



### TODO: add API call examples

# Database
- The database structure contains two tables, an `assets` table that contains the asset object. An asset is of type basic room or presidential suite or conference room or restaurant. It can be extended to include other reserve(able) hotel assets.
- A `reservations` table that contains reservations objects and is associated with the `assets` table through the `asset table's Id` by storing it as a foreign key in its "asset Id" value.

# Testing
- Tests for this project incorporated both JUNit and Integration tests.
- I used Mockito in several unit tests and H2 in memory databases for the integration tests.
- More elaborate descriptions of the tests and their functionalities can be found in the test folder within this project.


# Tech Stack

- API Creation:
    - Java
    - H2
    - SpringBoot
    - Hibernate
    - JPA
    - Lombok
- Testing:
    - JUnit 5
    - Mockito
    - H2
- User Input Testing:
    - Postman