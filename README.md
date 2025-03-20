# Blog-Backend mit KI-Integration, Kafka-basierter Validierung und Keycloak-Authentifizierung

Dieses Projekt demonstriert die Integration von KI-Diensten in eine moderne Microservices-Architektur unter Verwendung von Quarkus, Kafka und Keycloak. Es wurde als Teil der Vertiefungsarbeit im Kurs "Verteilte Systeme" entwickelt und setzt LangChain4J sowohl für die Blog-Generierung als auch für die Content-Moderation ein.

## Projektübersicht

Das System besteht aus drei Hauptmodulen:
1. **Blog-Service**: Hauptanwendung für das Blog-Backend mit KI-gestützter Blog-Generierung
2. **Validator-Service**: Separater Service für die Textvalidierung über Kafka
3. **Shared-Models**: Gemeinsam genutzte Modelle für die Kafka-Kommunikation

## Architektur und Technologie-Stack

Die Anwendung nutzt folgende Technologien:
- **Quarkus** als Java-Framework
- **MySQL** als Datenbank
- **Keycloak** für die Authentifizierung und Autorisierung
- **Kafka** für die asynchrone Kommunikation zwischen den Services
- **LangChain4J** für die Integration mit KI-Modellen (OpenAI)
- **Docker** und **Docker Compose** für die Containerisierung

### Blog-Validierungs-Workflow

1. Wenn ein neuer Blog erstellt wird (manuell oder KI-generiert), erhält er den Status `PENDING`
2. Der Blog-Service sendet eine Validierungsanfrage an Kafka
3. Der Validator-Service empfängt die Anfrage und führt die Validierung durch
4. Das Validierungsergebnis wird über Kafka zurückgesendet
5. Der Blog-Status wird auf `APPROVED` oder `REJECTED` aktualisiert

### KI-Integration

Das Projekt integriert LangChain4J mit OpenAI an zwei Stellen:

### 1. Blog-Generator (Blog-Service)
- Automatische Blog-Generierung mit anpassbaren Templates
- Konfigurierbare Parameter wie Zielgruppe, Tonfall und Textlänge
- Verschiedene Template-Typen (Standard, Technisch, Kreativ)

### 2. Content-Moderation (Validator-Service)
- KI-gestützte Prüfung auf unangemessene Inhalte
- Automatische Bewertung der Textqualität
- Detailliertes Feedback bei abgelehnten Blogs

## Vorbereitung

### Voraussetzungen

- Java 17+
- Maven
- Docker und Docker Compose
- OpenAI API-Key (für die KI-Integration)

Um den OpenAI API-Key zu konfigurieren, erstellen Sie eine `.env` Datei im Projektroot mit folgendem Inhalt:
```
OPENAI_API_KEY=Ihr-OpenAI-API-Schlüssel
```

> **Wichtig:** Der OpenAI API-Key wird sowohl vom Blog-Service für die Generierung als auch vom Validator-Service für die Content-Moderation benötigt. Ohne einen gültigen API-Key können beide KI-Funktionen nicht genutzt werden.

## Starten der Anwendung

Es gibt zwei Möglichkeiten, die Anwendung zu starten:

### Option 1: Mit fertigen GHCR-Images (empfohlen für schnelles Testen)

```bash
# Docker-Compose-Konfiguration erstellen
cp docker-compose.ghcr.yml.example docker-compose.yml

# Services starten
docker-compose up -d
```

Diese Variante verwendet die fertigen Container-Images von GitHub Container Registry und benötigt keinen lokalen Build. **Nach dem Start können Sie direkt über das Web-Frontend alle Features testen.**

### Option 2: Mit lokalem Build (empfohlen für Entwicklung)

```bash
# Docker-Compose-Konfiguration erstellen
cp docker-compose.build-local.yml.example docker-compose.yml

# Shared-Models bauen (wichtig!)
cd shared-models
mvn clean install
cd ..

# Blog-Backend bauen
mvn clean package -DskipTests

# Validator-Service bauen
cd validator-service
mvn clean package -DskipTests
cd ..

# Starten aller Services
docker-compose up -d
```

