package com.regulaition.chaincode.drr.algorithm.state

import com.regulaition.chaincode.drr.common.State
import org.hyperledger.fabric.contract.annotation.DataType
import org.hyperledger.fabric.contract.annotation.Property
import org.json.JSONObject
import java.nio.charset.StandardCharsets

@DataType
class Algorithm : State() {

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
    var nonce = 0
        private set

    @Property
    var ownerMSP: String? = null
        private set

    fun setOwner(owner: String?): Algorithm {
        this.ownerMSP = owner
        return this
    }

    fun setKey(): Algorithm {
        key = makeKey(arrayOf(DOCTYPE, hash!!))
        return this
    }

    fun setHash(hash: String?): Algorithm {
        this.hash = hash
        return this
    }

    fun setName(name: String?): Algorithm {
        this.name = name
        return this
    }

    fun setCreationDateTime(creationDateTime: String?): Algorithm {
        this.creationDateTime = creationDateTime
        return this
    }

    fun setNonce(nonce: Int): Algorithm {
        this.nonce = nonce
        return this
    }


    override fun toString(): String {
        return "Algorithm::" + key.toString() + "   " + hash.toString() + " " + name.toString() + " " + nonce
    }

    companion object {

        const val DOCTYPE = "Algorithm"

        /**
         * Deserialize a state data to Algorithm
         *
         * @param {Buffer} data to form back into the object
         */
        fun deserialize(data: ByteArray?): Algorithm? {
            return data.takeIf { it != null }?.let {
                constructAlgorithmFromJson(it)
            }
        }

        private fun constructAlgorithmFromJson(data: ByteArray): Algorithm {
            val stringJson = String(data, StandardCharsets.UTF_8)
            logger.debug("Fetched JSON string $stringJson")

            val json = JSONObject(stringJson)
            val hash = json.getString("hash")
            val name = json.getString("name")
            val creationDateTime = json.getString("creationDateTime")
            val ownerMSP = json.getString("ownerMSP")
            val nonce = json.getInt("nonce")
            return createInstance(hash, name, creationDateTime, nonce, ownerMSP)
        }

        fun serialize(paper: Algorithm?): ByteArray {
            return State.serialize(paper)
        }

        /**
         * Factory method to create a Algorithm object
         */
        fun createInstance(
            hash: String, name: String, creationDateTime: String,
            nonce: Int, ownerMSP: String
        ): Algorithm {
            return Algorithm().setName(name).setHash(hash)
                .setNonce(nonce).setKey().setCreationDateTime(creationDateTime).setOwner(ownerMSP)
        }
    }
}