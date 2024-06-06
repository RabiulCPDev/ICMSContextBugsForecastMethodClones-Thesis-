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
public class CommonClasses {

}


class Difference
{
    boolean hasDiff = false;
    String compare = "";
}

class SourceFileInformation
{
    String fileName, originalFilePath ;
    int cloneCount ;
}

class PreviousMethodBlockInformation
{
    public String methodName, fileName, className, signature, packageName ;
    public int startingTokenNumber, endingTokenNumber, origin=0;
    public int startingLineNumber, endingLineNumber ;
    public boolean isCloned=false, deleted=true, changed = false ;
    int methodid, globalmethodid ,changedInNextVersion=0;
}

class ClonePair
{
    public int fileNo1, fileNo2;
    public int startingLine1, endingLine1, startingLine2, endingLine2 ;
    public int startingToken1, endingToken1, startingToken2, endingToken2;
    public String fileName1, fileName2 ;
    int clone_family=-1;
}

class FileModification
{
    public String filePath ;
    public int changeAfterLine ;
}

class Method
{
    String methodName, fileName, className, signature, packageName ;
    int startingTokenNumber, endingTokenNumber,origin=-1 ;
    int startingLineNumber, endingLineNumber ;
    boolean isCloned=false;
    int methodid, globalmethodid, changed = 0,changedInNextVersion=0 ; ;
}

class ClonedMethod
{
    int methodid, globalmethodid, cloneid, version, startingclonedline, endingclonedline;
    String filename;
}

class Clone
{
    int cloneID, beginLine, endLine, pairCloneID;
}

class CloneBlock
{
    int cloneid, startingline, endingline, familyid;
    String filepath;
}

class CloneFamily
{
    int familyID;
    String cloneTemplate;
    //Clone [] cloneMembers =  new Clone[100];
}

class Version
{
    int versionID=0;
    Method[] methods = new Method[10000];
    CloneFamily [] clone_families = new CloneFamily[1000];
}

class CCFinder
{
    String repositoryURL = "";
    //static String ccfinderCommandPath = "d:/tools/ccfinder/bin/ccfx" ;

    String language_extension = " c h ";
    String ccfinder_language_command = "cpp";
    String preprocessed_file_extension = ".cpp.2_0_0_2.default.ccfxprep";
    String method_symbol = "f";

    


    SourceFileInformation [] sourceFiles = new SourceFileInformation [20000];
    int sourceFileCount = 0;
    CommonParameters commonParameters = new CommonParameters ();


    CCFinder ()
    {
        if (commonParameters.language_extension.contains("java"))
        {
            language_extension = " java ";
            ccfinder_language_command = "java";
            preprocessed_file_extension = ".java.2_0_0_0.default.ccfxprep";
            method_symbol = "m";
        }
        if (commonParameters.language_extension.contains("c"))
        {
            language_extension = " c h ";
            ccfinder_language_command = "cpp";
            preprocessed_file_extension = ".cpp.2_0_0_2.default.ccfxprep";
            method_symbol = "f";
        }
        if (commonParameters.language_extension.contains("cs"))
        {
            language_extension = " cs ";
            ccfinder_language_command = "csharp";
            preprocessed_file_extension = ".csharp.2_0_0_0.default.ccfxprep";
            method_symbol = "m";
        }
    }

    public void applyTool (String folderPath)
    {
        try
        {
            Process p1 = Runtime.getRuntime ().exec (commonParameters.ccfinderCommandPath + " d "+commonParameters.ccfinder_language_command+" -b 30000 -dn "+ folderPath) ;
            p1.waitFor () ;

            //Thread.sleep(5000) ;
            /*Process p2 = Runtime.getRuntime ().exec (absolutePaths.ccfinderCommandPath + " p a.ccfxd -o a.txt") ;
            p2.waitFor ();*/
            //Thread.sleep(5000) ;
        }
        catch (Exception e)
        {
            System.out.println ("applyCCFinder") ;
            System.out.println (e) ;
        }
    }

