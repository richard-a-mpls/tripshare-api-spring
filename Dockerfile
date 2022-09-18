FROM openjdk:20
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV DISABLE_CSRF=yes
ENV JWT_VERIFY_AUTH=YXBwX2tleV8yOnNlY3JldDI\=
ENV JWT_VERIFY_ENDPOINT=http://localhost:8083/validate
ENV prod_SPRING_DATA_MONGODB_URI=mongodb+srv://mongonewuserforme:QTms9tM1eGE0tr8x@cluster0.oeufb.mongodb.net/tripblogger?retryWrites\=true&w\=majority
ENV SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/mydatabase

ENTRYPOINT ["java","-jar","/app.jar"]