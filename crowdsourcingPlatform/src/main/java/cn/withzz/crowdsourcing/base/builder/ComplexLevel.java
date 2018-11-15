package cn.withzz.crowdsourcing.base.builder;


public enum ComplexLevel {
	EASY(-0.8f),NORMAL(0f),COMPLEX(1.4f);
	private float level;
	private ComplexLevel(float level){
		this.level=level;
	}
	public float getValue() {
		return level;
	}
	/**
	 * @param gaussianRand 服从(0,1)正态分布
	 * @return
	 */
	public static  ComplexLevel getLevel(double gaussianRand){
		if(gaussianRand>1)
			return COMPLEX;
		else if(gaussianRand<-1)
			return NORMAL;
		else
			return EASY;
		
	}
}
