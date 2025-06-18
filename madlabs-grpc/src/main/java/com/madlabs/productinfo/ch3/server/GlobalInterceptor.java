package com.madlabs.productinfo.ch3.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class GlobalInterceptor implements ServerInterceptor {

	private static final Logger logger = LogManager.getLogger(GlobalInterceptor.class.getName());

	@Override
	public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
			ServerCallHandler<ReqT, RespT> next) {

		logger.info("Remote Method Invoked - " + call.getMethodDescriptor().getFullMethodName() + ", Headers : " + headers);

		ServerCall<ReqT, RespT> serverCall = new OrderMgtServerCall<>(call);
		// Context ctx = Context.current();
		// return Contexts.interceptCall(ctx, call, headers, next);
		return new OrderMgtServerCallListener<>(next.startCall(serverCall, headers));
	}

}
