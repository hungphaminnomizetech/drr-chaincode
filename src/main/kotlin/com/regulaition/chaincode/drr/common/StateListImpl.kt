package com.regulaition.chaincode.drr.common

import org.apache.logging.log4j.kotlin.Logging
import org.hyperledger.fabric.contract.Context

/*
SPDX-License-Identifier: Apache-2.0
*/
/**
 * StateList provides a named virtual container for a set of ledger states. Each
 * state has a unique key which associates it with the container, rather than
 * the container containing a link to the state. This minimizes collisions for
 * parallel transactions on different states.
 */
class StateListImpl(private val ctx: Context, private val name: String, private val deserializer: StateDeserializer) :
    StateList {
    private val supportedClasses: Any? = null

    //Use Log4j Kotlin API
    companion object : Logging

    /**
     * Add a state to the list. Creates a new state in world state with appropriate
     * composite key. Note that state defines its own key. State object is
     * serialized before writing.
     */
    override fun addState(state: State): StateList {
        logger.trace("Adding state to $name")
        val splitKey: Array<String> = state.splitKey
        logger.trace("Split key as ${splitKey.toList()}")
        val ledgerKey = State.makeKey(arrayOf(name, *splitKey))
        logger.debug("ledger key is $ledgerKey")
        val data = State.serialize(state)
        ctx.stub.putState(ledgerKey, data)
        return this
    }

    /**
     * Get a state from the list using supplied keys. Form composite keys to
     * retrieve state from world state. State data is deserialized into JSON object
     * before being returned.
     */
    override fun getState(key: String): State? {
        logger.debug("Getting state with key $key")
        val data = ctx.stub.getState(key)
        return if (data != null) {
            deserializer.deserialize(data)
        } else {
            return null
        }
    }

    /**
     * Get a state from the list using supplied keys. Form composite keys to
     * retrieve state from world state. State data is deserialized into JSON object
     * before being returned.
     */
    override fun getAllStates(): List<State?>? {
        val compositeKey = ctx.stub.createCompositeKey(name)
        val data = ctx.stub.getStateByPartialCompositeKey(compositeKey)
        return data?.filterNot { it.value != null }?.map { deserializer.deserialize(it.value) }?.toList()
    }

    /**
     * Update a state in the list. Puts the new state in world state with
     * appropriate composite key. Note that state defines its own key. A state is
     * serialized before writing. Logic is very similar to addState() but kept
     * separate because it is semantically distinct.
     */
    override fun updateState(state: State): StateList {
        val ledgerKey = ctx.stub.createCompositeKey(name, *state.splitKey)
        val data = State.serialize(state)
        ctx.stub.putState(ledgerKey.toString(), data)
        return this
    }

}