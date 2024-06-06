/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.*;
import java.io.File;
import javax.swing.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

/**
 *
 * @author manishankar
 */
public class CloneDetection {

    public CommonParameters commonParameters = new CommonParameters();
    public CommonParameters cp = new CommonParameters();
    public CloneBlock [] cloneblocks = new CloneBlock[100000];
    public int cloneblockcount = 0;
    private int clone_class_count = 0;
    private CloneClass [] clone_classes = new CloneClass [500000];
    public Method [] methodBlocks = new Method[100000];
    int methodblockcount = 0;
    
    static ClonedMethod [] cloned_methods = new ClonedMethod[50000];
    static int cloned_method_count = 0;

    public AttributeNames an = new AttributeNames ();
    
    public DatabaseAccess da = new DatabaseAccess ();
    
    //String [] clones = new String[5000000];
    //int countofclones = 0;
    
    
    
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
    
    
    
    public void storeClonesToDatabase (int clonetype)
    {      
        //checking existing data.        
        if (confirmInsertion ("type"+clonetype+"clones")== false ){ return; }                
        
        for (int i =1;i<=commonParameters.revisionCount;i++)
        {                
            clone_class_count = 0;            
            try
            {
                storeClonesOfRevisionToDatabase (i, clonetype);
            }
            catch (Exception e)
            {
                System.out.println ("error. "+e);
                e.printStackTrace();
            }
        }
    }
    
    
    public void mapChangesToClones (int clonetype)
    {
        try
        {
            da.connect ();

            //getting the changes.
            String []filepaths = new String[40000];
            int [] slines = new int[40000];
            int [] elines = new int[40000];
            int [] revisions = new int[40000];
            int ccount = 0, i=0;
            da.executeQuery ("select filepath, startline, endline, revision from changes");
            while (da.result.next ())
            {
                filepaths[i] = da.result.getString("filepath");
                slines[i] = Integer.parseInt(da.result.getString("startline"));
                elines[i] = Integer.parseInt(da.result.getString("endline"));
                revisions[i] = Integer.parseInt(da.result.getString("revision"));
                i++;
            }
            ccount = i;
            da.disconnect();
            da.connect ();
            //mapping the changes.
            String fp = ""; int sl = 0, el = 0, rv = 0;
            for (i=0;i<ccount;i++)
            {
                System.out.println ("change = "+(i+1)+" of "+ccount);
                //System.out.println ("update type"+clonetype+"clones set changecount = changecount + 1 where revision = '"+rv+"' and filepath = '"+fp+"' and (("+sl+" >= startline and "+sl+" <= endline)or(startline >= "+sl+" and startline <= "+el+"))");
                fp = filepaths[i]; sl = slines[i]; el = elines[i]; rv = revisions[i];                
                da.executeUpdate ("update type"+clonetype+"clones set changecount = changecount + 1 where revision = '"+rv+"' and filepath = '"+fp+"' and (("+sl+" >= startline and "+sl+" <= endline)or(startline >= "+sl+" and startline <= "+el+"))");
            }

            da.disconnect ();
        }
        catch (Exception e)
        {
            
        }
    }
    
    
    public void mapChangesToClonesOld (int clonetype)
    {
        if (confirmInsertion ("type"+clonetype+"clonechanges")== false ){ return; }
                
        
        for (int i =1;i<=cp.revisionCount-1;i++)
        {
            System.out.println ("mapping changes to clones revision "+i);
            
            SingleChange [] changes = new SingleChange[10000];
            SingleClone [] clones = new SingleClone[10000];
                        
            changes = da.getChanges (i);
            clones = da.getClones(i, clonetype);
            
            //mapping changes.
            for (int j =0;clones[j] != null;j++)
            {
                int changecount = 0;
                String filepath1 = clones[j].filepath;
                int startline1 = Integer.parseInt(clones[j].startline);
                int endline1 = Integer.parseInt(clones[j].endline);
                for (int k=0;changes[k] != null; k++)
                {
                    String filepath2 = changes[k].filepath;
                    int startline2 = Integer.parseInt(changes[k].startline);
                    int endline2 = Integer.parseInt(changes[k].endline);
                    
                    //if clone falls within a change. or a change falls within a clone.
                    if ((filepath1.equals (filepath2) && (startline2 >= startline1) && (startline2 <= endline1))
                       ||(filepath1.equals (filepath2) && (startline1 >= startline2) && (startline1 <= endline2)))
                    {
                        changecount++;
                    }
                }
                clones[j].changecount = changecount+"";
                clones[j].clonetype = clonetype;
            }
            da.deleteClones (i, clonetype);
            da.insertClones(clones, clonetype);
        }
    }
    
