package org.mhag.model;

/**
 * @program MHAG
 * @ MhagData Class , store MHAG data
 * @version 2.1
 * support generator data
 * support new talisman system
 * @author Tifa@mh3
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MhagData {

	public MhagData()
	{
		for (int i = 0; i < nMaxCharm; i++)
			charmList[i] = new Charm();
	}

	// read data file
	public void readFile(Mhag mhag) 
	{
		readSkill(mhag);
		readJewel(mhag);
		readArmor(mhag);
		//readCharm(mhag);

	}

	// process data right after access
	public void dataPreProc()
	{
		genEffectList();
		preProcessJewelList();
		preProcessArmorList();

		/*
		for(int i = 0; i < Armor.armorIDTot[0]; i++)
		{
			Armor armor = armorList[0][i];
			System.out.printf("%d. %d %s\n", i, armor.getBG4Head(), armor.getArmorName());
		}
		 */

//		preProcessCharmList();
//		preProcessSkillList();
	}

	// process data right after access
	public void showReference(Mhag mhag) throws FileNotFoundException
	{
		MhagUtil.logLine(mhag, "Gernerate Reference Files");
		genRefSkill();
		genRefEffect();
		genRefJewel();
		genRefArmor();
		//genRefCharm();
//		genRefSkillClass();
		genRefCompleteSet();
	}

	// read skill from skill file
	public void readSkill(Mhag mhag) 
	{
		String file;
		file = "data/" + dirSave[mhag.getGame()] + fileSkill;
		/*
		if(mhag.getGame() == 0)
			file = dirDataTri + fileSkill;
		else
			file = dirDataP3rd + fileSkill;
		 */

		Scanner in = new Scanner(getClass().getResourceAsStream(file), "UTF-8");

		// check total # of skills
		int nMax = 0;
		String line = "";
		while(in.hasNext())
		{
			line = in.nextLine();
			if(line.startsWith("#"))continue;
			nMax++;
		}
		String logLine =  String.format
			("Total Number of Skills : %d",nMax);
		MhagUtil.logLine(mhag, logLine);

		in.close();

		skillList = new Skill[nMax];
		for (int i = 0; i < nMax; i++)
		{
			skillList[i] = new Skill();

		}
		Scanner in2 = new Scanner(getClass().getResourceAsStream(file), "UTF-8");

		// read skill entry
		int ioErr = 0;
		int skillIndex = 0;
		int game = mhag.getGame();
		while(in2.hasNext())
		{
			line = in2.nextLine();
			if(line.startsWith("#"))continue;
			// System.out.printf("%d\n", skillIndex);
			// System.out.println(line);
			ioErr = Skill.setSkillFromLine
				(line, skillList[skillIndex], game);

			if(ioErr != 0)
			{
				MhagUtil.logLine(mhag,
					"Error Found in Skill File");
				return;
			}
			skillIndex++;
		}
        System.out.println("check skills");

	}

	// generate effect list
	public void genEffectList()
	{
		int nMax = 0;
		for (int i = 0; i < Skill.skillIDTot; i++)
		{
			nMax += skillList[i].getNumEffect();
		}
		effectList = new Effect[nMax];

		for (int i = 0; i < nMax; i++)
		{
			effectList[i] = new Effect();
		}

		int n = 0;
		for (int i =0; i < Skill.skillIDTot; i++)
		{
			Skill skill = skillList[i];
			for (int j = 0; j < skill.getNumEffect(); j++)
			{
				effectList[n].setEffectFromSkill(skill, j);
				skill.setEffectID(j,n);
				n++;
			}
		}

	}

	// pre process jewel list 
	// (1.skill id from skill name
	//  2.jewel id/points for each skill id)
	public void preProcessJewelList()
	{
		for (int i = 0; i < Jewel.jewelIDTot; i++)
		{
			Jewel aJewel = jewelList[i];
			int[] id = new int[2];
			Arrays.fill(id, 0);

			for (int j = 0; j < aJewel.getNumSkill(); j++)
			{
				id[j] = getSkillIDFromName
					(aJewel.getSkillName()[j]);
			}
			aJewel.setSkillID(id);

			// set jewel id/points for the positive skill (1st one)
			int point = aJewel.getSkillPoint()[0];
			int rank = aJewel.getRank();
			int nSlot = aJewel.getNumSlot();
			if((skillList[id[0]].getJewelID(rank, nSlot) >= 0) &&
				(skillList[id[0]].getJewelSkillPoint(rank, nSlot) > point))continue;

			for (int j = rank; j <= 2; j++)
			{
				skillList[id[0]].setJewelID(j, nSlot, i);
				skillList[id[0]].setJewelSkillPoint(j, nSlot, point);
			}

		}

	}

	// pre process Armor list (skill id from skill name ), check b/g for head pieces
	public void preProcessArmorList()
	{
		for (int i = 0; i < Armor.getArmorMax(); i++)
		{
			for (int j = 0; j < 5; j++)
			{
				Armor armor = armorList[j][i];
				int[] id = new int[5];
				Arrays.fill(id, 0);
				for (int k = 0; k < armor.getNumSkill(); k++)
				{
					id[k] = getSkillIDFromName
						(armor.getSkillName()[k]);
				}
				armor.setSkillID(id);
			}

		}

		setBG4HeadAll();

	}

	public void setBG4HeadAll()
	{

		boolean[] checked = new boolean[Armor.armorIDTot[0]];
		Arrays.fill(checked, false);
		for(int i = 0; i < Armor.armorIDTot[0]; i++)
		{
			if(checked[i])continue;
			Armor armor =  armorList[0][i];
			String setName1 = armor.getSetName();
			checked[i] = true;

			boolean found = false;
			for(int j = i + 1; j < Armor.armorIDTot[0]; j++)
			{
				if(checked[j])continue;
				Armor armor2 =  armorList[0][j];

				if(setName1.equals(armor2.getSetName()))
				{
					if(((armor.getNumSkill() == 1) && (armor2.getNumSkill() != 1)) ||
						((armor.getNumSkill() != 1) && (armor2.getNumSkill() == 1)))
					{
						checked[j] = true;
						continue;
					}

					if(armor.getDefense(2) > armor2.getDefense(2))  //use G rank defense (G = high for mhtri and mh3g)
					{
						armor.setBG4Head(1);
						armor2.setBG4Head(2);
					}
					else
					{
						armor.setBG4Head(2);
						armor2.setBG4Head(1);
					}
					checked[j] = true;
					found = true;
					break;
				}

			}
			if(found)continue;

			// if not found, search the other body part
			for(int k = 1; k < 5; k++)
			{
				for(int j = 0; j < Armor.armorIDTot[k]; j++)
				{
					Armor armor2 =  armorList[k][j];
					if((setName1.equals(armor2.getSetName()))  &&
						(armor.getDefense(2) == armor2.getDefense(2)))
					{
						if(((armor.getNumSkill() == 1) && (armor2.getNumSkill() != 1)) ||
							((armor.getNumSkill() != 1) && (armor2.getNumSkill() == 1)))
							continue;

						if(armor2.getBladeOrGunner().equals("A"))
							armor.setBG4Head(0);
						else if(armor2.getBladeOrGunner().equals("B"))
							armor.setBG4Head(1);
						else
							armor.setBG4Head(2);
						found = true;
						break;
					}

				}
				if(found)break;
			}

		}
	}

	/* pre process skill list (genate skill list in class)
	public void preProcessSkillList()
	{
		indexSkillInClass = new int[5][Skill.skillIDTot];
		numSkillInClass = new int[5];
		Arrays.fill(numSkillInClass, 0);
		for (int i = 0; i < 5; i++)
			Arrays.fill(indexSkillInClass[i], 0);
		String classStr = "ABCD";
		String className = "";
		int classID = 0;

		for (int i = 0; i < Skill.skillIDTot; i++)
		{
			className = skillList[i].getSkillClass();
			classID = classStr.indexOf(className);
			if( classID == -1)
			{
				classID = 4;
			}
			indexSkillInClass[classID][numSkillInClass[classID]++] =
				skillList[i].getSkillID();
		}

	}
	 */

	/* pre process charm list (charm name , percentage )
	public void preProcessCharmList()
	{
		int[] numInClass = new int[5];
		Arrays.fill(numInClass, 0);
		int charmClass = 0;
		for (int i = 0; i < Charm.charmIDTot; i++)
		{
			Charm charm = charmList[i];
			charm.setCharmName();

			numInClass[charm.getCharmClass()] += 1;
		}

		// if no percentage, use average value
		for (int i = 0; i < Charm.charmIDTot; i++)
		{
			Charm charm = charmList[i];
			if(charm.getPercentage() == 0)
			{
				charmClass = charm.getCharmClass();
				charm.setPercentage(100/numInClass[charmClass]);
			}
		}

	}
	 */

	// read jewel from jewel file
	public void readJewel(Mhag mhag)
	{
		String file, fileItem;
		Scanner inItem = null;
		file = "data/" + dirSave[mhag.getGame()] + fileJewel;
		fileItem = "data/" + dirSave[mhag.getGame()] + fileJewelItem;
		/*
		if(mhag.getGame() == 0)
			file = dirDataTri + fileJewel;
		else
			file = dirDataP3rd + fileJewel;
		 */

		Scanner in = new Scanner(getClass().getResourceAsStream(file), "UTF-8");
		try {
			inItem = new Scanner(getClass().getResourceAsStream(fileItem), "UTF-8");
		} catch (Exception e) {
			ifItem = false;
		}

		// check total # of skills
		int nMax = 0;
		String line = "";
		while(in.hasNext())
		{
			line = in.nextLine();
			if(line.startsWith("#"))continue;
			nMax++;
		}
		String logLine = String.format
			("Total Number of Jewels : %d",nMax);
		MhagUtil.logLine(mhag,logLine);

		in.close();

		jewelList = new Jewel[nMax];
		for (int i = 0; i < nMax; i++)
		{
			jewelList[i] = new Jewel();

		}
		Scanner in2 = new Scanner(getClass().getResourceAsStream(file), "UTF-8");

		// read Jewel entry
		int ioErr = 0;
		int jewelIndex = 0;
		int game = mhag.getGame();
		while(in2.hasNext())
		{
			line = in2.nextLine();
			if(line.startsWith("#"))continue;
			// System.out.printf("%d\n", jewelIndex);
			//System.out.println(line);
			ioErr = Jewel.setJewelFromLine
				(line, jewelList[jewelIndex], game);
			if(ioErr != 0)
			{
				MhagUtil.logLine(mhag,
					"Error Found in Jewel File");
				return;
			}

			// add jewel materials
			if(ifItem)
			{
				String lineItem = inItem.nextLine();
				int startPos = MhagUtil.extractWordPos(lineItem, 0) + 1;
			        int endPos = MhagUtil.extractWordPos(lineItem, startPos);
				jewelList[jewelIndex].setItem(
					MhagUtil.extractWord(lineItem, startPos, endPos));
			}

			jewelIndex++;
		}

	}

	// read armor from armor file
	public void readArmor(Mhag mhag) 
	{
		String file, fileItem;
		Scanner inItem = null;
		file = "data/" + dirSave[mhag.getGame()] + fileArmor;
		fileItem = "data/" + dirSave[mhag.getGame()] + fileArmorItem;
		/*
		if(mhag.getGame() == 0)
			file = dirDataTri + fileArmor;
		else
			file = dirDataP3rd + fileArmor;
		 */

		Scanner in = new Scanner(getClass().getResourceAsStream(file), "UTF-8");
		try {
			inItem = new Scanner(getClass().getResourceAsStream(fileItem), "UTF-8");
		} catch (Exception e) {
			ifItem = false;
		}

		// check total # of skills
		int nMax[] = new int[5];
		Arrays.fill(nMax, 0);
		int nBodyPart = 0;
		int game = mhag.getGame();

		String line = "";
		while(in.hasNext())
		{
			line = in.nextLine();
			if(line.startsWith("#"))continue;
			String word = "";
			word = Armor.getBodyPartFromLine(line, game);
			nBodyPart = Armor.convertBodyPart(word);
			nMax[nBodyPart] += 1;
		}
		String logLine = String.format
			("Total Number of Armors : "
				+Arrays.toString(nMax));
		MhagUtil.logLine(mhag, logLine);
		in.close();

		// find max # of pieces of one part
		int nMax5 = 0;
		for(int i = 0; i < 5 ; i++)
		{
			if(nMax[i] > nMax5)
			{
				nMax5 = nMax[i];
			}
		}
		// System.out.println(nMax5);
		armorList = new Armor[5][nMax5];
		for (int i = 0; i < 5; i++)
		{
			for (int j =0; j < nMax5; j++)
			{
				armorList[i][j] = new Armor();
			}
		}

		Scanner in2 = new Scanner(getClass().getResourceAsStream(file), "UTF-8");
		// read Armor entry
		int ioErr = 0;
		int[] armorIndex = new int[5];
		Arrays.fill(armorIndex, 0);

		while(in2.hasNext())
		{
			line = in2.nextLine();
			if(line.startsWith("#"))continue;
			// System.out.println(Arrays.toString(armorIndex));
			// System.out.println(line);

			String word = "";
			word = Armor.getBodyPartFromLine(line, game);
			nBodyPart = Armor.convertBodyPart(word);
			ioErr = Armor.setArmorFromLine(line, nBodyPart,
				armorList[nBodyPart][armorIndex[nBodyPart]], game);

			// add armor materials
			if(ifItem)
			{
				String lineItem = inItem.nextLine();
				int startPos = MhagUtil.extractWordPos(lineItem, 0) + 1;
			        int endPos = MhagUtil.extractWordPos(lineItem, startPos);
				armorList[nBodyPart][armorIndex[nBodyPart]].setItem(
					MhagUtil.extractWord(lineItem, startPos, endPos));
			}

			if(ioErr != 0)
			{
				MhagUtil.logLine(mhag,
					"Error Found in Armor File");
				//return;
			}
			armorIndex[nBodyPart]++;

		}

	}

	// read charm from charm file
	public void readCharm(int game) 
	{
		try {
			Scanner filein = new Scanner(new File(getDirSave(game) + fileCharm));

			int numMax = nMaxCharm;
			int ind = 0;
			while(filein.hasNext())
			{
				Charm aCharm = new Charm();
				String line = filein.nextLine();
				aCharm.setCharmFromLine(line, this);
				charmList[ind] = aCharm;
				ind++;
				if(ind >= numMax) break;
			}
			numCharm = ind;

		} catch (FileNotFoundException ex) {

			readCharmDefault();
		}
	}

	public void readCharmDefault()
	{
		int numMax = nMaxCharm;

		Charm aCharm = new Charm();
		aCharm.setCharmFromLine("Auto-Guard +10", this);
		charmList[0] = aCharm;
		aCharm = new Charm();
		aCharm.setCharmFromLine("OOO", this);
		charmList[1] = aCharm;
		aCharm = new Charm();
		aCharm.setCharmFromLine("OO", this);
		charmList[2] = aCharm;
		numCharm = 3;
	}

	// copy charm from a set
	public void setCharm(Set set, int ind)
	{
		charmList[ind] = new Charm();
		charmList[ind].setNumSkill(set.getNumCharmSkill());
		charmList[ind].setNumSlot(set.getNumCharmSlot());
		for(int i = 0; i < set.getNumCharmSkill(); i++)
		{
			charmList[ind].setSkillID(i, set.getCharmSkillID(i));
			charmList[ind].setSkillPoint(i, set.getCharmSkillPoint(i));
		}
		charmList[ind].setCharmName(this);
		charmList[ind].setRank(this);
	}

	// get Skill ID, from a skill name
	public int getSkillIDFromName(String skillName)
	{
		int skillID = 0;
		if(skillName.equals("Torso Up"))return -1; // torso up -1
		for (int i = 0; i < Skill.skillIDTot; i++)
		{
			if(skillList[i].getSkillName().equals(skillName))
			{
				skillID = skillList[i].getSkillID();
				break;
			}

		}
		return skillID;
	}

	// write skill reference file
	public void genRefSkill() throws FileNotFoundException
	{
		PrintStream out = new PrintStream(fileRefSkill);
		for (int i = 0; i < Skill.skillIDTot; i++)
		{
			out.printf("%3d: %s\n",skillList[i].getSkillID(),
				skillList[i].getSkillName());
		}
		out.close();
	}

	// write effect reference file
	public void genRefEffect() throws FileNotFoundException
	{
		PrintStream out = new PrintStream(fileRefEffect);
		for (int i = 0; i < Effect.effectIDTot; i++)
		{
			out.printf("%3d: %s\n",effectList[i].getEffectID(),
				effectList[i].getEffectName());
		}
		out.close();
	}

	// write jewel reference file
	public void genRefJewel() throws FileNotFoundException
	{
		PrintStream out = new PrintStream(fileRefJewel);
		for (int i = 0; i < Jewel.jewelIDTot; i++)
		{
			Jewel jewel = jewelList[i];
			if(jewel.getNumSkill() == 2)
			{
				out.printf("%3d: %-20s %s %+d, %s %+d\n",
					jewelList[i].getJewelID(),
					jewelList[i].getJewelName(),
					jewelList[i].getSkillName()[0],
					jewelList[i].getSkillPoint()[0],
					jewelList[i].getSkillName()[1],
					jewelList[i].getSkillPoint()[1]);
			}
			else
			{
				out.printf("%3d: %-20s %s %+d\n",
					jewelList[i].getJewelID(),
					jewelList[i].getJewelName(),
					jewelList[i].getSkillName()[0],
					jewelList[i].getSkillPoint()[0]);
			}
		}
		out.close();
	}

	// write Armor reference file
	public void genRefArmor() throws FileNotFoundException
	{
		PrintStream out = new PrintStream(fileRefArmor);
		for (int i = 0; i < Armor.getArmorMax(); i++)
		{
			String[] armorNames = new String[5];
			Arrays.fill(armorNames, "");

			for (int j = 0; j < 5; j++)
			{
				armorNames[j] = armorList[j][i].getArmorName();
			}
  			out.printf("%3d: %s     %s     %s     %s     %s\n", i,
  				armorNames[0],armorNames[1],armorNames[2],
  				armorNames[3],armorNames[4]);
		}
		out.close();
	}

	// write input file for complete set
	public void genRefCompleteSet() throws FileNotFoundException
	{
		int nMax = Armor.getArmorMax() + 50;
		String[] codeBook = new String[nMax];
		int[] defenseList = new int[nMax];
		boolean[] blade = new boolean[nMax];

		Arrays.fill(codeBook, "");
		Arrays.fill(defenseList, 0);
		Arrays.fill(blade, false) ;

		boolean[][] checked = new boolean[5][nMax];
		for(int i =0; i < 5; i++)
			Arrays.fill(checked[i], false);

		int num = 0; // total number of full set
		int[] armorID = new int[5];  //armor IDs

		for(int i = 0; i < 5; i++) // from head piece first
		{
			for(int j = 0; j< Armor.armorIDTot[i]; j++)
			{
				if(checked[i][j])continue;
				Arrays.fill(armorID, -1);
				armorID[i] = j;
				Armor armor = armorList[i][j];
				String armorName = armor.getArmorName();
				int pos = armorName.indexOf(" ");
				if(pos == -1)
				{
					System.out.println("Error!");
					continue;
				}

				String armor1stWord = armorName.
					substring(0,pos).trim();
				boolean plusSet = true;
				if(armorName.indexOf("+") == -1)
					plusSet = false;
				int defense = armor.getDefense(2);

				// check chest/arm/waist/leg

				for(int k = 1; k < 5; k++)
				{
					for(int jk =0; jk < Armor.armorIDTot[k]; jk++)
					{
						if(checked[k][jk])continue;
						Armor armor2 = armorList[k][jk];
						String armor2Name = armor2.getArmorName();
						if(!armor2Name.contains(armor1stWord))continue;
						if((!plusSet) && armor2Name.contains("+"))continue;
						if((plusSet) && (!armor2Name.contains("+")))continue;
						if(armor2.getDefense(2) != defense)continue;
						armorID[k] = jk;
						break;
					}
				}

				int numFound = 0;
				for(int k =0; k < 5; k++)
				{
					if(armorID[k] != -1)
						numFound++;
				}
				if(numFound <= 1)continue;  //armor set has at least 2 pieces

				// add set

				StringBuilder code = new StringBuilder("");
				String setName = armor.getSetName();

				code.append(setName).append(" :");

				if(armor.getDefense(1) == 0) // G rank
				{
					defenseList[num] = defense;
					code.append(" G");
				}
				else if(armor.getDefense(0) == 0) // high rank
				{
					defenseList[num] = armor.getDefense(1);
					code.append(" H");
				}
				else
				{
					defenseList[num] = armor.getDefense(0);
					code.append(" L");
				}

				for(int k = 1; k < 5; k++) // other than head piece
				{
					if(armorID[k] != -1)
					{
						Armor armor2 = armorList[k][armorID[k]];
						if(armor2.getBladeOrGunner().equals("G"))
						{
							code.append(" G");
							blade[num] = false;
						}
						else
						{
							code.append(" B");
							blade[num] = true;
						}
						break;
					}
				}

				for(int k = 0; k < 5; k++)
				{
					if(armorID[k] == -1)continue;
					checked[k][armorID[k]] = true;
					code.append(" ").append(Armor.partFull.charAt(k));
					code.append(" ").append(String.valueOf(armorID[k]));
					code.append(" 0");
				}

				codeBook[num] = code.toString();

				//System.out.println(code.toString());
				num++;

			}
		}

		//sort based on defense
		int[] index = MhagUtil.sortIndex(num, defenseList);

		PrintStream outBlade = new PrintStream(fileCompleteBlade);
		PrintStream outGunner = new PrintStream(fileCompleteGunner);

		for(int i = num - 1; i > -1 ; i--)
		{
			int j = index[i];
			if(blade[j])
				outBlade.println(codeBook[j]);
			else
				outGunner.println(codeBook[j]);
		}
		outBlade.close();
		outGunner.close();

	}

	// calculator (one input version)
	public void calculator(Mhag mhag) throws FileNotFoundException
	{
		MhagUtil.logLine(mhag, "");
		MhagUtil.logLine(mhag, "Method: MHAG Set Calcualtor");

		Set aSet = new Set();  //create a new set
		aSet.setSetFromFile(mhag, mhag.getFileIn()); //read set

		MhagUtil.logLine(mhag, "Set Code:");
		String code = aSet.getSetCode();   //get set code
		MhagUtil.logLine(mhag, code);

		boolean pass = aSet.checkSet(mhag, this);  //check set
		if(!pass)
		{
			MhagUtil.logLine(mhag, "Error! Please Check!");
			System.exit(0);
		}

		aSet.calcSet(mhag, this);   //calculate set

		// prepare output file
		PrintStream outSave = new PrintStream(mhag.getFileOut());
		Output.init(mhag.getOutFormat(), outSave);

		aSet.save(mhag, this, outSave);  // save results

		Output.close(mhag.getOutFormat(), outSave);
		outSave.close();
	}

	// batch calculator (multiple code input)
	public void batchCalc(Mhag mhag) throws FileNotFoundException
	{
		MhagUtil.logLine(mhag, "");
		MhagUtil.logLine(mhag, "Method: MHAG Batch Calcualtor");

		Set aSet = new Set();  //create set data

  		Scanner in = new Scanner(new File(mhag.getFileIn()));

		PrintStream outSave = new PrintStream(mhag.getFileOut());
		Output.init(mhag.getOutFormat(), outSave);

		int num = 0;
		while (in.hasNext())
		{
			num++;
			MhagUtil.logLine(mhag, "");
			MhagUtil.logLine(mhag, String.format("Set: %d", num));

			String line = in.nextLine();

			boolean pass = aSet.setSetFromCode(mhag, line); //read set

			if(pass) pass = aSet.checkSet(mhag, this);  //check set
			if(!pass)
			{
				MhagUtil.logLine(mhag, "Error! Please Check!");
				continue;
			}

			aSet.calcSet(mhag, this);   //calculate set

			Output.batchHead(mhag.getOutFormat(), outSave, num);
			if(mhag.getOutFormat() <= 1)
				aSet.save(mhag, this, outSave);  // save results
			else
				aSet.saveWiki(mhag, this, outSave); 

		}
		Output.close(mhag.getOutFormat(), outSave);
		outSave.close();
	}

	// get armor class
	public Armor getArmor(int bodyPart, int armorID)
	{
		return armorList[bodyPart][armorID];
	}

	// get jewel class
	public Jewel getJewel(int jewelID)
	{
		return jewelList[jewelID];
	}

	// get effect class
	public Effect getEffect(int effectID)
	{
		return effectList[effectID];
	}

	// get Skill class
	public Skill getSkill(int skillID)
	{
		return skillList[skillID];
	}

	// get Charm class
	public Charm getCharm(int charmID)
	{
		return charmList[charmID];
	}

	//get number of charm
	public int getNumCharm() {return numCharm;}

	public void setNumCharm(int num) {numCharm = num;}

	// get armor list (index copy, name can be got from armorList)
	// sorted by armorName;
	public int[] getArmorList(int rank, boolean blade, boolean female, int bodyPart)
	{
		int nMax = Armor.armorIDTot[bodyPart];
		int[] index = new int[nMax];
		int num = 0;
		for (int i = 0; i < nMax; i++)
		{
			Armor armor = armorList[bodyPart][i];
			if(rank < armor.getRank())continue;
			if(blade && (armor.getBladeOrGunner().equals("G")))continue;
			if((!blade) && (armor.getBladeOrGunner().equals("B")))continue;
			if(female && !armor.ifFemale())continue;
			if(!female && !armor.ifMale())continue;

			index[num] = i;
			num++;
		}

		String[] nameStr =  getArmorListName(bodyPart, female, num, index);

		int[] indNew = MhagUtil.sortIndex(num, nameStr);

		int[] indFinal = new int[num + 1]; // add null
		indFinal[0] = -1;
		for(int i = 0; i < num; i++)
			indFinal[i + 1] = index[indNew[num - 1 - i]];

		return indFinal;
	}

	public List<Armor> getFullArmorList(int bodyPart)
	{
		int nMax = Armor.armorIDTot[bodyPart];
		List<Armor> armourList = new ArrayList<Armor>();

		for (int i = 0; i < nMax; i++)
		{
			Armor armor = armorList[bodyPart][i];
			if(armor != null) {
				armourList.add(armor);
			}
		}
		return armourList;
	}

	public String[] getArmorListName(int bodyPart, boolean female,
		int num, int[] index)
	{
		String[] nameStr = new String[num];
		for(int i = 0; i < num; i++)
		{
			String armorName  = armorList[bodyPart][index[i]].getArmorName();

			int pos = armorName.indexOf("/");
			if(pos == - 1)
				nameStr[i] = armorName;
			else
			{
				if(female)
					nameStr[i] = armorName.substring(pos + 1).trim();
				else
					nameStr[i] = armorName.substring(0, pos).trim();
			}
		}
		return nameStr;
	}

