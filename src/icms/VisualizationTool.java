/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package icms;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;

import java.util.*; 

/**
 *
 * @author smriti
 */

//class SubjectSystem
//{
//    String systemName = "";
//    String systemPath = "";
//    int revisionCount = 0;
//}


//class CircleTest extends JPanel {
//
//    private final int SIZE = 256;
//    //private int n;
//
//    /** @param n  the desired number of circles. */
//    public CircleTest() {
//        super(true);
//        this.setPreferredSize(new Dimension(SIZE, SIZE));
//    }

    /*@Override
    protected void paintComponent(Graphics g) 
    {        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.black);
        
        int n = 11;

        int width = getWidth();
        int height = getHeight();
        
        int n2 = (int)Math.ceil(Math.sqrt(n));
        int height2 = (int)Math.floor(height/n2);
        int width2 = (int)Math.floor(width/n2);
        
        int x = 1, y = 1, padding = 5;
        
        height = height - 3;
        width = width - 3;
        
        g2d.drawRect(x, y, width, height);
        
        int cx = x, cy = y;
        for (int i = 1;i<=n;i++)
        {      
            if (cx + width2 <= x + width && cy + height2 <= y + height)
            {
                g2d.drawRect (cx+padding, cy+padding, width2-2*padding, height2-2*padding);
                cx = cx + width2;
                if (cx+width2 > x + width)
                {
                    cx = x;
                    cy = cy + height2;
                }
            }
        }
    }*/
    
