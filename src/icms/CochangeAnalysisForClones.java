/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 *
 * @author mani
 */

class FragmentPair
{
    int id1=0, id2=0;
    String type1 ="", type2="";
    String cochangeCommits = "";
    int sameClass = 0;
    String clonedMethodType1 = "";
    String clonedMethodType2 = "";
    String nonclonedType = "";
    int support = 0;
    float confidence1 = 0f;
    float confidence2 = 0f;
}


class FragmentPairs
{    
    CommonParameters cp = new CommonParameters ();
    FragmentPair [] fragmentPairs = new FragmentPair[5000000];    
    int pairCount = 0;
    CodeFragments cf = new CodeFragments ();
    int supportThreshold  = 0;
    
    public void updateSupportAndConfidence ()
    {
        System.out.println ("updating support and confidence.");
        
        for (int i =0;i<pairCount;i++)
        {
            fragmentPairs[i].support = fragmentPairs[i].cochangeCommits.trim().split("[ ]+").length;
            fragmentPairs[i].confidence1 = (float)fragmentPairs[i].support / cf.getSupport(fragmentPairs[i].id1, fragmentPairs[i].type1.trim());
            fragmentPairs[i].confidence2 = (float)fragmentPairs[i].support / cf.getSupport(fragmentPairs[i].id2, fragmentPairs[i].type2.trim());
        }
    }
    
    
    public void getStatistics ()
    {
        int [] clones = new int [100000];
        int cloneCount = 0;
        
        //counter1 is for non-cloned code, counter2 is for clones from other classes, counter3 is for clones in the same class.
        int counter1=0, counter2=0, counter3 = 0;
        
        
        for (int i =0;i<pairCount;i++)
        {
            if (fragmentPairs[i].support > 1)
            {                
                if(fragmentPairs[i].type2.equals ("noncloned"))
                {
                    counter1++;                    
                }
                else if (fragmentPairs[i].sameClass == 0)
                {
                    counter2 += 2;                    
                }
                else
                {
                    counter3 += 2;                    
                }
                
                if(fragmentPairs[i].type2.equals ("noncloned"))
                {
                    int j =0;
                    for (j=0;j<cloneCount;j++)
                    {
                        if (clones[j] == fragmentPairs[i].id1)
                        {
                            break;
                        }
                    }
                    if (j == cloneCount)
                    {
                        clones[j] = fragmentPairs[i].id1;
                        cloneCount++;
                    }                    
                }
                else
                {
                    //this is for id1
                    int j =0;
                    for (j=0;j<cloneCount;j++)
                    {
                        if (clones[j] == fragmentPairs[i].id1)
                        {
                            break;
                        }
                    }
                    if (j == cloneCount)
                    {
                        clones[j] = fragmentPairs[i].id1;
                        cloneCount++;
                    }                
                    
                    //this is for id2.
                    j =0;
                    for (j=0;j<cloneCount;j++)
                    {
                        if (clones[j] == fragmentPairs[i].id2)
                        {
                            break;
                        }
                    }
                    if (j == cloneCount)
                    {
                        clones[j] = fragmentPairs[i].id2;
                        cloneCount++;
                    }                                                                                
                }
            }
        }
        
        System.out.println ("Statistics implemented on 04 May, 2014");
        System.out.println ("\taverage count of rules with non-cloned code = "+ (float)counter1/cloneCount);
        System.out.println ("\taverage count of rules with other-cloned code = "+ (float)counter2/cloneCount);
        System.out.println ("\taverage count of rules with same-cloned code = "+ (float)counter3/cloneCount);
    }
    
    
    
