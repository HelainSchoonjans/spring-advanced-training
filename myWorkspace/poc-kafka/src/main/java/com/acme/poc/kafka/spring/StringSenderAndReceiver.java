package com.acme.poc.kafka.spring;

import java.time.Instant;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component @Profile("kafka-string")
public class StringSenderAndReceiver {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Scheduled(fixedDelay = 1000)
	public void sendMessageEverySecond() {
		this.kafkaTemplate.send("greetings", "hello at "+Instant.now().toString())
			.completable()
			.thenAccept(insertedRecord-> System.out.println(insertedRecord.getRecordMetadata().offset()));
	}

	@KafkaListener(
			topics = "greetings", 
			groupId = "poc-kafka-string-consumer", 
			properties = {
					//"auto.offset.reset=earliest"
			})
	public void onMessage(ConsumerRecord<String, String>  record) {
		//System.out.println(record);
		System.out.println(record.value()+" "+record.offset());
	}	
}
