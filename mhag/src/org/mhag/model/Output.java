package org.mhag.model;

import java.io.PrintStream;

/**
 * @program MHAG
 * @ Outout Class
 * @version 1.0
 * @author Tifa@mh3
 */
public class Output {

	// output html head when create a new HTML file
	public static void init(int outForm, PrintStream outSave)
	{
		if(outForm == 1)
			initHTML(outSave);
		else if(outForm == 2)
			initHTMLWiki(outSave);
	}

	public static void initHTML(PrintStream outSave)
	{
		outSave.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		outSave.println("<HTML>");
		outSave.println("<HEAD>");
		outSave.println("<TITLE> Monster Hunter Armor Generator</TITLE>");
		outSave.println("<style type=\"text/css\">");
		outSave.println("body{background-color:lightyellow; margin:0;padding:0}");
		outSave.println(" </style>");
		outSave.println("</HEAD>");
		outSave.println("<BODY>");
		outSave.println("<table height=\"100%\" width=600 align=\"center\" vligh=\"top\" border=0>");
		outSave.println("<tr><td><h1 align=\"center\">Monster Hunter Armor Book</h1>");
		outSave.println("<p align=\"center\">geneated by MHAG v2.1</p></td></tr>");
		outSave.println("<tr><td>&nbsp;</td></tr>");
		outSave.println("</table>");
	}

	public static void initHTMLWiki(PrintStream outSave)
	{
		outSave.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		outSave.println("<HTML>");
		outSave.println("<HEAD>");
		outSave.println("<TITLE> Monster Hunter Armor Generator</TITLE>");
		outSave.println("<style type=\"text/css\">");
		outSave.println("body{background-color:black; margin:0;padding:0}");
		outSave.println(" </style>");
		outSave.println("<meta http-equiv=\"Content=Type\" content=\"text/html; charset=URF-8\"/>");
		outSave.println("</HEAD>");
		outSave.println("<BODY>");
	}

	// close html
	public static void close(int outForm, PrintStream outSave)
	{
		if(outForm != 0)
			closeHTML(outSave);
	}

	public static void closeHTML(PrintStream outSave)
	{
		outSave.println("</BODY>");
		outSave.println("</HTML>");
	}

	// output head title

	public static void head(int outForm, PrintStream outSave, String setName,
		int rank, boolean blade)
	{
		if(outForm == 0)
			headTEXT(outSave, setName, rank, blade);
		else if(outForm == 1)
			headHTML(outSave, setName, rank, blade);
	}

	public static void headTEXT(PrintStream outSave, String setName,
		int rank, boolean blade)
	{
		String title = new String();
		if(rank == 0)
			title = "Low Rank";
		else if(rank == 1)
			title = "High Rank";
		else
			title = "G Rank";

		String title2 = new String();
		if(blade)
			title2 = "Blademaster";
		else
			title2 = "Gunner";

		outSave.println(splitter1);
		String line = String.format(
			" %-40s%-12s%-12s", setName, title, title2);
		outSave.println(line);
		outSave.println(splitter2);

	}

	public static void headHTML(PrintStream outSave, String setName, int rank, boolean blade)
	{
		String color, title, title2;
		if(rank == 0)
		{
			color = "orange";
			title = "Low Rank";
		}
		else if(rank == 1)
		{
			color = "orangered";
			title = "High Rank";
		}
		else
		{
			color = "red";
			title = "G Rank";
		}

		if(blade)
			title2 = "Bladermaster";
		else
			title2 = "Gunner";

	      outSave.println("<table border=\"1\" cellpadding=\"2\" width=\"650\" rules=\"rows\" frame=\"box\">");
	      outSave.printf("<tr><td colspan=\"8\"><b>%s</b></td>\n", setName);
	      outSave.printf("<td colspan=\"3\"><font color=\"%s\">%s</font></td><td>%s</td></tr>\n",
		      color,title,title2);
	}

