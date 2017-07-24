package test.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.zfy.sbgl.common.mq.MQ;
import com.zfy.sbgl.common.mq.MQUtil;

public class TestReceive  {


	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {  
        // 创建连接和频道  
        ConnectionFactory factory = new ConnectionFactory();  
		factory.setHost("192.168.2.110");
		factory.setPort(5672);
		factory.setUsername("zfy");
		factory.setPassword("zfy");
		factory.setVirtualHost("zfy_virtual_host");
        Connection connection = factory.newConnection();  
        Channel channel = connection.createChannel();  
  
        channel.exchangeDeclare(MQ.EXCHANGE_BROADCAST, "fanout");  
        // 创建一个非持久的、唯一的且自动删除的队列  
        String queueName = channel.queueDeclare().getQueue();  
        // 为转发器指定队列，设置binding  
        channel.queueBind(queueName, MQ.EXCHANGE_BROADCAST, "");  
  
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");  
  
        QueueingConsumer consumer = new QueueingConsumer(channel);  
        // 指定接收者，第二个参数为自动应答，无需手动应答  
        channel.basicConsume(queueName, true, consumer);  
  
        while (true)  
        {  
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
            String message = new String(delivery.getBody());  
            System.out.println(" Received '" + message + "'");  
             
        }  
  
    }

	

}
