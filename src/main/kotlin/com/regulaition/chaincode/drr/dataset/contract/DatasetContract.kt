package com.regulaition.chaincode.drr.dataset.contract

import com.google.common.annotations.VisibleForTesting
import com.regulaition.chaincode.drr.dataset.command.GetAllDatasetsCommand
import com.regulaition.chaincode.drr.dataset.command.GetDatasetCommand
import com.regulaition.chaincode.drr.dataset.command.RegisterDatasetCommand
import com.regulaition.chaincode.drr.dataset.context.DatasetContext
import com.regulaition.chaincode.drr.dataset.manager.DatasetManager
import com.regulaition.chaincode.drr.dataset.state.Dataset
import org.apache.logging.log4j.kotlin.Logging
import org.hyperledger.fabric.contract.Context
import org.hyperledger.fabric.contract.ContractInterface
import org.hyperledger.fabric.contract.annotation.Contract
import org.hyperledger.fabric.contract.annotation.Transaction
import org.hyperledger.fabric.shim.ChaincodeStub

@Contract(name = "com.regulaition.chaincode.drr.dataset")
class DatasetContract: ContractInterface {
    internal var datasetManager: DatasetManager? = null

    init {
        //can make this better using DI, but not right now
        datasetManager = DatasetManager()
    }

    //Use Log4j Kotlin API
    companion object : Logging

    override fun beforeTransaction(ctx: Context?) {
        logger.trace("Executing transaction for Dataset[${this.hashCode()}] instance")
    }

    override fun createContext(stub: ChaincodeStub?): Context {
        logger.trace("Creating context for Dataset[${this.hashCode()}] instance")
        return DatasetContext(stub)
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    fun register(context: DatasetContext, command: RegisterDatasetCommand): Dataset? {
        logger.debug("Registering dataset with $command")
        return datasetManager?.registerDataset(context, command)
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    fun getDataset(context: DatasetContext, command: GetDatasetCommand): Dataset? {
        logger.debug("Fetch dataset with $command")
        return datasetManager?.getDataset(context, command)
    }

}