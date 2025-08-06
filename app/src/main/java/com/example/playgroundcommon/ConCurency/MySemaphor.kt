// Import all coroutine-related classes and functions (launch, async, delay, etc.)
import kotlinx.coroutines.*
// Import the Semaphore class - our core synchronization primitive
import kotlinx.coroutines.sync.Semaphore
// Import atomic operations for thread-safe variables (KMM compatible)
import kotlinx.atomicfu.atomic

/**
 * Binary Semaphore Mental Model:
 * - Think of it as a single parking space with a queue
 * - Only ONE coroutine can "park" (hold the lock) at a time
 * - Others wait in line until the space is free
 * - Reentrant = same coroutine can "re-park" multiple times
 */
class ReentrantCoroutineLock {
    // Create a binary semaphore with 1 permit (1 = unlocked, 0 = locked)
    // This is the "parking space" - only one coroutine can occupy it
    private val semaphore = Semaphore(permits = 1)
    
    // Atomic reference to track which coroutine Job currently owns the lock
    // Uses atomicfu for thread-safe operations across all platforms (KMM)
    private val owner = atomic<Job?>(null)
    
    // Atomic counter tracking how many times the owner has acquired the lock
    // Enables reentrancy - same coroutine can lock multiple times
    private val reentrancyCount = atomic(0)

    // Suspend function to acquire the lock (may suspend if lock is held)
    suspend fun lock() {
        // Get the current coroutine's Job - this uniquely identifies the coroutine
        val currentJob = currentCoroutineContext()[Job]
        
        // REENTRANCY CHECK: If current coroutine already owns the lock
        if (owner.value == currentJob) {
            // Increment the reentrancy counter atomically (thread-safe)
            reentrancyCount.incrementAndGet()
            // Early return - no need to acquire semaphore again
            return
        }
        
        // FIRST-TIME ACQUISITION: Try to acquire the binary semaphore
        // This will SUSPEND the coroutine if another coroutine holds the lock
        // The coroutine joins a FIFO queue and waits its turn
        semaphore.acquire()
        
        // SUCCESS! We now own the lock - update ownership atomically
        owner.value = currentJob
        // Set reentrancy count to 1 (first acquisition)
        reentrancyCount.value = 1
    }

    // Suspend function to release the lock
    suspend fun unlock() {
        // Get current coroutine's Job to verify ownership
        val currentJob = currentCoroutineContext()[Job]
        
        // OWNERSHIP VERIFICATION: Only the owner can unlock
        if (owner.value != currentJob) {
            // Throw exception if non-owner tries to unlock (programming error)
            throw IllegalStateException("Lock not owned by current coroutine")
        }

          val newCount = reentrancyCount.decrementAndGet()
        
        // FULL UNLOCK CHECK: Only release semaphore when count reaches 0
        if (newCount == 0) {
            owner.value = null
            // Release the binary semaphore - allows next waiting coroutine to proceed
            // This wakes up the next coroutine in the FIFO queue
            semaphore.release()
        }
        // If newCount > 0, we still own the lock (nested acquisition)
    }

    // Generic suspend function implementing the "loan pattern"
    // Automatically acquires lock, executes action, then releases lock
    suspend fun <T> withLock(action: suspend () -> T): T {
        // Acquire the lock (may suspend if unavailable)
        lock()
        // Use try-finally to GUARANTEE unlock even if action throws exception
        try {
            // Execute the user's critical section code
            return action()
        } finally {
            // ALWAYS unlock, even if action() throws an exception
            // This prevents deadlocks from uncaught exceptions
            unlock()
        }
    }
    
    // Debug property: Check if lock is currently held by anyone
    // availablePermits = 0 means someone holds the lock
    val isLocked: Boolean get() = semaphore.availablePermits == 0
    
    // Debug property: Get the Job of the coroutine that owns the lock
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
    // Create binary semaphore: 1 permit means "turnstile is open"
    private val semaphore = Semaphore(1) // Binary: 0 or 1 permits
    
