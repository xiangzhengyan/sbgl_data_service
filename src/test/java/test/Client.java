/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author xiangzy
 * @date 2014-12-19
 * 
 *       连接测试
 * 
 */
public class Client {
	private String ip;
	private int port;
	private Socket socket;
	private PrintWriter writer;
	private Reader reader;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	private void _connect() throws UnknownHostException, IOException {

		socket = new Socket(ip, port);
		socket.setKeepAlive(true);
		writer = new PrintWriter(socket.getOutputStream());
		reader = new InputStreamReader(socket.getInputStream());

	}

	public void close() {
		try {
			writer.close();
			reader.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void connect() throws InterruptedException {
		while (true) {
			try {
				_connect();
				System.out.println("连接成功！！！");
				return;
			} catch (Exception e) {
				System.err.println(e.getMessage() + "\n连接失败，5秒后重连");
				Thread.sleep(5000);
			}

		}
	}

	public void write(String s) {
		writer.write(s);
		writer.flush();
	}

	// TODO 要求服务器返回!
	public String read() throws IOException {
		char chars[] = new char[64];
		StringBuffer sb = new StringBuffer();
		String temp;
		int len;
		int index;
		while ((len = reader.read(chars)) != -1) {
			temp = new String(chars, 0, len);
			if ((index = temp.indexOf("!")) != -1) {
				sb.append(temp.substring(0, index));
				break;
			}
			sb.append(temp);

		}

		return sb.toString();

	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		String ip1 = "192.168.2.103";
		String ip2 = "116.55.245.125";
		Client client = new Client(ip1, 9527);
		client.connect();
		int count = 1;
		while (true) { 

			try {
				String str5 = "{\"type\":\"9999\",\"code\":\"JC530628101-01-99\",\"time\":\"20141230\",\"gpsbd\":[\"$GNGGA,090610.000,3041.102400,N,10402.440692,E,1,08,1.277,661.099,M,0,M,,*63\",\"$GNGLL,3041.102400,N,10402.440692,E,090610.000,A,A*42\",\"$GNGSA,A,3,18,21,14,15,25,12,31,,,,,,3.072,1.277,2.794*2C\",\"$GNGSA,A,3,170,,,,,,,,,,,,3.072,1.277,2.794*17\",\"$GPGSV,3,1,10,4,10,316,,12,28,85,29,14,50,308,29,15,6,77,14*73\",\"$GPGSV,3,2,10,18,66,112,24,21,22,185,36,22,69,331,,24,28,42,*4A\",\"$GPGSV,3,3,10,25,30,131,26,31,26,218,40*72\",\"$BDGSV,3,1,10,161,37,124,,162,45,221,37,163,52,167,,164,20,109,*65\",\"$BDGSV,3,2,10,165,29,242,,166,60,103,,167,83,154,5,169,75,26,*66\",\"$BDGSV,3,3,10,170,58,210,33,174,70,355,*67\",\"$GNRMC,090610.000,A,3041.102400,N,10402.440692,E,0.070,145.208,301214,,E,A*38\"],\"temp\":\"24.8\",\"moisture\":\"35.1\",\"data\":\"\"}!eom";
	
				client.write(str5 + count+++"\n");
//				System.out.println(client.read());
				Thread.sleep(5000);
			} catch (Exception e) {
				client.connect();
			}
		}

	}
}
