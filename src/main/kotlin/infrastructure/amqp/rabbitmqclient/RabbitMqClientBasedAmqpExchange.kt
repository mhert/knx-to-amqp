package infrastructure.amqp.rabbitmqclient

import domain.amqp.AmqpExchange

class RabbitMqClientBasedAmqpExchange(
    private val connection: RabbitMqClientBasedAmqpConnection,
    private val exchange: String
) : AmqpExchange {
    private val channel = connection.channel()

    override fun publish(routingKey: String, message: String) {
        channel.publish(exchange, routingKey, message)
    }
}
