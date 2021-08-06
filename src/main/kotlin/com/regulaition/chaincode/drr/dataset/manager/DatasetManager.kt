package com.regulaition.chaincode.drr.dataset.manager

import com.regulaition.chaincode.drr.dataset.command.GetDatasetCommand
import com.regulaition.chaincode.drr.dataset.command.RegisterDatasetCommand
import com.regulaition.chaincode.drr.dataset.context.DatasetContext
import com.regulaition.chaincode.drr.dataset.state.Dataset
import org.apache.logging.log4j.kotlin.Logging

class DatasetManager {

    //Use Log4j Kotlin API
    companion object : Logging

    fun registerDataset(context: DatasetContext, command: RegisterDatasetCommand): Dataset {
        logger.debug("Registering dataset with $command")

        val dataset = command.let {
            Dataset.createInstance(it.hash, it.name, it.creationDateTime, it.nonce, it.ownerMSP)
        }

        logger.debug("Creating $dataset in the ledger")
        context.datasetList.addDataset(dataset)

        return dataset
    }

    fun getDataset(context: DatasetContext, command: GetDatasetCommand): Dataset? {
        return context.datasetList.getDataset(command.hash)
    }
}