    public void mapClonesToMethods (int clonetype)
    {             
        if (confirmInsertion ("type"+clonetype+"clonesinmethods")== false ){ return; }
        
        for (int r =1;r<=cp.revisionCount;r++)
        {
            System.out.println ("mapping clones to methods revision "+r);
            
            SingleClone [] clones = da.getClones (r, clonetype);
            SingleMethod [] methods = da.getMethods (r);
            
            for (int i =0;clones[i] != null;i++)
            {
                clones[i].clonetype = clonetype;
                String cfilepath = clones[i].filepath;
                int cstartline = Integer.parseInt(clones[i].startline);
                int cendline = Integer.parseInt(clones[i].endline);
                
                for (int j =0;methods[j] != null;j++)
                {
                    String mfilepath = methods[j].filepath;
                    int mstartline = Integer.parseInt(methods[j].startline);
                    int mendline = Integer.parseInt(methods[j].endline);
                    
                    //the clone fragment should be within the method fully.
                    if (cfilepath.equals(mfilepath) && cstartline >= mstartline && cstartline <= mendline && cendline >= mstartline && cendline <= mendline)
                    {
                        clones[i].methodid = methods[j].methodid;
                        break;
                    }
                }
            }
            da.deleteClones(r, clonetype);
            da.insertClones (clones, clonetype);
        }
    }
    
    
    
    
    
    public void storeAndMapClones (int cloneType)
    {
        try
        {            
            /*for (int i =1;i<=commonParameters.revisionCount;i++)
            {
                System.out.println ("maping type "+cloneType+" clones to version = "+i);
                mapClonesOfRevisionToMethods(i,cloneType);

                //System.out.println ("maping type 123 clones to version = "+i);
                //mapMixedTypeClonesOfRevisionToMethods (i);
            }*/
        }
        catch (Exception e)
        {
            
        }        
    }
    
    
    public void mainMethod ()
    {
        try
        {
            //storing the clones to database.
            for (int i =1;i<=commonParameters.revisionCount;i++)
            {                
                storeClonesOfVersionToFile (i,1);
                storeClonesOfVersionToFile (i,2);
                storeClonesOfVersionToFile (i,3);                
                storeMixedTypeClonesOfVersionToFile (i);
            }
            
            for (int i =1;i<=commonParameters.revisionCount;i++)
            {
                System.out.println ("maping type 1 clones to version = "+i);
                mapClonesOfRevisionToMethods(i,1);
                
                System.out.println ("maping type 2 clones to version = "+i);
                mapClonesOfRevisionToMethods(i,2);
                
                System.out.println ("maping type 3 clones to version = "+i);
                mapClonesOfRevisionToMethods(i,3);

                System.out.println ("maping type 123 clones to version = "+i);
                mapClonesOfRevisionToMethods(i,123);
                
                /*System.out.println ("maping type 123 clones to version = "+i);
                mapMixedTypeClonesOfRevisionToMethods (i);*/
            }

            //mapping clones to methods.
            /*for (int i =1;i<=commonParameters.revisionCount;i++)
            {
                determineClonedMethods(i);
                storeClonedMethodsToDatabase ();
            }*/
        }
        catch (Exception e)
        {
            System.out.println ("error. mainMethod. "+e);
        }
    }


