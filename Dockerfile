FROM maven:3-eclipse-temurin-17-alpine AS base
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM base as test
RUN mvn test

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/app/target/dependency
COPY --from=base ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=base ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=base ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","dev.apma.cnat.userservice.CnatUserServiceApplication"]
