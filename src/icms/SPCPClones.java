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

class PotentialPair
{    
    int gcid1 = 0, gcid2 = 0, n = 0, spcp=0;
    int [] revisions = new int [500];
    int [] changes1 = new int [500];
    int [] changes2 = new int [500];
    int [] classid1 = new int [500];
    int [] classid2 = new int [500];
    int [] cochangerevisions = new int [500];
    int support = 0;
    float confidence = 0f;
}

class DifferentClassPair
{
    int gcid1 = 0, gcid2 = 0, n = 0, dcp = 1;
    int [] revisions = new int [500];
    int [] changes1 = new int [500];
    int [] changes2 = new int [500];
    int [] classid1 = new int [500];
    int [] classid2 = new int [500];  
    int [] cochangerevisions = new int [500];
    int support=0;
    float confidence = 0f;
}

class CloneNonclonePair
{
    int gcid = 0, gmid = 0, n = 0, cnp = 1;
    int [] revisions = new int [200];
    int [] clonechanges = new int [200];
    int [] methodchanges = new int [200];
    int [] classid = new int [200];
    int [] methodexists =  new int [200];
    int [] cochangerevisions = new int [200];
    int support=0;
    float confidence = 0f;    
}

class CloneHistory
{
    int gcid=0, n=0;
    int [] revisions = new int [500];
    int [] changes = new int [500];
}

class MethodHistory
{
    int gmid=0, n=0;
    int [] revisions = new int [500];
    int [] changes = new int [500];    
}



public class SPCPClones {
    
    CommonParameters cp = new CommonParameters ();
    AttributeNames an = new AttributeNames ();

    PotentialPair [] potentialPairs = new PotentialPair[5000000];
    int ppCount = 0; //potential pair count
    
    DifferentClassPair [] dcPairs = new DifferentClassPair[1000000];
    int dcpCount = 0;
    
    CloneNonclonePair [] cnPairs = new CloneNonclonePair[1000000]; //cnPairs = clone non-clone pairs.
    int cnpCount = 0;
    
    CloneHistory [] cHistories = new CloneHistory[50000];
    int cCount = 0;
    
    MethodHistory [] mHistories = new MethodHistory[50000];
    int mCount = 0;
    
    DataAccess da = new DataAccess ();
    
    
    
    public void determineSupportAndConfidencesSPCP (int cloneType)
    {
        //support and confidence for SPCP clone pairs.
        for (int i =0;i<ppCount;i++)
        {
            System.out.println ("support and confidence of  spcp "+i+" out of "+ppCount);
            if (potentialPairs[i].spcp == 0) {continue;}
            int support = 0;
            int n = potentialPairs[i].n;
            for (int j =0;j<n;j++)
            {
                if (potentialPairs[i].changes1[j] > 0 && potentialPairs[i].changes2[j] > 0)
                {
                    potentialPairs[i].cochangerevisions[support] = potentialPairs[i].revisions[j];
                    support++;
                }
            }
            if (support == 0)
            {
                int a = 1;
            }
            int support1 = getSupportOfClone (potentialPairs[i].gcid1);
            int support2 = getSupportOfClone (potentialPairs[i].gcid2);
            
            float confidence1 = (float)support/support1;
            float confidence2 = (float)support/support2;
            float confidence = 0f;
            confidence = confidence1;
            if (confidence1 < confidence2)
            {
                confidence = confidence2;
            }
            
            potentialPairs[i].support = support;
            potentialPairs[i].confidence = confidence;            
        }    
        
        try
        {
            //storing the spcp clone pairs.
            BufferedWriter writer = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/pairs_spcp_type"+cloneType+".txt"));
            for (int i =0;i<ppCount;i++)
            {
                System.out.println ("storing spcp "+i + "out of "+ppCount);
                if (potentialPairs[i].spcp == 0) {continue;}
                writer.write ("\n");
                writer.write ("gcid1 = "+potentialPairs[i].gcid1 + " : "
                        + "gcid2 = " + potentialPairs[i].gcid2 + " : "
                        + "support = "+potentialPairs[i].support + " : "
                        + "confidence = "+potentialPairs[i].confidence + " : "
                        + "cochangecommits = ");
                for (int j =0;j<potentialPairs[i].support;j++)
                {
                    writer.write (" " + potentialPairs[i].cochangerevisions[j] + " ");
                }
                writer.write (" : ");                
            }
            writer.close ();
            
        }
        catch (Exception e)
        {
            
        }
        
    }
    
    
    public void determineSupportAndConfidencesDCP (int cloneType)
    {
        //support and confidence for different class pairs.
        for (int i =0;i<dcpCount;i++)
        {
            System.out.println ("support and confidence of dcp "+i+" out of "+dcpCount);
            if (dcPairs[i].dcp == 0) {continue;}
            int support = 0;
            int n = dcPairs[i].n;
            for (int j =0;j<n;j++)
            {
                if (dcPairs[i].changes1[j] > 0 && dcPairs[i].changes2[j] > 0)
                {
                    dcPairs[i].cochangerevisions[support] = dcPairs[i].revisions[j];
                    support++;
                }
            }
            int support1 = getSupportOfClone (dcPairs[i].gcid1);
            int support2 = getSupportOfClone (dcPairs[i].gcid2);
            
            float confidence1 = (float)support/support1;
            float confidence2 = (float)support/support2;
            float confidence = 0f;
            confidence = confidence1;
            if (confidence1 < confidence2)
            {
                confidence = confidence2;
            }
            
            dcPairs[i].support = support;
            dcPairs[i].confidence = confidence;            
        }    
        
        try
        {
            //storing the different class clone pairs.
            BufferedWriter writer2 = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/pairs_differentclass_type"+cloneType+".txt"));
            for (int i =0;i<dcpCount;i++)
            {
                System.out.println ("storing dcp "+i + "out of "+dcpCount);
                if (dcPairs[i].dcp == 0) {continue;}
                writer2.write ("\n");
                writer2.write ("gcid1 = "+dcPairs[i].gcid1 + " : "
                        + "gcid2 = " + dcPairs[i].gcid2 + " : "
                        + "support = "+dcPairs[i].support + " : "
                        + "confidence = "+dcPairs[i].confidence + " : "
                        + "cochangecommits = ");
                for (int j =0;j<dcPairs[i].support;j++)
                {
                    writer2.write (" " + dcPairs[i].cochangerevisions[j] + " ");
                }
                writer2.write (" : ");                
            }
            writer2.close ();
            
        }
        catch(Exception e)
        {
            
        }
    }
    
