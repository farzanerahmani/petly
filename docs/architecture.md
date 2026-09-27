# Petly Backend Architecture

## Overview

Petly backend is a modular monolith built with Spring Boot.

The backend exposes REST APIs consumed by the Flutter mobile application.

## Package Structure

```text
com.petly
├── BackendApplication.java
│
├── common
│   ├── controller
│   ├── exception
│   └── response
│
└── pet
    ├── controller
    ├── service
    ├── repository
    └── entity