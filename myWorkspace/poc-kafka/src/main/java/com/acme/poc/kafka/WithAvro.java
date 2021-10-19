package com.acme.poc.kafka;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.acme.schemaregistry.avro.MemberRegistrationCommand;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class WithAvro {
	
	public static void main(String[] args) {		
		
		String topicName = MemberRegistrationCommand.class.getName();
		
		Properties producerProps = new Properties();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		producerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8085");
		
		try(KafkaProducer<String, MemberRegistrationCommand> producer = new KafkaProducer<>(producerProps)) {
			for (int i = 0; i < 100; i++) {
				MemberRegistrationCommand cmd = MemberRegistrationCommand.newBuilder()
					.setFirstname("John")
					.setLastname("Doe")
					.setUsername("jdoe")
					.setPassword("azerty")
					.build();
				Future<RecordMetadata> future = producer.send(new ProducerRecord<>(topicName, cmd));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	    Properties consumerProps = new Properties();
	    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");	 
	    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "poc-kafka-avro-consumer");
	    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());	 
	    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());	 
	    consumerProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8085");
	    
	    try(KafkaConsumer<String, MemberRegistrationCommand> consumer =  new KafkaConsumer<>(consumerProps)) {
	    	
	    	consumer.subscribe(List.of(topicName));
	    	while(true) {
			   	ConsumerRecords<String, MemberRegistrationCommand> records = consumer.poll(Duration.ofSeconds(5));
			   	System.out.println(records.count());
			   	for (ConsumerRecord<String, MemberRegistrationCommand> consumerRecord : records) {
					System.out.println(consumerRecord.value()+" "+consumerRecord.offset());
				}
	    	}
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
	}
}
