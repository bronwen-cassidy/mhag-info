MHAG: Monster Hunter Armor Generator 
Desktop 2.3b
Author : Tifa@mh3, Gondor
MHAG Project: code.google.com/mhag

About MHAG:
MHAG is a FREE armor toolkit, featuring a desktop version (MHAG Desktop) and an online version (MHAG Online).

Webite: 
http://mhag.info

Features:
1. MHAG Desktop: Create and organize sets offline
   a. Manually build armor sets with the built-in calculator
   b. Use the generator to search armor sets automatically within seconds
   c. Use the viewer to manage, browse and export armor sets
   d. The three components can interact with each other

2. MHAG Online: Create and share sets online
   a. Works on ANY device that connects to internet
   b. Features the same calculator as MHAG Desktop
   c. Use the online database to share and search mixed sets

3. Integration of MHAG Desktop and MHAG Online
   a. Share same data and core functions
   b. Easily access MHAG Online from MHAG Desktop

4. Support Games
   a. Monster Hunter Tri (Wii)
   b. Monster Hunter Portable 3rd (PSP/PS3)
   c. Monster Hunter 3 Ultimate (3DS/Wii U)
   d. (future) Monster Hunter Freedom Unite (PSP)*

(Notes)
**  If any user can provide MHFU data

System Requirements:
 1. MHAG Desktop:
  a. Any PC (WinXP or later, MacOS and Linux)
  b. JAVA 6 (JRE 1.6) or later
  c. Screen resolution: 1024x768 (minimum), 1280x720 or above (recommended)
 2. MHAG ONline:
  a. Any device (PC, tablet, smartphone)
  b. Web browser (Google Chrome, Mozilla Firefox, Safari, Internet Explorer, Opera etc.)

History (MHAG Desktop):
v.2.3b 8/27/2013
   1. Error: Reload Spd skill for the Grand Mutsu Set.
v.2.3a 8/23/2013
   1. Error: data for the Grand Yamoto Set.
v.2.3 7/21/2013
   1. Overwrite translations for MH3G to the  MH3U data 
   2. Add Jhen G and Ala G armor
   3. Maximum number of Charms is now 5000
v.2.2a 2/24/2011
   1. a few armor data errors were fixed. (Credit to Vuze from mh3g wetpaint site)
   2. Set URL links were corrected, so it links directed to the new MHAG Online Server for MH3G
   3. add Sword Saint Earring and Barrage Earring
   4. mhag model code is seperated from swing code (for future mobile port)
v.2.2 2/06/2011
   1. new feature: MH3G support (beta version)  All names were translated except for the material names.  Maximum skill points for talismans were mostly taken from MHP3rd and Japanese Wiki page (www10.atwiki.jp/mh3g/pages/72.html).  Armor names, skill names/points, slot info, resist data were taken from multiple sources and the previous games.  Materials and maximum defense will be checked in the future version, once data are available.
   2. erros (adjust) : guard up jewel skill error (MHP3rd), Coat jewel names changed (MHP3rd & MH3G), some japanese armor names fixed (MHP3rd & MH3G), poison skill effect name (Negate Poison) fixed (all games)
