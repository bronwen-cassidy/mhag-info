package org.mhag.model;

/**
 * @program MHAG
 * @ Armor Class
 * @version 1.1
 * @author Tifa@mh3
 *
 * v1.1 add methods to retrieve armorIDTot
 */

import java.util.Arrays;

public class Armor {

	public Armor()
	{
		defense = new int[3];
		resist = new int[5];
		skillID = new int[5];
		skillName = new String[5];
		skillPoint = new int[5];

		Arrays.fill(defense, 0);
		Arrays.fill(resist, 0);
		Arrays.fill(skillID, 0);
		Arrays.fill(skillName, "");
		Arrays.fill(skillPoint, 0);
	}

	// read armor entry from a line, get part info
	public static String getBodyPartFromLine(String line, int game)
	{
		int startPos = 0;
		int endPos = 0;
		int wordIndex = 0;
		String word = "";
		while(line != null )
		{
			endPos = MhagUtil.extractWordPos(line, startPos);
			word = MhagUtil.extractWord(line, startPos, endPos);
			//System.out.println(word);

			wordIndex++;

			// read Armor Class
			if((game == 0) || (game == 2))
			{
				if(wordIndex == 4)break;
			}
			else
			{
				if(wordIndex == 5)break;
			}
			startPos = endPos + 1;
		}
		return word;
	}

	// read armor entry from a line
	// nBodyPart : body part Index (use & check)
	public static int setArmorFromLine(String line, int nBodyPart,
		Armor armor, int game)
	{
		// armor id
		armor.armorID = armorIDTot[nBodyPart]++;

		int startPos = 0;
		int endPos = 0;
		int wordIndex = 0;
		String word = "";
		int resistIndex = 0;

		int addJP = 0;
		int addDef = 0;
		if(game == 1) //mhp3rd
			addJP = 1;
		else if(game == 3 || game == 4) //mh3g
		{
			addJP = 1;
			addDef = 1;
		}
		else if(game == 2) //mhfu
			addDef = 1;

		while(line != null )
		{
			endPos = MhagUtil.extractWordPos(line, startPos);
			word = MhagUtil.extractWord(line, startPos, endPos);
			//System.out.println(word);

			wordIndex++;

			if(wordIndex == 1)
			{
				// read armor Name
				armor.armorName = word;

			}
			else if((wordIndex == 2) && (addJP > 0)) 
			{
				// read armor Name japanese
				armor.armorNameJP = word;
			}
			else if(wordIndex == 2 + addJP)
			{
				// read blader/gunner/all
				String strBGA = "BGA";
				if(strBGA.indexOf(word) != -1)
				{
					armor.bladeOrGunner = word;
				}
				else
				{
					return 1;  // incorrect BGA;
				}
			}
			else if(wordIndex == 3 + addJP)
			{
				// gender
				String strBGA = "FMA";
				if(strBGA.indexOf(word) != -1)
				{
					armor.gender = word;
				}
				else
				{
					return 1;  // incorrect FMA;
				}
			}
			else if(wordIndex == 4 + addJP)
			{
				//Body Part (check)
				if( nBodyPart == convertBodyPart(word))
				{
					armor.bodyPart = nBodyPart;
				}
				else
				{
					return 1;  // inconsistent body part ind
				}
			}
			else if(wordIndex == 5 + addJP)
			{
				// read low rank defense

				if(word.equals("--"))
				{
					armor.rank = 1;
					armor.defense[0] = 0;
				}
				else
				{
					armor.rank = 0;
					int def = 0;
					def = Integer.valueOf(word);
					if((def <= 0)||(def > 200))
					{
						return 1; // incorrect defense
					}
					else
					{
						armor.defense[0] = def;
					}
				}
			}
			else if(wordIndex == 6 + addJP)
			{
				// read high rank defense
				if(word.equals("--"))
				{
					armor.rank = 2;
					armor.defense[0] = 0;
					armor.defense[1] = 0;
				}
				else
				{
					if(armor.defense[0] == 0)
						armor.rank = 1;
					int def = 0;
					def = Integer.valueOf(word);
					if((def <= 0)||(def > 200))
					{
						return 1; // incorrect defense
					}
					else
					{
						armor.defense[1] = def;
					}

					if(addDef == 0)
						armor.defense[2] = def;  //g rank def copied from high rank
				}

			}
			else if((wordIndex == 7 + addJP) && (addDef > 0))
			{
				// read g rank defense

				int def = 0;
				def = Integer.valueOf(word);
				if((def <= 0)||(def > 200))
				{
					return 1; // incorrect defense
				}
				else
				{
					armor.defense[2] = def;
				}
			}
			else if (wordIndex == 7 + addJP + addDef)
			{
				// read # of slots

				int nSlot = Integer.valueOf(word);
				if((nSlot < 0)||(nSlot > 3))
				{
					return 1;  // armor slot 0 - 3
				}
				else
				{
					armor.numSlot = nSlot;
				}
			}
			else if ((wordIndex >= 8 + addJP + addDef )&&(wordIndex <= 12 + addJP + addDef))
			{
				// read reisistance

				int nResist = Integer.valueOf(word);
				if((nResist < -20)||(nResist > 20))
				{
					return 1;  // armor resist range[-20,20]
				}
				else
				{
					armor.resist[resistIndex] = nResist;
					resistIndex++;
				}
			}
			else
			{
				// read skills name/points

				// Torso Up exception (no skill point)
				if( word.equals("Torso Up"))
				{
					armor.skillName[armor.numSkill] = word;
					armor.numSkill++;
					break;
				}

				if(endPos == -1)return 1;  // no skill point
				if(armor.numSkill == 5)return 1;  // skill <= 5

				// read Skill
				armor.skillName[armor.numSkill] = word;
				startPos = endPos + 1;
				endPos = MhagUtil.extractWordPos(line, startPos);
				word = MhagUtil.extractWord
					(line, startPos, endPos);
				armor.skillPoint[armor.numSkill] =
					Integer.valueOf(word);
				armor.numSkill++;
			}

			if(endPos  == -1)
			{
				if(wordIndex <= 11 + addJP + addDef)return 1;  // no skill ok
				break;
			}
			startPos = endPos + 1;
		}
		return 0;
	}