    public void getSameMethodStatistics ()
    {
        int cat1same = 0, cat1diff = 0, cat2same = 0, cat2diff=0, cat3same=0, cat3diff=0;
        
        for (int i =0;i<pairCount;i++)
        {
            if (fragmentPairs[i].support > supportThreshold)
            {
                if(fragmentPairs[i].type2.equals ("noncloned"))
                {
                    int cid = fragmentPairs[i].id1;
                    int r = Integer.parseInt(fragmentPairs[i].cochangeCommits.trim().split("[ ]+")[0]);
                    int mid1 = cp.getContainerMethodID(cid, r, 123);
                    int mid2 = fragmentPairs[i].id2;
                    
                    if (mid1 == mid2){ cat3same++; }
                    else {cat3diff++;}
                }
                else if (fragmentPairs[i].sameClass == 0)
                {
                    int r = Integer.parseInt(fragmentPairs[i].cochangeCommits.trim().split("[ ]+")[0]);
                    int cid1 = fragmentPairs[i].id1;
                    int cid2 = fragmentPairs[i].id2;
                    int mid1 = cp.getContainerMethodID(cid1, r, 123);
                    int mid2 = cp.getContainerMethodID(cid2, r, 123);
                    
                    if (mid1 == mid2) {cat2same++;}
                    else {cat2diff++;}                    
                }
                else
                {
                    int r = Integer.parseInt(fragmentPairs[i].cochangeCommits.trim().split("[ ]+")[0]);
                    int cid1 = fragmentPairs[i].id1;
                    int cid2 = fragmentPairs[i].id2;
                    int mid1 = cp.getContainerMethodID(cid1, r, 123);
                    int mid2 = cp.getContainerMethodID(cid2, r, 123);
                    
                    if (mid1 == mid2) {cat1same++;}
                    else {cat1diff++;}                                        
                }
            }
        }
        
        System.out.println ("same or different method coupling.");
        System.out.println ("\tcat 1 same = "+(float)cat1same*100/(cat1same+cat1diff));
        System.out.println ("\tcat 1 diff = "+(float)cat1diff*100/(cat1same+cat1diff));
        System.out.println ("\tcat 2 same = "+(float)cat2same*100/(cat2same+cat2diff));
        System.out.println ("\tcat 2 diff = "+(float)cat2diff*100/(cat2same+cat2diff));
        System.out.println ("\tcat 3 same = "+(float)cat3same*100/(cat3same+cat3diff));
        System.out.println ("\tcat 3 diff = "+(float)cat3diff*100/(cat3same+cat3diff));
        
        System.out.println ("\n\n"+(float)cat1same*100/(cat1same+cat1diff)+"\t"
                + ""+(float)cat1diff*100/(cat1same+cat1diff)+"\t"
                + ""+(float)cat2same*100/(cat2same+cat2diff)+"\t"
                + ""+(float)cat2diff*100/(cat2same+cat2diff)+"\t"
                + ""+(float)cat3same*100/(cat3same+cat3diff)+"\t"
                + ""+(float)cat3diff*100/(cat3same+cat3diff)+"");
    }
    
    
    
    
    public int getTotalCloneCount ()
    {
        String clones = "";
        for (int i =0;i<pairCount;i++)
        {
            if (fragmentPairs[i].support > supportThreshold)
            {
                if(fragmentPairs[i].type2.equals ("noncloned"))
                {
                    int cid = fragmentPairs[i].id1;
                    if (!clones.contains (" " + cid + " "))
                    {
                        clones += " " + cid + " ";
                    }
                    
                }
                else
                {
                    int cid = fragmentPairs[i].id1;
                    if (!clones.contains (" " + cid + " "))
                    {
                        clones += " " + cid + " ";
                    }                    
                    
                    cid = fragmentPairs[i].id2;
                    if (!clones.contains (" " + cid + " "))
                    {
                        clones += " " + cid + " ";
                    }                                        
                }
            }
        }
        
        return clones.trim().split("[ ]+").length;
    }
    
    
    public void getStatisticsClonedNoncloned ()
    {
        int count = 0;
        int special_count = 0;
        int special_support = 0;
        
        int tsupport = 0;
        float tconfidence = 0f;
        
        int total_count = 0;
        
        String clones = "";
        String totalclones = "";
        
        for (int i =0;i<pairCount;i++)
        {
            if (fragmentPairs[i].support > supportThreshold)
            {                
                total_count++;
                if(fragmentPairs[i].type2.equals ("noncloned"))
                {                                    
                    int cid = fragmentPairs[i].id1;
                    if (!clones.contains (" " + cid + " "))
                    {
                        clones += " " + cid + " ";
                    }
                                        
                    count++;
                    tsupport += fragmentPairs[i].support;
                    
                    tconfidence += fragmentPairs[i].confidence1 + fragmentPairs[i].confidence2;
                    
                    if (fragmentPairs[i].confidence1 == 1 || fragmentPairs[i].confidence2 == 1)
                    {
                        special_support += fragmentPairs[i].support;
                        special_count++;
                    }
                }
            }
        }
        
        int clone_count = clones.trim().split("[ ]+").length;
        int total_clones = cf.count;
        int totalclonecount = getTotalCloneCount();
        
        System.out.println ("\n\nstatistics for cloned-noncloned rules");
        System.out.println ("\tcount of rules = "+count);
        System.out.println ("\t% of rules in this set = "+(float)count*100/total_count);
        System.out.println ("\t% of clones involved in this set = "+(float)clone_count*100/totalclonecount);
        System.out.println ("\taverage support = "+(float)tsupport/count);
        System.out.println ("\taverage confidence = "+(float)tconfidence*0.5/count);  
        
        System.out.println ("\taverage support (confidence = 1) = "+(float)special_support/special_count);
        System.out.println ("\trule count (confidence = 1) = " + special_count);
        System.out.println ("\trule % (confidence = 1) = " + (float)special_count*50/count);
    }
    
    public void getStatisticsClonedOthercloned ()
    {
        int count = 0;
        int tsupport = 0;
        float tconfidence = 0f;
        
        int special_count = 0;
        int special_support = 0;
        
        int total_count = 0;
        
        String clones = "";
        
        for (int i =0;i<pairCount;i++)
        {
            if (fragmentPairs[i].support > supportThreshold)
            {                                
                total_count++;
                if (fragmentPairs[i].type2.equals (fragmentPairs[i].type1) && fragmentPairs[i].sameClass == 0)
                {
                    int cid1 = fragmentPairs[i].id1;
                    int cid2 = fragmentPairs[i].id2;
                    if (!clones.contains (" " + cid1 + " "))
                    {
                        clones += " " + cid1 + " ";
                    }
                    if (!clones.contains (" " + cid2 + " "))
                    {
                        clones += " " + cid2 + " ";
                    }
                                        
                    
                    count++;
                    tsupport += fragmentPairs[i].support;
                    
                    tconfidence += fragmentPairs[i].confidence1 + fragmentPairs[i].confidence2;
                    
                    if (fragmentPairs[i].confidence1 == 1 || fragmentPairs[i].confidence2 == 1)
                    {
                        special_support += fragmentPairs[i].support;
                        special_count++;
                    }
                }
            }
        }
        
        int clone_count = clones.trim().split("[ ]+").length;
        int total_clones = cf.count;
        int totalclonecount = getTotalCloneCount();
        
        
        System.out.println ("\n\nstatistics for cloned-cloned-different-class rules");
        System.out.println ("\tcount of rules = "+count);
        System.out.println ("\t% of rules in this set = "+(float)count*100/total_count);
        System.out.println ("\t% of clones involved in this set = "+(float)clone_count*100/totalclonecount);
        System.out.println ("\taverage support = "+(float)tsupport/count);
        System.out.println ("\taverage confidence = "+(float)tconfidence*0.5/count); 
        
        System.out.println ("\taverage support (confidence = 1) = "+(float)special_support/special_count);
        System.out.println ("\trule count (confidence = 1) = " + special_count);
        System.out.println ("\trule % (confidence = 1) = " + (float)special_count*50/count);
        
    }        
    
