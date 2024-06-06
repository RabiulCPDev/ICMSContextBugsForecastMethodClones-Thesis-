/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.*;

/**
 *
 * @author mani
 */


class PotentialClonePair
{
    int gcid1 = 0, gcid2 = 0, spcp=0;
    String madePairInRevisions = "";
    String gcid1ChangedInRevisions = "";
    String gcid2ChangedInRevisions = "";
}

class SPCPClonePair
{
    int gcid1 = 0, gcid2 = 0;
    String madePairInRevisions = "";
    String gcid1ChangedInRevisions = "";
    String gcid2ChangedInRevisions = "";
}


public class SPCPCloneDetection {
            
    PotentialClonePair [] pairs = new PotentialClonePair[500000];
    int pairCount = 0;
    
    
    CommonParameters cp = new CommonParameters ();
    CommonMethods cm = new CommonMethods ();
    AttributeNames an = new AttributeNames ();
    DataAccess da = new DataAccess ();  
    DatabaseAccess dbase =  new DatabaseAccess ();
    
    
    
    
    
    public void getSPCPClonePairsFromDatabase (int clonetype)
    {
        SingleSPCPClonePair [] spcppairs = new SingleSPCPClonePair [50000];
        int spcpPairCount = 0;
        
        PotentialClonePair [] ppairs =  new PotentialClonePair[5000000];
        int ppaircount = 0;
        
        
        //examining the revisions from last one to first one.
        for (int r =cp.revisionCount;r>=1;r--)
        {    
            System.out.println ("working on revision = "+r);
        
            SingleClone [] clones = dbase.getClones(r, clonetype);
            SingleClonePair [] pairs = dbase.getClonePairs(r, clonetype);
            
            //update change counts of the clones in potential pairs.
            for (int i =0;clones[i] != null;i++)
            {
                if (Integer.parseInt(clones[i].changecount) > 0)
                {
                    for (int j =0;j<ppaircount;j++)
                    {
                        if (clones[i].globalcloneid.equals (ppairs[j].gcid1+""))
                        {
                            ppairs[j].gcid1ChangedInRevisions += " " + r + " ";
                        }
                        if (clones[i].globalcloneid.equals (ppairs[j].gcid2+""))
                        {
                            ppairs[j].gcid2ChangedInRevisions += " " + r + " ";
                        }
                    }
                }
            }
            
            for (int i =0;pairs[i] != null;i++)
            {
                int j = 0;
                for (j=0;j<ppaircount;j++)
                {
                    if ((pairs[i].globalcloneid1.equals (ppairs[j].gcid1+"") && pairs[i].globalcloneid2.equals (ppairs[j].gcid2+""))
                    ||  (pairs[i].globalcloneid2.equals (ppairs[j].gcid1+"") && pairs[i].globalcloneid1.equals (ppairs[j].gcid2+"")))
                    {                        
                        //the potential pair is again found.
                        if (ppairs[j].spcp < 1)
                        {
                            ppairs[j].madePairInRevisions += " "+r+" ";
                            if (ppairs[j].gcid1ChangedInRevisions.trim().length() > 0 || ppairs[j].gcid2ChangedInRevisions.trim().length() > 0)
                            {
                                ppairs[j].spcp = 1;
                            }
                        }
                        break;
                    }
                }
                if (j == ppaircount) //the pair was not found. So include it in the potential pairs.
                {
                    ppairs[j] = new PotentialClonePair ();
                    ppairs[j].gcid1 = Integer.parseInt (pairs[i].globalcloneid1);
                    ppairs[j].gcid2 = Integer.parseInt (pairs[i].globalcloneid2);
                    ppairs[j].madePairInRevisions += " " + r + " ";
                    ppaircount++;
                }
            }

            
        }   
        int j =0;
        for (int i =0;i<ppaircount;i++)
        {
            if (ppairs[i].spcp == 1 && ppairs[i].gcid1 > -1 && ppairs[i].gcid2 > -1) 
            {
                spcppairs[j] = new SingleSPCPClonePair();
                spcppairs[j].globalcloneid1 = ppairs[i].gcid1+"";
                spcppairs[j].globalcloneid2 = ppairs[i].gcid2+"";
                spcppairs[j].madepairsinrevisions = ppairs[i].madePairInRevisions;
                spcppairs[j].gcid1changedinrevisions = ppairs[i].gcid1ChangedInRevisions;
                spcppairs[j].gcid2changedinrevisions = ppairs[i].gcid2ChangedInRevisions;
                spcppairs[j].clonetype = clonetype;
                j++;
            }
        }
        
        dbase.insertSPCPClones(spcppairs);
    }
    
    
    
