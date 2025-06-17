package com.madlabs.productinfo.ch2.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ecommerce.ProductInfoGrpc;
import ecommerce.ProductInfoOuterClass;
import ecommerce.ProductInfoOuterClass.Product;
import ecommerce.ProductInfoOuterClass.ProductID;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

public class ProductInfoImpl extends ProductInfoGrpc.ProductInfoImplBase {

	private Map<String, ProductInfoOuterClass.Product> dataMap = new HashMap<>();

	private static final Logger logger = LogManager.getLogger(ProductInfoImpl.class.getName());

	@Override
	public void addProduct(Product request, StreamObserver<ProductID> responseObserver) {

		String id = UUID.randomUUID().toString();
		dataMap.put(id, request);
		logger.info(" addProduct :  " + id);
		ProductID pId = ProductInfoOuterClass.ProductID.newBuilder().setValue(id).build();
		System.out.println();
		responseObserver.onNext(pId);
		responseObserver.onCompleted();
	}

	@Override
	public void getProduct(ProductID request, StreamObserver<Product> responseObserver) {

		logger.info(" getProduct :  " + request.getValue());

		if (dataMap.containsKey(request.getValue())) {
			ProductInfoOuterClass.Product product = dataMap.get(request.getValue());
			responseObserver.onNext(product);
			responseObserver.onCompleted();
		} else {
			responseObserver.onError(new StatusException(Status.NOT_FOUND));
		}

	}

}