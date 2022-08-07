package infrastructure.knx.logging

import domain.knx.KnxGateway
import domain.knx.KnxTelegram
import org.slf4j.Logger

class LoggingKnxGatewayFacade(
    private val logger: Logger,
    private val knxGateway: KnxGateway
) : KnxGateway {
    override fun listen(listener: (message: KnxTelegram) -> Unit) {
        knxGateway.listen { message: KnxTelegram ->
            logger.info("Received KnxTelegram - telegram:{}", message.toString())
        }

        knxGateway.listen(listener)
    }
}