	public static void headHTMLWiki(PrintStream outSave, String setName, int rank, boolean blade)
	{
		String color, title, title2;
		if(rank == 0)
		{
			color = "orange";
			title = "Low Rank";
		}
		else if(rank == 1)
		{
			color = "orangered";
			title = "High Rank";
		}
		else
		{
			color = "red";
			title = "G Rank";
		}

		if(blade)
			title2 = "Bladermaster";
		else
			title2 = "Gunner";

		outSave.printf("<h3><font color = \"white\">%s, %s, %s</font></h3>\n", setName, title, title2);
	    outSave.println("<table border=\"1\" bordercolor=white cellpadding=\"2\" width=\"900\" rules=\"rows\" frame=\"box\">");
		outSave.println("<tr><td colspan=\"10\"><table border=\"0\" cellpadding=\"0\" width=\"900\" rules=\"rows\">");
		/*
	    outSave.printf("<tr><td colspan=\"8\"><b>%s</b></td>\n", setName);
	    outSave.printf("<td colspan=\"3\"><font color=\"%s\">%s</font></td><td>%s</td></tr>\n",
		    color,title,title2);
		 */
	}

	// convert # of slots to a word
	public static String slotWord(int outForm, int numSlot)
	{
		if(outForm == 0)
			return slotWordTEXT(numSlot);
		else
			return slotWordHTML(numSlot);
	}

	public static String slotWordTEXT(int numSlot)
	{
		if(numSlot == 1)
		{
			return "O--";
		}
		else if(numSlot == 2)
		{
			return "OO-";
		}
		else if(numSlot == 3)
		{
			return "OOO";
		}
		else
		{
			return "---";
		}
	}

	public static String slotWordHTML(int numSlot)
	{
		if(numSlot == 1)
		{
			return "O";
		}
		else if(numSlot == 2)
		{
			return "OO";
		}
		else if(numSlot == 3)
		{
			return "OOO";
		}
		else
		{
			return "";
		}

	}

	// output weapon

	public static void weapon(int outForm, PrintStream outSave,
		String slots, String[] jewels)
	{
		if(outForm == 0)
			weaponTEXT(outSave, slots, jewels);
		else if(outForm == 1)
			weaponHTML(outSave, slots, jewels);
	}

	public static void weaponTEXT(PrintStream outSave, String slots,
		String[] jewels)
	{
		String line = String.format
			(" %-40s%3s   %s %s %s","Weapon",slots,
			jewels[0],jewels[1],jewels[2]);
		outSave.println(line);
	}

	public static void weaponHTML(PrintStream outSave, String slots, String[] jewels)
	{
		outSave.println("<tr><td colspan=\"12\"><table border=\"0\" cellpadding=\"0\" width=\"650\" rules=\"none\">");
		outSave.printf("<tr><td width=300>Weapon</td><td align=\"center\" width=\"30\"><font size=\"2\">" +
			"%s</font></td><td width=\"30\"></td><td width=240>%s&nbsp;%s&nbsp;%s&nbsp;</td></tr>\n",
			slots,jewels[0],jewels[1],jewels[2]);
	}

	// output armor

	public static void armor(int outForm, PrintStream outSave,
		String title, String slots, String[] jewels)
	{
		if(outForm == 0)
			armorTEXT(outSave, title, slots, jewels);
		else if(outForm == 1)
			armorHTML(outSave, title, slots, jewels);
	}

	public static void armorTEXT(PrintStream outSave, 
		String title, String slots, String[] jewels)
	{
		String line = String.format
			(" %-40s%3s   %s %s %s",title,slots,
			jewels[0],jewels[1],jewels[2]);
		outSave.println(line);
	}

	public static void armorHTML(PrintStream outSave,
		String title,String slots, String[] jewels)
	{

		outSave.printf("<tr><td>%s</td><td align=\"center\"><font size=\"2\">" +
			"%s</font></td><td></td><td>%s&nbsp;%s&nbsp;%s&nbsp;</td></tr>\n",
			title,slots,jewels[0],jewels[1],jewels[2]);
	}

