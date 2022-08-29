package com.fastcampus.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

suspend fun main() {
    //runBlockingTest()
    //launchTest()
    //launchTest2()
    //launchTest3()
    // asyncTest()
    //doSomething()
    //doSomething2()
    flowTest()
}

fun runBlockingTest() {
    runBlocking {
        println("Hello")
        println(Thread.currentThread().name)
    }
    println("World")
    println(Thread.currentThread().name)
}

fun launchTest() {
    runBlocking<Unit> {
        launch {
            delay(500L)
            println("World!")
        }
        println("Hello")
    }
}

fun launchTest2() {
    runBlocking {
        launch {
            val elapsedTime = measureTimeMillis {
                delay(150)
            }
            println("async task-1 $elapsedTime ms")
        }

        launch {
            val elapsedTime = measureTimeMillis {
                delay(100)
            }
            println("async task-2 $elapsedTime ms")
        }

    }
}

fun launchTest3() {
    runBlocking {
        val job1: Job = launch {
            val elapsedTime = measureTimeMillis {
                delay(150)
            }
            println("async task-1 $elapsedTime ms")
        }

        // job1은 취소했기 때문에 job2 만 실행됨.
        job1.cancel()

        // (start = CoroutineStart.LAZY) 설정을 통해 job2가 바로 실행이 안되고 start() 메소드를 호출해야 실행이 되게 할 수 있음.
        val job2: Job = launch(start = CoroutineStart.LAZY) {
            val elapsedTime = measureTimeMillis {
                delay(100)
            }
            println("async task-2 $elapsedTime ms")
        }

        println("start task-2")
        job2.start()
    }
}

fun sum(a: Int, b: Int) = a + b

fun asyncTest() {
    runBlocking<Unit> {
        val result1: Deferred<Int> = async {
            delay(100)
            sum(1, 3)
        }

        println("result1 : ${result1.await()}")

        val result2: Deferred<Int> = async {
            delay(100)
            delay(100)
            sum(2, 5)
        }

        println("result2 : ${result2.await()}")
    }
}

fun printHello() = println("hello")

suspend fun doSomething() {
    printHello()
}

suspend fun doSomething2() = coroutineScope {
    launch {
        delay(200)
        println("world!")
    }

    launch {
        printHello()
    }
}

fun flowTest() {
    runBlocking<Unit> {
        val flow = simple()
        flow.collect { value -> println(value) }
    }
}

fun simple(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}
