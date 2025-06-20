#!/bin/bash

./grpcwebproxy --backend_addr=localhost:50051 --run_tls_server=false  --backend_max_call_recv_msg_size=5242880

