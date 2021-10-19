package com.acme.poc.kafka;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
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

public class WithString {

	public static void main(String[] args) throws SQLException {

		String topicName = "greetings";
		
		Properties producerProps = new Properties();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		
		try(KafkaProducer<String, String> producer = new KafkaProducer<String, String>(producerProps)) {
			
			for (int i = 0; i < 100; i++) {
				Future<RecordMetadata> future = producer.send(new ProducerRecord<>(topicName, "hello from Java "+Instant.now().toString()));	
				System.out.println(future.get().offset());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	    Properties consumerProps = new Properties();
	    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");	 
	    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "poc-kafka-json-consumerx");
	    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());	 
	    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());	 
	    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
	    
	    
	    try(KafkaConsumer<String, String> consumer =  new KafkaConsumer<String, String>(consumerProps)) {
	    	
	    	consumer.subscribe(List.of(topicName));
	    	while(true) {
			   	ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
			   	System.out.println(records.count());
			   	for (ConsumerRecord<String, String> consumerRecord : records) {
					System.out.println(consumerRecord.value()+" "+consumerRecord.offset());
				}
	    	}
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
}
