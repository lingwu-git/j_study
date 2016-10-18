package com.lingwu.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Conditionͬ��
 * 
 * @author lingwu
 * @date 2016��10��17��
 * @see #ThreadObjectBlock ͬһ������
 */
public class ThreadCondition {

	int num;
	Lock lock = new ReentrantLock();// ������
	Condition condition = lock.newCondition();
	
	public static void main(String[] args) {
		final ThreadCondition tc = new ThreadCondition();
		for (int i = 0; i < 10; i++){
			new Thread(new Runnable(){
				public void run(){
					tc.add();
				}
			}).start();
			
			new Thread(new Runnable(){
				public void run(){
					tc.remove();
				}
			}).start();
		}
	}

	public void add() {
		//����Condition��await()��signal()��������������lock����֮�ڣ�����˵������lock.lock()��lock.unlock()֮��ſ���ʹ��
		lock.lock();
		/*
		 * ����ʹ��ѭ����ԭ��
		 * ���ͷ�������ܻ�ȡ�������̻߳�����ӣ���ͻ���ּӼ���ƽ��ʹ���෽�߳����õȴ�
		 * ���������Ŀ�� ������������ȡ��������ȥ����ά��ƽ��
		 */
		while (num == 1){
			try{
				condition.await();
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		num++;
		condition.signalAll();// �����������е�ĳ�̵߳���������
		System.out.println(Thread.currentThread().getName() + "-------" + num);
		lock.unlock();
	}
	
	public void remove() {
		lock.lock();
		while (num == 0) {
			try{
				condition.await();
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		num--;
		condition.signalAll();
		System.out.println(Thread.currentThread().getName() + "-------" + num);
		lock.unlock();
	}
}