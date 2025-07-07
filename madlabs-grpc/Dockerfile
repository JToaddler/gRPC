FROM amazoncorretto:17
 
COPY target/lib /app/lib
COPY target/madlabs-grpc-1.0.0.jar /app/madlabs-grpc.jar
COPY entrypoint.sh ./entrypoint.sh

RUN chmod g+rx ./entrypoint.sh \
	&& chmod g+rx /app/madlabs-grpc.jar \
	&& chmod g+rx -R /app/lib \
	&& chmod g+rx ./
	

EXPOSE 50051

CMD [ "./entrypoint.sh" ]