    public void getStatisticsClonedSamecloned ()
    {
        int count = 0;
        int tsupport = 0;
        float tconfidence = 0f;
        
        int special_count = 0;
        int special_support = 0;  
        
        int total_count = 0;
        
        String clones = "";
        
        for (int i =0;i<pairCount;i++)
        {
            if (fragmentPairs[i].support > supportThreshold)
            {                
                total_count++;
                
                if (fragmentPairs[i].type2.equals (fragmentPairs[i].type1) && fragmentPairs[i].sameClass == 1)
                {                    
                    int cid1 = fragmentPairs[i].id1;
                    int cid2 = fragmentPairs[i].id2;

                    if (!clones.contains (" " + cid1 + " "))
                    {
                        clones += " " + cid1 + " ";
                    }
                    if (!clones.contains (" " + cid2 + " "))
                    {
                        clones += " " + cid2 + " ";
                    }                
                    
                    
                    count++;
                    tsupport += fragmentPairs[i].support;
                    
                    tconfidence += fragmentPairs[i].confidence1 + fragmentPairs[i].confidence2;
                    
                    if (fragmentPairs[i].confidence1 == 1 || fragmentPairs[i].confidence2 == 1)
                    {
                        special_support += fragmentPairs[i].support;
                        special_count++;
                    }
                }
            }
        }
        
        int clone_count = clones.trim().split("[ ]+").length;
        int total_clones = cf.count;
        int totalclonecount = getTotalCloneCount();
        
        
        System.out.println ("\n\nstatistics for cloned-cloned-same-class rules");
        System.out.println ("\tcount of rules = "+count);
        System.out.println ("\t% of rules in this set = "+(float)count*100/total_count);
        System.out.println ("\t% of clones involved in this set = "+(float)clone_count*100/totalclonecount);
        System.out.println ("\taverage support = "+(float)tsupport/count);
        System.out.println ("\taverage confidence = "+(float)tconfidence*0.5/count);  
        
        System.out.println ("\taverage support (confidence = 1) = "+(float)special_support/special_count);
        System.out.println ("\trule count (confidence = 1) = " + special_count);
        System.out.println ("\trule % (confidence = 1) = " + (float)special_count*50/count);
        
    }
    
    
    public void getClonePairCount ()
    {
        int sameCloned = 0;
        int otherCloned = 0;
        int nonCloned = 0;
        
        for (int i =0;i<pairCount;i++)
        {
            if (fragmentPairs[i].type2.equals (fragmentPairs[i].type1) && fragmentPairs[i].sameClass == 1)
            {
                sameCloned++;
            }
            else if (fragmentPairs[i].type2.equals (fragmentPairs[i].type1) && fragmentPairs[i].sameClass == 0)
            {
                otherCloned++;
            }
            else
            {
                nonCloned++;
            }            
        }        
        System.out.println ("count of pairs (clone + noncloned) = "+nonCloned);
        System.out.println ("count of pairs (clone + clone + same) = "+sameCloned);
        System.out.println ("count of pairs (clone + clone + other) = "+otherCloned);
    }
    
    
    public void updatePairs (FragmentPair fp)
    {
        int i =0;
        for (i=0;i<pairCount;i++)
        {
            if (fragmentPairs[i].id1 == fp.id1 && fragmentPairs[i].id2 == fp.id2)
            {
                if (fragmentPairs[i].type1.equals (fp.type1) && fragmentPairs[i].type2.equals (fp.type2))
                {
                    if (fragmentPairs[i].sameClass == fp.sameClass)
                    {
                        String commit = fp.cochangeCommits.trim();
                        if (!fragmentPairs[i].cochangeCommits.contains (" " + commit+" "))
                        {
                            fragmentPairs[i].cochangeCommits += " " + fp.cochangeCommits + " ";
                        
                            fragmentPairs[i].nonclonedType += " " + fp.nonclonedType + " ";
                            fragmentPairs[i].clonedMethodType1 += " " + fp.clonedMethodType1 + " ";
                            fragmentPairs[i].clonedMethodType2 += " " + fp.clonedMethodType2 + " ";
                        }
                        break;
                    }
                }
            }
            
            if (fragmentPairs[i].id1 == fp.id2 && fragmentPairs[i].id2 == fp.id1)
            {
                if (fragmentPairs[i].type1.equals (fp.type2) && fragmentPairs[i].type2.equals (fp.type1))
                {
                    if (fragmentPairs[i].sameClass == fp.sameClass)
                    {
                        String commit = fp.cochangeCommits.trim();
                        if (!fragmentPairs[i].cochangeCommits.contains (" " + commit+" "))
                        {
                            fragmentPairs[i].cochangeCommits += " " + fp.cochangeCommits + " ";
                        
                            fragmentPairs[i].nonclonedType += " " + fp.nonclonedType + " ";
                            fragmentPairs[i].clonedMethodType1 += " " + fp.clonedMethodType1 + " ";
                            fragmentPairs[i].clonedMethodType2 += " " + fp.clonedMethodType2 + " ";
                        }
                        break;
                    }
                }
            }            
        }
        
        if (i == pairCount)
        {
            fragmentPairs[i] = new FragmentPair ();
            fragmentPairs[i].id1 = fp.id1;
            fragmentPairs[i].id2 = fp.id2;
            fragmentPairs[i].type1 = fp.type1;
            fragmentPairs[i].type2 = fp.type2;
            fragmentPairs[i].sameClass = fp.sameClass;
            fragmentPairs[i].nonclonedType = fp.nonclonedType;
            fragmentPairs[i].clonedMethodType1 = fp.clonedMethodType1;
            fragmentPairs[i].clonedMethodType2 = fp.clonedMethodType2;
            fragmentPairs[i].cochangeCommits = " " + fp.cochangeCommits + " ";
            
            pairCount++;
        }
    }
    
