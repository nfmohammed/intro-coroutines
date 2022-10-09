package miscellaneous

import doSomethingUsefulOne
import doSomethingUsefulTwo
import kotlinx.coroutines.*
import kotlin.system.*

fun main() = runBlocking<Unit> {
    val time = measureTimeMillis {
        println("The answer is ${concurrentSum()}")
    }
    println("Completed in $time ms")
}

//This way, if something goes wrong inside the code of the concurrentSum function, and it throws an exception,
// all the coroutines that were launched in its scope will be cancelled.
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}
