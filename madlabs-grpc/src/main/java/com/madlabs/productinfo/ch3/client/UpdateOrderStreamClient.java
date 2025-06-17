package com.madlabs.productinfo.ch3.client;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.StringValue;

import io.grpc.stub.StreamObserver;

public class UpdateOrderStreamClient implements StreamObserver<StringValue> {

	private final CountDownLatch finishLatch;
	private static final Logger logger = LogManager.getLogger(OrderMgtClient.class.getName());

	public UpdateOrderStreamClient(CountDownLatch latch) {
		super();
		this.finishLatch = latch;
	}

	@Override
	public void onNext(StringValue value) {
		logger.info("Update Orders Response : " + value.getValue());

		try {
			Thread.sleep(400l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onError(Throwable t) {
		logger.info("Order ID update error " + t.getMessage() + " " + t.getCause());

	}

	@Override
	public void onCompleted() {
		finishLatch.countDown();
		logger.info("Update orders response  completed!" + finishLatch.getCount());
	}

}
