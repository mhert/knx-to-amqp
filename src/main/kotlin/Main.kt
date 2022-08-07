import domain.knx.KnxTelegram
import infrastructure.ArgOrEnvParser
import infrastructure.amqp.logging.LoggingAmqpExchangeFacade
import infrastructure.amqp.rabbitmqclient.RabbitMqClientBasedAmqpConnection
import infrastructure.amqp.rabbitmqclient.RabbitMqClientBasedAmqpExchange
import infrastructure.knx.knxcore.KnxCoreBasedKnxGateway
import infrastructure.knx.logging.LoggingKnxGatewayFacade
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val argEnvParser = ArgOrEnvParser("knx-to-amqp", args, System.getenv())

    val knxGatewayAddressConfig = argEnvParser.requiredString("knxGatewayAddress", "KNX_GATEWAY_ADDRESS")
    val knxGatewayPortConfig = argEnvParser.optionalInt("knxGatewayPort", "KNX_GATEWAY_PORT", 3671)

    val amqpAddressConfig = argEnvParser.requiredString("amqpAddress", "AMQP_ADDRESS")
    val amqpPortConfig = argEnvParser.optionalInt("amqpPort", "AMQP_PORT", 5672)

    val targetExchangeNameConfig = argEnvParser.requiredString("targetExchangeName", "TARGET_EXCHANGE_NAME")

    argEnvParser.parse()

    val knxGatewayAddress = knxGatewayAddressConfig.toString()
    val knxGatewayPort = knxGatewayPortConfig.toInt()

    val amqpAddress = amqpAddressConfig.toString()
    val amqpPort = amqpPortConfig.toInt()

    val targetExchangeName = targetExchangeNameConfig.toString()

    val logger = LoggerFactory.getLogger("Controller")

    val knxGateway = LoggingKnxGatewayFacade(
        logger,
        KnxCoreBasedKnxGateway(knxGatewayAddress, knxGatewayPort)
    )

    val amqpConnection = RabbitMqClientBasedAmqpConnection(amqpAddress, amqpPort)
    val amqpTargetExchange = LoggingAmqpExchangeFacade(
        logger,
        RabbitMqClientBasedAmqpExchange(amqpConnection, targetExchangeName)
    )

    knxGateway.listen { message: KnxTelegram ->
        amqpTargetExchange.publish(message.destination, message.toJson())
    }
}
