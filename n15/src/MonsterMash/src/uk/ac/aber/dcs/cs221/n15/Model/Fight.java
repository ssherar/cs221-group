package uk.ac.aber.dcs.cs221.n15.Model;

import javax.persistence.*;

@Entity
@Table(name="fights")
public class Fight {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="fight_id")
	private int id;
	
	@Column(name="monster1")
	private String monster1;
	
	@Column(name="monster2")
	private String monster2;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMonster1() {
		return monster1;
	}

	public void setMonster1(String monster1) {
		this.monster1 = monster1;
	}

	public String getMonster2() {
		return monster2;
	}

	public void setMonster2(String monster2) {
		this.monster2 = monster2;
	}
	
}
