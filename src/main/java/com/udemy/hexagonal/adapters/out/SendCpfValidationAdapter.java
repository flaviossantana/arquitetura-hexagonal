package com.udemy.hexagonal.adapters.out;

import com.udemy.hexagonal.application.ports.out.SendCpfForValidationOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendCpfValidationAdapter implements SendCpfForValidationOutputPort {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${hexagonal.message.producer.topic.cpf.validation}")
    private String topicCpfValidation;


    @Override
    public void send(String cpf) {
        kafkaTemplate.send(topicCpfValidation, cpf);
    }
}
