/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author mani
 */


class Diff
{
    String diff1 = "";
    String diff2 = "";
    boolean gotDiff = true;
}

class InitialSetup
{
    String subjectSystem = "";
    String repositoryPath = "";
    String language="";
    int revisionCount = 0;
    String connectionString = "";
    String separator = "";
    int cloneTypeIndex = 0;

    InitialSetup()
    {
        
    }    
}

class AttributeNames
{
    public String methodName = "!!__methodname__!!";
    public String signature = "!!__signature__!!";
    public String className = "!!__classname__!!";
    public String packageName = "!!__packagename__!!";
    public String filePath = "!!__filepath__!!";
    public String startingLine = "!!__startingline__!!";
    public String endingLine = "!!__endingline__!!";
    public String methodID = "!!__methodid__!!";
    public String globalMethodID = "!!__globalmethodid__!!";
    public String changeCount = "!!__changecount__!!";
    public String actualMethodChangeCount = "!!__actualmethodchangecount__!!";
    public String methodChangeCount = "!!__methodchangecount__!!";

    public String cloneClassID = "!!__cloneclassid__!!";
    public String cloneID = "!!__cloneid__!!";
    public String cloneFilePath = "!!__clonefilepath__!!";
    public String cloneStartingLine = "!!__clonestartingline__!!";
    public String cloneEndingLine = "!!__cloneendingline__!!";
    public String cloneChangeCount = "!!__clonechangecount__!!";
    public String globalCloneID = "!!__globalcloneid__!!";
    public String actualCloneChangeCount = "!!__actualclonechangecount__!!";

    public String changeType = "!!__changetype__!!";
}

class RuleCondition
{
    float xyConfidence_min = 0.0f, xyConfidence_max = 0.0f ;
    float yxConfidence_min = 0.0f, yxConfidence_max = 0.0f;
    int support_min = 0;
    int support_max = 0;

    int cloned = 0, antecedent = -1, consequent = -1;
    String methodLocation = "";
}


public class CommonParameters {
    String subject_system = "carol";
    int revisionCount = 1699;
    String repositoryWebURL = "";
    String repositoryURL = "h:/systems/"+subject_system+"/repository" ;
    String connectionString = "jdbc:mysql://localhost:3306/lozano_imprint_carol_nicad" ;
    String database_name = "";
    String svnCommandPath = "C:/PROJECTS-CCFINDERX/lozano_imprint/lozano_imprint/Tools/SlikSvn/bin/svn" ;
    String ccfinderCommandPath = "tools/ccfinder/bin/ccfx" ;
    String windiffCommandPath = "tools/diffwin/bin/diff " ;
    String ctagsCommandPath = "ctags " ;
    String uncommentToolPath = "tools/uncomment.exe" ;
    String separator = "";
    String system_language="";
    //int maxGlobalMethodID = 0;

    //this is for c/c++

    //String language_extension = " c h ";
    //String ccfinder_language_command = "cpp";
    //String preprocessed_file_extension = ".cpp.2_0_0_2.default.ccfxprep";
    //String method_symbol = "f";

    //this is for C#

    //String language_extension = " cs ";
    //String ccfinder_language_command = "csharp";
    //String preprocessed_file_extension = ".csharp.2_0_0_0.default.ccfxprep";
    //String method_symbol = "m";

    //this is for java

    String language_extension = " java ";
    String ccfinder_language_command = "java";
    String preprocessed_file_extension = ".java.2_0_0_0.default.ccfxprep";
    String method_symbol = "m";

    //nicad settings.
    public String repositoryURLLinux = "/media/Education/subjectsystems/javasystems/"+subject_system+"/repository";
    public int clone_type_index = 1;

    AttributeNames an = new AttributeNames ();

