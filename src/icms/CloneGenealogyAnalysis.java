/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.*;
import javax.swing.JOptionPane;

/**
 *
 * @author me
 */
public class CloneGenealogyAnalysis {
    
    CommonParameters cp = new CommonParameters ();
    AttributeNames an = new AttributeNames ();    
    OriginAnalysis oa = new OriginAnalysis ();
    ChangeAnalysis ca = new ChangeAnalysis ();
    int matchThreshold = 70;
    
    int cloneType = 1;
    int maxGlobalCloneID = -1;
    
    String [] clones = new String [50000];
    int cloneCount = 0;
    
    DatabaseAccess da = new DatabaseAccess ();
        
    SingleClone [] currentclones = new SingleClone [50000];
    SingleClone [] previousclones = new SingleClone [50000];
    
    SingleMethod [] currentmethods = new SingleMethod [50000];
    SingleMethod [] previousmethods = new SingleMethod [50000];
    

    public boolean confirmInsertion (String tablename)
    {
        boolean hasdata = da.hasData(tablename);
        if (hasdata)
        {
            int answer = JOptionPane.showConfirmDialog(null, "The table contains data. Do you want to re-insert?");
            if (answer != 0)
            {
                return false;
            }
        }        
        return true;
    }    
    
    public void analyzeOrigin (int cloneType)
    {
        if (confirmInsertion ("type"+cloneType+"clonesinmethods")== false ){ return; }
        
        for (int i =1;i<=cp.revisionCount;i++)
        {
            currentclones = da.getClones(i, cloneType);
            previousclones = da.getClones(i-1, cloneType);
            
            currentmethods = da.getMethods(i);
            previousmethods = da.getMethods(i-1);
                                    
            
            if (i == 1)
            {
                setMaxGlobalCloneID (0, cloneType);
            }
            
            
            maxGlobalCloneID = getMaxGlobalCloneID (cloneType);            
            System.out.println ("type "+cloneType+" origin analysis. revision = "+i);
            analyzeOrigin (i, cloneType);            
            setMaxGlobalCloneID (maxGlobalCloneID, cloneType);  
        } 
        
        //updating the clone pairs with the globalcloneids.
        updateClonePairsAll (cloneType);        
    }

    public void updateClonePairs (int revision, int clonetype)
    {
        SingleClonePair [] clonepairs = da.getClonePairs(revision, clonetype);
        SingleClone [] clones = da.getClones(revision, clonetype);
        
        for (int i =0;clonepairs[i] != null;i++)
        {
            int p1 = Integer.parseInt(clonepairs[i].cloneid1);
            int p2 = Integer.parseInt(clonepairs[i].cloneid2);
            
            int gm1 = -1, gm2 = -1;
            
            for (int j=0;clones[j] != null;j++)
            {
                if (p1 == Integer.parseInt(clones[j].cloneid))
                {
                    gm1 = Integer.parseInt(clones[j].globalcloneid);
                }

                if (p2 == Integer.parseInt(clones[j].cloneid))
                {
                    gm2 = Integer.parseInt(clones[j].globalcloneid);
                }
                
                if (gm1 != -1 && gm2 != -1)
                {
                    break;
                }
            }
            clonepairs[i].globalcloneid1 = gm1+"";
            clonepairs[i].globalcloneid2 = gm2 + "";            
        }
        da.deleteClonePairs (revision, clonetype);
        da.insertClonePairs(clonepairs, clonetype);        
    }
    
