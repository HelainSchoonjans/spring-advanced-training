# exercise

Start the docker stack:

    docker-compose up

# tips

Using the processor of exercise 1, it is possible to have a generic consumer:

    @KafkaListener(topics = {“ReservationCommand”, “MemberRegistrationCommand”}
    public void onNewCommand(AbstractCommand command) {
        processor.process(command);
    }