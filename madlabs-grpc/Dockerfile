FROM openjdk:17
 
COPY target/lib /app/lib
COPY target/madlabs-grpc-1.0.0.jar /app/madlabs-grpc.jar


ENTRYPOINT ["java", "-jar", "/app/madlabs-grpc.jar"]
EXPOSE 50051