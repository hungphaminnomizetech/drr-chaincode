package com.regulaition.chaincode.drr.algorithm.manager

import com.regulaition.chaincode.drr.algorithm.command.GetAlgorithmCommand
import com.regulaition.chaincode.drr.algorithm.command.GetAllAlgorithmsCommand
import com.regulaition.chaincode.drr.algorithm.command.RegisterAlgorithmCommand
import com.regulaition.chaincode.drr.algorithm.context.AlgorithmContext
import com.regulaition.chaincode.drr.algorithm.state.Algorithm
import com.regulaition.chaincode.drr.common.State
import org.apache.logging.log4j.kotlin.Logging
import org.hyperledger.fabric.shim.ChaincodeException


class AlgorithmManager {

    //Use Log4j Kotlin API
    companion object : Logging

    fun registerAlgorithm(context: AlgorithmContext, command: RegisterAlgorithmCommand): Algorithm {
        logger.debug("Registering algorithm with $command")
        val algorithmAsBytes = context.stub.getState(State.makeKey(arrayOf(Algorithm.DOCTYPE, command.hash)))
        logger.debug("Returned $algorithmAsBytes")

        val algorithm = command.let {
            Algorithm.createInstance(it.hash, it.name, it.creationDateTime, it.nonce, it.ownerMSP)
        }

        logger.debug("Creating $algorithm in the ledger")
        context.stub.putState(algorithm.key, State.serialize(algorithm))
        return algorithm

    }

    private fun throwSomeException(message: String): Nothing = throw ChaincodeException(message)

    fun getAlgorithm(context: AlgorithmContext, command: GetAlgorithmCommand): Algorithm? {
        return Algorithm.deserialize(
            context.stub
                .getState(State.makeKey(arrayOf(Algorithm.DOCTYPE, command.hash)))
                ?: throwSomeException("No Algorithm found for hash ${command.hash}")
        )
    }

    fun getAllAlgorithms(context: AlgorithmContext, command: GetAllAlgorithmsCommand): Array<Algorithm> {
        val iterator = context.stub.getQueryResult(
            State.QUERY_DOCTYPE.replace(
                State.QUERY_DOCTYPE_PLACEHOLDER,
                Algorithm.DOCTYPE
            )
        )
        logger.debug("Query result $iterator")
        val mappedNotNull = iterator
            .map {
                logger.debug("key ${it.key} value ${it.value}")
                it.value
            }
            .mapNotNull {
                Algorithm.deserialize(it)
            }
            .toTypedArray()
        logger.debug("Mapped result to not null ${mappedNotNull.joinToString(", ")}")
        return mappedNotNull
    }
}