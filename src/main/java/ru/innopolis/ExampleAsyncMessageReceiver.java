package ru.innopolis;

import static ru.innopolis.JmsProvider.getDestination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ExampleAsyncMessageReceiver implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(ExampleAsyncMessageReceiver.class);

    private Connection connection;

    void startAsyncReceiver() throws JMSException {
        ConnectionFactory factory = JmsProvider.getConnectionFactory();

        connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = getDestination(session);
        session.createConsumer(dest).setMessageListener(this);
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            try {
                logger.info("Message received: {}", tm.getText());
            } catch (JMSException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }

    void destroy() throws JMSException {
        connection.close();
    }
}
