package com.madlabs.productinfo.ch3.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientCall.Listener;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

public class OrderMgtClientInterceptor implements ClientInterceptor {

	private static final Logger logger = LogManager.getLogger(OrderMgtClientInterceptor.class.getName());

	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
			CallOptions callOptions, Channel next) {

		logger.info(" interceptCall from client side " + method.getFullMethodName());

		return new OrderMgtClientCall<ReqT, RespT>(next.newCall(method, callOptions));
	}

}

class OrderMgtClientCall<ReqT, RespT> extends SimpleForwardingClientCall<ReqT, RespT> {

	private static final Logger logger = LogManager.getLogger(OrderMgtClientCall.class.getName());

	protected OrderMgtClientCall(ClientCall<ReqT, RespT> delegate) {
		super(delegate);
	}

	@Override
	public void start(Listener<RespT> responseListener, Metadata headers) {
		// TODO Auto-generated method stub
		logger.info(" interceptCall from client side ");
		super.start(new OrderMgtClientCallListener<RespT>(responseListener), headers);
	}

	@Override
	public void sendMessage(ReqT message) {
		logger.info(" Message from client -> server  :  " + message);
		super.sendMessage(message);
	}

}

class OrderMgtClientCallListener<RespT> extends SimpleForwardingClientCallListener<RespT> {

	private static final Logger logger = LogManager.getLogger(OrderMgtClientCallListener.class.getName());

	protected OrderMgtClientCallListener(Listener<RespT> delegate) {
		super(delegate);
	}

	@Override
	public void onHeaders(Metadata headers) {
		logger.info("header received from server:" + headers);
		super.onHeaders(headers);
	}

	@Override
	public void onMessage(RespT message) {
		logger.info("Message Received from Service -> client " + message);
		super.onMessage(message);
	}

}