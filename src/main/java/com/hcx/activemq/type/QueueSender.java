package com.hcx.activemq.type;

import com.alibaba.fastjson.JSONObject;
import com.hcx.activemq.model.Person;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 点对点发送方
 * Created by hongcaixia on 2019/7/14.
 */
public class QueueSender {

    //消息服务器的连接地址
    public static final String BROKER_URL = "tcp:120.79.91.143:61616";

    //消息目的地的名称
    public static final String DESTINATION_NAME = "myQueue";

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
            //TextMessage textMessage = session.createTextMessage("文本消息：Hello ActiveMQ");
            Person person = new Person();
            person.setName("小红");
            person.setPhone("13312344567");

            //创建一个对象消息
            //Message message = session.createObjectMessage(person);

            //把对象转成json格式数据
            String personJSON = JSONObject.toJSONString(person);
            TextMessage message = session.createTextMessage(personJSON);

            //创建一个映射消息
           /* MapMessage message = session.createMapMessage();
            message.setBoolean("booleanKey",true);
            message.setInt("intKey",520);
            message.setString("strKey","js_hcx");*/

           //创建一个字节消息
           /* BytesMessage message = session.createBytesMessage();
            message.writeBoolean(true);
            message.writeDouble(520.1314);
            message.writeUTF("CSDN_HCX");*/

            /*StreamMessage message = session.createStreamMessage();
            message.writeBoolean(true);
            message.writeDouble(520.1314);
            message.writeString("CSDN_HCX");*/

            //5.创建目的地
            Destination destination = session.createQueue(DESTINATION_NAME);

            //6.创建消息生产者
            messageProducer = session.createProducer(destination);

            //7.发送消息
            messageProducer.send(message);

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
