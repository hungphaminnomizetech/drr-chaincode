package com.regulaition.chaincode.drr.objective.state

import com.regulaition.chaincode.drr.common.State.Companion.makeKey
import com.regulaition.chaincode.drr.common.StateList
import org.hyperledger.fabric.contract.Context

class ObjectiveList(ctx: Context) {
    private val listName = ObjectiveList::class.java.simpleName
    private val assetName = Objective::class.java.simpleName
    private val stateList: StateList =
        StateList.getStateList(ctx, listName, Objective::deserialize)

    fun addObjective(objective: Objective): ObjectiveList {
        stateList.addState(objective)
        return this
    }

    fun getObjective(key: String): Objective? {
        return stateList.getState(makeKey(arrayOf(listName, assetName, key))) as Objective?
    }

    fun getAllObjectives(): List<Objective> {
        return stateList.getAllStates()?.filterNotNull()?.map { it as Objective } ?: emptyList()
    }

    fun updatePaper(objective: Objective): ObjectiveList {
        stateList.updateState(objective)
        return this
    }
}