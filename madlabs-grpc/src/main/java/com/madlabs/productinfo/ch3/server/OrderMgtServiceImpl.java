package com.madlabs.productinfo.ch3.server;

import java.util.logging.Logger;

import com.google.protobuf.StringValue;

import ecommerce.OrderManagementGrpc.OrderManagementImplBase;
import ecommerce.OrderManagementOuterClass;
import ecommerce.OrderManagementOuterClass.Order;
import io.grpc.stub.StreamObserver;

public class OrderMgtServiceImpl extends OrderManagementImplBase {

	private static final Logger logger = Logger.getLogger(OrderMgtServiceImpl.class.getName());

	private OrderData data = new OrderData();

	// Unary
	@Override
	public void addOrder(Order request, StreamObserver<StringValue> responseObserver) {

		logger.info("Order Added - ID: " + request.getId() + ", Destination : " + request.getDestination());
		data.orderMap.put(request.getId(), request);
		StringValue id = StringValue.newBuilder().setValue("100500").build();
		responseObserver.onNext(id);
		responseObserver.onCompleted();
	}

	// Unary
	@Override
	public void getOrder(StringValue request, StreamObserver<OrderManagementOuterClass.Order> responseObserver) {
		OrderManagementOuterClass.Order order = data.orderMap.get(request.getValue());
		if (order != null) {
			System.out.printf("Order Retrieved : ID - %s", order.getId());
			responseObserver.onNext(order);
			responseObserver.onCompleted();
		} else {
			logger.info("Order : " + request.getValue() + " - Not found.");
			responseObserver.onCompleted();
		}

	}

}