    public void determineSupportAndConfidencesCNP (int cloneType)
    {
        //support and confidence for clone non-clone pairs.
        for (int i =0;i<cnpCount;i++)
        {
            System.out.println ("support and confidence of  cnp "+i+" out of "+cnpCount);
            int support = 0;
            int n = cnPairs[i].n;
            for (int j =0;j<n;j++)
            {
                if (cnPairs[i].clonechanges[j] > 0 && cnPairs[i].methodchanges[j] > 0)
                {
                    cnPairs[i].cochangerevisions[support] = cnPairs[i].revisions[j];
                    support++;
                }
            }
            int support1 = getSupportOfClone (cnPairs[i].gcid);
            int support2 = getSupportOfMethod (cnPairs[i].gmid);
            
            float confidence1 = (float)support/support1;
            float confidence2 = (float)support/support2;
            float confidence = 0f;
            confidence = confidence1;
            if (confidence1 < confidence2)
            {
                confidence = confidence2;
            }
            
            cnPairs[i].support = support;
            cnPairs[i].confidence = confidence;            
        }   
        
        try
        {
            //storing the clone non-clone pairs.
            BufferedWriter writer3 = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/pairs_clonenonclone_type"+cloneType+".txt"));
            for (int i =0;i<cnpCount;i++)
            {
                System.out.println ("storing cnp "+i + "out of "+cnpCount);
                writer3.write ("\n");
                writer3.write ("gcid = "+cnPairs[i].gcid + " : "
                        + "gmid = " + cnPairs[i].gmid + " : "
                        + "support = "+cnPairs[i].support + " : "
                        + "confidence = "+cnPairs[i].confidence + " : "
                        + "cochangecommits = ");
                for (int j =0;j<cnPairs[i].support;j++)
                {
                    writer3.write (" " + cnPairs[i].cochangerevisions[j] + " ");
                }
                writer3.write (" : ");                
            }
            writer3.close ();
            
        }
        catch (Exception e)
        {
            
        }
    }
    