    public void updateClonePairsAll (int clonetype)
    {
        for (int i = 1;i<=cp.revisionCount;i++)
        {
            System.out.println ("updating clonepairs in revision = "+i);
            updateClonePairs(i, clonetype);
        }
    }
    
    
    
    
    public void analyzeOrigin (int revision, int cloneType)
    {
        try
        {                   
            cloneCount =0;
            int prevision = revision-1;
                       
            //get all the methods in revision.
            SingleMethod [] methods = currentmethods;
            
            SingleClone [] newcurrentclones = new SingleClone[100000];
            int newcurrentclonescount = 0;
            
            //determining the list of methods that have clones within them.
            SingleClone [] temp = currentclones;
            String list = "";
            for (int i =0;temp[i] != null;i++)
            {
                list += " " + temp[i].methodid + " ";
            }
            
            
            
            /*for (int k = revision-1;k>=1;k--)
            {
                pmethods = da.getData("methods", "filepath, startline, endline, methodid", k);
                if (pmethods[0] != null)
                {
                    prevision = k;
                    break;
                }
            }*/            
            
            int i=0,j=0;
            
            for (i=0; methods[i] != null; i++)
            {
                int mid = Integer.parseInt(methods[i].methodid);
                
                if (!list.contains (" " + mid + " ")) {continue;}
                
                System.out.println ("for "+mid);
                
                //get all clones in this method.
                SingleClone [] cclones = getCurrentClonesInMethod (mid);
                
                
                //get corresponding methodid in the previous revision.
                int gmid = -1;//Integer.parseInt(da.getData("methodgenealogies", "globalmethodid" , " where revision = "+revision + " and methodid = "+mid)[0].trim());
                for (j =0;currentmethods[j] != null;j++)
                {
                    int m = Integer.parseInt(currentmethods[j].methodid);
                    if (m == mid)
                    {
                        gmid = Integer.parseInt(currentmethods[j].globalmethodid);
                        break;
                    }
                }
                int previousmid = -1;
                SingleMethod previousmethod = new SingleMethod ();
                for (j = 0;previousmethods[j] != null;j++)
                {
                    int gm = Integer.parseInt (previousmethods[j].globalmethodid);
                    if (gm == gmid)
                    {
                        previousmid = Integer.parseInt(previousmethods[j].methodid);                        
                        previousmethod = previousmethods[j];
                        break;
                    }
                }
                
                
                SingleClone [] pclones = getPreviousClonesInMethod (previousmid);
                                                
                SingleClone [] ncclones = makeComplexCorrespondence (methods[i], previousmethod, revision, cclones, pclones, cloneType);
                
                for (int n=0;ncclones[n] != null;n++)
                {
                    newcurrentclones[newcurrentclonescount] = new SingleClone();
                    newcurrentclones[newcurrentclonescount] = ncclones[n];
                    
                    newcurrentclonescount++;
                }                
            }
            for (i=0;currentclones[i] != null;i++)
            {
                int cloneid = Integer.parseInt(currentclones[i].cloneid);
                for (j =0;j<newcurrentclonescount;j++)
                {
                    if (newcurrentclones[j].cloneid.equals(cloneid+""))
                    {
                        break;
                    }
                }
                if (j == newcurrentclonescount)
                {
                    newcurrentclones[newcurrentclonescount] = new SingleClone();
                    newcurrentclones[newcurrentclonescount] = currentclones[i];
                    newcurrentclonescount++;
                }
            }
            
            //delete previous clones.
            da.deleteClones(revision, cloneType);
            da.insertClones(newcurrentclones, cloneType);
            
        }
        catch (Exception e)
        {
            System.out.println ("error in analyzeOrigin. "+e);
        }
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
    
            
    public SingleClone [] makeComplexCorrespondence (SingleMethod cmethod, SingleMethod pmethod, int revision, SingleClone [] cclones, SingleClone [] pclones, int cloneType)    
    {
        SingleClone [] ncclones = new SingleClone[1000];
        int ncclonescount = 0;
        
        //if (cclones[0] == null){return ncclones;}
       
        try
        {   
            String usedIDs = "";            
            String compare [][] = ca.compareMethods (pmethod, cmethod, revision-1, revision);
            
                                                
            for (int i =0;cclones[i] != null;i++)
            {
                //int cgmid = Integer.parseInt(cp.getAttributeValueFromString(cclones[i], an.globalMethodID));                                
                int cstart = Integer.parseInt(cmethod.startline);
                        
                int coffset1 = Integer.parseInt(cclones[i].startline) - cstart; //clone start line - method start line.
                int coffset2 = Integer.parseInt(cclones[i].endline) - cstart; //clone end line - method start line.
                
                int poffset11 = getBeginOffset (compare, coffset1);
                int poffset22 = getEndOffset (compare, coffset2);
                
                
                //checking the exact correspondence.
                int j=0, gotexact = 0;
                for (j=0;pclones[j] != null;j++)
                {
                    int pstart = Integer.parseInt(pmethod.startline);  
                    int poffset1 = Integer.parseInt(pclones[j].startline) - pstart;
                    int poffset2 = Integer.parseInt(pclones[j].endline) - pstart;
                    
                    if (poffset1 == poffset11 && poffset2 == poffset22)
                    {
                        int gcid = Integer.parseInt(pclones[j].globalcloneid);
                        if (!usedIDs.contains (" "+gcid+" "))
                        {
                            cclones[i].globalcloneid = gcid+"";
                            ncclones[ncclonescount] = new SingleClone ();
                            ncclones[ncclonescount] = cclones[i];
                            ncclonescount++;
                                                        
                            usedIDs += " " + gcid + " ";
                            gotexact = 1;
                            break;
                        }                        
                    }
                }
                
                if (gotexact == 0)
                {
                    //checking the non-exact correspondence.
                    j=0;
                    for (j=0;pclones[j] != null;j++)
                    {
                        //int pgmid = Integer.parseInt(cp.getAttributeValueFromString(pclones[j], an.globalMethodID));                                
                        int pstart = Integer.parseInt(pmethod.startline);                   
                        int poffset1 = Integer.parseInt(pclones[j].startline) - pstart;
                        int poffset2 = Integer.parseInt(pclones[j].endline) - pstart;

                        if ((poffset1 >= poffset11 && poffset1 <= poffset22) || (poffset11 >= poffset1 && poffset11 <= poffset2))
                        {
                            //get gcid from previous clone.
                            int gcid = Integer.parseInt(pclones[j].globalcloneid);                        
                            if (!usedIDs.contains (" "+gcid+" "))
                            {
                                cclones[i].globalcloneid = gcid+"";
                                ncclones[ncclonescount] = new SingleClone ();
                                ncclones[ncclonescount] = cclones[i];
                                ncclonescount++;

                                usedIDs += " " + gcid + " ";
                                break;
                            }
                        }
                    }
                    if (pclones[j] == null) //this is a new clone.
                    {
                        maxGlobalCloneID++;
                        cclones[i].globalcloneid = maxGlobalCloneID + "";
                        ncclones[ncclonescount] = new SingleClone ();
                        ncclones[ncclonescount] = cclones[i];
                        ncclonescount++;                    

                        System.out.println ("\tmax global clone id = "+maxGlobalCloneID);
                    }
                }
            }
            
            //this is for the unmapped clone.
            for (int i =0;cclones[i] != null;i++)
            {
                if (cclones[i].globalcloneid == null)
                {
                    maxGlobalCloneID++;
                    cclones[i].globalcloneid = maxGlobalCloneID + "";
                    ncclones[ncclonescount] = new SingleClone ();
                    ncclones[ncclonescount] = cclones[i];
                    ncclonescount++;
                    
                    System.out.println ("\tunmapped clone. max global clone id = "+maxGlobalCloneID);
                }
            }
            
            
            //DO NOT DELETE THIS COMMENTED PART. THIS PART IS VERY IMPORTANT
            //tracking the old clones that are not detected as clones in the new revision.

            for (int i =0;pclones[i] != null;i++)
            {
                int gcid = Integer.parseInt(pclones[i].globalcloneid);

                if (!usedIDs.contains (" "+gcid+" "))
                {
                    //the previous clone fragment with gcid in this method has not been selected as a clone fragment in the new revision.
                    //We should track this clone fragment for detecting late propagation.

                    int pstart = Integer.parseInt(pmethod.startline);
                    int poffset1 = Integer.parseInt(pclones[i].startline) - pstart;
                    int poffset2 = Integer.parseInt(pclones[i].endline) - pstart;

                    int coffset1 = getBeginOffsetForOld (compare, poffset1);
                    int coffset2 = getEndOffsetForOld (compare, poffset2);

                    int cstart = Integer.parseInt(cmethod.startline);
                    int newstart = cstart + coffset1;
                    int newend = cstart + coffset2;
                    
                    SingleClone previousclone = new SingleClone ();
                    previousclone = pclones[i];
                    previousclone.startline = newstart+"";
                    previousclone.endline = newend+"";
                    previousclone.methodid = cmethod.methodid;
                    previousclone.cloneid = "-1";
                    previousclone.revision = revision+"";
                    previousclone.changecount = "0";
                    
                    ncclones[ncclonescount] = new SingleClone ();
                    ncclones[ncclonescount] = previousclone;
                    ncclonescount++;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = makeComplexCorrespondence (int gmid, int revision, String [] cclones, String [] pclones). "+ e);
        }
        
        return ncclones;
    }
    
    
    public SingleClone [] getPreviousClonesInMethod (int pmid)
    {
        SingleClone [] resultclones = new SingleClone [1000];
        int resultclonecount = 0;
        
        SingleClone [] clones = previousclones;
        for (int i =0;clones[i] != null;i++)
        {
            if (clones[i].methodid.equals (pmid+""))
            {
                resultclones[resultclonecount] = new SingleClone ();
                resultclones[resultclonecount] = clones[i];
                resultclonecount++;
            }
        }
        return resultclones;        
    }
    
    public SingleClone [] getCurrentClonesInMethod (int mid)
    {
        SingleClone [] resultclones = new SingleClone[1000];
        int resultclonecount = 0;
        
        SingleClone [] clones = currentclones;
        for (int i =0;clones[i] != null;i++)
        {
            if (clones[i].methodid.equals (mid+""))
            {
                resultclones[resultclonecount] = new SingleClone ();
                resultclones[resultclonecount] = clones[i];
                resultclonecount++;
            }
        }                
        return resultclones;
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
