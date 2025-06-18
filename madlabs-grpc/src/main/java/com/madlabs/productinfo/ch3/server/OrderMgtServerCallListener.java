package com.madlabs.productinfo.ch3.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.ForwardingServerCallListener;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;

public class OrderMgtServerCallListener<R> extends ForwardingServerCallListener<R> {

	private static final Logger logger = LogManager.getLogger(OrderMgtServerCallListener.class.getName());

	private final ServerCall.Listener<R> delegate;

	OrderMgtServerCallListener(ServerCall.Listener<R> delegate) {
		this.delegate = delegate;
	}

	@Override
	protected Listener delegate() {
		return delegate;
	}

	@Override
	public void onMessage(R message) {
		logger.info("Message Received from Client -> Service " + message);
		super.onMessage(message);
	}

	
	
}