    public void storeChangedClonesAndMethods (int cloneType)
    {
        try
        {
            //storing the changed clones.
            BufferedWriter writer4 = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/changed_clones_type"+cloneType+".txt"));
            for (int i =0;i<cCount;i++)
            {
                System.out.println ("storing clone "+i + "out of "+cCount);
                writer4.write ("\n");
                writer4.write ("gcid = "+cHistories[i].gcid + " : "
                        + "support = "+getSupportOfClone(cHistories[i].gcid) + " : "
                        + "changecommits = ");
                for (int j =0;j<cHistories[i].n;j++)
                {
                    if (cHistories[i].changes[j] > 0)
                    {
                        writer4.write (" " + cHistories[i].revisions[j] + " ");
                    }
                }
                writer4.write (" : ");                
            }
            writer4.close ();
            
            //storing the changed methods.
            BufferedWriter writer5 = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/changed_methods.txt"));
            for (int i =0;i<mCount;i++)
            {
                System.out.println ("storing method "+i + "out of "+mCount);
                writer5.write ("\n");
                writer5.write ("gmid = "+mHistories[i].gmid + " : "
                        + "support = "+getSupportOfMethod(mHistories[i].gmid) + " : "
                        + "changecommits = ");
                for (int j =0;j<mHistories[i].n;j++)
                {
                    if (mHistories[i].changes[j] > 0)
                    {
                        writer5.write (" " + mHistories[i].revisions[j] + " ");
                    }
                }
                writer5.write (" : ");                
            }
            writer5.close ();            
        }
        catch (Exception e)
        {
            
        }
    }
    
