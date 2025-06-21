package com.madlabs.error;

import banking.AccountBalanceServiceGrpc;
import banking.BankService.AccountBalanceResponse;
import banking.BankService.AccountRequest;
import banking.BankService.CustomError;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;

public class BankClient {

	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext().build();

		AccountBalanceServiceGrpc.AccountBalanceServiceBlockingStub stub = AccountBalanceServiceGrpc
				.newBlockingStub(channel);

		try {

			AccountBalanceResponse bookResponse = stub
					.getAccountBalance(AccountRequest.newBuilder().setAccountNumber("-10").build());

			System.out.println(bookResponse);
		} catch (StatusRuntimeException ex) {
			Status status = ex.getStatus();
			System.out.println("error code -" + status.getCode());
			System.out.println("error description -" + status.getDescription());

			Metadata metadata = Status.trailersFromThrowable(ex);
			Metadata.Key<CustomError> customErrorKey = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
			CustomError customError = metadata.get(customErrorKey);
			System.out.println(customError);
		}
		channel.shutdown();
	}
}