    CommonParameters ()
    {
        String language = "";
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream("common_parameters.txt")));
            subject_system = br.readLine ().split("[=]+")[1].trim();
            repositoryURL = br.readLine ().split("[=]+")[1].trim();
            language =  br.readLine ().split("[=]+")[1].trim();
            revisionCount = Integer.parseInt (br.readLine ().split("[=]+")[1].trim());
            connectionString = br.readLine ().split("[=]+")[1].trim();
            separator = " "+br.readLine ().split("[=]+")[1].trim()+" ";
            clone_type_index = Integer.parseInt(br.readLine ().split("[=]+")[1].trim());
            //maxGlobalMethodID = Integer.parseInt(br.readLine());
            br.close();
            
            svnCommandPath = "C:/PROJECTS-CCFINDERX/lozano_imprint/lozano_imprint/Tools/SlikSvn/bin/svn" ;
            ccfinderCommandPath = "tools/ccfinder/bin/ccfx" ;
            windiffCommandPath = "tools/diffwin/bin/diff " ;
            //windiffCommandPath = "diff " ;
            //ctagsCommandPath = "tools/ctags/ctags" ;
            ctagsCommandPath = "ctags " ;
            //ctagsCommandPath = "ctags" ;
            uncommentToolPath = "tools/uncomment.exe" ;
            system_language = language;    
            if (language.equals("java"))
            {
                language_extension = " java ";
                ccfinder_language_command = "java";
                preprocessed_file_extension = ".java.2_0_0_0.default.ccfxprep";
                method_symbol = "m";
            }
            else if (language.equals("c"))
            {
                language_extension = " c h ";
                ccfinder_language_command = "cpp";
                preprocessed_file_extension = ".cpp.2_0_0_2.default.ccfxprep";
                method_symbol = "f";
            }
            else if (language.equals("cs"))
            {
                language_extension = " cs ";
                ccfinder_language_command = "csharp";
                preprocessed_file_extension = ".csharp.2_0_0_0.default.ccfxprep";
                method_symbol = "m";
            }

            /*System.out.println ("The parameters are set.");
            System.out.println ("subject system  ="+subject_system);
            System.out.println ("repositoryURL = "+repositoryURL);
            System.out.println ("language = "+language);
            System.out.println ("revisionCount = "+revisionCount);
            System.out.println ("connectionString = "+connectionString);*/
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = setParameters");
        }
    }
    
    String getCloneOfRevision  (int cid, int revision, int cloneType)
    {
        try
        {
            String str="";
            BufferedReader breader = new BufferedReader (new InputStreamReader (new FileInputStream(subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            while ((str = breader.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                if (Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID)) == cid)
                {
                    return str;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneOfRevision. "+e);
        }
        return "";        
    }

    String getMethodOfRevision (int gmid, int revision)
    {
        try
        {
            String str="";
            BufferedReader breader = new BufferedReader (new InputStreamReader (new FileInputStream(subject_system+"/methods/methods_version_"+revision+".txt")));
            while ((str = breader.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                if (Integer.parseInt(getAttributeValueFromString(str, "globalmethodid")) == gmid)
                {
                    return str;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodOfRevision. "+e);
        }
        return "";
    }

    String getClonedMethodOfRevision (int gmid, int revision, int type)
    {
        try
        {
            String str="";
            BufferedReader breader = new BufferedReader (new InputStreamReader (new FileInputStream(subject_system+"/clonedmethods/type"+type+"_clonedmethods_version_"+revision+".txt")));
            while ((str = breader.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                if (Integer.parseInt(getAttributeValueFromString(str, "globalmethodid")) == gmid)
                {
                    return str;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodOfRevision. "+e);
        }
        return "";        
    }


    String getPathOfRevision (int revision)
    {
        return repositoryURL+ "/version-"+revision;
    }

    String getMethodsPathOfRevision (int revision)
    {
        return subject_system+"/methods/methods_version_"+revision+".txt";
    }

    String getChangesPathOfRevision (int revision)
    {
        return subject_system+"/changes/changes_version_"+revision+".txt";
    }
    
    String getClonesPathOfRevision (int revision, int cloneTypeIndex)
    {
        return subject_system+"/clones/type"+cloneTypeIndex+"_clones_version_"+revision+".txt";
    }

    String getMixedTypeClonesPathOfRevision (int revision)
    {
        return subject_system+"/clones/type123_clones_version_"+revision+".txt";
    }
    
    String getClonedMethodsPathOfRevision (int revision, int cloneTypeIndex)
    {
        return subject_system+"/clonedmethods/type"+cloneTypeIndex+"_clonedmethods_version_"+revision+".txt";
    }

    String getMixedTypeClonedMethodsPathOfRevision (int revision)
    {
        return subject_system+"/clonedmethods/type123_clonedmethods_version_"+revision+".txt";
    }
    
    String [] getAllLinesFromFile (String filePath)
    {
        String [] lines = new String[5000];
        String line="";
        int lineCount=0;
        
        try
        {
            //opening methods file.
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(filePath)));
            while ((line = br.readLine())!= null)
            {
                if (line.trim().length() == 0) {continue;}
                lines[lineCount] = line;
                lineCount++;
            }
            return lines;
        }
        catch(Exception e)
        {
            System.out.println ("error in method = getAllLinesFromFile. "+e);
        }
        return lines;
    }
    
    void writeAllLinesToFile (String [] lines, String filePath)
    {
        try
        {
            int i =0;
            BufferedWriter br = new BufferedWriter (new FileWriter (filePath));
            while (lines[i] != null)
            {
                br.write ("\n"+lines[i]);
                i++;
            }
            br.close();
        }
        catch (Exception e)
        {
            System.out.println ("error in method = writeAllLinesToFile. "+e);
        }
    }

    int getMaxGlobalMethodID ()
    {
        try
        {
            String line = "";
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(subject_system+"/maxglobalmethodid.txt")));
            line = br.readLine ();
            return Integer.parseInt(line);
            
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = getMaxGlobalMethodID." + e);
        }
        return 0;
    }
    void setMaxGlobalMethodID (int gmid)
    {
        try
        {
            String line = "";
            BufferedWriter br = new BufferedWriter (new FileWriter (subject_system+"/maxglobalmethodid.txt"));
            br.write(gmid+"");
            br.close();
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = getMaxGlobalMethodID." + e);
        }        
    }

    //getting the method attribute section.

    String getAttributeValueFromString (String str, String attributeName)
    {
        try
        {
            StringBuilder temp = new StringBuilder();
            int i1 = str.indexOf(attributeName, 0), i2=0;
            int i =0, l = str.length();
            for (i=i1;i<l;i++)
            {
                if (str.charAt(i) == '=')
                {
                    i2 = i+1;
                    break;
                }
            }            
            for (i = i2;i<l;i++)
            {
                if (str.charAt(i) == ':'){break;}
                temp.append(str.charAt(i));
            }
            String result = temp.toString();            
            
            return result.trim();
            
            /*int i2 = str.indexOf(separator.trim(), i1);
            if (i2 != -1)
            {
                temp = str.substring(i1, i2);
            }
            else
            {
                temp = str.substring(i1);
            }
            return temp.split("[=]+")[1].trim();*/
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getAttributeValueFromString. \nattribute name = "+attributeName+"\n input string = " + str + "\n" +e);
            return "";
        }        
    }


    //gmid = global method id.
    String getMethodString (int globalMethodID, String filePath)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(filePath)));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int m = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));

                if (m == globalMethodID) //method is obtained.
                {
                    br.close();
                    return str;
                }
            }
            br.close();
            
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodString. "+ e);
        }
        return "";
    }

    void copyFile (String fromFile, String toFile)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter (new FileWriter (toFile));

            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(fromFile)));
            String str = "";
            while ((str = br.readLine())!= null)
            {
                writer.write(str+"\n");
            }
            br.close();
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println ("error in method = copyFile. "+e);
        }
    }

    public String [] getFileDiff (String file1, String file2) throws Exception
    {
        Process p = Runtime.getRuntime().exec("tools/diffwin/bin/diff "+file1+" "+file2+"");
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        String [] diff = new String [500000];
        int lineCount = 0;

        while ((line = in.readLine()) != null)
        {
            diff[lineCount] = line;
            lineCount++;
        }
        in.close();

        return diff;
    }


