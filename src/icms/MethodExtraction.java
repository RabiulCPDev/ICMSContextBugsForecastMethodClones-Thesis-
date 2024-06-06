/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author mani
 */
public class MethodExtraction {
    //public int maxRevisionNumber = 0;
    public String fileList [] = new String [50000];
    public int [] fileModificationList = new int[50000];
    public int fileCount = 0;
    public Method [] methodBlocks = new Method[50000] ;
    public int methodCount = 0;
    public String packageName = "" ;
    public CommonParameters commonParameters = new CommonParameters ();
    public CommonParameters cp = new CommonParameters ();
    public String [] existingMethods = new String [100000];
    public int existingMethodCount = 0;


    //only the unchanged files.
    public String [] unchangedFiles = new String[5000];
    public int unchangedFileCount = 0;

    public AttributeNames an = new AttributeNames ();

    
    //both changed and new files.
    public String [] changedFiles = new String[5000];
    public int changedFileCount = 0;

    //public int currentVersion = 0;
    
    DatabaseAccess da = new DatabaseAccess ();
    
    
    public void storeMethodChangesToDatabase ()
    {
        String [] queries = new String[5000000];
        int querycount = 0;
        
        try
        {
            for (int i =1;i<commonParameters.revisionCount;i++)
            {
                System.out.println ("revision "+i);
                
                BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(cp.subject_system+"/methods/methods_version_"+i+"_change.txt")));
                String str = "";
                while((str = br.readLine())!= null)
                {
                    if (str.trim().length() == 0){continue;}
                    
                    String globalmethodid = cp.getAttributeValueFromString(str, an.globalMethodID);
                    String change = cp.getAttributeValueFromString(str, an.methodChangeCount);
                    
                    String query = "insert into methodchanges (globalmethodid, changecount, revision) values ("
                            + "'"+globalmethodid+"',"
                            + "'"+change+"',"
                            + "'"+i+"'"
                            + ")";
                    queries[querycount] = query;
                    querycount++;
                }
            }
            
