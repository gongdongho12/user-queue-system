package com.dongholab.domain.infrastructure.util

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

fun <T, S> parallelExecution(
    itemList: Collection<T>,
    function: (item: T) -> S,
    executor: Executor,
    chunkSize: Int = 80,
) {
    for (chunkedItemList in itemList.chunked(chunkSize)) {
        val threads = mutableListOf<CompletableFuture<Void>>()
        for (item in chunkedItemList) {
            threads.add(
                CompletableFuture.runAsync(
                    {
                        function(item)
                    },
                    executor
                ).toCompletableFuture()
            )
        }
        threads.waitForAllComputed()
    }
}
