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
        String bugRepRev ;
        
        
      public boolean checkMicroClone (int revision,int clonetype,SingleClone []clones,SingleChange []changes){
          
            
//            String rev =Integer.toString(revision-1);
//            if(!bugfixcommits.contains(rev)){
//                return false;
//            }
          
            
            // Get clones
//            SingleClone [] clones = da.getClones(revision-1, clonetype);
//            for(int j=0;clones[j]!=null;j++){
//                System.out.println("Revision : "+clones[j].revision +" FilePath "+ clones[j].filepath+" Global clone id= "+clones[j].globalcloneid);
//            }
            
            // System.out.println("Changes");
            // get changes
//            SingleChange [] changes = da.getChangess(revision-1);
//            for(int j=0;getChanges[j]!=null;j++){
//                System.out.println("Revision : "+getChanges[j].revision +" File Path "+ getChanges[j].filepath);
//            }

            
//            for(int j=0;spcpClones[j]!=null;j++){
//                System.out.println("globalid 1 = "+ spcpClones[j].globalcloneid1 +"   GlobalColneid 2 = "+spcpClones[j].globalcloneid2);
//            }
            
//             Set <String> globalid = new HashSet<>();
            boolean result = false;
            for(int j=0;changes[j]!=null;j++)
            {
               for(int k=0;clones[k]!=null;k++)
               {
                   int change_startLine=Integer.parseInt(changes[j].startline), changes_endLine=Integer.parseInt(changes[j].endline);
                   int clone_startLine =Integer.parseInt(clones[k].startline), clone_endLine =Integer.parseInt(clones[k].endline);
                   if(changes[j].filepath.equals(clones[k].filepath) && !((change_startLine>clone_endLine) || (changes_endLine<clone_startLine))) // We have to update the logic here
                   {
                       result = true;
                       return result;
                            
                   }
                }
               
            }
                  
            return result;
           
           
      }
      public boolean checkSpcp(SingleSPCPClonePair []spcpClones,SingleClone []clones, SingleChange []changes){
          
          
            Set <String> globalid = new HashSet<>();
           boolean result = false;
            for(int j=0;changes[j]!=null;j++)
            {
               for(int k=0;clones[k]!=null;k++)
               {
                   int change_startLine=Integer.parseInt(changes[j].startline), changes_endLine=Integer.parseInt(changes[j].endline);
                   int clone_startLine =Integer.parseInt(clones[k].startline), clone_endLine =Integer.parseInt(clones[k].endline);
                   if(changes[j].filepath.equals(clones[k].filepath) && !((change_startLine>clone_endLine) || (changes_endLine<clone_startLine))) // We have to update the logic here
                   {
                          globalid.add(clones[k].globalcloneid);
                            
                   }
                }
            }
                                  
                 for(int j=0;j<spcpClones.length && spcpClones[j]!=null;j++)
                  {
                      if(globalid.contains(spcpClones[j].globalcloneid1)) // Need to chek the logic next
                      {
                          result =true;
                          return result;
                      }
                  }
          return result;
          
      }
        
}
