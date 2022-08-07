package domain.amqp

interface AmqpExchange {
    fun publish(routingKey: String, message: String)
}
