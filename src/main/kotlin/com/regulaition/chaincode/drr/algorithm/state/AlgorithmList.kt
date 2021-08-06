package com.regulaition.chaincode.drr.algorithm.state

import com.regulaition.chaincode.drr.common.State.Companion.makeKey
import com.regulaition.chaincode.drr.common.StateList
import org.hyperledger.fabric.contract.Context

class AlgorithmList(ctx: Context) {
    private val listName = AlgorithmList::class.java.simpleName
    private val assetName = Algorithm::class.java.simpleName
    private val stateList: StateList =
        StateList.getStateList(ctx, listName, Algorithm.Companion::deserialize)

    fun addAlgorithm(algorithm: Algorithm): AlgorithmList {
        stateList.addState(algorithm)
        return this
    }

    fun getAlgorithm(key: String): Algorithm? {
        return stateList.getState(makeKey(arrayOf(listName, assetName, key))) as Algorithm?
    }

    fun getAllAlgorithms(): List<Algorithm> {
        return stateList.getAllStates()?.filterNotNull()?.map { it as Algorithm } ?: emptyList()
    }

    fun updatePaper(algorithm: Algorithm): AlgorithmList {
        stateList.updateState(algorithm)
        return this
    }
}