package com.hcx.activemq.type;

import com.alibaba.fastjson.JSONObject;
import com.hcx.activemq.model.Person;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

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
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        List<String> list = new ArrayList<String>();
        list.add("com.hcx.activemq.model");
        //设置受信任的包
        connectionFactory.setTrustedPackages(list);

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

                //解析json数据
                JSONObject jsonObject = JSONObject.parseObject(text);
                String name = jsonObject.getString("name");
                String phone = jsonObject.getString("phone");
                System.out.println("接收到的消息是："+name+","+phone);

                //直接把json字符串转成java对象
                Person person = JSONObject.parseObject(text,Person.class);
                System.out.println("接收到的消息是："+person.getName()+","+person.getPhone());

            }else if(receiveMessage instanceof ObjectMessage){
                Person person = (Person) ((ObjectMessage) receiveMessage).getObject();
                System.out.println("接收到的消息为："+person.getName()+" ,"+person.getPhone());
            }else if (receiveMessage instanceof MapMessage){
                boolean booleanValue = ((MapMessage) receiveMessage).getBoolean("booleanKey");
                int intValue = ((MapMessage) receiveMessage).getInt("intKey");
                String strValue = ((MapMessage) receiveMessage).getString("strKey");
                System.out.println(booleanValue+","+intValue+","+strValue);
            }else if(receiveMessage instanceof BytesMessage){
                //注意:读取顺序和写入顺序保持一致，否则会报错
                boolean booleanValue = ((BytesMessage) receiveMessage).readBoolean();
                double doubleValue  = ((BytesMessage) receiveMessage).readDouble();
                String strValue = ((BytesMessage) receiveMessage).readUTF();
                System.out.println(booleanValue+","+doubleValue+","+strValue);
            }else if(receiveMessage instanceof StreamMessage){
                //注意:读取顺序和写入顺序保持一致，否则会报错
                boolean booleanValue = ((StreamMessage) receiveMessage).readBoolean();
                double doubleValue  = ((StreamMessage) receiveMessage).readDouble();
                String strValue = ((StreamMessage) receiveMessage).readString();
                System.out.println(booleanValue+","+doubleValue+","+strValue);
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