	public static void armorHTMLWiki(PrintStream outSave,
		String name, String nameJP, String slots, String items)
	{
		outSave.printf("<tr style=\"color:white\"><td width=190>%s</td>"
				+ "<td width=190>%s</td><td align=\"center\"><font size=\"2\">" +
			"%s</font></td><td width=\"30\"></td><td width = 490>%s</td></tr>\n",
			name, nameJP, slots, items);
	}

	// output charm

	public static void charm(int outForm, PrintStream outSave,
		String title, String slots, String[] jewels)
	{
		if(outForm == 0)
			charmTEXT(outSave, title, slots, jewels);
		else if(outForm == 1)
			charmHTML(outSave, title, slots, jewels);
	}

	public static void charmTEXT(PrintStream outSave,
		String title, String slots, String[] jewels)
	{
		String line = String.format
			(" %-40s%3s   %s %s %s",title,slots,
			jewels[0],jewels[1],jewels[2]);
		outSave.println(line);
		outSave.println(splitter2);
	}

	public static void charmHTML(PrintStream outSave,
		String title,String slots, String[] jewels)
	{
		armorHTML(outSave, title, slots, jewels);
	}

	// output 2nd head line
	public static void head2nd(int outForm, PrintStream outSave)
	{
		if(outForm == 0)
			head2ndTEXT(outSave);
		else if(outForm == 1)
			head2ndHTML(outSave);
	}

	public static void head2ndTEXT(PrintStream outSave)
	{
		String line = String.format
			(" %20s%3s %3s %3s %3s %3s %3s %3s %3s",
			" ","WEP","HEA","CHE","ARM","WAI","LEG","CHA","TOT");
		outSave.println(line);
	}

	public static void head2ndHTML(PrintStream outSave)
	{
		outSave.println("</table></td></tr>");

		outSave.println("<tr align=\"right\" style=\"font-size:7pt\"><td colspan=\"2\"></td>");
		outSave.println("<td height=\"20\">WEP</td><td>HEAD</td><td>CHEST</td><td>ARM</td><td>WAIST</td><td>LEG</td>");
		outSave.println("<td>CHM</td><td>TOT</td><td colspan=\"2\"></td></tr>");
	}

	public static void head2ndHTMLWiki(PrintStream outSave)
	{
		outSave.println("</table></td></tr>");

     		outSave.println("<tr align=\"center\" style=\"font-size:9pt\"><td rowspan=\"7\" width=50></td>");
     		outSave.println("<td rowspan=\"7\" width=120></td><td width=120></td>");
	        outSave.printf("<td height=\"20\" width=50><img src=\"%s\"></td>"
					+ "<td width=50><img src=\"%s\"></td><td width=50><img src=\"%s\"></td><td width=50>"
					+ "<img src=\"%s\"></td><td width=50><img src=\"%s\"></td>",urlHead, urlChest, urlArm, urlWaist, urlLeg);
	        outSave.println("<td width=50><font color=\"white\">TOT</font></td><td></td></tr>");
	}

	// output defense

	public static void defense(int outForm, PrintStream outSave,
		String title, int[] values, int def, String bonusTitle, int[] bonus)
	{
		if(outForm == 0)
			defenseTEXT(outSave, title, values, def, bonusTitle);
		else if(outForm == 1)
			defenseHTML(outSave, title, values, def, bonus);

	}

	public static void defenseTEXT(PrintStream outSave, String title,
		int[] values, int def, String bonusTitle)
	{
		String line = String.format
			(" %-20s%3s %3d %3d %3d %3d %3d %3s %3d %s",
			title,nan,values[0],values[1],values[2],values[3],values[4],
			nan,def,bonusTitle);
		outSave.println(line);
	}

