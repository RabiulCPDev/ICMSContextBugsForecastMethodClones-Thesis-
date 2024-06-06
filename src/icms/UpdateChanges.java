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
public class UpdateChanges {
    
    CommonParameters cp = new CommonParameters ();
    AttributeNames an = new AttributeNames ();
    ChangeAnalysis ca = new ChangeAnalysis ();
    CommonMethods cm = new CommonMethods ();
    
    
    public void fixErrors ()
    {
        
    }
    
    public void detectChangesInClones (int cloneType)
    {
        try
        {
            for (int i =1;i<cp.revisionCount;i++)
            {
                System.out.println ("detecting changes in type "+cloneType+" clones. revision = "+i);                
                updateChangesInClones (i, cloneType);                
            }
        }
        catch (Exception e)
        {
            
        }        
    }
    
    public void detectChangesInMethods ()
    {
        try
        {
            for (int i =1;i<cp.revisionCount;i++)
            {
                System.out.println ("detecting changes in methods. revision = "+i);
                updateChangesInMethods (i);
            }
        }
        catch (Exception e)
        {
            
        }        
    }    
    
    public void updateChanges ()
    {
        try
        {
            for (int i =1;i<cp.revisionCount;i++)
            {
                System.out.println ("working on revision = "+i);
                
                System.out.println ("\tupdating clones");
                //updateChangesInClones (i);
                
                /*System.out.println ("\tupdating methods");
                updateChangesInMethods (i);*/
            }
        }
        catch (Exception e)
        {
            
        }
    }
    
    
    public void updateChangesInClones (int commit, int cloneType)  throws Exception          
    {
        //determining the clones that received some changes.
        String [] clones = new String [5000000];
        int n = 0;
        
        BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+commit+".txt")));
        String str = "";
        while ((str = br.readLine())!= null)
        {
            if (str.trim().length() == 0){continue;}
            
            String sgcid = cp.getAttributeValueFromString(str, an.globalCloneID);
            if (sgcid.trim().length () == 0) {continue;}
                        
            int gcid = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalCloneID));
            
            int changeCount = ca.getChangeCountInClone (gcid, commit, cloneType);
            
            if (changeCount > 0)
            {
                clones[n] = an.globalCloneID + " = " + gcid + " : " + an.cloneChangeCount + " = " + changeCount + " : ";
                n++;
            }            
        }  
        br.close();
        
        BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+commit+"_change.txt"));
        for (int i =0;i<n;i++)
        {
            writer.write ("\n"+clones[i]);
        }
        writer.close();
    }
    
    public void updateChangesInMethods (int commit) throws Exception
    {
        //determining the methods that received some changes.
        String [] methods = new String [5000000];
        int n = 0;
        
        BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/methods/methods_version_"+commit+".txt")));
        String str = "";
        while ((str = br.readLine())!= null)
        {
            if (str.trim().length() == 0){continue;}
            
            String sgmid = cp.getAttributeValueFromString(str, an.globalMethodID);
            if (sgmid.trim().length () == 0) {continue;}
            
            int gmid = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalMethodID));
            int changeCount = ca.getChangeCountInMethod (gmid, commit);
            
            if (changeCount > 0)
            {
                methods[n] = an.globalMethodID + " = " + gmid + " : " + an.methodChangeCount + " = " + changeCount + " : ";
                n++;                
            }
        } 
        br.close();
        
        BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/methods/methods_version_"+commit+"_change.txt"));
        for (int i =0;i<n;i++)
        {
            writer.write ("\n"+methods[i]);
        }   
        writer.close();
    }        
}