//	generate menu list
	public String[] getArmorListMenu(int bodyPart, boolean female,
		int num, int[] index, int language)
	{
		String[] nameStr = new String[num];
		nameStr[0] = "";
		for(int i = 1; i < num ; i++)  //first 1 is null;
		{
			String armorName  = "";
			if(language == 0) // english
				armorName = armorList[bodyPart][index[i]].getArmorName();
			else
				armorName = armorList[bodyPart][index[i]].getArmorNameJP();

			int pos = armorName.indexOf("/");
			if(pos == - 1)
				nameStr[i] = armorName;
			else
			{
				if(female)
					nameStr[i] = armorName.substring(pos + 1).trim();
				else
					nameStr[i] = armorName.substring(0, pos).trim();
			}
		}
		return nameStr;
	}

	public int[] getJewelList(int rank, int nSlot)
	{
		int nMax = Jewel.jewelIDTot;
		int[] index = new int[nMax];
		String[] nameStr =  new String[nMax];
		int num = 0;
		for (int i = 0; i < nMax; i++)
		{
			Jewel jewel = jewelList[i];
			if(rank < jewel.getRank())continue;
			if(jewel.getNumSlot() > nSlot)continue;

			index[num] = i;
			//nameStr[num] = jewel.getJewelNameShort();
			nameStr[num] = jewel.getJewelNameSkill(this, 0);  //sorted by english name
			num++;
		}


		int[] indNew = MhagUtil.sortIndex(num, nameStr);

		int[] indFinal = new int[num + 1]; // add null
		indFinal[0] = -1;
		for(int i = 0; i < num; i++)
			indFinal[i + 1] = index[indNew[num - 1 - i]];

		return indFinal;
	}

	public int[] getSkillList(int rank, int nSlot)
	{
		int nMax = Skill.skillIDTot;
		int[] index = new int[nMax];
		String[] nameStr =  new String[nMax];
		int num = 0;
		for (int i = 0; i < nMax; i++)
		{
			Skill skill = skillList[i];
			if(skill.getMaxSkillPoint(rank, nSlot) == 0)continue;

			index[num] = i;
			nameStr[num] = skill.getSkillName();
			num++;
		}

		int[] indNew = MhagUtil.sortIndex(num, nameStr);

		int[] indFinal = new int[num];  //num + 1]; // don't add null
//		indFinal[0] = -1;
		for(int i = 0; i < num; i++)
			indFinal[i] = index[indNew[num - 1 - i]];

		return indFinal;
	}

	// get list and exclude one exception
	public int[] getSkillList(int rank, int nSlot, int exception)
	{
		int nMax = Skill.skillIDTot;
		int[] index = new int[nMax];
		String[] nameStr =  new String[nMax];
		int num = 0;
		for (int i = 0; i < nMax; i++)
		{
			Skill skill = skillList[i];
			if(skill.getMaxSkillPoint(rank, nSlot) == 0)continue;
			if(skill.getSkillID() == exception)continue;

			index[num] = i;
			nameStr[num] = skill.getSkillName();
			num++;
		}

		int[] indNew = MhagUtil.sortIndex(num, nameStr);

		int[] indFinal = new int[num];  //num + 1]; // don't add null
//		indFinal[0] = -1;
		for(int i = 0; i < num; i++)
			indFinal[i] = index[indNew[num - 1 - i]];

		return indFinal;
	}

	// get list and exclude two exceptions
	public int[] getSkillList(int rank, int nSlot, int except1, String skillExcept2)
	{
		int nMax = Skill.skillIDTot;
		int[] index = new int[nMax];
		String[] nameStr =  new String[nMax];
		int num = 0;
		for (int i = 0; i < nMax; i++)
		{
			Skill skill = skillList[i];
			if(skill.getMaxSkillPoint(rank, nSlot) == 0)continue;
			if(skill.getSkillID() == except1)continue;
			if(skill.getSkillName().equals(skillExcept2))continue;

			index[num] = i;
			nameStr[num] = skill.getSkillName();
			num++;
		}

		int[] indNew = MhagUtil.sortIndex(num, nameStr);

		int[] indFinal = new int[num];  //num + 1]; // don't add null
//		indFinal[0] = -1;
		for(int i = 0; i < num; i++)
			indFinal[i] = index[indNew[num - 1 - i]];

		return indFinal;
	}

	// get skill tree list based on skill type
	public int[] getSkillList(int typeID)
	{
		int nMax = Skill.skillIDTot;
		int[] index = new int[nMax];
		String[] nameStr =  new String[nMax];
		int num = 0;
		for (int i = 0; i < nMax; i++)
		{
			Skill skill = skillList[i];
			if((typeID == 0) || (skill.getSkillType() == typeID))
			{
				index[num] = i;
				nameStr[num] = skill.getSkillName();
				num++;
			}
		}

		int[] indNew = MhagUtil.sortIndex(num, nameStr);

		int[] indFinal = new int[num];

		for(int i = 0; i < num; i++)
			indFinal[i] = index[indNew[num - 1 - i]];

		return indFinal;
	}

    public Skill[] getSkillList() {
        return skillList;
    }

	// get skill (effect) list based on skill type
	public int[] getEffectList(int id)
	{
		Skill skill = skillList[id];
		int num = skill.getNumEffect();
		int numNow = 0;
		for(int i = 0; i < num; i++)
		{
			if(skill.getEffectTrigger(i) < 0)continue;
			numNow++;
		}
		int[] ind = new int[numNow];
		numNow = 0;
		for(int i = 0; i < num; i++)
		{
			if(skill.getEffectTrigger(i) < 0)continue;
			ind[numNow] = skill.getEffectID(i);
			numNow++;
		}

		return ind;
	}
    
    public Effect[] getEffectList() {
        return effectList;
    }

	public String getDirSave(int game) {return dirSave[game];}

	public int getMaxRank(int game) {return maxRank[game];}

	public boolean getIfItem() {return ifItem;}

	public static int getNMaxCharm() {return nMaxCharm;}

	public static String getFileCharm() {return fileCharm;}

	// Constants for file names
	private final String[] dirSave = {"mhtri/", "mhp3rd/", "mhfu/", "mh3g/", "mh4/", "mhgen/"};
	private final String fileArmor = "armor.dat";
	private final String fileJewel = "jewel.dat";
	private final String fileSkill = "skill.dat";
	private final String fileArmorItem = "armor_item.dat";
	private final String fileJewelItem = "jewel_item.dat";