    public String getCloneInfo (int cloneID, int revision, int cloneType)
    {                        
        String file = cp.getCloneFile(cloneID, revision, cloneType);                
     
        int containerMethodID = cp.getContainerMethodID(cloneID, revision, cloneType);
        String containerMethodName = cp.getMethodName (containerMethodID,revision);        
        int methodStart = cp.getMethodStartingLine(containerMethodID, revision);
        int methodEnd = cp.getMethodEndingLine(containerMethodID, revision);
        
        int cloneStart = cp.getCloneStartingLine(cloneID, revision, cloneType);
        int cloneEnd = cp.getCloneEndingLine(cloneID, revision, cloneType);
        
        String rstring = "type = 'clonedfragment' cloneid='"+cloneID+"'"
                + " clonestartline_revision_"+revision+"='"+cloneStart+"'"
                + " cloneendline_revision_"+revision+"='"+cloneEnd+"'"
                + " containermethod='"+containerMethodName+"'"
                + " containermethodid='"+containerMethodID+"'"
                + " containermethodstartline_revision_"+revision+"='"+methodStart+"'"
                + " containermethodendline_revision_"+revision+"='"+methodEnd+"'"
                + " file='"+file+"'";
        
        return rstring;
    }
    
    public String getMethodInfo (int methodID, int revision)
    {
        String file = cp.getMethodFile(methodID, revision);                     
        int containerMethodID = methodID;
        String containerMethodName = cp.getMethodName (containerMethodID,revision);        
        int methodStart = cp.getMethodStartingLine(containerMethodID, revision);
        int methodEnd = cp.getMethodEndingLine(containerMethodID, revision);
                
        String rstring = "type = 'nonclonedfragment' "
                + " containermethod='"+containerMethodName+"'"
                + " containermethodid='"+containerMethodID+"'"
                + " containermethodstartline_revision_"+revision+"='"+methodStart+"'"
                + " containermethodendline_revision_"+revision+"='"+methodEnd+"'"
                + " file='"+file+"'";
        
        return rstring;
    }
    
    
    
    public void writePairs () throws Exception
    {
        BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system + "/" + "fragmentpairs_new.txt"));
        BufferedWriter writer1 = new BufferedWriter (new FileWriter (cp.subject_system + "/" + "fragmentpairs_new_different_class.txt"));
        BufferedWriter writer2 = new BufferedWriter (new FileWriter (cp.subject_system + "/" + "fragmentpairs_new_cloned_noncloned.txt"));
        BufferedWriter writer3 = new BufferedWriter (new FileWriter (cp.subject_system + "/" + "fragmentpairs_XML.xml"));
        //BufferedWriter writer2 = new BufferedWriter (new FileWriter (cp.subject_system + "/" + "fragmentpairs_special.txt"));
        
        writer3.write ("<pairs>");
        for (int i =0;i<pairCount;i++)
        {
            
            int cochangecommit1 = Integer.parseInt(fragmentPairs[i].cochangeCommits.trim().split("[ ]+")[0]);
            String fragment1info = "";
            String fragment2info = "";
            
            if (fragmentPairs[i].type1.equals ("cloned"))
            {
                fragment1info = getCloneInfo(fragmentPairs[i].id1, cochangecommit1, 123);
            }
            else
            {
                fragment1info = getMethodInfo (fragmentPairs[i].id1, cochangecommit1);
            }
            
            if (fragmentPairs[i].type2.equals ("cloned"))
            {
                fragment2info = getCloneInfo(fragmentPairs[i].id2, cochangecommit1, 123);
            }
            else
            {
                fragment2info = getMethodInfo(fragmentPairs[i].id2, cochangecommit1);
            }
            
            
            writer3.write ("<pair support = '"+fragmentPairs[i].cochangeCommits.trim().split("[ ]+").length+"' cochangecommits = '"+fragmentPairs[i].cochangeCommits.trim()+"' sameclass = '"+fragmentPairs[i].sameClass+"'>"
                    + "<fragment1 "+fragment1info+" />"
                    + "<fragment2 "+fragment2info+" />"
                    + "</pair>");
            
            
            writer.write ("\n");
            String pair = "id1 = "+fragmentPairs[i].id1 + " : "
                    + "id2 = "+fragmentPairs[i].id2 + " : "
                    + "type1 = "+fragmentPairs[i].type1 + " : "
                    + "type2 = "+fragmentPairs[i].type2 + " : "
                    + "sameclass = "+fragmentPairs[i].sameClass + " : "
                    //+ "clonedmethodtype1 = "+fragmentPairs[i].clonedMethodType1 + " : "
                    //+ "clonedmethodtype2 = "+fragmentPairs[i].clonedMethodType2 + " : "                    
                    + "cochangecommits = "+fragmentPairs[i].cochangeCommits + " : "                    
                    + "nonclonedmethodtype = "+fragmentPairs[i].nonclonedType + " : "
                    + "support = "+fragmentPairs[i].cochangeCommits.trim().split("[ ]+").length + " : ";
            
            writer.write (pair);
            
            if (fragmentPairs[i].type1.equals (fragmentPairs[i].type2) && fragmentPairs[i].sameClass==0)
            {
                writer1.write ("\n");
                writer1.write (pair);
            }
            if (!fragmentPairs[i].type1.equals (fragmentPairs[i].type2))
            {
                writer2.write ("\n");
                writer2.write (pair);
            }                        
        }
        writer3.write ("</pairs>");
        writer.close ();
        writer1.close();
        writer2.close();
        writer3.close();
    }
    
    public void sortPairs () throws Exception
    {
        for (int i =0;i<pairCount-1;i++)
        {
            int hi = i;
            int s1 = fragmentPairs[i].cochangeCommits.trim().split("[ ]+").length;
            for (int j=i+1;j<pairCount;j++)
            {
                int s2 = fragmentPairs[j].cochangeCommits.trim().split("[ ]+").length;
                if (s2 > s1)
                {
                    s1 = s2;
                    hi = j;
                }
            }
            FragmentPair fp = new FragmentPair ();
            FragmentPair fp1 = new FragmentPair ();
            fp = fragmentPairs[i];
            fp1 = fragmentPairs[hi];
            fragmentPairs[i] = fp1;
            fragmentPairs[hi] = fp;
        }
    }
    
    public void loadPairs () throws Exception
    {
        BufferedReader r = new BufferedReader (new InputStreamReader ( new FileInputStream (cp.subject_system + "/" + "fragmentpairs.txt")));
        String str = "";
        
        while ((str = r.readLine())!= null)
        {
            if (str.trim().length() == 0) {continue;}
            fragmentPairs[pairCount] = new FragmentPair ();
            
            fragmentPairs[pairCount].id1 = Integer.parseInt(str.split("[:]")[0].trim());
            fragmentPairs[pairCount].id2 = Integer.parseInt(str.split("[:]")[1].trim());
            fragmentPairs[pairCount].type1 = str.split("[:]")[2].trim();
            fragmentPairs[pairCount].type2 = str.split("[:]")[3].trim();
            fragmentPairs[pairCount].sameClass = Integer.parseInt(str.split("[:]")[4].trim());
            fragmentPairs[pairCount].nonclonedType = str.split("[:]")[5];
            fragmentPairs[pairCount].cochangeCommits = str.split("[:]")[6];
            
            pairCount++;
        }
    }
    
    
}