            da.insertData(queries, querycount);            
        }
        catch (Exception e)
        {
            System.out.println ("error. " + e);
        }        
    }
    
    
    public void storeMethodsToFile ()
    {
        String [] queries = new String[50000];
        int querycount = 0;
        int methodid = 0;
        
        try
        {
            for (int i =1;i<=commonParameters.revisionCount;i++)
            {
                methodid = 0;
                System.out.println ("revision "+i);
                
                BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.getMethodsPathOfRevision(i))));
                String str = "";
                while((str = br.readLine())!= null)
                {
                    if (str.trim().length() == 0){continue;}
                    
                    String methodname = cp.getAttributeValueFromString(str, an.methodName);
                    String filepath = cp.getAttributeValueFromString(str, an.filePath);
                    String packagename = cp.getAttributeValueFromString(str, an.packageName);
                    String classname = cp.getAttributeValueFromString(str, an.className);
                    String startline = cp.getAttributeValueFromString(str, an.startingLine);
                    String endline = cp.getAttributeValueFromString(str, an.endingLine);
                    String globalmethodid = cp.getAttributeValueFromString(str, an.globalMethodID);
                    String signature = cp.getAttributeValueFromString(str, an.signature);
                    //signature = signature.replaceAll("'", "''");
                    
                    String query = "insert into methods (methodname, filepath, packagename, classname, startline, endline, globalmethodid, signature, revision, methodid) values ("
                            + "'"+methodname+"',"
                            + "'"+filepath+"',"
                            + "'"+packagename+"',"
                            + "'"+classname+"',"
                            + "'"+startline+"',"
                            + "'"+endline+"',"
                            + "'"+globalmethodid+"',"
                            + "'"+signature+"',"
                            + "'"+i+"',"
                            + "'"+methodid+"'"
                            + ")";
                    queries[querycount] = query;
                    querycount++;
                    methodid++;
                    
                    if (querycount >= 1000)
                    {
                        da.insertData(queries, querycount);
                        querycount = 0;
                    }
                }
            }
            
            if (querycount > 0)
            {
                da.insertData(queries, querycount);            
            }
        }
        catch (Exception e)
        {
            
        }
    }
    
    public boolean confirmInsertion (String tablename)
    {
        boolean hasdata = da.hasData(tablename);
        if (hasdata)
        {
            int answer = JOptionPane.showConfirmDialog(null, "The table contains data. Do you want to re-insert?");
            if (answer != 0)
            {
                return false;
            }
        }        
        return true;
    }    
    
    public void storeMethodsToDatabase ()
    {
        if (confirmInsertion("methods") == false) {return;}
        
        try
        {            
            for (int i =1;i<=commonParameters.revisionCount;i++)
            {
                System.out.println ("revision "+i);
                SingleMethod [] methods = new SingleMethod[500000];
                int mid = 0;
                
                BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.getMethodsPathOfRevision(i))));
                String str = "";
                while((str = br.readLine())!= null)
                {
                    if (str.trim().length() == 0){continue;}
                    
                    methods[mid] = new SingleMethod();
                    methods[mid].revision = i+"";
                    methods[mid].methodid = (mid+1)+"";
                    methods[mid].globalmethodid = cp.getAttributeValueFromString(str, an.globalMethodID);
                    methods[mid].filepath = cp.getAttributeValueFromString(str, an.filePath);
                    methods[mid].startline = cp.getAttributeValueFromString(str, an.startingLine);
                    methods[mid].endline = cp.getAttributeValueFromString(str, an.endingLine);
                    methods[mid].methodname = cp.getAttributeValueFromString(str, an.methodName);
                    methods[mid].packagename = cp.getAttributeValueFromString(str, an.packageName);
                    methods[mid].classname = cp.getAttributeValueFromString(str, an.className);
                    methods[mid].signature = cp.getAttributeValueFromString(str, an.signature);
                    methods[mid].changecount = "0";
                    mid++;
                }
                br.close();
                da.insertMethods(methods);
            }               
        }
        catch (Exception e)
        {
            
        }
    }
    
    
    //mainMethod performs all the final tasks.
    public void mainMethod ()
    {
        for (int i =1;i<=commonParameters.revisionCount;i++)
        {            
            //clearing the counters for the next revision.
            methodCount = 0;
            fileCount = 0;
            existingMethodCount = 0;
            
            //getting all method of revision i.
            System.out.println ("storing methods of version = "+i);
            
            Date date1 = new Date();
            int minutes1 = date1.getMinutes();
            int seconds1 = date1.getSeconds()+minutes1*60;
            
            getAllMethods(i);                        
            storeMethodsToFile(i); //writing methods to files.

            Date date2 = new Date();
            int minutes2 = date2.getMinutes();
            int seconds2 = date2.getSeconds()+minutes2*60;

            System.out.println ("\ttime taken = "+(seconds2-seconds1));
            System.out.println ("storing methods of version = "+i+" complete\n");
        }                
    }
   
    public void storeMethodsToFile (int revision)
    {
        try
        {
            BufferedWriter br = new BufferedWriter (new FileWriter (commonParameters.getMethodsPathOfRevision(revision)));
            String line = "";

            System.out.println ("\tMethod Count in unchanged files = "+existingMethodCount+" (writing)") ;
            //Storing the existing methods.
            for (int i =0;i<existingMethodCount;i++)
            {
                br.write ("\n"+existingMethods[i]);
            }

            System.out.println ("\tMethod Count in changed or new files = "+methodCount+" (writing)") ;
            //System.out.println ("Method writing begins.");
            for (int i=0;i<methodCount;i++)
            {
                line = "";
                //line += "method id = "+ i + commonParameters.separator;
                line += an.methodName+" = "+ methodBlocks[i].methodName + commonParameters.separator;
                line += an.signature+" = "+ methodBlocks[i].signature + commonParameters.separator;
                line += an.filePath+" = "+ methodBlocks[i].fileName + commonParameters.separator;
                line += an.className+" = "+ methodBlocks[i].className + commonParameters.separator;
                line += an.packageName+" = "+ methodBlocks[i].packageName + commonParameters.separator;
                line += an.startingLine+" = "+ methodBlocks[i].startingLineNumber + commonParameters.separator;
                line += an.endingLine+" = "+ methodBlocks[i].endingLineNumber + commonParameters.separator;;
                br.write ("\n"+line);
            }
            br.close();
            //System.out.println ("Method writing ends.");
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = storeMethodsToFile."+e);
        }
    }

    //this method returns the signature of a method.
    //---------------------------------------------------------------------
    public String getMethodSignature (String signature)
    {
        try
        {
        //System.out.println ("getting method signature from " +signature);

        String psignature = signature;
        String[]signatureElements = new String [50] ;

        int i =0, j=0, beginsfrom=0, endsat=0, len;
        boolean bool1, bool2 ;


        len = psignature.length () ;

        for (i=0;i<len;i++)
        {
            if (Character.isLetter(psignature.charAt(i)))
            {
                beginsfrom = i ;
                break;
            }
        }
        psignature = psignature.substring (beginsfrom,len) ; //discarding the first unwanted character sets.

        signatureElements = psignature.split("[ \t]+");

        psignature = "" ;
        len = signatureElements.length ;
        i=0;
        while (i<len)
        {
            if (signatureElements[i].length() > 0)
            {
                if (psignature.length() > 0)
                {
                    bool1 = Character.isLetter (psignature.charAt(psignature.length()-1)) ;
                    if (bool1 == false)
                        bool1 = Character.isDigit (psignature.charAt(psignature.length()-1)) ;

                    bool2 = Character.isLetter (signatureElements[i].charAt(0)) ;
                    if (bool2 == false)
                        bool2 = Character.isLetter (signatureElements[i].charAt(0)) ;

                    if (bool1 == true && bool2 == true)
                        psignature = psignature + " " + signatureElements[i] ;
                    else
                    {
                        if ( psignature.charAt(psignature.length()-1) == ']' || psignature.charAt(psignature.length()-1) == '>' )
                        {
                            if (Character.isLetter(signatureElements[i].charAt(0)) || Character.isDigit(signatureElements[i].charAt(0)))
                                psignature = psignature + " " + signatureElements[i] ;
                            else
                                psignature = psignature + signatureElements[i] ;
                        }
                        else
                            psignature = psignature + signatureElements[i] ;
                    }
                }
                else
                    psignature = signatureElements[i] ;
            }
            i++ ;
        }

        i=psignature.indexOf ("(") ;

        len = psignature.length () ;
        int lcnt = 0, rcnt = 0;
        j = i ;
        j++ ;
        while (j<len)
        {
            if (psignature.charAt(j) == '(')
                lcnt++;
            if (psignature.charAt(j) == ')')
            {
                rcnt++ ;
                if (rcnt-lcnt == 1) break ;
            }
            j++ ;
        }

        String parameters = "" ;
        if ( i+1 < j-1)
        {
            if (i+1 >= 0 && i+1 < psignature.length() && j >= 0 && j < psignature.length())
                parameters = psignature.substring (i+1,j) ;
        }

        if (i>=0 && i < psignature.length())
            psignature = psignature.substring (0,i) ;

        String [] parameterElements = parameters.split ("[,]+") ;

        len = parameterElements.length ;
        parameters = "" ;

        i=0;
        while (i<len)
        {
            if (parameters.length() > 0)
                parameters = parameters + ","+ parameterElements[i].split("[ \t]+")[0] ;
            else
                parameters = parameterElements[i].split("[ \t]+")[0] ;
            i++ ;
        }

        len = psignature.split("[ ]+").length ;
        if (len > 1)
        {
            String psignature1 = psignature.split("[ ]+")[len-2] ;
            psignature1 = psignature1 + " " + psignature.split("[ ]+")[len-1] ;
            psignature = psignature1 ;
        }

        psignature = psignature + "(" + parameters + ")";

        return psignature ;
        }
        catch (Exception e)
        {
            System.out.println ("getMethodSignature");
            System.out.println (e) ;
        }
        return "";
    }//public static String getSignature (String signature)


    //this method finds the end line number of a method from a file given the starting line number of method in the file.
    public int getEndLineNumberOfMethod (int sline, String mname, String fname, int revision)
    {
        try
        {
        //System.out.println ("getting end line number of method "+ mname);

        int linecount = 0, sindex=0, lbrace=0, rbrace=0;
        String str = "" ;

        FileInputStream fstream = new FileInputStream (commonParameters.repositoryURL+"/version-"+revision+"/"+fname) ;
        DataInputStream dstream = new DataInputStream (fstream) ;

        while (dstream.available () != 0)
        {
            dstream.readLine () ;
            linecount++ ;
            if (linecount == sline-1)
                break ;
        }

        str = dstream.readLine () ;
        if (str.indexOf(";") > -1)
        {
            if (str.indexOf ("{") == -1)
            {
                dstream.close();
                return sline;
            }
        }
        sindex = str.indexOf(mname);

        for (int i =sindex;i<str.length();i++)
        {
            if (str.charAt(i) == '{') lbrace++ ;
            if (str.charAt(i) == '}')
            {
                rbrace++ ;
                if (lbrace == rbrace) return sline;
            }
        }

        linecount = sline ;

        while (dstream.available () != 0)
        {
            str = dstream.readLine () ;
            linecount++ ;

            for (int i =0;i<str.length();i++)
            {
                if (str.charAt(i) == '{') lbrace++ ;
                if (str.charAt(i) == '}')
                {
                    rbrace++ ;
                    if (lbrace == rbrace) return linecount;
                }
            }
        }
        }
        catch (Exception e)
        {
            System.out.println ("getEndLineNumberOfMethod") ;
            System.out.println (e) ;
        }

        return -1;
    }//public static int getEndLineNumberOfMethod (int sline, String mname, String fname)



    //this function is used to retrieve methods from files using ctags.
    public void retrieveMethodsFromFileNew (int revision)
    {
        try
        {
            //System.out.println ("------> I am trieving all methods of file: "+ fileName) ;

            String str = "", methodSignature = "" ;
            int sectionCount = 0, got = 0, packageFound = 0, methodTab = 0;

            //writing the file names for ctags.
            BufferedWriter bwriter= new BufferedWriter (new FileWriter ("filelistforctags.txt"));
            for (int i =0;i<fileCount-1;i++)
            {
                bwriter.write (fileList[i]+"\n");
            }
            bwriter.write (fileList[fileCount-1]);
            bwriter.close();


            //String files = "";
            //for (int icount =0;icount<fileCount;icount = icount+2)
            //{
                //files = " "+fileList[icount]+" "+fileList[icount+1];

                //System.out.println ("Begin Running ctags") ;
                String command = "";
                command = commonParameters.ctagsCommandPath + " -L filelistforctags.txt  --fields=+n ";
                System.out.println (command);
                Process p = Runtime.getRuntime ().exec (command) ; //ctags is called here. Tags file has been created.
                p.waitFor () ;
                //System.out.println ("End Running ctags") ;

                //tags file is being read now.
                FileInputStream fstream = new FileInputStream ("tags") ;
                DataInputStream dstream = new DataInputStream(fstream) ;

                while (dstream.available () != 0)
                {
                    str = dstream.readLine () ;
                    if (str.split("[ ,\t]+")[0].equals("!_TAG_PROGRAM_VERSION"))
                        break ;//main lines to be examined will appear now.
                }

                int previousMethodCount = methodCount ; //This is necessary for getting the range of methods of the current file.

                while (dstream.available () != 0)
                {
                    str = dstream.readLine () ;
                    sectionCount = str.split("[\t]+").length ;

                    //search for a method.
                    for (int i =0;i<sectionCount; i++)
                    {
                        if (str.split("[\t]+")[i].equals (commonParameters.method_symbol) && i > 0) //I have got the method information.
                        {
                            methodTab = i ;
                            methodBlocks[methodCount] = new Method () ;
                            methodBlocks[methodCount].methodName = str.split("[\t]+")[0] ;
                            methodBlocks[methodCount].fileName = str.split("[\t]+")[1] ;

                            String t = commonParameters.repositoryURL+"/version-"+revision+"";
                            int len = t.length() ;
                            String filename = methodBlocks[methodCount].fileName;
                            filename = filename.substring (len+1) ;
                            methodBlocks[methodCount].fileName = filename;

                            methodSignature = "";
                            for (int j =2;j< methodTab;j++)
                            {
                                methodSignature += str.split("[\t]+")[j] ;
                            }
                            methodBlocks[methodCount].signature = getMethodSignature(methodSignature) ;
                            methodBlocks[methodCount].startingLineNumber = Integer.parseInt(str.split("[\t]+")[methodTab+1].split("[:]+")[1]) ;

                            //finding the method in the preprocessed file of ccfinder output.
                            //if ccfinder does not contain this method we need not add this method in the method list.
                            if (str.split("[\t]+").length > methodTab+2)
                            {
                                if (str.split("[\t]+")[methodTab+2].split("[:]+").length > 1)
                                {
                                    methodBlocks[methodCount].className = str.split("[\t]+")[methodTab+2].split("[:]+")[1] ;
                                }
                            }
                            methodCount++ ;

                            break ;
                        }
                        else
                        {
                            if (str.split("[\t]+")[i].equals ("p")) //I have got the package information.
                            {
                                packageName = str.split("[\t]+")[0] ;
                                break;
                            }
                        }
                    }
                }

                //Assigning the package to all files of this method.
                for (int i=previousMethodCount;i<methodCount;i++)
                {
                    methodBlocks[i].packageName = packageName;
                }

                //getting the last line number of all methods.
                for (int i=previousMethodCount;i<methodCount;i++ )
                {
                    //System.out.println ("method file path: "+methodBlocks[i].fileName);
                    methodBlocks[i].endingLineNumber = getEndLineNumberOfMethod (methodBlocks[i].startingLineNumber, methodBlocks[i].methodName, methodBlocks[i].fileName, revision) ;
                }
            //} //for end.
        } //try end.
        catch (Exception e)
        {
            System.out.println ("retrieveMethodsFromFileNew") ;
            System.out.println (e) ;
        }

    } //public static void retrieveMethodsFromFile (String fileName) throws Exception



    //this function is used to retrieve methods from files using ctags.
    public void retrieveMethodsFromFile (String fileName, int revision)
    {
        try
        {
            //System.out.println ("------> I am trieving all methods of file: "+ fileName) ;

            String str = "", methodSignature = "" ;
            int sectionCount = 0, got = 0, packageFound = 0, methodTab = 0;

            //System.out.println ("Begin Running ctags") ;
            Process p = Runtime.getRuntime ().exec (commonParameters.ctagsCommandPath + " --fields=+n "+fileName) ; //ctags is called here. Tags file has been created.
            p.waitFor () ;
            //System.out.println ("End Running ctags") ;

            //tags file is being read now.
            FileInputStream fstream = new FileInputStream ("tags") ;
            DataInputStream dstream = new DataInputStream(fstream) ;

            while (dstream.available () != 0)
            {
                str = dstream.readLine () ;
                if (str.split("[ ,\t]+")[0].equals("!_TAG_PROGRAM_VERSION"))
                    break ;//main lines to be examined will appear now.
            }

            int previousMethodCount = methodCount ; //This is necessary for getting the range of methods of the current file.

            while (dstream.available () != 0)
            {
                str = dstream.readLine () ;
                sectionCount = str.split("[\t]+").length ;

                //search for a method.
                for (int i =0;i<sectionCount; i++)
                {
                    if (str.split("[\t]+")[i].equals (commonParameters.method_symbol) && i > 0) //I have got the method information.
                    {
                        methodTab = i ;
                        methodBlocks[methodCount] = new Method () ;
                        methodBlocks[methodCount].methodName = str.split("[\t]+")[0] ;
                        methodBlocks[methodCount].fileName = str.split("[\t]+")[1] ;

                        String t = commonParameters.repositoryURL+"/version-"+revision+"";
                        int len = t.length() ;
                        String filename = methodBlocks[methodCount].fileName;
                        filename = filename.substring (len+1) ;
                        methodBlocks[methodCount].fileName = filename;

                        methodSignature = "";
                        for (int j =2;j< methodTab;j++)
                        {
                            methodSignature += str.split("[\t]+")[j] ;
                        }
                        methodBlocks[methodCount].signature = getMethodSignature(methodSignature) ;
                        methodBlocks[methodCount].startingLineNumber = Integer.parseInt(str.split("[\t]+")[methodTab+1].split("[:]+")[1]) ;

                        //finding the method in the preprocessed file of ccfinder output.
                        //if ccfinder does not contain this method we need not add this method in the method list.
                        if (str.split("[\t]+").length > methodTab+2)
                        {
                            if (str.split("[\t]+")[methodTab+2].split("[:]+").length > 1)
                            {
                                methodBlocks[methodCount].className = str.split("[\t]+")[methodTab+2].split("[:]+")[1] ;
                            }
                        }
                        methodCount++ ;

                        break ;
                    }
                    else
                    {
                        if (str.split("[\t]+")[i].equals ("p")) //I have got the package information.
                        {
                            packageName = str.split("[\t]+")[0] ;
                            break;
                        }
                    }
                }
            }

            //Assigning the package to all files of this method.
            for (int i=previousMethodCount;i<methodCount;i++)
            {
                methodBlocks[i].packageName = packageName;
            }

            //getting the last line number of all methods.
            for (int i=previousMethodCount;i<methodCount;i++ )
            {
                methodBlocks[i].endingLineNumber = getEndLineNumberOfMethod (methodBlocks[i].startingLineNumber, methodBlocks[i].methodName, methodBlocks[i].fileName, revision) ;
            }
        }
        catch (Exception e)
        {
            System.out.println ("retrieveMethodsFromFile") ;
            System.out.println (e) ;
        }

    } //public static void retrieveMethodsFromFile (String fileName) throws Exception


    //this method is used to get all methods of a folder.
    public void getAllMethods (int revision)
    {
        try
        {
            //System.out.println ("------> I am retrieving all methods of version: " + revision) ;

            String folderName = commonParameters.repositoryURL+ "/version-"+revision;

            File file = new File (folderName);
            if (!file.exists())
                return;
            

            //getting the file list of a folder.
            
            getAllFilesOfFolder (folderName, commonParameters.language_extension);

            //retrieving methods from files.
            retrieveMethodsFromFileNew(revision);

            //first way
            /*if (revision % 2 != 3)
            {
                retrieveMethodsFromFileNew(revision);
            }
            else
            {
                changedFileCount=0;
                unchangedFileCount=0;

                System.out.println ("\tgetting changed and unchanged files. begin");
                getChangedAndUnchangedFiles(revision);
                System.out.println ("\tgetting changed and unchanged files. end");
                retrieveMethodsFromUnchangedFiles (revision);
                retrieveMethodsFromChangedFiles (revision);
            }*/
        }
        catch (Exception e)
        {
            System.out.println ("getAllMethods") ;
            System.out.println (e) ;
        }

    }//public static void getAllMethodsOfFolder (String folderName) throws Exception

    public void getChangedAndUnchangedFiles (int revision)
    {
        try
        {
            System.out.println ("\tfile count = "+fileCount);
            int i=0, count=0;
            for (i =0;i<fileCount;i++)
            {
                String cfilepath = fileList[i];
                String temp = commonParameters.repositoryURL+"/version-"+revision;
                int l = temp.length();
                temp = cfilepath.substring(l+1);
                String pfilepath = commonParameters.repositoryURL+"/version-"+(revision-1)+"/"+temp;

                File pfile = new File (pfilepath);

                if (pfile.exists())
                {
                    System.out.println ("\tchecking "+i);
                    Process p = Runtime.getRuntime().exec(commonParameters.windiffCommandPath+" "+ cfilepath + " " + pfilepath);
                    BufferedReader br = new BufferedReader (new InputStreamReader (p.getInputStream()));

                    if (br.readLine () != null)
                    {
                        changedFiles[changedFileCount] = fileList[i];
                        changedFileCount++;
                    }
                    else
                    {
                        unchangedFiles[unchangedFileCount] = fileList[i];
                        unchangedFileCount++;
                    }
                }
                else
                {
                    changedFiles[changedFileCount] = fileList[i];
                    changedFileCount++;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getUnchangedFiles. "+e);
        }
    }

    
    public void retrieveMethodsFromChangedFiles (int revision)
    {
        for (int i =0;i<changedFileCount;i++)
        {
            retrieveMethodsFromFile (changedFiles[i], revision);
        }
    }

    public void retrieveMethodsFromUnchangedFiles (int revision)
    {
        retrieveExistingMethods(revision);
    }


    public void retrieveExistingMethods (int revision)
    {
        try
        {
            String temp = "", temp2 = "";
            int l = 0, j=0;
            temp2 = commonParameters.repositoryURL+"/version-"+revision;
            l = temp2.length();
            
            
            String str = "";
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.subject_system+"/methods/methods_version_"+(revision-1)+".txt")));
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}

                for (j=0;j<unchangedFileCount;j++)
                {
                    temp = unchangedFiles[j].substring(l+1);
                    if (commonParameters.getAttributeValueFromString(str, an.filePath).equals(temp))
                    {
                        break;
                    }
                }
                if (j<unchangedFileCount)
                {
                    existingMethods[existingMethodCount] = str;
                    existingMethodCount++;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = retrieveExistingMethods. "+e);
        }
    }


