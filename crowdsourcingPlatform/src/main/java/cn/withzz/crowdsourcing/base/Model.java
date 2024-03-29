package cn.withzz.crowdsourcing.base;

import java.io.Serializable;

public class Model implements Comparable<Model>,Serializable{
	private transient int id;
	private float complexity;
	private User worker;
	private String skill;
	private Task task;
	public Model(float complexity, String skill, Task task) {
		super();
		this.complexity = complexity;
		this.skill = skill;
		this.task = task;
	}
	public Model() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getComplexity() {
		return complexity;
	}
	public void setComplexity(float complexity) {
		this.complexity = complexity;
	}
	public User getWorker() {
		return worker;
	}
	public void setWorker(User woker) {
		this.worker = woker;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public void finish(){
		float evaluate;
		float realSkill=worker.getRealSkillMap().get(getSkill());
		if(realSkill/complexity>=1){
			evaluate=(float) (Math.random()*0.2f+0.8f);
		}else{
			evaluate=(float) (Math.random()*realSkill);
		}
		float point=complexity*evaluate;
		this.worker.updateSkill2(this.skill,point);
	}
	public void finish(float evaluate){
		float point=complexity*evaluate;
		this.worker.updateSkill2(this.skill,point);
	}
	public int compareTo(Model o) {
		int a=((Float) (o.getComplexity())).compareTo(((Float) (this.getComplexity())));
		if(a==0)
			return -1; 
		 return a;
	}

	@Override
	public String toString() {
		return "Model{" +
				"id=" + id +
				", complexity=" + complexity +
				", woker=" + worker +
				", skill='" + skill + '\'' +
				", task=" + task.getId() +
				'}';
	}
	//	@Override
//	public String toString() {
//		return "Model [complexity=" + complexity
//				+ "\t, skill=" + skill + "\t]";
//	}
	
}