//    @Override
//    protected void paintComponent(Graphics g) 
//    {        
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setRenderingHint(
//            RenderingHints.KEY_ANTIALIASING,
//            RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setColor(Color.black);
//        
//        FolderClass folderClass = new FolderClass ();
//        
//        try
//        {
//            folderClass = getFolder ("H:/subjectsystems/ctags/repository/version-774"," c ");
//        }
//        catch (Exception e)
//        {
//            //do nothing.
//        }
//        
//        drawRectangleRecursive (folderClass, 1,1,getWidth()-3, getHeight()-3, g2d);
//        
//        /*int n = 11;
//
//        int width = getWidth();
//        int height = getHeight();
//        
//        int n2 = (int)Math.ceil(Math.sqrt(n));
//        int height2 = (int)Math.floor(height/n2);
//        int width2 = (int)Math.floor(width/n2);
//        
//        int x = 1, y = 1, padding = 5;
//        
//        height = height - 3;
//        width = width - 3;
//        
//        g2d.drawRect(x, y, width, height);
//        
//        int cx = x, cy = y;
//        for (int i = 1;i<=n;i++)
//        {      
//            if (cx + width2 <= x + width && cy + height2 <= y + height)
//            {
//                g2d.drawRect (cx+padding, cy+padding, width2-2*padding, height2-2*padding);
//                cx = cx + width2;
//                if (cx+width2 > x + width)
//                {
//                    cx = x;
//                    cy = cy + height2;
//                }
//            }
//        }*/
//    }
    
//    public void drawRectangleRecursive (FolderClass folderClass, int x, int y, int width, int height, Graphics2D g2d)
//    {
//        g2d.drawRect(x, y, width, height);
//        //g2d.setColor(Color.yellow);
//        //g2d.fillRect(x, y, width, height);
//        
//        if (folderClass.childCount == 0) return;
//        
//        FolderClass folderClass1 = new FolderClass ();
//        
//        int n = folderClass.childCount ;
//        
//        int n2 = (int)Math.ceil(Math.sqrt(n));
//        
//        if (n2 == 0)
//        {
//            int a = 1;
//        }
//        
//        int height2 = (int)Math.floor(height/n2);
//        int width2 = (int)Math.floor(width/n2);
//        
//        int padding = 5;
//        
//        
//        
//        int cx = x, cy = y;
//        for (int i = 0;i<n;i++)
//        {      
//            if (cx + width2 <= x + width && cy + height2 <= y + height)
//            {
//                folderClass1 = (FolderClass)folderClass.childList.get(i);
//                if (folderClass1.type.equals("folder"))
//                {
//                    drawRectangleRecursive (folderClass1, cx+padding,cy+padding,width2-2*padding, height2-2*padding, g2d);
//                }
//                else
//                {
//                    //g2d.setColor(Color.green);
//                    g2d.drawRect (cx+padding, cy+padding, width2-2*padding, height2-2*padding);
//                    cx = cx + width2;
//                    if (cx+width2 > x + width)
//                    {
//                        cx = x;
//                        cy = cy + height2;
//                    }
//                }
//            }
//        }      
//    }
    

//    public void create() 
//    {
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.add(new CircleTest());
//        f.pack();
//        f.setVisible(true);
//    }
//    
//    class FolderClass
//    {
//        String path = "", type = "";
//        int childFolderCount;
//        int childFileCount;
//        int childMethodCount;
//        int childCount;
//
//        LinkedList childFolderList = new LinkedList ();
//        LinkedList childFileList = new LinkedList ();
//        LinkedList childMethodList = new LinkedList ();
//        LinkedList childList = new LinkedList ();
//
//    }

    /*class FileClass
    {
        String filePath = "";
        int methodCount;

    }*/

//    class MethodClass
//    {
//
//    }    
//    
//    public FolderClass getFolder (String folderName, String fileExtension) throws Exception
//    {
//        FolderClass folder = new FolderClass ();
//        folder.path = folderName;
//        folder.type = "folder";
//        
//        FolderClass childFolder ;
//        FolderClass childFile ;
//        
//        
//        int childFolderCount = 0;
//        int childFileCount = 0;
//        int childCount = 0;
//        
//        LinkedList childFolderList = new LinkedList ();
//        LinkedList childFileList = new LinkedList ();
//        LinkedList childList = new LinkedList ();
//        
//        
//        File f = new File(folderName);
//        File[] listOfFiles = f.listFiles();
//        
//        for (int i = 0; i < listOfFiles.length; i++)
//        {
//            if (listOfFiles[i].isDirectory())
//            {
//                childFolder = new FolderClass ();
//                childFolder = getFolder (folderName+"/"+listOfFiles[i].getName(),fileExtension );
//                childCount++;
//                childList.add(childFolder);
//            }
//            else
//            {
//                if (listOfFiles[i].isFile())
//                {
//                    if (fileExtension.indexOf(listOfFiles[i].getName().split("[.]+")[listOfFiles[i].getName().split("[.]+").length-1]) > -1)
//                    {
//                        childFile = new FolderClass ();                        
//                        childFile =  getFile (folderName + "/" + listOfFiles[i].getName());
//                        childCount++;
//                        childList.add (childFile);
//                        
//                        //inserting the java file names into a global array.
//                        /*sourceFiles[sourceFileCount] =  new SourceFileInformation () ;
//                        sourceFiles[sourceFileCount].fileName = folderName + "/" + listOfFiles[i].getName() ;
//                        String t = sourceFiles[sourceFileCount].fileName;
//                        t = t.replace("/.ccfxprepdir", "");
//                        t = t.replace (preprocessed_file_extension, "");
//                        sourceFiles[sourceFileCount].originalFilePath = t ;                    
//                        sourceFileCount++;*/
//                    }                    
//                }
//            }
//        }
//        
//        folder.childFileCount = childFileCount;
//        folder.childFolderCount = childFolderCount;
//        folder.childFileList = childFileList ;
//        folder.childFolderList = childFolderList ;
//        folder.childCount = childCount;
//        folder.childList = childList;
//        
//        return folder;
//    }
//    
//    public FolderClass getFile (String fileName)
//    {
//        FolderClass file = new FolderClass ();
//        file.path = fileName;
//        file.type = "file";
//        
//        
//        return file;
//    }
    
    /*public static void getAllFilesOfFolder (String folderName, String fileExtension)
    {
        try
        {
        //System.out.println ("------> I am retrieving all files of folder: "+ folderName) ;
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                if (fileExtension.indexOf(listOfFiles[i].getName().split("[.]+")[listOfFiles[i].getName().split("[.]+").length-1]) > -1)
                {
                    //inserting the java file names into a global array.
                    sourceFiles[sourceFileCount] =  new SourceFileInformation () ;
                    sourceFiles[sourceFileCount].fileName = folderName + "/" + listOfFiles[i].getName() ;
                    String t = sourceFiles[sourceFileCount].fileName;
                    t = t.replace("/.ccfxprepdir", "");
                    t = t.replace (preprocessed_file_extension, "");
                    sourceFiles[sourceFileCount].originalFilePath = t ;                    
                    sourceFileCount++;
                }
            }
            else if (listOfFiles[i].isDirectory())
            {
                getAllFilesOfFolder (folderName+"/"+listOfFiles[i].getName(),fileExtension ) ;
            }
        }
        }
        catch (Exception e)
        {
            System.out.println ("getAllFilesOfFolder");
            System.out.println (e.getStackTrace()) ;
        }

    } */      
//}



//class Rectangle extends JComponent 
//{
//    int x=0, y=0, height=HEIGHT, width=WIDTH;
//    int padding = 2;
//    
//    public Rectangle (JFrame frm)
//    {
//        width = frm.getWidth();
//        height = frm.getHeight();
//    }
//    
//    public Rectangle (int wt, int ht)
//    {
//        height = ht;
//        width = wt;
//    }
//    
//    public void paint(Graphics g) 
//    {        
//        int n = 11;
//
//        int n2 = (int)Math.ceil(Math.sqrt(n));
//        int height2 = (int)Math.floor(height/n2);
//        int width2 = (int)Math.floor(width/n2);
//        
//        g.drawRect(x, y, width, height);
//        
//        int cx = x, cy = y;
//        for (int i = 1;i<=n;i++)
//        {      
//            if (cx + width2 <= x + width && cy + height2 <= y + height)
//            {
//                g.drawRect (cx+padding, cy+padding, width2-2*padding, height2-2*padding);
//                cx = cx + width2;
//                if (cx+width2 > x + width)
//                {
//                    cx = x;
//                    cy = cy + height2;
//                }
//            }
//        }        
//    }
//}

public class VisualizationTool {

    //public static SubjectSystem subjectSystem = new SubjectSystem();
    public JFrame frame = new JFrame ();
    public CommonParameters commonParameters = new CommonParameters ();

    public AttributeNames an = new AttributeNames ();
    
    //public static MainFrame mainFrame = new MainFrame ();
    
    
    public void mainMethod ()
    {
        generateAssociationRules ();
    }
    
    public void main1() throws Exception{
        // TODO code application logic here

        //frame.setSize(new Dimension(300,300));
        //frame.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /*Rectangle rectangle = new Rectangle (frame);
        frame.add(rectangle);
        frame.pack();*/
        
        //CircleTest ctest = new CircleTest ();
        //ctest.create();
        
        
        
        
        /*RuleCondition ruleCondition = new RuleCondition ();
        ruleCondition.support = 2;
        ruleCondition.xyConfidence = 1;
        ruleCondition.yxConfidence = 1;*/
        
        
        
        
        //initializeSubjectSystem ("carol", 774);
        //CCFinder ccfinder = new CCFinder ();
        
        //ccfinder.getTokenSequenceOfMethod (subjectSystem, 39, 213, dbase);
        
        
        
        
        //retrieveModifiedMethods();        
        //determineRules();
        
        getResultRulesForSignificance ();
        //getSpecificRules(1, 0.2f, 0f);
        
        
        
        //performStaticAnalysis ();
        
        
        //determineClonedRules ();
        /*searchRules (ruleCondition);
        findHighlyCoupledMethods();
        searchStructuralWeakness ();
        clonedHighlyCoupledMethods ();*/
        
        //help();
        //help1 ();
        
    }
    
//    
//    public void helpme () throws Exception
//    {
//        BufferedReader br1 = new BufferedReader (new InputStreamReader (new FileInputStream ("carol_rulesCopy.txt")));
//        BufferedReader br2 = new BufferedReader (new InputStreamReader (new FileInputStream ("carol_clonedrules.txt")));
//        BufferedWriter br = new BufferedWriter (new FileWriter ("carol_rules.txt"));
//        
//        String str = "";
//        String [] clonedRules  = new String[200000];
//        int clonedRulesCount = 0;
//        
//        System.out.println ("reading cloned rules.");
//        while ((str = br2.readLine())!= null)
//        {
//            if (str.trim().length() == 0){continue;}
//            clonedRules[clonedRulesCount] = str;
//            clonedRulesCount++;
//        }
//        
//        System.out.println ("updating rules file.") ;
//        int j = 0;
//        while ((str = br1.readLine())!= null)
//        {
//            if (str.trim().length() == 0){continue;}
//            
//            int i=0;
//            for (i=0;i<clonedRulesCount;i++)
//            {
//                if (clonedRules[i].contains(str.trim()))
//                {
//                    br.write ("\n"+clonedRules[i]);
//                    System.out.println ("got a match") ;
//                    break;
//                }
//            }            
//            if (i == clonedRulesCount)
//            {
//                br.write ("\n"+str);
//            }
//            j++;
//            
//            System.out.println ("done "+j) ;
//        }
//        br.close();   
//        System.out.println ("finally done.") ;
//    }
    
    
    
    public void generateAssociationRules ()
    {
        try
        {
            System.out.println ("retrieving modified methods.");
            retrieveModifiedMethodsFromFile ();
            System.out.println ("retrieving modified methods END.");
            
            System.out.println ("determining the rules.");
            determineRules();
            System.out.println ("determining the rules END.");
            
            System.out.println ("determining the cloned rules.");
            determineClonedRules ();  
            System.out.println ("determining the cloned rules END.");

            System.out.println ("determining method location information.");
            searchStructuralWeakness ();
            System.out.println ("determining method location information end.");
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = generateAssociationRules."+e) ;
        }
    }
    
    public void searchAssociationRules (RuleCondition rc) throws Exception
    {
        /*RuleCondition ruleCondition = new RuleCondition ();
        ruleCondition.support_min = sm;
        ruleCondition.support_max = sma;
        ruleCondition.xyConfidence_min = xym;
        ruleCondition.xyConfidence_max = xyma;
        ruleCondition.yxConfidence_min = yxm;
        ruleCondition.yxConfidence_max = yxma;*/
        
        
        System.out.println ("retrieving the rules.") ;
        searchRules (rc);
        
        /*System.out.println ("sorting the rules according to the support values.") ;
        sortRules ();*/
        
        //System.out.println ("finding structural weaknesses.") ;
        //searchStructuralWeakness ();
        //System.out.println ("finding structural weaknesses complete.") ;
        
        //System.out.println ("finding highly coupled methods.") ;
        //findHighlyCoupledMethods();
        //System.out.println ("finding highly coupled methods complete.") ;
        
        
        //not used here, but might be necessary
        //clonedHighlyCoupledMethods ();
    }
    
    public void sortRules () throws Exception
    {
        BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.subject_system+"_resultrules.txt")));
        
        String [] rules = new String[500000] ;
        int ruleCount = 0;
        String str = "";
        
        while ((str = br.readLine()) != null)
        {
            if (str.trim().length() == 0) {continue;}
            rules[ruleCount] = str;
            ruleCount++;
        }
        br.close();
        
        
        //sorting the rules according to the support values.                
        for (int i =0;i<ruleCount-1;i++)
        {
            int index = i;
            float highest = Float.parseFloat(rules[i].split("[:]+")[1].trim());
            
            for (int j = i+1;j<ruleCount;j++)
            {
                float support = Float.parseFloat(rules[j].split("[:]+")[1].trim());
                if (support > highest)
                {
                    index = j;
                    highest = support;
                }
            }
            String rule = rules[i];
            rules[i] = rules[index];
            rules[index] = rule;
        }
        
        /*String [] rulesSupport  = new String[500000];
        
        for (int i =0;i<ruleCount;i++)
        {
            rulesSupport[i] = rules[i];
        }*/
        
        BufferedWriter br1 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"_resultrules.txt"));
        for (int i =0;i<ruleCount;i++)
        {
            br1.write ("\n"+rules[i]);
        }
        br1.close();        
    }
    
    
    
    
    
    public static String analyzeIdentifier (String ts1, String ts2)
    {
        int idCount1 = 0, idCount2 = 0, commonIdCount=0;
        String methodName1 = "", methodName2 = "";
        String  ids1 = "";
        String  ids2 = "";
        
        int len1 = ts1.split("[ ]+").length, len2 = ts2.split("[ ]+").length;
        
        //counting the number of ids from the first token sequence.
        int got = 0;
        for (int i =0;i<len1;i++)
        {
            String temp = ts1.split("[ ]+")[i].trim();
            if (temp.split("[|]+")[0].equals("id"))
            {
                if (!ids1.contains (" "+temp+" "))
                {
                    idCount1++;
                    ids1 += " " + temp + " " ;
                    if (got == 0)
                    {
                        methodName1 = temp.trim().split("[|]+")[1];
                        got = 1;
                    }
                }
            }
        }   
        
        //counting the number of ids from the second token sequence.
        got = 0;
        for (int i =0;i<len2;i++)
        {
            String temp = ts2.split("[ ]+")[i].trim();
            if (temp.split("[|]+")[0].equals("id"))
            {
                if (!ids2.contains (" "+temp+" "))
                {
                    ids2 += " " + temp + " ";
                    idCount2++;
                    if (got == 0)
                    {
                        methodName2 = temp.trim().split("[|]+")[1];
                        got = 1;
                    }
                }
            }
        }         
        
        //counting the number of common ids between two sequences.
        
        String commonIds = "";
        len1 = ids1.trim().split("[ ]+").length; 
        for (int i =0;i<len1;i++)
        {
            String temp = "";
            temp = ids1.trim().split("[ ]+")[i].trim();
            if (ids2.contains(" "+ temp +" "))
            {
                commonIdCount++;
                commonIds += temp.trim() + "  ";
            }
        }        
        
        return "count of ids in method1 = "+idCount1+" : count of ids in method2 = "+idCount2 + " : count of common ids = "+commonIdCount +" : common Ids = " + commonIds ;        
    }
    
    
    
    
    //this method retrieves the followings from the database 
    // 1. a method (globalmethodid) -> that got some changes in some commit operations.
    // 2. the commits in which the method got some changes.
    // format => methodid:methodname:filename:revision1 revision2 revision3 revision4 ......
    public void retrieveModifiedMethodsFromFile ()
    {
        try
        {
            String [] retrievedMethods = new String[5000];
            int retrievedMethodCount = 0;
            String line = "";

            for (int revision =1;revision<=commonParameters.revisionCount-1;revision++)
            {
                System.out.println ("revision = "+ revision);

                BufferedReader breader = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.getMethodsPathOfRevision(revision))));


                //dbase.executeQuery("select globalmethodid, methodname, filename, signature, startinglinenumber, endinglinenumber from method where version = "+revision+" and changedinnextversion > 0");
                while ((line = breader.readLine())!= null)
                {
                    if (line.trim().length() == 0){continue;}

                    if (Integer.parseInt(commonParameters.getAttributeValueFromString(line, an.changeCount)) == 0){continue;}

                    String gmid = commonParameters.getAttributeValueFromString(line, an.globalMethodID)+"";
                    String mname = commonParameters.getAttributeValueFromString (line, an.methodName);
                    String fname = commonParameters.getAttributeValueFromString(line, an.filePath);

                    int i =0;
                    for (i =0;i<retrievedMethodCount;i++)
                    {
                        if (retrievedMethods[i].trim().split("["+commonParameters.separator+"]+")[0].equals(gmid))
                        {
                            break;
                        }
                    }
                    if (i < retrievedMethodCount) //the method already exists
                    {
                        retrievedMethods[i] += " "+revision + " ";
                    }
                    else // the method is a new method to include in the list.
                    {
                        retrievedMethods[retrievedMethodCount] = gmid+commonParameters.separator+mname+commonParameters.separator+fname+commonParameters.separator+revision + " ";
                        retrievedMethodCount++;
                    }
                }
            }

            //quick sorting the methods.
            quickSortMethods(retrievedMethods, 0, retrievedMethodCount-1);

            //writing the methods in a file.
            BufferedWriter br = new BufferedWriter (new FileWriter (commonParameters.subject_system+"/changedmethods.txt")) ;
            for (int i =retrievedMethodCount-1;i>=0;i--)
            {
                br.write ("\n\n"+retrievedMethods[i]);
            }
            br.close();
        }
        catch (Exception e)
        {
            System.out.println ("error in method = retrieveModifiedMethodsFromFile. "+e);
        }
    }
    
    
    void quickSortMethods(String arr[], int left, int right)
    {
        try
        {
            int i = left, j = right;
            String tmp = "";
            int pivot = arr[(left + right) / 2].split("[:]+")[3].trim().split("[ ]+").length;



            /* partition */
            while (i <= j) 
            {
                while (arr[i].split("[:]+")[3].trim().split("[ ]+").length < pivot)
                {
                      i++;
                }
                while (arr[j].split("[:]+")[3].trim().split("[ ]+").length > pivot)
                {
                      j--;
                }            
                if (i <= j) 
                {
                      tmp = arr[i];
                      arr[i] = arr[j];
                      arr[j] = tmp;
                      i++;
                      j--;
                }
            }

            /* recursion */
            if (left < j)
            {
                quickSortMethods(arr, left, j);
            }

            if (i < right)
            {
                quickSortMethods(arr, i, right);
            }
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = quickSortMethods."+e);
        }
    }
    
    
    
    //this method reads the file of methods and then determines the rules with confidences and cmin.
    //the rules are written in the file.
    //m1---->m2: cochange count : confidence : cochange commits.
    public void determineRules () throws Exception
    {
        String [] retrievedMethods = new String[5000];
        int retrievedMethodCount = 0;
        
        String [] rules = new String[10000000];
        int ruleCount = 0;
        
        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"/changedmethods.txt")));
        
        
        String line = "";
        while ((line = br.readLine())!= null)
        {
            if (line.length() > 0)
            {
                retrievedMethods[retrievedMethodCount] = line;
                retrievedMethodCount++;
            }
        }        
        br.close();

        
        
        int i=0,j=0, k=0, l=0;       
        
        //determination of rules and writing the rules in the file.
        for (i=0;i<retrievedMethodCount-1;i++)
        {
            for (j=i+1;j<retrievedMethodCount;j++)
            {
                int cochangeCount = 0;
                String cochangeCommits = "";
                
                String m1 = retrievedMethods[i].trim().split("[:]+")[0].trim();
                String m2 = retrievedMethods[j].trim().split("[:]+")[0].trim();
                
                String rlist1 = " "+retrievedMethods[i].split("[:]+")[3].trim()+" ";
                String rlist2 = " "+retrievedMethods[j].split("[:]+")[3].trim()+" ";
                
                int rcount1 = rlist1.trim().split("[ ]+").length;
                int rcount2 = rlist2.trim().split("[ ]+").length;
                
                float uniqueness = 0.0f;
                
                
                for (k=0;k<rcount1;k++)
                {
                    String v = rlist1.trim().split("[ ]+")[k];
                    if (rlist2.contains(" "+ v+" "))
                    {
                        cochangeCount++;
                        cochangeCommits += " "+ v + " "; 
                        
                        int cnt = 0;
                        
                        for (int m = 0;m<retrievedMethodCount;m++)
                        {
                            if ((" "+retrievedMethods[m].split("[:]+")[3].trim()+" ").contains(" "+v+" "))
                            {
                                cnt++;
                            }
                        }
                        
                        uniqueness += 1 / (float)(cnt-2+1);                                                
                    }
                }
                
                if (cochangeCount > 0)
                {
                    //confidence1 = confidence(m1---->m2) = support(m1 m2) / support (m1)
                    float confidence1 = (float)cochangeCount/ rcount1;

                    //confidence1 = confidence(m2---->m1) = support(m2 m1) / support (m2)
                    float confidence2 = (float)cochangeCount/ rcount2;

                    //br2.write ("\n\n"+m1+":"+m2+":"+cochangeCount+":"+uniqueness+":"+confidence1+":"+confidence2+":"+cochangeCommits);
                    rules[ruleCount] = m1+":"+m2+":"+cochangeCount+":"+uniqueness+":"+confidence1+":"+confidence2+":"+cochangeCommits;
                    ruleCount++;
                }
            }
        }
        
        //quick sorting the rules.        
        quickSortRules(rules, 0, ruleCount-1);

        BufferedWriter br2 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"/rules.txt")) ;
        //writing the rules to the file.
        for (int m=ruleCount-1;m>=0;m--)
        {
            br2.write ("\n\n"+rules[m]);
        }
        
        br2.close();        
    }
    

    void quickSortRules(String arr[], int left, int right)
    {
        int i = left, j = right;
        String tmp = "";
        int pivot = Integer.parseInt(arr[(left + right) / 2].split("[:]+")[2]);

 

        /* partition */
        while (i <= j) 
        {
            while (Integer.parseInt(arr[i].split("[:]+")[2]) < pivot)
            {
                  i++;
            }
            while (Integer.parseInt(arr[j].split("[:]+")[2]) > pivot)
            {
                  j--;
            }            
            if (i <= j) 
            {
                  tmp = arr[i];
                  arr[i] = arr[j];
                  arr[j] = tmp;
                  i++;
                  j--;

            }
      }

      /* recursion */
      if (left < j)
      {
            quickSortRules(arr, left, j);
      }

      if (i < right)
      {
            quickSortRules(arr, i, right);
      }
}
    

    public void getSpecificRules (int sup, float sig1, float sig2) throws Exception
    {
        int cnt=0;
        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"_rules_support.txt")));
        
        System.out.println ("selecting the rules.") ;
        String line =""; 
        while((line = br.readLine())!= null)
        {
            if (line.trim().length() == 0) {continue;}
            
            String m1="", m2="", support="", uniqueness="", confidence1="", confidence2="", revisionList = "";

            support = line.split("[:]+")[1].split("[ ]+")[1].trim();
            uniqueness = line.split("[:]+")[2].split("[ ]+")[1].trim();
            
            if (Integer.parseInt(support) == sup && Float.parseFloat(uniqueness) >= sig2 && Float.parseFloat(uniqueness) < sig1)
            {
                System.out.println (line) ;
                cnt++;
            }            
        }
        System.out.println ("count = "+ cnt);
    }
    
    
    
    public void getResultRulesForSignificance () throws Exception
    {
        int ruleCount = 0;
        String [] rules = new String[50000];
        
        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"_rules.txt")));
        
        System.out.println ("selecting the rules.") ;
        String line =""; 
        while((line = br.readLine())!= null)
        {
            if (line.trim().length() == 0) {continue;}
            
            String m1="", m2="", support="", uniqueness="", confidence1="", confidence2="", revisionList = "";
            
            m1 = line.split("[:]+")[0];
            m2 = line.split("[:]+")[1];
            support = line.split("[:]+")[2];
            uniqueness = line.split("[:]+")[3];
            confidence1 = line.split("[:]+")[4];
            confidence2 = line.split("[:]+")[5];
            revisionList = line.split("[:]+")[6];
            
            if (Float.parseFloat(confidence1) >= 0)
            {
                rules[ruleCount] = m1 + "---->" + m2 + ":support "+support+":significance "+uniqueness+":confidence "+confidence1+":revisionlist "+revisionList;
                ruleCount++;
            }
            /*if (Float.parseFloat(confidence2) >= 0)
            {
                rules[ruleCount] = m2 + "---->" + m1 + ":support "+support+":significance "+uniqueness+":confidence "+confidence2+":revisionlist "+revisionList;
                ruleCount++;
            }*/
        }
        
        
        
        //sorting the rules according to the support values.        
        System.out.println ("sorting the rules according to the support values.") ;
        for (int i =0;i<ruleCount-1;i++)
        {
            int index = i;
            float highest = Float.parseFloat(rules[i].split("[:]+")[1].split("[ ]+")[1]);
            
            for (int j = i+1;j<ruleCount;j++)
            {
                float support = Float.parseFloat(rules[j].split("[:]+")[1].split("[ ]+")[1]);
                if (support > highest)
                {
                    index = j;
                    highest = support;
                }
            }
            String rule = rules[i];
            rules[i] = rules[index];
            rules[index] = rule;
        }
        
        String [] rulesSupport  = new String[50000];
        
        for (int i =0;i<ruleCount;i++)
        {
            rulesSupport[i] = rules[i];
        }
        
        
        
        //sorting the rules according to the significance values.
        System.out.println ("sorting the rules according to the significance values.") ;
        for (int i =0;i<ruleCount-1;i++)
        {
            int index = i;
            float highest = Float.parseFloat(rules[i].split("[:]+")[2].split("[ ]+")[1]);
            
            for (int j = i+1;j<ruleCount;j++)
            {
                float significance = Float.parseFloat(rules[j].split("[:]+")[2].split("[ ]+")[1]);
                if (significance > highest)
                {
                    index = j;
                    highest = significance;
                }
            }
            String rule = rules[i];
            rules[i] = rules[index];
            rules[index] = rule;
        }
        
        String [] rulesSignificance = new String [50000];
        
        for (int i =0;i<ruleCount;i++)
        {
            rulesSignificance[i] = rules[i];
        }        
        
        BufferedWriter br2 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"_rules_support.txt")) ;
        BufferedWriter br3 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"_rules_significance.txt")) ;
        
        for (int i =0;i<ruleCount;i++)
        {
            br2.write ("\n"+rulesSupport[i]);
            br3.write ("\n"+rulesSignificance[i]);
        }
        
        br2.close();
        br3.close();                               
    }
    

    public boolean belongToSameCloneFamily (String gmid1, String gmid2, String revision, String type)
    {
        try
        {
            String line = "", gmid="";
            int f1=-99, f2=-98, g1 = 0, g2 = 0;
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.subject_system+"/clonedmethods/type"+type+"_clonedmethods_version_"+revision+".txt")));
            
            while ((line = br.readLine())!= null)
            {
                if (line.trim().length() == 0){continue;}
                gmid = commonParameters.getAttributeValueFromString(line, an.globalMethodID)+"";
                if (!gmid.equals(gmid1) && !gmid.equals(gmid2)) {continue;}

                if (gmid.equals(gmid1))
                {
                    f1 = Integer.parseInt(commonParameters.getAttributeValueFromString(line, an.cloneClassID));
                    g1 = 1;
                }
                else
                {
                    f2 = Integer.parseInt(commonParameters.getAttributeValueFromString(line, an.cloneClassID));
                    g2 = 1;
                }
                if (g1 == 1 && g2 == 1)
                {
                    br.close();
                    if (f1 == f2)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = belongToSameCloneFamily. "+e);
        }
        return false;
    }
    
    
    
    public void determineClonedRules () throws Exception
    {
        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"/rules.txt")));
        String line = "";
        String [] rules = new String [50000];
        int ruleCount = 0;
        
        while ((line = br.readLine()) != null)
        {
            if (line.length() == 0) {continue;}

            System.out.println ("working on rule = "+line) ;
            
            
            rules[ruleCount] = line;
            //if (!clonesChangedTogether.equals("0"))
            //{
            String revisionList = line.split("[:]+")[6].trim();

            String globalMethodID1 = line.split("[:]+")[0];
            String globalMethodID2 = line.split("[:]+")[1];


            for (int i =0;i<revisionList.split("[ ]+").length;i++)
            {

                String revision = revisionList.split("[ ]+")[i];

                if (belongToSameCloneFamily (globalMethodID1, globalMethodID2, revision, "1"))
                {
                    System.out.println ("Type1 rule");
                    rules[ruleCount] = line + " : "+revision+" Type1";                    
                    break;                                                        
                }

                if (belongToSameCloneFamily (globalMethodID1, globalMethodID2, revision, "2"))
                {
                    System.out.println ("Type2 rule");
                    rules[ruleCount] = line + " : "+revision+" Type2";                    
                    break;                                                        
                }

                if (belongToSameCloneFamily (globalMethodID1, globalMethodID2, revision, "3"))
                {
                    System.out.println ("Type3 rule");
                    rules[ruleCount] = line + " : "+revision+" Type3";                    
                    break;                                                        
                }                                        
            }                
            ruleCount++;
            //}
        }

        //writing the cloned rules.
       //cloned rule = a rule where the cloned portions of both methods had consistency ensuring change.
        BufferedWriter br2 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"/rules.txt")) ;
        for (int i =0;i<ruleCount;i++)
        {
            br2.write ("\n\n"+rules[i]);
        }
        br2.close();        
    }
    

    public boolean checkCondition (RuleCondition ruleCondition, int support, float confidence_xy, float confidence_yx, String location, int antecedent, int consequent, int cloned)
    {
        if ( ruleCondition.support_min <= support && support <= ruleCondition.support_max)
        { 
            //do nothing.
        }
        else
        {
            return false;
        }
        
        if (ruleCondition.xyConfidence_min <= confidence_xy && confidence_xy <= ruleCondition.xyConfidence_max)
        {
            //do nothing.
        }
        else
        {
            return false;
        }
        
        if (ruleCondition.yxConfidence_min <= confidence_yx && confidence_yx <= ruleCondition.yxConfidence_max)
        {
            //do nothing.
        }
        else
        {
            return false;
        }

        //condition checking for method location.
        if (ruleCondition.methodLocation.trim().length() > 0)
        {
            if (location.contains(ruleCondition.methodLocation.trim()) ){ }
            else
            {
                return false;
            }            
        }

        //condition checking for antecedent.
        if (ruleCondition.antecedent > -1)
        {
            if (ruleCondition.antecedent == antecedent) {}
            else {return false;}
        }

        //condition checking for consequent.
        if (ruleCondition.consequent > -1)
        {
            if (ruleCondition.consequent == consequent){}
            else {return false;}
        }

        //condition checking for clones
        if (ruleCondition.cloned == 1)
        {
            if (cloned != 1){ return false; }
        }
        
        
        return true;
    }
    
    
    public void searchRules (RuleCondition ruleCondition) throws Exception
    {
        String [] resultRules = new String [5000000];
        int resultRuleCount = 0, cloned=0;
        String line = "", location = "";
        
        int cmin1 = 0;
        float confidence1 = 1.6f;
        float confidence2= 0.0f;
        
        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"/rules.txt")));
        
        while ((line = br.readLine()) != null)
        {
            if (line.length() == 0) {continue;}
            
            cmin1 = Integer.parseInt(line.trim().split("[:]+")[2]);
            confidence1 = Float.parseFloat(line.trim().split("[:]+")[4]);
            confidence2 = Float.parseFloat(line.trim().split("[:]+")[5]);
            
            String m1 = line.trim().split("[:]+")[0];
            String m2 = line.trim().split("[:]+")[1];

            if (line.contains("same file")){ location = "same file"; }
            else if(line.contains("same folder")) {location = "same folder";}
            else {location = "different folders";}

            if (line.contains("Type")){cloned = 1;}
            else {cloned = 0;}
            
            int l = line.split("[:]+").length;
            
            if (checkCondition(ruleCondition, cmin1, confidence1, confidence2, location, Integer.parseInt(m1), Integer.parseInt(m2), cloned))
            {                                
                String temp = "";
                for (int m = 2;m<l;m++)
                {
                    if (m != 3 && m != 5)
                    {
                        temp = temp + ":"+line.split("[:]+")[m];
                    }
                }
                resultRules[resultRuleCount] = "\n"+m1+"---->"+m2+temp;
                
                resultRuleCount++;                
            }
            if (checkCondition(ruleCondition, cmin1, confidence2, confidence1, location, Integer.parseInt(m2), Integer.parseInt(m1), cloned))
            {
                String temp = "";
                for (int m = 2;m<l;m++)
                {
                    if (m != 3 && m != 4)
                    {
                        temp = temp + ":"+line.split("[:]+")[m];
                    }
                }
                
                resultRules[resultRuleCount] = "\n"+m2+"---->"+m1+temp;
                resultRuleCount++;                                
            }
        }
        br.close();
        
        
        //writing the result rules in a file.
        BufferedWriter br2 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"/resultrules.txt")) ;
        for (int i =0;i<resultRuleCount;i++)
        {
            br2.write("\n"+resultRules[i]);
        }
        br2.close();        
    }
    
    public void findHighlyCoupledMethods () throws Exception
    {
        String [] highlyCoupledMethods = new String [50000];
        int highlyCoupledMethodCount = 0;

        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"/resultrules.txt")));
        String line = "";
        
        int c = 0;
        while ((line = br.readLine()) != null)
        {
            if (line.length() == 0) {continue;}
            
            String antecedent = "";
            String consequent = "";            
            
            antecedent = line.split("[:]+")[0].split("[---->]+")[0];
            consequent = line.split("[:]+")[0].split("[---->]+")[1];
            
            int i =0;
            for (i=0;i<highlyCoupledMethodCount;i++)
            {
                if (highlyCoupledMethods[i].contains(consequent+":"))
                {
                    break;
                }
            }
            if (i == highlyCoupledMethodCount) // this is a new consequent
            {
                highlyCoupledMethods[highlyCoupledMethodCount] = consequent+":  "+antecedent+" ";
                highlyCoupledMethodCount++;
            }
            else // this is an old consequent
            {
                highlyCoupledMethods[i] += "  " + antecedent+" ";
            }
            
            c++;
            System.out.println ("rule complete = " + c ) ;
        }
        
        int totalCount = 0;
        
        //determining the count of antecedents for a consequent.
        
        for (int i =0;i<highlyCoupledMethodCount;i++)
        {
            int count = highlyCoupledMethods[i].split("[:]+")[1].trim().split("[ ]+").length;
            highlyCoupledMethods[i] += " : " + count;
            totalCount += count;
        }
        
        //sorting the highly coupled methods according to the count of antecedents.
        
        for (int i =0;i<highlyCoupledMethodCount-1;i++)
        {
            int hcount = Integer.parseInt(highlyCoupledMethods[i].split("[:]+")[2].trim());
            int hindex = i;
            for (int j =i+1;j<highlyCoupledMethodCount;j++)
            {
                if (hcount < Integer.parseInt(highlyCoupledMethods[j].split("[:]+")[2].trim()))
                {
                    hcount = Integer.parseInt(highlyCoupledMethods[j].split("[:]+")[2].trim());
                    hindex = j;
                }                
            }
            String temp = "";
            temp = highlyCoupledMethods[i];
            highlyCoupledMethods[i] = highlyCoupledMethods[hindex];
            highlyCoupledMethods[hindex] = temp;            
        }
        
        //writing the sorted highly coupled methods in the file.
        BufferedWriter br2 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"/rankedmethods.txt")) ;
        
        
        
        for (int i =0;i<highlyCoupledMethodCount;i++)
        {
            br2.write ("\n\n"+highlyCoupledMethods[i]);
        }
        //br2.write("\n\navg coupling = "+(float)totalCount/highlyCoupledMethodCount) ;
        br2.close();
        
    }
    
    
    public void clonedHighlyCoupledMethods () throws Exception
    {
        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"_rankedmethods.txt")));
        String line = "";
        
        String [] retrievedMethods = new String[5000];
        int retrievedMethodCount = 0;
        
        //getting the methods.
        while ((line = br.readLine()) != null)
        {
            if (line.length() == 0) {continue;}

            retrievedMethods[retrievedMethodCount] = line;
            retrievedMethodCount++;
        }

        //getting the cloned rules.
        BufferedReader br1 = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"_clonedrules.txt")));
        
        String [] clonedRules = new String[5000];
        int clonedRuleCount = 0;
        
        while ((line = br1.readLine()) != null)
        {
            if (line.length() == 0) {continue;}

            clonedRules[clonedRuleCount] = line;
            clonedRuleCount++;
        }
        
        String [] clonedMethods = new String[50000];
        int clonedMethodCount = 0;
        int t1Count = 0, t2Count = 0, t3Count = 0;
        
        for (int i =0;i<clonedRuleCount;i++)
        {
            String m1 = clonedRules[i].split("[:]+")[0];
            String m2 = clonedRules[i].split("[:]+")[1];
            
            int j = 0;
            for (j=0;j<retrievedMethodCount;j++)
            {
                String m = retrievedMethods[j].split("[:]+")[0];
                if (m.equals(m1))
                {
                    if (retrievedMethods[j].split("[:]+")[1].contains(m2))
                    {
                        clonedMethods[clonedMethodCount] = clonedRules[i];
                        clonedMethodCount++;
                        if (clonedRules[i].contains ("Type1"))
                        {
                            t1Count++;
                        }
                        else if (clonedRules[i].contains ("Type2"))
                        {
                            t2Count++;
                        }
                        else
                        {
                            t3Count++;
                        }
                        break;
                    }
                }
                if (m.equals(m2))
                {
                    if (retrievedMethods[j].split("[:]+")[1].contains(m1))
                    {
                        clonedMethods[clonedMethodCount] = clonedRules[i];
                        clonedMethodCount++;
                        if (clonedRules[i].contains ("Type1"))
                        {
                            t1Count++;
                        }
                        else if (clonedRules[i].contains ("Type2"))
                        {
                            t2Count++;
                        }
                        else
                        {
                            t3Count++;
                        }
                        break;
                    }                    
                }
            }
        } 
        
        int mCount = 0;
        
        for (int i =0;i<retrievedMethodCount;i++)
        {
            int j=0;
            for (j=0;j<clonedMethodCount;j++)
            {
                String clonedmethods = " " + clonedMethods[j].split("[:]+")[0] + " "+ clonedMethods[j].split("[:]+")[1] + " ";
                
                if (clonedmethods.contains(" "+retrievedMethods[i].split("[:]+")[0].trim()+" "))
                {
                    mCount++;
                    break;
                }
            }
        }
        
                
        BufferedWriter br2 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"_rankedclonedmethods.txt")) ;
        br2.write ("Type 1 = "+t1Count + " Type 2 = "+t2Count+" Type 3 = "+t3Count + " Method Count = "+mCount);
        for (int i =0;i<clonedMethodCount;i++)
        {
            br2.write ("\n\n"+clonedMethods[i]);
        }
        br2.close();
    }
    
    
    
    // this method determines whether methods are from 
    // same file or different files in the same folder
    // or different folders.
    // It searches the result rules only. not the entire rules.
    public void searchStructuralWeakness () throws Exception
    {
        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"/changedmethods.txt")));
        String line = "";
        
        String [] retrievedMethods = new String[500000];
        int retrievedMethodCount = 0;
        
        //getting the methods.
        while ((line = br.readLine()) != null)
        {
            if (line.length() == 0) {continue;}

            retrievedMethods[retrievedMethodCount] = line;
            retrievedMethodCount++;
        }
        
        BufferedReader br1 = new BufferedReader (new InputStreamReader(new FileInputStream(commonParameters.subject_system+"/rules.txt")));
        line = "";
        
        String [] resultRules = new String[500000];
        int resultRuleCount = 0;
        
        //getting the result rules.
        while ((line = br1.readLine()) != null)
        {
            if (line.length() == 0) {continue;}

            resultRules[resultRuleCount] = line;
            resultRuleCount++;
        }
        
        //updating the result rules with file and method name information.
        
        String [] structuralWeaknesses = new String[5000000];
        int structuralWeaknessCount = 0;
        
        int sameFile = 0, sameFolder = 0, differentFolder = 0;
        
        for (int i =0;i< resultRuleCount; i++)
        {
            String m1 = "", m2 = "";
            String file1 = "", file2 = "";
            
            //m1 = resultRules[i].split("[:]+")[0].split("[---->]+")[0];
            //m2 = resultRules[i].split("[:]+")[0].split("[---->]+")[1];
            m1 = resultRules[i].split("[:]+")[0].trim();
            m2 = resultRules[i].split("[:]+")[1].trim();
            
            
            int j =0, got1 = 0, got2 = 0;
            for (j=0;j<retrievedMethodCount;j++)
            {
                if (retrievedMethods[j].split("[:]+")[0].equals(m1) && got1 == 0)
                {
                    file1 = retrievedMethods[j].split("[:]+")[2];
                    got1 = 1;
                }
                if (retrievedMethods[j].split("[:]+")[0].equals(m2) && got2 == 0)
                {
                    file2 = retrievedMethods[j].split("[:]+")[2];
                    got2 = 1;
                }
                
                if (got1 == 1 && got2 ==1)
                {
                    break;
                }
            }   
            
            if (file1.equals(file2))
            {
                structuralWeaknesses[structuralWeaknessCount] = resultRules[i]+" : same file";
                structuralWeaknessCount++;
                sameFile++;
            }
            else
            {                
                int l1 = file1.split("/").length;
                String folder1 = "", f1 = "", folder2 = "", f2 = "";
                f1 = file1.split("/")[l1-1];
                if (l1 > 1)
                {
                    int l = f1.length();
                    folder1 = file1.substring(0, file1.length()-l);                    
                }
                
                int l2 = file2.split("/").length;
                f2 = file2.split("/")[l2-1];
                if (l2 > 1)
                {
                    int l = f2.length();
                    folder2 = file2.substring(0, file2.length()-l);                    
                }                
                
                if (folder1.equals(folder2))
                {
                    structuralWeaknesses[structuralWeaknessCount] = resultRules[i]+" : same folder different files";
                    structuralWeaknessCount++;
                    sameFolder++;
                }
                else
                {
                    structuralWeaknesses[structuralWeaknessCount] = resultRules[i]+" : different folder";
                    structuralWeaknessCount++;                    
                    differentFolder++;
                }                
            } 
            
            System.out.println ("weakness complete = "+(i+1)+" of "+resultRuleCount);
        }   
        
        BufferedWriter br2 = new BufferedWriter (new FileWriter (commonParameters.subject_system+"/rules.txt")) ;

        //br2.write ("same file = "+ (float)sameFile*100/resultRuleCount + " Same folder = " + (float)sameFolder*100/resultRuleCount + " Different Folder = " + (float)differentFolder*100/resultRuleCount);
        
        for (int i =0;i<structuralWeaknessCount;i++)
        {
            br2.write ("\n\n"+structuralWeaknesses[i]);
        }
        
        br2.close();                
    }
            
}


