package com.udemy.hexagonal.adapters.in.consumer;

import com.udemy.hexagonal.adapters.in.consumer.mapper.CustomerMessageMapper;
import com.udemy.hexagonal.adapters.in.consumer.message.CustomerMessage;
import com.udemy.hexagonal.application.core.domain.Customer;
import com.udemy.hexagonal.application.ports.in.UpdateCustomerInputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReceiveValidatedCpfConsumer {

    @Autowired
    private UpdateCustomerInputPort updateCustomerInputPort;

    @Autowired
    private CustomerMessageMapper customerMessageMapper;

    @KafkaListener(
            topics = "${hexagonal.message.producer.topic.cpf.validated}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void receive(CustomerMessage customerMessage) {
        log.info("Received validated CPF: {}", customerMessage);
        Customer customer = customerMessageMapper.toCustomer(customerMessage);
        log.info("Customer mapped: {}", customer);
        updateCustomerInputPort.updateCustomer(customer, customerMessage.getZipCode());
        log.info("Customer updated");
    }
}
