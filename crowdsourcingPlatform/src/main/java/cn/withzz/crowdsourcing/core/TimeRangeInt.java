package cn.withzz.crowdsourcing.core;

import java.io.Serializable;

import javax.management.DescriptorKey;


/**
 * 基于int的时段容器
 * @author shmily_zz
 *
 */
public class TimeRangeInt implements Serializable{
	public static final int   	MAX_SIZE = 24;
    private int value=0;
    private boolean hasNext=false;
    public TimeRangeInt(){
    	super();
    }
    
    public TimeRangeInt(String str) throws Exception {
		super();
		char []cs=str.toCharArray();
		if(cs.length!=24)
			throw new Exception("TimeRangeInt必须以24个字符初始化");
		for (int i = 0; i < cs.length; i++) {
			if(cs[i]=='1'){
				this.value|=(1<<(23-i));
			}
		}
	}

	public TimeRangeInt(int _value){
    	this.value=_value;
    }
	public String toString() {
		StringBuilder b = new StringBuilder(MAX_SIZE);
			int a=value;
			for (int i = 0; i < MAX_SIZE; i++) {
				b.insert(0,(a&1)==0? "0":"1");
				a>>>=1;
			}
		return b.toString();
	}
	public void addRange(int start,int end) throws Exception{
		if(start>end){
			throw new Exception("end必须大于等于start");
		}else if(start<1){
			throw new Exception("start必须大于等于1");
		}else if(end>MAX_SIZE){
			throw new Exception("end必须小于等于"+MAX_SIZE);
		}
		int m=(2<<(end-start))-1;
		value|=m<<(MAX_SIZE-end);
	}
	/**
	 * 用于转换数据库中不标准数据，勿使用
	 */
	@Deprecated
	public void addTimeRange(int start,int end) throws Exception{
		if(end==0)
			end=24;
		if(start==24)
			start=0;
		if(start<end){
			this.addRange(start+1, end);
		}else if(start>end){
			if((end<=12)&&(end+10>start)){
				this.addRange(start+1, end+10);
			}else{
				this.addRange(start+1, 24);
				this.addRange(1, end);
			}
		}
	}
	public TimeRangeInt and(TimeRangeInt tri){
		return new TimeRangeInt(this.value&tri.value);
	}
	public byte abs(){
		return (byte) Integer.bitCount(value);
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean hasNext(){
		return hasNext;
	}
	public boolean equals(TimeRangeInt obj) {
		// TODO Auto-generated method stub
		return this.value==obj.value;
	}
	public int[] toArray(){
		int [] a=new int [this.abs()];
		int v=this.value;
		for (int i = 0,j=0;v!=0; i++) {
			if((v&1)==1)
				a[j++]=23-i;
			v=v>>>1;
		}
		return a;
	}
	/**
	 * 时间段数
	 * @return
	 */
	public int ctp(){
		int[] timeArray=this.toArray();
		int m=timeArray.length;
		if(m==1||m==24){
			return 1;
		}else if(m==0){
			return 0;
		}else{
			int cnum=0;
			for (int i = 0; i < timeArray.length-1; i++) {
				cnum+= timeArray[i+1]-timeArray[i]>1? 1:0;
			}
			if(timeArray[timeArray.length-1]-timeArray[0]!=23)
				cnum++;
			return cnum;
		}
	}
	/**
	 * 连续时段系数
	 * @return
	 */
	public float ctc(){
		byte tsn=this.abs();
		int ctp=this.ctp();
		if(tsn==0)
			return 1f;
		else if(tsn<=12){
			return tsn/(tsn-1f+ctp);
		}else if(tsn<23){
			return (24f-tsn)/(23-tsn+ctp);
		}else {
			return 1f;
		}
	}
	/**
	 * 协同系数计算
	 * @return
	 */
	public float cwc(){
		return (float) (this.ctc()/(1+Math.exp(0-this.abs())));
	}

}