//class Database {
//    Connection conn ;
//    Statement stmt ;
//    public ResultSet result ;
//
//    String connectionString = "jdbc:mysql://localhost:3306/lozano_imprint_qmailadmin" ;
//    String userID = "root" ;
//    String password = "" ;
//
//
//    //connection is made here.
//    public void connect () throws Exception
//    {
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        conn = DriverManager.getConnection(connectionString, userID, password);
//        //System.out.println("connected");
//    }
//
//    //connection is closed here.
//    public void disconnect () throws Exception
//    {
//        conn.close();
//    }
//
//    //query is made here.
//    public void executeQuery (String query)
//    {
//        try
//        {
//            stmt = conn.createStatement();	   //creating the statement.
//            result = stmt.executeQuery(query); //executing the query.
//        }
//        catch (Exception e)
//        {
//            System.out.println ("\ncan not connect"  + e) ;
//        }
//    }
//
//    //this is for inserting.
//    public void executeUpdate (String query)
//    {
//        try
//        {
//            connect () ;
//
//            stmt = conn.createStatement();	   //creating the statement.
//            stmt.executeUpdate(query); //executing the query.
//
//            disconnect () ;
//        }
//        catch (Exception e)
//        {
//            System.out.println ("\ncan not connect"  + e) ;
//        }
//    }
//}

