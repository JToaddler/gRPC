package com.madlabs.productinfo.ch3.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.StringValue;

import ecommerce.OrderManagementOuterClass;
import ecommerce.OrderManagementOuterClass.Order;
import io.grpc.stub.StreamObserver;

public class UpdateOrdersStream implements StreamObserver<OrderManagementOuterClass.Order> {

	private static final Logger logger = LogManager.getLogger(UpdateOrdersStream.class.getName());

	StreamObserver<StringValue> responseObserver;
	StringBuilder updatedOrderStrBuilder = new StringBuilder().append("Updated Order IDs : ");
	OrderData data;

	public UpdateOrdersStream(StreamObserver<StringValue> responseObserverm, OrderData data) {
		super();
		this.responseObserver = responseObserverm;
		this.data = data;
	}

	@Override
	public void onNext(Order value) {
		if (value != null) {
			data.orderMap.put(value.getId(), value);
			updatedOrderStrBuilder.append(value.getId()).append(", ");
			logger.info("Order ID : " + value.getId() + " - Updated");
		}
	}

	@Override
	public void onError(Throwable t) {
		logger.info("Order ID update error " + t.getMessage() + " " + t.getCause());
	}

	@Override
	public void onCompleted() {
		logger.info("Update orders - Completed");
		StringValue updatedOrders = StringValue.newBuilder().setValue(updatedOrderStrBuilder.toString()).build();
		responseObserver.onNext(updatedOrders);
		responseObserver.onCompleted();
	}

}