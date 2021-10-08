interface MessagingService {
/* ... */

    val serviceVisitorsStats: ServiceVisitorsStats?
    val loggingService: LoggingService?

    fun handleRequest(clientId: String) {
        loggingService?.log("Request from $clientId")

        serviceVisitorsStats?.let {
            it.registerVisit(clientId)
            it.visitorsCounter?.let { secondIt ->
                loggingService?.log("Visitors count: ${secondIt.uniqueVisitorsCount}")
            }
        }
    }
}

interface ServiceVisitorsStats {
/* ... */

    val visitorsCounter: VisitorsCounter?

    fun registerVisit(clientId: String) {
        visitorsCounter?.registerVisit(clientId)
    }
}

interface LoggingService {
    fun log(logMessage: String)
}

class VisitorsCounter {
    var uniqueVisitorsCount: Int = 0
        private set

    fun registerVisit(clientId: String) {
// Пока что считаем всех посетителей уникальными
        uniqueVisitorsCount++
    }
}