package com.madlabs.productinfo.ch3.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class ContextInterceptor implements ServerInterceptor {

	private static final Context.Key<String> USER_ID_CTX_KEY = Context.key("userId");
	private static final String ADMIN_USER_ID = "admin";

	private static final Logger logger = LogManager.getLogger(ContextInterceptor.class.getName());

	@Override
	public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
			ServerCallHandler<ReqT, RespT> next) {
		logger.info(
				"Remote Method Invoked - " + call.getMethodDescriptor().getFullMethodName() + ", Headers : " + headers);

		Context ctx = Context.current().withValue(USER_ID_CTX_KEY, ADMIN_USER_ID);
		return Contexts.interceptCall(ctx, call, headers, next);
	}

}
