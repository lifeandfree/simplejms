package ru.innopolis;

import static ru.innopolis.JmsProvider.getDestination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

class ExampleMessageSender {
    private Logger logger = LoggerFactory.getLogger(ExampleMessageSender.class);

    private final MessageProducer producer;
    private final Session session;
    private final Connection connection;

    ExampleMessageSender() throws JMSException {
        ConnectionFactory factory = JmsProvider.getConnectionFactory();
        connection = factory.createConnection();
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = getDestination(session);
        producer = session.createProducer(dest);
    }

    void sendMessage(String message) throws JMSException {
        logger.info("Sending message: {}", message);
        TextMessage textMessage = session.createTextMessage(message);
        producer.send(textMessage);
    }

    void destroy() throws JMSException {
        connection.close();
    }
}