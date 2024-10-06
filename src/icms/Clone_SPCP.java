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
public class Clone_SPCP {
    
    
        DatabaseAccess da = new DatabaseAccess ();
        CommonParameters cp = new CommonParameters ();
        InvestigatingBugproneness ib = new InvestigatingBugproneness ();
        String bugfixcommits = ib.getBugFixCommits();
        String buggyglobalid="";
               
     
  public boolean checkBugReplication(SingleClone []clones, SingleChange []changes,int cloneIndex1)
    {
        for(int j=0;changes[j]!=null;j++)
          {

                   int change_startLine=Integer.parseInt(changes[j].startline), changes_endLine=Integer.parseInt(changes[j].endline);
                   int clone_startLine =Integer.parseInt(clones[cloneIndex1].startline), clone_endLine =Integer.parseInt(clones[cloneIndex1].endline);

                  // check clone 
                   if(changes[j].filepath.equals(clones[cloneIndex1].filepath) && ((change_startLine>clone_endLine) || (changes_endLine<clone_startLine))) // We have to update the logic here
                 {                      
//                    if(!buggyglobalid.contains(clones[cloneIndex1].globalcloneid)) buggyglobalid=buggyglobalid+" "+clones[cloneIndex1].globalcloneid;
                     return true;                      
                 }


          }
        return false;
    }
      
      public void revResult(int type){
          
          try {
              
              RevGroup []revGroup=new RevGroup[100000];
              Rev_File []revFile = new Rev_File[100000];
              
              int maxId=-1;
              
              for (int i = 1; i < cp.revisionCount; i++) {
                  SingleClone []clones = da.getClones(i-1, type);
                  for (int j = 0; clones[j]!=null; j++) {
                      int global_clone_id = Integer.parseInt(clones[j].globalcloneid);
                      int revision = Integer.parseInt(clones[j].revision);
                      
                      if(global_clone_id<0) continue;
                      maxId=Math.max(maxId,global_clone_id);
                      
                      if(revFile[global_clone_id]==null)
                      {
                          revFile[global_clone_id]= new Rev_File();
                      }
                      if(revFile[global_clone_id].revision_start>revision){
                          revFile[global_clone_id].revision_start =revision;
                          revFile[global_clone_id].fileName =clones[j].filepath;
                      }
                  }
              }
              
              
              
//              for (int i = 0; i<=maxId; i++) {
//                 if(revFile[i]!=null){
//                     System.out.println(revFile[i].fileName +" "+revFile[i].revision_start);
//                 }
//                 
//              }
              
              
                          
          int maxCloneId =-1;
          for (int i = 1; i < cp.revisionCount; i++) {
              SingleClonePair [] pairs = da.getClonePairs(i-1, type);
              for (int j = 0;pairs[j]!=null; j++) {
                  
                  int pairGid1 =Integer.parseInt(pairs[j].globalcloneid1);
                  int pairGid2=Integer.parseInt(pairs[j].globalcloneid2);
              
                  if(pairGid1<0 || pairGid2<0) continue;
                  maxCloneId =Math.max(maxCloneId,Math.max(pairGid1,pairGid2));
                  
                  if(revGroup[pairGid1]==null)
                  { 
                      revGroup[pairGid1]= new RevGroup();
                  }
                  if(revGroup[pairGid2]==null) 
                  {
                      revGroup[pairGid2]=new RevGroup();
                  }
                  
                  
                  if(!revGroup[pairGid1].pair.contains(pairs[j].globalcloneid2) && !revGroup[pairGid2].pair.contains(pairs[j].globalcloneid1)){
                      revGroup[pairGid1].pair= revGroup[pairGid1].pair+" "+pairs[j].globalcloneid2;
                  }
              }
          }
          
//          for (int i = 1; i <= maxCloneId; i++) {
//              if(revGroup[i]!=null){
//                  System.out.println("Clone id = "+i);
//                  System.out.println("pair:  "+revGroup[i].pair);
//              }
//          }
          
          
          // SAme revision or differnent
          int sameRevision =0, differentRevision=0,sameFile=0,differentFile=0;
          
          for (int i = 1; i <= maxCloneId; i++) {
              if(revGroup[i]!=null){
                 String [] pairs= revGroup[i].pair.split(" ");
                  
                 for(String pair : pairs) {
                     if(pair.length()==0) continue;
                     if(!buggyglobalid.contains(pair)) continue;
                      int gid = Integer.parseInt(pair);
                      if(revFile[i].revision_start==revFile[gid].revision_start){
                          sameRevision++;
                      }
                      
                      if(revFile[i].fileName.equals(revFile[gid].fileName))
                      {
                          sameFile++;
                      }
                      
                      
                  }
                 
              }
          }
              
              System.out.println("Total Same revision = "+sameRevision);
              System.out.println("Total Same file = "+sameFile);
              System.out.println(maxId+ "  "+maxCloneId);
              System.out.println(buggyglobalid);
              
                     
              
          } catch (Exception e) {
              e.printStackTrace();
          }
          
      }
      
      
      
      
      
      
      public boolean checkSpcp(SingleSPCPClonePair []spcpClones,SingleClone []clones, SingleChange []changes)
            {


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


 class RevGroup{
        String pair="";
}

    class Rev_File
        {
            int revision_start=Integer.MAX_VALUE;
            String fileName;
        }