	public static void defenseHTML(PrintStream outSave, String title,
		int[] value, int def, int[] bonus)
	{

	        outSave.printf("<tr align=\"right\"><td colspan=\"2\" align=\"left\">%s</td>\n",title);

		outSave.printf("<td>---");
		for(int i = 0; i < 5; i++)
		{
			outSave.printf("</td><td>%d",value[i]);
		}
		outSave.printf("\n");
		outSave.printf("</td><td>---</td><td>%d</td>\n",def);
		outSave.println("<td align=\"left\" colspan=\"2\" rowspan=\"6\">");
		outSave.println("<table width=\"20\" border=\"0\" cellpadding=\"2\" rules=\"none\">");

		//add bonus table

		boolean ifBonus = Set.ifSkillBonus(bonus);

		if(ifBonus)
		{
			// defense
			if(bonus[5] > 0)
				outSave.println("<tr align=\"center\"><td>&nbsp;&uarr;</td></tr>\n");
			else if(bonus[5] < 0)
				outSave.println("<tr align=\"center\"><td>&nbsp;&darr;</td></tr>\n");
			else
				outSave.println("<tr><td>&nbsp;</td></tr>\n");

			// elements
			for(int i = 0; i < 5; i++)
			{
				if(bonus[i] > 0)
				       outSave.println("<tr align=\"center\"><td>&nbsp;&uarr;</td></tr>");
				else if(bonus[i] < 0)
				       outSave.println("<tr align=\"center\"><td>&nbsp;&darr;</td></tr>");
				else
				       outSave.println("<tr><td>&nbsp;</td></tr>");
			}
		}
		outSave.println("</table></td></tr>");
	}

	public static void defenseHTMLWiki(PrintStream outSave, String title,
		int[] value, int def, int[] bonus)
	{

		outSave.printf("<tr style=\"color:white\" align=\"center\"><td align=\"right\">%s</td>\n",title);

		for(int i = 0; i < 5; i++)
		{
			outSave.printf("</td><td>%d",value[i]);
		}
		outSave.printf("\n");
		outSave.printf("</td><td>%d</td>\n",def);
		outSave.println("<td align=\"left\" rowspan=\"6\">");
		// no bonus 
		outSave.println("</td></tr>");
	}

	// output resist

	public static void resist(int outForm, PrintStream outSave, int resistInd,
		String title, int[] values, int res, String bonusTitle)
	{
		if(outForm == 0)
			resistTEXT(outSave, resistInd, title, values, res, bonusTitle);
		else if(outForm == 1)
			resistHTML(outSave, resistInd, values, res);
	}

	public static void resistTEXT(PrintStream outSave, int resistInd, String title,
		int[] values, int res, String bonusTitle)
	{
		String line = String.format
			(" %-20s%3s %3d %3d %3d %3d %3d %3s %3d %s",
			title,nan,values[0],values[1],values[2],values[3],values[4],
			nan,res,bonusTitle);
		outSave.println(line);
		if(resistInd == 4)
			outSave.println(splitter2);
	}

	public static void resistHTML(PrintStream outSave, int resistInd,
		int[] value, int res)
	{
		if(resistInd == 0)  // fire
		{
			outSave.println("<tr style=\"color:red\" align=\"right\">" +
				"<td rowspan=\"5\" valign=\"top\" align=\"left\">");
			outSave.println("<font color=\"black\">Resist</font></td>" +
				"<td align=\"left\"><font color=\"red\">Fire</color>");

			outSave.printf("<td>---");
			for(int i = 0; i < 5; i++)
			{
				outSave.printf("</td><td>%d",value[i]);
			}
			outSave.printf("\n");
			outSave.printf("</td><td>---</td><td>%d</td></tr>\n",res);
		}
		else
		{
			String titleNew,color;
			if(resistInd == 1)
			{
				titleNew = "Water";
				color= "blue";
			}
			else if (resistInd == 2)
			{
				titleNew = "Ice";
				color= "darkcyan";
			}
			else if (resistInd == 3)
			{
				titleNew = "Thunder";
				color= "orange";
			}
			else
			{
				titleNew = "Dragon";
				color= "purple";
			}
			outSave.printf("<tr style=\"color:%s\" align=\"right\"><td align=\"left\">%s</td><td>---\n",
				color,titleNew);
			for(int i = 0; i < 5; i++)
			{
				outSave.printf("</td><td>%d",value[i]);
			}
			outSave.printf("\n");
			outSave.printf("</td><td>---</td><td>%d</td></tr>\n",res);
		}
	}

