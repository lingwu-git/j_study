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
 * �����ӷ���
 * @author lingwu
 */
public class LongSocketServer {

	private static final int port = 12345;
	private static final String ip = "127.0.0.1";
	
	static boolean flag = true;// �ͻ����Ƿ񻹶�ȡ��Ϣ
	
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
			System.out.println("�����������ɹ�...");
			while (true) {
				Socket socket = server.accept();
				// ������Խ�����������
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
			//��ȡ������������    
			while (LongSocketServer.flag) {
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
				Object obj = input.readObject();
				System.out.println("���գ�\t"+obj);
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
			//��������˷�������    
			while (true) {
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				System.out.print("\n������: \t");    
				String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
				out.writeObject(str);
				out.flush();
				
				if ("exit".equals(str)) {
					socket.close();
					LongSocketServer.flag = false;
					System.out.println("�ͻ��˶Ͽ������˵�����");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * �����ÿ���Ự�����߳�
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
			System.out.println("���ӳɹ�" + socket.getInetAddress() + ":" + socket.getPort());
			
			while (true) {
				// ����1���Ӳ�����Ϣ�Ͽ�����
				if (System.currentTimeMillis() - lastSendTime > 1000*60) {
					socket.close();
					LongSocketServer.flag = false;
					System.out.println("�������Ͽ���ͻ��˵�����");
					break;
				} else {
					// ��ȡ�ͻ�������    
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					Object clientInput = input.readObject();
					// ����ͻ�������    
					System.out.println("�ͻ��˷�����������:" + clientInput);
					
					// ��ͻ��˻ظ���Ϣ    
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					out.writeObject("�Զ��ظ���" + clientInput);
					out.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}