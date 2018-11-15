package cn.withzz.crowdsourcing.core;

public class WokerSetInt {
	private  int   size;
    private int[] value=null;
    private boolean hasNext=false;
    private int wokerNum=0;
    public WokerSetInt(int _size){
    	this.size=_size;
    	this.value=new int[size/32+1];
    }
    public WokerSetInt(int[] _value){
    	this.value=_value;
    }
	public String toString() {
		StringBuilder b = new StringBuilder(size);
		for (int i = 0; i < value.length; i++) {
			int v=value[i];
			for (int j = 0;v!=0; j++) {
				if((v&1)==1)
					b.append(i*32+j+",");
				v=v>>>1;
			}
		}
		String r=b.toString();
		return "".equals(r)? "null":r;
	}
	public int [] toWokerIndexArray(){
		int [] wokers=new int [this.abs()];
		int k=0;
		for (int i = 0; i < value.length; i++) {
			int v=value[i];
			for (int j = 0;v!=0; j++) {
				if((v&1)==1){
					wokers[k]=i*32+j;
					k++;
				}
				v=v>>>1;
			}
		}
		return wokers;
	}
	public int [] getTopNId(int n){
		int [] wokers=new int [n];
		for (int i = 0; i < n; i++) {
			wokers[i]=-1;
		}
		int k=0;
		for (int i = 0; i < value.length&&k<n; i++) {
			int v=value[i];
			for (int j = 0;v!=0&&k<n; j++) {
				if((v&1)==1){
					wokers[k]=i*32+j;
					k++;
				}
				v=v>>>1;
			}
		}
		return wokers;
	}
	/**
	 * 
	 * @param index 从0开始
	 * @throws Exception
	 */
	public void addWokerIndex(int index) throws Exception{
		if(index<0){
			throw new Exception("index必须大于等于0");
		}else if(index>size){
			throw new Exception("index必须小于等于"+size);
		}
		value[index/32]|=1<<(index&31);
		wokerNum++;
	}
	public WokerSetInt and(WokerSetInt wsi){
		int[] temp=value.clone();
		for (int i = 0; i < value.length; i++) {
			temp[i]&=wsi.value[i];
		}
		WokerSetInt w=new WokerSetInt(temp);
		w.setSize(this.size);
		return w;
	}
	public int abs(){
		if(wokerNum!=0)
			return wokerNum;
		int num=0;
		for (int i = 0; i < value.length; i++) {
//			int v=value[i];
//			while(v!=0){
//				num+=(v&1);
//				v=v>>>1;
//			}
			num+=Integer.bitCount(value[i]);
		}
		return num;
	}
	public boolean hasNext(){
		return hasNext;
	}
	public boolean equals(WokerSetInt obj) {
		return this.value==obj.value;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int[] getValue() {
		return value;
	}
	public void setValue(int[] value) {
		this.value = value;
	}
	
	
	
}