	// calculate max possible armor pieces for one part
	public static int getArmorMax()
	{
		int num = 0;
		for(int i = 0; i < 5 ; i++)
		{
			if(armorIDTot[i] > num)
			{
				num = armorIDTot[i];
			}
		}
		return num;
	}


	// get armor name
	public String getArmorName() {return armorName;}

	// get armor name
	public String getArmorNameJP() {return armorNameJP;}

	// get set name
	public String getSetName()
	{
		int pos;
		if(!armorName.contains("/"))  //no multiple name
		{
			pos = armorName.lastIndexOf(" ");
			if(armorName.contains("+"))
				return  armorName.substring(0, pos).trim() + "+ Set";
			else {
                try {
                    return armorName.substring(0, pos).trim() + " Set";
                } catch (Exception e) {
                    return armorName;
                }
            }
		}
		else
		{
			int posSplit =  armorName.indexOf("/");
			String name1 = armorName.substring(0, posSplit-1).trim();
			int pos1 = name1.lastIndexOf(" ");
			String name2 = armorName.substring(posSplit+1).trim();
			int pos2 = name2.lastIndexOf(" ");
			if(armorName.contains("+"))
				return name1.substring(0, pos1).trim() + "/" +
					name2.substring(0, pos2).trim() + "+ Set";
			else
				return name1.substring(0, pos1).trim() + "/" +
					name2.substring(0, pos2).trim() + " Set";
		}
	}

	//get armor Total ID
	public static int[] getArmorIDTot() {return armorIDTot;}

