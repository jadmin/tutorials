/*
 * @(#)Consumer.java	2017-03-30
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Consumer
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Consumer.java 2017-03-30 2017-03-30 00:04:31 Exp $
 */
public class Consumer {

	private static final String QUEUE_NAME = "log2";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Producer.HOST);
        factory.setUsername(Producer.USERNAME);
        factory.setPassword(Producer.PASSWORD);
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = QUEUE_NAME;

        Producer.XT xt = Producer.XT.HEADERS;

        switch (xt) {
            case DEFAULT:
                //队列的相关参数需要与第一次定义该队列时相同，否则会出错，使用channel.queueDeclarePassive()可只被动绑定已有队列，而不创建
                channel.queueDeclare(queueName, true, false, true, null);
                break;
            case FANOUT:
                //接收端也声明一个fanout交换机
                channel.exchangeDeclare(Producer.XCHG_NAME, "fanout", true, true, null);
                //channel.exchangeDeclarePassive() 可以使用该函数使用一个已经建立的exchange
                //声明一个临时队列，该队列会在使用完比后自动销毁
                queueName = channel.queueDeclare().getQueue();
                //将队列绑定到交换机,参数3无意义此时
                channel.queueBind(queueName, Producer.XCHG_NAME, "");
                break;
            case DIRECT:
                channel.exchangeDeclare(Producer.XCHG_NAME, "direct", true, true, null);
                queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, Producer.XCHG_NAME, "info"); //绑定一个routing key，可以绑定多个
                channel.queueBind(queueName, Producer.XCHG_NAME, "warning");
                break;
            case TOPIC:
                channel.exchangeDeclare(Producer.XCHG_NAME, "topic", true, true, null);
                queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, Producer.XCHG_NAME, "warning.#"); //监听两种模式 #匹配一个或多个单词 *匹配一个单词
                channel.queueBind(queueName, Producer.XCHG_NAME, "*.blue");
                break;
            case HEADERS:
                channel.exchangeDeclare(Producer.XCHG_NAME, "headers", true, true, null);
                queueName = channel.queueDeclare().getQueue();
                Map<String, Object> headers = new HashMap<String, Object>() {{
                    put("name", "test");
                    put("sex", "male");
                    put("x-match", "any");//all==匹配所有条件，any==匹配任意条件
                }};
                channel.queueBind(queueName, Producer.XCHG_NAME, "", headers);
                break;
        }

        // 在同一时间不要给一个worker一个以上的消息。
        // 不要将一个新的消息分发给worker知道它处理完了并且返回了前一个消息的通知标志（acknowledged）
        // 替代的，消息将会分发给下一个不忙的worker。
        channel.basicQos(1); //server push消息时的队列长度

        //用来缓存服务器推送过来的消息
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //为channel声明一个consumer，服务器会推送消息
        //参数1:队列名称
        //参数2：是否发送ack包，不发送ack消息会持续在服务端保存，直到收到ack。  可以通过channel.basicAck手动回复ack
        //参数3：消费者
        channel.basicConsume(queueName, false, consumer);
        //channel.basicGet() //使用该函数主动去服务器检索是否有新消息，而不是等待服务器推送

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println("Received " + new String(delivery.getBody()));

            //回复ack包，如果不回复，消息不会在服务器删除
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            //channel.basicReject(); channel.basicNack(); //可以通过这两个函数拒绝消息，可以指定消息在服务器删除还是继续投递给其他消费者
        }
    }
}
