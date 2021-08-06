package com.regulaition.chaincode.drr.algorithm.contract

import com.regulaition.chaincode.drr.algorithm.manager.AlgorithmManager
import com.regulaition.chaincode.drr.algorithm.command.GetAlgorithmCommand
import com.regulaition.chaincode.drr.algorithm.command.GetAllAlgorithmsCommand
import com.regulaition.chaincode.drr.algorithm.command.RegisterAlgorithmCommand
import com.regulaition.chaincode.drr.algorithm.context.AlgorithmContext
import com.regulaition.chaincode.drr.algorithm.state.Algorithm
import org.apache.logging.log4j.kotlin.Logging
import org.hyperledger.fabric.contract.Context
import org.hyperledger.fabric.contract.ContractInterface
import org.hyperledger.fabric.contract.annotation.Contract
import org.hyperledger.fabric.contract.annotation.Transaction
import org.hyperledger.fabric.shim.ChaincodeStub

@Contract(name = "com.regulaition.chaincode.drr.algorithm")
class AlgorithmContract : ContractInterface {
    private var algorithmManager: AlgorithmManager? = null

    init {
        //can make this better using DI, but not right now
        algorithmManager = AlgorithmManager()
    }

    //Use Log4j Kotlin API
    companion object : Logging

    override fun beforeTransaction(ctx: Context?) {
        logger.trace("Executing transaction for Algorithm[${this.hashCode()}] instance")
    }

    override fun createContext(stub: ChaincodeStub?): Context {
        logger.trace("Creating context for Algorithm[${this.hashCode()}] instance")
        return AlgorithmContext(stub)
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    fun register(context: AlgorithmContext, command: RegisterAlgorithmCommand): Algorithm? {
        logger.debug("Registering Algorithm with $command")
        return algorithmManager?.registerAlgorithm(context, command)
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    fun getAlgorithm(context: AlgorithmContext, command: GetAlgorithmCommand): Algorithm? {
        logger.debug("Fetch Algorithm with $command")
        return algorithmManager?.getAlgorithm(context, command)
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    fun getAllAlgorithms(context: AlgorithmContext, command: GetAllAlgorithmsCommand): Array<Algorithm>? {
        logger.debug("Fetch Algorithms with $command")
        return algorithmManager?.getAllAlgorithms(context, command)
    }

}