class CochangeDetectionAndPrediction
{
    CommonParameters cp = new CommonParameters ();
    AttributeNames an = new AttributeNames ();
    FragmentPairs fpairs = new FragmentPairs ();
    CodeFragments cfragments = new CodeFragments ();
    ChangeAnalysis ca = new ChangeAnalysis ();
    int cloneType = 123;
    
    
    //Shared Variables.
    int [] changedClones = new int[1000];
    int [] changedClasses = new int [1000];
    String [] clonedMethodTypes = new String [1000];
    int [] containerMethods = new int[1000];
    int [] cloneChangeCounts = new int [1000];
    int ccCount = 0;
    
    
    //shared variables.
    int [] changedNonclones = new int[1000];
    String [] changedNoncloneTypes = new String [1000];
    int ncCount = 0;
    
    
    //prediction purpose
    int thresholdValue = 0;
    int startCommit = 1;
    
    //checking purpose.
    int fault1=0, fault2=0, correct=0;
    
    
    
    
    public void detectCochange () throws Exception
    {
        for (int r = startCommit; r < cp.revisionCount; r++)
        {
            System.out.println ("working on revision = "+r);
            
            if (isCommitEligible(r) == 0) {System.out.println ("commit "+r+" is not eligible.");continue;}
            
            
            
            //determining the clones that received some changes.
            ccCount = 0; //changed clone count.            
            getChangedClones (r);
            
            
            //determining the non-cloned code fragments that received some changes.
            ncCount = 0;
            getChangedNonclones (r);
            
            
            //pairs of changed cloned fragments.
            for (int i =0;i<ccCount-1;i++)
            {
                for (int j =i+1;j<ccCount;j++)
                {
                    FragmentPair fp = new FragmentPair ();
                    fp.id1 = changedClones[i];
                    fp.id2 = changedClones[j];  
                    fp.type1 = "cloned";
                    fp.type2 = "cloned";
                    
                    if (changedClasses[i] == changedClasses[j])
                    {
                        fp.sameClass = 1;
                    }
                    fp.cochangeCommits = " "+r+" ";
                    fp.clonedMethodType1 = clonedMethodTypes[i];
                    fp.clonedMethodType2 = clonedMethodTypes[j];
                    fpairs.updatePairs(fp);
                }
            }
            
            //pairs of changed cloned and non-cloned fragments.
            for (int i =0;i<ccCount;i++)
            {
                for (int j =0;j<ncCount;j++)
                {
                    FragmentPair fp = new FragmentPair ();
                    fp.id1 = changedClones[i];
                    fp.id2 = changedNonclones[j];                                        
                    fp.type1 = "cloned";
                    fp.type2 = "noncloned";
                    fp.nonclonedType = changedNoncloneTypes[j];
                    fp.clonedMethodType1 = clonedMethodTypes[i];
                    fp.cochangeCommits = " "+r+" ";
                    fpairs.updatePairs(fp);
                }
            }
        
            /*System.out.println ("fault 1 (actual change 0, but I considered them changed) = " + fault1);
            System.out.println ("fault 2 (actually changed, but I considered them not changed) = " + fault2);
            System.out.println ("correct = "+correct);

            System.out.println ("% of fault1 = "+(float)fault1*100/(fault1+fault2+correct));
            System.out.println ("% of fault2 = "+(float)fault2*100/(fault1+fault2+correct));
            System.out.println ("% of correct = "+(float)correct*100/(fault1+fault2+correct));*/         
        }
    }
    
    
    
    public void getChangedClones (int commit)  throws Exception          
    {
        //determining the clones that received some changes.
        BufferedReader br1 = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/clonedmethods/type123_clonedmethods_version_"+commit+"_change.txt")));
        String str = "";
        while ((str = br1.readLine())!= null)
        {
            if (str.trim().length() == 0){continue;}
            
            String sgcid = cp.getAttributeValueFromString(str, an.globalCloneID);
            if (sgcid.trim().length() == 0) {continue;}
            
            int gcid = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalCloneID));
            int changeCount = Integer.parseInt(cp.getAttributeValueFromString(str, an.actualCloneChangeCount));
                        
