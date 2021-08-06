package com.regulaition.chaincode.drr.common

import org.hyperledger.fabric.contract.Context

interface StateList {
    /**
     * Add a state to the list. Creates a new state in worldstate with appropriate
     * composite key. Note that state defines its own key. State object is
     * serialized before writing.
     */
    fun addState(state: State): StateList

    /**
     * Get a state from the list using supplied keys. Form composite keys to
     * retrieve state from world state. State data is deserialized into JSON object
     * before being returned.
     */
    fun getState(key: String): State?

    /**
     * Get all state from the ledger using partial composite keys. Form composite keys to
     * retrieve state from world state. State data is deserialized into JSON object
     * before being returned.
     */
    fun getAllStates(): List<State?>?

    /**
     * Update a state in the list. Puts the new state in world state with
     * appropriate composite key. Note that state defines its own key. A state is
     * serialized before writing. Logic is very similar to addState() but kept
     * separate becuase it is semantically distinct.
     */
    fun updateState(state: State): StateList

    companion object {
        /*
     * SPDX-License-Identifier: Apache-2.0
     */
        /**
         * StateList provides a named virtual container for a set of ledger states. Each
         * state has a unique key which associates it with the container, rather than
         * the container containing a link to the state. This minimizes collisions for
         * parallel transactions on different states.
         */
        /**
         * Store Fabric context for subsequent API access, and name of list
         */
        fun getStateList(ctx: Context, listName: String, deserializer: StateDeserializer): StateList {
            return StateListImpl(ctx, listName, deserializer)
        }
    }
}