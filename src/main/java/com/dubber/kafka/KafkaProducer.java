package com.dubber.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
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
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_BROKER_LIST);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 指定partition
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.dubber.kafka.MyPartition");
        // 标识消费者客户端
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<Integer, String>(props);
    }

    public static void main(String[] args) throws IOException {
        KafkaProducer producer = new KafkaProducer();
        producer.sendMsg();
        System.in.read();
    }

    public void sendMsg() {
        int num = 0;
        while (num < 50) {
            // 异步发送
            String message = "消息" + num;
            producer.send(new ProducerRecord<>(KafkaProperties.TOPIC, 1, "message"),
                    (RecordMetadata recordMetadata, Exception e) ->
                            System.out.println("message send to:[" + recordMetadata.partition() + "],offset:[" + recordMetadata.offset() + "]")
            );
            num++;
        }

    }
}