    public void applyCCFinder (int revision)
    {
        try
        {
            Process p1 = Runtime.getRuntime ().exec (commonParameters.ccfinderCommandPath + " d "+commonParameters.ccfinder_language_command+" -b 30 -dn "+ commonParameters.repositoryURL+"/version-"+revision) ;
            p1.waitFor () ;

            //Thread.sleep(5000) ;
            /*Process p2 = Runtime.getRuntime ().exec (absolutePaths.ccfinderCommandPath + " p a.ccfxd -o a.txt") ;
            p2.waitFor ();*/
            //Thread.sleep(5000) ;
        }
        catch (Exception e)
        {
            System.out.println ("applyCCFinder") ;
            System.out.println (e) ;
        }        
    }


    /*CCFinder (SubjectSystem ss, int version) throws Exception
    {
        initializeCCFinder (ss);
        detectClonesAndFamiliesFromVersion(version);
    }*/

    public String getTokenSequenceOfMethod (int mid, int version) throws Exception
    {
        String tokenSequence = "";
        initializeCCFinder();
        detectClonesAndFamiliesFromVersion(version);
        tokenSequence = getTokenSequenceForMethod (mid, version);

        return tokenSequence;
    }

    public void initializeCCFinder ()
    {
        repositoryURL = commonParameters.repositoryURL;
        sourceFileCount = 0;
    }

    public void detectClonesAndFamiliesFromVersion ( int version ) throws Exception
    {
        String repositoryPath = repositoryURL+"/version-"+version;
        getAllFilesOfFolder (repositoryPath+"/.ccfxprepdir", "ccfxprep");
    }

