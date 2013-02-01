package uk.ac.aber.dcs.cs221.n15.Tests;
import static org.junit.Assert.*;
import java.math.*;

import org.junit.*;
import uk.ac.aber.dcs.cs221.n15.Controller.*;
import uk.ac.aber.dcs.cs221.n15.Model.*;
import java.util.*;
public class TestMonster {
	private Monster youngMonster;
	private Monster adultMonster;
	private Monster elderlyMonster;
	private Monster weakMonster;
	private Monster strongMonster;

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

		weakMonster = new Monster();
		weakMonster.setAggression(50);
		weakMonster.setStrength(50);
		weakMonster.setHealth(100);
		weakMonster.setFertility(100);

		strongMonster = new Monster();
		strongMonster.setAggression(100);
		strongMonster.setStrength(100);
		strongMonster.setHealth(100);
		strongMonster.setFertility(100);
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

	@Test
	public void testFightMonsters() {
		Monster winner = mdao.fight(weakMonster, strongMonster);
		assertTrue(winner.getHealth() < 100);
	}

	@Test
	public void testBreeding() {
		List<Monster> children = mdao.breed(weakMonster, strongMonster);
		for(Monster m : children)
			System.out.println(m);
		System.out.println("");
	}

	@Test
	public void testStrength() {
		assertEquals(elderlyMonster.getStrength(), 50);
		elderlyMonster.setStrength(200);
		assertFalse(elderlyMonster.getStrength() == 200);

		elderlyMonster.setStrength(-100);
		assertFalse(elderlyMonster.getStrength() == -100) ;

	}

	@Test
	public void testAggression()
	{
		assertEquals(youngMonster.getAggression(), 50);
		youngMonster.setAggression(200);
		assertFalse(youngMonster.getAggression() == 200);

		youngMonster.setAggression(-100);
		assertFalse(youngMonster.getAggression() == -100);
	}

	@Test
	public void testFertility()
	{
		assertEquals(adultMonster.getFertility(), 50);
		adultMonster.setFertility(200);
		assertFalse(adultMonster.getFertility() == 200);

		adultMonster.setFertility(-100);
		assertFalse(adultMonster.getAggression() == -100);
	}

	@Test
	public void testHealth()
	{
		assertEquals(youngMonster.getHealth(), 100);
		youngMonster.setHealth(200);
		assertFalse(youngMonster.getHealth() == 200);

		youngMonster.setHealth(-100);
		assertFalse(youngMonster.getHealth() == -100);
	}
}
