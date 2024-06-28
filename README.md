# Blog Backend Projekt

## Überblick

Dies ist ein einfaches Blog-Backend-Projekt, das mit Quarkus entwickelt wurde. Es ermöglicht das Erstellen, Abrufen, Aktualisieren und Löschen von Blog-Einträgen. Das Projekt demonstriert die Verwendung von Quarkus für die Entwicklung einer RESTful API mit Dependency Injection, Datenpersistenz und Transaktionsmanagement.

## Projektstruktur

- **`pom.xml`**: Definiert die Abhängigkeiten und Konfigurationen für das Projekt.
- **`src/main/java`**: Enthält den Quellcode der Anwendung.
- **`src/test/java`**: Enthält die Testfälle.
- **`target`**: Enthält die kompilierten Dateien und Artefakte.

## Hauptkomponenten

### BlogResource (REST-Endpunkte)
Definiert die REST-Endpunkte für das Blog-System.

- **GET /blogs**: Ruft alle Blogs ab.
- **POST /blogs**: Erstellt einen neuen Blog.
- **PUT /blogs/{id}**: Aktualisiert einen bestehenden Blog.
- **DELETE /blogs/{id}**: Löscht einen bestehenden Blog.

### BlogService (Geschäftslogik)
Enthält die Geschäftslogik für das Blog-System.

- **getBlogs()**: Ruft alle Blogs ab.
- **addBlog(Blog blog)**: Fügt einen neuen Blog hinzu.
- **deleteBlog(Long blogId)**: Löscht einen Blog anhand der ID.
- **updateBlog(Blog blog)**: Aktualisiert einen bestehenden Blog.
- **getBlogById(Long id)**: Ruft einen Blog anhand der ID ab.

### BlogRepository (Datenhaltung)
Verwaltet die Datenhaltung der Blog-Einträge.

- **getBlogs()**: Gibt alle Blogs zurück.
- **addBlog(Blog blog)**: Fügt einen neuen Blog zur Liste hinzu.
- **deleteBlog(Blog blog)**: Entfernt einen Blog aus der Liste.

### Blog (Entitätsklasse)
Repräsentiert das Blog-Datenmodell.

- **Felder**:
  - `id`: Primärschlüssel, automatisch generiert.
  - `title`: Titel des Blogs.
  - `content`: Inhalt des Blogs.
  - `comments`: Liste der Kommentare zu diesem Blog.
  - `likes`: Liste der Likes zu diesem Blog.

- **Methoden**:
  - `addComment(Comment comment)`: Fügt einen Kommentar hinzu und setzt die Beziehung zum Blog.
  - `removeComment(Comment comment)`: Entfernt einen Kommentar und setzt die Beziehung zum Blog auf `null`.
  - `addLike(BlogLike like)`: Fügt ein Like hinzu und setzt die Beziehung zum Blog.
  - `removeLike(BlogLike like)`: Entfernt ein Like und setzt die Beziehung zum Blog auf `null`.

## Installation und Ausführung

1. **Voraussetzungen**:
   - Java 17
   - Maven

2. **Projekt klonen**:
   ```sh
   git clone <repository-url>
   cd blog-backend

3. **Projekt starten**:
   ```sh
    ./mvnw quarkus:dev

### Zugriff auf die Anwendung**:

API-Endpunkte: http://localhost:8080/blogs
Quarkus Dev UI: http://localhost:8080/q/dev/

## Tests
Tests befinden sich im Verzeichnis src/test/java. Um die Tests auszuführen, verwende den folgenden Befehl:
   ```sh
    ./mvnw test