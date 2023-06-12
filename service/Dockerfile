FROM amazoncorretto:11

COPY entrypoint.sh /entrypoint.sh
COPY target/service-1.0-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["/bin/sh", "entrypoint.sh"]