//class SourceFileInformation
//{
//    String fileName, originalFilePath ;
//    int cloneCount ;
//}

class CCFinder1
{
    String repositoryURL = "";
    //static String ccfinderCommandPath = "d:/tools/ccfinder/bin/ccfx" ;
    
    String language_extension = " c h ";
    String ccfinder_language_command = "cpp";
    String preprocessed_file_extension = ".cpp.2_0_0_2.default.ccfxprep";
    String method_symbol = "f";
    
    
    SourceFileInformation [] sourceFiles = new SourceFileInformation [20000];
    int sourceFileCount = 0;
    CommonParameters commonParameters = new CommonParameters ();
    
    
    /*CCFinder (SubjectSystem ss, int version) throws Exception
    {
        initializeCCFinder (ss);
        detectClonesAndFamiliesFromVersion(version);
    }*/
    
    public void initializeCCFinder ()
    {
        repositoryURL = commonParameters.repositoryURL;
        sourceFileCount = 0;
    }
    
    public void detectClonesAndFamiliesFromVersion ( int version ) throws Exception
    {
        String repositoryPath = repositoryURL+"version-"+version;
        getAllFilesOfFolder (repositoryPath+"/.ccfxprepdir", "ccfxprep");
    }    
    
    public void getAllFilesOfFolder (String folderName, String fileExtension)
    {
        try
        {
        //System.out.println ("------> I am retrieving all files of folder: "+ folderName) ;
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                if (fileExtension.indexOf(listOfFiles[i].getName().split("[.]+")[listOfFiles[i].getName().split("[.]+").length-1]) > -1)
                {
                    //inserting the java file names into a global array.
                    sourceFiles[sourceFileCount] =  new SourceFileInformation () ;
                    sourceFiles[sourceFileCount].fileName = folderName + "/" + listOfFiles[i].getName() ;
                    String t = sourceFiles[sourceFileCount].fileName;
                    t = t.replace("/.ccfxprepdir", "");
                    t = t.replace (preprocessed_file_extension, "");
                    sourceFiles[sourceFileCount].originalFilePath = t ;                    
                    sourceFileCount++;
                }
            }
            else if (listOfFiles[i].isDirectory())
            {
                getAllFilesOfFolder (folderName+"/"+listOfFiles[i].getName(),fileExtension ) ;
            }
        }
        }
        catch (Exception e)
        {
            System.out.println ("getAllFilesOfFolder");
            System.out.println (e.getStackTrace()) ;
        }

    } //public static void getAllMethodsOfFolder (String folderName)

    
    
    public String getTokenSequence (String filepath, int slinenumber, int elinenumber, int version)
    {
        try
        {
        int i=0;
        String token_file_path = "", str="", original_file_path = "";
        token_file_path = repositoryURL+"version-"+version+"/"+filepath;
        for (i=0;i<sourceFileCount;i++)
        {
            original_file_path = sourceFiles[i].originalFilePath.replace('\\', '/');
            if (original_file_path.equals(token_file_path))
            {
                token_file_path = sourceFiles[i].fileName; //this is the prep dir filepath
                break;
            }
        }

        FileInputStream fstream = new FileInputStream (token_file_path);
        BufferedReader br = new BufferedReader (new InputStreamReader (fstream)) ;

        int lineno=0;
        int got = 0;
        String tokenSequence = "";
        
        while ((str = br.readLine())!= null)
        {
            //token_number++;
            lineno = Integer.parseInt(str.split("[.]+")[0],16);
            if (lineno >= slinenumber)
            {
                got = 1;
            }
            
            if (lineno >= elinenumber+1)
            {
                br.close();
                return tokenSequence ;
                //break;
            }
            
            if (got == 1)
            {
                tokenSequence += " " + str.split("[\t]+")[2];                
            }
        }
        br.close();
        }
        catch (Exception e)
        {
            System.out.println ("getTokenSequence");
            System.out.println (e.getStackTrace());
        }
        return "";
    }
    
}