package com.regulaition.chaincode.drr.objective.manager

import com.regulaition.chaincode.drr.common.State
import com.regulaition.chaincode.drr.objective.command.ExecuteObjectiveCommand
import com.regulaition.chaincode.drr.objective.command.GetObjectiveCommand
import com.regulaition.chaincode.drr.objective.command.RegisterObjectiveCommand
import com.regulaition.chaincode.drr.objective.command.SubmitObjectiveCommand
import com.regulaition.chaincode.drr.objective.context.ObjectiveContext
import com.regulaition.chaincode.drr.objective.state.Objective
import com.regulaition.chaincode.drr.objective.state.ObjectiveState
import org.apache.logging.log4j.kotlin.Logging
import org.hyperledger.fabric.shim.ChaincodeException

class ObjectiveManager {

    //Use Log4j Kotlin API
    companion object : Logging

    fun registerObjective(context: ObjectiveContext, command: RegisterObjectiveCommand): Objective {
        logger.debug("Registering objective with $command")
        val objectiveAsBytes = context.stub.getState(State.makeKey(arrayOf(Objective.DOCTYPE, command.hash)))
        logger.debug("Returned $objectiveAsBytes")

        val objective = command.let {
            Objective.createInstance(
                it.hash,
                it.name,
                it.creationDateTime,
                it.nonce,
                it.ownerMSP,
                it.algorithmId,
                it.datasetIds.toList(),
                "",
                ObjectiveState.REGISTERED
            )
        }

        logger.debug("Creating $objective in the ledger")
        context.stub.putState(objective.key, State.serialize(objective))
        return objective

    }

    fun executeObjective(context: ObjectiveContext, command: ExecuteObjectiveCommand): Objective? {
        logger.debug("Executing objective with $command")
        val objectiveAsBytes = context.stub.getState(State.makeKey(arrayOf(Objective.DOCTYPE, command.hash)))
        logger.debug("Returned $objectiveAsBytes")

        val objective = Objective.deserialize(objectiveAsBytes)
        objective?.setState(ObjectiveState.EXECUTED)

        logger.debug("Updating $objective in the ledger")
        context.stub.putState(objective?.key, State.serialize(objective))
        return objective
    }

    fun submitObjective(context: ObjectiveContext, command: SubmitObjectiveCommand): Objective? {
        logger.debug("Submitting objective with $command")
        val objectiveAsBytes = context.stub.getState(State.makeKey(arrayOf(Objective.DOCTYPE, command.hash)))
        logger.debug("Returned $objectiveAsBytes")

        val objective = Objective.deserialize(objectiveAsBytes)
        objective?.setState(ObjectiveState.SUBMITTED)
        objective?.setResult(command.result)

        logger.debug("Updating $objective in the ledger")
        context.stub.putState(objective?.key, State.serialize(objective))
        return objective
    }

    private fun throwSomeException(message: String): Nothing = throw ChaincodeException(message)

    fun getObjective(context: ObjectiveContext, command: GetObjectiveCommand): Objective? {
        return Objective.deserialize(
            context.stub
                .getState(State.makeKey(arrayOf(Objective.DOCTYPE, command.hash)))
                ?: throwSomeException("No objectives found for hash ${command.hash}")
        )
    }
}