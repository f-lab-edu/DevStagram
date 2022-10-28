FROM adoptopenjdk:11-jre-openj9
ADD target/devsta-gateway-0.0.1-SNAPSHOT.jar devsta-gateway-0.0.1-SNAPSHOT.jar
ADD application.yml application.yml
ENV JAVA_OPTS=""
#ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","/simple-svc.jar", "--spring.profiles.active=default"]
ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","devsta-gateway-0.0.1-SNAPSHOT.jar"]
EXPOSE 9999 9090 9091 9092