> **Wichtig**: Bei lokalem Build muss das Shared-Models-Modul zuerst gebaut werden, da es von den anderen Modulen benötigt wird.

### Zugriff auf die Services und das Web-Frontend

Die Services sind dann unter folgenden URLs erreichbar:
- **Web-Frontend**: http://localhost:8082
  - **Homepage**: http://localhost:8082/ - Login-Seite mit Zugang zu allen Funktionen
  - **Blog-Generator**: http://localhost:8082/blog-generator.html - KI-gestützte Blog-Erstellung mit allen Parametern
  - **Blog-Übersicht**: http://localhost:8082/blogs.html - Liste aller Blogs mit Filterung nach Status
- **Validator-Service**: http://localhost:8083
- **Keycloak**: http://localhost:8080
- **Kafka**: localhost:9093
- **OpenAPI / Swagger-UI**: http://localhost:8082/swagger-ui

**Hinweis: Das integrierte Web-Frontend ist die empfohlene Methode zum Testen aller Funktionen.** Es bietet eine benutzerfreundliche Oberfläche mit:
- Automatischer Token-Generierung (Login mit alice/alice)
- Vollständiger KI-Blog-Generator mit allen Template-Optionen
- Blog-Übersicht mit Filterung und Paginierung
- Status-Anzeige und Verwaltung aller Blog-Einträge

