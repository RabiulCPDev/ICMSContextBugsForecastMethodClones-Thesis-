/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icms;

import java.sql.ResultSet;

/**
 *
 * @author rabiul
 */
public class Micro_SPCP {
    
    
        DatabaseAccess da = new DatabaseAccess ();
        CommonParameters cp = new CommonParameters ();
        InvestigatingBugproneness ib = new InvestigatingBugproneness ();
        String bugfixcommits = ib.getBugFixCommits();
        
      public void getResult (int revision,int clonetype){
          
//            // Get specific type of clones clones
//            SingleClone [] clones = da.getClones(revision-1, clonetype);
//            for(int j=0;clones[j]!=null;j++){
//                System.out.println(clones[j].revision +" . "+ clones[j].filepath+" "+clones[j].startline+" "+clones[j].endline);
//            }
//            
//            System.out.println("Changes");
//            // get Specific changes
//           SingleChange [] getChanges = da.getChangess(i);
//           for(int j=0;getChanges[j]!=null;j++){
//               System.out.println("Revision :"+getChanges[j].revision +"File Path "+ getChanges[j].filepath+"StartLine: "+getChanges[j].startline+"Endline: "+getChanges[j].endline);
//           }

                // Get SPCP 
                SingleSPCPClonePair [] spcpClones = da.getSPCPClones(clonetype);
                for(int j=0;spcpClones[j]!=null;j++){
                    System.out.println("globalid 1 ="+ spcpClones[j].globalcloneid1 +"   GlobalColneid 2 = "+spcpClones[j].globalcloneid2);
                }
            
           
           
      }
        
    
    
    

    
    public static void main(String[] args) {
        Micro_SPCP mic_spcp = new Micro_SPCP();
       mic_spcp.getResult(10, 1);
    }
}
