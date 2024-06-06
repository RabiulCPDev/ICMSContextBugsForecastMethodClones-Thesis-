/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.*;

/**
 *
 * @author me
 */
public class OriginAnalysisForClones {
    
    CommonParameters cp = new CommonParameters ();
    AttributeNames an = new AttributeNames ();    
    OriginAnalysis oa = new OriginAnalysis ();
    ChangeAnalysis ca = new ChangeAnalysis ();
    int matchThreshold = 70;
    
    int cloneType = 1;
    int maxGlobalCloneID = -1;
    
    String [] clones = new String [50000];
    int cloneCount = 0;

    
    public void analyzeOrigin (int cloneType)
    {
        for (int i =1;i<=cp.revisionCount;i++)
        {
            if (i == 1)
            {
                setMaxGlobalCloneID (0, cloneType);
            }
            
            
            maxGlobalCloneID = getMaxGlobalCloneID (cloneType);            
            System.out.println ("type "+cloneType+" origin analysis. revision = "+i);
            analyzeOrigin (i, cloneType);            
            setMaxGlobalCloneID (maxGlobalCloneID, cloneType);            
        }                
    }

    
    
    public void analyzeOrigin (int revision, int cloneType)
    {
        try
        {       
            
            cloneCount =0;
                       
            //get all the methods in revision.
            String [] methods = getMethods (revision);
            
            //get all methods in previous revision.
            String [] pmethods = getMethods (revision-1);
            
            int prevision = revision-1;
            for (int k = revision-1;k>=1;k--)
            {
                pmethods = getMethods (k);
                if (pmethods[0] != null)
                {
                    prevision = k;
                    break;
                }
            }            
            
            int i=0,j=0;
            
            for (i=0; methods[i] != null; i++)
            {
                int gmid = Integer.parseInt(cp.getAttributeValueFromString(methods[i], an.globalMethodID));
                
                //get all clones in this method.
                String [] cclones = getClonesInMethod (gmid, revision, cloneType);
                String [] pclones = getClonesInMethod (gmid, revision-1, cloneType);
                String [] newclones = new String [1000];
                                                
                newclones = makeComplexCorrespondence (gmid, revision, cclones, pclones);
                
                for (j=0;newclones[j] != null;j++)
                {
                    clones[cloneCount] = newclones[j];
                    cloneCount++;
                }                                                                
            }
            
            System.out.print("\twriting begins");
            BufferedWriter br = new BufferedWriter (new FileWriter (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt"));
            for (i =0;i<cloneCount;i++)
            {
                br.write ("\n" + clones[i]);
            }
            br.close ();
            System.out.print("\twriting done\n");
        }
        catch (Exception e)
        {
            System.out.println ("error in analyzeOrigin. "+e);
        }
    }
    

    public int getBeginOffsetForOld (String [][] compare, int poffset)
    {
        try
        {
            for (int k =0;compare[k][0] != null; k++)
            {
                if (compare[k][0].equals (""+poffset))
                {
                    if (compare[k][2].trim().length () > 0)
                    {
                        int coffset = Integer.parseInt(compare[k][2].trim());
                        return coffset;
                    }
                    else
                    {
                        for (int i=k;compare[i][2] != null;i++)
                        {
                            if (compare[i][2].trim().length() > 0)
                            {
                                int coffset = Integer.parseInt(compare[i][2].trim());
                                return coffset;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getBeginOffsetForOld (String [][] compare, int coffset). "+e);
        }

        return -1;
    }


    public int getEndOffsetForOld (String [][] compare, int poffset)
    {
        try
        {
        for (int k =0;compare[k][0] != null; k++)
        {
            if (compare[k][0].equals (""+poffset))
            {
                if (compare[k][2].trim().length () > 0)
                {
                    int coffset = Integer.parseInt(compare[k][2].trim());
                    return coffset;
                }
                else
                {
                    for (int i=k;i>=0;i--)
                    {
                        if (compare[i][2].trim().length() > 0)
                        {
                            int coffset = Integer.parseInt(compare[i][2].trim());
                            return coffset;
                        }
                    }
                }
            }
        }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getEndOffsetForOld (String [][] compare, int coffset). "+e);
        }

        return -1;
    }



    
    public int getBeginOffset (String [][] compare, int coffset)
    {
        try
        {
            for (int k =0;compare[k][0] != null; k++)
            {
                if (compare[k][2].equals (""+coffset))
                {
                    if (compare[k][0].trim().length () > 0)
                    {
                        int poffset = Integer.parseInt(compare[k][0].trim());
                        return poffset;
                    }
                    else
                    {
                        for (int i=k;compare[i][0] != null;i++)
                        {
                            if (compare[i][0].trim().length() > 0)
                            {
                                int poffset = Integer.parseInt(compare[i][0].trim());
                                return poffset;                            
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getBeginOffset (String [][] compare, int coffset). "+e);
        }
        
        return -1;
    }
    
    public int getEndOffset (String [][] compare, int coffset)
    {
        try
        {
        for (int k =0;compare[k][0] != null; k++)
        {
            if (compare[k][2].equals (""+coffset))
            {
                if (compare[k][0].trim().length () > 0)
                {
                    int poffset = Integer.parseInt(compare[k][0].trim());
                    return poffset;
                }
                else
                {
                    for (int i=k;i>=0;i--)
                    {
                        if (compare[i][0].trim().length() > 0)
                        {
                            int poffset = Integer.parseInt(compare[i][0].trim());
                            return poffset;                            
                        }
                    }
                }
            }
        }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getEndOffset (String [][] compare, int coffset). "+e);
        }
        
        return -1;
    }
    
    
    
    /*public String [] makeCorrespondence (int gmid, int revision, String [] cclones, String [] pclones)    
    {
        String [] ncclones = new String[1000];
        if (cclones[0] == null){return ncclones;}
        
        try
        {   
            String usedIDs = "";
            //ChangeAnalysis ca = new ChangeAnalysis ();
            //String compare [][] = ca.compareMethods (gmid, revision-1, gmid, revision);
            //ca.dispose();
            
                                                
            for (int i =0;cclones[i] != null;i++)
            {
                String cfile = cp.getAttributeValueFromString(cclones[i], an.cloneFilePath);
                int csline = Integer.parseInt(cp.getAttributeValueFromString(cclones[i], an.cloneStartingLine));
                int celine = Integer.parseInt(cp.getAttributeValueFromString(cclones[i], an.cloneEndingLine));
                String ccode = cp.getCode(revision, cfile, csline, celine);
                
                double bestMatch = -1;
                int bestIndex = -1;
                int bestgcid = -1;
                
                int j=0;
                for (j=0;pclones[j] != null;j++)
                {
                    String pfile = cp.getAttributeValueFromString(pclones[j], an.cloneFilePath);
                    int psline = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.cloneStartingLine));
                    int peline = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.cloneEndingLine));
                    int gcid = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.globalCloneID));
                    
                    int prevision = revision - 1;
                    String pcode = cp.getCode(prevision, pfile, psline, peline);
                    
                    double match = cp.getStrikeAMatch(ccode, pcode);
                    
                    if (match > 70 && match > bestMatch && !usedIDs.contains (" "+gcid+" "))
                    {
                        bestMatch = match;
                        bestIndex = j;
                        bestgcid = gcid;
                    }
                }
                if (bestMatch > 70)// this threshold value comes from Lozano's study.
                {
                    ncclones[i] = cclones[i] + an.globalCloneID + " = " + bestgcid + " : ";
                    usedIDs += " " + bestgcid + " ";
                }
                else
                {
                    maxGlobalCloneID++;
                    ncclones[i] = cclones[i] + an.globalCloneID + " = " + maxGlobalCloneID + " : ";                                        
                    System.out.println ("\tMAX GLOBAL clone id = "+maxGlobalCloneID + " (revision = "+revision+") ");
                }                               
            }            
        }
        catch (Exception e)
        {
            System.out.println ("error in method = makeCorrespondence (int gmid, int revision, String [] cclones, String [] pclones). "+ e);
        }
        
        return ncclones;
    }*/
    
    
    
    
    public String [] makeComplexCorrespondence (int gmid, int revision, String [] cclones, String [] pclones)    
    {
        String [] ncclones = new String[1000];
        int ncclonescount = 0;
        if (cclones[0] == null){return ncclones;}
       
        try
        {   
            String usedIDs = "";            
            //String compare [][] = ca.compareMethods (gmid, revision-1, gmid, revision);
            String [][] compare = new String [100][10];
            
                                                
            for (int i =0;cclones[i] != null;i++)
            {
                ncclonescount = i;

                int cgmid = Integer.parseInt(cp.getAttributeValueFromString(cclones[i], an.globalMethodID));                                
                int cstart = cp.getMethodStartingLine(cgmid, revision);
                        //Integer.parseInt(cp.getAttributeValueFromString(cclones[0], an.startingLine));
                int coffset1 = Integer.parseInt(cp.getAttributeValueFromString(cclones[i], an.cloneStartingLine)) - cstart;
                int coffset2 = Integer.parseInt(cp.getAttributeValueFromString(cclones[i], an.cloneEndingLine)) - cstart;
                
                int poffset11 = getBeginOffset (compare, coffset1);
                int poffset22 = getEndOffset (compare, coffset2);
                
                int j=0;
                for (j=0;pclones[j] != null;j++)
                {
                    int pgmid = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.globalMethodID));                                
                    int pstart = cp.getMethodStartingLine(pgmid, revision-1);                    
                    int poffset1 = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.cloneStartingLine)) - pstart;
                    int poffset2 = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.cloneEndingLine)) - pstart;
                    
                    if ((poffset1 >= poffset11 && poffset1 <= poffset22) || (poffset11 >= poffset1 && poffset11 <= poffset2))
                    {
                        int gcid = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.globalCloneID));                        
                        if (!usedIDs.contains (" "+gcid+" "))
                        {
                            ncclones[i] = cclones[i] + " : " + an.globalCloneID + " = " + gcid + " : ";
                            usedIDs += " " + gcid + " ";
                            break;
                        }
                    }
                }
                if (pclones[j] == null)
                {
                    maxGlobalCloneID++;
                    ncclones[i] = cclones[i] + " : " + an.globalCloneID + " = " + maxGlobalCloneID + " : ";                                        
                    System.out.println ("\tmax global clone id = "+maxGlobalCloneID);
                }                                
            }

            //tacking the old clones that are not detected as clones in the new revision.


            for (int i =0;pclones[i] != null;i++)
            {
                int gcid = Integer.parseInt(cp.getAttributeValueFromString(pclones[i], an.globalCloneID));

                if (!usedIDs.contains (" "+gcid+" "))
                {
                    //the previous clone fragment with gcid in this method has not been selected as a clone fragment in the new revision.
                    //We should track this clone fragment for detecting late propagation.

                    //format.
                    //!!__globalmethodid__!! = 125 : !!__cloneclassid__!! = 1 : !!__cloneid__!! = 1 : !!__clonefilepath__!! = asp.c : !!__clonestartingline__!! = 41 : !!__cloneendingline__!! = 49 :  : !!__globalcloneid__!! = 10 :


                    int pgmid = Integer.parseInt(cp.getAttributeValueFromString(pclones[i], an.globalMethodID));
                    int pstart = cp.getMethodStartingLine(pgmid, revision-1);
                    int poffset1 = Integer.parseInt(cp.getAttributeValueFromString(pclones[i], an.cloneStartingLine)) - pstart;
                    int poffset2 = Integer.parseInt(cp.getAttributeValueFromString(pclones[i], an.cloneEndingLine)) - pstart;

                    String cfilepath = cp.getAttributeValueFromString(pclones[i], an.cloneFilePath);

                    int coffset1 = getBeginOffsetForOld (compare, poffset1);
                    int coffset2 = getEndOffsetForOld (compare, poffset2);

                    int cstart = cp.getMethodStartingLine(pgmid, revision);
                    int newstart = cstart + coffset1;
                    int newend = cstart + coffset2;

                    ncclonescount++;
                    ncclones[ncclonescount] = an.globalMethodID + " = " + pgmid + " : " + an.cloneClassID + " = -1 : "+an.cloneID + " = -1 : "+an.cloneFilePath+ " = "+cfilepath + " : "+ an.cloneStartingLine + " = "+ newstart + " : " + an.cloneEndingLine + " = " + newend + " : " + an.globalCloneID + " = " + gcid + " : ";
                }
            }



        }
        catch (Exception e)
        {
            System.out.println ("error in method = makeComplexCorrespondence (int gmid, int revision, String [] cclones, String [] pclones). "+ e);
        }
        
        return ncclones;
    }
    
    /*public String [] makeSimpleCorrespondence (String [] cclones, String [] pclones)
    {                
        String [] ncclones = new String[10000];        
        if (cclones[0] == null){return ncclones;}
        
        try
        {            
            int i =0,j=0;
                        
            for (i=0;cclones[i] != null; i++)
            {
                int cstart = Integer.parseInt(cp.getAttributeValueFromString(cclones[0], an.startingLine));
                int coffset1 = Integer.parseInt(cp.getAttributeValueFromString(cclones[i], an.cloneStartingLine)) - cstart + 1;
                int coffset2 = Integer.parseInt(cp.getAttributeValueFromString(cclones[i], an.cloneEndingLine))  - cstart + 1;

                for (j=0;pclones[j] != null;j++)
                {
                    int pstart = Integer.parseInt(cp.getAttributeValueFromString(pclones[0], an.startingLine));
                    int poffset1 = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.cloneStartingLine)) - pstart +1;
                    int poffset2 = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.cloneEndingLine)) - pstart + 1;

                    if (coffset1 == poffset1 && coffset2 == poffset2) //got the origin.
                    {
                        int gcid = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.globalCloneID));
                        ncclones[i] = cclones[i] + " : " + an.globalCloneID + " = " + gcid + " : ";
                        break;
                    }
                }
                if (pclones[j] == null)
                {
                    int newgcid = getMaxGlobalCloneID ()+1;
                    ncclones[i] = cclones[i] + " : " + an.globalCloneID + " = " + newgcid + " : ";
                    setMaxGlobalCloneID (newgcid);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = makeSimpleCorrespondence (String [] cclones, String [] pclones). "+e);
        }
        return ncclones;
    }*/
    
    
    public String [] getClonesInMethod (int gmid, int revision, int cloneType)
    {
        String [] clones = new String[1000];
        int n=0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str=br.readLine())!=null)
            {
                if (str.trim().length() == 0) {continue;}
                int mid = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
                if (gmid == mid)
                {
                    clones[n] = str;
                    n++;
                }                
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethods (int revision). "+e);
        }        

        
        return clones;
    }
    
    public String [] getMethods (int revision)
    {
        String [] methods = new String [50000];
        int n = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/methods/methods_version_"+revision+".txt")));
            String str = "";
            
            while ((str=br.readLine())!=null)
            {
                if (str.trim().length() == 0) {continue;}
                methods[n] = str;
                n++;
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethods (int revision). "+e);
        }        
        
        return methods;
    }
    

    /*public String [] getClonedMethods (int revision)
    {
        String [] clonedMethods = new String [500000];
        int n = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/type123_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str=br.readLine())!=null)
            {
                if (str.trim().length() == 0) {continue;}
                clonedMethods[n] = str;
                n++;
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethods (int revision). "+e);
        }        
        
        return clonedMethods;
    }*/
    
    
    
    /*public void analyzeOriginold ()
    {
        try
        {                        
            for (int r = 1;r<=cp.revisionCount;r++)
            {
                System.out.println ("origin analysis for clones. revision = "+r);
                
                String [] clones = new String [50000];
                int cloneCount = 0;
                
                BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream(cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+r+".txt")));
                String str ="";
                
                while ((str = br.readLine())!= null)
                {
                    if (str.trim().length() == 0) {continue;}
                    
                    int cloneStartLine = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneStartingLine));
                    int cloneEndLine = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneEndingLine));
                    int globalMethodID = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
                    String filePath = cp.getAttributeValueFromString(str, an.filePath);                    
                    String cloneCode = getCode(cloneStartLine, cloneEndLine, filePath, r);                       
                    
                    int origin = getOrigin(cloneCode, globalMethodID, r);
                    if (origin == -1)
                    {
                        origin = getMaxGlobalCloneID ()+1;
                        setMaxGlobalCloneID (origin);                                                
                    }
                    clones[cloneCount] = str + an.globalCloneID + " = " + origin + " : ";
                    cloneCount++;
                }
                br.close ();
                
                BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+r+".txt"));
                for (int i =0;i<cloneCount;i++)
                {
                    writer.write ("\n"+clones[i]);
                }
                writer.close ();
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in analyzeOrigin. "+e);
        }
    }*/
    
    public String getCode (int startLine, int endLine, String filePath, int revision)
    {
        try
        {
            String code = "";
            BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream(cp.subject_system+"/repository/version-"+revision+"/"+filePath)));
            String str ="";
            int c = 0;

            while ((str = br.readLine())!= null)
            {
                c++;
                if (c > endLine)
                {
                    break;
                }
                if (c >= startLine && c<=endLine)
                {
                    code += " "+str;
                }
            }
            br.close();
            return code;
        }
        catch (Exception e)
        {
            System.out.println ("error in getCode. "+e);
        }
        
        return "";
    }
    
