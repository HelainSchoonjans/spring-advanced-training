package com.acme.poc.kafka.spring;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.acme.schemaregistry.avro.MemberRegistrationCommand;

@Component @Profile("kafka-avro")
public class AvroSenderAndReceiver {

	@Autowired
	private KafkaTemplate<String, MemberRegistrationCommand> kafkaTemplate;
	
	@Scheduled(fixedDelay = 1000)
	public void sendMessageEverySecond() {
		MemberRegistrationCommand cmd = MemberRegistrationCommand.newBuilder()
				.setFirstname("John")
				.setLastname("Doe")
				.setUsername("jdoe")
				.setPassword("azerty")
				.build();
		
		this.kafkaTemplate.send("MemberRegistrationCommand", cmd)
			.completable()
			.thenAccept(insertedRecord-> System.out.println(insertedRecord.getRecordMetadata().offset()));
	}

	@KafkaListener(topics = "MemberRegistrationCommand", groupId = "poc-kafka-string-consumer")
	public void onMessage(ConsumerRecord<String, MemberRegistrationCommand>  record) {
		//System.out.println(record);
		System.out.println(record.value()+" "+record.offset());
	}	
}
