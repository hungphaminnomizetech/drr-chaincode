package com.regulaition.chaincode.drr.objective.context

import com.regulaition.chaincode.drr.objective.state.ObjectiveList
import org.hyperledger.fabric.contract.Context
import org.hyperledger.fabric.shim.ChaincodeStub

class ObjectiveContext(stub: ChaincodeStub?) : Context(stub) {
    var objectiveList: ObjectiveList = ObjectiveList(this)
}
