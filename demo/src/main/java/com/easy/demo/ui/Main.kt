package com.easy.demo.ui

import kotlinx.coroutines.*
import okhttp3.internal.wait
import kotlin.random.Random

fun main(args: Array<String>) {
    println("star")
//    exampleBlocking7()
    saleTickets()
    println("end")
}

fun exampleBlocking() {
    runBlocking(Dispatchers.Default) {
        printlnWithTime("one - from thread ${Thread.currentThread().name}")
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    printlnWithTime("three - from thread ${Thread.currentThread().name}")
}

fun exampleBlocking2() = runBlocking {
    printlnWithTime("one - from thread ${Thread.currentThread().name}")
    launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    printlnWithTime("three - from thread ${Thread.currentThread().name}")
}

fun exampleBlocking3() {
    printlnWithTime("one - from thread ${Thread.currentThread().name}")
    GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    printlnWithTime("three - from thread ${Thread.currentThread().name}")
}

fun exampleBlocking4() = runBlocking {
    printlnWithTime("one - from thread ${Thread.currentThread().name}")
    val job = GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    printlnWithTime("three - from thread ${Thread.currentThread().name}")
    job.join()
}

fun printlnWithTime(message: String) {
    println("$message -- ${System.currentTimeMillis().toString().takeLast(4)}")
}

fun printlnDelayed(message: String) {
    //当前线程延迟1000毫秒,这会阻塞当前线程
    Thread.sleep(5000)
    printlnWithTime(message)
}

fun exampleBlocking5() = runBlocking {
    val startTime = System.currentTimeMillis()
    //启动一个新的协程,用于计算结果
    val deferred1 = async { calculateHardThings(10) }
    val deferred2 = async { calculateHardThings(20) }
    val deferred3 = async { calculateHardThings(30) }
    //await会阻塞当前线程,等待计算完毕,并且返回协程的计算结果
    val sum = deferred1.await() + deferred2.await() + deferred3.await()
    println("sum = $sum")
    val endTime = System.currentTimeMillis()
    println("总耗时: ${endTime - startTime}")
}

fun exampleBlocking6() = runBlocking {
    val startTime = System.currentTimeMillis()
    //启动一个新的协程,用于计算结果
    val deferred1 = async { calculateHardThings(10) }.await()
    val deferred2 = async { calculateHardThings(20) }.await()
    val deferred3 = async { calculateHardThings(30) }.await()
    //await会阻塞当前线程,等待计算完毕,并且返回协程的计算结果
    val sum = deferred1 + deferred2 + deferred3
    println("sum = $sum")
    val endTime = System.currentTimeMillis()
    println("总耗时: ${endTime - startTime}")
}

fun exampleBlocking7() = runBlocking {
    val startTime = System.currentTimeMillis()
    //启动一个新的协程,用于计算结果
    val deferred1 = withContext(Dispatchers.Default) { calculateHardThings(10) }
    val deferred2 = withContext(Dispatchers.Default) { calculateHardThings(20) }
    val deferred3 = withContext(Dispatchers.Default) { calculateHardThings(30) }
    //await会阻塞当前线程,等待计算完毕,并且返回协程的计算结果
    val sum = deferred1 + deferred2 + deferred3
    println("sum = $sum")
    val endTime = System.currentTimeMillis()
    println("总耗时: ${endTime - startTime}")
}

suspend fun calculateHardThings(startNum: Int): Int {
    delay(1000)
    return startNum * 10
}
fun saleTickets() = runBlocking {
    //要卖出的票总数
    var ticketsCount = 100
    //售票员数量
    val salerCount = 4
    //此列表保存async返回值状态,用于控制协程等待
    val salers: MutableList<Deferred<Unit>> = mutableListOf()
    repeat(salerCount) {
        val deferred = async {
            while (ticketsCount > 0) {
                println("第${it + 1}个售票员 卖出第${100 - ticketsCount + 1}张火车票")
                ticketsCount--
                //随机延迟100-1000毫秒,使每次售票时间不相同
                val random = Random.nextInt(10)+1
                delay((random * 100).toLong())
            }
        }
        salers.add(deferred)
    }
    salers.forEach { it.await() }
}