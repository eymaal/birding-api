# Birding with Google Maps API
Birding with Google Maps API is a REST-API designed to support REST Consumers to track and sight birds in the Leicestershire and Rutland region.
## Tech Stack
This REST API is built with the following technologies.
- Java 17
- Spring Boot
- Spring Security
- Google Cloud Platform
- Lombok

* A JDK supporting Java 17, and MySQL database are required to run this API.
* Development of this API was done on a Windows 11 machine, using IntelliJ IDE. 

### DB credentials.
specify database name, username and password in ``` application.properties ```
### To build the server,
```
mvn clean build
```

### To start the server,
```
mvn spring-boot:run
```
## References
1. [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
1. [Spring Data JPA Docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
1. [Spring Security Docs](https://docs.spring.io/spring-security/reference/index.html)
1. [Guide to JWT](https://www.youtube.com/watch?v=lA18U8dGKF8&t=1033s)
1. [Google Cloud Storage Docs](https://cloud.google.com/storage/docs/)
1. [Guide to cloud buckets](https://medium.com/@raviyasas/spring-boot-file-upload-with-google-cloud-storage-5445ed91f5bc)
1. [Java Optional](https://medium.com/@raviyasas/spring-boot-file-upload-with-google-cloud-storage-5445ed91f5bc)
1. [HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)