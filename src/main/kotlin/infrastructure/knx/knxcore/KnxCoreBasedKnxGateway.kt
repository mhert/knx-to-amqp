package infrastructure.knx.knxcore

import domain.knx.KnxGateway
import domain.knx.KnxTelegram
import li.pitschmann.knx.core.CEMIAware
import li.pitschmann.knx.core.address.GroupAddress
import li.pitschmann.knx.core.body.Body
import li.pitschmann.knx.core.cemi.CEMI
import li.pitschmann.knx.core.communication.DefaultKnxClient
import li.pitschmann.knx.core.communication.KnxClient
import li.pitschmann.knx.core.config.Config
import li.pitschmann.knx.core.config.ConfigBuilder
import li.pitschmann.knx.core.plugin.ObserverPlugin
import java.net.InetAddress
import java.time.OffsetDateTime

typealias listener = (message: KnxTelegram) -> Unit

class KnxCoreBasedKnxGateway(
    knxGatewayAddress: String,
    knxGatewayPort: Int
) : KnxGateway {
    private val listeners = mutableListOf<listener>()
    private val config: Config = ConfigBuilder
        .tunneling(InetAddress.getByName(knxGatewayAddress), knxGatewayPort, true)
        .plugin(object : ObserverPlugin {
            override fun onInitialization(client: KnxClient?) {}

            override fun onIncomingBody(item: Body?) {
                if (item is CEMIAware) {
                    listeners.forEach {listener ->
                        listener(cemiToKnxTelegram(item.cemi))
                    }
                }
            }

            override fun onOutgoingBody(item: Body?) {}
        })
        .build()

    private val client = DefaultKnxClient.createStarted(config)

    override fun listen(listener: (message: KnxTelegram) -> Unit) {
        listeners.add(listener)
    }

    private fun cemiToKnxTelegram(cemi: CEMI): KnxTelegram {
        return KnxTelegram(
            source = cemi.sourceAddress.address,
            destination = (cemi.destinationAddress as GroupAddress).addressLevel3,
            date = OffsetDateTime.now(),
            data = cemi.data
        )
    }
}
