package test.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class QueueReceive implements Runnable {

	private final static String QUEUE_NAME = "Test_Queue2";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		QueueReceive client = new QueueReceive();
		QueueReceive client2 = new QueueReceive();
		new Thread(client).start();
		new Thread(client2).start();
	}

	public void run() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("192.168.2.110");
			factory.setPort(5672);
			factory.setUsername("zfy");
			factory.setPassword("zfy");
			factory.setVirtualHost("zfy_virtual_host");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.basicQos(1);//同一时间分配给超过1个消息，用来平均分配任务量，而不是任务数。
			channel.queueDeclare(QUEUE_NAME, true, false, false, null);// 第2个参数true代表消息持久化

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, false, consumer);// 第2个参数false代表需要手动发送“已处理完”标示给消息服务器

			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(" [x] Received '" + message + "'" + Thread.currentThread().getId());
				Thread.sleep(30000);
				System.out.println(" [x] work done");
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
