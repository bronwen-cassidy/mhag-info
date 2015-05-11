package org.mhag.swing;

import java.util.Arrays;
import org.mhag.model.*;
import org.mhag.swing.MhagGui.Task;

/**
 * @program MHAG
 * @ Generator GUI Class
 * @version 1.0
 * @author Tifa@mh3
 */

public class GenGUI {

	public GenGUI(Generator aGen, Mhag aMhag, MhagData aMhagData)
	{
		gen = aGen;
		mhag = aMhag;
		mhagData = aMhagData;
	}

	// generator main operator for gui, output set code
	public String[] genMainGui(Task task, MhagGui mhagGui, Set aSet)
	{
		boolean ifBlade = true;
		boolean ifFemale = false;
		int rank = mhagData.getMaxRank(mhag.getGame());
		int[][] searchSpace = new int[6][100];  //indeces of possible pieces
		int[] searchNum = new int[6];  //number of possible pieces
		for(int i = 0; i < 6; i++)
			Arrays.fill(searchSpace[i], -1);
		Arrays.fill(searchNum, 0);

		int num = 0;
		if((gen.getGenMode() == 2) || (gen.getOptMethod() == 1))
			num = 1;
		else
			num = gen.getNumOptSet();

		//if((genMode <= 1) && ifCharm)
		//	genCharmList();

		String[] setCodes = new String[num];

		if(gen.getGenMode() == 2)  // jewel optimization
		{
			gen.initJewel(aSet.getRank());
			/*
			System.out.println(numEffectOpt);
			System.out.println(Arrays.toString(skills));
			System.out.println(Arrays.toString(effects));
			System.out.println(Arrays.toString(triggers));
			System.out.println(aSet.getRank());
			 */
			int score = 0;
			//int progress = 1;
			Set genSet = new Set();

			genSet.copySetMin(aSet);  //get Set

			if(!genSet.getBlade() && gen.getIfCheckGun() && 
					((gen.getNumWeaponSlot() == 2) || (gen.getNumWeaponSlot() == 3)))
				gen.setIfSlotGunPart(true);
			else
				gen.setIfSlotGunPart(false);

			//genSet.setRate(this);
			score = gen.setOptimizer(genSet);

			genSet.setSetName(String.format("Set 1 (%d)", score));
			setCodes[0] = genSet.getSetCode();
			//System.out.println(setCodes[0]);
			/*
			mhag.setLogOpt(0);
			mhag.setOutLog(System.out);
			genSet.calcSet(mhag, mhagData);
			 */
		}
		else if(gen.getGenMode() == 1) //  partial set search
		{
			rank = aSet.getRank();
			ifBlade = aSet.getBlade();
			ifFemale = aSet.getFemale();
			gen.initJewel(rank);

			if(!ifBlade && gen.getIfCheckGun() &&
					((gen.getNumWeaponSlot() == 2) || (gen.getNumWeaponSlot() == 3)))
				gen.setIfSlotGunPart(true);
			else
				gen.setIfSlotGunPart(false);

			for(int bodyPart = 0; bodyPart < 5; bodyPart++)
			{
				if(aSet.getInUse(bodyPart))
				{
					//only one
					searchNum[bodyPart] = 1;
					searchSpace[bodyPart][0] = aSet.getArmorID(bodyPart);
				}
				else
				{
					int[] temp = gen.getArmorList(rank, ifBlade, ifFemale, bodyPart);
					searchNum[bodyPart] = temp.length;
					System.arraycopy(temp, 0, searchSpace[bodyPart], 0, temp.length);
				}
			}

			if(aSet.getInUse(6))
			{
				searchNum[5] = 1;
				searchSpace[5][0] = 0;
				mhagData.setCharm(aSet, 0);
				mhagData.setNumCharm(1);
			}
			else
			{
				if(gen.getIfCharm())
					mhagData.readCharm(mhag.getGame());
				else
					mhagData.readCharmDefault();

				int[] temp = gen.getCharmList(rank);
				searchNum[5] = temp.length;
				System.arraycopy(temp, 0, searchSpace[5], 0, temp.length);
			}

			setCodes = genSearch(task, mhagGui, rank, ifBlade, searchNum, searchSpace);
		}
		else  // full set search
		{
			if(gen.getArmorRankOpt() == 1)  //if low rank pieces only
				rank =  0;
			else if(gen.getArmorRankOpt() == 2)  //if high rank pieces only
				rank = 1;
			else if(gen.getArmorRankOpt() == 4)  // if high/low rank pieces
				rank = 1;

			ifBlade = gen.getBlade();
			ifFemale = gen.getFemale();
			gen.initJewel(rank);

			if(!ifBlade && gen.getIfCheckGun() &&
					((gen.getNumWeaponSlot() == 2) || (gen.getNumWeaponSlot() == 3)))
				gen.setIfSlotGunPart(true);
			else
				gen.setIfSlotGunPart(false);

			for(int bodyPart = 0; bodyPart < 5; bodyPart++)
			{
				int[] temp = gen.getArmorList(rank, ifBlade, ifFemale, bodyPart);
				searchNum[bodyPart] = temp.length;
				System.arraycopy(temp, 0, searchSpace[bodyPart], 0, temp.length);
			}

			if(gen.getIfCharm())
				mhagData.readCharm(mhag.getGame());
			else
				mhagData.readCharmDefault();

			int[] temp = gen.getCharmList(rank);
			searchNum[5] = temp.length;
			System.arraycopy(temp, 0, searchSpace[5], 0, temp.length);

			setCodes = genSearch(task, mhagGui, rank, ifBlade, searchNum, searchSpace);
		}

		return setCodes;
	}

