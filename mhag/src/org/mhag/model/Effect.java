package org.mhag.model;

/**
 * @program MHAG
 * @ Effect Class
 * @version 1.0
 * @author Tifa@mh3
 */

public class Effect {

	public Effect()
	{
	}

	// get effect list
	public void setEffectFromSkill(Skill skill, int ithEffect)
	{
		effectID = effectIDTot++;  //start from 1
		effectName = skill.getEffectName()[ithEffect];
		effectNameJP = skill.getEffectNameJP()[ithEffect];
		effectTrigger =skill.getEffectTrigger()[ithEffect];
		skillID = skill.getSkillID();
		skillName = skill.getSkillName();
		//System.out.printf("%d\n", effectIDTot);
	}

	// get effect ID
	public int getEffectID() {return effectID;}

	// get effect name
	public String getEffectName() {return effectName;}

	// get effect name japanese
	public String getEffectNameJP() {return effectNameJP;}

	// get effect Trigger
	public int getEffectTrigger() {return effectTrigger;}

	//get skill ID
	public int getSkillID() {return skillID;}

	//get skill Name
	public String getSkillName() {return skillName;}

	private int effectID = 0;  // Effect ID
	private String effectName = "";   // Effect Name
	private String effectNameJP = "";   // Effect Name japanese
	private int skillID = 0;  // SKill ID
	private String skillName = "";  // Skill Name
	private int effectTrigger = 0; // Trigger Point

	static int effectIDTot = 0;
}