> **Wichtig:** Es kann nach dem Start einige Minuten dauern (ca. 3-4 Minuten), bis Keycloak vollständig hochgefahren ist und Token abgerufen werden können. Auf der Homepage (http://localhost:8082/) können Sie mit dem Button "Token testen/Templates ansehen" überprüfen, ob das System vollständig betriebsbereit ist. Falls Fehler auftreten (z.B. beim Abrufen der Blogs), klicken Sie auf "Token löschen" und generieren Sie einen neuen Token mit "Anmelden".

## API-Dokumentation

Die vollständige OpenAPI-Dokumentation ist unter http://localhost:8082/swagger-ui verfügbar.

### Wichtigste API-Endpunkte

#### Standard Blog-API
- `GET /blogs` - Alle Blogs abrufen
- `GET /blogs/{id}` - Einzelnen Blog abrufen
- `POST /blogs` - Neuen Blog erstellen
- `PUT /blogs/{id}` - Blog aktualisieren
- `DELETE /blogs/{id}` - Blog löschen

#### KI-gestützte Blog-Generierung
- `GET /blogs/ai/templates` - Verfügbare Templates und Parameter abrufen
- `GET /blogs/ai/test` - Test-Blog generieren (ohne Authentifizierung)
- `POST /blogs/ai` - Blog mit KI generieren (mit Authentifizierung)

#### Blog-Status-API
- `GET /blogs/{id}/status` - Status eines Blogs abfragen
- Status-Werte:
  - `PENDING`: Blog wartet auf Validierung
  - `APPROVED`: Blog wurde validiert und freigegeben
  - `REJECTED`: Blog wurde validiert und abgelehnt

## Kafka-Kommunikation

Die Services kommunizieren über zwei Kafka-Topics:
- `blog-validation-requests`: Anfragen zur Textvalidierung
- `blog-validation-responses`: Antworten mit Validierungsergebnissen

## Feature-Übersicht

- **Blog-Verwaltung**: Erstellen, Abrufen, Aktualisieren und Löschen von Blog-Beiträgen
- **Kommentar-Verwaltung**: Kommentare zu Blog-Beiträgen hinzufügen
- **Likes-Verwaltung**: Benutzer können Blog-Beiträge liken und unliken
- **Filterung**: Blog-Beiträge nach Titel filtern
- **Asynchrone Textvalidierung**: Blog-Inhalte werden asynchron mittels Kafka validiert
- **KI-gestützte Blog-Generierung**: Automatische Erstellung von Blog-Beiträgen mit verschiedenen Parametern:
  - Verschiedene Template-Typen (Standard, Technisch, Kreativ)
  - Konfigurierbare Zielgruppen (allgemein, technisch, fachspezifisch)
  - Einstellbare Tonalität (informativ, unterhaltsam, sachlich)
  - Kontrolle über Länge und Umfang der Inhalte

## Anmeldung und Authentifizierung

Das System ist mit folgenden Benutzern vorkonfiguriert:

1. **alice**
   - Benutzername: `alice`
   - Passwort: `alice`
   - Rollen: `admin`, `user`

2. **bob**
   - Benutzername: `bob`
   - Passwort: `bob`
   - Rollen: normale Benutzerrollen

> **Hinweis**: Falls Probleme mit der Anmeldung auftreten, starten Sie die Anwendung mit `docker-compose down -v && docker-compose up -d`, um alle Volumes zu löschen und das System neu zu initialisieren.

## Token von Keycloak holen

Sie können den Access-Token von Keycloak mit folgendem **curl**-Befehl abrufen:

```bash
curl -X POST http://localhost:8080/realms/blog/protocol/openid-connect/token \
    -d 'grant_type=password' \
    -d 'client_id=backend-service' \
    -d 'username=alice' \
    -d 'password=alice' \
    -d 'client_secret=secret'
```

## Testen der Anwendung

### Methode 1: Über das Web-Frontend (empfohlen)

Die einfachste Methode zum Testen aller Funktionen ist das integrierte Web-Frontend:

1. **Homepage öffnen**: http://localhost:8082/
2. **Anmelden** mit Benutzer `alice` und Passwort `alice` (Klicken Sie auf "Anmelden")
3. **Token testen**: Nach erfolgreicher Anmeldung erscheint der Button "Token testen/Templates ansehen" - klicken Sie darauf, um zu prüfen, ob das System bereit ist
4. **Blog-Generator verwenden**: Klicken Sie auf "Blog-Generator starten", wählen Sie die gewünschten Parameter und erstellen Sie einen KI-generierten Blog
5. **Blog-Übersicht anzeigen**: Klicken Sie auf "Blogs anzeigen", um alle erstellten Blogs zu sehen
6. **Status prüfen**: In der Blog-Übersicht sehen Sie den Status der Blogs, der nach kurzer Zeit von PENDING zu APPROVED oder REJECTED wechseln sollte

Das Frontend bietet eine intuitive Oberfläche für alle Funktionen ohne Programmieraufwand.

### Methode 2: Über die API (für fortgeschrittene Tests)

Alternativ können Sie die API direkt mit curl-Befehlen testen. Beachten Sie, dass innerhalb des Containers der Service auf Port 8081 läuft:

#### 1. **Einen neuen Blog erstellen** (mit Validierung)

```bash
# Token holen
TOKEN=$(docker exec -i blog-service curl -s -X POST \
  -d "client_id=backend-service" \
  -d "username=alice" \
  -d "password=alice" \
  -d "client_secret=secret" \
  -d "grant_type=password" \
  http://keycloak:8080/realms/blog/protocol/openid-connect/token | sed -E 's/.*"access_token":"([^"]*).*/\1/')

# Blog erstellen
docker exec -i blog-service curl -s -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Neuer Blog-Beitrag", "content":"Dies ist ein Beispielinhalt für einen neuen Blog-Beitrag."}' \
  http://localhost:8081/blogs
```

#### 2. **Templates für KI-Blog-Generierung abrufen**

```bash
docker exec -i blog-service curl -X GET "http://localhost:8081/blogs/ai/templates"
```

#### 3. **Test-Blog mit KI generieren**

```bash
docker exec -i blog-service curl -X GET "http://localhost:8081/blogs/ai/test?title=Test%20KI&topic=Quarkus"
```

#### 4. **Vollständigen Blog mit KI generieren**

```bash
# Token holen (falls nicht bereits vorhanden)
TOKEN=$(docker exec -i blog-service curl -s -X POST \
  -d "client_id=backend-service" \
  -d "username=alice" \
  -d "password=alice" \
  -d "client_secret=secret" \
  -d "grant_type=password" \
  http://keycloak:8080/realms/blog/protocol/openid-connect/token | sed -E 's/.*"access_token":"([^"]*).*/\1/')

# KI-Blog erstellen
docker exec -i blog-service curl -s -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"KI und moderne Entwicklung","templateType":"standard","audience":"technische","tone":"informativ","length":"kurz (ca. 300-500 Wörter)"}' \
  http://localhost:8081/blogs/ai
```

#### 5. **Blog-Status prüfen**

```bash
# Blog-Status prüfen (ersetze <ID> mit der ID des erstellten Blogs)
docker exec -i blog-service curl -s -X GET \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8081/blogs/<ID>/status
```

> **Hinweis**: Die Befehle verwenden `localhost:8081` innerhalb des Docker-Containers. Von außerhalb des Containers erreichen Sie den Service über `http://localhost:8082`.

## Projektstruktur

- **blog-backend** (Hauptmodul): KI-gestützte Blog-Generierung und Verwaltung
  - `ch.hftm.ai.boundary`: REST-Ressourcen für KI-Endpunkte
  - `ch.hftm.ai.prompt`: Prompt-Templates und Konfiguration
  - `ch.hftm.ai.service`: KI-Service-Definition mit LangChain4J
  - `ch.hftm.boundary`: Allgemeine REST-Ressourcen
  - `ch.hftm.control`: Business-Logik
  - `ch.hftm.entity`: Domain-Modelle
  - `ch.hftm.repository`: Datenzugriff mit Panache
  - `ch.hftm.messaging`: Kafka-Integration

- **validator-service**: Unabhängiger Service zur Textvalidierung
  - `ch.hftm.messaging`: Kafka-Integration für Validierung
  - `ch.hftm.messaging.service`: Validierungs-Logik mit KI-basierter Content-Moderation
  - `ContentModerationService`: LangChain4J-Service zur intelligenten Inhaltsprüfung

- **shared-models**: Geteilte DTO-Klassen für Kafka-Messaging
  - `ch.hftm.messaging`: Validierungs-Request/Response-Modelle

## Validierungsregeln

Der Validator-Service kombiniert grundlegende Regeln mit KI-basierter Moderation:

### Basis-Validierungen:
- Text darf nicht leer sein
- Text muss mindestens 10 Zeichen lang sein
- Fallback-Validierung auf bestimmte verbotene Inhalte

### KI-Validierung mit LangChain4J (ContentModerationService):
- Prüfung auf unangemessene Sprache, Beleidigungen oder Hassrede
- Bewertung der inhaltlichen Qualität
- Generierung einer spezifischen Ablehnungsbegründung bei problematischen Inhalten

Die KI-Moderation nutzt das Modell `gpt-4o-mini` mit einer Temperatur von 0.7 für ausgewogene Entscheidungen.

## Fehlerbehebung

### Keycloak-Probleme

Falls Keycloak nicht korrekt startet oder Authentifizierungsprobleme auftreten:

```bash
# Services stoppen und Volumes löschen
docker-compose down -v

# Services neu starten
docker-compose up -d
```

### OpenAI-API-Probleme

Falls die OpenAI-Integration nicht funktioniert:
1. Stellen Sie sicher, dass ein gültiger OpenAI-API-Key in der `.env`-Datei konfiguriert ist
2. Prüfen Sie die Logs beider Services:
   ```bash
   docker logs blog-service    # Für Probleme mit der Blog-Generierung
   docker logs validator-service   # Für Probleme mit der Content-Moderation
   ```
3. Verwenden Sie den `/blogs/ai/test`-Endpunkt, der weniger Token verwendet und weniger anfällig für Timeouts ist
4. Beachten Sie, dass der Validator-Service das Modell `gpt-4o-mini` verwendet (konfiguriert in validator-service/src/main/resources/application.properties)

Wenn KI-generierte Blogs im Status `PENDING` verbleiben, kann dies bedeuten, dass der Validator-Service nicht ordnungsgemäß funktioniert. Überprüfen Sie die Validator-Service-Logs auf Fehler und stellen Sie sicher, dass der OpenAI API-Key korrekt konfiguriert ist.