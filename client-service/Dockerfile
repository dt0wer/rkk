FROM sgrio/java-oracle:jre_10
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} clientservice.jar
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/clientservice.jar"]