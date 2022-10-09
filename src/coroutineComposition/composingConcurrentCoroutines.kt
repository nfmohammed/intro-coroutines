import kotlinx.coroutines.*
import kotlin.system.*

//https://kotlinlang.org/docs/composing-suspending-functions.html#concurrent-using-async
fun main() = runBlocking<Unit> {
//sampleStart
    val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
//sampleEnd
}

