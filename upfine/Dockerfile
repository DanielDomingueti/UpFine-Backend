FROM openjdk:11.0.16-jdk

#mkdir + cd
WORKDIR /usr/src/spring-boot

COPY . .
RUN chmod -R 775 /usr/src/spring-boot

#Clean up the mvnw file
RUN sed -i 's/\r$//' mvnw

RUN ./mvnw clean package -Dmaven.test.skip=false -e
RUN ./mvnw clean compile -Dmaven.test.skip=false -e
RUN ./mvnw clean install -Dmaven.test.skip=false -e

EXPOSE 8080

CMD ["java", "-jar", "./target/upfine-0.0.1-SNAPSHOT.jar"]