package com.regulaition.chaincode.drr.objective.command

import org.hyperledger.fabric.contract.annotation.DataType
import org.hyperledger.fabric.contract.annotation.Property

@DataType
data class RegisterObjectiveCommand(
    @field:Property val hash: String,
    @field:Property val name: String,
    @field:Property val nonce: Long,
    @field:Property val ownerMSP: String,
    @field:Property val creationDateTime: String,
    @field:Property val algorithmId: String,
    @field:Property val datasetIds: Array<String>
)

@DataType
data class ExecuteObjectiveCommand(@field:Property val hash: String)

@DataType
data class SubmitObjectiveCommand(@field:Property val hash: String, @field:Property val result: String)

@DataType
data class GetObjectiveCommand(@field:Property val hash: String, @field:Property val ownerMSP: String)

@DataType
data class GetAllObjectivesCommand(@field:Property val ownerMSP: String)