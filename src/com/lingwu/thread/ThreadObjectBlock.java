package com.lingwu.thread;

/**
 * �߳�ͬ��
 * 
 * @author lingwu
 * @date 2016��10��17��
 * @see #ThreadCondition ͬһ������
 */
public class ThreadObjectBlock {

	int num;
	/**
	 * ����wait��notifyʵ��
	 * ����̶߳�һ����һ��һ��1.
	 */
	public static void main(String[] args) {
		final ThreadObjectBlock block = new ThreadObjectBlock();
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				public void run() {
					block.add();
				}
			}).start();
			new Thread(new Runnable() {
				public void run() {
					block.remove();
				}
			}).start();
		}
	}
	
	public synchronized void add() {
		/*
		 * ����ʹ��ѭ����ԭ��
		 * ���ͷ�������ܻ�ȡ�������̻߳�����ӣ���ͻ���ּӼ���ƽ��ʹ���෽�߳����õȴ�
		 * ���������Ŀ�� ������������ȡ��������ȥ����ά��ƽ��
		 */
		while (num == 1) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		num++;
		notify();
		System.out.println(Thread.currentThread().getName() + "-------" + num);
	}
	
	public synchronized void remove() {
		while (num == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		num--;
		notify();
		System.out.println(Thread.currentThread().getName() + "-------" + num);
	}
}
