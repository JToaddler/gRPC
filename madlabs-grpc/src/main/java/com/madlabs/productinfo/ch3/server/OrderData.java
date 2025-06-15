package com.madlabs.productinfo.ch3.server;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ecommerce.OrderManagementOuterClass;

public class OrderData {

	Map<String, OrderManagementOuterClass.Order> orderMap;

	public OrderData() {

		OrderManagementOuterClass.Order ord1 = OrderManagementOuterClass.Order.newBuilder().setId("102")
				.addItems("Google Pixel 3A").addItems("Mac Book Pro").setDestination("Mountain View, CA").setPrice(1800)
				.build();
		OrderManagementOuterClass.Order ord2 = OrderManagementOuterClass.Order.newBuilder().setId("103")
				.addItems("Apple Watch S4").setDestination("San Jose, CA").setPrice(400).build();
		OrderManagementOuterClass.Order ord3 = OrderManagementOuterClass.Order.newBuilder().setId("104")
				.addItems("Google Home Mini").addItems("Google Nest Hub").setDestination("Mountain View, CA")
				.setPrice(400).build();
		OrderManagementOuterClass.Order ord4 = OrderManagementOuterClass.Order.newBuilder().setId("105")
				.addItems("Amazon Echo").setDestination("San Jose, CA").setPrice(30).build();
		OrderManagementOuterClass.Order ord5 = OrderManagementOuterClass.Order.newBuilder().setId("106")
				.addItems("Amazon Echo").addItems("Apple iPhone XS").setDestination("Mountain View, CA").setPrice(300)
				.build();

		orderMap = Stream.of(new AbstractMap.SimpleEntry<>(ord1.getId(), ord1),
				new AbstractMap.SimpleEntry<>(ord2.getId(), ord2), new AbstractMap.SimpleEntry<>(ord3.getId(), ord3),
				new AbstractMap.SimpleEntry<>(ord4.getId(), ord4), new AbstractMap.SimpleEntry<>(ord5.getId(), ord5))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	}

}
