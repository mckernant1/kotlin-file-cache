package com.github.mckernant1.collections

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking


fun <T, R> Iterable<T>.cartesianProduct(other: Iterable<R>): List<Pair<T, R>> =
    this.flatMap { outer ->
        other.map { inner ->
            Pair(outer, inner)
        }
    }


fun <T> Iterable<T>?.equals(other: Iterable<T>?, preserveOrder: Boolean = true): Boolean =
    when {
        other == null || this == null -> this == null && other == null
        preserveOrder -> this == other
        else -> this.fold(true) { acc: Boolean, t: T ->
            acc && other.any { it == t }
        }
    }

fun <T, R> Iterable<T>.mapParallel(transform: suspend (T) -> R): List<R> = runBlocking {
    return@runBlocking map { async { transform(it) } }.awaitAll()
}