            if (changeCount > 0)
            {                
                int clsid = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneClassID));
                changedClones[ccCount] = gcid;
                changedClasses[ccCount] = clsid;
                cloneChangeCounts[ccCount] = changeCount;

                int msline = Integer.parseInt(cp.getAttributeValueFromString(str, an.startingLine));
                int meline = Integer.parseInt(cp.getAttributeValueFromString(str, an.endingLine));
                int csline = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneStartingLine));
                int celine = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneEndingLine));

                if (meline-msline - celine + csline < 2)
                {  
                    clonedMethodTypes[ccCount] = "fully-cloned";
                }
                else
                {
                    clonedMethodTypes[ccCount] = "partially-cloned";
                }                                                            
                containerMethods[ccCount] = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));

                ccCount++;

                cfragments.updateCodeFragments(gcid, "cloned", commit);
            }                                
        }        
    }
    
    public void getChangedNonclones (int commit) throws Exception
    {
        BufferedReader br2 = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/methods/methods_version_"+commit+"_change.txt")));
        String str = "";
        while ((str = br2.readLine())!= null)
        {
            if (str.trim().length() == 0){continue;}
            
            String sgmid = cp.getAttributeValueFromString(str, an.globalMethodID);
            if (sgmid.trim().length() == 0) {continue;}            
            
            int gmid = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
            int changeCount = Integer.parseInt(cp.getAttributeValueFromString(str, an.actualMethodChangeCount));
            
            if (changeCount > 0)
            {
                int ccInClones = 0;
                
                for (int i =0;i<ccCount;i++)
                {
                    if (containerMethods[i] == gmid)
                    {
                        ccInClones+= cloneChangeCounts[i];
                    }
                }

                if (changeCount > ccInClones)
                {
                    changedNonclones[ncCount] = gmid;
                    String mtype = getMethodTypeNew (gmid, commit);
                    changedNoncloneTypes[ncCount] = mtype;
                    ncCount++;
                    
                    cfragments.updateCodeFragments(gmid, "noncloned", commit);
                }                
            }                
        }                                
    }
    
    
    public int changeCount (int id, String type, int commit)
    {
        int count = 0;
        for (int i =0;i<cfragments.count;i++)
        {
            if (cfragments.ids[i] == id && cfragments.types[i].equals (type))
            {
                String commits = cfragments.commits[i].trim();
                int n = commits.split("[ ]+").length;
                for (int j =0;j<n;j++)
                {
                    int c = Integer.parseInt(commits.split("[ ]+")[j].trim());
                    if (c < commit)
                    {
                        count++;
                    }
                }
                break;
            }
        }
        
        return count;
    }
        
    public void predictCochange () throws Exception
    {
        int count_tncc = 0;
        int count_pncc = 0;
        int count_cp_ncc = 0;

        int count_tocc = 0;
        int count_pocc = 0;
        int count_cp_occ = 0;

        int count_tscc = 0;
        int count_pscc = 0;
        int count_cp_scc = 0;

        int count_tcc = 0;
        int count_pcc = 0;
        int count_cp_cc = 0;    
        
        
        for (int r=startCommit;r<cp.revisionCount;r++)
        {
            System.out.println ("prediction in revision = "+r);            
            int isEligible = isCommitEligible(r);
            if (isEligible == 0) 
            {
                System.out.println ("commit "+r+" is not eligible.");
                continue;
            }            
            
            
            ccCount = 0; //changed clone count.
            getChangedClones (r);
            
            
            ncCount = 0; //changed noncloned count.
            getChangedNonclones (r);
            
            
            
            for (int i =0;i<ccCount;i++)
            {
                int clone = changedClones[i];
                
                if (changeCount(clone,"cloned", r) <= thresholdValue)
                { 
                    System.out.println ("\t\t\t\t not analyzed.");
                    continue; 
                }
                System.out.println ("\t\t\t\t analyzed.");
                

                //prediction of non-cloned co-change candidates.
                String tncc = "", pncc="", cp_ncc=""; //tncc = true non-cloned co-change canidates, pncc = predicted non-cloned co-change candidates, cp_ncc = correctly predicted non-cloned co-change candidates.
                for (int j =0;j<ncCount;j++)
                {
                    tncc += " " + changedNonclones[j] + " ";
                }      
                String temp = "";
                temp = predictNonclones (clone, r);
                pncc = temp.trim(); //pncc = predicted noncloned co-change candidates
                cp_ncc = getCommons(tncc, pncc); //cp_ncc = correctly predicted non-cloned co-change candidates

                if (tncc.trim().length() > 0){count_tncc += tncc.trim().split("[ ]+").length;}
                if (pncc.trim().length() > 0){count_pncc += pncc.trim().split("[ ]+").length;}
                if (cp_ncc.trim().length() > 0){count_cp_ncc += cp_ncc.trim().split("[ ]+").length;}


                // prediction of other cloned co-change candidates.
                String tocc="", pocc="", cp_occ=""; // tocc = true other cloned co-change candidates.
                for (int j =0;j<ccCount;j++)
                {
                    if (changedClasses[j] != changedClasses[i])
                    {
                        tocc += " " + changedClones[j] + " ";
                    }
                }                                                             
                pocc = predictOtherclones(clone, r); //pocc = predicted other cloned co-change candidates.
                cp_occ = getCommons(tocc, pocc); // cp_occ = correctly predicted other cloned co-change candidates

                if (tocc.trim().length() > 0){count_tocc += tocc.trim().split("[ ]+").length;}
                if (pocc.trim().length() > 0){count_pocc += pocc.trim().split("[ ]+").length;}
                if (cp_occ.trim().length() > 0){count_cp_occ += cp_occ.trim().split("[ ]+").length;}


                //prediction of same clone co-change candidates.
                String tscc="", pscc="", cp_scc="";
                for (int j =0;j<ccCount;j++)
                {
                    if (changedClasses[j] == changedClasses[i] && i != j)
                    {
                        tscc += " " + changedClones[j] + " ";
                    }
                }   
                pscc = predictSameclones(clone, r); //pscc = predicted same cloned co-change candidates.
                cp_scc = getCommons(tscc, pscc); // cp_scc = correctly predicted same cloned co-change candidates

                if (tscc.trim().length() > 0){count_tscc += tscc.trim().split("[ ]+").length;}
                if (pscc.trim().length() > 0){count_pscc += pscc.trim().split("[ ]+").length;}
                if (cp_scc.trim().length() > 0){count_cp_scc += cp_scc.trim().split("[ ]+").length;}


                //prediction of all co-change candidates.
                String tcc = getUnion(tncc, tocc, tscc);
                String pcc = getUnion(pncc, pocc, pscc);
                String cp_cc = getCommons (tcc, pcc);


                if (tcc.trim().length() > 0){count_tcc += tcc.trim().split("[ ]+").length;}
                if (pcc.trim().length() > 0){count_pcc += pcc.trim().split("[ ]+").length;}
                if (cp_cc.trim().length() > 0){count_cp_cc += cp_cc.trim().split("[ ]+").length;}
            }            
        }
        System.out.println ("\n\nCo-change Threshold = "+thresholdValue+"\n");
        System.out.println ("\nprediction of non-cloned co-change candidates");
        System.out.println ("\t\ttrue = "+count_tncc + " predicted = "+count_pncc+ " correctly predicted = "+count_cp_ncc);
        System.out.println ("\t\trecall in predicting non-cloned co-change candidates = "+(float)count_cp_ncc*100/count_tncc);
        System.out.println ("\t\tprecision in predicting non-cloned co-change candidates = "+(float)count_cp_ncc*100/count_pncc);
        
        System.out.println ("\nprediction of other-cloned co-change candidates");
        System.out.println ("\t\ttrue = "+count_tocc + " predicted = "+count_pocc+ " correctly predicted = "+count_cp_occ);
        System.out.println ("\t\trecall in predicting other-cloned co-change candidates = "+(float)count_cp_occ*100/count_tocc);
        System.out.println ("\t\tprecision in predicting other-cloned co-change candidates = "+(float)count_cp_occ*100/count_pocc);        

        System.out.println ("\nprediction of same-cloned co-change candidates");
        System.out.println ("\t\ttrue = "+count_tscc + " predicted = "+count_pscc+ " correctly predicted = "+count_cp_scc);
        System.out.println ("\t\trecall in predicting same-cloned co-change candidates = "+(float)count_cp_scc*100/count_tscc);
        System.out.println ("\t\tprecision in predicting same-cloned co-change candidates = "+(float)count_cp_scc*100/count_pscc);        
        
        System.out.println ("\nprediction of all co-change candidates, "
                + "\n\t\ttrue = "+count_tcc+", predicted = "+count_pcc+", correctly predicted = "+count_cp_cc+" ");
        System.out.println ("\t\trecall in predicting all co-change candidates = "+(float)count_cp_cc*100/count_tcc);
        System.out.println ("\t\tprecision in predicting all co-change candidates = "+(float)count_cp_cc*100/count_pcc);                
    }
    
    public String getCommons (String ms1, String ms2)
    {
        String result = "";
        
        String mstring1 = ms1.trim();
        String mstring2 = " " + ms2 + " ";
        int ln = mstring1.split("[ ]+").length;
        
        for (int i =0;i<ln;i++)
        {
            String m = mstring1.split("[ ]+")[i].trim();
            if (mstring2.contains(" " + m + " "))
            {
                result += " " + m + " ";
            }
        }
        
        return result;
    }
    
    public String getUnion (String str1, String str2, String str3)
    {
        String result = " " + str1 + " ";
         
        for (int i =0;i<str2.trim().split("[ ]+").length;i++)
        {
            String m = str2.trim().split("[ ]+")[i].trim();
            if (!result.contains (" " + m + " "))
            {
                result += " " + m + " ";
            }
        }

        for (int i =0;i<str3.trim().split("[ ]+").length;i++)
        {
            String m = str3.trim().split("[ ]+")[i].trim();
            if (!result.contains (" " + m + " "))
            {
                result += " " + m + " ";
            }
        }
        
        return result;        
    }                         
    
    
    public String predictNonclones (int cloneID, int commit)
    {
        String nonclones = "";
                
        for (int i =0;i<fpairs.pairCount;i++)
        {
            int lcc = 0; //lower commit count
            int l = fpairs.fragmentPairs[i].cochangeCommits.trim().split("[ ]+").length;
            int j =0;
            for (j=0;j<l;j++)
            {
                int c = Integer.parseInt(fpairs.fragmentPairs[i].cochangeCommits.trim().split("[ ]+")[j].trim());
                if (c < commit) {lcc++;}
            }
            
            if (lcc > thresholdValue && fpairs.fragmentPairs[i].nonclonedType.trim().length () > 0 && fpairs.fragmentPairs[i].id1 == cloneID)
            {
                if (!nonclones.contains (" " + fpairs.fragmentPairs[i].id2 + " "))
                {
                    nonclones += " " + fpairs.fragmentPairs[i].id2 + " ";
                }
            }
        }
        return nonclones;
    }
    
    public String predictSameclones (int cloneID, int commit)
    {
        String sameclones = "";
                
        for (int i =0;i<fpairs.pairCount;i++)
        {
            int lcc = 0; //lower commit count
            int l = fpairs.fragmentPairs[i].cochangeCommits.trim().split("[ ]+").length;
            int j =0;
            for (j=0;j<l;j++)
            {
                int c = Integer.parseInt(fpairs.fragmentPairs[i].cochangeCommits.trim().split("[ ]+")[j].trim());
                if (c < commit) {lcc++;}
            }
            

            
            if (lcc > thresholdValue && fpairs.fragmentPairs[i].type1.equals(fpairs.fragmentPairs[i].type2) && (fpairs.fragmentPairs[i].id1 == cloneID || fpairs.fragmentPairs[i].id2 == cloneID))
            {
                if (fpairs.fragmentPairs[i].sameClass == 1)
                {
                    if (fpairs.fragmentPairs[i].id1 == cloneID)
                    {
                        if (!sameclones.contains (" "+fpairs.fragmentPairs[i].id2+" "))
                        {
                            sameclones += " "+ fpairs.fragmentPairs[i].id2 + " ";
                        }
                    }
                    else
                    {
                        if (!sameclones.contains (" "+fpairs.fragmentPairs[i].id1+" "))
                        {
                            sameclones += " "+ fpairs.fragmentPairs[i].id1 + " ";
                        }                        
                    }
                }
            }
        }
        return sameclones;        
    }
    
    public String predictOtherclones (int cloneID, int commit)
    {
        String otherclones = "";
                
        for (int i =0;i<fpairs.pairCount;i++)
        {
            int lcc = 0; //lower commit count
            int l = fpairs.fragmentPairs[i].cochangeCommits.trim().split("[ ]+").length;
            int j =0;
            for (j=0;j<l;j++)
            {
                int c = Integer.parseInt(fpairs.fragmentPairs[i].cochangeCommits.trim().split("[ ]+")[j].trim());
                if (c < commit) {lcc++;}
            }
            
            if (lcc > thresholdValue && fpairs.fragmentPairs[i].type1.equals(fpairs.fragmentPairs[i].type2) && (fpairs.fragmentPairs[i].id1 == cloneID || fpairs.fragmentPairs[i].id2 == cloneID))
            {
                if (fpairs.fragmentPairs[i].sameClass == 0)
                {
                    if (fpairs.fragmentPairs[i].id1 == cloneID)
                    {
                        if (!otherclones.contains (" "+fpairs.fragmentPairs[i].id2+" "))
                        {
                            otherclones += " "+ fpairs.fragmentPairs[i].id2 + " ";
                        }
                    }
                    else
                    {
                        if (!otherclones.contains (" "+fpairs.fragmentPairs[i].id1+" "))
                        {
                            otherclones += " "+ fpairs.fragmentPairs[i].id1 + " ";
                        }                        
                    }
                }
            }
        }
        return otherclones;        
    }
    
    
    public int isCommitEligible (int r) throws Exception
    {
        String files = "";
        BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/methods/methods_version_"+r+"_change.txt")));
        String str = "";
        int mccount = 0;
        
        while ((str = br.readLine())!= null)
        {
            if (str.trim().length() == 0){continue;}
            String file = "!"+cp.getAttributeValueFromString(str, an.filePath)+"!";
            int ccount = Integer.parseInt(cp.getAttributeValueFromString(str, an.actualMethodChangeCount));
            
            if (ccount > 0)
            {
                mccount++;
            }
            
            if (!files.contains(file) && ccount > 0)
            {
                files += file+",";
            }
        }        
        
        System.out.println ("number of changed methods = "+mccount);
        
        if (files.trim().split("[,]+").length-1 > 10)
        {
            return 0;
        }
        /*if (mccount > 10)
        {
            return 0;
        }*/
        return 1;
    }
    
    
