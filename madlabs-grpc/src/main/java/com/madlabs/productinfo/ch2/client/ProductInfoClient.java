package com.madlabs.productinfo.ch2.client;

import ecommerce.ProductInfoGrpc;
import ecommerce.ProductInfoOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ProductInfoClient {

	public static void main(String[] args) {

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();

		ProductInfoGrpc.ProductInfoBlockingStub stub = ProductInfoGrpc.newBlockingStub(channel);

		ProductInfoOuterClass.Product product = ProductInfoOuterClass.Product.newBuilder().setId("id of the product")
				.setDescription("test description").setName("test name").setPrice(23.34f).build();

		ProductInfoOuterClass.ProductID productIdRes = stub.addProduct(product);
		System.out.println(" Response : " + productIdRes.getValue());

		ProductInfoOuterClass.Product prod2 = stub.getProduct(productIdRes);

		channel.shutdown();
	}

}
