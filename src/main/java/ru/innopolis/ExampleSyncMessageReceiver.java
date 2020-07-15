package ru.innopolis;

import static ru.innopolis.JmsProvider.getDestination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ExampleSyncMessageReceiver extends Thread {
    private Logger logger = LoggerFactory.getLogger(ExampleSyncMessageReceiver.class);

    private Connection connection;
    private MessageConsumer consumer;

    ExampleSyncMessageReceiver() throws JMSException {
        super("ExampleSyncMessageReceiver");
        setDaemon(true);

        ConnectionFactory factory = JmsProvider.getConnectionFactory();
        connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination dest = getDestination(session);
        consumer = session.createConsumer(dest);
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (i < 25) {
                final Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    TextMessage tm = (TextMessage) message;
                    logger.info("Message received: {}", tm.getText());
                }
                Thread.sleep(1000);
                logger.info("waiting");
                i++;
            }
            connection.close();
        } catch (JMSException | InterruptedException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

}