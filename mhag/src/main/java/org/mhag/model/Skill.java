package org.mhag.model;

/**
 * @program MHAG
 * @ Skill Class
 * @version 2.1
 * add generator support
 * support new talisman system
 * @author Tifa@mh3
 */

import java.util.Arrays;

public class Skill {

	public Skill()
	{
		effectID = new int[7];
		effectTrigger = new int[7];
		effectName = new String[7];
		effectNameJP = new String[7];

		Arrays.fill(effectID, 0);
		Arrays.fill(effectTrigger, 0);
		Arrays.fill(effectName, "");
		Arrays.fill(effectNameJP, "");
		for(int i = 0; i < 3; i++)
		{
			Arrays.fill(jewelID[i], -1);
			Arrays.fill(jewelSkillPoint[i], 0);
			Arrays.fill(maxSkillPoint[i], 0);
		}
	}

    	// read skill entry from a line
	public static int setSkillFromLine(String line, Skill skill, int game)
	{
		// skill id
 		skill.skillID = skillIDTot++;  //start from 1;

		int startPos = 0;
		int endPos = 0;
		int wordIndex = 0;
		skill.numEffect = 0;
		String word = "";

		int addJP = 0;
		boolean addG = false;
		if(game == 1) //mhp3rd
			addJP = 1;
		else if(game == 3 || game == 4) //mh3g
		{
			addJP = 1;
			addG = true;
		}
		else if(game == 2) //mhfu, placeholder
			addG = true;

		while(line != null )
		{
			endPos = MhagUtil.extractWordPos(line, startPos);
			word = MhagUtil.extractWord(line, startPos, endPos);
			//System.out.println(word);

			wordIndex++;

			if(wordIndex == 1)
			{
				// read Skill Name
				skill.skillName = word;
			}
			else if((wordIndex == 2) && (addJP > 0))
			{
				// read Skill Name japanese
				skill.skillNameJP = word;
			}
			else if (wordIndex == 2 + addJP)
			{
				int id = Integer.valueOf(word);
				skill.skillType = id;
			}
			else if(wordIndex == 3 + addJP)
			{
				// read Skill Class
				if(addG)
				{
					int[] numbers = new int [12];
					int nMax = 0;

					MhagUtil.extractInt(word, nMax, numbers);
					for ( int i = 0; i < 3; i++)
						for ( int j = 0; j < 4; j++)
							skill.maxSkillPoint[i][j] = numbers[i*4+j];
				}
				else
				{
					int[] numbers = new int [8];
					int nMax = 0;

					MhagUtil.extractInt(word, nMax, numbers);
					for ( int i = 0; i < 2; i++)
						for ( int j = 0; j < 4; j++)
							skill.maxSkillPoint[i][j] = numbers[i*4+j];

					// g rank data copied from high rank for mhtri and mhp3rd
					for ( int j = 0; j < 4; j++)
						skill.maxSkillPoint[2][j] = skill.maxSkillPoint[1][j];
				}


			}
			else
			{
				// read effects name/trigger points

				if(endPos == -1)return 1; //error no skill point
				if(skill.numEffect == 7)return 1; // effect <= 6


				// read effects
				skill.effectName[skill.numEffect] = word;
				if(addJP > 0)
				{
					// read effects japanese
					startPos = endPos + 1;
					endPos = MhagUtil.extractWordPos(line, startPos);
					word = MhagUtil.extractWord
						(line, startPos, endPos);
					skill.effectNameJP[skill.numEffect] = word;
				}
				//read points
				startPos = endPos + 1;
				endPos = MhagUtil.extractWordPos(line, startPos);
				word = MhagUtil.extractWord
					(line, startPos, endPos);
				int trigger = Integer.valueOf(word);
				skill.effectTrigger[skill.numEffect] = trigger;

				if(trigger < 0) skill.hasNegative = true;

				skill.numEffect++;
			}

			if(endPos  == -1)
			{
				if(wordIndex <= 2 + addJP)return 1;  // error no effect
				break;
			}
			startPos = endPos + 1;
		}

		return 0;
	}

