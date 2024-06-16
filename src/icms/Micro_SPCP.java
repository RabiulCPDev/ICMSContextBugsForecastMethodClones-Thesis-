/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icms;

/**
 *
 * @author rabiul
 */
public class Micro_SPCP {
    
    
        DatabaseAccess da = new DatabaseAccess ();
        CommonParameters cp = new CommonParameters ();
        InvestigatingBugproneness ib = new InvestigatingBugproneness ();
        String bugfixcommits = ib.getBugFixCommits();
        
      public void getResult (int revision,int clonetype,SingleSPCPClonePair []spcpClones){
          
            int flag =0;
            String rev =Integer.toString(revision);
            if(bugfixcommits.contains(rev)){
                flag =1;
            }else{
                return;
            }
          
          
            // Get clones
            SingleClone [] clones = da.getClones(revision-1, clonetype);
            for(int j=0;clones[j]!=null;j++){
                System.out.println("Revision : "+clones[j].revision +" FilePath "+ clones[j].filepath+" Global clone id= "+clones[j].globalcloneid);
            }
            
            System.out.println("Changes");
            // get changes
            SingleChange [] getChanges = da.getChangess(revision);
            for(int j=0;getChanges[j]!=null;j++){
                System.out.println("Revision : "+getChanges[j].revision +" File Path "+ getChanges[j].filepath);
            }

            
            for(int j=0;spcpClones[j]!=null;j++){
                System.out.println("globalid 1 = "+ spcpClones[j].globalcloneid1 +"   GlobalColneid 2 = "+spcpClones[j].globalcloneid2);
            }
            
            
           
           
      }
        
}
