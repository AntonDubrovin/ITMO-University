import java.util.concurrent.locks.ReentrantLock

/**
 * Bank implementation.
 *
 * :TODO: This implementation has to be made thread-safe.
 *
 * @author : Dubrovin Anton
 */
class BankImpl(n: Int) : Bank {
    private val accounts: Array<Account> = Array(n) { Account() }

    override val numberOfAccounts: Int
        get() = accounts.size

    /**
     * :TODO: This method has to be made thread-safe.
     */
    override fun getAmount(index: Int): Long {
        accounts[index].lock.lock()
        val amount = accounts[index].amount
        accounts[index].lock.unlock()
        return amount
    }

    /**
     * :TODO: This method has to be made thread-safe.
     */
    override val totalAmount: Long
        get() {
            for (account in accounts) {
                account.lock.lock()
            }
            var sum = 0L
            for (account in accounts) {
                sum += account.amount
            }
            for (account in accounts) {
                account.lock.unlock()
            }
            return sum
        }

    /**
     * :TODO: This method has to be made thread-safe.
     */
    override fun deposit(index: Int, amount: Long): Long {
        require(amount > 0) { "Invalid amount: $amount" }
        accounts[index].lock.lock()
        val account = accounts[index]
        var result = 0L
        try {
            check(!(amount > Bank.MAX_AMOUNT || account.amount + amount > Bank.MAX_AMOUNT)) { "Overflow" }
            account.amount += amount
            result += amount
            return result
        } finally {
            accounts[index].lock.unlock()
        }
    }

    /**
     * :TODO: This method has to be made thread-safe.
     */
    override fun withdraw(index: Int, amount: Long): Long {
        require(amount > 0) { "Invalid amount: $amount" }
        accounts[index].lock.lock()
        val account = accounts[index]
        var result = account.amount
        try {
            check(account.amount - amount >= 0) { "Underflow" }
            account.amount -= amount
            result -= amount
            return result
        } finally {
            accounts[index].lock.unlock()
        }
    }

    /**
     * :TODO: This method has to be made thread-safe.
     */
    override fun transfer(fromIndex: Int, toIndex: Int, amount: Long) {
        require(amount > 0) { "Invalid amount: $amount" }
        require(fromIndex != toIndex) { "fromIndex == toIndex" }
        accounts[toIndex.coerceAtMost(fromIndex)].lock.lock()
        accounts[toIndex.coerceAtLeast(fromIndex)].lock.lock()
        val from = accounts[fromIndex]
        val to = accounts[toIndex]
        try {
            check(amount <= from.amount) { "Underflow" }
            check(!(amount > Bank.MAX_AMOUNT || to.amount + amount > Bank.MAX_AMOUNT)) { "Overflow" }
            from.amount -= amount
            to.amount += amount
        } finally {
            accounts[fromIndex].lock.unlock()
            accounts[toIndex].lock.unlock()
        }

    }

    /**
     * Private account data structure.
     */
    class Account {
        /**
         * Amount of funds in this account.
         */
        val lock: ReentrantLock = ReentrantLock()
        var amount: Long = 0
    }
}