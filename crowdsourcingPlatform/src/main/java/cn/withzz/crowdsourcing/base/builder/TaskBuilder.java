package cn.withzz.crowdsourcing.base.builder;

import java.util.ArrayList;
import java.util.Random;

import cn.withzz.crowdsourcing.base.Model;
import cn.withzz.crowdsourcing.base.Task;

public class TaskBuilder {
	/**
	 * 
	 * @param modelNum 模块数
	 * @param g		       阈值g
	 * @param skillType技能类型
	 * @param level    难度等级
	 * @return
	 */
	public static Task create(int modelNum, int g, String skillType,
			ComplexLevel level) {
		Task task = new Task();
		task.setSkill(skillType);
		// 设置阈值g
		task.setG(g);
		Random random = new Random();
		// 生成modelNum个模块并装配
		for (int i = 0; i < modelNum; i++) {
			// 这里生成符合正态分布的随机数
			float complex = sigmod(random.nextGaussian() + level.getValue());
			Model model = new Model();
			model.setTask(task);
			model.setComplexity(complex);
			model.setSkill(skillType);
			task.getModels().add(model);
		}
		return task;
	}

	/**
	 * 生成num个任务的集合各项属性符合正态分布
	 * 
	 * @param num
	 * @return
	 */
	public static ArrayList<Task> createTasks(int num,int maxG,int maxModelNum, String skillType) {
		ArrayList<Task> taskList = new ArrayList<Task>(num);
		Random randLevel = new Random();
		Random randomG = new Random();
		Random randomModelNum = new Random();
		for (int i = 0; i < num; i++) {
			taskList.add(
					create(
							Math.round(sigmod(randomModelNum.nextGaussian())*maxModelNum+1),
							Math.round(sigmod(randomG.nextGaussian())*maxG+1), 
							skillType,
							ComplexLevel.getLevel(randLevel.nextGaussian())
						  )
						 );
		}

		return taskList;
	}
	public static ArrayList<Task> createTasksWithStaticG(int num,int g,int maxModelNum, String skillType) {
		ArrayList<Task> taskList = new ArrayList<Task>(num);
		Random randLevel = new Random();
		Random randomG = new Random();
		Random randomModelNum = new Random();
		for (int i = 0; i < num; i++) {
			taskList.add(
					create(
							Math.round(sigmod(randomModelNum.nextGaussian())*maxModelNum+1),
							g, 
							skillType,
							ComplexLevel.getLevel(randLevel.nextGaussian())
						  )
						 );
		}

		return taskList;
	}
	public static float sigmod(double x) {
		return (float) (1 / (1 + Math.exp(-x)));
	}
}
