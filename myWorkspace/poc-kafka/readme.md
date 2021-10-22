# introduction

This project uses Kafka and Avro. 

To generate the sources, start with a compile command. Avro indeed uses an avc file to generate java sources.
You can then mark the target/generated-sources folder as 'Generated Sources Root' to have Intellij detect the sources.

# exercise

Start the docker stack:

    docker-compose up

# tips

Using the processor of exercise 1, it is possible to have a generic consumer:

    @KafkaListener(topics = {“ReservationCommand”, “MemberRegistrationCommand”}
    public void onNewCommand(AbstractCommand command) {
        processor.process(command);
    }