    public void determineSupportAndConfidences ()
    {        
        //support and confidence for different class pairs.
        for (int i =0;i<dcpCount;i++)
        {
            System.out.println ("support and confidence of dcp "+i+" out of "+dcpCount);
            if (dcPairs[i].dcp == 0) {continue;}
            int support = 0;
            int n = dcPairs[i].n;
            for (int j =0;j<n;j++)
            {
                if (dcPairs[i].changes1[j] > 0 && dcPairs[i].changes2[j] > 0)
                {
                    dcPairs[i].cochangerevisions[support] = dcPairs[i].revisions[j];
                    support++;
                }
            }
            int support1 = getSupportOfClone (dcPairs[i].gcid1);
            int support2 = getSupportOfClone (dcPairs[i].gcid2);
            
            float confidence1 = (float)support/support1;
            float confidence2 = (float)support/support2;
            float confidence = 0f;
            confidence = confidence1;
            if (confidence1 < confidence2)
            {
                confidence = confidence2;
            }
            
            dcPairs[i].support = support;
            dcPairs[i].confidence = confidence;            
        }
        
        //support and confidence for clone non-clone pairs.
        for (int i =0;i<cnpCount;i++)
        {
            System.out.println ("support and confidence of  cnp "+i+" out of "+cnpCount);
            int support = 0;
            int n = cnPairs[i].n;
            for (int j =0;j<n;j++)
            {
                if (cnPairs[i].clonechanges[j] > 0 && cnPairs[i].methodchanges[j] > 0)
                {
                    cnPairs[i].cochangerevisions[support] = cnPairs[i].revisions[j];
                    support++;
                }
            }
            int support1 = getSupportOfClone (cnPairs[i].gcid);
            int support2 = getSupportOfMethod (cnPairs[i].gmid);
            
            float confidence1 = (float)support/support1;
            float confidence2 = (float)support/support2;
            float confidence = 0f;
            confidence = confidence1;
            if (confidence1 < confidence2)
            {
                confidence = confidence2;
            }
            
            cnPairs[i].support = support;
            cnPairs[i].confidence = confidence;            
        }    
        
        
        //support and confidence for SPCP clone pairs.
        for (int i =0;i<ppCount;i++)
        {
            System.out.println ("support and confidence of  spcp "+i+" out of "+ppCount);
            if (potentialPairs[i].spcp == 0) {continue;}
            int support = 0;
            int n = potentialPairs[i].n;
            for (int j =0;j<n;j++)
            {
                if (potentialPairs[i].changes1[j] > 0 && potentialPairs[i].changes2[j] > 0)
                {
                    potentialPairs[i].cochangerevisions[support] = potentialPairs[i].revisions[j];
                    support++;
                }
            }
            if (support == 0)
            {
                int a = 1;
            }
            int support1 = getSupportOfClone (potentialPairs[i].gcid1);
            int support2 = getSupportOfClone (potentialPairs[i].gcid2);
            
            float confidence1 = (float)support/support1;
            float confidence2 = (float)support/support2;
            float confidence = 0f;
            confidence = confidence1;
            if (confidence1 < confidence2)
            {
                confidence = confidence2;
            }
            
            potentialPairs[i].support = support;
            potentialPairs[i].confidence = confidence;            
        }    
                        
        try
        {
            //storing the spcp clone pairs.
            BufferedWriter writer = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/pairs_spcp.txt"));
            for (int i =0;i<ppCount;i++)
            {
                System.out.println ("storing spcp "+i + "out of "+ppCount);
                if (potentialPairs[i].spcp == 0) {continue;}
                writer.write ("\n");
                writer.write ("gcid1 = "+potentialPairs[i].gcid1 + " : "
                        + "gcid2 = " + potentialPairs[i].gcid2 + " : "
                        + "support = "+potentialPairs[i].support + " : "
                        + "confidence = "+potentialPairs[i].confidence + " : "
                        + "cochangecommits = ");
                for (int j =0;j<potentialPairs[i].support;j++)
                {
                    writer.write (" " + potentialPairs[i].cochangerevisions[j] + " ");
                }
                writer.write (" : ");                
            }
            writer.close ();
            
            //storing the different class clone pairs.
            BufferedWriter writer2 = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/pairs_differentclass.txt"));
            for (int i =0;i<dcpCount;i++)
            {
                System.out.println ("storing dcp "+i + "out of "+dcpCount);
                if (dcPairs[i].dcp == 0) {continue;}
                writer2.write ("\n");
                writer2.write ("gcid1 = "+dcPairs[i].gcid1 + " : "
                        + "gcid2 = " + dcPairs[i].gcid2 + " : "
                        + "support = "+dcPairs[i].support + " : "
                        + "confidence = "+dcPairs[i].confidence + " : "
                        + "cochangecommits = ");
                for (int j =0;j<dcPairs[i].support;j++)
                {
                    writer2.write (" " + dcPairs[i].cochangerevisions[j] + " ");
                }
                writer2.write (" : ");                
            }
            writer2.close ();
            
            //storing the clone non-clone pairs.
            BufferedWriter writer3 = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/pairs_clonenonclone.txt"));
            for (int i =0;i<cnpCount;i++)
            {
                System.out.println ("storing cnp "+i + "out of "+cnpCount);
                writer3.write ("\n");
                writer3.write ("gcid = "+cnPairs[i].gcid + " : "
                        + "gmid = " + cnPairs[i].gmid + " : "
                        + "support = "+cnPairs[i].support + " : "
                        + "confidence = "+cnPairs[i].confidence + " : "
                        + "cochangecommits = ");
                for (int j =0;j<cnPairs[i].support;j++)
                {
                    writer3.write (" " + cnPairs[i].cochangerevisions[j] + " ");
                }
                writer3.write (" : ");                
            }
            writer3.close ();
            
            
            //storing the changed clones.
            BufferedWriter writer4 = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/changed_clones.txt"));
            for (int i =0;i<cCount;i++)
            {
                System.out.println ("storing clone "+i + "out of "+cCount);
                writer4.write ("\n");
                writer4.write ("gcid = "+cHistories[i].gcid + " : "
                        + "support = "+getSupportOfClone(cHistories[i].gcid) + " : "
                        + "changecommits = ");
                for (int j =0;j<cHistories[i].n;j++)
                {
                    if (cHistories[i].changes[j] > 0)
                    {
                        writer4.write (" " + cHistories[i].revisions[j] + " ");
                    }
                }
                writer4.write (" : ");                
            }
            writer4.close ();
            
            //storing the changed methods.
            BufferedWriter writer5 = new BufferedWriter (new FileWriter ( cp.subject_system + "/results_crossboundary/changed_methods.txt"));
            for (int i =0;i<mCount;i++)
            {
                System.out.println ("storing method "+i + "out of "+mCount);
                writer5.write ("\n");
                writer5.write ("gmid = "+mHistories[i].gmid + " : "
                        + "support = "+getSupportOfMethod(mHistories[i].gmid) + " : "
                        + "changecommits = ");
                for (int j =0;j<mHistories[i].n;j++)
                {
                    if (mHistories[i].changes[j] > 0)
                    {
                        writer5.write (" " + mHistories[i].revisions[j] + " ");
                    }
                }
                writer5.write (" : ");                
            }
            writer5.close ();
            
        }
        catch (Exception e)
        {
            
        }

        
                
    }
    
    public int getSupportOfClone (int gcid)
    {
        int support = 0;
        for (int i =0;i<cCount;i++)
        {
            if (cHistories[i].gcid == gcid)
            {
                int n = cHistories[i].n;
                for (int j = 0;j<n;j++)
                {
                    if (cHistories[i].changes[j] > 0)
                    {
                        support++;
                    }
                }
                break;
            }
        }
        
        return support;
    }

