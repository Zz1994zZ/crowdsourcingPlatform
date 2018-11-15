package cn.withzz.crowdsourcing.core;

import java.util.ArrayList;
import java.util.List;

import cn.withzz.crowdsourcing.base.Model;
import cn.withzz.crowdsourcing.base.Task;
import cn.withzz.crowdsourcing.base.User;
/**
 * 协同候选组
 * wokerSetInt中前size个下标对应tempList中的工人集合
 * @author shmily_zz
 */
public class CoCGroup implements Comparable<CoCGroup>{
	private Task task;
	private WokerSetInt wokerSetInt;
	List<UserInfoAndRank> tempList;
	private int size;
	//该候选组对task的最差分配总效用
	private float minXY;
	public CoCGroup(Task task, WokerSetInt wokerSetInt,
			List<UserInfoAndRank> tempList, int size) {
		super();
		this.task = task;
		this.wokerSetInt = wokerSetInt;
		this.tempList = tempList;
		this.size = size;
		int[] index=wokerSetInt.getTopNId(size);
		int mNum=task.getModels().size();
		int i=size-mNum;
		for (Model model : task.getModels()) {
			minXY+=model.getComplexity()*tempList.get(index[i]).getRank();
			i++;
		}
	}
	public int compareTo(CoCGroup o) {
		return ((Float)(this.getMinXY())).compareTo((Float)(o.getMinXY()));
	}
	public float getMinXY() {
		return minXY;
	}
	public List<User> getUsers(){
		List<User> users=new ArrayList<User>();
		for (int index : wokerSetInt.getTopNId(size)) {
			users.add(tempList.get(index).getUser());
		}
		return users;
	}
	public void show(){
		List<User> users=this.getUsers();
		for (int i = 0; i < size; i++) {
			System.out.print(users.get(i)+"\t");
		}
		System.out.println();
		System.out.println(this.minXY);
		System.out.println();
	}
	@Override
	public int hashCode() {
		return this.getUsers().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoCGroup other = (CoCGroup) obj;
		if (Float.floatToIntBits(minXY) != Float.floatToIntBits(other.minXY))
			return false;
		if (size != other.size)
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		if (tempList == null) {
			if (other.tempList != null)
				return false;
		} else if (!tempList.equals(other.tempList))
			return false;
		if(((CoCGroup)obj).hashCode()==this.hashCode())
		return true;
		return false;
	}
	
}