    // Function demonstrating critical section protection
    suspend fun criticalSection(id: String) {
        // Print that we're about to wait (may not wait if semaphore is available)
        println("[$id] Waiting for semaphore...")
        
        // Try to acquire the semaphore - SUSPENDS if not available
        // This is like trying to pass through a turnstile
        semaphore.acquire() // Will block if permit not available
        
        // Use try-finally to ensure we ALWAYS release the semaphore
        try {
            // WE'RE IN THE CRITICAL SECTION - only one coroutine can be here
            println("[$id] 🔒 Entered critical section")
            
            // Simulate some work in the critical section
            delay(1000) // Simulate work
            
            // About to leave the critical section
            println("[$id] 🔓 Leaving critical section")
        } finally {
            // CRITICAL: Always release the semaphore, even if exception occurs
            // This opens the turnstile for the next waiting coroutine
            semaphore.release() // Always release!
        }
    }
}

/**
 * Example 2: Resource Pool with Binary Semaphore
 * Mental Model: Single shared resource (printer, database connection, etc.)
 */
class SharedResourceManager {
    // Binary semaphore protecting access to the shared resource
    private val resourceLock = Semaphore(1)
    
    // The shared resource - only one coroutine can use it at a time
    private var resource: String? = null
    
    // Function to safely use the shared resource
    suspend fun useResource(userId: String): String {
        // Acquire exclusive access to the resource
        resourceLock.acquire()
        
        try {
            // CRITICAL SECTION: Only one coroutine can execute this code
            
            // Lazy initialization - create resource if it doesn't exist
            if (resource == null) {
                println("[$userId] Creating resource...")
                // Simulate expensive resource creation (database connection, etc.)
                delay(500) // Simulate expensive resource creation
                
                // Create the resource with a unique timestamp
                resource = "SharedResource_${System.currentTimeMillis()}"
            }
            
            // Use the shared resource
            println("[$userId] Using resource: $resource")
            
            // Simulate work with the resource
            delay(200) // Simulate work with resource
            
            // Return the resource identifier
            return resource!!
        } finally {
            // ALWAYS release the lock when done with the resource
            resourceLock.release()
        }
    }
}

/**
 * Example 3: Producer-Consumer with Binary Semaphore
 * Mental Model: Single buffer slot
 */
class SingleSlotBuffer<T> {
    // Semaphore tracking empty slots (starts with 1 empty slot)
    private val empty = Semaphore(1) // Buffer starts empty
    
    // Semaphore tracking full slots (starts with 0 full slots)
    private val full = Semaphore(0)  // No items initially
    
    // The actual buffer - can hold one item
    private var buffer: T? = null
    
    // Producer function - adds item to buffer
    suspend fun produce(item: T) {
        // Wait for an empty slot (will suspend if buffer is full)
        empty.acquire() // Wait for empty slot
        
        // CRITICAL: We now have exclusive access to the empty buffer
        // Store the item in the buffer
        buffer = item
        println("Produced: $item")
        
        // Signal that buffer now contains an item
        // This wakes up any consumer waiting for a full slot
        full.release() // Signal item available
    }
    
    // Consumer function - removes item from buffer
    suspend fun consume(): T {
        // Wait for a full slot (will suspend if buffer is empty)
        full.acquire() // Wait for item
        
        // CRITICAL: We now have exclusive access to the full buffer
        // Extract the item from the buffer
        val item = buffer!!
        
        // Clear the buffer slot
        buffer = null
        println("Consumed: $item")
        
        // Signal that buffer is now empty
        // This wakes up any producer waiting for an empty slot
        empty.release() // Signal slot empty
        
        // Return the consumed item
        return item
    }
}

// ============================================================================
// REENTRANT LOCK EXAMPLES
// ============================================================================

// Function demonstrating nested lock acquisition (reentrancy)
suspend fun accessResource(lock: ReentrantCoroutineLock, id: String) {
    // Print that we're about to try acquiring the lock
    println("[$id] Attempting to acquire lock...")
    
    // Use the lock to protect a critical section
    lock.withLock {
        // OUTER CRITICAL SECTION - we now own the lock
        println("[$id] 🔒 Lock acquired! Accessing resource...")
        
        // Simulate some work in the outer critical section
        delay(100)

        // NESTED LOCK ACQUISITION - demonstrate reentrancy
        println("[$id] Attempting reentrant lock...")
        
        // Same coroutine acquiring the lock again - this should NOT deadlock
        lock.withLock {
            // INNER CRITICAL SECTION - reentrancy count is now 2
            println("[$id] 🔒🔒 Reentrant lock acquired! Inner critical section...")
            
            // Simulate work in the inner critical section
            delay(50)
            
            // About to release the inner lock (reentrancy count becomes 1)
            println("[$id] 🔓 Releasing reentrant lock...")
        } // Inner lock automatically released here
        
        // Back in outer critical section - reentrancy count is 1 again
        println("[$id] 🔓 Releasing outer lock. Resource access complete.")
    } // Outer lock automatically released here - reentrancy count becomes 0
}

