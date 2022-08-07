# knx-to-amqp
With this app you can transfer knx telegrams to RabbitMQ

The app receives a telegram from a KNX gateway, converts it to JSON and delivers it to an exchange. The routing key is the destination address of the telegram.

## KNX Telegram structure
The converted KNX telegram has the following fields:

| field       | description                                                                                    |
|-------------|------------------------------------------------------------------------------------------------|
| source      | The telegram source device address                                                             |
| destination | The destination 3-digit group address                                                          |
| date        | The date, when the message was received in the format `DateTimeFormatter.ISO_OFFSET_DATE_TIME` |
| data        | The telegrams data, formatted in base64                                                        |

An example looks like this:
```json
{
   "source":"1.2.3",
   "destination":"4/5/6",
   "date":"2022-08-07T21:40:00.253458649+02:00",
   "data":"Da0="
}
```

## RabbitMQ configuration
Make sure you create the exchange before you start the app. And don't forget to configure your queues and bindings. 

## Build
### Docker image
```shell
docker buildx build --tag mhert/knx-to-amqp:latest .
```

## Usage
### Docker
```shell
docker run mhert/knx-to-amqp:latest
```

## Configuration
You can control the app via environment variables or arguments.

### Required config
| env var              | argument             | used for                                    |
|----------------------|----------------------|---------------------------------------------|
| KNX_GATEWAY_ADDRESS  | --knxGatewayAddress  | Connection to KNX                           |
| AMQP_ADDRESS         | --amqpAddress        | Connection to RabbitMQ                      |
| TARGET_EXCHANGE_NAME | --targetExchangeName | Target exchange for converted knx telegrams |

### Optional config
| env var          | argument         | used for               |
|------------------|------------------|------------------------|
| KNX_GATEWAY_PORT | --knxGatewayPort | Connection to KNX      |
| AMQP_PORT        | --amqpPort       | Connection to RabbitMQ |
