package com.madlabs.error;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class BankServer {

	private static final Logger logger = LogManager.getLogger(BankServer.class.getName());

	private Server server;
	final int port = 50053;

	private void start() throws IOException {

		server = ServerBuilder.forPort(port).addService(new BankServiceImpl()).build().start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// Use stderr here since the logger may have been reset by its JVM shutdown
			// hook.
			logger.info("*** shutting down gRPC server since JVM is shutting down");
			BankServer.this.stop();
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

	public static void main(String[] args) {

		try {
			BankServer server = new BankServer();
			server.start();
			server.blockUntilShutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
