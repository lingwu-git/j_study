package com.lingwu.base;

import java.util.HashMap;
import java.util.Map;

/**
 * http://blog.csdn.net/qq352773277/article/details/41675407
 * 在Java的集合中，判断两个对象是否相等的规则是：
 * 	1，判断两个对象的hashCode是否相等
 * 	如果不相等，认为两个对象也不相等，完毕
 * 	如果相等，转入2
 * 	2，判断两个对象用equals运算是否相等
 * 	如果不相等，认为两个对象也不相等
 * 	如果相等，认为两个对象相等
 * 
 * @author lingwu
 * @date 2016年10月20日
 */
public class HashTest {

	public static void main(String[] args) {
		Map<Hash, Integer> map = new HashMap<Hash, Integer>();

		map.put(new Hash(), 1);
		System.out.println(map.get(new Hash()));
	}

}

class Hash {

	public int hashCode() {
		return 1;
	}

	public boolean equals(Object obj) {
		return true;
	}
}