# MusicAPI

![Licence](https://img.shields.io/github/license/Ileriayo/markdown-badges?style=for-the-badge)
<br/>
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

This project is an API Test built using **Java, Java Spring, Swagger UI to provide interactive API documentation, MySQL as the database, JPA and Hibernate to manage the database migrations and uses Spring Security for password cryptography**.

The API simulates the functionality of a cloud music streaming platform like [Spotify](https://open.spotify.com/). Users can register themselves into app, and logged-in users can create playlists with songs already added in the system.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/PedroSmaxY/MusicAPI.git
```

2. Install dependencies with Maven
```bash
cd MusicAPI
mvn install
```

3. Install [MySQL](https://dev.mysql.com/downloads/workbench/)

## Usage

1. Configure `application.properties` file should include the following settings to configure the Database integration in `src/main/resources`.
```properties
# Configure the following lines in application.properties file:
spring.datasource.username=DB_USERNAME
spring.datasource.password=DB_PASSWORD
```

2. Start the application with Maven:
```bash
mvn spring-boot:run
```

3. the API will be accessible at `https://localhost:8080`.

## API Documentation
To view the API documentation, start the application and navigate to the following URL in your browser: `http://localhost:8080/swagger-ui.html`.

## API Endpoints
The API provides the following endpoints:

### Users

```markdown
GET /users/{id} - Retrieve user details by their ID.

GET /users/username/{username} - Retrieve user details by their username.

GET /users/search - Search for users based on a query string.

POST /users/login - Authenticate a user and log them in.

POST /users/register - Register a new user.

PUT /users/{id} - Update user details by their ID.

PUT /users/username/{username} - Update user details by their username.

DELETE /users/{id} - Delete user by their ID.

DELETE /users/username/{username} - Delete user by their username.
```

### Songs

```markdown
GET /songs - Retrieve a pageable list with all songs.

GET /songs/search - Search for songs based on a query string.

GET /songs - Retrieve a pageable list with all songs.

GET /songs/{title} - Retrieve a list of songs matching the given title.

GET /songs/download/{id} - Download the audio file of a specific song by its ID.

GET /songs/stream/{id} - Stream the audio file of a specific song by its ID.

POST /songs/upload - Upload a new song along with its metadata.

PUT /songs/update/{id} - Update the details of an existing song.

DELETE /songs/delete/{id} - Delete a specific song by its ID.
```

### Playlists

```markdown
GET /playlists/{playlistId} - Retrieve details of a playlist by its ID.

GET /playlists - Retrieve a paginated list of playlists.

GET /playlists/search - Search for playlists based on a query string.

POST /playlists/create - Create a new playlist.

POST /playlists/{playlistId}/add-songs - Add songs to an existing playlist.

POST /playlists/{playlistId}/remove-songs - Remove songs from an existing playlist.

PUT /playlists/update/{playlistId} - Update a playlist's title and/or image.

DELETE /playlists/{playlistsId} - Delete a playlist by its ID.
```

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request to the repository.

When contributing to this project, please follow the existing code style, [commit conventions](https://www.conventionalcommits.org/en/v1.0.0/), and submit your changes in a separate branch.