    public int getOrigin (String clone, int gmid, int revision)
    {
        try
        {
            double bestMatch = 0;
            int origin = -1;
            
            for (int r = revision-1;r>=1;r--)
            {
                bestMatch = 0;
                BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+r+".txt")));
                String str = "";
                
                while ((str = br.readLine())!= null)
                {
                    if (str.trim().length() == 0) {continue;}
                    
                    int id = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalMethodID));
                    if (id == gmid)
                    {
                        int cloneStartLine = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneStartingLine));
                        int cloneEndLine = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneEndingLine));
                        String filePath = cp.getAttributeValueFromString(str, an.filePath);
                        String cloneCode = getCode(cloneStartLine, cloneEndLine, filePath, r);                                                                
                        
                        double match = oa.compareStrings(clone, cloneCode)*100;                        
                        if (match > bestMatch)
                        {
                            bestMatch = match;
                            origin = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                        }
                    }                    
                }
                br.close();
                if (bestMatch > matchThreshold)
                {
                    return origin;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in getOrigin. "+e);
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
    
    public void setMaxGlobalCloneID (int mcid, int cloneType)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter (new FileWriter (cp.subject_system+"/maxglobalcloneid_type"+cloneType+".txt"));
            bw.write (""+mcid+"");
            bw.close();
        }
        catch (Exception e)
        {
            System.out.println ("error in setMaxGlobalCloneID. "+e);
        }
    }        
}
