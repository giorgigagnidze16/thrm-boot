# Thermostat management  service
P.S this project was created in a very short period of time

### HOW TO RUN THE PROJECT 

Run docker-compose.yml on your local machine.
<br/> It is preferred to first run mysql service and wait before starting backend service
<br/> 

### Navigate to http://localhost:3000/ to play around.

### The  project uses: </br>

Java 17, Spring Boot, Spring Security, Sprig Data JPA, RabbitMQ <br/>
React.js, TypeScript, Axios, Docker, MySQL, etc. </br>

## What can be improved
implement logic for refreshTokens and improve JWT auth </br>
use Redux for user state management

## Back end container:
https://hub.docker.com/r/jetskibaby/backend

## Front end repository:
Could've used JSP or Thymeleaf but decided to go with an entire client application </br>
https://github.com/giorgigagnidze16/prj-thrm-client

Container: <br/>
https://hub.docker.com/r/jetskibaby/thermo-front

## Temperature Generator repository:

https://github.com/giorgigagnidze16/temperature-generator

Container: <br/>
https://hub.docker.com/r/jetskibaby/generator


# Note
### front end might have problems fetching device temperature on time (due to delayed response from generated temperature), this can be simply fixed by increasing timeout
#### also in some cases deciding whether device is critical or not, All of this can easily be fixed but this was done in a really short period of time and little bugs may be present
