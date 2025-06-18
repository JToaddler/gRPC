package com.madlabs.productinfo.ch3.server;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;

public class OrderMgtServerCall<ReqT, RespT> extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> {

	private static final Logger logger = LogManager.getLogger(OrderMgtServerCall.class.getName());

	Metadata.Key<String> CUSTOM_HEADER_KEY = Metadata.Key.of("custom_server_header_key",
			Metadata.ASCII_STRING_MARSHALLER);

	Random random = new Random();

	public OrderMgtServerCall(ServerCall<ReqT, RespT> delegate) {
		super(delegate);
	}

	@Override
	protected ServerCall<ReqT, RespT> delegate() {
		return super.delegate();
	}

	@Override
	public MethodDescriptor<ReqT, RespT> getMethodDescriptor() {
		return super.getMethodDescriptor();
	}

	@Override
	public void sendMessage(RespT message) {
		logger.info("Message from Service -> Client : " + message);
		super.sendMessage(message);
	}

	@Override
	public void sendHeaders(Metadata headers) {
		headers.put(CUSTOM_HEADER_KEY, Integer.toString(random.nextInt()));
		super.sendHeaders(headers);
	}

}