	//get armor Total ID for a part
	public static int getArmorIDTot(int bodyPart) {return armorIDTot[bodyPart];}

	// get armor ID
	public int getArmorID() {return armorID;}

	// get number of skill
	public int getNumSkill() {return numSkill;}

	// get skill names
	public String[] getSkillName() {return skillName;}

	// get skill points
	public int[] getSkillPoint() {return skillPoint;}

	// get skill IDs
	public int[] getSkillID() {return skillID;}

	// geta low rank
	public int getRank() {return rank;}

	// set skill ID in an armor
	public void setSkillID(int[] id)
	{
		for (int i = 0; i < 5; i++)
		{
			skillID[i] = id[i];
		}
	}

	// get blader/gunner info
	public String getBladeOrGunner() {return bladeOrGunner;}

	// get gender info
	public String getGender() {return gender;}

	public boolean ifMale() 
	{
		if(gender.equals("F"))
			return false;
		else
			return true;
	}

	public boolean ifFemale() 
	{
		if(gender.equals("M"))
			return false;
		else
			return true;
	}

	// get blader/gunner info
	public int getBG4Head() {return bg4Head;}

	// get blader/gunner info
	public void setBG4Head(int opt) {bg4Head = opt;}

	// get body part
	public int getBodyPart() {return bodyPart;}

	// get # of slots
	public int getNumSlot() {return numSlot;}

	// get defense
	public int getDefense(int rank)
	{
		return defense[rank];
	}

	// get resist
	public int[] getResist() {return resist;}

	// convert armor part character to index
	public static int convertBodyPart(String aBodyPart)
	{
		return Armor.partFull.indexOf(aBodyPart);
	}

	// convert armor part index to character
	public static String convertBodyPart(int index)
	{
		return partFull.substring(index,index);
	}

	// get armor tips
	public String getToolTips(MhagData mhagData, int language)
	{
		StringBuilder tips = new StringBuilder("");

		int lastPos;
		if(numSlot == 0)
			lastPos = numSkill-1;
		else
			lastPos = numSkill;

		for (int i = 0; i < numSkill; i++)
		{
			if(skillName[i].equals("Torso Up"))
			{
				tips.append(skillName[i]);
				break;
			}
			Skill skill = mhagData.getSkill(skillID[i]);
			String name = "";
			if(language == 0)
				name = skill.getSkillName();
			else
				name = skill.getSkillNameJP();

			String skillLine = String.format("%s %+d",
				name,skillPoint[i]);
			tips.append(skillLine);
			if(i != lastPos) tips.append(", ");
		}
		for (int i = 0; i < numSlot; i++)
		{
			tips.append("O");
		}

		return tips.toString();
	}

	// access armor materials
	public String getItem() {return items;}
	public void setItem(String entry) {items = entry;}

	private int armorID = 0;  // Armor ID
	private String armorName = "";   // Armor Piece Name
	private String armorNameJP = "";   // Armor Piece Name japanese
	private String bladeOrGunner = "";  // Blade/Gunner/All
	private String gender = "";  // Male/Female/All
	private int rank = 2; //lr 0 / hr 1 /gr 2
	private int bodyPart = 0;  // Head/Chest/Arms/Waist/Legs
	private int numSlot = 0;   // # of slots: 0 - 3
	private int[] defense;  // 0: lr, 1: hr, 2: gr defense
	private int[] resist;  // 0-4: Fire/Water/Ice/Thunder/Dragon
	private int numSkill = 0;  // # of Skill types (max 5 for all mh3 armor)
	private int[] skillID;  // Skill ID
	private String[] skillName;  // Skill Name
	private int[] skillPoint; // Skill Points
	private String items = ""; //armor materials

	private int bg4Head = 0;  // 0: both; 1: blade; 2: gunner

	static int[] armorIDTot;
	static final String partFull = "HCAWL";
	static
	{
		armorIDTot = new int[5];
		Arrays.fill(armorIDTot, 0);
	}
}