    public int getSupportOfMethod (int gmid)
    {
        int support = 0;
        for (int i =0;i<mCount;i++)
        {
            if (mHistories[i].gmid == gmid)
            {
                int n = mHistories[i].n;
                for (int j = 0;j<n;j++)
                {
                    if (mHistories[i].changes[j] > 0)
                    {
                        support++;
                    }
                }
                break;
            }
        }
        
        return support;
    }
    
    
    public void getChangedClonesAndMethods (int cloneType)
    {
        int i = 0;
               
        //initializing the counters.
        cCount = 0;
        mCount = 0;
        
        for (i=1;i<cp.revisionCount;i++)
        {            
            System.out.println ("working on revision (getting changed clones and methods) = "+i);
            String [][] changedClones = da.getChangedClones(i, cloneType);
            String [][] changedMethods = da.getChangedMethods(i);
            
            for (int j =0;changedClones[j][0] != null;j++)
            {
                int gcid = Integer.parseInt(changedClones[j][0]);
                int change = Integer.parseInt(changedClones[j][2]);
                
                int index = includeClone (gcid);
                
                int n = cHistories[index].n;
                cHistories[index].changes[n] = change;
                cHistories[index].revisions[n] = i;
                cHistories[index].n++;                
            }
            for (int k=0;changedMethods[k][0] != null;k++)
            {
                int gmid = Integer.parseInt(changedMethods[k][0]);                
                int change = Integer.parseInt(changedMethods[k][1]);
                int index = includeMethod (gmid);
                
                int n = mHistories[index].n;
                mHistories[index].changes[n] = change;
                mHistories[index].revisions[n] = i;
                mHistories[index].n++;                
            }
            
            //update clones.
            /*for (int j=0;j<cCount;j++)
            {
                updateClone (i,j, cHistories[j].gcid);
            }
            for (int j=0;j<mCount;j++)
            {
                updateMethod (i,j, mHistories[j].gmid);
            }*/          
        }
        
        System.out.println ("count of changed clone fragments = "+cCount);
        System.out.println ("count of changed methods = "+mCount);
        
        //need to calculate the support and confidence of the non-spcp pairs.        
    }
            
    public void getCloneNonclonePairs (int  cloneType)
    {
        int i = 0;
                
        for (i=1;i<cp.revisionCount;i++)
        {                  
            System.out.println ("working on revision (getting clone non-clone pairs) = "+i);
            String [][] changedClones = da.getChangedClones(i, cloneType);
            String [][] changedNoncloneMethods = da.getChangedNoncloneMethods(i, cloneType);
            
            for (int j =0;changedClones[j][0] != null;j++)
            {
                int gcid = Integer.parseInt(changedClones[j][0]);
                int clsid = Integer.parseInt(changedClones[j][1]);
                int change = Integer.parseInt(changedClones[j][2]);
                
                for (int k=0;changedNoncloneMethods[k][0] != null;k++)
                {
                    int gmid = Integer.parseInt(changedNoncloneMethods[k][0]);
                    int change2 = Integer.parseInt(changedNoncloneMethods[k][1]);
                                        
                    int index = includeCloneNonclonePair (gcid, gmid);
                    
                    //analyzeCloneNonclonePair (i,index, cnPairs[index].gcid, cnPairs[index].gmid);
                    int n = cnPairs[index].n;
                    cnPairs[index].clonechanges[n] = change;
                    cnPairs[index].methodchanges[n] = change2;
                    cnPairs[index].classid[n] = clsid;
                    cnPairs[index].revisions[n] = i;
                    //cnPairs[index].methodexists[n] = doesMethodExist(i, gmid);
                    cnPairs[index].n++;                    
                }
            }
            
            //analyze clone-nonclone pair.
            /*for (int j=0;j<cnpCount;j++)
            {
                analyzeCloneNonclonePair (i,j, cnPairs[j].gcid, cnPairs[j].gmid);
            }*/
        }
        
        System.out.println ("clone nonclone pair count = "+cnpCount);
        
        //need to calculate the support and confidence of the non-spcp pairs.
        
        
    }
    
    public void analyzeCloneNonclonePair (int revision, int index, int gcid, int gmid)
    {
        int n = cnPairs[index].n;
        cnPairs[index].clonechanges[n] = getChangesToCloneFragment (revision, gcid);
        cnPairs[index].methodchanges[n] = getChangesToMethod (revision, gmid);
        cnPairs[index].classid[n] = getCloneClassID(revision, gcid);
        cnPairs[index].revisions[n] = revision;
        cnPairs[index].methodexists[n] = doesMethodExist(revision, gmid);
        cnPairs[index].n++;
    }
    
    
    public int includeClone (int gcid)
    {
        int i =0, index = -1;
        for (i=0;i<cCount;i++)
        {
            if (cHistories[i].gcid == gcid)
            {
                index = i;
                break;
            }
        }
        if (i == cCount)
        {
            cHistories[i] = new CloneHistory ();
            cHistories[i].gcid = gcid;
            index = i;
            cCount++;
        }
        return index;
    }
    
