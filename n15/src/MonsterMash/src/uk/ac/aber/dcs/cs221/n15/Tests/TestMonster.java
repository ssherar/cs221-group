package uk.ac.aber.dcs.cs221.n15.Tests;
import static org.junit.Assert.*;

import org.junit.*;
import uk.ac.aber.dcs.cs221.n15.Controller.*;
import uk.ac.aber.dcs.cs221.n15.Model.*;
import java.util.*;
public class TestMonster {
	private Monster youngMonster;
	private Monster adultMonster;
	private Monster elderlyMonster;
	
	private MonsterDAO mdao;
	
	@Before
	public void setup() {
		mdao = new MonsterDAO();
		
		Calendar calYoung = new GregorianCalendar();
		Calendar calAdult = new GregorianCalendar();
		Calendar calElderly = new GregorianCalendar();
		
		calYoung.setTime(new Date());
		calYoung.add(Calendar.DATE, -5);
		
		calAdult.setTime(new Date());
		calAdult.add(Calendar.DATE, -12);
		
		calElderly.setTime(new Date());
		calElderly.add(Calendar.DATE, -17);
		
		youngMonster = new Monster();
		youngMonster.setAggression(50);
		youngMonster.setStrength(50);
		youngMonster.setFertility(50);
		youngMonster.setHealth(100);
		youngMonster.setDob(calYoung.getTime());
		
		adultMonster = new Monster();
		adultMonster.setAggression(50);
		adultMonster.setStrength(50);
		adultMonster.setFertility(50);
		adultMonster.setHealth(100);
		adultMonster.setDob(calAdult.getTime());
		
		elderlyMonster = new Monster();
		elderlyMonster.setAggression(50);
		elderlyMonster.setStrength(50);
		elderlyMonster.setFertility(50);
		elderlyMonster.setHealth(100);
		elderlyMonster.setDob(calElderly.getTime());
	}

	@Test
	public void testAgingYoung() {
		mdao.age(youngMonster);
		assertTrue(youngMonster.getStrength() > 50);
		assertTrue(youngMonster.getAggression() > 50);
		assertTrue(youngMonster.getFertility() > 50);
		assertTrue(youngMonster.getHealth() == 100);
	}
	
	@Test
	public void testAgingAdult() {
		mdao.age(youngMonster);
		mdao.age(adultMonster);
		assertTrue(adultMonster.getStrength() > youngMonster.getStrength());
		assertTrue(adultMonster.getAggression() > youngMonster.getAggression());
		assertTrue(adultMonster.getFertility() == youngMonster.getFertility());
		assertTrue(adultMonster.getHealth() == 100);
	}
	
	@Test
	public void testAgingElderly() {
		mdao.age(adultMonster);
		mdao.age(elderlyMonster);
		assertTrue(elderlyMonster.getStrength() ==  adultMonster.getStrength());
		assertTrue(elderlyMonster.getAggression() == adultMonster.getAggression());
		assertTrue(elderlyMonster.getFertility() == adultMonster.getFertility());
		assertTrue(elderlyMonster.getHealth() < 100);
	}
}
