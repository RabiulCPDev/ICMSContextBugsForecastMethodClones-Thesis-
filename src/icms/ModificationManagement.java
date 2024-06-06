/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.*;

/**
 *
 * @author manishankar
 */
public class ModificationManagement {

    //public int maxRevisionNumber = 0;
    public int modificationCount = 0;
    public int beginRevisionNumber = 0;
    public CommonParameters absolutePaths = new CommonParameters ();
    String insertQuery = "" ;
    public CommonParameters commonParameters = new CommonParameters ();

    String [] changes = new String[10000];
    int changeCount = 0;

    public AttributeNames an = new AttributeNames ();
    
    String [] queries = new String[5000000];
    int querycount = 0;
    
    DatabaseAccess da = new DatabaseAccess ();


    
    public void extractModifications ()
    {
        querycount = 0;
        for (int i =1;i<=commonParameters.revisionCount-1;i++)
        {
            System.out.println ("change extraction between "+i + " " + (i+1));
            getDifferencesBetweenVersions(commonParameters.subject_system+"/repository/version-"+i,i);
        }
        if (querycount > 0)
        {
            da.insertData(queries, querycount);
        }        
    }
    
    public void mainMethod ()
    {
        //storing the modifications to database.
        //getModifications ();

        /*for (int i =1;i<=commonParameters.revisionCount-1;i++)
        {
            storeModificationsToFile (i);
            System.out.println ("stored modification of commit = "+i);
        }*/
                
        //mapping the modifications to the methods and cloned methods.
        for (int i =1;i<= commonParameters.revisionCount-1;i++)
        {
            //mapModificationsToMethodsOfSpecificVersion (i);            
            /*mapModificationsToMethods (i);
            System.out.println ("mapped modification to methods version = "+i);*/
            
            //mapModificationsToClonedMethods (i);
            mapModificationsToMixedTypeClonedMethods (i);
            System.out.println ("mapped modification to cloned methods version = "+i);
        }
    }
    
    
    public void mapModificationsToMethods (int revision)
    {
        String [] methods = new String [5000];
        String [] changes = new String [5000];
        String [] count = new String [5000];

        String cfilepath="", mfilepath="";
        int msline, meline, csline;

        methods = commonParameters.getAllLinesFromFile(commonParameters.getMethodsPathOfRevision(revision));
        changes = commonParameters.getAllLinesFromFile(commonParameters.getChangesPathOfRevision(revision));        

        for (int i =0;changes[i] != null; i++)
        {
            cfilepath = commonParameters.getAttributeValueFromString(changes[i],an.filePath);
            csline = Integer.parseInt(commonParameters.getAttributeValueFromString(changes[i], an.startingLine));

            for (int j=0; methods[j] != null; j++)
            {
                mfilepath = commonParameters.getAttributeValueFromString(methods[j], an.filePath);
                msline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.startingLine));
                meline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.endingLine));

                if (cfilepath.equals(mfilepath))
                {
                    if (csline >= msline && csline <= meline)
                    {
                        if (count[j] == null)
                        {
                            count[j] = "";
                        }
                        count[j] += " 1";
                    }
                }
            }
        }

        for (int i=0;methods[i] != null;i++)
        {
            if (count[i] != null)
            {
                methods[i] += an.changeCount+" = "+ count[i].trim().split("[ ]+").length+""+commonParameters.separator;
            }
            else
            {
                methods[i] += an.changeCount+" = 0"+commonParameters.separator;
            }
        }

        commonParameters.writeAllLinesToFile(methods, commonParameters.getMethodsPathOfRevision(revision));
    }


    public void mapModificationsToMixedTypeClonedMethods (int revision)
    {
        String [] methods = new String [5000];
        String [] changes = new String [5000];
        String [] count = new String [5000];

        String cfilepath="", mfilepath="";
        int msline, meline, csline;

        methods = commonParameters.getAllLinesFromFile(commonParameters.getMixedTypeClonedMethodsPathOfRevision(revision));
        changes = commonParameters.getAllLinesFromFile(commonParameters.getChangesPathOfRevision(revision));

        for (int i =0;changes[i] != null; i++)
        {
            cfilepath = commonParameters.getAttributeValueFromString(changes[i], an.filePath);
            csline = Integer.parseInt(commonParameters.getAttributeValueFromString(changes[i], an.startingLine));

            for (int j=0; methods[j] != null; j++)
            {
                mfilepath = commonParameters.getAttributeValueFromString(methods[j], an.filePath);
                msline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.startingLine));
                meline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.endingLine));

                if (cfilepath.equals(mfilepath))
                {
                    if (csline >= msline && csline <= meline)
                    {
                        if (count[j] == null)
                        {
                            count[j] = "";
                        }

                        count[j] += " 1";
                    }
                }
            }
        }

        for (int i=0;methods[i] != null;i++)
        {
            if (count[i] != null)
            {
                methods[i] += an.cloneChangeCount+" = "+ count[i].trim().split("[ ]+").length+""+commonParameters.separator;
            }
            else
            {
                methods[i] += an.cloneChangeCount+" = 0"+commonParameters.separator;
            }
        }

        commonParameters.writeAllLinesToFile(methods, commonParameters.getMixedTypeClonedMethodsPathOfRevision(revision));
    }


    public void mapModificationToClones (int revision)
    {
        
    }
    

    public void mapModificationsToClonedMethods (int revision)
    {
        String [] methods = new String [5000];
        String [] changes = new String [5000];
        String [] count = new String [5000];

        String cfilepath="", mfilepath="";
        int msline, meline, csline;

        methods = commonParameters.getAllLinesFromFile(commonParameters.getClonedMethodsPathOfRevision(revision, commonParameters.clone_type_index));
        changes = commonParameters.getAllLinesFromFile(commonParameters.getChangesPathOfRevision(revision));

        for (int i =0;changes[i] != null; i++)
        {
            cfilepath = commonParameters.getAttributeValueFromString(changes[i], an.filePath);
            csline = Integer.parseInt(commonParameters.getAttributeValueFromString(changes[i], an.startingLine));

            for (int j=0; methods[j] != null; j++)
            {
                mfilepath = commonParameters.getAttributeValueFromString(methods[j], an.filePath);
                msline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.startingLine));
                meline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.endingLine));

                if (cfilepath.equals(mfilepath))
                {
                    if (csline >= msline && csline <= meline)
                    {
                        if (count[j] == null)
                        {
                            count[j] = "";
                        }

                        count[j] += " 1";
                    }
                }
            }
        }

        for (int i=0;methods[i] != null;i++)
        {
            if (count[i] != null)
            {
                methods[i] += an.cloneChangeCount+" = "+ count[i].trim().split("[ ]+").length+""+commonParameters.separator;
            }
            else
            {
                methods[i] += an.cloneChangeCount+" = 0"+commonParameters.separator;
            }
        }

        commonParameters.writeAllLinesToFile(methods, commonParameters.getClonedMethodsPathOfRevision(revision, commonParameters.clone_type_index));
    }

    
    
    

    public void storeModificationsToFile (int previousVersion)
    {
        getDifferencesBetweenVersions(commonParameters.getPathOfRevision(previousVersion),previousVersion);
        //commonParameters.writeAllLinesToFile(changes, commonParameters.getChangesPathOfRevision(previousVersion));
    }
    
    
    public void getDifferencesBetweenVersions (String previousVersionPath, int previousVersion)
    {
        try
        {
            File folder = new File(previousVersionPath);
            File[] listOfFiles = folder.listFiles();
            String tempfilename1 ="", tempfilename2="", tempstring="" ;

            if (listOfFiles == null) return ;

            for (int i = 0; i < listOfFiles.length; i++)
            {
                if (listOfFiles[i].isFile())
                {
                    if (absolutePaths.language_extension.indexOf(listOfFiles[i].getName().split("[.]+")[listOfFiles[i].getName().split("[.]+").length-1]) > -1)
                    {
                        tempfilename1 = previousVersionPath+"/"+listOfFiles[i].getName();
                        tempfilename1 = tempfilename1.replaceAll("\\\\","/");
                        tempstring = absolutePaths.repositoryURL+"/version-"+previousVersion;
                        int nextversion = previousVersion + 1 ;
                        tempfilename2 = absolutePaths.repositoryURL+"/version-"+nextversion+tempfilename1.substring(tempstring.length());

                        File file = new File (tempfilename2);
                        if (file.exists())
                        {
                            storeDifferencesBetweenFiles(tempfilename1,tempfilename2,previousVersion);
                        }
                    }
                }
                if (listOfFiles[i].isDirectory())
                {
                    if (!listOfFiles[i].getName().equals(".ccfxprepdir"))
                    {
                        getDifferencesBetweenVersions(previousVersionPath+"/"+listOfFiles[i].getName(),previousVersion);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println  ("error in method = getDifferencesBetweenVersions. "+e);
        }
    }

    public void storeDifferencesBetweenFiles (String filepath1, String filepath2, int previousversion)
    {
        try
        {
            String abspath1 = "", abspath2 = "";
            File file1 = new File (filepath1);
            File file2 = new File (filepath2);
            abspath1 = file1.getAbsolutePath();
            abspath2 = file2.getAbsolutePath();

            Process p = Runtime.getRuntime().exec(absolutePaths.windiffCommandPath+" "+ abspath1 + " " + abspath2);
            //p.waitFor ();

            BufferedReader br = new BufferedReader (new InputStreamReader (p.getInputStream()));

            String str = "", temp = "", tempfilepath = "", changetype="";
            int startingline = -1, endingline = -1;
            boolean b = false;
     
            temp = absolutePaths.repositoryURL+"/version-"+previousversion;
            tempfilepath = filepath1.substring(temp.length()+1);

            while ((str = br.readLine()) != null)
            {
                b = str.matches ("([0-9]+,)?[0-9]+[a|c|d][0-9]+(,[0-9]+)?") ;
                if (b == true)
                {
                    for (int i =0;i<str.length();i++)
                    {
                        if (str.charAt(i) == 'a' || str.charAt(i) == 'c' || str.charAt(i) == 'd')
                        {
                            startingline = -1;
                            endingline = -1;
                            changetype = str.charAt(i)+"";
                            temp = str.substring(0, i);
                            startingline = Integer.parseInt (temp.split("[,]+")[0]) ;
                            if (temp.split("[,]+").length > 1)
                            {
                                endingline = Integer.parseInt (temp.split("[,]+")[1]) ;
                            }
                            if (endingline == -1)
                            {
                                endingline = startingline;
                            }
                            
                            queries[querycount]  = "insert into changes (revision, filepath, changetype, startline, endline) values ("
                                    + "'"+previousversion+"',"
                                    + "'"+tempfilepath+"',"
                                    + "'"+changetype+"',"
                                    + "'"+startingline+"',"
                                    + "'"+endingline+"'"
                                    + ")";
                            
                            querycount++;
                            
                            if (querycount >= 1000)
                            {
                                da.insertData(queries, querycount);
                                querycount = 0;
                            }                            
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = storeDifferencesBetweenFiles. "+ e);
        }
                
    }
}