v.2.1 1/26/2011
   1. new look and feel. 
   2. new feature: Now all games are integrated into a single package.  Users can choose a game at the beginning.  All user data are saved in sub-dir.
   3. new feature: Data structure are modified to allow G-rank data.  (so it's ready for MHFU and MH3G support)
   4. new feature: Add the option to display armor/charm materials in the calculator.  Data exactly from the open source project TriDB (thanks to Feox for convert the data to MHAG format) and P3DB (Most armor/charm names were synchronized with mhf3.com). 
   5. new feature: Enhance keyboard controls. Most comboboxes now has auto-completion function.  "enter" key can trigger button.
   6. new feature: Adjust female/male checkbox.  Now if male/female is selected, only male/female sets are available in the armor selection menu. (mostly for mp3rd and future games)  The generator also search sets only for specific gender.  
   7. error: The Boss' Set and Snake's Set have different skills (mhp3rd).  Fixed, sets are now separated.
   8. Fix some typo/translations of armor names or jewel names. (Bnahabra Pants, Mutsu Gusoku, Archer's Gauntlets, Chainmail Armor+, Shadow's Shades, Alloy Faulds+, Bnahabra Mail+, Bnahabra Faulds+, Bnahabra Greaves, Steal Coil+, Damascus Set, Assult Jewel, Storm Jewel)
v.2.0 10/18/2011:
   1. bug: partial search never stops when charm is manually reset; fixed
   2. bug: "slot from two gun parts" failed to search some cases; fixed
   3. bug: "reset" function in the calculator doesn't reset gunner/blade low/high rank and female/male radio buttons; fixed
   4. bug: in GUI background thread; fixed
   5. new feature: armor suggestion also outputs all armor pieces that contain the selected skills, sorted by total skill points.
   6. update README description
   7. update USAGE 
   8. correct a few mhp3rd data errors (bnahabra set, hunter's S set, ludroth U set, Escadora set)
v.2.0 RC2 10/11/2011:
   1. bug: "slot from two gun parts" doesn't work properly. fixed
   2. new feature: add mhag icon 
   3. update MHAG Online URL
v.2.0 RC1 10/10/2011:
   1. new: delete about tab panel, add about button/dialog
   2. new feature: add scroll pane for low resolution screen (such as 1024x768)
   3. new feature: change new mhag icon, add splash screen
   4. new feature: in the generator function, add an option for gunner weapon slots, so the slots can be from two different parts.
v.1.2 beta 6 09/30/2011:
   1. bug: skill list in the generator panel should show activated skills(effects).
   2. bug: "load to calculator" function (gunner or low rank)
   3. new feature: suggestions of armor pieces according to the skills
   4. new feature: full/partial set search
   5. new feature: advanced setting panel
v.1.2 beta 5 09/19/2011:
   1. bug: set url link for special characters
   2. bug: menu selection for "yukumu armor pieces" (armor pieces #1)
v.1.2 beta 4 09/12/2011:
   1. bug: japanese language support. Finally, it works in windows.
v.1.2 beta 3 09/10/2011:
   1. add set name information in url link for MHAG Online, support special characters
   2. add japanese data
   3. remove output feature from the calculator
   4. new feature: reset button 
   5. new feature: english/japanese switch (menu only, mhp3rd only)  (currently only work under linux)
   6. new feature: in viewer panel, if the code book doesn't exist, MHAG automatically creates one.
   7. bug: after load a "auto-guard" set, one can input the 2nd skill in the calculator. fixed
v.1.2 beta 2 09/04/2011:
   1. new feature: add myCharm dialog (charm manager)
   2. change mhag to support both tri/p3rd in the same package. (source code)
   3. new feature : generate url link for set code
   4. bug: if set name contains ":", switch it to ".". (for url/set code)
v.1.2 beta 1 08/31/2011:
   1.new feature : new generator panel, jewel optimization (experimental)
   2.improve speed of "load to Calculator" 
   3.tweak jewel preview: add "# of slot" information after jewel short name (if # > 1); this may cut the last letter of jewel name
   4.delete help panel
v.1.1c 08/27/2011:
   1.bug : combobox menu error for the 2nd talisman skill
   2.tweak gui
v.1.1b 08/11/2011:
   1.adjust talisman skill point menu for Auto-Guard
   2.bug : sometimes an armor piece cannot be selected in the calculator
v.1.1a 08/03/2011:
   1.change charm slot menu (more user friendly)
   2.adjust gui preview frame
v.1.1 08/02/2011:
   1.new feature :  New talisman system (mainly for mhp3rd), max skill point data from official strategy book
   2.bug : change output typo: SKill to Skill
   3.bug :  wrong tooltips when swtiching female/male
   4.bug : backward support to java 1.5 (fixed save & exit window)
   5.error : guild+ armor data error , vangis coat typo
v.1.0 Final Release, 01/10/2011:
   1.new feature : add backward support to java 1.5 and Mac OS
   2.new feature : add "save before exit" dialog when code list is changed.
   3.new feature : automatically keep user's data file name and code book name.  So, if MHAG finds a name different from "MyData", it memorizes it for future use. 
   4.new feature : Help panel is now more user-friendly.  It contains a list and a content panel.  All help data are automatically extracted from USAGE.txt
   5."load" in the viewer now resets preview panel.
   6.error: redundant items found in MHP3rd's charm data. deleted
v.1.0 beta 4, 01/06/2011:
   1.new feature : add support to mhp3rd (experimental); a simple version of charm system  
   2.add mhp3rd data, translation is partly based on www.mhf3.com. include code book for full sets.
   3.bug: "load to calculator" load error for low rank set or sets with empty part(s). fixed
   4.add new MHAG logo.
v.1.0 beta 3, 01/03/2011:
   1.new feature : add new tool tips for armor pieces. tools tips contains skill names, skill points and slot information.
   2.Add cover page for HTML output.
   3.Add code books for blademaster/gunner full set.
   4.bug: "save & export" button in the viewer doesn't save code book. fixed
   5.bug: when loading code book in the viewer, MHAG claims code error in preview panel. fixed
   6.bug: debug info shows in console when using cut/paste function. deleted
   7.README.txt and about text are revised.
   8.error: skill data error found in gathering skill. fixed
v.1.0 beta 2, 01/01/2011:
   1.bug fixed: in HTML output, the first character is missing for the activated skill names
v.1.0 beta 1, 01/01/2011: 
   1.initial release for test; It has a calculator and a viewer.

Installation (MHAG Desktop):
1. Make sure your computer have JRE (Java Runtime Environment). If not, download and install JRE at http://www.java.com
2. Unzip mhag pack to a folder, then enter mhag folder.
3. Run MHAG:
     1) windows : in File Explorer, double click the icon of Mhag.jar
     2) linux : use command line "java -jar Mhag.jar" in a console.
     3) Mac OS : double click

Known Issues:
TBD

Acknowledgements
Many thanks to Capcom unity, Bobo's www.mhf3.com, Monster Hunter Wikia.
Thank CHUMP, Vyracritus, OZ78, Slater etc from Capcom Unity, Xalugami from mhf3.com, Stuff from MH3 Wikia for testing mhag and offering suggestions

