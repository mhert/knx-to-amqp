FROM amazoncorretto:17 as builder
RUN mkdir /gradle-user-home
RUN mkdir /app
WORKDIR /app
ENV GRADLE_USER_HOME /gradle-user-home
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts ./
COPY gradle.properties ./
COPY src ./src
# starting with Java 13 posix_spawn is used in linux, instead of vfork. Unfortunatley this is not
# working with arm64. So we're using vfork
RUN ./gradlew -Djdk.lang.Process.launchMechanism=vfork clean build -i --stacktrace --no-daemon

FROM amazoncorretto:17
LABEL org.opencontainers.image.source=https://github.com/mhert/house-knx-automation
COPY --from=builder /app/build/libs/app-1.0-SNAPSHOT.jar /app.jar
CMD [ "java", "-jar", "/app.jar"]
