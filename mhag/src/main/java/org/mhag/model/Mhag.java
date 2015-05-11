package org.mhag.model;

/**
 * @program MHAG
 * @ Main Mhag Class
 * @version 1.0
 * @author Tifa@mh3
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Mhag
{

	public Mhag(int version)
	{
		game = version;
	}

	// MHAG command-line help
	public static void showHelp()
	{
		System.out.println("Usage: javac Mhag method " +
			"<cal/bat/gen/ref> in <input.dat> " +
			"out <result> log <''/log/off> " +
			"format <text/html>");
		System.exit(0);
	}

	// read MHAG arguments
	public void procArg(String[] args){


		int i = 0;
		while ((args.length != 0) &&(i < args.length))
		{
			if(args[i].equals("method"))
			{
				i++;
				if(i >= args.length) showHelp();
				else if(args[i].equals("cal"))
				{
					method = 0;
				}
				else if(args[i].equals("bat"))
				{
					method = 1;
				}
				else if(args[i].equals("gen"))
				{
					method = 2;
				}
				else if(args[i].equals("ref"))
				{
					method = 3;
				}
				else
				{
					showHelp();
				}
			}
			else if(args[i].equals("in"))
			{
				i++;
				if(i >= args.length) showHelp();
				else fileIn = args[i];
			}
			else if(args[i].equals("out"))
			{
				i++;
				if(i >= args.length) showHelp();
				else fileOut = args[i];
			}
			else if(args[i].equals("log"))
			{
				i++;
				if(i >= args.length) showHelp();
				if(args[i].equals("off"))
				{
					logOpt = 2;
				}
				else
				{
					logOpt = 1;
					fileLog = args[i];
				}
			}
			else if(args[i].equals("format"))
			{
				i++;
				if(i >= args.length) showHelp();
				if(args[i].equals("html"))
				{
					outFormat = 1;
				}
				else
				{
					outFormat = 0;
				}
			}
			else if(args[i].startsWith("#"))
			{}
			else    // error
			{
				showHelp();
			}
			i++;
		}
		if( outFormat == 0)
		{
			if(!fileOut.endsWith(".txt"))
				fileOut = fileOut +".txt";
		}
		else
		{
			if(!fileOut.endsWith(".html"))
				fileOut = fileOut + ".html";
		}

	}
	
	// set up MHAG log file
	public void prepareLogFile() throws FileNotFoundException
	{
		if(logOpt == 0)
		{
			outLog = System.out;
		}
		else if (logOpt == 1)
		{
			outLog = new PrintStream(fileLog);
		}
	}

	// output MHAG arguments
	public void showMhagArgs()
	{
		if(logOpt == 2)return;
		outLog.printf("Auguments: \n");
		outLog.printf("Method flag: %d\n",method);
		outLog.printf("FileIn: %s\n",fileIn);
		outLog.printf("FileOut: %s\n",fileOut);
		outLog.printf("FileLog: %s\n",fileLog);
		outLog.printf("LogOPT flag: %s\n",logOpt);
		outLog.printf("OutFormat flag: %d\n",outFormat);
		outLog.println("");
	}

	//output MHAG welcome info
	public void showMhagInfo()
	{
		if(logOpt == 2)return;
		outLog.println(Output.splitter1);
		outLog.println("MHAG: ver 2.3b");
		outLog.println("Monster Hunter Armor Generator");
		outLog.println("By Tifa@mh3, Aug 2013");
		outLog.println("http://www.youtube.com/mh3journey");
		outLog.println("http://code.google.com/p/mhag/");
		outLog.println(Output.splitter1);
		outLog.println("");
	}

	//init process
	public void init(MhagData mhagData, String[] args)
		throws FileNotFoundException
	{
		procArg(args);
		prepareLogFile();

		showMhagInfo(); //display MHAG welcome info
		showMhagArgs(); //display MHAG arguments

		mhagData.readFile(this);  //read file
		mhagData.dataPreProc();  //pre process

		if(method == 3)mhagData.showReference(this); //show ref
	}

	// get game version
	public int getGame() {return game;}

	// get method
	public int getMethod() {return method;}

	// set method
	public void setMethod(int opt) {method = opt;}

	// get log option
	public int getLogOpt() {return logOpt;}

	// set log option
	public void setLogOpt(int log) {logOpt = log;}

	// get output format
	public int getOutFormat() {return outFormat;}

	// set output format
	public void setOutFormat(int formatOpt) {outFormat = formatOpt;}

	// get out object for log outputs
	public PrintStream getOutLog() {return outLog;}

	// set out object for log outputs
	public void setOutLog(PrintStream logObj) {outLog = logObj;}

	public String getFileOut() {return fileOut;}

	public void setFileOut(String file) {fileOut = file;}

	public String getFileIn() {return fileIn;}

	public void setFileIn(String file) {fileIn = file;}

	// get log file
	public String getFileLog() {return fileLog;}

	public void setFileLog(String file) {fileLog = file;}

	// main process method
	public void proc(MhagData mhagData) throws FileNotFoundException
	{
		if(method == 0)
		{
			mhagData.calculator(this);
		}
		else if(method == 1)
		{
			mhagData.batchCalc(this);
		}
		else if(method == 2)
		{
			Generator.generator(this, mhagData);
		}
	}

	public static void main(String[] args) throws FileNotFoundException
	{

		Mhag mhag = new Mhag(3);
		MhagData mhagData = new MhagData();

  		//mhag.method = 3;  //tifa temp
		mhag.init(mhagData, args);

  		mhag.method = 2;  //tifa temp

		mhag.proc(mhagData);

//		String dir = System.getProperty("user.dir");
//		System.out.println(dir);

	}
	private int game = 0; // game, 0: tri; 1: mhp3rd

	private int method = 0;  // MHAG method 0: caluclator; 1: batch;
				 // 2: generator; 3: reference;
				 // 4: backward support old code file
	private String fileIn = "input.dat";  // MHAG default input file
	private String fileOut = "result";  // MHAG default output file
	private String fileLog = "log";  // MHAG default log file
	private int logOpt = 0 ;  //log information option "": screen
			         // "log": file 2: "off" : off
	private int outFormat = 0; // Output Format 0: text; 1: html
	private PrintStream outLog = null;  // object for writing log

	// set to public for io access
}
