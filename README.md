# Blog Backend mit Kafka-basierter Text-Validierung

Dieses Projekt besteht aus drei Modulen:
1. **Blog-Service**: Hauptanwendung für das Blog-Backend
2. **Validator-Service**: Separater Service für die Textvalidierung über Kafka
3. **Shared-Models**: Gemeinsam genutzte Modelle für die Kafka-Kommunikation

## Architektur

Die Anwendung nutzt folgende Technologien:
- Quarkus als Framework
- MySQL als Datenbank
- Keycloak für die Authentifizierung
- Kafka für die asynchrone Kommunikation zwischen den Services

### Blog-Status-Ablauf

1. Wenn ein neuer Blog erstellt wird, erhält er den Status `PENDING`
2. Der Blog-Service sendet eine Validierungsanfrage an Kafka
3. Der Validator-Service empfängt die Anfrage und führt die Validierung durch
4. Das Validierungsergebnis wird über Kafka zurückgesendet
5. Der Blog-Status wird auf `APPROVED` oder `REJECTED` aktualisiert

## Vorbereitung

### Voraussetzungen

- Java 17+
- Maven
- Docker und Docker Compose

## Starten der Anwendung

Es gibt zwei Möglichkeiten, die Anwendung zu starten:

### Option 1: Mit fertigen GHCR-Images

Images herunterladen:
```bash
# Images direkt von GHCR ziehen
docker pull ghcr.io/21yanick/blog-backend:latest
docker pull ghcr.io/21yanick/validator-service:latest
```

Oder:
- https://github.com/users/21yanick/packages/container/blog-backend
- https://github.com/users/21yanick/packages/container/validator-service


```bash
# Verwendung der GHCR-Images
cp docker-compose.ghcr.yml.example docker-compose.yml
docker-compose up -d
```

Diese Variante verwendet die fertigen Container-Images von GitHub Container Registry und benötigt keinen lokalen Build.

### Option 2: Mit lokalem Build

```bash
cp docker-compose.local-build.yml.example docker-compose.yml

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

Die Services sind dann unter folgenden URLs erreichbar:
- Blog-Service: http://localhost:8082
- Validator-Service: http://localhost:8083
- Keycloak: http://localhost:8080
- Kafka: localhost:9093

## API-Dokumentation

Die OpenAPI-Dokumentation ist unter http://localhost:8082/swagger-ui verfügbar.

### Blog-Status-API

- `GET /blogs/{id}/status` - Status eines Blogs abfragen
- Status-Werte:
  - `PENDING`: Blog wartet auf Validierung
  - `APPROVED`: Blog wurde validiert und freigegeben
  - `REJECTED`: Blog wurde validiert und abgelehnt

## Kafka-Kommunikation

Die Services kommunizieren über zwei Kafka-Topics:
- `blog-validation-requests`: Anfragen zur Textvalidierung
- `blog-validation-responses`: Antworten mit Validierungsergebnissen

## Entwickler-Informationen

### Validierungsregeln

Der Validator-Service prüft folgende Regeln:
- Text darf nicht leer sein
- Text muss mindestens 10 Zeichen lang sein
- Text darf keine verbotenen Inhalte haben

## Features

- **Blog-Verwaltung**: Erstellen, Abrufen, Aktualisieren und Löschen von Blog-Beiträgen.
- **Kommentar-Verwaltung**: Kommentare zu Blog-Beiträgen hinzufügen.
- **Likes-Verwaltung**: Benutzer können Blog-Beiträge liken und unliken.
- **Filterung**: Blog-Beiträge nach Titel filtern.
- **Asynchrone Textvalidierung**: Blog-Inhalte werden asynchron mittels Kafka validiert und der Status entsprechend aktualisiert.

## Token von Keycloak holen

Du kannst den Access-Token von Keycloak mit folgendem **curl**-Befehl abrufen:

```bash
curl -X POST http://localhost:8080/realms/blog/protocol/openid-connect/token \
    -d 'grant_type=password' \
    -d 'client_id=backend-service' \
    -d 'username=testuser' \
    -d 'password=123456' \
    -d 'client_secret=secret'
```

> **Hinweis**: Die Keycloak-Konfiguration wird automatisch beim Start geladen. Das System ist bereits mit dem Testuser `testuser` (Passwort: `123456`) vorkonfiguriert, der die `admin`-Rolle besitzt.

## Beispielhafte API-Aufrufe

### 1. **Einen neuen Blog erstellen** (mit Validierung)

```bash
# Token holen
TOKEN=$(docker exec -i blog-service curl -s -X POST \
  -d "client_id=backend-service" \
  -d "username=testuser" \
  -d "password=123456" \
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

### 2. **Status eines Blogs abrufen**

```bash
# Blog-Status prüfen (ersetze <ID> mit der ID des erstellten Blogs)
docker exec -i blog-service curl -s -X GET \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8081/blogs/<ID>/status
```

Die Antwort enthält den aktuellen Validierungsstatus (PENDING, APPROVED oder REJECTED).