//        public void retrieveExistingMethods (int revision, String filepath)
//    {
//        try
//        {
//            String temp = "", temp2 = "";
//            int l = 0, j=0;
//            temp2 = commonParameters.repositoryURL+"/version-"+revision;
//            l = temp2.length();
//            temp = filepath.substring(l+1);
//
//            String str = "";
//            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.subject_system+"/methods/methods_version_"+(revision-1)+".txt")));
//            while ((str = br.readLine())!= null)
//            {
//                if (str.trim().length() == 0){continue;}
//
//                if (commonParameters.getAttributeValueFromString(str, "filepath").equals(temp))
//                {
//                    existingMethods[existingMethodCount] = str;
//                    existingMethodCount++;
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            System.out.println ("error in method = retrieveExistingMethods. "+e);
//        }
//    }


//    public void determineFileModificationList ()
//    {
//        try
//        {
//        int i =0, previous_version=currentVersion-1;
//        for (i=0;i<fileCount;i++)
//        {
//            fileModificationList[i] = 0;
//        }
//        Database dbase = new Database ();
//        String dfilename = "", cfilename="";
//
//        dbase.connect ();
//        dbase.executeQuery ("select distinct(filepath) from filemodification where version = "+previous_version);
//        while (dbase.result.next ())
//        {
//            dfilename = dbase.result.getString ("filepath");
//
//            for (i =0;i<fileCount;i++)
//            {
//                cfilename = fileList[i];
//                String t = commonParameters.repositoryURL+"/version-"+currentVersion+"";
//                int len = t.length() ;
//                //String filename = cfilename;
//                cfilename = cfilename.substring (len+1) ;
//                if (cfilename.equals(dfilename))
//                {
//                    fileModificationList[i]=1;
//                    break;
//                }
//            }
//        }
//        dbase.disconnect ();
//        }
//        catch (Exception e)
//        {
//            System.out.println ("determineFileModificationList");
//            System.out.println (e) ;
//        }
//
//    }

