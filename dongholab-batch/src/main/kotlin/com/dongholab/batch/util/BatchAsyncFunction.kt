package com.dongholab.batch.util

import org.springframework.batch.integration.async.AsyncItemProcessor
import org.springframework.batch.integration.async.AsyncItemWriter
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

fun generateTaskExecutor(
    corePoolSize: Int? = null,
    threadNamePrefix: String? = "commonBatchJob",
    executorAppend: (ThreadPoolTaskExecutor.() -> Unit)? = null
): TaskExecutor {
    val executor = ThreadPoolTaskExecutor()
    executor.corePoolSize = corePoolSize ?: 1
    executor.setQueueCapacity(1000)
    executor.setThreadNamePrefix("$threadNamePrefix-")
    executor.apply {
        executorAppend?.let {
            it()
        }
    }
    executor.initialize()
    return executor
}

fun <T, K> ItemProcessor<T, K>.toAsync(
    thread: Int? = null,
    namePrefix: String? = null,
    executorAppend: (ThreadPoolTaskExecutor.() -> Unit)? = null
): AsyncItemProcessor<T, K> {
    val asyncItemProcessor = AsyncItemProcessor<T, K>()
    asyncItemProcessor.setDelegate(this)
    asyncItemProcessor.setTaskExecutor(generateTaskExecutor(thread, namePrefix, executorAppend))
    return asyncItemProcessor
}

fun <T, K> ItemProcessor<T, K>.toAsync(
    taskExecutor: TaskExecutor
): AsyncItemProcessor<T, K> {
    val asyncItemProcessor = AsyncItemProcessor<T, K>()
    asyncItemProcessor.setDelegate(this)
    asyncItemProcessor.setTaskExecutor(taskExecutor)
    return asyncItemProcessor
}

fun <T> ItemWriter<T>.toAsync(): AsyncItemWriter<T> {
    val asyncItemWriter: AsyncItemWriter<T> = AsyncItemWriter()
    asyncItemWriter.setDelegate(this)
    return asyncItemWriter
}