//
//    int getGlobalMethodID (String methodString)
//    {
//        return Integer.parseInt (methodString.split("[:]+")[8].split("[=]+")[1].trim());
//    }
//
//    String getFilePathOfMethod (String methodString)
//    {
//        return methodString.split("[:]+")[3].split("[=]+")[1].trim();
//    }

//    String getClassNameOfMethod (String methodString)
//    {
//        return methodString.split("[:]+")[4].split("[=]+")[1].trim();
//    }
//
//    String getPackageNameOfMethod (String methodString)
//    {
//        return methodString.split("[:]+")[5].split("[=]+")[1].trim();
//    }
//
//    String getSignatureOfMethod (String methodString)
//    {
//        return methodString.split("[:]+")[2].split("[=]+")[1].trim();
//    }
//
//    int getStartingLineNumberOfMethod (String methodString)
//    {
//        return Integer.parseInt(methodString.split("[:]+")[6].split("[=]+")[1].trim());
//    }
//    int getEndingLineNumberOfMethod (String methodString)
//    {
//        return Integer.parseInt(methodString.split("[:]+")[7].split("[=]+")[1].trim());
//    }
//
//    int getChangeCountOfMethod (String methodString)
//    {
//        return Integer.parseInt(methodString.split("[:]+")[9].split("[=]+")[1].trim());
//    }
//
//    String getMethodNameOfMethod(String methodString)
//    {
//        return methodString.split("[:]+")[1].split("[=]+")[1].trim();
//    }
//
//
//    int getCloneStartingLineNumberOfMethod (String methodString)
//    {
//        return Integer.parseInt(methodString.split("[:]+")[12].split("[=]+")[1].trim());
//    }
//
//    int getCloneEndingLineNumberOfMethod (String methodString)
//    {
//        return Integer.parseInt(methodString.split("[:]+")[13].split("[=]+")[1].trim());
//    }
//
//    int getCloneFamilyIDOfMethod (String methodString)
//    {
//        return Integer.parseInt(methodString.split("[:]+")[9].split("[=]+")[1].trim());
//    }

    //getting the clones attributes section.
