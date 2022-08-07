package infrastructure.amqp.rabbitmqclient

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

class RabbitMqClientBasedAmqpConnection(
    amqpAddress: String,
    amqpPort: Int
) {
    private val connection: Connection

    init {
        val factory = ConnectionFactory()
        factory.host = amqpAddress
        factory.port = amqpPort

        connection = factory.newConnection()
    }

    fun channel(): RabbitMqClientChannel {
        return RabbitMqClientChannel(connection.createChannel())
    }
}