//    public void determineNewFiles ()
//    {
//        try
//        {
//       Database dbase = new Database ();
//       String [] old_file_list = new String [10000];
//       int old_file_count = 0, previous_version = currentVersion-1,i=0,j=0;
//
//       dbase.connect ();
//       dbase.executeQuery ("select distinct(filename) from method where version = "+previous_version) ;
//       while (dbase.result.next ())
//       {
//           old_file_list[old_file_count] = dbase.result.getString ("filename");
//           old_file_count++;
//       }
//       dbase.disconnect ();
//
//       for(i=0;i<fileCount;i++)
//       {
//            String cfilename = fileList[i];
//            String t = commonParameters.repositoryURL+"/version-"+currentVersion+"";
//            int len = t.length() ;
//            //String filename = cfilename;
//            cfilename = cfilename.substring (len+1) ;
//
//           for (j=0;j<old_file_count;j++)
//           {
//               if (old_file_list[j].equals(cfilename))
//               {
//                   break;
//               }
//           }
//            if (j==old_file_count)
//            {
//                fileModificationList[i] = 2;
//            }
//       }
//        }
//        catch (Exception e)
//        {
//            System.out.println ("determineNewFiles");
//            System.out.println (e) ;
//        }
//
//    }

//    public void getMethodsFromUnchangedFiles ()
//    {
//        try
//        {
//        int i=0, previous_version = currentVersion - 1;
//        Database dbase = new Database ();
//        String old_files = "";
//
//        for (i=0;i<fileCount;i++)
//        {
//            if (fileModificationList[i]==0)
//            {
//                String cfilename = fileList[i];
//                String t = commonParameters.repositoryURL+"/version-"+currentVersion+"";
//                int len = t.length() ;
//                //String filename = cfilename;
//                cfilename = cfilename.substring (len+1) ;
//
//                old_files = old_files + "'"+cfilename+"',";
//            }
//        }
//        if (old_files.length()>0)
//            old_files = old_files.substring(0, old_files.length()-1);
//        else
//            return;
//
//        dbase.connect ();
//        dbase.executeQuery ("select * from method where version = "+previous_version + " and filename in ("+old_files+") ");
//        while (dbase.result.next())
//        {
//            methodBlocks[methodCount] = new Method();
//            methodBlocks[methodCount].methodName = dbase.result.getString ("methodname");
//            methodBlocks[methodCount].fileName = dbase.result.getString ("filename");
//            methodBlocks[methodCount].signature = dbase.result.getString ("signature");
//            methodBlocks[methodCount].className = dbase.result.getString ("classname");
//            methodBlocks[methodCount].packageName = dbase.result.getString ("packagename");
//            methodBlocks[methodCount].startingLineNumber = Integer.parseInt(dbase.result.getString ("startinglinenumber"));
//            methodBlocks[methodCount].endingLineNumber = Integer.parseInt(dbase.result.getString ("endinglinenumber"));
//            methodCount++;
//        }
//        dbase.disconnect ();
//        }
//        catch (Exception e)
//        {
//            System.out.println ("getMethodsFromUnchangedFiles") ;
//            System.out.println (e) ;
//        }
//
//    }


    //this function gets all methods of a folder.
