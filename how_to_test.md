* API endpoints and response schema can be **tested** via **[Open-API Swagger UI](http://localhost:8080/swagger-ui/index.html)**.
* Also, Integration test can be run using command
```shell
.\mvnw test
```

* API-key needs to be used with header **X-API-KEY** as a token for authentication\
Key can be found in [application.properties](src/main/resources/application.properties) under property `backbase.api.key.value` and changed if needed.
