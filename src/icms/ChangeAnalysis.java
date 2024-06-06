/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package icms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.*;
/**
 *
 * @author smriti
 */
public class ChangeAnalysis extends javax.swing.JFrame {

    CommonParameters commonParameters = new CommonParameters ();
    
    public String test1 = "";
    public String method1Previous = "", method1Post = "", method2Previous = "", method2Post = "";
    public String method1Diff = "", method2Diff = "";
    public String ruleHistory = "";
    public String method1 = "13", method2 = "35";
    public String methodName1 = "", methodName2 = "";
    public String filePath1 = "", filePath2 = "";
    public String commitList1 = " 42  51  54  58  91  153  156  158 ", commitList2 = " 42  51  54  58  91  153  156  158  591  593 ";
    public String cloneInformation ="", locationInformation = "", commitList = "";
    
    public String cochangeList = "42  51  54  58  91  153  156  158";
    public String cloneType = "";
    int currentCommit = 42;
    int currentIndex = 0;
    public String ruleSupport = "";
    public String ruleConfidence = "";
    public int m1=0, m2=0;

    public AttributeNames an = new AttributeNames ();
    
    String tempfile1 = "file1.txt";
    String tempfile2 = "file2.txt";
    String tempfile3 = "file3.txt";
    String tempfile4 = "file4.txt";
    
    
    DatabaseAccess da = new DatabaseAccess ();
    
    
    
    
    /**
     * Creates new form ChangeAnalysis
     */
    public ChangeAnalysis() {
        initComponents();
    }
    
    public void analyzeRuleConsistingOfMethods (int mid1, int mid2)
    {
        try
        {
            String methodInfo1 = getMethodInfo(mid1);
            String methodInfo2 = getMethodInfo (mid2);
            String ruleInfo = getRuleContainingMethods(mid1, mid2);
            
            
            method1 = ruleInfo.trim().split("[:]+")[0].trim().split("[---->]+")[0];
            method2 = ruleInfo.trim().split("[:]+")[0].trim().split("[---->]+")[1];
            //methodName1 = "", methodName2 = "", methodFile1 ="", methodFile2="", commits1="", commits2= "";
            String ctype = "";

            if (method1.equals(methodInfo1.split("[:]+")[0].trim()))
            {
                methodName1 = methodInfo1.split("[:]+")[1].trim();
                methodName2 = methodInfo2.split("[:]+")[1].trim();
                filePath1 = methodInfo1.split("[:]+")[2].trim();
                filePath2 = methodInfo2.split("[:]+")[2].trim();
                commitList1 = methodInfo1.split("[:]+")[3].trim();
                commitList2 = methodInfo2.split("[:]+")[3].trim();
            }
            else
            {
                methodName2 = methodInfo1.split("[:]+")[1].trim();
                methodName1 = methodInfo2.split("[:]+")[1].trim();
                filePath2 = methodInfo1.split("[:]+")[2].trim();
                filePath1 = methodInfo2.split("[:]+")[2].trim();
                commitList2 = methodInfo1.split("[:]+")[3].trim();
                commitList1 = methodInfo2.split("[:]+")[3].trim();
            }

            String temp = "";
            
            //getting clone information.
            if (ruleInfo.contains("Type"))
            {                
                if (ruleInfo.contains("Type1"))
                {
                    ctype = "Type1";
                }
                else if (ruleInfo.contains ("Type2"))
                {
                    ctype = "Type2";
                }
                else
                {
                    ctype = "Type3";
                }
                //temp += "This rule is related to changes to the clone fragments from the same clone class (Clone Type = "+(String)jTable1.getValueAt(row, 5)+").";
                cloneInformation = "This rule is related to changes to the clone fragments (Clone Type = "+ctype+") from the same clone class.";
                //temp += "<br/>Clone Type = "+(String)jTable1.getValueAt(row, 5)+"";
                //temp += "<br/>Changes occurred in commit on revision = "+(String)jTable1.getValueAt(row, 6);s
            }

            //getting location information.
            if (ruleInfo.contains ("same file"))
            {
                //if (temp.trim().length() > 0)
                //{
                    //    temp += "<br/>";
                    //}
                //temp += "The methods ("+methodName1+", "+methodName2+") in this rule belong to the same file.";
                locationInformation = "The <b>Antecedent</b> ("+methodName1+"), and <b>Consequent</b> ("+methodName2+") in this rule belong to the same file.";
            }
            else if (ruleInfo.contains("same folder different files"))
            {
                //if (temp.trim().length() > 0)
                //{
                    //   temp += "<br/>";
                    //}
                //temp += "The methods ("+methodName1+", "+methodName2+") in this rule belong to different files of the same folder.";
                locationInformation = "The <b>Antecedent</b> ("+methodName1+"), and <b>Consequent</b> ("+methodName2+") in this rule belong to different files of the same folder.";
            }
            else
            {
                //if (temp.trim().length() > 0)
                //{
                    //    temp += "<br/>";
                    //}
                //temp += "The methods ("+methodName1+", "+methodName2+") in this rule belong to different folders.";
                locationInformation = "The <b>Antecedent</b> ("+methodName1+"), and <b>Consequent</b> ("+methodName2+") in this rule belong to different folders.";
            }

            //getting the co-change commit list.
            //temp += "<br/>The list of commits where these methods co-changed = "+(String)jTable1.getValueAt(row, 3) + "&nbsp;(Co-change Count = "+((String)jTable1.getValueAt(row, 3)).trim().split("[ ]+").length +")";
            commitList =  "The list of commits where these methods co-changed = "+ruleInfo.split("[:]+")[3] + "&nbsp;(Co-change Count = "+ruleInfo.split("[:]+")[3].trim().split("[ ]+").length +")";
            temp += "";

            ruleHistory = "<html>"+temp+"</html>";
            currentCommit = Integer.parseInt(ruleInfo.split("[:]+")[3].trim().split("[ ]+")[0]);
            currentIndex = 0;
            //method1 = method1+"";
            //changeAnalysis.method2 = method2+"";
            //changeAnalysis.methodName1 = methodName1;
            //changeAnalysis.methodName2 = methodName2;
            //changeAnalysis.filePath1 = methodFile1;
            //changeAnalysis.filePath2 = methodFile2;
            //changeAnalysis.commitList1 = commits1;
            //changeAnalysis.commitList2 = commits2;
            cochangeList = ruleInfo.split("[:]+")[3].trim();
            //changeAnalysis.setVisible(true);
            //changeAnalysis.setDefaultCloseOperation(HIDE_ON_CLOSE);
            cloneType = ctype;
            ruleSupport = ruleInfo.split("[:]+")[1].trim();
            ruleConfidence = ruleInfo.split("[:]+")[2].trim();                                    
        }
        catch (Exception e)
        {
            
        }
    }    
    
    public String getMethodInfo (int mid)
    {
        try
        {
            String line = "";
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream( commonParameters.subject_system+"/changedmethods.txt")));
            while ((line = br.readLine())!= null)
            {
                if (line.trim().length() ==0){continue;}

                if (Integer.parseInt(line.trim().split("[:]+")[0].trim()) == mid)
                {
                    br.close();
                    return line;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = getMethodInfo." + e);
        }

        return "";
    }

