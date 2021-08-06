package com.regulaition.chaincode.drr.objective.state

import com.regulaition.chaincode.drr.common.State
import com.regulaition.chaincode.drr.dataset.state.Dataset
import org.hyperledger.fabric.contract.annotation.DataType
import org.hyperledger.fabric.contract.annotation.Property
import org.json.JSONObject
import java.nio.charset.StandardCharsets

@DataType
class Objective : State() {

    @Property
    var hash: String? = null
        private set

    @Property
    var name: String? = null
        private set

    @Property
    var creationDateTime: String? = null
        private set

    @Property
    var nonce = 0L
        private set

    @Property
    var ownerMSP: String? = null
        private set

    @Property
    var algorithmId: String? = null
        private set

    @Property
    var datasetIds: List<String> = emptyList()
        private set

    @Property
    var result: String? = null
        private set

    @Property
    var state: ObjectiveState? = ObjectiveState.REGISTERED

    fun setOwner(owner: String?): Objective {
        this.ownerMSP = owner
        return this
    }

    fun setKey(): Objective {
        key = makeKey(arrayOf(DOCTYPE, hash!!))
        return this
    }

    fun setState(state: ObjectiveState?): Objective {
        this.state = state
        return this
    }

    fun setHash(hash: String?): Objective {
        this.hash = hash
        return this
    }

    fun setName(name: String?): Objective {
        this.name = name
        return this
    }

    fun setCreationDateTime(creationDateTime: String?): Objective {
        this.creationDateTime = creationDateTime
        return this
    }

    fun setNonce(nonce: Long): Objective {
        this.nonce = nonce
        return this
    }

    fun setAlgorithmId(algorithmId: String?): Objective {
        this.algorithmId = algorithmId
        return this
    }

    fun setDatasetIds(datasetIds: List<String>): Objective {
        this.datasetIds = if (datasetIds.isNullOrEmpty()) emptyList() else datasetIds
        return this
    }

    fun setResult(result: String?): Objective {
        this.result = result
        return this
    }

    override fun toString(): String {
        return "Objective::" + key.toString() + "   " + hash.toString() + " " + name.toString() + " " + nonce
    }

    companion object {

        const val DOCTYPE = "Objective"

        /**
         * Deserialize a state data to dataset
         *
         * @param {Buffer} data to form back into the object
         */
        fun deserialize(data: ByteArray?): Objective? {
            return data.takeIf { it != null }?.let {
                constructObjectiveFromJson(it)
            }
        }

        private fun constructObjectiveFromJson(data: ByteArray): Objective {
            val stringJson = String(data, StandardCharsets.UTF_8)
            logger.debug("Fetched JSON string $stringJson")

            val json = JSONObject(stringJson)
            val hash = json.getString("hash")
            val name = json.getString("name")
            val creationDateTime = json.getString("creationDateTime")
            val ownerMSP = json.getString("ownerMSP")
            val nonce = json.getLong("nonce")
            val algorithmId = json.getString("algorithmId")
            val datasetIds = json.getJSONArray("datasetIds").toList()
            val result = json.getString("result")
            val state = json.getString("state")
            return createInstance(
                hash, name, creationDateTime, nonce, ownerMSP, algorithmId,
                datasetIds as List<String>, result, ObjectiveState.valueOf(state)
            )
        }

        fun serialize(paper: Objective?): ByteArray {
            return State.serialize(paper)
        }

        /**
         * Factory method to create a dataset object
         */
        fun createInstance(
            hash: String,
            name: String,
            creationDateTime: String,
            nonce: Long,
            ownerMSP: String,
            algorithmId: String?,
            datasetIds: List<String>,
            result: String?,
            state: ObjectiveState?
        ): Objective {
            return Objective()
                .setName(name)
                .setHash(hash)
                .setNonce(nonce)
                .setKey()
                .setCreationDateTime(creationDateTime)
                .setOwner(ownerMSP)
                .setAlgorithmId(algorithmId)
                .setDatasetIds(datasetIds)
                .setResult(result)
                .setState(state)
        }
    }
}