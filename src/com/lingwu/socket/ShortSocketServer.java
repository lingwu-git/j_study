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
 * �����ӷ���
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
			// ��ȡ������������
			DataInputStream input = new DataInputStream(socket.getInputStream());
			// ��������˷�������
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			System.out.print("������: \t");
			String str = new BufferedReader(new InputStreamReader(System.in))
					.readLine();
			out.writeUTF(str);

			String ret = input.readUTF();
			System.out.println("�������˷��ع�������: " + ret);

			out.close();
			input.close();
			socket.close();
			System.out.println("�ͻ��˹رյ�ǰ�������������" + System.currentTimeMillis());
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
			System.out.println("�����������ɹ�...");
			while (true) {
				Socket socket = server.accept();
				// ������Խ�����������
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
 * ÿ���Ự�����߳�
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
			System.out.println("���ӳɹ�" + socket.getInetAddress() + ":" + socket.getPort());
			// ��ȡ�ͻ�������
			DataInputStream input = new DataInputStream(socket.getInputStream());
			String clientInputStr = input.readUTF();// ����Ҫע��Ϳͻ����������д������Ӧ,�������
													// EOFException
			// ����ͻ�������
			System.out.println("�ͻ��˷�����������:" + clientInputStr);

			// ��ͻ��˻ظ���Ϣ
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			System.out.print("������:\t");
			// ���ͼ��������һ��
			String s = new BufferedReader(new InputStreamReader(System.in))
					.readLine();
			out.writeUTF(s);

			out.close();
			input.close();
			socket.close();
			System.out.println("�������رյ�ǰ��ͻ��˵�����" + System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}