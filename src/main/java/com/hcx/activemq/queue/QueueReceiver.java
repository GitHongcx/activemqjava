package com.hcx.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.xml.soap.Text;

/**
 * 点对点消息接收者
 * Created by hongcaixia on 2019/7/14.
 */
public class QueueReceiver {

    //消息服务器的连接地址
    public static final String BROKER_URL = "tcp:120.79.91.143:61616";

    //消息目的地的名称
    public static final String DESTINATION_NAME = "myQueue";

    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageConsumer messageConsumer = null;

        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        try {
            //2.创建连接
            connection = connectionFactory.createConnection();

            //在接收消息之前，需要把连接启动
            connection.start();

            //3.创建session JMS1.1规范
            /**
             * Boolean.FALSE：非事务消息
             * Session.AUTO_ACKNOWLEDGE：消息确认机制
             */
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            //4.创建目的地
            Destination destination = session.createQueue(DESTINATION_NAME);

            //5.创建消息消费者
            messageConsumer = session.createConsumer(destination);


            //6.接收消息 receive()为阻塞方法，会一直等 直到等到消息为止
            Message receiveMessage = messageConsumer.receive();

            //判断是否为文本消息
            if(receiveMessage instanceof TextMessage){
                String text = ((TextMessage) receiveMessage).getText();
                System.out.println("接收到的消息是："+text);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null!=messageConsumer){
                    messageConsumer.close();
                }
                if(null!=session){
                    session.close();
                }
                if(null!=connection){
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
