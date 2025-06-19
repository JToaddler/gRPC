package com.madlabs.productinfo.ch3.client;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.StringValue;

import ecommerce.OrderManagementGrpc;
import ecommerce.OrderManagementGrpc.OrderManagementBlockingStub;
import ecommerce.OrderManagementGrpc.OrderManagementStub;
import ecommerce.OrderManagementOuterClass;
import ecommerce.OrderManagementOuterClass.Order;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class OrderMgtClient {

	private static final Logger logger = LogManager.getLogger(OrderMgtClient.class.getName());

	public static void main(String[] args) {

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext()
				.intercept(new OrderMgtClientInterceptor()).build();

		OrderManagementBlockingStub stub = OrderManagementGrpc.newBlockingStub(channel).withDeadlineAfter(1000000l,
				TimeUnit.MILLISECONDS);
		OrderManagementGrpc.OrderManagementStub asyncStub = OrderManagementGrpc.newStub(channel);

		OrderManagementOuterClass.Order order = OrderManagementOuterClass.Order.newBuilder().setId("101")
				.addItems("iPhone XS").addItems("Mac Book Pro").setDestination("San Jose, CA").setPrice(2300).build();

		try {
			// Unary func Add Order

			StringValue result = stub.addOrder(order);
			logger.info("AddOrder Response -> : " + result.getValue());
		} catch (StatusRuntimeException e) {
			if (e.getStatus().getCode() == Status.Code.DEADLINE_EXCEEDED) {
				logger.info("Deadline Exceeded. : " + e.getMessage());
			} else {
				logger.info("Unspecified error from the service -> " + e.getMessage());
			}
		}

		// Unary func Get Order
		StringValue id = StringValue.newBuilder().setValue("101").build();
		OrderManagementOuterClass.Order orderResponse = stub.getOrder(id);
		logger.info("GetOrder Response -> : " + orderResponse.toString());

		// 2 Server-Streaming Search Orders
		logger.info("searchOrders ");
		StringValue searchStr = StringValue.newBuilder().setValue("Google").build();

		Iterator<OrderManagementOuterClass.Order> matchingOrdersItr = stub.searchOrders(searchStr);
		logger.info("searchOrders response received ");
		while (matchingOrdersItr.hasNext()) {
			OrderManagementOuterClass.Order matchingOrder = matchingOrdersItr.next();
			logger.info("Search Order Response -> Matching Order - " + matchingOrder.getId());
		}
		logger.info("searchOrders response completed ");
		// client streaming RPC
		updateOrder(asyncStub);

	}

	private static void updateOrder(OrderManagementBlockingStub stub) {

		OrderManagementOuterClass.Order updOrder1 = OrderManagementOuterClass.Order.newBuilder().setId("102")
				.addItems("Google Pixel 3A").addItems("Google Pixel Book").setDestination("Mountain View, CA")
				.setPrice(1100).build();

		OrderManagementOuterClass.Order updOrder2 = OrderManagementOuterClass.Order.newBuilder().setId("103")
				.addItems("Apple Watch S4").addItems("Mac Book Pro").addItems("iPad Pro").setDestination("San Jose, CA")
				.setPrice(2800).build();

		OrderManagementOuterClass.Order updOrder3 = OrderManagementOuterClass.Order.newBuilder().setId("104")
				.addItems("Google Home Mini").addItems("Google Nest Hub").addItems("iPad Mini")
				.setDestination("Mountain View, CA").setPrice(2200).build();

	}

	private static void updateOrder(OrderManagementStub asyncStub) {

		OrderManagementOuterClass.Order updOrder1 = OrderManagementOuterClass.Order.newBuilder().setId("102")
				.addItems("Google Pixel 3A").addItems("Google Pixel Book").setDestination("Mountain View, CA")
				.setPrice(1100).build();

		OrderManagementOuterClass.Order updOrder2 = OrderManagementOuterClass.Order.newBuilder().setId("103")
				.addItems("Apple Watch S4").addItems("Mac Book Pro").addItems("iPad Pro").setDestination("San Jose, CA")
				.setPrice(2800).build();

		OrderManagementOuterClass.Order updOrder3 = OrderManagementOuterClass.Order.newBuilder().setId("104")
				.addItems("Google Home Mini").addItems("Google Nest Hub").addItems("iPad Mini")
				.setDestination("Mountain View, CA").setPrice(2200).build();

		final CountDownLatch finishLatch = new CountDownLatch(1);
		UpdateOrderStreamClient updateStream = new UpdateOrderStreamClient(finishLatch);

		StreamObserver<Order> updateOrderStream = asyncStub.updateOrders(updateStream);

		updateOrderStream.onNext(updOrder1);
		updateOrderStream.onNext(updOrder2);
		updateOrderStream.onNext(updOrder3);

		try {
			Thread.sleep(7000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Client onCompleted");
		updateOrderStream.onCompleted();

		try {
			if (!finishLatch.await(300, TimeUnit.SECONDS)) {
				logger.info("FAILED : Process orders cannot finish within 2 seconds");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
