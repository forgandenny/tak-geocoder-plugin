package dev.jonpoulton.geocoder.core

import kotlinx.coroutines.runBlocking
import timber.log.Timber

fun <T> runBlockingOrNull(block: suspend () -> T): T? = runBlocking {
  try {
    block.invoke()
  } catch (e: Exception) {
    Timber.w(e)
    null
  }
}
