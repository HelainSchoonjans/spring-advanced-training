package com.acme.poc.kafka;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;



public class WithJson {

	public static class ReservationCommand {
		public int bookId;
		public LocalDate pickupDate, returnDate;
		public String username;
	}

	public static void main(String[] args) throws SQLException {

		String topicName = ReservationCommand.class.getSimpleName();

		Properties producerProps = new Properties();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		

		try (KafkaProducer<String, ReservationCommand> producer = new KafkaProducer<>(producerProps)) {
			for (int i = 1; i <= 100; i++) {
				var command = new ReservationCommand();
				command.username = "jdoe";
				command.bookId = 1;
				command.pickupDate = LocalDate.now().plusDays(1);
				command.returnDate = LocalDate.now().plusDays(7);

				Future<RecordMetadata> future = producer.send(new ProducerRecord<>(topicName, "jane.doe", command));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Properties consumerProps = new Properties();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "poc-kafka-json-consumer");
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		//consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		
		try (KafkaConsumer<String, ReservationCommand> consumer = new KafkaConsumer<>(consumerProps)) {

			consumer.subscribe(List.of(topicName));
			while (true) {
				ConsumerRecords<String, ReservationCommand> records = consumer.poll(Duration.ofSeconds(5));
				System.out.println(records.count());
				for (ConsumerRecord<String, ReservationCommand> consumerRecord : records) {
					System.out.println(consumerRecord.value() + " " + consumerRecord.offset());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
