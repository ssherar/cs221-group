package uk.ac.aber.dcs.cs221.n15.Model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fight_request database table.
 * 
 */
@Entity
@Table(name="fight_request")
public class MonsterFightRequests implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="fight_id")
	private int fightId;

	private String attackingMonster;

	private String defendingMonster;

    public MonsterFightRequests() {
    }

	public int getFightId() {
		return this.fightId;
	}

	public void setFightId(int fightId) {
		this.fightId = fightId;
	}

	public String getAttackingMonster() {
		return this.attackingMonster;
	}

	public void setAttackingMonster(String attackingMonster) {
		this.attackingMonster = attackingMonster;
	}

	public String getDefendingMonster() {
		return this.defendingMonster;
	}

	public void setDefendingMonster(String defendingMonster) {
		this.defendingMonster = defendingMonster;
	}

}