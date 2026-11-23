# V-Booth Backend

REST API for **V-Booth**, a digital guestbook and photo booth platform for events such as weddings, graduations, and gatherings. Guests scan a QR code, take or upload a photo, decorate it with a collage frame, and leave a voice message for the host. No login or app install is required for guests.

> Frontend repository: [V-Booth-FE](https://github.com/ibrahimahmads/V-Booth-FE)

## Background

V-Booth started as a real project: I built it to help a family member's wedding. It was deployed to production and used by guests at the event. The backend is currently offline because the free hosting trial has ended, so the live demo is not available.

## Features

**Host (User role)**
- Sign in with Google
- Pay to create an event guestbook (Midtrans)
- Edit event details and set a custom URL slug
- Generate a QR code for guests
- Configure the collage frame used by guests
- View the gallery and download guest photos

**Guest (no login)**
- Open the event gallery through the slug link or QR code
- Take a photo at the venue or upload one from the phone gallery
- Edit the photo with the collage frame set by the host
- Record a voice message for the host
- See submitted messages in the gallery and download the edited photo
- Edit their own message by repeating the flow (guests cannot delete a message once sent)

**Admin**
- Standard administration tasks (user, event, and transaction management)

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4 |
| Build tool | Maven (Maven Wrapper included) |
| Authentication | JWT stored in an HTTP-only cookie |
| Social login | Google OAuth |
| Payment | Midtrans |
| Database | PostgreSQL |
| Media storage | Cloudinary |
| Containerization | Docker |

## Roles

| Role | Access |
| --- | --- |
| `USER` | Event owner. Authenticated, pays for and manages events. |
| `GUEST` | Event visitor. No login, access through the event slug. |
| `ADMIN` | Platform administrator. |

## Architecture Overview

```
React SPA (Vercel)  <--HTTPS + HTTP-only cookie-->  Spring Boot API
                                                      |-- Google OAuth   (sign in)
                                                      |-- Midtrans       (event payment)
                                                      |-- Cloudinary     (photos and audio)
                                                      |-- PostgreSQL
```

Security notes:
- JWT is stored in an HTTP-only cookie, so it cannot be read by JavaScript, which reduces token theft through XSS.
- Guests access only the public routes of a specific event slug.

## Getting Started

### Prerequisites
- JDK 21
- PostgreSQL (a local instance or a hosted one)
- Google OAuth client credentials
- Midtrans account (sandbox is fine for development)
- Cloudinary account

### Configuration

Secrets are never committed. Provide them through environment variables or a local, git-ignored configuration file. Set the values your `application` configuration expects, for example:

```
# PostgreSQL connection (URL, username, password)
# JWT secret and expiration
# Google OAuth client ID / secret
# Midtrans server key and client key (sandbox or production)
# Cloudinary cloud name, API key, API secret
# Allowed frontend origin (CORS)
```

<!-- TODO: replace the list above with the exact property names from application.properties / application.yml -->

### Run locally

```bash
git clone https://github.com/ibrahimahmads/V-Booth-BE.git
cd V-Booth-BE
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`.

### Build

```bash
./mvnw clean package
```

### Docker

The Dockerfile in this repository is named `DockerFile`, so pass it explicitly:

```bash
docker build -f DockerFile -t v-booth-be .
docker run -p 8080:8080 --env-file .env v-booth-be
```

<!-- TODO: confirm the exposed port -->

## Project Structure

```
.
├── .mvn/wrapper     # Maven Wrapper
├── src              # Application source
├── DockerFile       # Container build
├── mvnw / mvnw.cmd  # Maven Wrapper scripts
└── pom.xml          # Dependencies and build config
```

## Status

- Built and deployed for a real wedding event
- Backend hosting is currently inactive (free trial ended)
- Planned: redeploy on a free tier and add a demo mode

## Author

[@ibrahimahmads](https://github.com/ibrahimahmads)
