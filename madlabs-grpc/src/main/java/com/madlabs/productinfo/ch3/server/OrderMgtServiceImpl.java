package com.madlabs.productinfo.ch3.server;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.StringValue;

import ecommerce.OrderManagementGrpc.OrderManagementImplBase;
import ecommerce.OrderManagementOuterClass;
import ecommerce.ProductInfoGrpc;
import ecommerce.ProductInfoOuterClass;
import ecommerce.OrderManagementOuterClass.Order;
import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class OrderMgtServiceImpl extends OrderManagementImplBase {

	private static final Logger logger = LogManager.getLogger(OrderMgtServiceImpl.class.getName());

	private OrderData data = new OrderData();
	

	
	// Unary
	@Override
	public void addOrder(Order request, StreamObserver<StringValue> responseObserver) {

		logger.info("Order Added - ID: " + request.getId() + ", Destination : " + request.getDestination());
		try {
			Thread.sleep(1002l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.orderMap.put(request.getId(), request);
		StringValue id = StringValue.newBuilder().setValue("100500").build();
		
		Context ctx = Context.current();
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

	// Server-Streaming RPC
	@Override
	public void searchOrders(StringValue request, StreamObserver<Order> responseObserver) {

		logger.info("searchOrders received ");

		for (Map.Entry<String, OrderManagementOuterClass.Order> orderEntry : data.orderMap.entrySet()) {

			OrderManagementOuterClass.Order order = orderEntry.getValue();

			for (String item : order.getItemsList()) {
				if (item.contains(request.getValue())) {
					logger.info("searchOrders sending data ");
					responseObserver.onNext(order);
					break;
				}
			}
			try {
				Thread.sleep(500l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("searchOrders onCompleted ");
		responseObserver.onCompleted();
	}

	// Client-Streaming RPC
	@Override
	public StreamObserver<Order> updateOrders(StreamObserver<StringValue> responseObserver) {
		return new UpdateOrdersStream(responseObserver, this.data);
	}

}
