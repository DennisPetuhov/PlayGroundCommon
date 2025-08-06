import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.atomicfu.atomic

/**
 * Binary Semaphore Mental Model:
 * - Think of it as a single parking space with a queue
 * - Only ONE coroutine can "park" (hold the lock) at a time
 * - Others wait in line until the space is free
 * - Reentrant = same coroutine can "re-park" multiple times
 */
class ReentrantCoroutineLock {
    // Binary semaphore: 1 permit = lock available, 0 permits = lock taken
    private val semaphore = Semaphore(permits = 1)
    
    // Thread-safe owner tracking using atomicfu (KMM compatible)
    private val owner = atomic<Job?>(null)
    private val reentrancyCount = atomic(0)

    suspend fun lock() {
        val currentJob = currentCoroutineContext()[Job]
        
        // Check if current coroutine already owns the lock (reentrancy)
        if (owner.value == currentJob) {
            reentrancyCount.incrementAndGet()
            return
        }
        
        // Acquire the binary semaphore (will suspend if unavailable)
        semaphore.acquire()
        
        // Now we own the lock
        owner.value = currentJob
        reentrancyCount.value = 1
    }

    suspend fun unlock() {
        val currentJob = currentCoroutineContext()[Job]
        
        // Verify ownership
        if (owner.value != currentJob) {
            throw IllegalStateException("Lock not owned by current coroutine")
        }

        val newCount = reentrancyCount.decrementAndGet()
        if (newCount == 0) {
            // Fully unlocked, release the semaphore
            owner.value = null
            semaphore.release()
        }
    }

    suspend fun <T> withLock(action: suspend () -> T): T {
        lock()
        try {
            return action()
        } finally {
            unlock()
        }
    }
    
    // Useful for debugging
    val isLocked: Boolean get() = semaphore.availablePermits == 0
    val lockOwner: Job? get() = owner.value
}

// ============================================================================
// EXAMPLES: Understanding Binary Semaphore Patterns
// ============================================================================

/**
 * Example 1: Basic Binary Semaphore Usage
 * Mental Model: Turnstile with single passage
 */
class BinarySemaphoreExample {
    private val semaphore = Semaphore(1) // Binary: 0 or 1 permits
    
    suspend fun criticalSection(id: String) {
        println("[$id] Waiting for semaphore...")
        semaphore.acquire() // Will block if permit not available
        try {
            println("[$id] 🔒 Entered critical section")
            delay(1000) // Simulate work
            println("[$id] 🔓 Leaving critical section")
        } finally {
            semaphore.release() // Always release!
        }
    }
}

/**
 * Example 2: Resource Pool with Binary Semaphore
 * Mental Model: Single shared resource (printer, database connection, etc.)
 */
class SharedResourceManager {
    private val resourceLock = Semaphore(1)
    private var resource: String? = null
    
    suspend fun useResource(userId: String): String {
        resourceLock.acquire()
        try {
            if (resource == null) {
                println("[$userId] Creating resource...")
                delay(500) // Simulate expensive resource creation
                resource = "SharedResource_${System.currentTimeMillis()}"
            }
            println("[$userId] Using resource: $resource")
            delay(200) // Simulate work with resource
            return resource!!
        } finally {
            resourceLock.release()
        }
    }
}

/**
 * Example 3: Producer-Consumer with Binary Semaphore
 * Mental Model: Single buffer slot
 */
class SingleSlotBuffer<T> {
    private val empty = Semaphore(1) // Buffer starts empty
    private val full = Semaphore(0)  // No items initially
    private var buffer: T? = null
    
    suspend fun produce(item: T) {
        empty.acquire() // Wait for empty slot
        buffer = item
        println("Produced: $item")
        full.release() // Signal item available
    }
    
    suspend fun consume(): T {
        full.acquire() // Wait for item
        val item = buffer!!
        buffer = null
        println("Consumed: $item")
        empty.release() // Signal slot empty
        return item
    }
}

// ============================================================================
// REENTRANT LOCK EXAMPLES
// ============================================================================

suspend fun accessResource(lock: ReentrantCoroutineLock, id: String) {
    println("[$id] Attempting to acquire lock...")
    lock.withLock {
        println("[$id] 🔒 Lock acquired! Accessing resource...")
        delay(100)

        // Demonstrate reentrancy - same coroutine can acquire again
        println("[$id] Attempting reentrant lock...")
        lock.withLock {
            println("[$id] 🔒🔒 Reentrant lock acquired! Inner critical section...")
            delay(50)
            println("[$id] 🔓 Releasing reentrant lock...")
        }
        println("[$id] 🔓 Releasing outer lock. Resource access complete.")
    }
}

/**
 * Advanced Example: Bank Account with Reentrant Lock
 * Demonstrates why reentrancy is crucial for complex operations
 */
class BankAccount(private var balance: Double = 0.0) {
    private val lock = ReentrantCoroutineLock()
    
    suspend fun deposit(amount: Double) {
        lock.withLock {
            println("Depositing $amount")
            balance += amount
            logTransaction("DEPOSIT", amount) // This calls getBalance() - needs reentrancy!
        }
    }
    
    suspend fun withdraw(amount: Double): Boolean {
        return lock.withLock {
            if (balance >= amount) {
                println("Withdrawing $amount")
                balance -= amount
                logTransaction("WITHDRAW", amount)
                true
            } else {
                false
            }
        }
    }
    
    suspend fun getBalance(): Double {
        return lock.withLock { // Reentrant: can be called from deposit/withdraw
            balance
        }
    }
    
    private suspend fun logTransaction(type: String, amount: Double) {
        // This method calls getBalance() while already holding the lock
        val currentBalance = getBalance() // Reentrancy needed here!
        println("$type: $amount, New Balance: $currentBalance")
    }
}

// ============================================================================
// DEMONSTRATION FUNCTION
// ============================================================================

suspend fun demonstrateSemaphores() {
    println("=== Binary Semaphore Demo ===")
    
    val binaryExample = BinarySemaphoreExample()
    coroutineScope {
        repeat(3) { i ->
            launch {
                binaryExample.criticalSection("Coroutine-$i")
            }
        }
    }
    
    println("\n=== Reentrant Lock Demo ===")
    val lock = ReentrantCoroutineLock()
    coroutineScope {
        repeat(2) { i ->
            launch {
                accessResource(lock, "Worker-$i")
            }
        }
    }
    
    println("\n=== Bank Account Demo ===")
    val account = BankAccount(100.0)
    coroutineScope {
        launch { account.deposit(50.0) }
        launch { account.withdraw(30.0) }
        launch { println("Final balance: ${account.getBalance()}") }
    }
}

fun main() = runBlocking {
    demonstrateSemaphores()
}