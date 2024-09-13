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

## Hauptkomponenten

### BlogResource (REST-Endpunkte)
Die **REST-Endpunkte** ermöglichen die Verwaltung der Blog-Einträge, einschliesslich Kommentaren und Likes. Hier sind die wichtigsten Endpunkte:

- **GET /blogs**: Abrufen aller Blogs oder Filtern nach Titel (mit optionaler Paginierung).
- **POST /blogs**: Erstellen eines neuen Blogs.
- **PUT /blogs/{id}**: Aktualisieren eines bestehenden Blogs anhand der ID.
- **PATCH /blogs/{id}**: Teilweises Aktualisieren eines Blogs (z. B. nur Titel oder Inhalt).
- **DELETE /blogs/{id}**: Löschen eines Blogs anhand der ID.
- **POST /blogs/{id}/comments**: Hinzufügen eines Kommentars zu einem Blog.
- **POST /blogs/{id}/likes**: Hinzufügen eines Likes zu einem Blog.
- **DELETE /likes/{id}**: Entfernen eines Likes von einem Blog anhand der Like-ID.

### BlogService (Geschäftslogik)
Die **Geschäftslogik** des Blog-Systems ist in der `BlogService`-Klasse implementiert. Sie übernimmt die Verarbeitung der Blog-Daten, einschliesslich CRUD-Operationen (Create, Read, Update, Delete) sowie der Verwaltung von Kommentaren und Likes. Folgende Methoden sind enthalten:

- **getBlogs()**: Gibt eine Liste aller Blogs zurück.
- **getBlogsByTitle(String title)**: Filtert Blogs nach dem Titel.
- **addBlog(Blog blog)**: Fügt einen neuen Blog hinzu.
- **deleteBlog(Long blogId)**: Löscht einen Blog anhand der ID.
- **updateBlog(Blog blog)**: Aktualisiert einen bestehenden Blog.
- **addCommentToBlog(Long blogId, Comment comment)**: Fügt einem Blog einen neuen Kommentar hinzu.
- **addLikeToBlog(Long blogId, BlogLike like)**: Fügt einem Blog einen Like hinzu.
- **removeLikeFromBlog(Long likeId)**: Entfernt einen Like anhand der Like-ID.

### Blog (Entitätsklasse)
Die **Blog-Entitätsklasse** repräsentiert das Blog-Modell, das in der Datenbank gespeichert wird. Sie besteht aus den folgenden Attributen und Methoden:

- **id**: Der eindeutige Bezeichner des Blogs.
- **title**: Der Titel des Blogs (Validierung: zwischen 2 und 100 Zeichen).
- **content**: Der Inhalt des Blogs (Validierung: mindestens 10 Zeichen).
- **comments**: Liste der zugeordneten Kommentare.
- **likes**: Liste der zugeordneten Likes.

**Methoden**:

- **addComment(Comment comment)**: Fügt einen Kommentar hinzu und setzt die Beziehung zwischen Blog und Kommentar.
- **removeComment(Comment comment)**: Entfernt einen Kommentar und setzt die Beziehung auf `null`.
- **addLike(BlogLike like)**: Fügt einen Like hinzu und setzt die Beziehung zwischen Blog und Like.
- **removeLike(BlogLike like)**: Entfernt einen Like und setzt die Beziehung auf `null`.

### BlogLike (Entitätsklasse)
Die **BlogLike-Entität** repräsentiert die Likes für Blog-Einträge.

- **id**: Der eindeutige Bezeichner des Likes.
- **user**: Der Benutzername des Nutzers, der den Blog geliked hat.
- **blog**: Das Blog, dem der Like zugeordnet ist.

**Methoden**:

- **setBlog(Blog blog)**: Setzt die Beziehung zum Blog.
- **getBlog()**: Gibt das Blog zurück, dem der Like zugeordnet ist.


### Comment (Entitätsklasse)
Die **Comment-Entität** repräsentiert die Kommentare zu den Blog-Einträgen.

- **id**: Der eindeutige Bezeichner des Kommentars.
- **text**: Der Text des Kommentars (Validierung: zwischen 3 und 500 Zeichen).
- **blog**: Das Blog, dem der Kommentar zugeordnet ist.

**Methoden**:

- **setBlog(Blog blog)**: Setzt die Beziehung zum Blog.
- **getBlog()**: Gibt das Blog zurück, dem der Kommentar zugeordnet ist.

## API-Dokumentation
Die vollständige API-Dokumentation ist über die **Swagger UI** verfügbar, die durch die Integration von **OpenAPI** bereitgestellt wird. Sie bietet eine interaktive Schnittstelle zum Testen und Erkunden der API-Endpunkte.

- **Swagger UI**: [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)
- **OpenAPI-Spezifikation**: [http://localhost:8080/openapi](http://localhost:8080/openapi)

Die Dokumentation umfasst alle Endpunkte, Parameter, Anfragen und Antworten, einschliesslich Beispielen und Fehlercodes.

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
git clone https://github.com/21yanick/VerteilteSysteme.git
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