//    public int determineChangeCount1 (int method, int revision) throws Exception
//    {
//        int cCount = 0;
//        BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/methods/methods_version_"+revision+".txt")));
//        String str = "";
//        
//        while ((str = br.readLine())!= null)
//        {
//            if (str.trim().length() == 0){continue;}
//            int m = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
//            if (m == method)
//            {
//                cCount = Integer.parseInt(cp.getAttributeValueFromString(str, an.changeCount));
//                return cCount;
//            }
//        }
//        
//        return cCount;
//    }    
    
    public String getMethodTypeNew (int method, int revision) throws Exception
    {        
        String methodType = "";
        int got = 0;
        
        BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
        String str = "";
        
        while ((str = br.readLine())!= null)
        {
            if (str.trim().length() == 0){continue;}
            int m = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
            if (m == method)
            {
                got = 1;
                int scline = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneStartingLine));
                int ecline = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneEndingLine));
                int sline = Integer.parseInt(cp.getAttributeValueFromString(str, an.startingLine));
                int eline = Integer.parseInt(cp.getAttributeValueFromString(str, an.endingLine));                                
                
                if (eline-sline - ecline + scline < 2)
                {
                    methodType = "fully-cloned";
                }
                else
                {
                    methodType = "partially-cloned";
                }
            }
        }
        if (got == 0)
        {
            methodType = "fully-noncloned";
        }
        return methodType;
    }
    
    
    
    