	public static void resistHTMLWiki(PrintStream outSave, int resistInd,
		int[] value, int res)
	{
		if(resistInd == 0)  // fire
		{
			outSave.printf("<tr align=\"center\">" +
				"<td align=\"right\"><img src=\"%s\"></td>\n", urlFire);

			for(int i = 0; i < 5; i++)
			{
				outSave.printf("</td><td><font color=\"red\">%d</font>",value[i]);
			}
			outSave.printf("\n");
			outSave.printf("</td><td><font color=\"red\">%d</font></td></tr>\n",res);
		}
		else
		{
			String titleNew,color;
			if(resistInd == 1)
			{
				titleNew = urlWater;
				color= "blue";
			}
			else if (resistInd == 2)
			{
				titleNew = urlIce;
				color= "cyan";
			}
			else if (resistInd == 3)
			{
				titleNew = urlThunder;
				color= "yellow";
			}
			else
			{
				titleNew = urlDragon;
				color= "fuchsia";
			}
			outSave.printf("<tr align=\"center\"><td align=\"right\"><img src=\"%s\">\n",
				titleNew);
			for(int i = 0; i < 5; i++)
			{
				outSave.printf("</td><td><font color=\"%s\">%d</font>",color,value[i]);
			}
			outSave.printf("\n");
			outSave.printf("</td><td><font color=\"%s\">%d</font></td></tr>\n",color, res);
		}
	}

	// output torso up skill
	public static void torso(int outForm, PrintStream outSave,
		String[] torsoupList, int nSkill)
	{
		if(outForm == 0)
			torsoTEXT(outSave, torsoupList);
		else if(outForm == 1)
			torsoHTML(outSave, torsoupList, nSkill);
	}

	public static void torsoTEXT(PrintStream outSave, String[] list)
	{
		String line = String.format
			(" %-20s%3s %3s %3s %3s %3s %3s %3s %3s",
			"Skills: Torso Up",list[0],list[1],list[2],list[3],
			list[4],list[5],list[6],list[7]);
		outSave.println(line);
	}

	public static void torsoHTML(PrintStream outSave, String[] torsoupList
		, int nSkill)
	{
		outSave.printf("<tr align=\"right\"><td width=\"50\" rowspan=\"%d\""+
			" valign=\"top\" align=\"left\">Skill</td>\n",nSkill+1);
		outSave.println("<td width=\"100\" align=\"left\">Torso Up");
		for (int i =0; i < 8; i++)
		{
			outSave.printf("</td><td width=\"30\"><font color=\"" +
				"green\">%s</font>",torsoupList[i]);
		}
	        outSave.printf("</td><td width=\"20\"></td><td align=\"left\"></td></tr>");
	}

	public static void torsoHTMLWiki(PrintStream outSave, String[] torsoupList
		, int nSkill)
	{
		outSave.printf("<tr style=\"color:white\" align=\"center\"><td width=\"50\" rowspan=\"%d\""+
			" valign=\"top\" align=\"left\">Skill</td>\n",nSkill+1);
		outSave.println("<td width=\"100\" align=\"left\">Torso Up</td><td width=\"/100\">");
		for (int i =1; i < 6; i++)
		{
			outSave.printf("</td><td width=\"50\"><font color=\"" +
				"green\">%s</font>",torsoupList[i]);
		}
		outSave.printf("</td><td width=\"50\"><font color=\"" +
			"green\">%s</font>",torsoupList[7]);
		outSave.printf("</td><td align=\"left\"></td></tr>");
	}

	// output skill

	public static void skill(int outForm, PrintStream outSave, boolean firstline,
		String title, int[] values, String effectName, int ifEff,
		int nSkill)
	{
		if(outForm == 0)
			skillTEXT(outSave, title, values, effectName);
		else if(outForm == 1)
			skillHTML(outSave, firstline, title, values, effectName,
				ifEff, nSkill);
	}

	public static void skillTEXT(PrintStream outSave, String title,
		int[] values, String effectName)
	{
		String[] str = new String[8];
		for (int i =0; i < 8; i++)
		{
			if(values[i] == 0)
				str[i] =  "---";
			else
				str[i] = String.valueOf(values[i]);
		}

		String line = String.format
			(" %-20s%3s %3s %3s %3s %3s %3s %3s %3s %s",
			title,str[0],str[1],str[2],str[3],str[4],str[5],
			str[6],str[7],effectName);
		outSave.println(line);
	}

