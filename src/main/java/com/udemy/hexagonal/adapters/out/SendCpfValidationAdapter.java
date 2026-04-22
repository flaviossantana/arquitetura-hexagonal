package com.udemy.hexagonal.adapters.out;

import com.udemy.hexagonal.application.ports.out.SendCpfForValidationOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendCpfValidationAdapter implements SendCpfForValidationOutputPort {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${hexagonal.message.producer.topic.cpf.validation}")
    private String topicCpfValidation;


    @Override
    public void send(String cpf) {
        log.info("Sending CPF for validation: {}", cpf);
        kafkaTemplate.send(topicCpfValidation, cpf);
        log.info("CPF sent for validation");
    }
}
