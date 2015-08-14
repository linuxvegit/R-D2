package dto;

import java.util.concurrent.LinkedBlockingQueue;

public class Club {
	private String name;
	private int newId;

	public Club() {
	}

	public Club(String name, int newId) {
		this.name = name;
		this.newId = newId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNewId() {
		return newId;
	}

	public void setNewId(int newId) {
		this.newId = newId;
	}

	@Override
	public String toString() {
		return "Club [name=" + name + ", newId=" + newId + "]";
	}

}
