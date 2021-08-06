package com.regulaition.chaincode.drr.algorithm.context

import com.regulaition.chaincode.drr.algorithm.state.AlgorithmList
import org.hyperledger.fabric.contract.Context
import org.hyperledger.fabric.shim.ChaincodeStub

class AlgorithmContext(stub: ChaincodeStub?) : Context(stub) {
    var algorithmList: AlgorithmList = AlgorithmList(this)
}
