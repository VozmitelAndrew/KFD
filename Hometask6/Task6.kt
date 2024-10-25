

//для тестов
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class ThreadPool(threadCount: Int) : Executor {

    private val lock = ReentrantLock()
    private val tasksToRun = LinkedList<Runnable>()
    private val isTasksToRun = lock.newCondition()
    private val workingThreads = List(threadCount) { WorkerThread() }
    private var sleeping = false

    override fun execute(command: Runnable) {
        if(!sleeping)
            lock.withLock {
                tasksToRun.add(command)
                isTasksToRun.signal()
            }
    }

    fun shutdown(wait: Boolean) {
        lock.withLock {
            sleeping = true
            isTasksToRun.signalAll()
        }

        //у меня было 2 мысли - прокинуть if снаружи блока или оставить его внутри каждого foreach.
        // Мне кажется что это экономит копейки, но немного нарушaет DRY
        if(wait){
            workingThreads.forEach{it.join()}
        }else{
            workingThreads.forEach{it.interrupt()}
        }
    }


    inner class WorkerThread: Thread(){
        init {
            this.start()
        }

        override fun run(){
            while(!sleeping){
                lock.lock()
                if(tasksToRun.isEmpty() && !sleeping){
                    isTasksToRun.await()
                }
                if(sleeping){
                    lock.unlock()
                    break
                }
                val task = tasksToRun.pop()
                lock.unlock()
                task.run()
            }
        }
    }
}


// NOTE: почти все тесты нагенерил ГПТха, но задание я писал сам (по крайней мере на 90% :p)
fun main() {
    ThreadPoolTest().runAllTests()
}

class ThreadPoolTest {

    private lateinit var threadPool: ThreadPool

    fun runAllTests() {
        println("Запуск тестов для ThreadPool...")
        shouldExecuteSingleTask()
        shouldExecuteTasksInParallel()
        shutdownShouldWaitForAllTasksToFinish()
        shutdownShouldNotWaitIfWaitIsFalse()
        shouldHandleTasksQueueWhenOnlyOneThreadIsAvailable()
        shouldAvoidRaceConditionWhenAddingTasksFromMultipleThreads()
        shouldNotAcceptNewTasksAfterShutdown()
        println("Все тесты пройдены успешно.")
    }

    private fun setup(threadCount: Int = 4) {
        threadPool = ThreadPool(threadCount)
    }

    private fun tearDown() {
        threadPool.shutdown(true)
    }

    fun shouldExecuteSingleTask() {
        setup()
        val taskCompleted = CountDownLatch(1)
        threadPool.execute { taskCompleted.countDown() }
        taskCompleted.await()  // Ожидаем выполнения задачи
        println("Тест shouldExecuteSingleTask пройден.")
        tearDown()
    }

    fun shouldExecuteTasksInParallel() {
        setup()
        val taskCount = 5
        val tasksCompleted = CountDownLatch(taskCount)
        repeat(taskCount) {
            threadPool.execute {
                Thread.sleep(100)
                tasksCompleted.countDown()
            }
        }
        tasksCompleted.await()  // Ожидаем выполнения всех задач
        println("Тест shouldExecuteTasksInParallel пройден.")
        tearDown()
    }

    fun shutdownShouldWaitForAllTasksToFinish() {
        setup()
        val tasksCompleted = CountDownLatch(2)
        threadPool.execute {
            Thread.sleep(200)
            tasksCompleted.countDown()
        }
        threadPool.execute {
            Thread.sleep(200)
            tasksCompleted.countDown()
        }
        threadPool.shutdown(true)
        assert(tasksCompleted.count == 0L) { "Не все задачи завершены до shutdown" }
        println("Тест shutdownShouldWaitForAllTasksToFinish пройден.")
    }

    fun shutdownShouldNotWaitIfWaitIsFalse() {
        setup()
        val taskCompleted = CountDownLatch(1)
        threadPool.execute {
            Thread.sleep(500)
            taskCompleted.countDown()
        }
        threadPool.shutdown(false)
        Thread.sleep(100)  // Короткая задержка для завершения shutdown
        assert(taskCompleted.count > 0) { "Задача не была оставлена при shutdown(false)" }
        println("Тест shutdownShouldNotWaitIfWaitIsFalse пройден.")
    }

    fun shouldHandleTasksQueueWhenOnlyOneThreadIsAvailable() {
        setup(1)
        val tasksCompleted = CountDownLatch(3)
        repeat(3) {
            threadPool.execute {
                Thread.sleep(100)
                tasksCompleted.countDown()
            }
        }
        tasksCompleted.await()  // Ожидаем завершения всех задач
        println("Тест shouldHandleTasksQueueWhenOnlyOneThreadIsAvailable пройден.")
        tearDown()
    }

    fun shouldAvoidRaceConditionWhenAddingTasksFromMultipleThreads() {
        setup()
        val taskCount = 10
        val tasksCompleted = CountDownLatch(taskCount)
        val threads = List(5) {
            Thread {
                repeat(2) {
                    threadPool.execute {
                        Thread.sleep(100)
                        tasksCompleted.countDown()
                    }
                }
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        tasksCompleted.await()  // Ожидаем завершения всех задач
        println("Тест shouldAvoidRaceConditionWhenAddingTasksFromMultipleThreads пройден.")
        tearDown()
    }

    fun shouldNotAcceptNewTasksAfterShutdown() {
        setup()
        threadPool.shutdown(true)
        val taskCompleted = CountDownLatch(1)
        threadPool.execute { taskCompleted.countDown() }
        Thread.sleep(100)  // Короткая задержка, чтобы убедиться, что задача не выполняется
        assert(taskCompleted.count > 0) { "Пул принял задачу после завершения" }
        println("Тест shouldNotAcceptNewTasksAfterShutdown пройден.")
    }
}
