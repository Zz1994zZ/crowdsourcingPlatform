package cn.withzz.crowdsourcing.core;
/**
 * 用来生成时段组合
 * @author shmily_zz
 *
 */
public class CCreator {
	private int n;
	private int m;
	private int [] temp;
	private boolean hasNext=true;
	/**
	 * @param _n 总段数
	 * @param _m 组合结果段数
	 */
	public CCreator(int _n,int _m){
		this.n=_n;
		this.m=_m;
		temp=new int[m+1];
		for (int i = 0; i <m; i++) {
			temp[i]=1<<(n-i);
		}
		temp[m]=1;
	}
	/**
	 * 获得下一个组合，建议这样遍历：
	 * while(cc.hasNext()){
	 *			TimeRangeInt t=new TimeRangeInt(cc.next());
	 *}
	 * @return
	 */
	public int next(){
		int r=0;
		for (int i = 0; i <m; i++) {
			r+=temp[i];
		}
		hasNext=false;
		for (int i =m-1 ; i >=0; i--) {
			if(temp[i]>>>1==temp[i+1])
				continue;
			else{
				hasNext=true;
				temp[i]>>>=1;
				for (int j = i+1; j <=m-1; j++) {
					temp[j]=temp[i]>>>(j-i);
				}
				break;
			}
		}
		return r>>>1;
	}
	public boolean hasNext(){
		return hasNext;
	}
}
