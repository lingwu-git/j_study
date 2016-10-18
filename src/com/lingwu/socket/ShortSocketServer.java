package com.lingwu.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 短连接服务
 * 
 * @author lingwu
 */
public class ShortSocketServer {

	private static final int port = 12345;
	private static final String ip = "127.0.0.1";

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
			// 读取服务器端数据
			DataInputStream input = new DataInputStream(socket.getInputStream());
			// 向服务器端发送数据
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			System.out.print("请输入: \t");
			String str = new BufferedReader(new InputStreamReader(System.in))
					.readLine();
			out.writeUTF(str);

			String ret = input.readUTF();
			System.out.println("服务器端返回过来的是: " + ret);

			out.close();
			input.close();
			socket.close();
			System.out.println("客户端关闭当前与服务器的链接" + System.currentTimeMillis());
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
				new HandlerThread(socket).start();
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

/**
 * 每个会话处理线程
 * 
 * @author lingwu
 */
class HandlerThread extends Thread {

	Socket socket;

	public HandlerThread(Socket s) {
		socket = s;
	}

	public void run() {
		try {
			System.out.println("连接成功" + socket.getInetAddress() + ":" + socket.getPort());
			// 读取客户端数据
			DataInputStream input = new DataInputStream(socket.getInputStream());
			String clientInputStr = input.readUTF();// 这里要注意和客户端输出流的写方法对应,否则会抛
													// EOFException
			// 处理客户端数据
			System.out.println("客户端发过来的内容:" + clientInputStr);

			// 向客户端回复信息
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			System.out.print("请输入:\t");
			// 发送键盘输入的一行
			String s = new BufferedReader(new InputStreamReader(System.in))
					.readLine();
			out.writeUTF(s);

			out.close();
			input.close();
			socket.close();
			System.out.println("服务器关闭当前与客户端的链接" + System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}