	// get skill type index
	public int getSkillType() {return skillType;}

	// get skill type name
	public static String getSkillTypeName(int id)
	{
		if(id == 1)
			return "Status Related";
		else if(id == 2)
			return "Resistances";
		else if(id == 3)
			return "Stamina/Movement";
		else if(id == 4)
			return "Item Related";
		else if(id == 5)
			return "Blademaster";
		else if(id == 6)
			return "Gunner";
		else
			return "Miscellaneous";
	}

	// get Skill ID
	public int getSkillID() {return skillID;}

	// get skill name
	public String getSkillName() {return skillName;}

	// get skill name japanese
	public String getSkillNameJP() {return skillNameJP;}

	// get max skill point
	public int[][] getMaxSkillPoint() {return maxSkillPoint;}

	public int getMaxSkillPoint(int rank, int nSlot)
	{
		return maxSkillPoint[rank][nSlot];
	}

	// get number of effects
	public int getNumEffect() {return numEffect;}

	// get Effect Name
	public String[] getEffectName() {return effectName;}
	public String getEffectName(int ind) {return effectName[ind];}

	// get Effect Name japanese
	public String[] getEffectNameJP() {return effectNameJP;}
	public String getEffectNameJP(int ind) {return effectNameJP[ind];}

	// get Effect ID
	public int[] getEffectID() {return effectID;}
	public int getEffectID(int ind) {return effectID[ind];}

	// set Effect ID
	public void setEffectID(int index, int id) {effectID[index] = id;}

	// get Effect Trigger Points
	public int[] getEffectTrigger() {return effectTrigger;}
	public int getEffectTrigger(int ind) {return effectTrigger[ind];}

	// get JewelID
	public int[] getJewelID(int rank)
	{
		return jewelID[rank];
	}

	public int getJewelID(int rank, int nSlot)
	{
		return jewelID[rank][nSlot];
	}

	// set JewelID
	public void setJewelID(int rank, int nSlot, int id)
	{
		jewelID[rank][nSlot] = id;
	}

	// get JewelSkillPoint
	public int[] getJewelSkillPoint(int rank)
	{
		return jewelSkillPoint[rank];
	}

	public int getJewelSkillPoint(int rank, int nSlot)
	{
		return jewelSkillPoint[rank][nSlot];
	}

	// set JewelSkillPoint
	public void setJewelSkillPoint(int rank, int nSlot, int points)
	{
		jewelSkillPoint[rank][nSlot] = points;
	}

	// get/set hasNegative
	public boolean getHasNegative() {return hasNegative;}

	public void setHasNegative(boolean ifNeg) {hasNegative = ifNeg;}

	// rule out blade/gunner specific negative skills, note: this works for mhtri and mhp3rd
	public boolean getBGSpec(boolean blade)
	{
		if(blade)
			if("Precision Recoil Reload Spd".contains(skillName))
				return false;
			else
				return true;
		else
			if("FastCharge Sharpener Sharpness".contains(skillName))
				return false;
			else
				return true;
	}

	private int skillID = 0; // Skill ID
	private String skillName = "";  // Skill Name
	private String skillNameJP = "";  // Skill Name japanese
//	private String skillClass = "";  // A/B/C/D  
	private int skillType = 0; // 1 - 7
	private int[][] maxSkillPoint = new int [3][4]; //max point (1st index: low rank 0, high rank 1, g rank 2)
							// 2nd index: 0 - 3 slots
	private int numEffect = 0;  // # Effects , 6 max, 3 pos ,3 neg now 7
	private String[] effectName;   // Effect Name
	private String[] effectNameJP;   // Effect Name japanese name
	private int[] effectID;   // Effect IDs
	private int[] effectTrigger;  //skill points to tigger effect

	//generator data
	private int[][] jewelID = new int[3][4]; //jewelID for the skill (1st index: lowrank 0, highrank 1, g rank 2)
	private int[][] jewelSkillPoint = new int[3][4]; // skill points on the jewel
	private boolean hasNegative = false; // has negative effect or not

	static int skillIDTot = 0;

}
