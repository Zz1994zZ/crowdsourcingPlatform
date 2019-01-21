package cn.withzz.crowdsourcing.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import cn.withzz.crowdsourcing.core.CoCGroup;
import cn.withzz.crowdsourcing.core.TimeRangeInt;
import cn.withzz.crowdsourcing.core.WokerSetInt;

public class Task implements Serializable{
	private transient int id;
	private TreeSet<Model> models=new TreeSet<Model>();
	private transient List<User> register=new ArrayList<User>();
	private String skill;
	private int g;
	private float complexity;
	
	//附加
	private transient LinkedList<CoCGroup> ccgs;
	private transient CoCGroup ccg;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CoCGroup getCcg() {
		return ccg;
	}

	public void setCcg(CoCGroup ccg) {
		this.ccg = ccg;
	}

	public LinkedList<CoCGroup> getCcgs() {
		return ccgs;
	}

	public void setCcgs(LinkedList<CoCGroup> ccgs) {
		this.ccgs = ccgs;
	}

	public TreeSet<Model> getModels() {
		return models;
	}

	public void setModels(TreeSet<Model> models) {
		this.models = models;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public float getComplexity() {
		return complexity;
	}

	public void setComplexity(float complexity) {
		this.complexity = complexity;
	}

	public List<User> getRegister() {
		return register;
	}

	public void setRegister(List<User> register) {
		this.register = register;
	}
//	public  void prepare() throws Exception{
//		ArrayList<User> devs=new WokerSelecter(register).selectDevFromAll(this);
//		if(devs.size()>=this.models.size()){
//			for (int i = 0; i <this.models.size(); i++) {
//				this.models.get(i).setWoker(devs.get(i));
//			}
//		}
//	}
//	public  void prepare2() throws Exception{
//		ArrayList<User> devs=new WokerSelecter(register).selectByAbility(this);
//		Collections.sort(this.models);
//		if(devs.size()>=this.models.size()){
//			for (int i = 0; i <this.models.size(); i++) {
//				this.models.get(i).setWoker(devs.get(i));
//			}
//		}
//	}
	public   void process(){
		for(Model model:this.getModels()){
			model.finish();
		}
	}
	public float computeXY() throws Exception{
		float xy=0;
		TimeRangeInt timeRange=new TimeRangeInt();
		timeRange.addRange(1, 24);
		for (Model model:models){
			timeRange=timeRange.and(model.getWoker().getTimeRangeInt());
			xy+=model.getComplexity()*model.getWoker().getSkillPoint(skill);
		}
		return xy/this.models.size();
	}
	public float computeZXY() throws Exception{
		float xy=0;
		TimeRangeInt timeRange=new TimeRangeInt();
		timeRange.addRange(1, 24);
		for (Model model:models){
			timeRange=timeRange.and(model.getWoker().getTimeRangeInt());
			xy+=model.getComplexity()*model.getWoker().getSkillPoint(skill);
		}
		return xy/this.models.size()*timeRange.cwc();
	}
	public byte computeCWP() throws Exception{
		TimeRangeInt timeRange=new TimeRangeInt();
		timeRange.addRange(1, 24);
		for (Model model:models){
			timeRange=timeRange.and(model.getWoker().getTimeRangeInt());
		}
		return timeRange.abs();
	}
	
	public void clearModels(){
		for (Model model:models){
			model.setWoker(null);
		}
	}
	public void showDevs(){
		for (Model model : models) {
			System.out.println(model);
		}
	}
	
}
