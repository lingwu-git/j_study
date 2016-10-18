package com.lingwu.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 长连接服务
 * @author lingwu
 */
public class LongSocketServer {

	private static final int port = 12345;
	private static final String ip = "127.0.0.1";
	
	static boolean flag = true;// 客户端是否还读取消息
	
	public static void main(String[] args) {
		switch (Integer.parseInt(args[0])) {
			case 1:
				server();
				break;
			case 2:
				client();
				break;
			default:
				break;
		}
	}

	private final static void client() {
		try {
			Socket socket = new Socket(ip, port);
			new ClientWriteThread(socket).start();
			new ClientReaderThread(socket).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final static void server() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("服务器启动成功...");
			while (true) {
				Socket socket = server.accept();
				// 这里可以进行容器管理
				new ServerHandlerThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class ClientReaderThread extends Thread {
	
	Socket socket;
	
	public ClientReaderThread(Socket s) {
		socket = s;
	}
	
	public void run() {
		try {
			//读取服务器端数据    
			while (LongSocketServer.flag) {
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
				Object obj = input.readObject();
				System.out.println("接收：\t"+obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ClientWriteThread extends Thread {
	
	Socket socket;
	
	public ClientWriteThread(Socket s) {
		socket = s;
	}
	
	public void run() {
		try {
			//向服务器端发送数据    
			while (true) {
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				System.out.print("\n请输入: \t");    
				String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
				out.writeObject(str);
				out.flush();
				
				if ("exit".equals(str)) {
					socket.close();
					LongSocketServer.flag = false;
					System.out.println("客户端断开与服务端的连接");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * 服务端每个会话处理线程
 * @author lingwu
 */
class ServerHandlerThread extends Thread {
	
	Socket socket;
	long lastSendTime = System.currentTimeMillis();
	
	public ServerHandlerThread(Socket s) {
		socket = s;
	}
	
	public void run() {
		try {
			System.out.println("连接成功" + socket.getInetAddress() + ":" + socket.getPort());
			
			while (true) {
				// 超过1分钟不发消息断开连接
				if (System.currentTimeMillis() - lastSendTime > 1000*60) {
					socket.close();
					LongSocketServer.flag = false;
					System.out.println("服务器断开与客户端的连接");
					break;
				} else {
					// 读取客户端数据    
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					Object clientInput = input.readObject();
					// 处理客户端数据    
					System.out.println("客户端发过来的内容:" + clientInput);
					
					// 向客户端回复信息    
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					out.writeObject("自动回复：" + clientInput);
					out.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}