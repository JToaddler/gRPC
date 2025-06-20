package com.madlabs.productinfo.ch2.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;

public class ProductInfoServer {

	private static final Logger logger = Logger.getLogger(ProductInfoServer.class.getName());

	ExecutorService executorService;
	private Server server;

	private void start() throws IOException {
		/* The port on which the server should run */
		int port = 50052;
//		server = ServerBuilder.forPort(port).addService(new ProductInfoImpl())
//				.executor(Executors.newFixedThreadPool(20)).build().start();

		executorService = Executors.newFixedThreadPool(20);

		server = NettyServerBuilder.forPort(port).addService(new ProductInfoImpl()).executor(executorService)
				.bossEventLoopGroup(new NioEventLoopGroup(1)).workerEventLoopGroup(new NioEventLoopGroup(125))
				.channelType(NioServerSocketChannel.class).maxInboundMessageSize(Integer.MAX_VALUE)
				.maxConnectionIdle(5, TimeUnit.MINUTES).permitKeepAliveWithoutCalls(true)
				.permitKeepAliveTime(180, TimeUnit.SECONDS).build().start();

		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// Use stderr here since the logger may have been reset by its JVM shutdown
			// hook.
			logger.info("*** shutting down gRPC server since JVM is shutting down");
			ProductInfoServer.this.stop();
			logger.info("*** server shut down");
		}));
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	private void stop() {
		if (server != null) {

			executorService.shutdown();
			try {
				if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
					executorService.shutdownNow();
				}
			} catch (InterruptedException e) {
				executorService.shutdownNow();
			}

			server.shutdown();

		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		final ProductInfoServer server = new ProductInfoServer();
		server.start();
		server.blockUntilShutdown();
	}

}
