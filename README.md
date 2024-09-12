# Blog Backend Projekt

## Überblick

Dieses Projekt implementiert ein Blog-Backend, das mit Quarkus entwickelt wurde. Es ermöglicht das Verwalten von Blog-Beiträgen, einschliesslich der Erstellung, Abruf, Aktualisierung und Löschung von Blogs. Darüber hinaus können Benutzer Kommentare und Likes hinzufügen, was die Interaktion mit Blogposts fördert. Das Backend bietet eine RESTful API, die auf eine einfache und skalierbare Architektur setzt.

## Features

- **Blog-Verwaltung**: Erstellen, Abrufen, Aktualisieren und Löschen von Blog-Beiträgen.
- **Kommentar-Verwaltung**: Kommentare zu Blog-Beiträgen hinzufügen.
- **Likes-Verwaltung**: Benutzer können Blog-Beiträge liken und unliken.
- **Filterung**: Blog-Beiträge nach Titel filtern.

## Projektstruktur

- **`pom.xml`**: Maven-Projektdatei, die Abhängigkeiten und Build-Konfiguration definiert.
- **`src/main/java`**: Enthält die Hauptlogik der Anwendung.
- **`src/test/java`**: Enthält die Testfälle für die Kernlogik.
- **`target`**: Build-Ordner, der die kompilierten Artefakte enthält.

## Hauptkomponenten

### BlogResource (REST-Endpunkte)
Die REST-Endpunkte für die Verwaltung der Blogs und Interaktionen:

- **GET /blogs**: Abrufen aller Blogs oder Filtern nach Titel.
- **POST /blogs**: Erstellen eines neuen Blogs.
- **PUT /blogs/{id}**: Aktualisieren eines bestehenden Blogs.
- **DELETE /blogs/{id}**: Löschen eines Blogs.
- **POST /blogs/{id}/comments**: Kommentar zu einem Blog hinzufügen.
- **POST /blogs/{id}/likes**: Like zu einem Blog hinzufügen.
- **DELETE /likes/{id}**: Like von einem Blog entfernen.

### BlogService (Geschäftslogik)
Die Geschäftslogik, die die Interaktionen mit den Blogdaten verarbeitet:

- **getBlogs()**: Abrufen aller Blogs.
- **addBlog(Blog blog)**: Hinzufügen eines neuen Blogs.
- **deleteBlog(Long blogId)**: Löschen eines Blogs.
- **updateBlog(Blog blog)**: Aktualisieren eines bestehenden Blogs.
- **addCommentToBlog(Long blogId, Comment comment)**: Hinzufügen eines Kommentars zu einem Blog.
- **addLikeToBlog(Long blogId, BlogLike like)**: Hinzufügen eines Likes zu einem Blog.
- **removeLikeFromBlog(Long likeId)**: Entfernen eines Likes.

### Blog (Entitätsklasse)
Das Blog-Datenmodell, das in der Datenbank gespeichert wird:

- **id**: Eindeutiger Bezeichner des Blogs.
- **title**: Titel des Blogs.
- **content**: Inhalt des Blogs.
- **comments**: Liste der Kommentare zu einem Blog.
- **likes**: Liste der Likes zu einem Blog.

### BlogLike (Entitätsklasse)
Das Like-Datenmodell, das einen Like für einen Blog-Eintrag darstellt:

- **id**: Eindeutiger Bezeichner des Likes.
- **user**: Benutzer, der den Blog-Eintrag geliked hat.

## API-Dokumentation

Die vollständige API-Dokumentation ist über Swagger UI erreichbar:

- **Swagger UI**: [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)
- **OpenAPI-Dokumentation**: [http://localhost:8080/openapi](http://localhost:8080/openapi)

Beispiele für Anfragen:
```bash
# Abrufen aller Blogs
curl -X GET "http://localhost:8080/blogs" -H "accept: application/json"

# Hinzufügen eines Kommentars zu einem Blog
curl -X POST "http://localhost:8080/blogs/1/comments" -H "Content-Type: application/json" -d '{"text":"Grossartiger Beitrag!"}'
```

## Installation und Ausführung

### Voraussetzungen
- Java 17
- Maven

### Projekt klonen und starten
```sh
git clone <repository-url>
cd blog-backend
./mvnw quarkus:dev
```

### Zugriff auf die Anwendung
- **API-Endpunkte**: [http://localhost:8080/blogs](http://localhost:8080/blogs)
- **Quarkus Dev UI**: [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)

## Tests

Führe die Tests mit folgendem Befehl aus:
```sh
./mvnw test
```
