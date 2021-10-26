# Синхронизация с помощью тонкой блокировки

Задание включает в себя следующие исходные файлы:

* [`src/Bank.kt`](src/Bank.kt) содержит интерфейс для гипотетического банка.
* [`src/BankImpl.kt`](src/BankImpl.kt) содержит реализацию операций банка для однопоточного случая.
  Данная реализация небезопасна для использования из нескольких потоков одновременно.
  * Альтернативная реализация на Java дана в [`java/BankImpl.java`](java/BankImpl.java).   

Необходимо доработать реализую `BackImpl` так, чтобы она стала безопасной для использования из множества потоков одновременно. 

* Для реализации необходимо использовать тонкую блокировку. 
  * Синхронизация должна осуществляться для каждого счета по отдельности. 
  * Добавьте поле `lock` типа `java.util.concurrent.locks.ReentrantLock` (класс для примитива блокировки) в класс `BankImpl.Account`
    и используйте его для блокировки операций над соответствующим счетом.
  * В Kotlin можно использовать [`lock.withLock { ... }`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.concurrent/java.util.concurrent.locks.-lock/with-lock.html)
    для работы с одним или двумя счетами, но для блокировки всех счетов потребуется напрямую использовать методы `lock()` и `unlock()`.
* Для обеспечения линеаризуемости операций должна использоваться двухфазная блокировка для всех операций.
* Для избегания ситуации взаимной блокировки (deadlock) необходимо использовать иерархическую блокировку.
* Весь код должен содержаться в файле `src/BankImpl.kt` или `src/BankImpl.java`. 
  * В случае использования Java, удалите `src/BankImpl.kt` и скопируйте `java/BankImpl.java` в `src/BankImpl.java` в качестве шаблона кода. 
* **В заголовке файла, в строке `@author` впишите вашу фамилию и имя**.