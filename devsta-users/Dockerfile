FROM adoptopenjdk:11-jre-openj9
ADD devsta-users-0.0.1-SNAPSHOT.jar devsta-users-0.0.1-SNAPSHOT.jar
ADD application.yml application.yml
#ADD application-db.properties application-db.properties
ENV JAVA_OPTS=""
#ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","/simple-svc.jar", "--spring.profiles.active=default"]
ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","devsta-users-0.0.1-SNAPSHOT.jar"]
EXPOSE 9090
EXPOSE 17017