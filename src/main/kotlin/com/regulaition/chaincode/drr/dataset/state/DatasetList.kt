package com.regulaition.chaincode.drr.dataset.state

import com.regulaition.chaincode.drr.common.State.Companion.makeKey
import com.regulaition.chaincode.drr.common.StateList
import org.hyperledger.fabric.contract.Context

class DatasetList(ctx: Context) {
    private val listName = DatasetList::class.java.simpleName
    private val assetName = Dataset::class.java.simpleName
    private val stateList: StateList =
        StateList.getStateList(ctx, listName, Dataset::deserialize)

    fun addDataset(dataset: Dataset): DatasetList {
        stateList.addState(dataset)
        return this
    }

    fun getDataset(key: String): Dataset? {
        return stateList.getState(makeKey(arrayOf(listName, assetName, key))) as Dataset?
    }

    fun getAllDatasets(): List<Dataset> {
        return stateList.getAllStates()?.filterNotNull()?.map { it as Dataset } ?: emptyList()
    }

    fun updatePaper(dataset: Dataset): DatasetList {
        stateList.updateState(dataset)
        return this
    }
}