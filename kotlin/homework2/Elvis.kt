@file:Suppress("FoldInitializerAndIfToElvis")

/**
 * TODO Упростите эту функцию так, чтобы в ней осталось не более одного утверждения 'if'.
 */
fun sendMessageToClient(
    client: Client?,
    message: String?,
    mailer: Mailer
) {
    val email = client?.personalInfo?.email ?: return
    mailer.sendMessage(email, message ?: return)
}

class Client(val personalInfo: PersonalInfo?)

class PersonalInfo(val email: String?)

interface Mailer {
    fun sendMessage(email: String, message: String)
}
