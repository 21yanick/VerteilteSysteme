# Blog Backend Projekt

## Überblick

Dieses Projekt implementiert ein Blog-Backend, das mit Quarkus entwickelt wurde. Es ermöglicht das Verwalten von Blog-Beiträgen, einschließlich der Erstellung, des Abrufs, der Aktualisierung und der Löschung von Blogs. Benutzer können Kommentare und Likes hinzufügen, um die Interaktion mit Blogposts zu fördern. Das Backend bietet eine RESTful API auf einer skalierbaren Architektur, die durch Keycloak abgesichert ist.

**Hinweis:** Der Token wird erfolgreich abgerufen, aber es gibt Probleme bei der Verarbeitung des Tokens in der API.

## Aktueller Status

- **Keycloak** ist erfolgreich konfiguriert und generiert Zugriffstokens.
- **Quarkus API** und die MySQL-Datenbank funktionieren. Datenbankverbindungen sind erfolgreich.
- **Tokenverarbeitung**: Aktuell wird der Zugriffstoken zwar erfolgreich abgerufen, kann jedoch nicht korrekt von der API verarbeitet werden. Es besteht ein Problem bei der Überprüfung des Tokens durch Quarkus/OIDC.

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
- **DELETE /blogs/likes/{likeId}**: Entfernen eines Likes von einem Blog anhand der Like-ID.
- **GET /blogs/{id}**: Hole einen Blog anhand der ID.
- **PUT /blogs/{id}**: Aktualisieren eines bestehenden Blogs anhand der ID.
- **DELETE /blogs/{id}**: Löschen eines Blogs anhand der ID.
- **PATCH /blogs/{id}**: Teilweises Aktualisieren eines Blogs (z. B. nur Titel oder Inhalt).
- **POST /blogs/{id}/comments**: Hinzufügen eines Kommentars zu einem Blog.
- **POST /blogs/{id}/likes**: Hinzufügen eines Likes zu einem Blog.

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

- **Swagger UI**: [http://localhost:8081/swagger-ui](http://localhost:8081/swagger-ui)
- **OpenAPI-Spezifikation**: [http://localhost:8081/openapi](http://localhost:8081/openapi)

Die Dokumentation umfasst alle Endpunkte, Parameter, Anfragen und Antworten, einschliesslich Beispielen und Fehlercodes.

Beispiele für Anfragen:
```bash
# Abrufen aller Blogs
curl -X GET "http://localhost:8081/blogs" -H "accept: application/json"

# Hinzufügen eines Kommentars zu einem Blog
curl -X POST "http://localhost:8081/blogs/1/comments" -H "Content-Type: application/json" -d '{"text":"Grossartiger Beitrag!"}'
```

**Achtung:** Die Token-Generierung mit Keycloak funktioniert, jedoch gibt es derzeit Probleme bei der Verarbeitung der Tokens durch die API.

## Schritte zur Installation und Ausführung

### 1. **Repository klonen**

Beginne damit, das GitHub-Repository zu klonen:

```bash
git clone https://github.com/21yanick/VerteilteSysteme.git
cd VerteilteSysteme
```

### 2. **MySQL-Datenbank starten**

Starte die MySQL-Datenbank mit dem folgenden Befehl:

```bash
docker run \
  --name blog-mysql \
  --network blog-nw \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=blogdb \
  -e MYSQL_USER=dbuser \
  -e MYSQL_PASSWORD=dbuser \
  -p 3306:3306 \
  mysql:8.0
```

### 3. **Keycloak-Server starten und Realm importieren**

Starte den Keycloak-Server und importiere das Realm aus dem Export-Ordner:

```bash
docker run \
  --name blog-keycloak \
  --network blog-nw \
  -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -v ${PWD}/export:/opt/keycloak/data/import \
  quay.io/keycloak/keycloak:latest \
  start-dev --import-realm
```

### 4. **Quarkus-Anwendung bauen und starten**

Baue die Quarkus-Anwendung und starte sie in einem Docker-Container:

```bash
./mvnw clean package

docker build -f src/main/docker/Dockerfile.jvm -t quarkus/blog-backend .

docker run \
  --name blog-quarkus-app \
  --network blog-nw \
  -p 8081:8081 \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:mysql://blog-mysql:3306/blogdb \
  -e QUARKUS_DATASOURCE_USERNAME=dbuser \
  -e QUARKUS_DATASOURCE_PASSWORD=dbuser \
  -e QUARKUS_HTTP_PORT=8081 \
  -e QUARKUS_OIDC_AUTH_SERVER_URL=http://blog-keycloak:8080/realms/blog-backend \
  quarkus/blog-backend
```

### 5. **Token von Keycloak holen**

Du kannst den Access-Token von Keycloak mit folgendem **HTTPie**-Befehl abrufen:

```bash
http --form POST http://localhost:8080/realms/blog-backend/protocol/openid-connect/token \
    grant_type=password \
    client_id=blog-client \
    username=testuser \
    password=123456 \
    client_secret=dn0uRMYmeN2ID2ieF7FC0nG8ixihDK2W
```



### Zugriff auf die Anwendung
- **API-Endpunkte**: [http://localhost:8080/blogs](http://localhost:8080/blogs)
- **Quarkus Dev UI**: [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)

## Aktueller Status

- **Keycloak** ist erfolgreich konfiguriert und generiert Zugriffstokens.
- **Quarkus API** und die MySQL-Datenbank funktionieren. Datenbankverbindungen sind erfolgreich.
- **Tokenverarbeitung**: Aktuell wird der Zugriffstoken zwar erfolgreich abgerufen, kann jedoch nicht korrekt von der API verarbeitet werden. Es besteht ein Problem bei der Überprüfung des Tokens durch Quarkus/OIDC.

## Beispielhafte API-Aufrufe (Funktioniert nicht richtig)

### 1. **Abrufen aller Blogs** (mit Token)

```bash
http GET http://localhost:8081/blogs \
    Authorization:"Bearer <access_token>"
```

### 2. **Einen Kommentar zu einem Blog hinzufügen**

```bash
http POST http://localhost:8081/blogs/1/comments \
    Authorization:"Bearer <access_token>" \
    text="Toller Beitrag!"
```