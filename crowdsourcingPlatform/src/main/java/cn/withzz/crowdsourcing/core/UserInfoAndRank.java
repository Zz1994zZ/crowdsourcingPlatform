package cn.withzz.crowdsourcing.core;

import java.io.Serializable;

import cn.withzz.crowdsourcing.base.User;

public class UserInfoAndRank implements Comparable<UserInfoAndRank>,Serializable{
	private static final long serialVersionUID = -2896668298679236894L;
	private User user;
	private float rank;

	public UserInfoAndRank(User user_, float rank_) {
		this.user = user_;
		this.rank = rank_;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public float getRank() {
		return rank;
	}

	public void setRank(float rank) {
		this.rank = rank;
	}

	public int compareTo(UserInfoAndRank o) {
		return ((Float) (o.getRank())).compareTo(((Float) (this.getRank())));
	}
}
