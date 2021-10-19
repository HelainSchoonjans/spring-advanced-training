package com.acme.poc.kafka.spring;

import java.time.LocalDate;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


class ReservationCommand {
	public int bookId;
	public LocalDate pickupDate, returnDate;
	public String username;	
}

@Component @Profile("kafka-json")
public class JsonSenderAndReceiver {
	
	@Autowired
	private KafkaTemplate<String, ReservationCommand> kafkaTemplate;
	
	@Scheduled(fixedDelay = 1000)
	public void sendMessageEverySecond() {
		var command = new ReservationCommand();
		command.username = "jdoe";
		command.bookId = 1;
		command.pickupDate = LocalDate.now().plusDays(1);
		command.returnDate = LocalDate.now().plusDays(7);
		this.kafkaTemplate.send("ReservationCommand", command)
			.completable()
			.thenAccept(insertedRecord-> System.out.println("message sent has offset "+insertedRecord.getRecordMetadata().offset()));
	}

	@KafkaListener(topics = "ReservationCommand", groupId = "poc-kafka-json-consumer")
	public void onMessage(ConsumerRecord<String, ReservationCommand>  record) {
		//System.out.println(record);
		ReservationCommand command = record.value();
		System.out.println("onMessage " +command+" "+record.offset());
	}
}

