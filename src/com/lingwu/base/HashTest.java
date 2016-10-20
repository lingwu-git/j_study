package com.lingwu.base;

import java.util.HashMap;
import java.util.Map;

/**
 * http://blog.csdn.net/qq352773277/article/details/41675407
 * ��Java�ļ����У��ж����������Ƿ���ȵĹ����ǣ�
 * 	1���ж����������hashCode�Ƿ����
 * 	�������ȣ���Ϊ��������Ҳ����ȣ����
 * 	�����ȣ�ת��2
 * 	2���ж�����������equals�����Ƿ����
 * 	�������ȣ���Ϊ��������Ҳ�����
 * 	�����ȣ���Ϊ�����������
 * 
 * @author lingwu
 * @date 2016��10��20��
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