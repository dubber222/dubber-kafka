package com.dubber.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.util.Properties;

/**
 * 循环发送
 *
 * @author dubber
 */
public class KafkaProducerWithLoop implements Runnable {

    private final org.apache.kafka.clients.producer.KafkaProducer<Integer, String> producer;

    public KafkaProducerWithLoop() {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaProperties.KAFKA_BROKER_LIST);
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("partitioner.class", "com.dubber.kafka.MyPartition");
        props.put("client.id", "producerDemo");
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

    @Override
    public void run() {
        int messageNo = 0;
        while (true) {
            String messageStr = "message-" + messageNo;
            producer.send(new ProducerRecord<>(KafkaProperties.TOPIC, messageNo, messageStr),
                    (RecordMetadata recordMetadata, Exception e) ->
                            System.out.println("message send to:[" + recordMetadata.partition() + "],offset:[" + recordMetadata.offset() + "]")
            );
            ++messageNo;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        KafkaProducerWithLoop producer = new KafkaProducerWithLoop();
        new Thread(producer).start();
    }
}
