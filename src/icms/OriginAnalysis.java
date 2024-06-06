/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.util.ArrayList;
import java.io.*;
/**
 *
 * @author manishankar
 */
public class OriginAnalysis {

    public MethodExtraction methodManagement = new MethodExtraction ();
    
    CommonParameters commonParameters = new CommonParameters ();
    
    String [][] previousMethods = new String[50000][2];
    public int previousMethodCount = 0;
    
    String [][] currentMethods = new String[50000][2];
    public int methodCount = 0;

    public AttributeNames an = new AttributeNames();

    
    public void mainMethod ()
    {
        for (int i=1;i<= commonParameters.revisionCount;i++)
        {
            System.out.println ("determining genealogies. working on revision = "+i);
            analyzeOriginInFile (i);
            //analyzeOriginInDatabase (i);
        }
    }
    
    
    public void getPreviousMethodsFromFile (int revision)
    {
        try
        {
            previousMethodCount = 0;
            int previousRevision = revision-1;

            File f1 = new File(commonParameters.getMethodsPathOfRevision(previousRevision));
            if (f1.exists())
            {
                String line = "";
                BufferedReader br1 = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.getMethodsPathOfRevision(previousRevision))));
                while ((line = br1.readLine())!= null)
                {
                    if (line.trim().length() == 0){continue;}
                    previousMethods[previousMethodCount][0] = line;
                    previousMethods[previousMethodCount][1] = "true";
                    previousMethodCount++ ;                    
                }
            }
            else
            {
                previousMethodCount = 0;
            }            
        }
        catch (Exception e)
        {
            
        }
    }
    
    public void getPreviousMethodsFromCurrentMethods ()
    {        
        for (int i =0;i<methodCount;i++)
        {
            previousMethods[i][0] = currentMethods[i][0];
            previousMethods[i][1] = "true";
        }
        previousMethodCount = methodCount;
    }
    

    public void analyzeOriginInFile (int revision)
    {
        try
        {
            /*previousMethodCount = 0;
            int previousRevision = revision-1;

            File f1 = new File(commonParameters.getMethodsPathOfRevision(previousRevision));
            if (f1.exists())
            {
                String line = "";
                BufferedReader br1 = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.getMethodsPathOfRevision(previousRevision))));
                while ((line = br1.readLine())!= null)
                {
                    if (line.trim().length() == 0){continue;}
                    previousMethods[previousMethodCount][0] = line;
                    previousMethods[previousMethodCount][1] = "true";
                    previousMethodCount++ ;                    
                }
            }
            else
            {
                previousMethodCount = 0;
            }*/
            
            if (methodCount > 0)
            {
                getPreviousMethodsFromCurrentMethods ();
            }
            else
            {
                getPreviousMethodsFromFile (revision);
            }

            File f2 = new File(commonParameters.getMethodsPathOfRevision(revision));
            if (f2.exists())
            {
                methodCount = 0;
                String line = "";
                BufferedReader br1 = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.getMethodsPathOfRevision(revision))));
                while ((line = br1.readLine())!= null)
                {
                    if (line.trim().length() == 0){continue;}
                    currentMethods[methodCount][0] = line;
                    methodCount++ ;
                }
            }
            else
            {
                methodCount = 0;
            }

            analyzeOrigin(revision);

            if (methodCount > 0)
            {
                BufferedWriter br = new BufferedWriter (new FileWriter (commonParameters.getMethodsPathOfRevision(revision)));
                for (int i =0;i<methodCount;i++)
                {
                    br.write("\n"+currentMethods[i][0]);
                }
                //System.out.println ("Global methods updated.");
                br.close();
            }            
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = analyzeOriginInFile. "+e);
        }
    }

    public void analyzeOrigin (int currentVersion)
    {
        try
        {
            int i , j ;
            boolean sim = false ;
            int maxglobalmethodid = 0;


            if (previousMethodCount == 0)
            {
                for (i=0;i<methodCount;i++)
                {
                    currentMethods[i][0] += an.globalMethodID+" = "+(i+1)+""+commonParameters.separator;
                }
                commonParameters.setMaxGlobalMethodID(methodCount);
                return;
            }
            else
            {
                maxglobalmethodid = commonParameters.getMaxGlobalMethodID();
            }

            //System.out.println ("\nfinding exact origins") ;
            //find origins of the non deleted methods.
            for (i=0;i<previousMethodCount;i++)
            {
                for (j=0;j<methodCount;j++)
                {
                    if (findMethodSimilarity (currentVersion,i,j) && !currentMethods[j][0].contains(an.globalMethodID))
                    {
                        previousMethods[i][1] = "false"; //previous method is not deleted.
                        currentMethods[j][0] += an.globalMethodID+" = " + commonParameters.getAttributeValueFromString(previousMethods[i][0],an.globalMethodID)+" "+commonParameters.separator + " ";
                        break ;
                    }
                }
            }
            //System.out.println ("\ncompleted finding exact origins.") ;

            double maxSimilarityOnLocationChange = -99, maxSimilarityOnSignatureChange = -99, similarityOnLocationChange = -99, similarityOnSignatureChange = -99 ;
            int maxSimilarMethodIDOnSignatureChange =-1, maxSimilarMethodIDOnLocationChange =-1 ;

            //System.out.println ("\ncalculating derieved origins.") ;

            for (i=0;i<previousMethodCount;i++)
            {
                if (previousMethods[i][1].equals("true"))
                {
                    maxSimilarityOnSignatureChange = -99;

                    //same location, different signature.
                    for (j=0;j<methodCount;j++)
                    {
                        if (!currentMethods[j][0].contains(an.globalMethodID))
                        {
                            if (findLocationSimilarity (currentVersion, i, j) == true && findSignatureSimilarity (i,j) == false)
                            {
                                similarityOnSignatureChange = calculateStrikeAMatch (currentVersion,i,j) ;
                                if (similarityOnSignatureChange > maxSimilarityOnSignatureChange)
                                {
                                    maxSimilarityOnSignatureChange  = similarityOnSignatureChange ;
                                    maxSimilarMethodIDOnSignatureChange = j ;
                                }
                            }
                        }
                    }

                    if (maxSimilarityOnSignatureChange > 70)
                    {
                        currentMethods[maxSimilarMethodIDOnSignatureChange][0] += an.globalMethodID+" = " + commonParameters.getAttributeValueFromString(previousMethods[i][0], an.globalMethodID)+commonParameters.separator;
                        previousMethods[i][1] = "false" ; //previous method is not deleted.
                    }
                    else
                    {
                        maxSimilarityOnLocationChange = -99 ;

                        //same signature, different location.
                        for (j=0;j<methodCount;j++)
                        {
                            if (!currentMethods[j][0].contains(an.globalMethodID))
                            {
                                if (findSignatureSimilarity (i,j) == true && findLocationSimilarity (currentVersion, i,j) == false)
                                {
                                    similarityOnLocationChange = calculateStrikeAMatch (currentVersion,i,j) ;

                                    if (similarityOnLocationChange > maxSimilarityOnLocationChange)
                                    {
                                        maxSimilarityOnLocationChange = similarityOnLocationChange ;
                                        maxSimilarMethodIDOnLocationChange = j ;
                                    }
                                }
                            }
                        }

                        if (maxSimilarityOnLocationChange > 70)
                        {
                            currentMethods[maxSimilarMethodIDOnLocationChange][0] += an.globalMethodID+" = " + commonParameters.getAttributeValueFromString(previousMethods[i][0], an.globalMethodID)+commonParameters.separator;
                            previousMethods[i][1] = "false" ; //previous method is not deleted.
                        }
                    }
                }
            }//end for

            //System.out.println ("\ncompleted calculating derieved origins.") ;

            //showing the newly created methods.
            for (i=0;i<methodCount;i++)
            {
                if (!currentMethods[i][0].contains(an.globalMethodID))
                {
                    maxglobalmethodid++;
                    currentMethods[i][0] += an.globalMethodID+" = " + maxglobalmethodid+commonParameters.separator ;
                }
                commonParameters.setMaxGlobalMethodID(maxglobalmethodid);
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = analyzeOrigin. "+e);
        }
    } //public static void analyzeOrigin ()

    public boolean findMethodSimilarity (int currentVersion, int previous, int current)
    {
        try
        {
            if ( !findLocationSimilarity (currentVersion, previous, current) ) return false ;
            return findSignatureSimilarity (previous, current) ;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = findMethodSimilarity. "+e);
            return false;
        }
    }

    public boolean findLocationSimilarity (int currentVersion, int previous, int current)
    {
        try
        {
            String pfilename = "", cfilename = "" ;
            pfilename = commonParameters.getAttributeValueFromString(previousMethods[previous][0], an.filePath) ;
            cfilename = commonParameters.getAttributeValueFromString(currentMethods[current][0], an.filePath);

            if (!pfilename.equals(cfilename))
            {
                return false;
            }
            if (! commonParameters.getAttributeValueFromString(previousMethods[previous][0], an.packageName).equals(commonParameters.getAttributeValueFromString(currentMethods[current][0],an.packageName)))
            {
                return false;
            }

            if ( commonParameters.getAttributeValueFromString(previousMethods[previous][0], an.className).equals(commonParameters.getAttributeValueFromString(currentMethods[current][0], an.className)) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = findLocationSimilarity. "+e);
            return false;
        }
    }

    public boolean findSignatureSimilarity (int previous, int current)
    {
        try
        {
            String psignature = "", csignature = "" ;
            psignature = commonParameters.getAttributeValueFromString( previousMethods[previous][0], an.signature) ;
            csignature = commonParameters.getAttributeValueFromString( currentMethods[current][0], an.signature) ;

            if ( psignature.equals (csignature) )
            {
                return true ;
            }
            else
            {
                return false ;
            }
        }
        catch (Exception e)
        {
            System.out.println ("error in method = findSignatureSimilarity. "+e);
            return false;
        }
    }


    //determining the similarities between functions using Stroke A Match algorithm.
    //------------------------------------------------------------------------------
    public double calculateStrikeAMatch (int currentVersion, int previous, int current) throws Exception
    {
        try
        {
            String pfilename="", cfilename = "", str;
            int sln, eln, i=0,j=0;
            double similarity = 0.0;
            String previousSingleString = "", currentSingleString = "" ;
            int pversion = currentVersion - 1 ;

            //pfilename = absolutePaths.repositoryURL+"/version-"+pversion+"/"+previousMethodBlocks[previous].fileName ;
            //cfilename = absolutePaths.repositoryURL+"/version-"+currentVersion+"/"+methodBlocks[current].fileName ;

            pfilename = commonParameters.repositoryURL+"/version-"+pversion+"/"+commonParameters.getAttributeValueFromString(previousMethods[previous][0], an.filePath) ;
            cfilename = commonParameters.repositoryURL+"/version-"+currentVersion+"/"+commonParameters.getAttributeValueFromString(currentMethods[current][0], an.filePath);


            //String t = "repository/version-"+currentVersion+"";
            //int len = t.length() ;
            //cfilename = cfilename.substring (len+1) ;


            //System.out.println (pfilename + " \n" + cfilename) ;

            //getting the lines of previous function.


            sln = Integer.parseInt(commonParameters.getAttributeValueFromString (previousMethods[previous][0], an.startingLine));
            eln = Integer.parseInt(commonParameters.getAttributeValueFromString (previousMethods[previous][0], an.endingLine));

            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream(pfilename)));

            i=0;
            while ((str = br.readLine ()) != null)
            {
                i++ ;
                if (i > eln) break ;

                if (i >= sln && i <= eln)
                    previousSingleString = previousSingleString + str.trim()+" " ;
            }

            //getting the lines of current function.
            sln = Integer.parseInt(commonParameters.getAttributeValueFromString (currentMethods[current][0], an.startingLine));
            eln = Integer.parseInt(commonParameters.getAttributeValueFromString (currentMethods[current][0], an.endingLine));

            BufferedReader br2 = new BufferedReader (new InputStreamReader (new FileInputStream(cfilename)));

            i=0;
            while ((str = br2.readLine ()) != null)
            {
                i++ ;
                if (i > eln) break ;

                if (i >= sln && i <= eln)
                    currentSingleString = currentSingleString + str.trim() + " ";
            }

            previousSingleString = previousSingleString.trim () ;
            currentSingleString = currentSingleString.trim () ;

            similarity = compareStrings (previousSingleString, currentSingleString) * 100 ;

            return similarity ;
        }
        catch (Exception e)
        {
            System.out.println ("error in method = calculateStrikeAMatch. "+e);
            return 0.0;
        }
    }//public static boolean calculateStrokeAMatch ()



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
}
