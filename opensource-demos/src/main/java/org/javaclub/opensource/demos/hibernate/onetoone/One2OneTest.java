package org.javaclub.opensource.demos.hibernate.onetoone;

import org.hibernate.Session;
import org.junit.Test;

public class One2OneTest{
	/**
	 * 从Person端加载
	 */
	@Test
	public void testLoad1(){
		Session session = null;
		try{
			session = HibernateUtils.getSession();
			session.beginTransaction();
			
			Person person = (Person)session.load(Person.class, 1);
			IdCard card = person.getIdCard();
			System.out.println(card.getCardNo());
			Person p = card.getPerson();
			System.out.println(p.getName());
			System.out.println("person.name = " + person.getName());
			System.out.println("idCard.cardNo = "+person.getIdCard().getCardNo());
			System.out.println(person.getIdCard().getPerson());
			
			session.getTransaction().commit();
		}catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			HibernateUtils.closeSession(session);
		}
	}
	
	/**
	 * 从IdCard端加载
	 */
	@Test
	public void testLoad2(){
		Session session = null;
		try{
			session = HibernateUtils.getSession();
			session.beginTransaction();
			
			IdCard idCard = (IdCard)session.load(IdCard.class, 1);
			System.out.println("idCard.cardNo = "+idCard.getCardNo());
			System.out.println("Person.name = "+idCard.getPerson().getName());
			
			session.getTransaction().commit();
		}catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			HibernateUtils.closeSession(session);
		}
	}
	
	@Test
	public void testSave1(){
		Session session = null;
		try{
			session = HibernateUtils.getSession();
			session.beginTransaction();
			IdCard idCard = new IdCard();
			idCard.setCardNo("8888888888888");
			
			Person person = new Person();
			person.setName("张三");
			person.setIdCard(idCard);
			
			//在一对一主键关联映射中，存储person对象的时候，其关联对象idCard被同时存储
			//而不会抛出TransisentObjectException异常，因为他默认了级联属性
			session.save(person);
			
			session.getTransaction().commit();
		}catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			HibernateUtils.closeSession(session);
		}
	}
}