    public void updateClone (int revision, int index, int gcid)
    {
        int n = cHistories[index].n;
        cHistories[index].changes[n] = getChangesToCloneFragment (revision, gcid);
        cHistories[index].revisions[n] = revision;
        cHistories[index].n++;
    }
    
    
    public int includeMethod (int gmid)
    {
        int i =0, index = -1;
        for (i=0;i<mCount;i++)
        {
            if (mHistories[i].gmid == gmid)
            {
                index = i;
                break;
            }
        }
        if (i == mCount)
        {
            mHistories[i] = new MethodHistory ();
            mHistories[i].gmid = gmid;
            index = i;
            mCount++;
        }
        return index;
    }  
    
    public void updateMethod (int revision, int index, int gmid)
    {
        int n = mHistories[index].n;
        mHistories[index].changes[n] = getChangesToMethod (revision, gmid);
        mHistories[index].revisions[n] = revision;
        mHistories[index].n++;
    }    
    
    
    public int includeCloneNonclonePair (int gcid, int gmid)
    {
        int index = -1;
        int i =0;
        for (i=0;i<cnpCount;i++)
        {
            if (cnPairs[i].gcid == gcid && cnPairs[i].gmid == gmid)
            {
                index = i;
                break;
            }
        }
        if (i == cnpCount)
        {
            cnPairs[i] = new CloneNonclonePair();
            cnPairs[i].gcid = gcid;
            cnPairs[i].gmid = gmid;
            index = i;
            cnpCount++;
        }
        return index;
    }
    
    
    public void getDifferentClassPairs (int cloneType)
    {        
        int i = 0, j=0, k=0, gcid1=0, gcid2=0, clsid1=0, clsid2=0, index=-1;
        
        int test = 0;
                
        for (i=1;i<cp.revisionCount;i++)
        {       
            System.out.println ("working on revision (getting different class pairs) = "+i);
            String [][] changedClones = da.getChangedClones(i, cloneType);

            //getting the pairs from this revision = i.
            for (j =0;changedClones[j][0] != null;j++)
            {
                gcid1 = Integer.parseInt(changedClones[j][0]);
                clsid1 = Integer.parseInt(changedClones[j][1]);
                for (k=j+1;changedClones[k][0] != null;k++)
                {
                    gcid2 = Integer.parseInt(changedClones[k][0]);
                    clsid2 = Integer.parseInt(changedClones[k][1]);
                    
                    if (clsid1 != clsid2)
                    {
                        index = includeDifferentClassPair (gcid1, gcid2);                        
                        
                        int n = dcPairs[index].n;
                        dcPairs[index].revisions[n] = i;
                        dcPairs[index].classid1[n] = clsid1;
                        dcPairs[index].classid2[n] = clsid2;

                        dcPairs[index].changes1[n] = Integer.parseInt(changedClones[j][2]);
                        dcPairs[index].changes2[n] = Integer.parseInt(changedClones[k][2]);
                        dcPairs[index].n++;                        
                    }
                }
            }            
            da.dispose ();
        }
        
        
        //marking the non-different class pairs.
        int nondcpCount = 0;
        for (i=0;i<dcpCount;i++)
        {
            int n = dcPairs[i].n;
            for (j=0;j<n;j++)
            {
                if ((dcPairs[i].classid1[j] == dcPairs[i].classid2[j]) && (dcPairs[i].classid1[j] > 0))
                {
                    dcPairs[i].dcp = 0;
                    nondcpCount++;
                    break;
                }
            }
        }
        
        System.out.println ("different class pair count = "+(dcpCount-nondcpCount));    
        
        //need to calculate the support and confidence of the non-spcp pairs.
    }    
    
    public int includeDifferentClassPair (int gcid1, int gcid2)
    {
        int index = differentClassPairExists(gcid1, gcid2);
        if (index == -1)
        { 
            dcPairs[dcpCount] =  new DifferentClassPair();
            dcPairs[dcpCount].gcid1 = gcid1;
            dcPairs[dcpCount].gcid2 = gcid2;
            index = dcpCount;
            dcpCount++;
        }                
        return index;
    }
    
