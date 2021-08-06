# DRR Chaincode

This is the chaincode for Digital Regulatory Reporting. There are 3 assets within the chaincode:

- Algorithms
- Datasets
- Objectives

# Tech Stack
## Development Stack

- Kotlin 1.3
- Gradle 6.4.1
- Jackson for JSON Serialization / Deserialization
- Hyperledger fabric SDK

# How To Use

Once installed onto the network, query the assets using the "peer" command

e.g.
- `peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n amlchaincode --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt -c '{"function":"com.regulaition.chaincode.drr.dataset:register","Args":["{\"hash\":\"hash1_1\",\"name\":\"Dataset1\",\"nonce\":1,\"ownerMSP\":\"testorg\",\"creationDateTime\":\"2021-09-08\"}"]}'`
- `peer chaincode query -C mychannel -n amlchaincode -c '{"function":"com.regulaition.chaincode.drr.dataset:getDataset","Args":["{\"hash\":\"hash1_1\",\"ownerMSP\":\"testorg\"}"]}'`