	public static void skillHTML(PrintStream outSave, boolean firstline, String title,
		int[] values, String effectName, int ifEff, int nSkill)
	{
		effectName = new String(effectName.substring(2));
		String arrow;
		if(ifEff == 0)
			arrow = "&nbsp;";
		else
			arrow = "&rarr;";

		if(firstline)   // format table, add 1st line
		{
			outSave.printf("<tr align=\"right\"><td width=\"50\" " +
				"rowspan=\"%d\" valign=\"top\" align=\"left\">Skill</td>\n",nSkill);
			outSave.printf("<td width=\"100\" align=\"left\">%s\n",title.substring(8));
			for(int i = 0; i < 8; i++)
			{
				outSave.printf("</td><td width=\"30\">%d",values[i]);
			}
			outSave.printf("\n");

			if(ifEff < 0 ) // negative skill
				outSave.printf("</td><td width=\"20\" color=darkred>%s</td><td align=\"left\">" +
					"<font color=darkred>%s</font></td></tr>\n",arrow,effectName);
			else if(ifEff == 0 ) // inactive skill
				outSave.printf("</td><td width=\"20\">%s</td><td align=\"left\">" +
					"%s</font></td></tr>\n",arrow,effectName);
			else // postive skill
				outSave.printf("</td><td width=\"20\"><font color=darkblue>%s</font></td>" +
					"<td align=\"left\"><font color=darkblue>%s</font></td></tr>\n",arrow,effectName);
		}
		else    // other lines
		{
			outSave.printf("<tr align=\"right\"><td align=\"left\">%s\n",title.substring(8));
			for(int i = 0; i < 8; i++)
			{
				outSave.printf("</td><td>%d",values[i]);
			}
			outSave.printf("\n");

			if(ifEff < 0 ) // negative skill
				outSave.printf("</td><td color=darkred>%s</td><td align=\"left\">" +
					"<font color=darkred>%s</font></td></tr>\n",arrow,effectName);
			else if(ifEff == 0 ) // inactive skill
				outSave.printf("</td><td width=\"20\">%s</td><td align=\"left\">" +
					"%s</td></tr>\n",arrow,effectName);
			else // postive skill
				outSave.printf("</td><td><font color=darkblue>%s</font></td>" +
					"<td align=\"left\"><font color=darkblue>%s</font></td></tr>\n",arrow,effectName);
		}

	}

	public static void skillHTMLWiki(PrintStream outSave, boolean firstline, String title, String titleJP,
		int[] values, String effectName, int ifEff, int nSkill)
	{
		String arrow;
		if(ifEff == 0)
			arrow = "&nbsp;";
		else
			arrow = "&rarr;";

		if(firstline)   // format table, add 1st line
		{
			outSave.printf("<tr style=\"color:white\" align=\"center\"><td width=\"50\" " +
				"rowspan=\"%d\" valign=\"top\" align=\"left\">Skill</td>\n",nSkill);
			outSave.printf("<td width=\"100\" align=\"left\">%s\n",title.substring(8));
			outSave.printf("</td><td width=\"100\" align=\"left\">%s\n",titleJP);
			for(int i = 1; i < 6; i++)
			{
				outSave.printf("</td><td width=\"30\">%d",values[i]);
			}
			outSave.printf("</td><td width=\"30\">%d",values[7]);
			outSave.printf("\n");

			if(ifEff < 0 ) // negative skill
				outSave.printf("</td><td align=\"left\">" +
					"<font color=red><b>%s</b></font></td></tr>\n",effectName);
			else if(ifEff == 0 ) // inactive skill
				outSave.printf("</td><td align=\"left\">" +
					"<b>%s</b></font></td></tr>\n",effectName);
			else // postive skill
				outSave.printf("</td><td align=\"left\">" +
					"<b>%s</b></td></tr>\n",effectName);
		}
		else    // other lines
		{
			outSave.printf("<tr style=\"color:white\" align=\"center\"><td align=\"left\">%s\n",title.substring(8));
			outSave.printf("</td><td align=\"left\">%s\n",titleJP);
			for(int i = 1; i < 6; i++)
			{
				outSave.printf("</td><td>%d",values[i]);
			}
			outSave.printf("</td><td>%d",values[7]);
			outSave.printf("\n");

			if(ifEff < 0 ) // negative skill
				outSave.printf("</td><td align=\"left\">" +
					"<font color=red><b>%s</b></font></td></tr>\n",effectName);
			else if(ifEff == 0 ) // inactive skill
				outSave.printf("</td><td align=\"left\">" +
					"<b>%s</b></td></tr>\n",effectName);
			else // postive skill
				outSave.printf("</td><td align=\"left\" font=white>" +
					"<b>%s</b></font></td></tr>\n",effectName);
		}

	}