    public int differentClassPairExists(int gcid1, int gcid2)
    {
        int index = -1;
        
        for (int i =0;i<dcpCount;i++)
        {
            if ((dcPairs[i].gcid1 == gcid1 && dcPairs[i].gcid2 == gcid2) || (dcPairs[i].gcid1 == gcid2 && dcPairs[i].gcid2 == gcid1))
            {
                index = i;
                break;
            }
        }
        
        return index;        
    }
    
    
    public void getSPCPClonePairs (int cloneType)
    {
        int i = 0;
                
        for (i=1;i<cp.revisionCount;i++)
        {            
            System.out.println ("working on revision (getting spcp clone pairs) = "+i);
            
            if (i == 36)
            {
                int a = 1;
            }
            
            String [][] pairs = da.getPotentialClonePairs(i, cloneType);
            int j =0;
            while (pairs[j][0] != null)
            {
                int gcid1 = Integer.parseInt(pairs[j][0]);
                int clsid1 = Integer.parseInt(pairs[j][1]);
                int change1 = Integer.parseInt(pairs[j][2]);
                
                int gcid2 = Integer.parseInt(pairs[j][3]);
                int clsid2 = Integer.parseInt(pairs[j][4]);
                int change2 = Integer.parseInt(pairs[j][5]);
                
                int index = includePair (gcid1, gcid2);                                 
                int n = potentialPairs[index].n;
                
                //the same pair in the same revision should not be included more than once.
                if (potentialPairs[index].revisions[n-1] == i) {continue;}
                
                potentialPairs[index].revisions[n] = i;
                potentialPairs[index].classid1[n] = clsid1;
                potentialPairs[index].classid2[n] = clsid2;

                potentialPairs[index].changes1[n] = change1;
                potentialPairs[index].changes2[n] = change2;
                potentialPairs[index].n++;                                            
                
                j++;
            }
        }
        
        //determine SPCP pairs.
        int spcpCount = 0;
        for (i=0;i<ppCount;i++)
        {
            int n = potentialPairs[i].n;
            for (int j=n-1;j>=0;j--)
            {
                if (potentialPairs[i].classid1[j] > 0 && potentialPairs[i].classid2[j] > 0)
                {
                    if (potentialPairs[i].classid1[j] == potentialPairs[i].classid2[j])
                    {
                        potentialPairs[i].spcp = 1;
                        spcpCount++;
                    }
                    break;
                }
            }             
        }
        
        System.out.println ("spcp Count = "+spcpCount);
        
        //show SPCP clone pairs.
        for (i=0;i<ppCount;i++)
        {
            if (potentialPairs[i].spcp == 0) {continue;}
            System.out.print ("\n\npair "+i+" ("+potentialPairs[i].gcid1 + " " + potentialPairs[i].gcid2+") \n");
            int n = potentialPairs[i].n;
                        
            for (int j=0;j<n;j++)
            {
                System.out.print ("\t"+potentialPairs[i].revisions[j]);
            }            
            System.out.print ("\n");
            for (int j=0;j<n;j++)
            {
                System.out.print ("\t"+potentialPairs[i].classid1[j]);
            }
            System.out.print ("\n");
            for (int j=0;j<n;j++)
            {
                System.out.print ("\t"+potentialPairs[i].classid2[j]);
            }            
            System.out.print ("\n");
            for (int j=0;j<n;j++)
            {
                System.out.print ("\t"+potentialPairs[i].changes1[j]);
            }            
            System.out.print ("\n");
            for (int j=0;j<n;j++)
            {
                System.out.print ("\t"+potentialPairs[i].changes2[j]);
            }                        
        }
    }
    
    public int includePair (int gcid1, int gcid2)
    {
        int index = pairExists(gcid1, gcid2);
        if (index == -1)
        { 
            potentialPairs[ppCount] =  new PotentialPair();
            potentialPairs[ppCount].gcid1 = gcid1;
            potentialPairs[ppCount].gcid2 = gcid2;
            index = ppCount;
            ppCount++;
        }        
        return index;
    }
    
    public void analyzePair (int revision, int index, int gcid1, int gcid2)
    {
        int clsid1 = getCloneClassID(revision, gcid1);
        int clsid2 = getCloneClassID(revision, gcid2);

        int n = potentialPairs[index].n;
        potentialPairs[index].revisions[n] = revision;
        potentialPairs[index].classid1[n] = clsid1;
        potentialPairs[index].classid2[n] = clsid2;

        int changes1 = getChangesToCloneFragment (revision, gcid1);
        int changes2 = getChangesToCloneFragment (revision, gcid2);

        potentialPairs[index].changes1[n] = changes1;
        potentialPairs[index].changes2[n] = changes2;
        potentialPairs[index].n++;            
    }
    