	public String[] genSearch(Task task, MhagGui mhagGui, int rank, boolean ifBlade,
			int[] searchNum, int[][] searchSpace) 
	{
		if(gen.getOptMethod() == 0)
			return full(task, mhagGui, rank, ifBlade, searchNum,searchSpace);
		else if(gen.getOptMethod() == 1)
			return sa(task, mhagGui, rank, ifBlade, searchNum, searchSpace);
		else
			return ga(task, mhagGui, rank, ifBlade, searchNum, searchSpace);
	}

	//numerate sets , full search algorithm
	public String[] full(Task task, MhagGui mhagGui, int rank, boolean blade,
			int[] searchNum, int[][] searchSpace)
	{
		// stored set data
		String[] bestSets = new String[gen.getNumOptSet()];
		for(int i = 0; i < gen.getNumOptSet(); i++)
			bestSets[i] = "";

		// stored scoring
		int[] bestScores = new int[gen.getNumOptSet()];
		Arrays.fill(bestScores, 0);

		// working set data
		Set aSet = new Set();

		//System.out.println(Arrays.toString(searchNum));
		int nTot = 1;
		for(int i = 0; i < 6; i++)
			nTot *= searchNum[i];
		//System.out.println(nTot);
		int[] indices = new int[6];

		int progress = 1;
		int max = mhagGui.getProgressBar().getMaximum();
		for (int i = 0; i < nTot; i++)
		{
			if(task.isCancelled())return null;
			if(i == nTot/max * progress)
			{
				mhagGui.getProgressBar().setValue(progress);
				progress++;
			}

			indices = MhagUtil.getIndexArray(i, searchNum); //Armor.getArmorIDTot());
			aSet.init();
			gen.input(rank, blade, aSet, indices, searchSpace);

			int score = gen.setOptimizer(aSet);

			gen.updateBest(bestSets, bestScores, aSet, score);

		}
		/*
		bestSet.calcSet(mhag, mhagData);
		bestSet.save(mhag, mhagData, System.out);  // save results
		System.out.println(max);
		 */

		// update set names (final process)
		for(int i = 0; i < gen.getNumOptSet(); i++)
		{
			aSet.init();
			aSet.setSetFromCode(mhag, bestSets[i]);
			aSet.setRate(gen);
			int score = aSet.getRate();
			aSet.setSetName(String.format("Set %d (%d)", i + 1, score));
			bestSets[i] = aSet.getSetCode();
		}

		return bestSets;
	}

	// genetic algorithm
	public String[] ga(Task task, MhagGui mhagGui, int rank, boolean blade,
			int[] searchNum, int[][] searchSpace)
	{
		return null;
	}

	//simulated annealing
	public String[] sa(Task task, MhagGui mhagGui, int rank, boolean blade,
			int[] searchNum, int[][] searchSpace) 
	{
		int numStep = 10000;
		double t0 = 100.0/numStep;
		int[] indices = gen.initialIndex(searchNum, searchSpace);
		Set setOld = new Set();
		Set aSet = new Set();
		aSet.init();
		gen.input(rank, blade, aSet, indices, searchSpace);
		int score = gen.setOptimizer(aSet);

		int progress = 1;
		int max = mhagGui.getProgressBar().getMaximum();

		for(int i = 0; i < numStep; i++)
		{
			if(task.isCancelled())return null;
			if(i == numStep/max * progress)
			{
				mhagGui.getProgressBar().setValue(progress);
				progress++;
			}
			//backup
			setOld.copySet(aSet);
			int scoreOld = score;
			int part = gen.getRand().nextInt(6);
			int indexOld = indices[part];
			//change
			indices[part] = gen.getRand().nextInt(searchNum[part]);
			aSet.init();
			gen.input(rank, blade, aSet, indices, searchSpace);
			score = gen.setOptimizer(aSet);

			//Monte Carlo step
			if((score > scoreOld) || 
					(gen.getRand().nextDouble() <= Math.exp((score - scoreOld)/t0/(numStep - 1 - i))))
			{
			}
			else
			{
				aSet.copySet(setOld);
				score = scoreOld;
				indices[part] = indexOld;
			}
		}

		aSet.setSetName(String.format("Set 1 (%d)", score));
		String[] bestSets = new String[1];
		bestSets[0] = aSet.getSetCode();
		return bestSets;
	}

	private Generator gen;
	private Mhag mhag;
	private MhagData mhagData;
}
