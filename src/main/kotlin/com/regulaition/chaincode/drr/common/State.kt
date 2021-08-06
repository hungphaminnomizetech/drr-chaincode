/*
SPDX-License-Identifier: Apache-2.0
*/
package com.regulaition.chaincode.drr.common

import org.apache.logging.log4j.kotlin.Logging
import org.json.JSONObject
import java.lang.String.join
import java.nio.charset.StandardCharsets

/**
 * State class. States have a class, unique key, and a lifecycle current state
 * the current state is determined by the specific subclass
 */
open class State
/**
 * @param {String|Object} class An identifiable class of the instance
 * @param {keyParts[]} elements to pull together to make a key for the objects
 */
{
    var key: String? = null
        protected set
    val splitKey: Array<String>
        get() = splitKey(key)

    companion object : Logging {

        var QUERY_DOCTYPE_PLACEHOLDER = "@DOCTYPE_PLACEHOLDER@"
        var QUERY_DOCTYPE = "{\n" +
                "\t\"selector\": {\n" +
                "\t\t\"key\": {\n" +
                "\t\t\t\"\$regex\": \"$QUERY_DOCTYPE_PLACEHOLDER\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}"
        /**
         * Convert object to buffer containing JSON data serialization Typically used
         * before putState()ledger API
         *
         * @param {Object} JSON object to serialize
         * @return {buffer} buffer with the data to store
         */
        fun serialize(`object`: State?): ByteArray {
            val jsonStr = JSONObject(`object`).toString()
            return jsonStr.toByteArray(StandardCharsets.UTF_8)
        }

        /**
         * Join the keyParts to make a unified string
         *
         * @param (String[]) keyParts
         */
        fun makeKey(keyParts: Array<String>): String {
            val joinedKey = join(":", *keyParts)
            logger.debug("Returning $joinedKey as key")
            return joinedKey
        }

        fun splitKey(key: String?): Array<String> {
            val splitKeys = key!!.split(":").toTypedArray()
            logger.debug("Split keys ${splitKeys.toList()} obtained for key $key")
            return splitKeys
        }
    }
}