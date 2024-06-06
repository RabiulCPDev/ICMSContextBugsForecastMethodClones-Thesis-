/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;

/**
 *
 * @author mani
 */
public class CommonMethods {
    
    CommonParameters cp = new CommonParameters ();
    AttributeNames an = new AttributeNames ();
    
    
    
    public void applyBlindRenamingToFile (String filepath)
    {
        try
        {
            String [] result = new String [10000];
            int count = 0;
            
            String str = "";
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (filepath)));
            
            while ((str = br.readLine ())!= null)
            {
                if (str.trim().length() == 0)
                {
                    result[count] = str;
                }
                else
                {
                    result[count] = getBlindRenamedString(str);
                }
                count++;
            }
            
            BufferedWriter writer = new BufferedWriter (new FileWriter (filepath));
            for (int i =0;i<count;i++)
            {
                writer.write (result[i]+"\n");
            }
            writer.close ();            
        }
        catch (Exception e)
        {
            System.out.println ("error in method = applyBlindRenamingToFile. "+e);
        }
    }
    
    public String getBlindRenamedString (String statement)
    {
        String brStatement = "";
        
        int l = statement.trim().length();
        int hasBegan = 0;
        char ch = 'a';
        String id = "";
        for (int i =0;i<l;i++)
        {
            ch = statement.trim().charAt(i);
            if (hasBegan == 1)
            {
                if (isMiddleIDCharacter (ch))
                {
                    id += ch;
                }
                else
                {
                    if (isKeyword (id))
                    {
                        brStatement += id;
                    }
                    else
                    {
                        brStatement += "id";
                    }
                    brStatement += ch;
                    id = "";
                    hasBegan = 0;
                }
            }
            else
            {
                if (isFirstIDCharacter (ch))
                {
                    id += ch;
                    hasBegan = 1;
                }
                else
                {
                    brStatement += ch;
                }
            }
        }
        
        return brStatement;
    }
    
    public boolean isFirstIDCharacter (char ch)
    {
        if (Character.isLetter(ch) || ch == '_' || ch == '$') {return true;}
        return false;
    }
    
    public boolean isMiddleIDCharacter (char ch)
    {
        if (Character.isLetterOrDigit(ch) || ch == '_' || ch == '$') {return true;}
        return false;
    }
    
    public boolean isKeyword (String str)
    {
        String id = " " + str.trim()+" ";
        String keywords = " abstract assert "
                + " boolean break byte "
                + " case catch char class const continue "
                + " default do double "
                + " else enum extends "
                + " final finally float for "
                + " goto "
                + " if implements import instanceof int interface "
                + " long "
                + " native new "
                + " package private protected public "
                + " return "
                + " short static strictfp super switch synchronized "
                + " this throw throws transient try "
                + " void volatile "
                + " while ";
        
        if (!keywords.contains (id))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    
    public String getClonesInRevision (int revision, int cloneType)
    {
        String result ="";
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";

            while ((str = br.readLine())!=null)
            {
                if (str.trim().length() == 0){continue;}
                int id = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalCloneID));
                
                result = result + " " + id + " ";                
            }
            br.close();
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getClonesInRevision. "+e);
        }                
        return result;
    }
    
    
    //get clones.
    public String getClonesFromClass (int revision, int cloneclass, int cloneType)
    {
        String clones = "";
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
                       
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int classid = Integer.parseInt (cp.getAttributeValueFromString(str, an.cloneClassID));
                int cloneid = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                
                if (classid == cloneclass)
                {
                    clones = clones + " " + cloneid + " ";
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println ("error in getClonesFromClass. "+e);
        }        
        
        return clones;
    }
    

    // it returns space separated global clone ids.
    public String getChangedClones (int revision, int cloneType)
    {
        String result = ""; //0 = clone id, 1 = class id, 2 = change count.
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+"_change.txt")));
            String str = "";
                       
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int gcid = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                
                result = result + " " + gcid + " ";
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangedCloneFragments. "+e);
        }
        
        return result;
    }    
    
    public int getCloneClass (int revision, int gcid, int cloneType)
    {
        int result = -1;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (!str.contains(an.globalCloneID)){continue;}
                int id = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                int classid = Integer.parseInt (cp.getAttributeValueFromString(str, an.cloneClassID));
                
                if (gcid == id)
                {
                    result = classid;
                    break;
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println ("error in getCloneClassID. "+e);
        }
        
        return result;
    }
    
    
    public int getChangesToClone (int revision, int gcid, int cloneType)
    {
        int result = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+"_change.txt")));
            String str = "";
                                    
            while ((str = br.readLine())!=null)
            {
                if (str.trim().length() == 0){continue;}
                int id = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalCloneID));
                if (id == gcid)
                {
                    result = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneChangeCount));
                    break;
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangesToCloneFragment. "+e);
        }
                
        return result;
    }        

    public boolean writeClone (int cid, int rev, int cloneType, String file)
    {
        try
        {
            String commonFilePath =cp.repositoryURL+ "/version-"+rev;
            int version = rev  ;
            String str="";
            String filename = "";
            int sline, eline;


            String cloneString = cp.getCloneOfRevision (cid, version, cloneType);
            if (cloneString.trim().length() == 0) {return false;}
            filename = cp.getAttributeValueFromString(cloneString, an.cloneFilePath);
            sline = Integer.parseInt(cp.getAttributeValueFromString(cloneString, an.cloneStartingLine));
            eline = Integer.parseInt(cp.getAttributeValueFromString(cloneString, an.cloneEndingLine));
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
        catch (Exception e)
        {
            System.out.println ("error in method = writeClone. "+e);
        }
        return false;
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
    
    public String [] getFileDiff (String file1, String file2)
    {
        String [] diff = new String [5000];
        try
        {
            Process p = Runtime.getRuntime().exec(cp.windiffCommandPath + " "+file1+" "+file2+"");
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
    
    public String getCloneFile (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                if (cid == cloneID)
                {
                    String file = getAttributeValueFromString(str, an.cloneFilePath);
                    br.close();
                    return file;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneEndingLine (int cloneID, int revision). "+e);
        }
        
        return "";                
    }
    
    String getAttributeValueFromString (String str, String attributeName)
    {
        try
        {
            StringBuilder temp = new StringBuilder();
            int i1 = str.indexOf(attributeName, 0), i2=0;
            int i =0, l = str.length();
            for (i=i1;i<l;i++)
            {
                if (str.charAt(i) == '=')
                {
                    i2 = i+1;
                    break;
                }
            }            
            for (i = i2;i<l;i++)
            {
                if (str.charAt(i) == ':'){break;}
                temp.append(str.charAt(i));
            }
            String result = temp.toString();            
            
            return result.trim();
            
            /*int i2 = str.indexOf(separator.trim(), i1);
            if (i2 != -1)
            {
                temp = str.substring(i1, i2);
            }
            else
            {
                temp = str.substring(i1);
            }
            return temp.split("[=]+")[1].trim();*/
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getAttributeValueFromString. \nattribute name = "+attributeName+"\n input string = " + str + "\n" +e);
            return "";
        }        
    }
    
    public int isMethodClone (int cloneID, int revision, int cloneType)
    {        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                if (cid == cloneID)
                {
                    //get the container method id.
                    int gmid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                    int msline = getMethodStartingLine(gmid, revision);
                    int meline = getMethodEndingLine(gmid, revision);
                    int csline = getCloneStartingLine(cloneID, revision, cloneType);
                    int celine = getCloneEndingLine(cloneID, revision, cloneType);
                    
                    br.close();
                    
                    //if the difference in line count is one or zero.
                    //then the clone fragment is considered as a full method.
                    if (meline - msline - celine + csline < 2)
                    {                        
                        return 1;
                    }
                    else
                    {
                        return 0;
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneEndingLine (int cloneID, int revision). "+e);
        }
        
        return 0;
    }
    
    public int getMethodStartingLine (int methodID, int revision)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/methods/methods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                if (mid == methodID)
                {
                    int sline = Integer.parseInt(getAttributeValueFromString(str, an.startingLine));
                    br.close();
                    return sline;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodStartingLine (int methodID, int revision). "+e);
        }
        
        return -1;                
    }

    public int getMethodEndingLine (int methodID, int revision)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/methods/methods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                if (mid == methodID)
                {
                    int eline = Integer.parseInt(getAttributeValueFromString(str, an.endingLine));
                    br.close();
                    return eline;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodEndingLine (int methodID, int revision). "+e);
        }
        
        return -1;                
    }
    
    public int getCloneStartingLine (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                if (cid == cloneID)
                {
                    int sline = Integer.parseInt(getAttributeValueFromString(str, an.cloneStartingLine));
                    br.close();
                    return sline;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneStartingLine (int cloneID, int revision). "+e);
        }
        
        return -1;        
    }
    
    public int getCloneEndingLine (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                if (cid == cloneID)
                {
                    int eline = Integer.parseInt(getAttributeValueFromString(str, an.cloneEndingLine));
                    br.close();
                    return eline;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneEndingLine (int cloneID, int revision). "+e);
        }
        
        return -1;        
    } 
    
    public int getMaxGlobalCloneID (int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/maxglobalcloneid_type"+cloneType+".txt")));
            String str = br.readLine ().trim();
            int mcid = Integer.parseInt(str);
            br.close ();
            return mcid;
        }
        catch (Exception e)
        {
            System.out.println ("error in getMaxGlobalCloneID. "+e);
        }
        return 0;
    }
    
    public String getClonesInMethod (int gmid, int revision, int cloneType)
    {
        String clones = "";
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                
                if (mid == gmid)
                {
                    if (!clones.contains (" "+cid+" "))
                    {
                        clones += " " + cid + " ";
                    }
                }
            }
            br.close ();
            return clones;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneEndingLine (int cloneID, int revision). "+e);
        }
        
        return "";                
    }
    
    public int getContainerMethodID (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                if (cid == cloneID)
                {
                    br.close ();
                    return mid;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = public int getContainerMethodID (int cloneID, int revision). "+e);
        }
        
        return -1;
    }
    
    public int getChangeCountInClone (int gcid, int revision, int cloneType)
    {
        String [] changes = new String [5000];
        changes = cp.getAllLinesFromFile(cp.getChangesPathOfRevision(revision));
        
        int clonesline = getCloneStartingLine(gcid, revision, cloneType);
        int cloneeline = getCloneEndingLine(gcid, revision, cloneType);
        
        int count = 0;
        
        for (int i =0;changes[i] != null; i++)
        {
            String changefilepath = cp.getAttributeValueFromString(changes[i], an.filePath);
            int csline = Integer.parseInt(cp.getAttributeValueFromString(changes[i], an.startingLine));
            int celine = Integer.parseInt(cp.getAttributeValueFromString(changes[i], an.endingLine));
            
            if ((csline >= clonesline && csline <= cloneeline) || (clonesline >= csline && clonesline <= celine))
            {
                count++;
            }
        }
                
        return count;
    }
    
    
}
