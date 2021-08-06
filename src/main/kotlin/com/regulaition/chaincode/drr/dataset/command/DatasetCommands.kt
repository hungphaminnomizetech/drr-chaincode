package com.regulaition.chaincode.drr.dataset.command

import org.hyperledger.fabric.contract.annotation.DataType
import org.hyperledger.fabric.contract.annotation.Property

@DataType
data class RegisterDatasetCommand(
    @field:Property val hash: String,
    @field:Property val name: String,
    @field:Property val nonce: Int,
    @field:Property val ownerMSP: String,
    @field:Property val creationDateTime: String
)

@DataType
data class GetDatasetCommand(@field:Property val hash: String, @field:Property val ownerMSP: String)

@DataType
data class GetAllDatasetsCommand(@field:Property val ownerMSP: String)