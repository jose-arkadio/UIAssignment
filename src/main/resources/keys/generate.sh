#!/bin/bash

openssl genrsa 2048 | openssl pkcs8 -topk8 -nocrypt -out RSA256-JWT-private-key.pem
openssl rsa -in RSA256-JWT-private-key.pem -pubout -outform PEM -out RSA256-JWT-public-key.pem