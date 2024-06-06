/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;
import java.io.*;

/**
 *
 * @author mani
 */
public class PreprocessRevisions {

    CommonParameters commonParameters = new CommonParameters ();
    

    public void mainMethod ()
    {
        try
        {
            for (int i =1;i<=commonParameters.revisionCount;i++)
            {
                System.out.println ("Normalizing version "+i) ;
                normalizeSourceCodeOfSpecificVersion (i);
            }
        }
        catch (Exception e)
        {
            System.out.println("error: method name = processAllVersions") ;
        }
    }
    public void normalizeSourceCodeOfSpecificVersion (int version) throws Exception
    {
        normalizeSourceCodeOfFolder (commonParameters.repositoryURL+"/version-"+version);
    }

    //This method normalizes the source codes of a folder.
    public void normalizeSourceCodeOfFolder (String folderpath) throws Exception
    {
        //System.out.println ("--------------------> Normalization of source code begins. ("+folderpath+")") ;

        File folder = new File(folderpath);
        File[] listOfFiles = folder.listFiles();
        String formattedFile = "", filepath = "" ;

        if (listOfFiles == null) return;

    	for (int i = 0; i < listOfFiles.length; i++)
    	{
            if (listOfFiles[i].isFile())
            {
                if (listOfFiles[i].getName().split("[.]+")[listOfFiles[i].getName().split("[.]+").length-1].equals (commonParameters.language_extension)) //only java files will be considered.
                {
                    normalizeSourceCodeOfFile (folderpath+"/"+listOfFiles[i].getName());
                }
            }
            else if (listOfFiles[i].isDirectory())
            {
                normalizeSourceCodeOfFolder (folderpath+"/"+listOfFiles[i].getName() ) ;
            }
    	}

        //System.out.println ("--------------------> Source code normalization complete. ("+folderpath+")") ;
    }

    public void normalizeSourceCodeOfFile (String filepath) throws Exception
    {
        System.out.println ("normalizing -> "+ filepath) ;

        //removal of comments.
        //Process p = Runtime.getRuntime ().exec (absolutePaths.uncommentToolPath + " "+filepath+" " + " myfolders/temp/temp.java ") ;
        //p.waitFor () ;
        try
        {
            Process p = Runtime.getRuntime ().exec(commonParameters.uncommentToolPath + " " + filepath + " temp.c");
            p.waitFor();

            //getting file name.
            //File file = new File (filepath) ;

            //p = Runtime.getRuntime ().exec("cmd /c del " + filepath);
            //p.waitFor();

            //p = Runtime.getRuntime ().exec("cmd /c copy temp.c " + filepath);
            //p.waitFor();

            //uncomment(filepath);
        }
        catch (Exception e)
        {
            System.out.println ("error 1 = "+e) ;
        }

        FileInputStream fstream = new FileInputStream ("temp.c") ;
        BufferedReader br = new BufferedReader (new InputStreamReader (fstream));
        String formattedFile = "", str = "" ;

        try
        {
        while ((str = br.readLine()) != null)
        {
            if ( str.trim().length() > 0 )
            {
                if (str.trim().equals ("{") || str.trim().equals ("}"))
                {
                    formattedFile = formattedFile + " " + str.trim(); //addition with the previous line.
                }
                else
                {
                    formattedFile = formattedFile + "\n" + str.trim(); //addition of trimmed line in the next line.
                }
            }
        }
        }
        catch (Exception e)
        {
            System.out.println ("error 2 = "+e) ;
        }
        //removing the previous new line if there is any.
        if (formattedFile.trim().length()>0)
        if (formattedFile.trim().charAt(0) == '\n')
        {
            formattedFile = formattedFile.trim().substring(1);
        }

        FileWriter fr = new FileWriter (filepath) ;
        BufferedWriter br1 = new BufferedWriter (fr) ;
        br1.write (formattedFile) ;
        br1.close () ;
    }

}
