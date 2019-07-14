package com.hcx.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 发布订阅模式 发布者
 * Created by hongcaixia on 2019/7/14.
 */
public class TopicPublisher {

    //消息服务器的连接地址
    public static final String BROKER_URL = "tcp:120.79.91.143:61616";

    //消息目的地的名称
    public static final String DESTINATION_NAME = "myTopic";

    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;

        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        try {
            //2.创建连接
            connection = connectionFactory.createConnection();
            //3.创建session JMS1.1规范
            /**
             * Boolean.FALSE：非事务消息
             * Session.AUTO_ACKNOWLEDGE：消息确认机制
             */
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            //4.创建消息
            TextMessage textMessage = session.createTextMessage("文本消息：Hello ActiveMQ");

            //5.创建目的地
            Destination destination = session.createTopic(DESTINATION_NAME);

            //6.创建消息生产者
            messageProducer = session.createProducer(destination);

            //7.发送消息
            messageProducer.send(textMessage);

            System.out.println("消息发送完毕");

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null!=messageProducer){
                    messageProducer.close();
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
