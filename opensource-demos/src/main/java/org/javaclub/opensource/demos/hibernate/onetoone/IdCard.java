package org.javaclub.opensource.demos.hibernate.onetoone;

public class IdCard {
	private int id;
	private String cardNo;
	private Person person;
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	@Override
	public String toString() {
		return "IdCard [cardNo=" + cardNo + ", id=" + id + ", person=" + person
				+ "]";
	}
	
	
}
