package org.javaclub.opensource.demos.hibernate.onetoone;

public class Person {
	private int id;
	
	private String name;

	private IdCard idCard;
	
	public IdCard getIdCard() {
		return idCard;
	}

	public void setIdCard(IdCard idCard) {
		this.idCard = idCard;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", idCard=" + idCard + ", name=" + name
				+ "]";
	}
	
	
}
