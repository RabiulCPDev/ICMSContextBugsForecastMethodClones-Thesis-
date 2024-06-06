/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icms;

import java.io.File;

/**
 *
 * @author rabiul
 */
public class AuthorAnalysis {
    
    
    CommonParameters cp =new CommonParameters();
    
    String subject_system= cp.subject_system;
    String revisionPath = cp.repositoryURL;
    int totalRevision = cp.revisionCount;
    String system_langue = cp.system_language;
    
    int clonetype =3;
    DatabaseAccess da = new DatabaseAccess();
    SingleClone [] clones = da.getClones(10, clonetype);
    int len = clones.length;
    
    
    AuthorAccess authorAccess = new AuthorAccess();
    String versionPath = revisionPath+"/version-10";
    File files [];
    
    public AuthorAnalysis(){
       
    }
    
    
    public void Runner(){
        for (int i = 0; i < len; i++) {
            if(clones[i]!=null)
             System.out.println("Rev :"+clones[i].revision+" File name : "+clones[i].filepath+" startline : "+clones[i].startline);
        }
        files= authorAccess.getFiles(10, versionPath);
        if(files!=null){
            for(File file:files){
               if(file.getName().contains("."+system_langue))
                   System.out.println(file.getName());
            }
        }else{
            System.out.println("Null");
        }
        


    }
    
    
    
    
    public static void main(String[] args) {
        AuthorAnalysis t = new AuthorAnalysis();
        t.Runner();
    }
}
