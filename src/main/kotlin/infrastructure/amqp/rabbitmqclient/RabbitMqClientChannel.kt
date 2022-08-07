package infrastructure.amqp.rabbitmqclient

import com.rabbitmq.client.Channel

class RabbitMqClientChannel(private val channel: Channel) {
    fun publish(exchange: String, routingKey: String, message: String) {
        channel.basicPublish(
            exchange,
            routingKey,
            null,
            message.toByteArray(Charsets.UTF_8)
        )
    }
}
