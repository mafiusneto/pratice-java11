# Pratice Java (incident)

Project MVC in java 

### Requeriments

- Java 11
- Spring Boot 2.7.18

### Libraries

- Spring web
- Spring data
- h2 (data base)
- lombok
- swagger

### Commands and util links

- Run project
```bash
./mvnw spring-boot:run
```

- Run tests
```bash
mvn clean test
# or
mvn test
```

- Compile, run test and jar generate
```bash
mvn clean install

#without test
mvn clean install -DskipTests
```

- Run docker compose
```bash
docker compose up -d
```

- Console H2

~http://localhost:8080/h2-console~

- Swagger

http://localhost:8080/swagger-ui.html [link1](http://localhost:8080/swagger-ui.html) or [link2](http://localhost:8080/swagger-ui/index.html)

## Paths

- List all incidents
```bash
curl -X 'GET' \
  'http://localhost:8080/incidents' \
  -H 'accept: */*'
```

- Create incident

```bash
curl -X 'POST' \
  'http://localhost:8080/incidents' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "name incident",
  "description": "description incident"
}'
```

- Update incident

```bash
curl -X 'PUT' \
  'http://localhost:8080/incidents/1' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "name incident closed",
  "description": "description incident",
  "closed": true
}'
```

- Find incident by id

```bash
curl -X 'GET' \
  'http://localhost:8080/incidents/1' \
  -H 'accept: */*'
```

- Delete incident by id

```bash
curl -X 'DELETE' \
  'http://localhost:8080/incidents/1' \
  -H 'accept: */*'
```

- List incidents with pagination and filter

```bash
curl -X 'GET' \
  'http://localhost:8080/incidents/page?page=0&size=20&sortBy=id&direction=DESC' \
  -H 'accept: */*'
```

- List latest incidents

```bash
curl -X 'GET' \
  'http://localhost:8080/incidents/latest' \
  -H 'accept: */*'
```

# Nota para melhorias

- Add field/enum status. open,closed, removed* ...
- Delete virtual, não remover da base apenas ocultar, mantem histórico
- Add histórico de mensagens do incident, incident é o cabeçalho.
- Melhorar filtro, para filtros mais complexos (criteria)
- Add mais testes. repository...?
- Remover autowired para adequar a solid (st)
- Mapear erros no ControllerAdvice
- Add algum logger