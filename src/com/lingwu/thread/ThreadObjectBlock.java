package com.lingwu.thread;

/**
 * 线程同步
 * 
 * @author lingwu
 * @date 2016年10月17日
 * @see #ThreadCondition 同一个例子
 */
public class ThreadObjectBlock {

	int num;
	/**
	 * 利于wait、notify实现
	 * 多个线程对一个数一加一减1.
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
		 * 这里使用循环的原因
		 * 当释放锁后可能获取到锁的线程还是添加，则就会出现加减不平衡使多余方线程永久等待
		 * 所以这里的目的 即便是连续获取到锁丢出去继续维持平衡
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
