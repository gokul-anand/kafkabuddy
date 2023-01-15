package com.devbuddy.kafkabuddy.services;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {
    
    private KafkaTemplate<String, String> template;

    private Map<String, Integer> topicOffsetMap = new HashMap<>();

    @Autowired
    public KafkaListenerService(KafkaTemplate<String, String> template) {
        this.template = template;
        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers","http://localhost:9092");
        this.template.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configs,new StringDeserializer(), new StringDeserializer()));
    }
    
    public ConsumerRecord<String, String> readMessage(String topic) {
        ConsumerRecord<String, String> consumerRecord;
        // TODO: improvise logic to handle partition & offset, if required
        int partition = 0;

        if(!topicOffsetMap.containsKey(topic)) {
            topicOffsetMap.put(topic, 0);
        };

        int offset = topicOffsetMap.get(topic);
        consumerRecord = this.template.receive(topic, partition, offset);
        topicOffsetMap.put(topic, offset+1);
        return consumerRecord;
    }
}
