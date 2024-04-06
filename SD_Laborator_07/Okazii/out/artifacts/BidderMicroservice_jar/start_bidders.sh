#!/bin/bash

# Numărul de instanțe Bidder pe care vrei să le lansezi
NUM_INSTANCES=100

# Bucla care lansează instanțele
for (( i=1; i<=NUM_INSTANCES; i++ ))
do
   echo "Starting Bidder instance $i"
   java -jar BidderMicroservice.jar &
done

echo "$NUM_INSTANCES instances of Bidder Microservice have been started."
