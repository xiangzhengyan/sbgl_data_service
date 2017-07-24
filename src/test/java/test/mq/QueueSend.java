package test.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class QueueSend {
	
	private final static String QUEUE_NAME = "Test_Queue2";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws java.io.IOException{
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("192.168.2.110");
	    factory.setPort(5672);
		factory.setUsername("zfy");
		factory.setPassword("zfy");
		factory.setVirtualHost("zfy_virtual_host");
		
	    Connection connection = factory.newConnection();
	    
	    Channel channel = connection.createChannel();
	    channel.queueDeclare(QUEUE_NAME, true, false, false, null);//第2个参数true代表消息持久化
	    
	    String message = "bbbbbbbbbbbbbbbb";
	    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
	    System.out.println(" [x] Sent '" + message + "'");
	    
	    channel.close();
	    connection.close();

	}

}
