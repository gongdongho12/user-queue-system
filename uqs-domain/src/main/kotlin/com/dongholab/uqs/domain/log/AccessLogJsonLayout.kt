package com.dongholab.uqs.domain.log

import ch.qos.logback.access.spi.IAccessEvent
import ch.qos.logback.contrib.json.JsonLayoutBase

class AccessLogJsonLayout : JsonLayoutBase<IAccessEvent>() {
    override fun doLayout(event: IAccessEvent?): String {
        return "[ZzL]" + super.doLayout(event)
    }

    override fun toJsonMap(iAccessEvent: IAccessEvent): MutableMap<String, Any?> {
        var map = mutableMapOf<String, Any?>()
        map["service_name"] = System.getProperty("dd.service.name")
        map["@e_idx"] = "service-dongholab" //
        map["logType"] = "access"
        map["tag"] = System.getProperty("tag", "N/A")
        map["profile"] = System.getProperty("spring.profiles.active")
        map["method"] = iAccessEvent.method
        map["uri"] = iAccessEvent.requestURI
        map["status"] = iAccessEvent.response.status
        map["operationName"] = iAccessEvent.request.getAttribute("operationName")
        map["variableString"] = iAccessEvent.request.getAttribute("variables")
        map["query"] = iAccessEvent.request.getAttribute("query")
        map["queryString"] = iAccessEvent.queryString
        map["contentLength"] = iAccessEvent.contentLength
        map["referer"] = extractHeader(iAccessEvent, "referer")
        map["userAgent"] = extractHeader(iAccessEvent, "user-agent")
        map["headers"] = iAccessEvent.requestHeaderMap
        map["elapsedTime"] = iAccessEvent.elapsedTime
        addTimestamp("@timestamp", true, iAccessEvent.timeStamp, map)
        return map
    }

    private fun extractHeader(iAccessEvent: IAccessEvent, key: String): String {
        return iAccessEvent.getRequestHeader(key) ?: ""
    }
}