//	private final String fileCharm = dirData+"charm.dat";
	private final String dirRef = "reference/";
	private final String fileRefArmor = dirRef+"ref_armor.dat";
	private final String fileRefJewel = dirRef+"ref_jewel.dat";
	private final String fileRefSkill = dirRef+"ref_skill.dat";
//	private final String fileRefCharm = dirRef+"ref_charm.dat";
	private final String fileRefEffect = dirRef+"ref_effect.dat";
//	private final String fileRefSkillClass = dirRef+"ref_skill_class.dat";
	private final String fileCompleteBlade = dirRef+"blade_sets.input";
	private final String fileCompleteGunner = dirRef+"gunner_sets.input";

  	static String fileCharm = "mycharm.dat";
	
	private final int[] maxRank = {1, 1, 2, 2, 2, 1}; //max rank: high for mhtri and mhp3rd, G for mhfu and mh3g and mh4g

	// Some Constants
	static String emptyName = "-----";
	static int nMaxCharm = 5000;

	// data
	private Armor[][] armorList;
	private Skill[] skillList;
	private Jewel[] jewelList;
	private Effect[] effectList;
	private Charm[] charmList = new Charm[nMaxCharm];
	private int numCharm = 0;
	private boolean ifItem = true;

	// indeces
//	private int[][] indexSkillInClass;  // skill id list
//	private int[] numSkillInClass;  // number skill in class

}
