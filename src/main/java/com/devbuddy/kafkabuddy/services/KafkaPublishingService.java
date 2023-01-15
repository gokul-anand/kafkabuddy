package com.devbuddy.kafkabuddy.services;

import java.util.Map;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaPublishingService {
   
    @Autowired
	private KafkaTemplate<String, String> template;

    public void publishMessage(String topic, String message, Map<String, String> headers) {
        ProducerRecord<String,String> producerRecord = new ProducerRecord<String,String>(topic, message);
        headers.forEach((key, value) -> {
            producerRecord.headers().add(key, value.getBytes());
        });
        this.template.send(producerRecord);
    }

}
