package com.madlabs.productinfo.ch3.client;

import java.util.Iterator;
import java.util.logging.Logger;

import com.google.protobuf.StringValue;

import ecommerce.OrderManagementGrpc;
import ecommerce.OrderManagementOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class OrderMgtClient {

	private static final Logger logger = Logger.getLogger(OrderMgtClient.class.getName());

	public static void main(String[] args) {

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

		OrderManagementGrpc.OrderManagementBlockingStub stub = OrderManagementGrpc.newBlockingStub(channel);
		OrderManagementGrpc.OrderManagementStub asyncStub = OrderManagementGrpc.newStub(channel);

		OrderManagementOuterClass.Order order = OrderManagementOuterClass.Order.newBuilder().setId("101")
				.addItems("iPhone XS").addItems("Mac Book Pro").setDestination("San Jose, CA").setPrice(2300).build();

		// Add Order
		StringValue result = stub.addOrder(order);
		logger.info("AddOrder Response -> : " + result.getValue());

		// Get Order
		StringValue id = StringValue.newBuilder().setValue("101").build();
		OrderManagementOuterClass.Order orderResponse = stub.getOrder(id);
		logger.info("GetOrder Response -> : " + orderResponse.toString());

		// Search Orders
		StringValue searchStr = StringValue.newBuilder().setValue("Google").build();

		Iterator<OrderManagementOuterClass.Order> matchingOrdersItr = stub.searchOrders(searchStr);

		while (matchingOrdersItr.hasNext()) {
			OrderManagementOuterClass.Order matchingOrder = matchingOrdersItr.next();
			logger.info("Search Order Response -> Matching Order - " + matchingOrder.getId());
			logger.info(" Order : " + order.getId() + "\n " + matchingOrder.toString());
		}

	}

}
