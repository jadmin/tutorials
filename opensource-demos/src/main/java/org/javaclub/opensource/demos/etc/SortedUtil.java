/*
 * @(#)SortedUtil.java	2012-3-8
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.etc;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * SortedUtil
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: SortedUtil.java 2012-3-8 16:48:30 Exp $
 */
public class SortedUtil {
	
	public static <K, V> Map<K, V> keysortMap(boolean asc) {
		class CollatorComparator implements Comparator<K> {
			
			private boolean asc;
			private Collator collator = Collator.getInstance();
			
			public CollatorComparator(boolean asc) {
				super();
				this.asc = asc;
			}

			public int compare(Object element1, Object element2) {
				CollationKey key1 = collator.getCollationKey(element1.toString().toLowerCase());
				CollationKey key2 = collator.getCollationKey(element2.toString().toLowerCase());
				int back = asc ? key1.compareTo(key2) : -key1.compareTo(key2);
				return back;
			}
		}
		
		return new TreeMap<K, V>(new CollatorComparator(asc));
	}
	
	public static int[] bubbleSort(int[] data) {
		int temp;
		for (int i = 0; i < data.length; i++) {
			for (int j = data.length - 1; j > i; j--) {
				if (data[i] > data[j]) { // 每个数都比较n次,如果data[i]>data[j]成立,则交换两个数
					temp = data[i];
					data[i] = data[j];
					data[j] = temp;
				}
			}

		}
		return data;
	}

	public static void main(String[] args) {
		Map<String, Object> keysortMap = SortedUtil.keysortMap(true);
		
		for(int i = 0; i < 10; i++) {
			String s = "" + (int)(Math.random()*1000);
			keysortMap.put(s, s);
		}

		keysortMap.put("abcd","abcd");
		keysortMap.put("Abc", "Abc");
		keysortMap.put("bbb","bbb");
		keysortMap.put("BBBB", "BBBB");
		keysortMap.put("北京","北京");
		keysortMap.put("中国","中国");
		keysortMap.put("上海", "上海");
		keysortMap.put("厦门", "厦门");
		keysortMap.put("香港", "香港");
		keysortMap.put("碑海", "碑海");
		Collection col = keysortMap.values();
		Iterator it = col.iterator();
		while(it.hasNext()) {
			 System.out.println(it.next());
		}
		int[] aa = {4, 2,1,7,90,23};
		bubbleSort(aa);
	}

}
