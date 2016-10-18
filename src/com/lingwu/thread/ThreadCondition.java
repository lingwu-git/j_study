package com.lingwu.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition同步
 * 
 * @author lingwu
 * @date 2016年10月17日
 * @see #ThreadObjectBlock 同一个例子
 */
public class ThreadCondition {

	int num;
	Lock lock = new ReentrantLock();// 锁对象
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
		//调用Condition的await()和signal()方法，都必须在lock保护之内，就是说必须在lock.lock()和lock.unlock()之间才可以使用
		lock.lock();
		/*
		 * 这里使用循环的原因
		 * 当释放锁后可能获取到锁的线程还是添加，则就会出现加减不平衡使多余方线程永久等待
		 * 所以这里的目的 即便是连续获取到锁丢出去继续维持平衡
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
		condition.signalAll();// 唤醒阻塞队列的某线程到就绪队列
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