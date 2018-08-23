FROM sgrio/java-oracle:jre_10
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} bpm.jar
EXPOSE 8083
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bpm.jar"]