//    public void getAllFilesOfFolderNew (String folderName, String fileExtension, int revision)
//    {
//        try
//        {
//        //System.out.println ("------> I am retrieving all files of folder: "+ folderName) ;
//        File folder = new File(folderName);
//        File[] listOfFiles = folder.listFiles();
//
//        for (int i = 0; i < listOfFiles.length; i++)
//        {
//            if (listOfFiles[i].isFile())
//            {
//                if (fileExtension.indexOf(listOfFiles[i].getName().split("[.]+")[listOfFiles[i].getName().split("[.]+").length-1]) > -1)
//                {
//                    //inserting the java file names into a global array.
//                    //fileList[fileCount] = folderName + "/" + listOfFiles[i].getName() ;
//                    //fileCount++ ;
//                    String filepath = folderName + "/" + listOfFiles[i].getName() ;
//
//
//                    String cfilepath = filepath;
//                    String temp = commonParameters.repositoryURL+"/version-"+revision;
//                    int l = temp.length();
//                    temp = cfilepath.substring(l+1);
//                    String pfilepath = commonParameters.repositoryURL+"/version-"+(revision-1)+"/"+temp;
//
//                    File pfile = new File (pfilepath);
//
//                    if (pfile.exists())
//                    {
//                        Process p = Runtime.getRuntime().exec(commonParameters.windiffCommandPath+" "+ cfilepath + " " + pfilepath);
//                        BufferedReader br = new BufferedReader (new InputStreamReader (p.getInputStream()));
//                        if (br.readLine () != null)
//                        {
//                            changedFiles[changedFileCount] = filepath;
//                            changedFileCount++;
//                        }
//                        else
//                        {
//                            unchangedFiles[unchangedFileCount] = filepath;
//                            unchangedFileCount++;
//                        }
//                    }
//                    else
//                    {
//                        changedFiles[changedFileCount] = filepath;
//                        changedFileCount++;
//                    }
//                }
//            }
//            else if (listOfFiles[i].isDirectory())
//            {
//                getAllFilesOfFolderNew (folderName+"/"+listOfFiles[i].getName(),fileExtension, revision ) ;
//            }
//        }
//        }
//        catch (Exception e)
//        {
//            System.out.println ("getAllFilesOfFolderNew");
//            System.out.println (e) ;
//        }
//
//    } //public static void getAllMethodsOfFolder (String folderName)


    //this function gets all methods of a folder.
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
                    fileList[fileCount] = folderName + "/" + listOfFiles[i].getName() ;
                    fileCount++ ;
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
            System.out.println (e) ;
        }

    } //public static void getAllMethodsOfFolder (String folderName)
    
    
    public void fixMethods ()
    {
        try
        {
            for (int i =2;i<=cp.revisionCount;i++)
            {
                System.out.println ("working on revision = "+i);
                BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/methods/"+ "methods_version_"+i+".txt")));                
                String []  methods = new String [500000];
                int methodCount = 0;
                
                String str = "";
                while ((str = br.readLine())!= null)
                {
                    if (str.trim().length() == 0){continue;}
                    methodCount++;
                    if (methodCount > 0) {break;}
                }
                br.close ();
                if (methodCount == 0)
                {
                    int prevision = i-1;
                    BufferedReader br1 = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/methods/"+ "methods_version_"+prevision+".txt")));                
                    String []  pmethods = new String [500000];
                    int pmethodCount = 0;
                    
                    while ((str = br1.readLine())!= null)
                    {
                        if (str.trim().length() == 0) {continue;}
                        pmethods[pmethodCount] = str;
                        pmethodCount++;
                    }                    
                    br1.close ();
                    
                    if (pmethodCount > 0)
                    {
                        System.out.println ("fixing revision from previous revision.");
                        BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/methods/"+ "methods_version_"+i+".txt"));
                        for (int j=0;j<pmethodCount;j++)
                        {
                            writer.write ("\n");
                            writer.write (pmethods[j]);
                        }
                        writer.close();
                    }
                }
            }
        }
        catch (Exception e)
        {
            
        }        
        
    }
    
    public void restoreMethods ()
    {
        try
        {            
            for (int i =1;i<=cp.revisionCount;i++)
            {
                System.out.println ("working on revision = "+i);
                BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/methods/"+ "methods_version_"+i+".txt")));                
                String []  methods = new String [500000];
                int methodCount = 0;
                
                String str = "";
                while ((str = br.readLine())!= null)
                {
                    if (str.trim().length() == 0){continue;}
                    
                    String methodName = cp.getAttributeValueFromString(str, an.methodName);
                    //String methodID = cp.getAttributeValueFromString(str, an.methodID);
                    String signature = cp.getAttributeValueFromString(str, an.signature);
                    String filePath = cp.getAttributeValueFromString(str, an.filePath);
                    String className = cp.getAttributeValueFromString(str, an.className);
                    String packageName = cp.getAttributeValueFromString(str, an.packageName);
                    String startingLine = cp.getAttributeValueFromString(str, an.startingLine);
                    String endingLine = cp.getAttributeValueFromString(str, an.endingLine);
                    
                    String methodString = an.methodName + " = "+methodName+" : "
                            //+ an.methodID + " = "+methodID+" : "
                            + an.signature + " = "+signature+" : "
                            + an.filePath + " = " + filePath+" : "
                            + an.className + " = " + className+" : "
                            + an.packageName + " = "+packageName+" : "
                            + an.startingLine + " = "+startingLine + " : "
                            + an.endingLine + " = "+endingLine + " : ";
                    methods[methodCount] = methodString;
                    methodCount++;
                }
                br.close();
                
                BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/methods/"+ "methods_version_"+i+".txt"));
                for (int j=0;j<methodCount;j++)
                {
                    writer.write ("\n");
                    writer.write (methods[j]);
                }
                writer.close ();
            }
        }
        catch (Exception e)
        {
            System.out.println ("error.");
        }                        
        
    }
    
}
