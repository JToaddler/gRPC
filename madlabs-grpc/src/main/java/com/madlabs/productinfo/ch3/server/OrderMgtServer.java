package com.madlabs.productinfo.ch3.server;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class OrderMgtServer {
	private static final Logger logger = LogManager.getLogger(OrderMgtServer.class.getName());

	private Server server;

	private void start() throws IOException {
		
		/* The port on which the server should run */
		int port = 50051;
		server = ServerBuilder.forPort(port).intercept(new ContextInterceptor()).intercept(new GlobalInterceptor())
				.addService(new OrderMgtServiceImpl()).build().start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// Use stderr here since the logger may have been reset by its JVM shutdown
			// hook.
			logger.info("*** shutting down gRPC server since JVM is shutting down");
			OrderMgtServer.this.stop();
			logger.info("*** server shut down");
		}));
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		final OrderMgtServer server = new OrderMgtServer();
		server.start();
		server.blockUntilShutdown();
	}

}