//
//    String getFilePathOfClone (String cloneString)
//    {
//        return cloneString.split("[:]+")[2].split("[=]+")[1].trim();
//    }
//
//    int getStartingLineNumberOfClone (String cloneString)
//    {
//        return Integer.parseInt(cloneString.split("[:]+")[3].split("[=]+")[1].trim());
//    }
//
//    int getEndingLineNumberOfClone (String cloneString)
//    {
//        return Integer.parseInt(cloneString.split("[:]+")[4].split("[=]+")[1].trim());
//    }

    //getting the change attribute section.
    
//    String getFilePathOfChange (String changeString)
//    {
//        return changeString.split("[:]+")[0].split("[=]+")[1].trim();
//    }
//
//    int getStartingLineNumberOfChange (String changeString)
//    {
//        return Integer.parseInt(changeString.split("[:]+")[2].split("[=]+")[1].trim());
//    }
//
//    int getEndingLineNumberOfChange (String changeString)
//    {
//        return Integer.parseInt(changeString.split("[:]+")[3].split("[=]+")[1].trim());
//    }

    public double getStrikeAMatch (String str1, String str2)
    {
        return compareStrings(str1, str2)*100;
    }

    public double compareStrings(String str1, String str2)
    {
        try
        {
            ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
            ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());

            int intersection = 0;
            int union = pairs1.size() + pairs2.size();

            for (int i=0; i<pairs1.size(); i++)
            {
                Object pair1=pairs1.get(i);

                for(int j=0; j<pairs2.size(); j++)
                {
                    Object pair2=pairs2.get(j);
                    if (pair1.equals(pair2))
                    {
                        intersection++;
                        pairs2.remove(j);
                        break;
                    }
                }
            }

            if (union == 0)
                return 0 ;

            return (2.0*intersection)/union ;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = compareStrings. "+e);
            return 0.0;
        }
    }


    private ArrayList wordLetterPairs(String str)
    {
        ArrayList allPairs = new ArrayList();

        try
        {
            // Tokenize the string and put the tokens/words into an array
            String[] words = str.split("\\s");

            // For each word
            for (int w=0; w < words.length; w++)
            {
                // Find the pairs of characters
                if (words[w].trim().length() > 0)
                {
                    String[] pairsInWord = letterPairs(words[w]);

                    for (int p=0; p < pairsInWord.length; p++)
                    {
                        allPairs.add(pairsInWord[p]);
                    }
                }
            }
            return allPairs;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = wordLetterPairs."+e);
        }
        return null;
    }

    private String[] letterPairs(String str)
    {
        try
        {
            int numPairs = str.length()-1;
            String[] pairs = new String[numPairs];
            for (int i=0; i<numPairs; i++)
            {
                pairs[i] = str.substring(i,i+2);
            }
            return pairs;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = letterPairs." + e);
        }
        return null;
    }
    
    
    
    
    //--------------------------------------------------------------------------------------------------
    //getting parameters for clones.
    //--------------------------------------------------------------------------------------------------
    
    public int getCloneClass (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                if (cid == cloneID)
                {
                    int classID = Integer.parseInt(getAttributeValueFromString(str, an.cloneClassID));
                    br.close();
                    return classID;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneClass (int cloneID, int revision). "+e);
        }
        
        return -1;
    }
    
    public int getCloneStartingLine (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                if (cid == cloneID)
                {
                    int sline = Integer.parseInt(getAttributeValueFromString(str, an.cloneStartingLine));
                    br.close();
                    return sline;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneStartingLine (int cloneID, int revision). "+e);
        }
        
        return -1;        
    }
    
    public int getCloneEndingLine (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                if (cid == cloneID)
                {
                    int eline = Integer.parseInt(getAttributeValueFromString(str, an.cloneEndingLine));
                    br.close();
                    return eline;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneEndingLine (int cloneID, int revision). "+e);
        }
        
        return -1;        
    }
    
    public String getCloneFile (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                if (cid == cloneID)
                {
                    String file = getAttributeValueFromString(str, an.cloneFilePath);
                    br.close();
                    return file;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getCloneEndingLine (int cloneID, int revision). "+e);
        }
        
        return "";                
    }
    
    public String getClonesInClass (int classID, int revision, int cloneType)
    {
        String clones = "";
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int clsid = Integer.parseInt(getAttributeValueFromString(str, an.cloneClassID));
                if (clsid == classID)
                {
                    int cloneID = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                    clones += " " + cloneID + " ";
                }
            }
            return clones;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getClass (int cloneID, int revision). "+e);
        }        
        
        return "";
        
    }
        
    
    //--------------------------------------------------------------------------------------------------
    //getting parameters for methods.
    //--------------------------------------------------------------------------------------------------
    
    public String getMethodFile (int methodID, int revision)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/methods/methods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                if (mid == methodID)
                {
                    String file = getAttributeValueFromString(str, an.filePath);
                    br.close();
                    return file;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodFile (int methodID, int revision). "+e);
        }
        
        return "";                        
    }
    
    public int getMethodStartingLine (int methodID, int revision)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/methods/methods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                if (mid == methodID)
                {
                    int sline = Integer.parseInt(getAttributeValueFromString(str, an.startingLine));
                    br.close();
                    return sline;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodStartingLine (int methodID, int revision). "+e);
        }
        
        return -1;                
    }

    public int getMethodEndingLine (int methodID, int revision)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/methods/methods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                if (mid == methodID)
                {
                    int eline = Integer.parseInt(getAttributeValueFromString(str, an.endingLine));
                    br.close();
                    return eline;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodEndingLine (int methodID, int revision). "+e);
        }
        
        return -1;                
    }
    
    public int getContainerMethodID (int cloneID, int revision, int cloneType)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/clonedmethods/type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int cid = Integer.parseInt(getAttributeValueFromString(str, an.globalCloneID));
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                if (cid == cloneID)
                {
                    br.close ();
                    return mid;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = public int getContainerMethodID (int cloneID, int revision). "+e);
        }
        
        return -1;
    }
    
    public String getMethodName (int methodID, int revision)
    {
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system+"/methods/methods_version_"+revision+".txt")));
            String str = "";
            while ((str=br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int mid = Integer.parseInt(getAttributeValueFromString(str, an.globalMethodID));
                if (mid == methodID)
                {
                    String mname = getAttributeValueFromString(str, an.methodName);
                    br.close();
                    return mname;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = getMethodEndingLine (int methodID, int revision). "+e);
        }
        
        return "";                
        
    }
    
    public String getCode (int revision, String file, int startLine, int endLine)
    {
        String code = "";
        
        try
        {
            int line = 0;
            int effectiveRevision = revision;
            
            while (effectiveRevision >= 1)
            {
                File f = new File (subject_system + "/repository/version-"+effectiveRevision+"/" +file);
                if (f.exists())
                {
                    break;
                }
                effectiveRevision--;
            }
            
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (subject_system + "/repository/version-"+effectiveRevision+"/" +file)));
            String str = "";
            while ((str = br.readLine ())!= null)
            {
                line++;
                if (line >= startLine && line <= endLine)
                {
                    code  = code + " " + str;
                }
            }
            br.close();
        }
        catch (Exception e)
        {
            System.out.println ("error");
        }
        
        return code;        
    }
            
        
}
