package cn.withzz.crowdsourcing.util;

import java.util.Random;

import cn.withzz.crowdsourcing.core.TimeRangeInt;

public class DataCreator {
	private static final int [] array=new int[TimeRangeInt.MAX_SIZE];
	static {
		for (int i = 0; i < array.length; i++) {
			array[i]=1<<i;
		}
	}
	public static TimeRangeInt getAData(){
		int a=0;
		int j=0;
		for (int i = 0; i < array.length; i++) {
			if(new Random().nextBoolean()){
				a+=array[i];
				j++;				
			}
		}
		return new TimeRangeInt(a);
	}
}
