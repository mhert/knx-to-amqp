package infrastructure.amqp.logging

import domain.amqp.AmqpExchange
import org.slf4j.Logger

class LoggingAmqpExchangeFacade(
    private val logger: Logger,
    private val exchange: AmqpExchange
) : AmqpExchange {
    override fun publish(routingKey: String, message: String) {
        logger.info("Publish AMQP message - routingKey:{} message:{}", routingKey, message)

        exchange.publish(routingKey, message)
    }
}