    public String getRuleContainingMethods (int mid1, int mid2)
    {
        try
        {
            String line = "";
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream( commonParameters.subject_system+"/resultrules.txt")));
            while ((line = br.readLine())!= null)
            {
                if (line.trim().length() ==0){continue;}

                if (line.trim().split("[:]+")[0].trim().equals(mid1+"---->"+mid2) || line.trim().split("[:]+")[0].trim().equals(mid2+"---->"+mid1))
                {
                    br.close();
                    return line;
                }
            }
        }
        catch (Exception e)
        {
            
        }
        
        return "";
    }
    

    public void generateRevisionList ()
    {
        int [] antRevisionList = new int [500];
        int antRevisionCount = 0;

        int [] conRevisionList = new int [500];
        int conRevisionCount = 0;

        
        int len = 0, i=0, j=0;
        len = commitList1.trim().split("[ ]+").length;
        antRevisionCount = len;
        for (i =0;i<len;i++)
        {
            antRevisionList[i] = Integer.parseInt(commitList1.trim().split("[ ]+")[i]);
        }

        //sorting
        for (i=0;i<antRevisionCount-1;i++)
        {
            int li = i;

            for (j=i+1;j<antRevisionCount;j++)
            {
                if (antRevisionList[j] < antRevisionList[li])
                {
                    li = j;
                }
            }
            int t = antRevisionList[i];
            antRevisionList[i] = antRevisionList[li];
            antRevisionList[li] = t;
        }

        System.out.println (antRevisionList) ;
        
        len = commitList2.trim().split("[ ]+").length;
        conRevisionCount = len;
        for (i=0;i<len;i++)
        {
            conRevisionList[i] = Integer.parseInt (commitList2.trim().split("[ ]+")[i]);
        }

        //sorting
        for (i=0;i<conRevisionCount-1;i++)
        {
            int li = i;

            for (j=i+1;j<conRevisionCount;j++)
            {
                if (conRevisionList[j] < conRevisionList[li])
                {
                    li = j;
                }
            }
            int t = conRevisionList[i];
            conRevisionList[i] = conRevisionList[li];
            conRevisionList[li] = t;
        }

        //mering
        int l=0;
        
        for (i=0;i<antRevisionCount;i++)
        {
            for (j=0;j<conRevisionCount;j++)
            {
                if (antRevisionList[i] == conRevisionList[j]){break;}
                if (conRevisionList[j] > antRevisionList[i])
                {
                    for(l=conRevisionCount-1;l>=j;l--)
                    {
                        conRevisionList[l+1] = conRevisionList[l];
                    }
                    conRevisionList[j] = antRevisionList[i];
                    conRevisionCount++;
                    break;
                }
            }
            if (j == conRevisionCount)
            {
                conRevisionList[j] = antRevisionList[i];
                conRevisionCount++;
            }
        }

        String changedBox = "width:15px; height: 15px; border-width: 1px; border-color: black; border-style: solid; background-color: #0000ff;";
        String unchangedBox = "width:15px; height: 15px; border-width: 1px; border-color: black; border-style: solid;";
        String tableString = "<table cellspacing='4' cellpadding ='4'>";
        tableString += "<tr><td style='text-align: right;padding-right: 20px;'>Commit List</td>";
        for (i=0;i<conRevisionCount;i++)
        {
            tableString += "<td>"+conRevisionList[i]+"</td>";
        }
        tableString += "</tr>";
        tableString += "<tr><td style='text-align: right;padding-right: 20px;'>Method Name = "+methodName1+", Method ID = "+method1+" (<b>Antecedent</b>)</td>";
        for (i=0;i<conRevisionCount;i++)
        {
            if ((" "+commitList1+" ").contains(" "+conRevisionList[i]+" "))
            {
                tableString += "<td> <div style='"+changedBox+"'></div> </td>";
            }
            else
            {
                tableString += "<td> <div style='"+unchangedBox+"'></div> </td>";
            }
        }
        tableString += "<td style='padding-left: 50px;'> <div style='"+changedBox+"'></div> </td> <td> Method changed </td>";
        tableString += "</tr>";
        tableString += "<tr><td style='text-align: right; padding-right: 20px;'>Method Name = "+methodName2+", Method ID = "+method2+" (<b>Consequent</b>)</td>";
        for (i=0;i<conRevisionCount;i++)
        {
            if ((" "+commitList2+" ").contains(" "+conRevisionList[i]+" "))
            {
                tableString += "<td> <div style='"+changedBox+"'></div> </td>";
            }
            else
            {
                tableString += "<td> <div style='"+unchangedBox+"'></div> </td>";
            }
        }
        tableString += "<td style='padding-left: 50px;'> <div style='"+unchangedBox+"'></div> </td> <td> Method did not change </td>";
        tableString += "</tr>";  
        tableString += "<tr><td style='text-align: right; padding-right: 20px;'>Antecedent and Consequent Co-changed</td>";
        for (i=0;i<conRevisionCount;i++)
        {
            if ((" "+commitList+" ").contains(" "+conRevisionList[i]+" "))
            {
                tableString += "<td> Yes </td>";
            }
            else
            {
                tableString += "<td> No </td>";
            }
        }        
        tableString += "</tr>";
        tableString += "</table>";        

        jLabel15.setText ("<html>"+tableString+"</html>");

        String labelString = "<table><tr>";
        labelString += "<td valign = 'middle'>Antecedent file path &nbsp;&nbsp;= "+filePath1 +" <br/>Consequent file path = "+filePath2 +"";
        labelString += "<br/><br/>"+locationInformation;
        labelString += "<br/>"+cloneInformation+"</td>";
        labelString += "<td valign='top' style='padding-left: 20px;'><b>Rule = ( "+method1+" => "+method2+" )</b> <br/> <b>Rule Support = "+ruleSupport+"</b><br/><b>Rule Confidence = "+ruleConfidence+"</b></td></tr>";
        //labelString += "Antecedent file path &nbsp;&nbsp;= "+filePath1 +"" ;
        //labelString += "<br/>Consequent file path = "+filePath2 +"" ;
        //labelString += "<br/><b>"+cloneInformation+"</b>";
        //labelString += "<br/><b>"+locationInformation+"</b>";
        //labelString += "<br/>"+locationInformation;

        jLabel16.setText ("<html>"+labelString+"</html>");

    }

    
    public boolean writeClone (int cid, int rev, int cloneType, String file) throws Exception
    {
        String commonFilePath =commonParameters.repositoryURL+ "/version-"+rev;
        int version = rev  ;
        String str="";
        String filename = "";
        int sline, eline;

       
        String cloneString = commonParameters.getCloneOfRevision (cid, version, cloneType);
        if (cloneString.trim().length() == 0) {return false;}
        filename = commonParameters.getAttributeValueFromString(cloneString, an.cloneFilePath);
        sline = Integer.parseInt(commonParameters.getAttributeValueFromString(cloneString, an.cloneStartingLine));
        eline = Integer.parseInt(commonParameters.getAttributeValueFromString(cloneString, an.cloneEndingLine));
        filename = commonFilePath+"/" + filename;

        BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
        BufferedWriter br1 = new BufferedWriter (new FileWriter (file));
        
        int i =0;
        while ((str = br.readLine())!= null)
        {
            i++;
            if (i >= sline && i<= eline)
            {
                br1.write(str+"\n"); //so there is always a new line at the end.
            }
        }
        br1.close();
        br.close();
        //System.out.println ("file is written.");  
        return true;
    }
    
    public boolean writeFile (String fromfile, int startline, int endline, String tofile )
    {
        try
        {
            String str = "";
            BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream (fromfile)));
            BufferedWriter br1 = new BufferedWriter (new FileWriter (tofile));

            int i =0;
            while ((str = br.readLine())!= null)
            {
                i++;
                if (i >= startline && i<= endline)
                {
                    br1.write(str+"\n");
                }
            }
            br1.close();
            br.close();
            
            return true;        
        }
        catch (Exception e)
        {
            return false;
        }
        
    }

    public boolean writeMethod (int m, int rev, String file) throws Exception
    {
        String commonFilePath =commonParameters.repositoryURL+ "/version-"+rev;
        int version = rev  ;
        String str="";
        String filename = "";
        int sline=-1, eline=-1;

        
        //getting information from database.
        String [] methods = da.getData("methods", "filepath, startline, endline, methodid", " where revision = "+version);
        for (int i=0;methods[i]!=null;i++)
        {
            int methodid = Integer.parseInt(methods[i].split("[,]+")[3].trim());
            if (methodid == m)
            {
                filename = methods[i].split("[,]+")[0].trim();
                sline = Integer.parseInt(methods[i].split("[,]+")[1].trim());
                eline = Integer.parseInt(methods[i].split("[,]+")[2].trim());
                break;
            }
        }
        
        
        
       
        /*String methodString = commonParameters.getMethodOfRevision (m, version);
        if (methodString.trim().length() == 0) {return false;}
        filename = commonParameters.getAttributeValueFromString(methodString, an.filePath);
        sline = Integer.parseInt(commonParameters.getAttributeValueFromString(methodString, an.startingLine));
        eline = Integer.parseInt(commonParameters.getAttributeValueFromString(methodString, an.endingLine));*/
        filename = commonFilePath+"/" + filename;

        BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
        BufferedWriter br1 = new BufferedWriter (new FileWriter (file));
        
        int i =0;
        while ((str = br.readLine())!= null)
        {
            i++;
            if (i >= sline && i<= eline)
            {
                br1.write(str+"\n");
            }
        }
        br1.close();
        br.close();
        //System.out.println ("file is written.");
        return true;
    }
    
    
    public String [] getCloneWithLineNumber (int cid, int rev, String file) throws Exception
    {
        String [] cloneBody = new String[5000];
        int lineCount = 0;
        String str = "";

        BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream (file)));
        while ((str = br.readLine())!= null)
        {
                cloneBody[lineCount] = (lineCount+1)+" "+str;
                lineCount++;
        }

        return cloneBody;
    }
    
    

    public String [] getMethodWithLineNumber (int m, int rev, int similarity, String file) throws Exception
    {
        String [] methodBody = new String[5000];
        int lineCount = 0;
        String str = "";
        int startingClonedLine = -1, endingClonedLine = -1;
        String clonedPart = "";
        if (similarity == 1)
        {
            clonedPart = getClonedPartOfMethod(m, rev);
            if (clonedPart.trim().length() > 0)
            {
                startingClonedLine = Integer.parseInt(clonedPart.split("[ ]+")[0].trim());
                endingClonedLine = Integer.parseInt(clonedPart.split("[ ]+")[1].trim());
            }
        }

        BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream (file)));
        while ((str = br.readLine())!= null)
        {
                methodBody[lineCount] = (lineCount+1)+" "+str;
                lineCount++;
        }

        if (similarity == 1)
        {
            for (int i =0;methodBody[i] != null; i++)
            {
                if (i+1 >= startingClonedLine && i+1 <= endingClonedLine)
                {
                    String part1 = methodBody[i].split("[ ]+")[0];
                    int len = part1.length();
                    String part2 = methodBody[i].substring(len);
                    part2 = "<i><b>" + part2 + "</b></i>";
                    methodBody[i] = part1 + " " + part2;
                }
            }
        }
        
        return methodBody;
    }

    public String getClonedPartOfMethod (int m, int rev) throws Exception
    {
        String startingline = "", endingline = "";
        int type = Integer.parseInt(cloneType.charAt(4)+"");
        
        String methodString = commonParameters.getClonedMethodOfRevision(m, rev, type);

        
        startingline = commonParameters.getAttributeValueFromString (methodString,an.cloneStartingLine)+"";
        endingline = commonParameters.getAttributeValueFromString(methodString,an.cloneEndingLine)+"";
        
        System.out.println ("startingline = "+startingline) ;
        System.out.println ("endingline = "+endingline) ;

        return startingline + " " + endingline;
    }

    public String [] getFileDiff (String file1, String file2)
    {
        String [] diff = new String [5000];
        try
        {
            Process p = Runtime.getRuntime().exec(commonParameters.windiffCommandPath + " "+file1+" "+file2+"");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            
            int lineCount = 0;

            while ((line = in.readLine()) != null)
            {
                diff[lineCount] = line;
                lineCount++;
            }
            in.close();            
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getFileDiff. "+ e);
        }

        return diff;
        
    }
    
    public int getChangeCountInMethod (int mid, int rev)
    {
        try
        {
            String [] diff12  = new String[5000];
            
            String [] strings1 = readMethod (mid, rev);
            String [] strings2 = readMethod (mid, rev+1);
            
            if (compareStrings(strings1, strings2) == true)
            {
                return 0;
            }
            

            if (writeMethod(mid, rev, tempfile1) && writeMethod (mid, rev+1, tempfile2))
            {
                diff12 = getFileDiff(tempfile1, tempfile2);            
            }
            
            int cc = 0;
            for (int i =0;diff12[i] != null;i++ )
            {
                String line = diff12[i];
                if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}
                cc++;
            }
            return cc;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = analyzeHistory. "+e);
        }
        
        return 0;        
    }
    
    
    public boolean compareStrings (String [] strings1, String [] strings2)
    {
        try
        {
            int i =0;
            while (strings1[i] != null)
            {
                
                if (strings2[i] == null)
                {
                    return false;
                }
                
                if (!strings1[i].equals (strings2[i]))
                {
                    return false;
                }
                
                i++;
            }
            
            if (strings2[i] == null)
            {
                return true;
            }
            
            return false;
        }
        catch (Exception e)
        {
            System.out.println ("error. "+e);
        }
        return false;
    }
    
    
    public String [] readClone (int cid, int rev, int cloneType) throws Exception
    {
        String [] result = new String [10000];
        int n = 0;
        
        String commonFilePath =commonParameters.repositoryURL+ "/version-"+rev;
        int version = rev  ;
        String str="";
        String filename = "";
        int sline, eline;

       
        String cloneString = commonParameters.getCloneOfRevision (cid, version, cloneType);
        if (cloneString.trim().length() == 0) {return result;}
        filename = commonParameters.getAttributeValueFromString(cloneString, an.cloneFilePath);
        sline = Integer.parseInt(commonParameters.getAttributeValueFromString(cloneString, an.cloneStartingLine));
        eline = Integer.parseInt(commonParameters.getAttributeValueFromString(cloneString, an.cloneEndingLine));
        filename = commonFilePath+"/" + filename;

        BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
        
        int i =0;
        while ((str = br.readLine())!= null)
        {
            i++;
            if (i >= sline && i<= eline)
            {
                result[n] = str;
                n++;
            }
        }
        br.close();
        return result;
    }
    
    
    public String [] readMethod (int gmid, int rev) throws Exception
    {
        String [] result = new String [10000];
        int n = 0;
        
        String commonFilePath =commonParameters.repositoryURL+ "/version-"+rev;
        int version = rev  ;
        String str="";
        String filename = "";
        int sline, eline;

       
        String methodString = commonParameters.getMethodOfRevision (gmid, version);
        if (methodString.trim().length() == 0) {return result;}
        filename = commonParameters.getAttributeValueFromString(methodString, an.filePath);
        sline = Integer.parseInt(commonParameters.getAttributeValueFromString(methodString, an.startingLine));
        eline = Integer.parseInt(commonParameters.getAttributeValueFromString(methodString, an.endingLine));
        filename = commonFilePath+"/" + filename;

        BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
        
        int i =0;
        while ((str = br.readLine())!= null)
        {
            i++;
            if (i >= sline && i<= eline)
            {
                result[n] = str;
                n++;
            }
        }
        br.close();
        return result;        
    }
    
    
    public int getChangeCountInClone (int cid, int rev, int cloneType)
    {
        try
        {
            String [] diff12  = new String[5000];

            String [] strings1 = readClone (cid, rev, cloneType);
            String [] strings2 = readClone (cid, rev+1, cloneType);
            
            if (compareStrings(strings1, strings2) == true)
            {
                return 0;
            }

            int diffcount = 0;
            if (writeClone(cid, rev,cloneType, tempfile1) && writeClone (cid, rev+1,cloneType, tempfile2))
            {
                diffcount = getFileDiffCount (tempfile1, tempfile2);
                //diff12 = getFileDiff(tempfile1, tempfile2);
            }
            
            
            /*int cc = 0;
            for (int i =0;diff12[i] != null;i++ )
            {
                String line = diff12[i];
                if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}
                cc++;
            }*/
            return diffcount;
        }
        catch (Exception e)
        {
            
        }
        
        return 0;
    }
    
    public Difference getCloneDiff (int cid, int rev, int cloneType)
    {
        try
        {
            String [] file1String = new String[5000];
            String [] file2String = new String[5000];
            String [] diff12  = new String[5000];


            writeClone(cid, rev,cloneType, tempfile1);
            file1String = getCloneWithLineNumber(cid, rev, tempfile1);

            writeClone (cid, rev+1, cloneType, tempfile2);
            file2String = getCloneWithLineNumber(cid, rev+1, tempfile2);

            diff12 = getFileDiff(tempfile1, tempfile2);
            String compare1 = compareFiles1(rev, rev+1, diff12, file1String, file2String );
            
            Difference diff = new Difference ();
            if (diff12[0] == null)
            {
                diff.hasDiff = false;
            }
            else
            {
                diff.hasDiff = true;
            }
            diff.compare = compare1;



            return diff;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = analyzeHistory. "+e);
        }
        
        return null;        
    }
    
    
     
    public Difference getMethodDiff (int m, int rev)
    {        
        try
        {
            Difference diff = new Difference ();
            
            String [] file1String = new String[5000];
            String [] file2String = new String[5000];
            String [] diff12  = new String[5000];


            writeMethod(m, rev, tempfile1);
            file1String = getMethodWithLineNumber(m, rev, 0, tempfile1);

            writeMethod (m, rev+1, tempfile2);
            file2String = getMethodWithLineNumber(m, rev+1, 0, tempfile2);

            diff12 = getFileDiff(tempfile1, tempfile2);
            String compare1 = compareFiles1(rev, rev+1, diff12, file1String, file2String );
            
            if (diff12[0] == null)
            {
                diff.hasDiff = false;
            }
            else
            {
                diff.hasDiff = true;                       
            }
            diff.compare = compare1;

            return diff;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = analyzeHistory. "+e);
        }
        
        return null;
    }
    
    
    
    
    public void analyzeHistory1 (int m1, int m2, int rev, Diff d)
    {
        
        try
        {
        String [] file1String = new String[5000];
        String [] file1StringClones = new String[5000];
        String [] file2String = new String[5000];
        String [] diff12  = new String[5000];

        String [] file3String = new String[5000];
        String [] file3StringClones =  new String[5000];
        String [] file4String = new String[5000];
        String [] diff34 = new String [5000];


        writeMethod(m1, rev, tempfile1);
        file1String = getMethodWithLineNumber(m1, rev, 0, tempfile1);

        writeMethod (m1, rev+1, tempfile2);
        file2String = getMethodWithLineNumber(m1, rev+1, 0, tempfile2);

        diff12 = getFileDiff(tempfile1, tempfile2);
        if (diff12[0] == null)
        {
            d.gotDiff = false;
        }

        writeMethod(m2, rev, tempfile3);
        file3String = getMethodWithLineNumber(m2, rev, 0, tempfile3);

        writeMethod (m2, rev+1, tempfile4);
        file4String = getMethodWithLineNumber(m2, rev+1, 0, tempfile4);

        diff34 = getFileDiff (tempfile3, tempfile4);
        
        if (diff34[0] == null)
        {
            d.gotDiff = false;
        }        

        String [] clonediff = new String[5000];
        clonediff = getFileDiff (tempfile1, tempfile3);

        String compare1 = compareFiles1(rev, rev+1, diff12, file1String, file2String );
        jLabel6.setText (compare1);
        d.diff1 = compare1;

        String compare2 = compareFiles1(rev, rev+1, diff34, file3String, file4String );
        jLabel5.setText (compare2);
        d.diff2 = compare2;

        if (cloneType.trim().length() > 0)
        {
            file1StringClones = getMethodWithLineNumber(m1, rev,1, tempfile1);
            file3StringClones = getMethodWithLineNumber(m2, rev,1, tempfile3);
        }
        else
        {
            file1StringClones = getMethodWithLineNumber(m1, rev,0, tempfile1);
            file3StringClones = getMethodWithLineNumber(m2, rev,0, tempfile3);
        }

        String compare3 = compareFiles1(rev, rev, clonediff, file1StringClones, file3StringClones);
        jLabel9.setText (compare3);                           
        
        }
        catch (Exception e)
        {
            System.out.println ("error in method = analyzeHistory. "+e);
        }
    }
    

    public void analyzeHistory (int m1, int m2, int rev)
    {
        try
        {
        String [] file1String = new String[5000];
        String [] file1StringClones = new String[5000];
        String [] file2String = new String[5000];
        String [] diff12  = new String[5000];

        String [] file3String = new String[5000];
        String [] file3StringClones =  new String[5000];
        String [] file4String = new String[5000];
        String [] diff34 = new String [5000];


        writeMethod(m1, rev, tempfile1);
        file1String = getMethodWithLineNumber(m1, rev, 0, tempfile1);

        writeMethod (m1, rev+1, tempfile2);
        file2String = getMethodWithLineNumber(m1, rev+1, 0, tempfile2);

        diff12 = getFileDiff(tempfile1, tempfile2);

        writeMethod(m2, rev, tempfile3);
        file3String = getMethodWithLineNumber(m2, rev, 0, tempfile4);

        writeMethod (m2, rev+1, tempfile3);
        file4String = getMethodWithLineNumber(m2, rev+1, 0, tempfile4);

        diff34 = getFileDiff (tempfile3, tempfile4);

        String [] clonediff = new String[5000];
        clonediff = getFileDiff (tempfile1, tempfile3);

        String compare1 = compareFiles(rev, rev+1, diff12, file1String, file2String );
        jLabel6.setText (compare1);

        String compare2 = compareFiles(rev, rev+1, diff34, file3String, file4String );
        jLabel5.setText (compare2);

        if (cloneType.trim().length() > 0)
        {
            file1StringClones = getMethodWithLineNumber(m1, rev,1, tempfile1);
            file3StringClones = getMethodWithLineNumber(m2, rev,1, tempfile3);
        }
        else
        {
            file1StringClones = getMethodWithLineNumber(m1, rev,0, tempfile1);
            file3StringClones = getMethodWithLineNumber(m2, rev,0, tempfile3);
        }

        String compare3 = compareFiles(rev, rev, clonediff, file1StringClones, file3StringClones);
        jLabel9.setText (compare3);                          
        
        }
        catch (Exception e)
        {
            System.out.println ("error in method = analyzeHistory. "+e);
        }
    }


    
    /*public void analyzeHistory1(int m1, int m2, int rev)
    {
        // TODO code application logic here

        String [] fileString1 = new String[5000];
        int lineCount1 = 0;
        String [] fileString2 = new String[5000];
        int lineCount2 = 0;

        String [] diff12  = new String[5000];
        int lineCount12 = 0;

        String [] fileString3 = new String[5000];
        int lineCount3 = 0;
        String [] fileString4 = new String[5000];
        int lineCount4 = 0;

        String [] diff34 = new String [5000];
        int lineCount34 = 0;

        try
        {
            BufferedReader br;
            BufferedWriter br1;
            BufferedReader in;
            Process p;

            Database dbase = new Database ();
            int version=139, methodid = 0, sl = 0, el=0, methodid2 = 0;
            String filename = "", str = "";
            int sline = 132, eline = 236, i=0;
            int vp =1;
            String commonFilePath = "";


            //reading file1.
            //dbase.connectionString = "jdbc:mysql://localhost:3306/lozano_imprint_"+systemName+"_nicad";
            commonFilePath = commonParameters.repositoryURL + "/version-";
            version = rev  ;
            methodid = m1;
            methodid2 = m2 ;
            
            
            vp =1 ;
            i=0;

            dbase.connect ();
            dbase.executeQuery("select filename, startinglinenumber, endinglinenumber from method where version = '"+version+"' and globalmethodid = '"+methodid+"'");
            dbase.result.next();
            filename = dbase.result.getString ("filename");
            sline = Integer.parseInt(dbase.result.getString("startinglinenumber"));
            eline = Integer.parseInt(dbase.result.getString("endinglinenumber"));
            dbase.disconnect ();

            filename = commonFilePath+version+"/" + filename;

            br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
            //br = new BufferedReader(new InputStreamReader (new FileInputStream ("file11.txt")));
            br1 = new BufferedWriter (new FileWriter ("file1.txt"));
            fileString1[lineCount1] = (lineCount1+1)+" "+" ";
            lineCount1++;
            while ((str = br.readLine())!= null)
            {
                i++;
                if (i >= sline && i<= eline)
                {
                    br1.write("\n"+str);
                    //method1Previous = method1Previous + "\n"+str;
                    fileString1[lineCount1] = (lineCount1+1)+" "+str;
                    lineCount1++;
                    //System.out.println(str) ;
                }
            }
            br1.close();
            br.close();
            System.out.println ("file 1 written.");

            //reading file2
            //methodid = 0;
            i=0;


            dbase.connect ();
            dbase.executeQuery("select filename, startinglinenumber, endinglinenumber from method where version = '"+(version+vp)+"' and globalmethodid = '"+methodid+"'");
            dbase.result.next();
            filename = dbase.result.getString ("filename");
            sline = Integer.parseInt(dbase.result.getString("startinglinenumber"));
            eline = Integer.parseInt(dbase.result.getString("endinglinenumber"));
            dbase.disconnect ();

            filename = commonFilePath+(version+1)+"/" + filename;

            br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
            //br = new BufferedReader(new InputStreamReader (new FileInputStream ("file21.txt")));
            br1 = new BufferedWriter (new FileWriter ("file2.txt"));
            fileString2[lineCount2] = (lineCount2+1)+" "+" ";
            lineCount2++;
            while ((str = br.readLine())!= null)
            {
                i++;
                if (i >= sline && i<= eline)
                {
                    br1.write("\n"+str);
                    
                    fileString2[lineCount2] = (lineCount2+1)+" "+str;
                    lineCount2++;
                }
            }
            br1.close();
            br.close();

            System.out.println ("file 2 written.");


            //reading file3
            methodid = methodid2;
            i=0;


            dbase.connect ();
            dbase.executeQuery("select filename, startinglinenumber, endinglinenumber from method where version = '"+version+"' and globalmethodid = '"+methodid+"'");
            dbase.result.next();
            filename = dbase.result.getString ("filename");
            sline = Integer.parseInt(dbase.result.getString("startinglinenumber"));
            eline = Integer.parseInt(dbase.result.getString("endinglinenumber"));
            dbase.disconnect ();

            filename = commonFilePath+version+"/" + filename;

            br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
            br1 = new BufferedWriter (new FileWriter ("file3.txt"));
            fileString3[lineCount3] = (lineCount3+1)+" "+" ";
            lineCount3++;
            while ((str = br.readLine())!= null)
            {
                i++;
                if (i >= sline && i<= eline)
                {
                    br1.write("\n"+str);
                    //System.out.println(str) ;
                    //method2Previous = method2Previous + "\n"+str;

                    fileString3[lineCount3] = (lineCount3+1)+" "+str;
                    lineCount3++;
                }
            }
            br1.close();
            br.close();

            System.out.println ("file 3 written.");


            //reading file4
            //methodid = 0;
            i=0;


            dbase.connect ();
            dbase.executeQuery("select filename, startinglinenumber, endinglinenumber from method where version = '"+(version+vp)+"' and globalmethodid = '"+methodid+"'");
            dbase.result.next();
            filename = dbase.result.getString ("filename");
            sline = Integer.parseInt(dbase.result.getString("startinglinenumber"));
            eline = Integer.parseInt(dbase.result.getString("endinglinenumber"));
            dbase.disconnect ();

            filename = commonFilePath+(version+1)+"/" + filename;

            br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
            br1 = new BufferedWriter (new FileWriter ("file4.txt"));
            fileString4[lineCount4] = (lineCount4+1)+" "+" ";
            lineCount4++;
            while ((str = br.readLine())!= null)
            {
                i++;
                if (i >= sline && i<= eline)
                {
                    br1.write("\n"+str);
                    //System.out.println(str) ;
                    //method2Post = method2Post + "\n"+str;

                    fileString4[lineCount4] = (lineCount4+1)+" "+str;
                    lineCount4++;

                }
            }
            br1.close();
            br.close();

            System.out.println ("file 4 written.");


            //comparing file1 and file2

            p = Runtime.getRuntime().exec("c:/tools/diffwin/bin/diff file1.txt file2.txt");
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            //br1 = new BufferedWriter (new FileWriter ("compare1.txt"));
            while ((line = in.readLine()) != null) {
                //br1.write ("\n"+line);
                //System.out.println(line);
                //method1Diff = method1Diff + "\n"+line;
                diff12[lineCount12] = line;
                lineCount12++;
            }
            //br1.close();
            in.close();

            //System.out.println ("comparison 1 written.");
            
            System.out.println ("*********************************************************************************************************************.");


            //comparing file3 and file4

            p = Runtime.getRuntime().exec("c:/tools/diffwin/bin/diff file3.txt file4.txt");
            in = new BufferedReader( new InputStreamReader(p.getInputStream()));
            //br1 = new BufferedWriter (new FileWriter ("compare2.txt"));
            line = null;
            while ((line = in.readLine()) != null) {
                //br1.write("\n"+line);
                //System.out.println(line);
                //method2Diff = method2Diff + "\n"+line;
                diff34[lineCount34] = line;
                lineCount34++;
            }
            //br1.close();
            in.close();

            System.out.println ("comparison 2 written.");


        }
        catch (Exception e)
        {
            System.out.println ("error: "+e) ;
        }
    }*/
    

    public String compareFiles (int revision1, int revision2,  String [] diff12, String [] fileString1, String [] fileString2)
    {
        String line = "";

        //this is only for coloring.
        for (int i1 =0;diff12[i1] != null;i1++ )
        {
            line = diff12[i1];
            if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}
            int oldline1 = -1;
            int oldline2 = -1;
            int newline1 = -1;
            int newline2 = -1;

            String oldline = line.split("[a|c|d]+")[0];
            String newline = line.split("[a|c|d]+")[1];

            oldline1 = Integer.parseInt (oldline.split("[,]+")[0]);
            oldline2 = oldline1;
            if (oldline.contains(","))
            {
                oldline2 = Integer.parseInt (oldline.split("[,]+")[1]);
            }


            newline1 = Integer.parseInt (newline.split("[,]+")[0]);
            newline2 = newline1;
            if (newline.contains(","))
            {
                newline2 = Integer.parseInt (newline.split("[,]+")[1]);
            }

            if (line.contains("a") || line.contains("c"))
            {
                for (int c=newline1;c<=newline2;c++)
                {
                    String part1 = fileString2[c-1].split("[ ]+")[0];
                    int len = part1.length();
                    String part2 = fileString2[c-1].substring(len);

                    if (line.contains("a"))
                    {
                        fileString2[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                    else
                    {
                        fileString2[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                }
            }
            if (line.contains("d") || line.contains("c"))
            {
                for (int c=oldline1;c<=oldline2;c++)
                {
                    String part1 = fileString1[c-1].split("[ ]+")[0];
                    int len = part1.length();
                    String part2 = fileString1[c-1].substring(len);

                    if (line.contains("d"))
                    {
                        fileString1[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                    else
                    {
                        fileString1[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                }
            }

        }

        for (int i1 =0;diff12[i1] != null;i1++ )
        {
            line = diff12[i1];
            if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}

            int oldline1 = -1;
            int oldline2 = -1;
            int newline1 = -1;
            int newline2 = -1;

            String oldline = line.split("[a|c|d]+")[0];
            String newline = line.split("[a|c|d]+")[1];

            oldline1 = Integer.parseInt (oldline.split("[,]+")[0]);
            oldline2 = oldline1;
            if (oldline.contains(","))
            {
                oldline2 = Integer.parseInt (oldline.split("[,]+")[1]);
            }


            newline1 = Integer.parseInt (newline.split("[,]+")[0]);
            newline2 = newline1;
            if (newline.contains(","))
            {
                newline2 = Integer.parseInt (newline.split("[,]+")[1]);
            }

            
            int oldlines = oldline2 - oldline1 + 1;
            int newlines = newline2 - newline1 + 1;

            if (newlines > oldlines || line.contains ("a")) // we need space in oldfile.
            {
                int cnt = newlines - oldlines;
                if (line.contains ("a")){ cnt = cnt + 1; }
                int oldln = 0;
                for (int l =0; fileString1[l] != null;l++)
                {
                    if (fileString1[l].trim().length() > 0)
                    {
                        if (Integer.parseInt(fileString1[l].split("[ ]+")[0]) == oldline2)
                        {
                            oldln = l;
                            break;
                        }
                    }
                }

                int lineCount1 = 0;
                while (fileString1[lineCount1] != null)
                {
                    lineCount1++;
                }

                for (int l = lineCount1-1;l>=oldln+1;l--)
                {
                    fileString1[l+cnt] = fileString1[l];
                    fileString1[l] = "";
                }
                lineCount1 = lineCount1 + cnt;
            }
            if (oldlines > newlines || line.contains ("d"))
            {
                int cnt = oldlines -  newlines;
                if (line.contains ("d")){ cnt = cnt + 1; }
                int newln = 0;

                for (int l =0;fileString2[l] != null;l++)
                {
                    if (fileString2[l].trim().length()>0)
                    {
                        if (Integer.parseInt(fileString2[l].split("[ ]+")[0]) == newline2)
                        {
                            newln = l;
                            break;
                        }
                    }
                }

                int lineCount2 = 0;
                while (fileString2[lineCount2] != null)
                {
                    lineCount2++;
                }

                for (int l =lineCount2-1;l>=newln+1;l--)
                {
                    fileString2[l+cnt] = fileString2[l];
                    fileString2[l] = "";
                }
                lineCount2 = lineCount2+cnt;
            }
            
        }


        String tableText  = "<table cellspacing='0' cellpadding='0' width='100%'>";

        /*tableText += "<tr>";
        tableText += "<td colspan='2'>"+"Revision = "+revision1+" </td>";
        tableText += "<td colspan='2'>"+"Revision = " + revision2+" </td>";
        tableText += "</tr>";*/

        for (int n = 0;!(fileString1[n] == null && fileString2[n] == null);n++)
        {
            String part1="", part2="", part3="", part4 ="";
            int len =0;
            if (fileString1[n] != null)
            {
                if (fileString1[n].trim().length() > 0)
                {
                    part1 = fileString1[n].split("[ ]+")[0];
                    len = part1.length();
                    part2 = fileString1[n].substring(len);
                }
            }

            if (fileString2[n] != null)
            {
                if (fileString2[n].trim().length() > 0)
                {
                    part3 = fileString2[n].split("[ ]+")[0];
                    len = part3.length();
                    part4 = fileString2[n].substring(len);
                }
            }


            tableText += "<tr>";
            tableText += "<td align='right' valign='top' style='width: 20px; background-color:#e9e8e2; padding-right: 5px; padding-bottom: 5px;padding-top: 5px;'>"+part1+"</td>";
            if (part2.trim().split("[ ]+")[0].contains("font") || part4.trim().split("[ ]+")[0].contains("font"))
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#b1cdec;padding-bottom: 5px;padding-top: 5px;'>"+part2+"</td>";
            }
            else
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#ffffff;padding-bottom: 5px;padding-top: 5px;'>"+part2+"</td>";
            }
            tableText += "<td align='right' valign='top' style='width: 20px; background-color:#e9e8e2; padding-right: 5px;padding-bottom: 5px;padding-top: 5px;'>"+part3+"</td>";
            if (part2.trim().split("[ ]+")[0].contains("font") || part4.trim().split("[ ]+")[0].contains("font"))
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#b1cdec;padding-bottom: 5px;padding-top: 5px;'>"+part4+"</td>";
            }
            else
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#ffffff;padding-bottom: 5px;padding-top: 5px;'>"+part4+"</td>";
            }
            tableText += "</tr>";
        }
        tableText += "</table>";
        tableText = "<html>" + tableText + "</html>";

        return tableText;
    }
    
    
    public String [][] compareClones (String clonefile1, int clonestartline1, int cloneendline1, String clonefile2, int clonestartline2, int cloneendline2)
    {
        try
        {
            writeFile(clonefile1, clonestartline1, cloneendline1, tempfile1);
            writeFile(clonefile2, clonestartline2, cloneendline2, tempfile2);
            
            String compare[][] = compareFiles (tempfile1, tempfile2);
            return compare;            
        }
        catch (Exception e)
        {
            
        }
        String [][] temp = new String [1][1];
        return temp;        
    }
    
    
    public String [][] compareMethods (SingleMethod previousmethod, SingleMethod currentmethod, int previousrevision, int currentrevision)
    {
        try
        {
            String pfilepath = commonParameters.subject_system+"/repository/version-"+previousrevision+"/" + previousmethod.filepath;
            int pstartline = Integer.parseInt(previousmethod.startline);
            int pendline = Integer.parseInt(previousmethod.endline);
            
            String cfilepath = commonParameters.subject_system+"/repository/version-"+currentrevision+"/" + currentmethod.filepath;
            int cstartline = Integer.parseInt(currentmethod.startline);
            int cendline = Integer.parseInt(currentmethod.endline);
            
            writeFile(pfilepath, pstartline, pendline, tempfile1);
            writeFile(cfilepath, cstartline, cendline, tempfile2);
                    
            String compare[][] = compareFiles (tempfile1, tempfile2);
            return compare;
        }
        catch (Exception e)
        {
            
        }
        String [][] temp = new String [1][1];
        return temp;        
    }
    
    public String [][] compareMethods (String previousmethod, String currentmethod, int previousrevision, int currentrevision)
    {
        try
        {
            String pfilepath = commonParameters.subject_system+"/repository/version-"+previousrevision+"/" + previousmethod.split("[,]+")[0].trim();
            int pstartline = Integer.parseInt(previousmethod.split("[,]+")[1].trim());
            int pendline = Integer.parseInt(previousmethod.split("[,]+")[2].trim());
            
            String cfilepath = commonParameters.subject_system+"/repository/version-"+currentrevision+"/" + currentmethod.split("[,]+")[0].trim();
            int cstartline = Integer.parseInt(currentmethod.split("[,]+")[1].trim());
            int cendline = Integer.parseInt(currentmethod.split("[,]+")[2].trim());
            
            writeFile(pfilepath, pstartline, pendline, tempfile1);
            writeFile(cfilepath, cstartline, cendline, tempfile2);
                    
            String compare[][] = compareFiles (tempfile1, tempfile2);
            return compare;
        }
        catch (Exception e)
        {
            
        }
        String [][] temp = new String [1][1];
        return temp;
    }
    
    public String [] readFile (String file)
    {
        String [] fileString = new String[10000];
        int n =0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (file)));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                fileString[n] = str;
                n++;
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = readFile (String file). "+e);
        }
        
        return fileString;
    }
    
    
    //it determines the count of changes in file 1.
    //if lines are added before the beginning or after the changing then these will not be considered as changes.
    public int getFileDiffCount (String file1, String file2)
    {
        String [] diff12 = getFileDiff(file1, file2);
        String [] fileString1 = readFile (file1);
        String [] fileString2 = readFile (file2);
        
        int diffcount = 0;
        
        
        //get number of lines in file1.
        int n = 0; 
        while (fileString1[n] != null)
        {
            n++;
        }
        //n is the line number of file1.
        
        
        
        //[][0] = line number from file1.
        //[][1] = line from file1.
        //[][2] = line number from file2.
        //[][3] = line from file2.        
        String line = "";
        
        
        for (int i =0;diff12[i] != null;i++ )
        {
            line = diff12[i];
            if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}
            int oldline1 = -1;
            int oldline2 = -1;
            int newline1 = -1;
            int newline2 = -1;
            
            if (line.contains ("0a")){continue;}
            if (line.contains (""+n+"a")) {continue;}                        
            
            diffcount++;
        }
        
        return diffcount;
    }
    
    
    
    public String [][] compareFiles (String file1, String file2)
    {
        String [] diff12 = getFileDiff(file1, file2);
        String [] fileString1 = readFile (file1);
        String [] fileString2 = readFile (file2);
        
        
        //[][0] = line number from file1.
        //[][1] = line from file1.
        //[][2] = line number from file2.
        //[][3] = line from file2.        
        String [][] compare = new String [10000][4];
        
        
        String [][] compare1 = new String[10000][2];
        String [][] compare2 = new String[10000][2];
        int n1 = 0, n2=0;
        String line = "";
        
        
        int c1=0, c2=0;
        
        for (int i =0;diff12[i] != null;i++ )
        {
            line = diff12[i];
            if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}
            int oldline1 = -1;
            int oldline2 = -1;
            int newline1 = -1;
            int newline2 = -1;

            String oldline = line.split("[a|c|d]+")[0];
            String newline = line.split("[a|c|d]+")[1];

            oldline1 = Integer.parseInt (oldline.split("[,]+")[0]);
            oldline2 = oldline1;
            if (oldline.contains(","))
            {
                oldline2 = Integer.parseInt (oldline.split("[,]+")[1]);
            }


            newline1 = Integer.parseInt (newline.split("[,]+")[0]);
            newline2 = newline1;
            if (newline.contains(","))
            {
                newline2 = Integer.parseInt (newline.split("[,]+")[1]);
            }
            
            oldline1 = oldline1 - 1;
            oldline2 = oldline2 - 1;
            newline1 = newline1 - 1;
            newline2 = newline2 - 1;
            
            //append case.
            if (line.contains("a"))
            {
                //taking care of file1.
                for (int j=c1;j<=oldline1;j++)
                {
                    compare1[n1][0] = j+"";
                    compare1[n1][1] = fileString1[j];
                    n1++;
                }
                for (int j=newline1;j<=newline2;j++)
                {
                    compare1[n1][0] = "";
                    compare1[n1][1] = "";
                    n1++;
                }
                c1 = oldline1+1;
                
                //taking care of file2.
                for (int j=c2;j<=newline2;j++)
                {
                    compare2[n2][0] = j+"";
                    compare2[n2][1] = fileString2[j];
                    n2++;
                }
                c2 = newline2+1;
            }
            
            //delete case
            if (line.contains("d"))
            {
                //taking care of file1.
                for (int j=c1;j<=oldline2;j++)
                {
                    compare1[n1][0] = j+"";
                    compare1[n1][1] = fileString1[j];
                    n1++;
                }
                c1 = oldline2+1;
                
                //taking care of file2.
                for (int j=c2;j<=newline1;j++)
                {
                    compare2[n2][0] = j+"";
                    compare2[n2][1] = fileString2[j];
                    n2++;
                }
                for (int j=oldline1;j<=oldline2;j++)
                {
                    compare2[n2][0] = "";
                    compare2[n2][1] = "";
                    n2++;
                }
                c2 = newline1+1;
            }
            
            //change case.
            if (line.contains ("c"))
            {
                //taking care of file1.
                for (int j=c1;j<=oldline2;j++)
                {
                    compare1[n1][0] = j+"";
                    compare1[n1][1] = fileString1[j];
                    n1++;
                }
                c1 = oldline2+1;
                
                //taking care of file2.
                for (int j=c2;j<=newline2;j++)
                {
                    compare2[n2][0] = j+"";
                    compare2[n2][1] = fileString2[j];
                    n2++;
                }
                c2 = newline2+1;
                
                //taking of white spaces.
                int l1 = oldline2-oldline1+1;
                int l2 = newline2-newline1+1;
                                               
                if (l1>l2)
                {
                    for (int j=0;j<l1-l2;j++)
                    {
                        compare2[n2][0] = "";
                        compare2[n2][1] = "";
                        n2++;
                    }
                }
                else
                {
                    for (int j=0;j<l2-l1;j++)
                    {
                        compare1[n1][0] = "";
                        compare1[n1][1] = "";
                        n1++;
                    }
                }                
            }                        
        }
        
        for (int j=c1;fileString1[j] != null;j++)
        {
            compare1[n1][0] = j+"";
            compare1[n1][1] = fileString1[j];
            n1++;
        }
        
        for (int j=c2;fileString2[j] != null;j++)
        {
            compare2[n2][0] = j+"";
            compare2[n2][1] = fileString2[j];
            n2++;
        }
        
        for (int i =0;i<n1;i++)
        {
            compare[i][0] = compare1[i][0];
            compare[i][1] = compare1[i][1];
            compare[i][2] = compare2[i][0];
            compare[i][3] = compare2[i][1];            
        }
        
        
        return compare;
    }

    public String compareFiles1 (int revision1, int revision2,  String [] diff12, String [] fileString1, String [] fileString2)
    {
        String line = "";

        //this is only for coloring.
        for (int i1 =0;diff12[i1] != null;i1++ )
        {
            line = diff12[i1];
            if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}
            int oldline1 = -1;
            int oldline2 = -1;
            int newline1 = -1;
            int newline2 = -1;

            String oldline = line.split("[a|c|d]+")[0];
            String newline = line.split("[a|c|d]+")[1];

            oldline1 = Integer.parseInt (oldline.split("[,]+")[0]);
            oldline2 = oldline1;
            if (oldline.contains(","))
            {
                oldline2 = Integer.parseInt (oldline.split("[,]+")[1]);
            }


            newline1 = Integer.parseInt (newline.split("[,]+")[0]);
            newline2 = newline1;
            if (newline.contains(","))
            {
                newline2 = Integer.parseInt (newline.split("[,]+")[1]);
            }

            if (line.contains("a") || line.contains("c"))
            {
                for (int c=newline1;c<=newline2;c++)
                {
                    String part1 = fileString2[c-1].split("[ ]+")[0];
                    int len = part1.length();
                    String part2 = fileString2[c-1].substring(len);

                    if (line.contains("a"))
                    {
                        fileString2[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                    else
                    {
                        fileString2[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                }
            }
            if (line.contains("d") || line.contains("c"))
            {
                for (int c=oldline1;c<=oldline2;c++)
                {
                    String part1 = fileString1[c-1].split("[ ]+")[0];
                    int len = part1.length();
                    String part2 = fileString1[c-1].substring(len);

                    if (line.contains("d"))
                    {
                        fileString1[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                    else
                    {
                        fileString1[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                }
            }

        }

        for (int i1 =0;diff12[i1] != null;i1++ )
        {
            line = diff12[i1];
            if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}

            int oldline1 = -1;
            int oldline2 = -1;
            int newline1 = -1;
            int newline2 = -1;

            String oldline = line.split("[a|c|d]+")[0];
            String newline = line.split("[a|c|d]+")[1];

            oldline1 = Integer.parseInt (oldline.split("[,]+")[0]);
            oldline2 = oldline1;
            if (oldline.contains(","))
            {
                oldline2 = Integer.parseInt (oldline.split("[,]+")[1]);
            }


            newline1 = Integer.parseInt (newline.split("[,]+")[0]);
            newline2 = newline1;
            if (newline.contains(","))
            {
                newline2 = Integer.parseInt (newline.split("[,]+")[1]);
            }

            
            int oldlines = oldline2 - oldline1 + 1;
            int newlines = newline2 - newline1 + 1;

            if (newlines > oldlines || line.contains ("a")) // we need space in oldfile.
            {
                int cnt = newlines - oldlines;
                if (line.contains ("a")){ cnt = cnt + 1; }
                int oldln = 0;
                for (int l =0; fileString1[l] != null;l++)
                {
                    if (fileString1[l].trim().length() > 0)
                    {
                        if (Integer.parseInt(fileString1[l].split("[ ]+")[0]) == oldline2)
                        {
                            oldln = l;
                            break;
                        }
                    }
                }

                int lineCount1 = 0;
                while (fileString1[lineCount1] != null)
                {
                    lineCount1++;
                }

                for (int l = lineCount1-1;l>=oldln+1;l--)
                {
                    fileString1[l+cnt] = fileString1[l];
                    fileString1[l] = "";
                }
                lineCount1 = lineCount1 + cnt;
            }
            if (oldlines > newlines || line.contains ("d"))
            {
                int cnt = oldlines -  newlines;
                if (line.contains ("d")){ cnt = cnt + 1; }
                int newln = 0;

                for (int l =0;fileString2[l] != null;l++)
                {
                    if (fileString2[l].trim().length()>0)
                    {
                        if (Integer.parseInt(fileString2[l].split("[ ]+")[0]) == newline2)
                        {
                            newln = l;
                            break;
                        }
                    }
                }

                int lineCount2 = 0;
                while (fileString2[lineCount2] != null)
                {
                    lineCount2++;
                }

                for (int l =lineCount2-1;l>=newln+1;l--)
                {
                    fileString2[l+cnt] = fileString2[l];
                    fileString2[l] = "";
                }
                lineCount2 = lineCount2+cnt;
            }
            
        }


        String tableText  = "<table cellspacing='0' cellpadding='0' width='100%'>";

        /*tableText += "<tr>";
        tableText += "<td colspan='2'>"+"Revision = "+revision1+" </td>";
        tableText += "<td colspan='2'>"+"Revision = " + revision2+" </td>";
        tableText += "</tr>";*/

        for (int n = 0;!(fileString1[n] == null && fileString2[n] == null);n++)
        {
            String part1="", part2="", part3="", part4 ="";
            int len =0;
            if (fileString1[n] != null)
            {
                if (fileString1[n].trim().length() > 0)
                {
                    part1 = fileString1[n].split("[ ]+")[0];
                    len = part1.length();
                    part2 = fileString1[n].substring(len);
                }
            }

            if (fileString2[n] != null)
            {
                if (fileString2[n].trim().length() > 0)
                {
                    part3 = fileString2[n].split("[ ]+")[0];
                    len = part3.length();
                    part4 = fileString2[n].substring(len);
                }
            }


            tableText += "<tr>";
            tableText += "<td align='right' valign='top' style='width: 20px; background-color:#e9e8e2; padding-right: 5px; padding-bottom: 5px;padding-top: 5px;'>"+part1+"</td>";
            if (part2.trim().split("[ ]+")[0].contains("font") || part4.trim().split("[ ]+")[0].contains("font"))
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#b1cdec;padding-bottom: 5px;padding-top: 5px;'>"+part2+"</td>";
            }
            else
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#ffffff;padding-bottom: 5px;padding-top: 5px;'>"+part2+"</td>";
            }
            tableText += "<td align='right' valign='top' style='width: 20px; background-color:#e9e8e2; padding-right: 5px;padding-bottom: 5px;padding-top: 5px;'>"+part3+"</td>";
            if (part2.trim().split("[ ]+")[0].contains("font") || part4.trim().split("[ ]+")[0].contains("font"))
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#b1cdec;padding-bottom: 5px;padding-top: 5px;'>"+part4+"</td>";
            }
            else
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#ffffff;padding-bottom: 5px;padding-top: 5px;'>"+part4+"</td>";
            }
            tableText += "</tr>";
        }
        tableText += "</table>";

        return tableText;
    }
    
    public void setValues ()
    {
        //jLabel16.setText (ruleHistory);    
        
        
        /*DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0);
        
        String [] row = new String[2];
        row[0]= "<html><b>Method ID</b> = "+ method1+"</html>"; row[1] = "<html><b>Method ID</b> = "+ method2+"</html>";
        model.addRow(row);
        
        row[0]= "<html><b>Method Name</b> = "+ methodName1+"</html>"; row[1] = "<html><b>Method Name</b> = "+ methodName2+"</html>";
        model.addRow(row);
        
        row[0]= "<html><b>File Path</b> = "+ filePath1+"</html>"; row[1] = "<html><b>File Path</b> = "+ filePath2+"</html>";
        model.addRow(row);
        
        row[0]= "<html><b>Changed in Commits</b> = "+ commitList1+"</html>"; row[1] = "<html><b>Changed in Commits</b> = "+ commitList2+"</html>";
        model.addRow(row);
        
        
        DefaultTableModel model1 = (DefaultTableModel) jTable4.getModel();
        model1.setRowCount(0); 
        String [] row1 = new String[1];
        
        row1[0] = cloneInformation;
        model1.addRow(row1);
        
        row1[0] = locationInformation;
        model1.addRow(row1);        
        
        row1[0] = commitList;
        model1.addRow(row1); */
        
        
        jLabel11.setText ("Method Co-change Details ( Current Commit = "+currentCommit+" )");

        jLabel8.setText ("<html><b>Antecedent, Revision = "+currentCommit+"</b></html>");
        jLabel12.setText ("<html><b>Antecedent, Revision = "+(currentCommit+1)+"</b></html>");

        jLabel3.setText ("<html><b>Consequent, Revision = "+currentCommit+"</b></html>");
        jLabel4.setText ("<html><b>Consequent, Revision = "+(currentCommit+1)+"</b></html>");
        
        jLabel13.setText ("<html><b>Antecedent, Revision = "+currentCommit+"</b></html>");
        jLabel14.setText ("<html><b>Consequent, Revision = "+currentCommit+"</b></html>");
        
        
        analyzeHistory(Integer.parseInt(method1), Integer.parseInt(method2), currentCommit);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane6 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Rule History Analysis");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Important Information Regarding the Rule"
            }
        ));
        jScrollPane5.setViewportView(jTable4);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("jLabel15");
        jLabel15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("jLabel16");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Method Co-change History for the Rule");

        jScrollPane8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("jLabel8");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Change Analysis");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("jLabel12");

        jLabel6.setText("jLabel6");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane2.setViewportView(jLabel6);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("jLabel3");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Change Analysis");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane1.setViewportView(jLabel5);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("jLabel13");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Similarity Analysis");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("jLabel14");

        jLabel9.setText("jLabel9");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane4.setViewportView(jLabel9);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(142, 142, 142)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(411, 411, 411))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(112, 112, 112)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(105, 105, 105)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(360, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 419, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel12)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(323, Short.MAX_VALUE))
        );

        jScrollPane8.setViewportView(jPanel1);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setText("< Previous Commit");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Next Commit >");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 905, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(274, 274, 274)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(286, 286, 286))
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 905, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 908, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        analyzeRuleConsistingOfMethods(m1, m2);
        generateRevisionList ();
        setValues ();          
    }//GEN-LAST:event_formWindowOpened

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        if (currentIndex < cochangeList.split("[ ]+").length-1) {
            currentIndex++;
            currentCommit = Integer.parseInt(cochangeList.split("[ ]+")[currentIndex]);
            setValues();
        }
}//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        if (currentIndex > 0) {
            currentIndex--;
            currentCommit = Integer.parseInt(cochangeList.split("[ ]+")[currentIndex]);
            setValues();
        }
}//GEN-LAST:event_jButton2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChangeAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangeAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangeAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangeAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChangeAnalysis changeAnalysis = new ChangeAnalysis ();
                changeAnalysis.setVisible(true);
                changeAnalysis.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable4;
    // End of variables declaration//GEN-END:variables
}
