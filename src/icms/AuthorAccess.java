/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import sun.nio.ch.FileKey;

/**
 *
 * @author rabiul
 */
public class AuthorAccess {
    
    CommonParameters cp =new CommonParameters();

        String subject_system= cp.subject_system+"/author";
        String revisionPath = cp.repositoryURL;
        int totalRevision = cp.revisionCount;
        String system_langue = cp.system_language;
   public AuthorAccess() {
        
    }
    
    
    
    public File [] getFiles(int revision,String versionPath){
        
                 File files = new File(versionPath);
                 File filesArray []= files.listFiles();
                 return filesArray;
    }
    
   
    public String authorName(String fileName,int lineNumber,int revision){
              String filePath = subject_system+ "/"+system_langue+"/"+fileName+ ".txt"; 
              File file = new File(filePath);
            
    }
    
    
    public void createAuthorFile(int revision){
            
        try {
                // checck the file exist in the autor/version-revision
        
            String languageFolderPath = subject_system+"/"+system_langue;
            File languageFolder = new File(languageFolderPath);
            if(!languageFolder.exists()){
                languageFolder.mkdir();
                System.out.println("Folder created successfully "+languageFolderPath);
            }
            
            String versionFolderPath = languageFolderPath+"/version-"+revision;
            File versionFolder = new File(versionFolderPath);
            if(versionFolder.exists()){
                System.out.println(versionFolderPath + "exists");
                return;
            }
            versionFolder.mkdir();
            
            String versionPath = revisionPath+"/version-"+revision;
            File files [] = getFiles(revision,versionPath);

            // Create the files of author

            for (File file : files) {
                String file_name =file.getName();
                if(file_name.contains("."+system_langue)){

                    if(!authorCommand(revision, file_name,versionFolderPath)){
                        System.out.println("Error in Creating File author files in author folder in revison: "+revision);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Creating author file failed");
        }
        
        
    }
    public boolean authorCommand(int revision, String file,String versionFolderPath){
        try {
            
            String versionPath = revisionPath+"/version-"+revision;
            Process pro = Runtime.getRuntime().exec("svn blame "+versionPath+"/"+file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line=" ";
            String fileName = file+".txt";
            FileWriter authorFile = new FileWriter(versionFolderPath+"/"+fileName);
            
            while((line = reader.readLine())!=null){
                authorFile.write(line+"\n");
            }
            authorFile.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error in command ");
            return false;
        }
        
        
    }
    
    
    
        public void run(){
             for (int i = 2; i <totalRevision ; i++) {
                 createAuthorFile(i);
                 System.out.println("Author file is saved for revision : "+i);
             }
             
        }
        
    public static void main(String[] args) {
        AuthorAccess test = new AuthorAccess();
        test.run();
    }
}
