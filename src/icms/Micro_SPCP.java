/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icms;

import  java.util.Set;
import java.util.HashSet;

/**
 *
 * @author rabiul
 */
public class Micro_SPCP {
    
    
        DatabaseAccess da = new DatabaseAccess ();
        CommonParameters cp = new CommonParameters ();
        InvestigatingBugproneness ib = new InvestigatingBugproneness ();
        String bugfixcommits = ib.getBugFixCommits();
        int result = 0;
        
      public int getResult (int revision,int clonetype,SingleSPCPClonePair []spcpClones){
          
            
            String rev =Integer.toString(revision);
            if(!bugfixcommits.contains(rev)){
                return 0;
            }
          
          
            // Get clones
            SingleClone [] clones = da.getClones(revision-1, clonetype);
//            for(int j=0;clones[j]!=null;j++){
//                System.out.println("Revision : "+clones[j].revision +" FilePath "+ clones[j].filepath+" Global clone id= "+clones[j].globalcloneid);
//            }
            
           // System.out.println("Changes");
            // get changes
            SingleChange [] getChanges = da.getChangess(revision-1);
//            for(int j=0;getChanges[j]!=null;j++){
//                System.out.println("Revision : "+getChanges[j].revision +" File Path "+ getChanges[j].filepath);
//            }

            
//            for(int j=0;spcpClones[j]!=null;j++){
//                System.out.println("globalid 1 = "+ spcpClones[j].globalcloneid1 +"   GlobalColneid 2 = "+spcpClones[j].globalcloneid2);
//            }
            
             Set <String> globalid = new HashSet<>();

            for(int j=0;getChanges[j]!=null;j++){
               for(int k=0;clones[k]!=null;k++)
               {
                   if(getChanges[j].filepath.equals(clones[k].filepath)) // We have to update the logic here
                   {
                       globalid.add(clones[k].globalcloneid);
                   }
               }
            }
            
            
            
            
                  for(int j=0;j<spcpClones.length && spcpClones[j]!=null;j++)
                  {
                      if(globalid.contains(spcpClones[j].globalcloneid1)) // Need to chek the logic next
                      {
                          result =1;
                          break;
                      }
                  }
                  
            
            
            
            
            
            return result;
           
           
      }
        
}
