package com.dubber.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * @author dubber
 */
public class MyPartition implements Partitioner {


    /**
     * 通过分区的树龄
     * @param topic
     * @param key
     * @param bytes
     * @param o1
     * @param bytes1
     * @param cluster
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] bytes, Object o1,
                         byte[] bytes1, Cluster cluster) {
        List<PartitionInfo> partitionerList = cluster.partitionsForTopic(topic);
        //获得所有的分区
        int numPart = partitionerList.size();

        //获得key的 hashcode
        int hashCode = key.hashCode();

        return Math.abs(hashCode%numPart);
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> map) {
    }
}
