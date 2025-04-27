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


./mvnw spring-boot:run

http://localhost:8080/h2-console


# Nota para melhorias

- Add field/enum status. open,closed, removed* ...
- Delete virtual, não remover da base apenas ocultar, mantem histórico
- Add histórico de mensagens do incident, incident é o cabeçalho.
- Melhorar filtro, para filtros mais complexos (criteria)
- Add mais testes. repository...?
- Remover autowired para adequar a solid (st)
- Mapear erros no ControllerAdvice
- Add algum logger