/**
 * Advanced Example: Bank Account with Reentrant Lock
 * Demonstrates why reentrancy is crucial for complex operations
 */
class BankAccount(private var balance: Double = 0.0) {
    // Reentrant lock protecting the account balance
    private val lock = ReentrantCoroutineLock()
    
    // Method to deposit money into the account
    suspend fun deposit(amount: Double) {
        // Acquire lock to ensure atomic operation
        lock.withLock {
            // CRITICAL SECTION: Modify balance safely
            println("Depositing $amount")
            
            // Update the balance
            balance += amount
            
            // Log the transaction - THIS CALLS getBalance() WHICH NEEDS THE LOCK!
            // Without reentrancy, this would deadlock because getBalance() tries to acquire
            // the same lock that deposit() already holds
            logTransaction("DEPOSIT", amount) // This calls getBalance() - needs reentrancy!
        }
    }
    
    // Method to withdraw money from the account
    suspend fun withdraw(amount: Double): Boolean {
        // Use lock and return the result of the critical section
        return lock.withLock {
            // CRITICAL SECTION: Check balance and withdraw if sufficient funds
            
            // Check if we have enough money
            if (balance >= amount) {
                println("Withdrawing $amount")
                
                // Deduct the amount
                balance -= amount
                
                // Log the transaction (again, needs reentrancy for getBalance())
                logTransaction("WITHDRAW", amount)
                
                // Return success
                true
            } else {
                // Insufficient funds - return failure
                false
            }
        }
    }
    
    // Method to safely read the current balance
    suspend fun getBalance(): Double {
        // Even reading needs the lock for consistency
        return lock.withLock { // Reentrant: can be called from deposit/withdraw
            // Return the current balance
            balance
        }
    }
    
    // Private method to log transactions
    private suspend fun logTransaction(type: String, amount: Double) {
        // This method calls getBalance() while already holding the lock
        // REENTRANCY IS ESSENTIAL HERE - without it, this would deadlock
        
        // Get current balance - this requires acquiring the lock again!
        val currentBalance = getBalance() // Reentrancy needed here!
        
        // Print the transaction log
        println("$type: $amount, New Balance: $currentBalance")
    }
}

// ============================================================================
// DEMONSTRATION FUNCTION
// ============================================================================

// Main demonstration function showing all examples
suspend fun demonstrateSemaphores() {
    // === DEMO 1: Basic Binary Semaphore ===
    println("=== Binary Semaphore Demo ===")
    
    // Create an instance of the basic semaphore example
    val binaryExample = BinarySemaphoreExample()
    
    // Create a coroutine scope to launch multiple coroutines
    coroutineScope {
        // Launch 3 coroutines that will compete for the semaphore
        repeat(3) { i ->
            // Each coroutine runs concurrently
            launch {
                // All coroutines try to enter the critical section
                // Only one can succeed at a time due to the binary semaphore
                binaryExample.criticalSection("Coroutine-$i")
            }
        }
        // coroutineScope waits for ALL child coroutines to complete
    }
    
    // === DEMO 2: Reentrant Lock ===
    println("\n=== Reentrant Lock Demo ===")
    
    // Create a shared reentrant lock
    val lock = ReentrantCoroutineLock()
    
    // Create another coroutine scope
    coroutineScope {
        // Launch 2 worker coroutines
        repeat(2) { i ->
            launch {
                // Each worker will acquire the lock and demonstrate reentrancy
                accessResource(lock, "Worker-$i")
            }
        }
        // Wait for both workers to complete
    }
    
    // === DEMO 3: Complex Reentrancy with Bank Account ===
    println("\n=== Bank Account Demo ===")
    
    // Create a bank account with initial balance of $100
    val account = BankAccount(100.0)
    
    // Create a coroutine scope for concurrent banking operations
    coroutineScope {
        // Launch deposit operation
        launch { account.deposit(50.0) }
        
        // Launch withdrawal operation (runs concurrently with deposit)
        launch { account.withdraw(30.0) }
        
        // Launch balance inquiry (runs concurrently with other operations)
        launch { println("Final balance: ${account.getBalance()}") }
        
        // All operations are thread-safe due to the reentrant lock
    }
}

fun main() = runBlocking {

    demonstrateSemaphores()
}