//    public String getMethodType (int method, int revision) throws Exception
//    {        
//        String methodType = "";
//        String cloneChanged = "";
//        String nonclonedChanged = "0";
//        //int cCount = determineChangeCount(method, revision);
//        int total_ccCount = 0;
//        int got = 0;
//        
//        BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
//        String str = "";
//        
//        while ((str = br.readLine())!= null)
//        {
//            if (str.trim().length() == 0){continue;}
//            int m = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
//            if (m == method)
//            {
//                got = 1;
//                int scline = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneStartingLine));
//                int ecline = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneEndingLine));
//                int sline = Integer.parseInt(cp.getAttributeValueFromString(str, an.startingLine));
//                int eline = Integer.parseInt(cp.getAttributeValueFromString(str, an.endingLine));                                
//                int ccCount = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneChangeCount));                
//                int cClass = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneClassID));
//                
//                total_ccCount += ccCount;
//                
//                if (eline-sline - ecline + scline < 2)
//                {
//                    methodType = "fully-cloned";
//                }
//                else
//                {
//                    methodType = "partially-cloned";
//                }
//                
//                if (ccCount > 0)
//                {
//                    cloneChanged += " " + cClass + " ";                    
//                }
//            }
//        }
//        if (got == 0)
//        {
//            methodType = "fully-noncloned";
//        }
//        if (cCount > total_ccCount)
//        {
//            nonclonedChanged = "1";
//        }
//        return methodType+";"+cloneChanged+";"+nonclonedChanged;
//    }
    
}


public class CochangeAnalysisForClones {
    
}


class CodeFragments
{
    int [] ids = new int [500000];
    String [] types = new String [500000];
    String [] commits = new String [500000];
    
    int count = 0;
    
    
    public void updateCodeFragments (int id, String type, int commit)
    {
        int i =0;
        for (i=0;i<count;i++)
        {
            if (ids[i] == id && types[i].equals (type))
            {
                if (!commits[i].contains (" "+commit+" "))
                {
                    commits[i] += " " + commit + " ";
                }
                break;
            }
        }
        if (i == count)
        {
            ids[i] = id;
            types[i] = type;
            commits[i] = " " + commit + " ";
            count++;
        }
    }
    
    public int getSupport (int id, String type)
    {
        for (int i =0;i<count;i++)
        {
            if (id == ids[i] && type.equals (types[i]))
            {
                return commits[i].trim().split("[ ]+").length;
            }
        }
        return 0;
    }
    
}



