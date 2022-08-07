package domain.knx

interface KnxGateway {
    fun listen(listener: (message: KnxTelegram) -> Unit)
}
