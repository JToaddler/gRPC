package com.madlabs.error;

import banking.AccountBalanceServiceGrpc.AccountBalanceServiceImplBase;
import banking.BankService;
import banking.BankService.CustomError;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;

public class BankServiceImpl extends AccountBalanceServiceImplBase {
	@Override
	public void getAccountBalance(BankService.AccountRequest request,
			StreamObserver<banking.BankService.AccountBalanceResponse> responseObserver) {

		Long accountNumber = Long.parseLong(request.getAccountNumber());

		if (accountNumber <= 0) {
			Metadata metadata = new Metadata();
			Metadata.Key<CustomError> customError = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
			metadata.put(customError, CustomError.newBuilder().setMessage("Database Error- Connection Refused.")
					.setErrorType("CONNECTION_REFUSED").build());
			var statusRuntimeException = Status.NOT_FOUND
					.withDescription("The requested Account Number cannot be found.").asRuntimeException(metadata);
			responseObserver.onError(statusRuntimeException);
			return;
		}

		banking.BankService.AccountBalanceResponse response = banking.BankService.AccountBalanceResponse.newBuilder()
				.setAccountNumber(request.getAccountNumber()).setBalance(1000.00).build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
