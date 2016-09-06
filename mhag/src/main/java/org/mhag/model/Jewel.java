package org.mhag.model;

/**
 * @program MHAG
 * @ Jewel Class
 * @version 1.0
 * @author Tifa@mh3
 */

import java.util.Arrays;

public class Jewel {

	public Jewel()
	{
		skillID = new int[2];
		skillPoint = new int[2];
		skillName = new String[2];

		Arrays.fill(skillID, 0);
		Arrays.fill(skillPoint, 0);
		Arrays.fill(skillName, "");
	}

    	// read jewel entry from a line
	public static int setJewelFromLine(String line, Jewel jewel, int game)
	{
		// jewel id
		jewel.jewelID = jewelIDTot++; // start from 1

		int startPos = 0;
		int endPos = 0;
		int wordIndex = 0;
		String word = "";
		int addJP = 0;
		if((game == 1) || (game == 3) || (game == 4) || game == 5) //mhp3rd, mh3g
			addJP = 1;

		while(line != null )
		{
			endPos = MhagUtil.extractWordPos(line, startPos);
			word = MhagUtil.extractWord(line, startPos, endPos);
			//System.out.println(word);

			wordIndex++;

			if(wordIndex == 1)
			{
				// read Jewel Name
				jewel.jewelName = word;

			}
			else if((wordIndex == 2) && (addJP > 0))
			{
				// read Jewel Name japanese
				jewel.jewelNameJP = word;

			}
			else if(wordIndex == 2 + addJP)
			{
				// read low/high rank

				if(word.equals("L"))
				{
					jewel.rank = 0;
				}
				else if (word.equals("H"))
				{
					jewel.rank = 1;
				}
				else if (word.equals("G"))
				{
					jewel.rank = 2;
				}
				else
				{
					return 1;
				}
			}
			else if(wordIndex == 3 + addJP)
			{
				// read # of slots

				int nSlot = Integer.valueOf(word);
				if((nSlot < 1)||(nSlot > 3))
				{
					return 1;  // jewel needs slot 1 - 3
				}
				else
				{
					jewel.numSlot = nSlot;
				}
			}
			else
			{
				// read skills name/points

				if(endPos == -1)return 1; //error no skill point
				if(jewel.numSkill == 2)return 1;  // skill <= 2

				// read Skill
				jewel.skillName[jewel.numSkill] = word;
				startPos = endPos + 1;
				endPos = MhagUtil.extractWordPos(line, startPos);
				word = MhagUtil.extractWord
					(line, startPos, endPos);
				jewel.skillPoint[jewel.numSkill] =
					Integer.valueOf(word);
				jewel.numSkill++;
			}

			if(endPos  == -1)
			{
				if(wordIndex <= 3 + addJP)return 1;  // error no skill
				break;
			}
			startPos = endPos + 1;
		}
		if( (jewel.skillPoint[0] <= 0) || (jewel.skillPoint[1] > 0))
			return 1;  // 1st skill >0 , 2nd skill <= 0

		return 0;
	}

	// get jewel name
	public String getJewelName() {return jewelName;}

	// get jewel name japanese
	public String getJewelNameJP() {return jewelNameJP;}

	// get jewel name short
	public String getJewelNameShort()
	{
		int pos = jewelName.indexOf("Jewel");
		if(pos == -1) pos = jewelName.indexOf("Jwl");
		String jewelShort = jewelName.substring(0, pos - 1).trim();

		if(jewelName.indexOf("+") != -1)
			return jewelShort+("+");
		else
		{
			if(numSlot > 1)
			{
				if(jewelShort.length() <= 9)
					return String.format("%s%d", jewelShort, numSlot);
				else
					return String.format("%s%d", jewelShort.substring(0,9), numSlot);
			}
			else
				return jewelShort;
		}
	}

	// get jewel name with only positive skill
	public String getJewelNameSkill(MhagData mhagData, int language)
	{
		for (int i = 0; i < numSkill; i++)
		{
			if(skillPoint[i] > 0)
			{
				if(language == 0)
					return skillName[i]+" +"+
						String.valueOf(skillPoint[i]);
				else
				{
					Skill skill = mhagData.getSkill(skillID[i]);
					return skill.getSkillNameJP()+" +"+
						String.valueOf(skillPoint[i]);
				}
			}
		}
		return "---";
	}

	// get jewel toolTips for menu : jewelName + slots
	public String getJewelToolTips(int language)
	{
		StringBuilder tips = new StringBuilder("");
		if(language == 0)
			tips.append(jewelName);
		else
			tips.append(jewelNameJP);
		tips.append(" ");

		for (int i = 0; i < numSlot; i++)
			tips.append("O");

		return tips.toString();
	}

	// get jewel ID
	public int getJewelID() {return jewelID;}

	// get # of skills in a jewel
	public int getNumSkill() {return numSkill;}

	// get skill names in a jewel
	public String[] getSkillName() {return skillName;}

	// get skill IDs in a jewel
	public int[] getSkillID() {return skillID;}

	// get skill points in a jewel
	public int[] getSkillPoint() {return skillPoint;}

	// geta low rank
	public int getRank() {return rank;}

	// get # of slots
	public int getNumSlot() {return numSlot;}

	// set skill ID in a jewel
	public void setSkillID(int[] id)
	{
		for (int i = 0; i < 2; i++)
		{
			skillID[i] = id[i];
		}
	}

	public String getItem() {return items;}
	public void setItem(String entry) {items = entry;}

	private int jewelID = 0; // Jewel ID
	private String jewelName = ""; //Jewel Name
	private String jewelNameJP = ""; //Jewel Name japanese
	private int rank = 0; // lr 0/ hr 1/ gr 2
	private int numSlot = 0;  // 0 -3 slots
	private int numSkill = 0; // 2 skills max
	private int[] skillID;  //  Skill ID
	private String[] skillName;  //  Skill Name
	private int[] skillPoint;  // 1st positve; 2nd negative
	private String items = ""; //jewel materials

	static int jewelIDTot = 0;
}