    public int pairExists (int gcid1, int gcid2)
    {
        int index = -1;
        
        for (int i =0;i<ppCount;i++)
        {
            if ((potentialPairs[i].gcid1 == gcid1 && potentialPairs[i].gcid2 == gcid2) || (potentialPairs[i].gcid1 == gcid2 && potentialPairs[i].gcid2 == gcid1))
            {
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    
    public String [][] getPotentialClonePairs (int revision)
    {
        String [][] result = new String [10000][2];
        int count = 0;
        
        String [] changedClones = getChangedCloneFragments(revision);
        
        for (int i =0;changedClones[i] != null;i++)
        {
            int gcid = Integer.parseInt(changedClones[i]);
            int clsid = getCloneClassID (revision, gcid);
            String clones = getClonesFromClass (revision, clsid).trim();
            
            int n = clones.split("[ ]+").length;
            for (int j=0;j<n;j++)
            {
                int id = Integer.parseInt(clones.split("[ ]+")[j]);
                if (id != gcid)
                {
                    result[count][0] = gcid+"";
                    result[count][1] = id+"";
                    count++;
                }
            }
        }                
        return result;
    }
    
    
    public int doesMethodExist (int revision, int gmid)
    {
        int result = 0;
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/methods/"+"methods_version_"+revision+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int id = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
                if (id == gmid)
                {
                    result = 1;
                    break;
                }
            }
            br.close();
            br = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in doesMethodExist. "+e);
        }        
        
        return result;
    }
    
    public int getChangesToMethod (int revision, int gmid)
    {
        int result = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/methods/"+"methods_version_"+revision+"_change.txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int id = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
                if (id == gmid)
                {
                    result = Integer.parseInt(cp.getAttributeValueFromString(str, an.methodChangeCount));
                    break;
                }
            }
            br.close();
            br = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangesToMethod. "+e);
        }
                
        return result;        
    }
    
    public int getChangesToCloneFragment (int revision, int gcid)
    {
        int result = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type123_clonedmethods_version_"+revision+"_change.txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
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
            br = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangesToCloneFragment. "+e);
        }
                
        return result;
    }
    
    
    public String getChangedNoncloneMethods (int revision)
    {
        String result = "";
        String changedMethods = getChangedMethods(revision).trim();
        String clonedMethods = " " + getClonedMethods (revision) + " ";
        
        int l = changedMethods.split("[ ]+").length;
        if (changedMethods.trim().length() == 0){ l = 0; }
        
        for (int i =0;i<l;i++)
        {
            String cm = changedMethods.split("[ ]+")[i].trim();
            if (!clonedMethods.contains(" "+cm+" "))
            {
                result = result + " " + cm + " ";
            }            
        }
                
        return result;
    }
    
    
    public String getClonedMethods (int revision)
    {
        String result = "";
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type123_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                String id = cp.getAttributeValueFromString(str, an.globalMethodID);
                result = result + " " + id + " ";
            }
            br.close();
            br = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in getClonedMethods. "+e);
        }
        return result;
    }
    
    public String getChangedMethods (int revision)
    {
        String result = "";
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/methods/"+"methods_version_"+revision+"_change.txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                String gmid = cp.getAttributeValueFromString(str, an.globalMethodID);
                result = result + " " + gmid + " ";
            }
            br.close();
            br = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangedMethods. "+e);
        }
        
        return result;        
    }
    

    public String [] getChangedCloneFragments (int revision)
    {
        String [] result = new String[1000];
        int n = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type123_clonedmethods_version_"+revision+"_change.txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                String gcid = cp.getAttributeValueFromString(str, an.globalCloneID);
                result[n] = gcid;
                n++;
            }
            br.close();
            br = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangedCloneFragments. "+e);
        }
        
        return result;
    }    
    
    public int getCloneClassID (int revision, int gcid)
    {
        int result = -1;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type123_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int id = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                int classid = Integer.parseInt (cp.getAttributeValueFromString(str, an.cloneClassID));
                
                if (gcid == id)
                {
                    result = classid;
                    break;
                }
            }
            br.close();
            br = null;
            System.gc();
        }
        catch (IOException e)
        {
            System.out.println ("error in getCloneClassID. "+e);
        }
        
        return result;
    }
    
    public String getClonesFromClass (int revision, int classid)
    {
        String result = "";
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type123_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int id = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                int clsid = Integer.parseInt (cp.getAttributeValueFromString(str, an.cloneClassID));
                
                if (classid == clsid)
                {
                    result = result + " " + id + " ";
                }
            }
            br.close();
            br = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in getCloneClassID. "+e);
        }
                
        
        return result ;
    }
    
}
