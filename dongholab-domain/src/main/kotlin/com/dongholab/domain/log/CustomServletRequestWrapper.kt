package com.dongholab.domain.log

import com.dongholab.domain.infrastructure.util.toJsonObject
import org.springframework.util.StringUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper

class CustomServletRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val rawBody: String

    init {
        val stringBuilder = StringBuilder()
        var bufferedReader: BufferedReader? = null
        try {
            if (request.inputStream != null) {
                bufferedReader = BufferedReader(InputStreamReader(request.inputStream))
                val charBuffer = CharArray(128)
                var bytesRead: Int
                while (bufferedReader.read(charBuffer).also { bytesRead = it } > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead)
                }
            }
        } catch (e: Exception) {
            throw e
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close()
                } catch (e: Exception) {
                    throw e
                }
            }
        }
        rawBody = stringBuilder.toString()
        if (!StringUtils.isEmpty(rawBody)) {
            val body = rawBody.toJsonObject(Any::class.java)
            if (body is Map<*, *>) {
                request.setAttribute("operationName", body["operationName"])
                request.setAttribute("variables", body["variables"]?.toString())
                request.setAttribute("query", body["query"])
            }
        }
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(rawBody.toByteArray())
        return object : ServletInputStream() {
            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(readListener: ReadListener) {}

            override fun read(): Int {
                return byteArrayInputStream.read()
            }
        }
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(this.inputStream))
    }
}