    //this method detects and returns the SPCP clone pairs.
    
    public SPCPClonePair [] getSPCPClonePairs (int cloneType) throws Exception
    {
        SPCPClonePair [] spcpPairs = new SPCPClonePair [50000];
        int spcpPairCount = 0;
        
        int [] clones = new int [50000];
        int [] classes = new int [50000];
        int [] changes = new int [50000];
        int count = 0;
        
        //examining the revisions from last one to first one.
        for (int r =cp.revisionCount;r>=1;r--)
        {    
            System.out.println ("working on revision = "+r);
            
            
            //retriving all clones in the arrays.
            count = 0;            
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+r+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                clones[count] = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalCloneID));
                classes[count] = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneClassID));                                                
                if (r == cp.revisionCount)
                {
                    changes[count] = 0;
                }
                else
                {
                    changes[count] = da.getChangesToClone(r, clones[count], cloneType);
                }
                count++;
            }
            
            //making pairs of clones.
            for (int i =0;i<count-1;i++)
            {
                for (int j = i+1;j<count;j++)
                {
                    if (classes[i] == classes[j])
                    {
                        int p1 = clones[i];
                        int p2 = clones[j];
                        int found1 = 0;
                        int found2 = 0;
                        int pairFound = 0;
                        
                        //did we get this pair before?  or
                        //were all the elements of the pair found before?
                        int k = 0;
                        for (k=0;k<pairCount;k++)
                        {
                            if (pairs[k].spcp == 1) {continue;}
                            if ((pairs[k].gcid1 == p1 && pairs[k].gcid2 == p2) || (pairs[k].gcid2 == p1 && pairs[k].gcid1 == p2))
                            {
                                pairs[k].madePairInRevisions += " " + r + " ";
                                if (changes[i] > 0)
                                {
                                    if (pairs[k].gcid1 == p1){ pairs[k].gcid1ChangedInRevisions += " " + r + " "; }
                                    else{ pairs[k].gcid2ChangedInRevisions += " " + r + " "; }
                                }
                                if (changes[j] > 0)
                                {
                                    if (pairs[k].gcid1 == p2){ pairs[k].gcid1ChangedInRevisions += " " + r + " "; }
                                    else{ pairs[k].gcid2ChangedInRevisions += " " + r + " "; }                                    
                                }
                                
                                if (pairs[k].gcid1ChangedInRevisions.trim().length() > 0 || pairs[k].gcid2ChangedInRevisions.trim().length() > 0)
                                {
                                    //we have got an SPCP clone pair.
                                    pairs[k].spcp = 1;
                                    
                                    spcpPairs[spcpPairCount] = new SPCPClonePair ();
                                    spcpPairs[spcpPairCount].gcid1 = pairs[k].gcid1;
                                    spcpPairs[spcpPairCount].gcid2 = pairs[k].gcid2;
                                    spcpPairs[spcpPairCount].madePairInRevisions = pairs[k].madePairInRevisions;
                                    spcpPairs[spcpPairCount].gcid1ChangedInRevisions = pairs[k].gcid1ChangedInRevisions;
                                    spcpPairs[spcpPairCount].gcid2ChangedInRevisions = pairs[k].gcid2ChangedInRevisions;
                                    spcpPairCount++;
                                    
                                    
                                    System.out.println ("\n\npair: "+pairs[k].gcid1 + ", "+pairs[k].gcid2);
                                    System.out.println ("made pair in revisions = "+pairs[k].madePairInRevisions);
                                    System.out.println (pairs[k].gcid1 + " changed in revisions = "+pairs[k].gcid1ChangedInRevisions);
                                    System.out.println (pairs[k].gcid2 + " changed in revisions = "+pairs[k].gcid2ChangedInRevisions);
                                }
                                
                                pairFound = 1;
                                break;
                            }                            
                            if (pairs[k].gcid1 == p1 || pairs[k].gcid2 == p1) {found1 = 1;}
                            if (pairs[k].gcid1 == p2 || pairs[k].gcid2 == p2) {found2 = 1;}                            
                        }
                        
                        //adding the pair as a potential one.
                        if (pairFound == 0 && (found1 == 0 || found2 == 0)) 
                        {
                            pairs[pairCount] = new PotentialClonePair ();
                            pairs[pairCount].gcid1 = p1;
                            pairs[pairCount].gcid2 = p2;
                            pairs[pairCount].madePairInRevisions += " " + r + " ";
                            if (changes[i] > 0)
                            {
                                pairs[pairCount].gcid1ChangedInRevisions += " " + r + " ";
                            }
                            if (changes[j] > 0)
                            {
                                pairs[pairCount].gcid2ChangedInRevisions += " " + r + " ";
                            }
                            pairCount++;
                        }
                    }
                }
            }
            
        }
        
        writeSPCPClonePairs (cloneType, spcpPairs);
        
        return spcpPairs;
    }
    
    public void writeSPCPClonePairs (int cloneType, SPCPClonePair [] spcpPairs)
    {
        try
        {
            BufferedWriter br = new BufferedWriter (new FileWriter (cp.subject_system+"/spcp/type"+cloneType+"spcpclonepairs.txt"));
            int i =0;
            
            while (spcpPairs[i] != null)
            {
                br.write ("\n"+spcpPairs[i].gcid1 + " : "+spcpPairs[i].gcid2 + " : "+spcpPairs[i].gcid1ChangedInRevisions + " : " + spcpPairs[i].gcid2ChangedInRevisions + " : "+ spcpPairs[i].madePairInRevisions);
                i++;
            }
            br.close ();
        }
        catch (Exception e)
        {
            System.out.println ("error in method = writeSPCPClonePairs. "+e);
        }
    }
 
    public SPCPClonePair [] readSPCPClonePairs (int cloneType)
    {
        SPCPClonePair [] spcpPairs = new SPCPClonePair [50000];
        int i =0;
        String str = "";
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/spcp/type"+cloneType+"spcpclonepairs.txt")));
            while ((str = br.readLine ())!= null)
            {
                if (str.trim().length() == 0) {continue;}
                int gcid1 = Integer.parseInt(str.trim().split("[:]+")[0].trim());
                int gcid2 = Integer.parseInt(str.trim().split("[:]+")[1].trim());
                String ch1 = str.trim().split("[:]+")[2].trim();
                String ch2 = str.trim().split("[:]+")[3].trim();
                String mp = str.trim().split("[:]+")[4].trim();
                
                spcpPairs[i] = new SPCPClonePair ();
                spcpPairs[i].gcid1 = gcid1;
                spcpPairs[i].gcid2 = gcid2;
                spcpPairs[i].gcid1ChangedInRevisions = ch1;
                spcpPairs[i].gcid2ChangedInRevisions = ch2;
                spcpPairs[i].madePairInRevisions = mp;
                i++;
            }
            br.close ();
        }
        catch (Exception e)
        {
            System.out.println ("error in method = readSPCPClonePairs. "+e);
        }
        
        return spcpPairs ;
    }
            
            
    
    // this method determines and returns the individual SPCP clones by reading the SPCP clone pairs.
    
    public String getSPCPClones (int cloneType)
    {
        SPCPClonePair [] spcpPairs = readSPCPClonePairs (cloneType);
        String spcpClones = "";
        
        String lastRevisionClones = " "+cm.getClonesInRevision(cp.revisionCount, cloneType)+" ";            
        //String lastRevisionClones = " ";            

        int i = 0;
        while (spcpPairs[i] != null)
        {
            int cls1 = cm.getCloneClass(cp.revisionCount, spcpPairs[i].gcid1, cloneType);
            int cls2 = cm.getCloneClass(cp.revisionCount, spcpPairs[i].gcid2, cloneType);

            if (cls1 != cls2) {i++;continue;}

            //if (!spcpClones.contains(" " + spcpPairs[i].gcid1 + " ") && lastRevisionClones.contains(" " + spcpPairs[i].gcid1 + " "))
            if (!spcpClones.contains(" " + spcpPairs[i].gcid1 + " "))
            {
                spcpClones += " " + spcpPairs[i].gcid1 + " ";
            }
            //if (!spcpClones.contains(" " + spcpPairs[i].gcid2 + " ") && lastRevisionClones.contains(" " + spcpPairs[i].gcid2 + " "))
            if (!spcpClones.contains(" " + spcpPairs[i].gcid2 + " "))
            {
                spcpClones += " " + spcpPairs[i].gcid2 + " ";
            }  
            i++;
        }

        System.out.println ("SPCP clones: "+ spcpClones);
        return spcpClones ;
    }
}