    public void getAllFilesOfFolder (String folderName, String fileExtension)
    {
        try
        {
        //System.out.println ("------> I am retrieving all files of folder: "+ folderName) ;
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                if (fileExtension.indexOf(listOfFiles[i].getName().split("[.]+")[listOfFiles[i].getName().split("[.]+").length-1]) > -1)
                {
                    //inserting the java file names into a global array.
                    sourceFiles[sourceFileCount] =  new SourceFileInformation () ;
                    sourceFiles[sourceFileCount].fileName = folderName + "/" + listOfFiles[i].getName() ;
                    String t = sourceFiles[sourceFileCount].fileName;
                    t = t.replace("/.ccfxprepdir", "");
                    t = t.replace (preprocessed_file_extension, "");
                    sourceFiles[sourceFileCount].originalFilePath = t ;
                    sourceFileCount++;
                }
            }
            else if (listOfFiles[i].isDirectory())
            {
                getAllFilesOfFolder (folderName+"/"+listOfFiles[i].getName(),fileExtension ) ;
            }
        }
        }
        catch (Exception e)
        {
            System.out.println ("getAllFilesOfFolder");
            System.out.println (e.getStackTrace()) ;
        }

    } //public static void getAllMethodsOfFolder (String folderName)


    public String getTokenSequenceForMethod (int mid, int version) throws Exception
    {
        int sline = 0, eline = 0, stoken = 0, etoken = 0;
        String filepath = "";

        //stoken = getStartingTokenNumberOfLine(filepath, sline, version);
        //etoken = getEndingTokenNumberOfLine (filepath, eline, version);

        String tokenSequence = "";
        tokenSequence = getTokenSequence (filepath, sline, eline, version);
        return tokenSequence;
    }

    public String getIDsFromTokens (String tokenSequence)
    {
        int len = tokenSequence.trim().split("[ ]+").length;
        String s = "";
        String idSequence = "";

        for (int i =0;i<len;i++)
        {
            s = tokenSequence.trim().split("[ ]+")[i];
            if (s.indexOf("id|") == 0)
            {
                if (!idSequence.contains(s.split("[|]+")[1]))
                {
                    idSequence += " " + s.split("[|]+")[1]+" ";
                }
            }
        }

        return idSequence.trim();
    }

    public String getTokens (String filepath)
    {
        try
        {
            FileInputStream fstream = new FileInputStream (filepath+""+preprocessed_file_extension);
            BufferedReader br = new BufferedReader (new InputStreamReader (fstream)) ;
            String str = "";

            int lineno=0;
            int got = 0;
            String tokenSequence = "";

            while ((str = br.readLine())!= null)
            {
                tokenSequence += " " + str.split("[\t]+")[2];
            }
            br.close();
            return tokenSequence;
        }
        catch (Exception e)
        {
            System.out.println ("error = "+e);
        }
        return "";
    }

    public String getTokenSequenceOfLine (String filePath, int line)
    {       
        try
        {
            String tokenSequence = "";
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(filePath)));
            String str = "";

            while ((str = br.readLine())!= null)
            {
                //token_number++;
                int lineno = Integer.parseInt(str.split("[.]+")[0],16);
                if (lineno == line)
                {
                    tokenSequence += " " + str.split("[\t]+")[2];
                }
                if (lineno > line)
                {
                    br.close();
                    return tokenSequence;
                }
            }
            br.close();
            return tokenSequence;

        }
        catch (Exception e)
        {
            System.out.println ("error in method = getTokenSequenceOfLine. "+e);
        }


        return "";
    }


    public String getTokenSequence (String filepath, int slinenumber, int elinenumber, int version)
    {
        try
        {
        int i=0;
        String token_file_path = "", str="", original_file_path = "";
        token_file_path = repositoryURL+"/version-"+version+"/"+filepath;
        for (i=0;i<sourceFileCount;i++)
        {
            original_file_path = sourceFiles[i].originalFilePath.replace('\\', '/');
            if (original_file_path.equals(token_file_path))
            {
                token_file_path = sourceFiles[i].fileName; //this is the prep dir filepath
                break;
            }
        }

        FileInputStream fstream = new FileInputStream (token_file_path);
        BufferedReader br = new BufferedReader (new InputStreamReader (fstream)) ;

        int lineno=0;
        int got = 0;
        String tokenSequence = "";

        while ((str = br.readLine())!= null)
        {
            //token_number++;
            lineno = Integer.parseInt(str.split("[.]+")[0],16);
            if (lineno >= slinenumber)
            {
                got = 1;
            }

            if (lineno >= elinenumber+1)
            {
                br.close();
                return tokenSequence ;
                //break;
            }

            if (got == 1)
            {
                tokenSequence += " " + str.split("[\t]+")[2];
            }
        }
        br.close();
        }
        catch (Exception e)
        {
            System.out.println ("getTokenSequence. error = "+e);
            //System.out.println (e.getStackTrace());
        }
        return "";
    }

    /*public int getEndingTokenNumberOfLine (String filepath, int linenumber, int version)
    {
        try
        {
        int i=0;
        String token_file_path = "", str="", original_file_path = "";
        token_file_path = repositoryURL+"/version-"+version+"/"+filepath;
        for (i=0;i<sourceFileCount;i++)
        {
            original_file_path = sourceFiles[i].originalFilePath.replace('\\', '/');
            if (original_file_path.equals(token_file_path))
            {
                token_file_path = sourceFiles[i].fileName; //this is the prep dir filepath
                break;
            }
        }

        FileInputStream fstream = new FileInputStream (token_file_path);
        BufferedReader br = new BufferedReader (new InputStreamReader (fstream)) ;

        int lineno=0;
        int token_number = 0;
        while ((str = br.readLine())!= null)
        {
            token_number++;
            lineno = Integer.parseInt(str.split("[.]+")[0],16);
            if (lineno == linenumber+1)
            {
                token_number--;
                br.close();
                return token_number;
            }
        }
        br.close();
        }
        catch (Exception e)
        {
            System.out.println ("getStartingTokenNumberOfLine");
            System.out.println (e.getStackTrace());
        }
        return -1;
    } */
}