    public void mapMixedTypeClonesOfRevisionToMethods (int revision)
    {
        try
        {
            String [] clonedMethods = new String [5000];
            int clonedMethodCount = 0;
            String [] methods = new String[5000];
            String [] clones = new String[5000];
            String methodFilePath = "", cloneFilePath ="";
            int msline, meline, csline, celine;

            //getting the methods of revision.
            methods = commonParameters.getAllLinesFromFile(commonParameters.getMethodsPathOfRevision(revision));

            //getting the clones of revision.
            clones = commonParameters.getAllLinesFromFile(commonParameters.getMixedTypeClonesPathOfRevision(revision));

            int i =0, j=0;
            for (i=0; clones[i] != null; i++)
            {
                for (j=0; methods[j] != null; j++)
                {
                    methodFilePath = commonParameters.getAttributeValueFromString (methods[j], an.filePath);
                    cloneFilePath = commonParameters.getAttributeValueFromString(clones[i], an.cloneFilePath);
                    if (methodFilePath.equals(cloneFilePath))
                    {
                        msline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.startingLine));
                        meline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.endingLine));
                        int gmid = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.globalMethodID));

                        csline = Integer.parseInt(commonParameters.getAttributeValueFromString (clones[i], an.cloneStartingLine));
                        celine = Integer.parseInt(commonParameters.getAttributeValueFromString (clones[i], an.cloneEndingLine));

                        if (csline >= msline && celine <= meline)
                        {
                            //got cloned method.
                            clonedMethods[clonedMethodCount] = an.globalMethodID + " = " + gmid + " : " + clones[i];
                            clonedMethodCount++;
                        }
                    }
                }
            }
            clonedMethods[clonedMethodCount] = null;
            commonParameters.writeAllLinesToFile(clonedMethods, commonParameters.getMixedTypeClonedMethodsPathOfRevision(revision));
        }
        catch (Exception e)
        {
            System.out.println ("error. in method = mapClonesOfRevisionToMethods. "+ e);
        }
    }

    
    public void mapClonesOfRevisionToMethods (int revision, int cloneTypeIndex)
    {
        try
        {
            String [] clonedMethods = new String [5000];
            int clonedMethodCount = 0;
            String [] methods = new String[5000];
            String [] clones = new String[5000];            
            String methodFilePath = "", cloneFilePath ="";
            int msline, meline, csline, celine;
            
            //getting the methods of revision.
            methods = commonParameters.getAllLinesFromFile(commonParameters.getMethodsPathOfRevision(revision));
            
            //getting the clones of revision.
            clones = commonParameters.getAllLinesFromFile(commonParameters.getClonesPathOfRevision(revision,cloneTypeIndex));
            
            int i =0, j=0;
            for (i=0; clones[i] != null; i++)
            {
                for (j=0; methods[j] != null; j++)
                {
                    methodFilePath = commonParameters.getAttributeValueFromString (methods[j], an.filePath);
                    cloneFilePath = commonParameters.getAttributeValueFromString(clones[i], an.cloneFilePath);
                    if (methodFilePath.equals(cloneFilePath))
                    {
                        msline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.startingLine));
                        meline = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.endingLine));
                        
                        int gmid = Integer.parseInt(commonParameters.getAttributeValueFromString(methods[j], an.globalMethodID));
                        
                        csline = Integer.parseInt(commonParameters.getAttributeValueFromString (clones[i], an.cloneStartingLine));
                        celine = Integer.parseInt(commonParameters.getAttributeValueFromString (clones[i], an.cloneEndingLine));
                        
                        if (csline >= msline && celine <= meline)
                        {
                            //got cloned method.
                            clonedMethods[clonedMethodCount] = an.globalMethodID + " = " + gmid + " : " + clones[i];
                            clonedMethodCount++;                            
                        }
                    }
                }
            }
            clonedMethods[clonedMethodCount] = null;
            commonParameters.writeAllLinesToFile(clonedMethods, commonParameters.getClonedMethodsPathOfRevision(revision, cloneTypeIndex));
        }
        catch (Exception e)
        {
            System.out.println ("error. in method = mapClonesOfRevisionToMethods. "+ e);
        }
    }
    
        
    class CloneClass
    {
        int fragment_count;
        CloneFragment [] clone_fragment = new CloneFragment[1000];
    }

    class CloneFragment
    {
        String file_path = "";
        int starting_line ;
        int ending_line;
    }

    public void getCloneInformation (int version, int clone_type) throws Exception
    {
        clone_class_count = 0;

        String clone_folder_name="",clone_file_name="";
        if (clone_type == 1)
        {
            clone_folder_name = "functions-clones" ; //type1
            clone_file_name = "functions-clones-0.00.xml" ;
        }
        else if (clone_type == 2)
        {
            clone_folder_name = "functions-blind-clones" ; //type2
            clone_file_name = "functions-blind-clones-0.00.xml" ;
        }
        else
        {
            clone_folder_name = "functions-blind-clones" ; //type3
            clone_file_name = "functions-blind-clones-0.30.xml" ;
        }

        int classcount = 0, clonecount=0, startingline=0, endingline=0, index=-1, len=0;
        String filepath = "";

        String xmlfilepath = commonParameters.repositoryURL + "/version-"+version+"_"+clone_folder_name+"/version-"+version+"_"+clone_file_name;
        String temp = commonParameters.repositoryURLLinux + "/version-"+version;


        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        File file = new File (xmlfilepath) ;
        if (!file.exists()) {return ;}
        Document doc = docBuilder.parse (file);

        // normalize text representation
        doc.getDocumentElement ().normalize ();
        System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

        NodeList classList = doc.getElementsByTagName("clone");
        Node singleClass = null;
        classcount = classList.getLength();

        System.out.println("Count of pairs : " + classcount);

        //int cloneid = 1;
        
        for (int i =0;i<classcount;i++)
        {
            singleClass = classList.item(i);
            //clonecount = Integer.parseInt(singleClass.getAttributes().item(1).getTextContent());
            clonecount = 2;

            Element singleClassElement = (Element)singleClass;
            NodeList filelist = singleClassElement.getElementsByTagName("source");

            clone_classes[clone_class_count] = new CloneClass();
            clone_classes[clone_class_count].fragment_count = clonecount;

            //System.out.println ("count of files = "+filelist.item(0).getAttributes().getLength()) ;
            for (int j =0;j<clonecount;j++)
            {
                Node singlefile = filelist.item(j);

                filepath = singlefile.getAttributes().item(1).getTextContent();
                int i1 = filepath.indexOf("version-"+version);
                int l1 = ("version-"+version).length();

                //filepath = filepath.substring(temp.length()+1);
                filepath = filepath.substring(i1+l1+1);

                /*if ( commonParameters.language_extension.indexOf("c") > 0 ||  commonParameters.language_extension.indexOf("cs") > 0 )
                {
                    filepath = filepath.substring(0,filepath.length()-8);
                }*/

                startingline = Integer.parseInt(singlefile.getAttributes().item(3).getTextContent());
                endingline = Integer.parseInt(singlefile.getAttributes().item(0).getTextContent());

                clone_classes[clone_class_count].clone_fragment[j] = new CloneFragment();
                clone_classes[clone_class_count].clone_fragment[j].file_path = filepath;
                clone_classes[clone_class_count].clone_fragment[j].starting_line = startingline;
                clone_classes[clone_class_count].clone_fragment[j].ending_line = endingline;
            }
            clone_class_count++;
        }
    }

    public int isMicroCloneClass (CloneClass clone_class)
    {
        for (int i =0;i<clone_class.fragment_count;i++)
        {
            int endline = clone_class.clone_fragment[i].ending_line;
            int startline = clone_class.clone_fragment[i].starting_line;
            
            if (endline-startline+1 <= 4) {return 1;}
        }
        
        return 0;
    }
    
    
    public int classExistence (CloneClass clone_class)
    {
        int i =0, j=0, l=0;
        for (i=0;i<clone_class_count;i++)
        {
            if (clone_classes[i].fragment_count == clone_class.fragment_count)
            {
                for (j=0;j<clone_class.fragment_count;j++)
                {
                    for (l=0;l<clone_classes[i].fragment_count;l++)
                    {
                        if (clone_class.clone_fragment[j].file_path.equals(clone_classes[i].clone_fragment[l].file_path) && clone_class.clone_fragment[j].starting_line == clone_classes[i].clone_fragment[l].starting_line && clone_class.clone_fragment[j].ending_line == clone_classes[i].clone_fragment[l].ending_line)
                            break;
                    }
                    if (l == clone_classes[i].fragment_count)
                        break;
                }
                if (j == clone_class.fragment_count)
                    return 1;
            }
        }
        return 0;
    }


    public void storeMixedTypeClonesOfVersionToFile (int version) throws Exception
    {
        int cloneTypeIndex = 3;
        BufferedWriter br = new BufferedWriter (new FileWriter (commonParameters.getMixedTypeClonesPathOfRevision(version)));
        System.out.println ("Storing clones of version - "+version) ;

        String clone_folder_name="",clone_file_name="";
        if (cloneTypeIndex == 1)
        {
            clone_folder_name = "functions-clones" ; //type1
            clone_file_name = "functions-clones-0.0.xml" ;
        }
        else if (cloneTypeIndex == 2)
        {
            clone_folder_name = "functions-blind-clones" ; //type2
            clone_file_name = "functions-blind-clones-0.0.xml" ;
        }
        else
        {
            clone_folder_name = "functions-blind-clones" ; //type3
            clone_file_name = "functions-blind-clones-0.3.xml" ;
        }

        /*if (cloneTypeIndex == 2)
        {
            getCloneInformation(version, 1);
        }
        if (cloneTypeIndex == 3)
        {
            getCloneInformation(version, 2);
        }*/

        CloneClass clone_class = new CloneClass ();
        for (int l=0;l<=100;l++)
        {
            clone_class.clone_fragment[l] = new CloneFragment ();
        }

        int classcount = 0, clonecount=0, startingline=0, endingline=0, index=-1, len=0;
        String filepath = "";

        String xmlfilepath = commonParameters.repositoryURL + "/version-"+version+"_"+clone_folder_name+"/version-"+version+"_"+clone_file_name;

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        File file = new File (xmlfilepath) ;
        if (!file.exists()) {return ;}
        Document doc = docBuilder.parse (file);

        // normalize text representation
        doc.getDocumentElement ().normalize ();
        System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

        NodeList classList = doc.getElementsByTagName("class");
        Node singleClass = null;
        classcount = classList.getLength();

        System.out.println("Cont of classes : " + classcount);

        int cloneid = 1;

        for (int i =0;i<classcount;i++)
        {
            singleClass = classList.item(i);
            clonecount = Integer.parseInt(singleClass.getAttributes().item(1).getTextContent());

            Element singleClassElement = (Element)singleClass;
            NodeList filelist = singleClassElement.getElementsByTagName("source");

            clone_class.fragment_count = clonecount;

            for (int j =0;j<clonecount;j++)
            {
                Node singlefile = filelist.item(j);

                filepath = singlefile.getAttributes().item(1).getTextContent();
                int i1 = filepath.indexOf("version-"+version);
                int l1 = ("version-"+version).length();


                //filepath = filepath.substring(temp.length()+1);
                filepath = filepath.substring(i1+l1+1);

                if ( commonParameters.language_extension.indexOf("c") > 0 ||  commonParameters.language_extension.indexOf("cs") > 0 )
                {
                    filepath = filepath.substring(0,filepath.length()-8);
                }

                startingline = Integer.parseInt(singlefile.getAttributes().item(3).getTextContent());
                endingline = Integer.parseInt(singlefile.getAttributes().item(0).getTextContent());

                clone_class.clone_fragment[j].file_path = filepath;
                clone_class.clone_fragment[j].starting_line = startingline;
                clone_class.clone_fragment[j].ending_line = endingline;
            }

            //if (classExistence (clone_class) == 0)
            if ( 1 > 0)
            {
                for (int j = 0;j<clonecount;j++)
                {
                    String cloneDetails = "";

                    cloneDetails += an.cloneClassID+" = "+(i+1)+commonParameters.separator;
                    cloneDetails += an.cloneID+" = "+cloneid+commonParameters.separator;
                    cloneDetails += an.cloneFilePath+" = "+clone_class.clone_fragment[j].file_path+commonParameters.separator;
                    cloneDetails += an.cloneStartingLine+" = "+clone_class.clone_fragment[j].starting_line+ commonParameters.separator;
                    cloneDetails += an.cloneEndingLine+" = "+clone_class.clone_fragment[j].ending_line+ commonParameters.separator;

                    br.write("\n"+cloneDetails);

                    cloneid++;
                }
            }
            else
            {
                System.out.println ("similar class found") ;
            }
        }
        br.close();
    }



    public void storeClonesOfVersionToFile (int version, int cloneTypeIndex) throws Exception
    {
        BufferedWriter br = new BufferedWriter (new FileWriter (commonParameters.getClonesPathOfRevision(version, cloneTypeIndex)));
        System.out.println ("Storing clones of version - "+version) ;        

        String clone_folder_name="",clone_file_name="";
        if (cloneTypeIndex == 1)
        {
            clone_folder_name = "functions-clones" ; //type1
            clone_file_name = "functions-clones-0.0.xml" ;
        }
        else if (cloneTypeIndex == 2)
        {
            clone_folder_name = "functions-blind-clones" ; //type2
            clone_file_name = "functions-blind-clones-0.0.xml" ;
        }
        else
        {
            clone_folder_name = "functions-blind-clones" ; //type3
            clone_file_name = "functions-blind-clones-0.2.xml" ;
        }

        if (cloneTypeIndex == 2)
        {
            getCloneInformation(version, 1);
        }
        if (cloneTypeIndex == 3)
        {
            getCloneInformation(version, 2);
        }

        CloneClass clone_class = new CloneClass ();
        for (int l=0;l<=100;l++)
        {
            clone_class.clone_fragment[l] = new CloneFragment ();
        }

        int classcount = 0, clonecount=0, startingline=0, endingline=0, index=-1, len=0;
        String filepath = "";

        String xmlfilepath = commonParameters.repositoryURL + "/version-"+version+"_"+clone_folder_name+"/version-"+version+"_"+clone_file_name;
        String temp = commonParameters.repositoryURLLinux + "/version-"+version;


        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        File file = new File (xmlfilepath) ;
        if (!file.exists()) {return ;}
        Document doc = docBuilder.parse (file);

        // normalize text representation
        doc.getDocumentElement ().normalize ();
        System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

        NodeList classList = doc.getElementsByTagName("class");
        Node singleClass = null;
        classcount = classList.getLength();

        System.out.println("Cont of classes : " + classcount);

        int cloneid = 1;

        for (int i =0;i<classcount;i++)
        {
            singleClass = classList.item(i);
            clonecount = Integer.parseInt(singleClass.getAttributes().item(1).getTextContent());

            Element singleClassElement = (Element)singleClass;
            NodeList filelist = singleClassElement.getElementsByTagName("source");

            clone_class.fragment_count = clonecount;

            for (int j =0;j<clonecount;j++)
            {
                Node singlefile = filelist.item(j);

                filepath = singlefile.getAttributes().item(1).getTextContent();
                int i1 = filepath.indexOf("version-"+version);
                int l1 = ("version-"+version).length();


                //filepath = filepath.substring(temp.length()+1);
                filepath = filepath.substring(i1+l1+1);

                if ( commonParameters.language_extension.indexOf("c") > 0 ||  commonParameters.language_extension.indexOf("cs") > 0 )
                {
                    filepath = filepath.substring(0,filepath.length()-8);
                }

                startingline = Integer.parseInt(singlefile.getAttributes().item(3).getTextContent());
                endingline = Integer.parseInt(singlefile.getAttributes().item(0).getTextContent());

                clone_class.clone_fragment[j].file_path = filepath;
                clone_class.clone_fragment[j].starting_line = startingline;
                clone_class.clone_fragment[j].ending_line = endingline;
            }

            if (classExistence (clone_class) == 0)
            {
                for (int j = 0;j<clonecount;j++)
                {
                    String cloneDetails = "";
//                    cloneblocks[cloneblockcount] = new CloneBlock();
//                    cloneblocks[cloneblockcount].cloneid = cloneid;
//                    cloneblocks[cloneblockcount].filepath = clone_class.clone_fragment[j].file_path;
//                    cloneblocks[cloneblockcount].startingline = clone_class.clone_fragment[j].starting_line;
//                    cloneblocks[cloneblockcount].endingline = clone_class.clone_fragment[j].ending_line;
//                    cloneblocks[cloneblockcount].familyid = i+1;
//                    cloneblockcount++;

                    cloneDetails += an.cloneClassID+" = "+(i+1)+commonParameters.separator;
                    cloneDetails += an.cloneID+" = "+cloneid+commonParameters.separator;
                    cloneDetails += an.cloneFilePath+" = "+clone_class.clone_fragment[j].file_path+commonParameters.separator;
                    cloneDetails += an.cloneStartingLine+" = "+clone_class.clone_fragment[j].starting_line+ commonParameters.separator;
                    cloneDetails += an.cloneEndingLine+" = "+clone_class.clone_fragment[j].ending_line+ commonParameters.separator;

                    br.write("\n"+cloneDetails);

                    cloneid++;
                }
            }
            else
            {
                System.out.println ("similar class found") ;
            }
        }
        br.close();
    }

    public void storeClonesOfRevisionToDatabase (int version, int cloneTypeIndex) throws Exception
    {
        System.out.println ("Storing clones of version - "+version) ;
        
        SingleClone [] clonefragments = new SingleClone[50000];
        int clonefragmentcount = 0;
        
        SingleClonePair [] clonepairs = new SingleClonePair[500000];
        int clonepaircount = 0;
        
        SingleClone clonefragment = new SingleClone ();
        SingleClonePair clonepair = new SingleClonePair ();
        
        clone_class_count = 0;

        String clone_folder_name="",clone_file_name="";
        if (cloneTypeIndex == 1)
        {
            clone_folder_name = "functions-clones" ; //type1
            clone_file_name = "functions-clones-0.00.xml" ;
        }
        else if (cloneTypeIndex == 2)
        {
            clone_folder_name = "functions-blind-clones" ; //type2
            clone_file_name = "functions-blind-clones-0.00.xml" ;
        }
        else
        {
            clone_folder_name = "functions-blind-clones" ; //type3
            clone_file_name = "functions-blind-clones-0.30.xml" ;
        }

        if (cloneTypeIndex == 2)
        {
            getCloneInformation(version, 1);
        }
        if (cloneTypeIndex == 3)
        {
            getCloneInformation(version, 2);
        }

        CloneClass clone_class = new CloneClass ();
        for (int l=0;l<2;l++)
        {
            clone_class.clone_fragment[l] = new CloneFragment ();
        }

        int classcount = 0, clonecount=0, startingline=0, endingline=0;
        String filepath = "";
        String xmlfilepath = commonParameters.repositoryURL + "/version-"+version+"_"+clone_folder_name+"/version-"+version+"_"+clone_file_name;

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        File file = new File (xmlfilepath) ;
        if (!file.exists()) {return ;}
        Document doc = docBuilder.parse (file);

        // normalize text representation
        doc.getDocumentElement ().normalize ();
        System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

        NodeList classList = doc.getElementsByTagName("clone");
        Node singleClass = null;
        classcount = classList.getLength();

        System.out.println("Cont of pairs : " + classcount);

        for (int i =0;i<classcount;i++)
        {
            singleClass = classList.item(i);
            //clonecount = Integer.parseInt(singleClass.getAttributes().item(1).getTextContent());
            clonecount = 2;

            Element singleClassElement = (Element)singleClass;
            NodeList filelist = singleClassElement.getElementsByTagName("source");

            clone_class.fragment_count = clonecount;

            for (int j =0;j<clonecount;j++)
            {
                Node singlefile = filelist.item(j);

                filepath = singlefile.getAttributes().item(1).getTextContent();
                int i1 = filepath.indexOf("version-"+version);
                int l1 = ("version-"+version).length();
                filepath = filepath.substring(i1+l1+1);

                /*if ( commonParameters.language_extension.indexOf("c") > 0 ||  commonParameters.language_extension.indexOf("cs") > 0 )
                {
                    filepath = filepath.substring(0,filepath.length()-8);
                }*/

                startingline = Integer.parseInt(singlefile.getAttributes().item(3).getTextContent());
                endingline = Integer.parseInt(singlefile.getAttributes().item(0).getTextContent());

                clone_class.clone_fragment[j].file_path = filepath;
                clone_class.clone_fragment[j].starting_line = startingline;
                clone_class.clone_fragment[j].ending_line = endingline;
            }

            int microclones = isMicroCloneClass (clone_class);
            if (microclones == 1) { continue; }
            
            if (classExistence (clone_class) == 0)
            {
                //saving each fragment in the class.
                int [] cloneids = new int[10];
                for (int j = 0;j<clonecount;j++)
                {
                    clonefragment.revision = version+"";
                    clonefragment.filepath = clone_class.clone_fragment[j].file_path;
                    clonefragment.startline = clone_class.clone_fragment[j].starting_line+"";
                    clonefragment.endline = clone_class.clone_fragment[j].ending_line+"";
                    
                    int n =0;
                    for (n=0;n<clonefragmentcount;n++)
                    {
                        if (clonefragments[n].filepath.equals(clonefragment.filepath) && clonefragments[n].startline.equals(clonefragment.startline) && clonefragments[n].endline.equals(clonefragment.endline))
                        {    
                            //The clone fragment was previously inserted.
                            break;
                        }
                    }
                    cloneids[j] = n;
                    
                    if (n == clonefragmentcount) //the clone fragment not found.
                    {
                        clonefragments[clonefragmentcount] = new SingleClone();  
                        clonefragments[clonefragmentcount].revision = version+"";
                        clonefragments[clonefragmentcount].filepath = clonefragment.filepath;
                        clonefragments[clonefragmentcount].startline = clonefragment.startline;
                        clonefragments[clonefragmentcount].endline = clonefragment.endline;
                        clonefragments[clonefragmentcount].cloneid = clonefragmentcount+"";
                        
                        clonefragmentcount++;
                    }                    
                }
                
                //saving the class.
                clonepairs[clonepaircount] = new SingleClonePair ();
                clonepairs[clonepaircount].revision = version+"";
                clonepairs[clonepaircount].cloneid1 = cloneids[0]+"";
                clonepairs[clonepaircount].cloneid2 = cloneids[1]+"";
                
                clonepaircount++;
            }
            else
            {
                System.out.println ("similar pair found") ;
            }
        } 
        
        da.insertClones (clonefragments, cloneTypeIndex);
        da.insertClonePairs (clonepairs, cloneTypeIndex);
    }    
    
    
    public void fixClonedMethods (int cloneType)
    {
        try
        {
            for (int i =2;i<=cp.revisionCount;i++)
            {
                System.out.println ("working on revision = "+i);
                String str = "";
                /*BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/"+ "type"+cloneType+"_clonedmethods_version_"+i+".txt")));                
                String []  methods = new String [500000];
                int methodCount = 0;
                
                
                while ((str = br.readLine())!= null)
                {
                    if (str.trim().length() == 0){continue;}
                    methodCount++;
                    if (methodCount > 0) {break;}
                }
                br.close ();*/
                
                File file = new File (cp.subject_system+"/repository/version-"+i+"_functions-blind-clones");
                File file2 = new File (cp.subject_system+"/repository/version-"+i+"_functions-clones");
                
                if (!file.exists() && !file2.exists())
                //if (methodCount == 0)
                {
                    int prevision = i-1;
                    BufferedReader br1 = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system+"/clonedmethods/"+ "type"+cloneType+"_clonedmethods_version_"+prevision+".txt")));                
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
                        BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/clonedmethods/"+ "type"+cloneType+"_clonedmethods_version_"+i+".txt"));
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

}