	// output end line
	public static void end(int outForm, PrintStream outSave)
	{
		if(outForm == 0)
			endTEXT(outSave);
		else
			endHTML(outSave);
	}

	public static void endTEXT(PrintStream outSave)
	{
		outSave.println(splitter1);
	}

	public static void endHTML(PrintStream outSave)
	{
		outSave.println("</table>");
		outSave.println("");
	}

	// output empty line if there's no skill
	public static void nullSkillHTML(PrintStream outSave)
	{
		outSave.println("<tr align=\"right\"><td width=\"50\" align=\"left\">Skill</td>\n");
	        outSave.println("<td width=\"100\" align=\"left\">No Skill\n");
		for(int i = 0; i < 8; i++)
		{
			outSave.printf("</td><td width=\"30\">---");
		}
		outSave.printf("</td><td width=\"20\"></td><td align=\"left\">%s</td></tr>",MhagData.emptyName);
	}

	// output header for batch output
	public static void batchHead(int outForm, PrintStream outSave, int num)
	{
		if(outForm == 0)
			batchHeadTEXT(outSave, num);
		else if(outForm == 1)
			batchHeadHTML(outSave, num);
	}

	public static void batchHeadTEXT(PrintStream outSave, int num)
	{
		String line = String.format(" Set : %d", num);
		outSave.println(line);
	}

	public static void batchHeadHTML(PrintStream outSave, int num)
	{
		outSave.printf("<h3 style=\"page-break-before:always\">" +
			"Set %4d</h3>\n", num);
	}

	public static String splitter1 = "========================================" +
		"========================================";
	public static String splitter2 = "----------------------------------------" +
		"----------------------------------------";
	public static String nan = "---";

	public static String urlFire = "http://image.wetpaint.com/image/3/DKCj-55w9QOZG2pJTHQYog1056";
	public static String urlWater = "http://image.wetpaint.com/image/3/2xsExbDeqfMh09NWfVGIbA3725";
	public static String urlIce = "http://image.wetpaint.com/image/3/YmcalOfoVER5a_51dzOViw1011";
	public static String urlThunder = "http://image.wetpaint.com/image/3/Rjmqj6NkhcAa1eGuKOk5Hw894";
	public static String urlDragon = "http://image.wetpaint.com/image/3/mK7vQ8FXBw0maPGGCG-Asw988";
	public static String urlHead = "http://image.wetpaint.com/image/2/oC9iQ85kWn1Q8WN-ON5aow444";
	public static String urlChest = "http://image.wetpaint.com/image/2/Hwq7Gi82zMDzyQmROhPJAQ487";
	public static String urlArm = "http://image.wetpaint.com/image/2/DTNWhiZKuLsmLNt-XD3dgw335";
	public static String urlWaist = "http://image.wetpaint.com/image/2/dRN2N903Ikoa7KnMq212ew569";
	public static String urlLeg = "http://image.wetpaint.com/image/2/ky4o_fDZfkOH3sKlePGz_w468";
	public static String urlPreview = "http://image.wetpaint.com/image/1/oDF6usSGbBUub3OHKVSp0w27963";

}
