package com.regulaition.chaincode.drr.dataset.context

import com.regulaition.chaincode.drr.dataset.state.DatasetList
import org.hyperledger.fabric.contract.Context
import org.hyperledger.fabric.shim.ChaincodeStub

class DatasetContext(stub: ChaincodeStub?) : Context(stub) {
    var datasetList: DatasetList = DatasetList(this)
}
