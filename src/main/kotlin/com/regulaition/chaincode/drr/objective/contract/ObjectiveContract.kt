package com.regulaition.chaincode.drr.objective.contract

import com.regulaition.chaincode.drr.objective.command.ExecuteObjectiveCommand
import com.regulaition.chaincode.drr.objective.command.GetObjectiveCommand
import com.regulaition.chaincode.drr.objective.command.RegisterObjectiveCommand
import com.regulaition.chaincode.drr.objective.command.SubmitObjectiveCommand
import com.regulaition.chaincode.drr.objective.context.ObjectiveContext
import com.regulaition.chaincode.drr.objective.manager.ObjectiveManager
import com.regulaition.chaincode.drr.objective.state.Objective
import org.apache.logging.log4j.kotlin.Logging
import org.hyperledger.fabric.contract.Context
import org.hyperledger.fabric.contract.ContractInterface
import org.hyperledger.fabric.contract.annotation.Contract
import org.hyperledger.fabric.contract.annotation.Transaction
import org.hyperledger.fabric.shim.ChaincodeStub

@Contract(name = "com.regulaition.chaincode.drr.objective")
class ObjectiveContract : ContractInterface {
    private var objectiveManager: ObjectiveManager? = null

    init {
        //can make this better using DI, but not right now
        objectiveManager = ObjectiveManager()
    }

    //Use Log4j Kotlin API
    companion object : Logging

    override fun beforeTransaction(ctx: Context?) {
        logger.trace("Executing transaction for Objective[${this.hashCode()}] instance")
    }

    override fun createContext(stub: ChaincodeStub?): Context {
        logger.trace("Creating context for Objective[${this.hashCode()}] instance")
        return ObjectiveContext(stub)
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    fun register(context: ObjectiveContext, command: RegisterObjectiveCommand): Objective? {
        logger.debug("Registering Objective with $command")
        return objectiveManager?.registerObjective(context, command)
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    fun execute(context: ObjectiveContext, command: ExecuteObjectiveCommand): Objective? {
        logger.debug("Executing Objective with $command")
        return objectiveManager?.executeObjective(context, command)
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    fun submit(context: ObjectiveContext, command: SubmitObjectiveCommand): Objective? {
        logger.debug("Submitting Objective with $command")
        return objectiveManager?.submitObjective(context, command)
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    fun getObjective(context: ObjectiveContext, command: GetObjectiveCommand): Objective? {
        logger.debug("Fetch Objective with $command")
        return objectiveManager?.getObjective(context, command)
    }

}