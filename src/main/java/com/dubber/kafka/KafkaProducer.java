package com.dubber.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.util.Properties;

/**
 * @author dubber
 */
public class KafkaProducer {

    private final org.apache.kafka.clients.producer.KafkaProducer<Integer, String> producer;

    public KafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaProperties.KAFKA_BROKER_LIST);
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 指定partition
        props.put("partitioner.class", "com.dubber.kafka.MyPartition");
        props.put("client.id", "producerDemo");
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<Integer, String>(props);
    }

    public void sendMsg() {
        producer.send(new ProducerRecord<>(KafkaProperties.TOPIC, 1, "message"),
                (RecordMetadata recordMetadata, Exception e) ->
                        System.out.println("message send to:[" + recordMetadata.partition() + "],offset:[" + recordMetadata.offset() + "]")
        );
    }

    public static void main(String[] args) throws IOException {
        KafkaProducer producer = new KafkaProducer();
        producer.sendMsg();
        System.in.read();
    }
}
