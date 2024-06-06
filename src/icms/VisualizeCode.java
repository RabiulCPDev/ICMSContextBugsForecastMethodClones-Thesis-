/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;
import java.io.*;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author me
 */
public class VisualizeCode extends javax.swing.JFrame {

    /**
     * Creates new form VisualizeCode
     */
    
    CommonParameters cp = new CommonParameters ();
    AttributeNames an = new AttributeNames ();
    int currentCommit = -1;
    
    int codeid = -1;
    int commitoperation = -1;
    String codetype = "clone";
    int cloneType = -1;
    int commit = 0;
    
    
    
    public void showChanges ()
    {
        showChangesInTable ();
    }
    
    
    public void showChanges (int offset)
    {
        showChangesInTable (offset);
        if (1 > 0)
        {
            return;
        }
        
        int id = Integer.parseInt(jTextField1.getText().trim());
        String type = jTextField2.getText().trim();
        int commit = Integer.parseInt(jTextField3.getText().trim());
        if (currentCommit == -1)
        {
            currentCommit = commit;
        }
        else
        {
            currentCommit = commit + offset;
            jTextField3.setText (currentCommit+"");
        }        
        
        String diff = "";                
        ChangeAnalysis ca = new ChangeAnalysis ();
        Difference d = new Difference ();
        
        if (type.contains ("clone"))
        {
            d = ca.getCloneDiff (id, currentCommit, cloneType);
            
            //details of revision = currentCommit
            int r1 = currentCommit;
            String file1 = cp.getCloneFile(id, r1, cloneType);
            int sline1 = cp.getCloneStartingLine(id, r1, cloneType);
            int eline1 = cp.getCloneEndingLine(id, r1, cloneType);
            int class1 = cp.getCloneClass(id, r1, cloneType);
            String otherClones = cp.getClonesInClass(class1, r1, cloneType);
            String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1+", ClassID = "+class1;
            
            //details fo revision = currentCommit + 1
            int r2 = currentCommit+1;
            String file2 = cp.getCloneFile(id, r2, cloneType);
            int sline2 = cp.getCloneStartingLine(id, r2, cloneType);
            int eline2 = cp.getCloneEndingLine(id, r2, cloneType);
            int class2 = cp.getCloneClass(id, r2, cloneType);
            String str2 = "Revision = "+r2+", File = "+file2+", StartLine = "+sline2+", EndLine = "+eline2+", ClassID = "+class2;
            
            jTextField5.setText (str1);
            jTextField6.setText (str2);            
            jLabel6.setText ("All other clones (class = "+class1 + ", revision = "+r1+") = ");
            jTextField4.setText (otherClones);
        }
        else
        {
            d = ca.getMethodDiff(id, currentCommit);
            
            //details of revision = currentCommit
            int r1 = currentCommit;
            String file1 = cp.getMethodFile(id, r1);
            int sline1 = cp.getMethodStartingLine(id, r1);
            int eline1 = cp.getMethodEndingLine(id, r1);
            String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;
            
            
            //details fo revision = currentCommit + 1
            int r2 = currentCommit+1;
            String file2 = cp.getMethodFile(id, r2);
            int sline2 = cp.getMethodStartingLine(id, r2);
            int eline2 = cp.getMethodEndingLine(id, r2);
            String str2 = "Revision = "+r2+", File = "+file2+", StartLine = "+sline2+", EndLine = "+eline2;
                        
                        
            jTextField5.setText (str1);
            jTextField6.setText (str2);
            jLabel6.setText ("N/A");
            jTextField4.setText ("N/A");            
        }        
        jLabel1.setText ("<html>"+d.compare+"</html>");  
    }
    
    public void showChangesExternalUse ()
    {
        jTextField1.setText(codeid+"");
        jTextField2.setText (codetype);
        jTextField3.setText (commitoperation+"");
        showChangesInTable (0);
    }
    
    public String [] getCode (String filepath, int startline, int endline)
    {
        String [] codefragment = new String [500];
        int linecount = 0;
        
        try
        {            
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (filepath)));
            String str = "";
            int count = 0;
            
            while ((str = br.readLine())!= null)
            {
                count++;
                if (count >= startline && count <= endline)
                {
                    codefragment[linecount] = str;
                    linecount++;
                }
            }
            
        }
        catch (Exception e)
        {
            System.out.println ("error in getCode.");
        }   
        return codefragment;
    }
    
    public String getCloneInfo (int cloneid, int clonetype, int revision)
    {
        DatabaseAccess da = new DatabaseAccess ();
        String temp = da.getData("select filepath, startline, endline from type"+clonetype+"clones where revision ='"+revision+"' and cloneid = '"+cloneid+"'", "filepath, startline, endline")[0];
        String file1 = cp.subject_system+"/repository/version-"+revision+"/"+temp.split("[,]+")[0].trim();
        int sline1 = Integer.parseInt(temp.split("[,]+")[1].trim());
        int eline1 = Integer.parseInt(temp.split("[,]+")[2].trim());
        return sline1+","+eline1+","+file1;
    }
    
    public String[][] getCloneChanges (int cloneid, int clonetype, int revision)
    {
        String [][] clonechange = new String [10000][4];
        
        try
        {
            String [][] compare = new String[1000][4];

            cloneType = clonetype;
            codeid = cloneid;
            codetype = "clone";
            commit = revision;
            currentCommit = commit;

            ChangeAnalysis ca = new ChangeAnalysis ();
            DatabaseAccess da = new DatabaseAccess ();

            String file1 = "";
            String file2 = "";

            int sline1=0, eline1=0, sline2=0, eline2=0;
            if (codetype.contains ("clone"))
            {
                //details of revision = currentCommit
                int r1 = currentCommit;  

                String temp = da.getData("select filepath, startline, endline from type"+cloneType+"clones where revision ='"+r1+"' and cloneid = '"+codeid+"'", "filepath, startline, endline")[0];
                file1 = cp.subject_system+"/repository/version-"+r1+"/"+temp.split("[,]+")[0].trim();
                sline1 = Integer.parseInt(temp.split("[,]+")[1].trim());
                eline1 = Integer.parseInt(temp.split("[,]+")[2].trim());
                String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;

                //details of revision = currentCommit + 1
                int r2 = currentCommit+1;
                file2 = cp.subject_system+"/repository/version-"+r2+"/"+temp.split("[,]+")[0].trim();

                compare = ca.compareFiles(file1, file2);
            }
            else
            {
                //this part is for method change.
            }                    

            String [] column0 = new String [10000];
            String [] column1 = new String [10000];
            String [] column2 = new String [10000];
            String [] column3 = new String [10000];
            int count = 0;



            int bgot = 0;
            int egot = 0;

            for (int i =0;compare[i][0] != null;i++)
            {
                if (compare[i][0].equals(""+(sline1-1))) {bgot = 1;}
                if (bgot == 0) {continue;}
                
                
                if (compare[i][0].equals(""+(eline1-1))) {egot = 1;}
                if (egot == 1) {break;}
                
                //for html generation.
                column0[count] = compare[i][0];
                column2[count] = compare[i][2];

                if (compare[i][1].equals(compare[i][3]))
                {
                    column1[count] = compare[i][1];
                    column3[count] = compare[i][3];

                }
                else
                {
                    column1[count] = "<font color='blue'>"+compare[i][1]+"</font>";
                    column3[count] = "<font color='blue'>"+compare[i][3]+"</font>";

                }
                
                clonechange[count][0] = column0[count];
                clonechange[count][1] = column1[count];
                clonechange[count][2] = column2[count];
                clonechange[count][3] = column3[count];
                
                count++;  
            }
            clonechange[count][0] = "-99";
            clonechange[count][1] = "-99";
            clonechange[count][2] = "-99";
            clonechange[count][3] = "-99";
            count++;
        }
        catch(Exception e)
        {
            System.out.println ("error in visualiza code. "+e);
        }                          
        return clonechange;
    }
    
    
    public void showChangesInTable2 ()
    {        
        try
        {
            String [][] compare = new String[1000][4];

            //cloneType = Integer.parseInt(jTextField7.getText().trim());                
            //codeid = Integer.parseInt(jTextField1.getText().trim());
            //codetype = jTextField2.getText().trim();
            //commit = Integer.parseInt(jTextField3.getText().trim());

            if (currentCommit == -1)
            {
                currentCommit = commit;
            }
            else
            {
                currentCommit = commit + 0;
                //jTextField3.setText (currentCommit+"");
            }        

            ChangeAnalysis ca = new ChangeAnalysis ();
            DatabaseAccess da = new DatabaseAccess ();

            String file1 = "";
            String file2 = "";

            int sline1=0, eline1=0, sline2=0, eline2=0;
            if (codetype.contains ("clone"))
            {
                //details of revision = currentCommit
                int r1 = currentCommit;  

                String temp = da.getData("select filepath, startline, endline from type"+cloneType+"clones where revision ='"+r1+"' and globalcloneid = '"+codeid+"'", "filepath, startline, endline")[0];
                file1 = cp.subject_system+"/repository/version-"+r1+"/"+temp.split("[,]+")[0].trim();
                sline1 = Integer.parseInt(temp.split("[,]+")[1].trim());
                eline1 = Integer.parseInt(temp.split("[,]+")[2].trim());
                String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;

                //details of revision = currentCommit + 1
                int r2 = currentCommit+1;
                file2 = cp.subject_system+"/repository/version-"+r2+"/"+temp.split("[,]+")[0].trim();
                
                //jTextField5.setText (file1);
                //jTextField6.setText (file2);
                
                compare = ca.compareFiles(file1, file2);
            }
            else
            {
                //details of revision = currentCommit
                int r1 = currentCommit;            
                file1 = cp.getMethodFile(codeid, r1);
                sline1 = cp.getMethodStartingLine(codeid, r1);
                eline1 = cp.getMethodEndingLine(codeid, r1);
                String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;


                //details fo revision = currentCommit + 1
                int r2 = currentCommit+1;
                file2 = cp.getMethodFile(codeid, r2);
                sline2 = cp.getMethodStartingLine(codeid, r2);
                eline2 = cp.getMethodEndingLine(codeid, r2);
                String str2 = "Revision = "+r2+", File = "+file2+", StartLine = "+sline2+", EndLine = "+eline2;


                jTextField5.setText (str1);
                jTextField6.setText (str2);
                jLabel6.setText ("N/A");
                jTextField4.setText ("N/A"); 

                //compare = ca.compareMethods(codeid, r1, codeid, r2);
            }        

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);



            String [] column0 = new String [10000];
            String [] column1 = new String [10000];
            String [] column2 = new String [10000];
            String [] column3 = new String [10000];
            int count = 0;



            int bgot = 0;
            int egot = 0;

            for (int i =0;compare[i][0] != null;i++)
            {
                String [] row = new String[4];
                row[0] = compare[i][0];
                row[2] = compare[i][2];

                if (row[0].equals(""+(sline1-1))) {bgot = 1;}
                if (bgot == 0) {continue;}
                
                
                if (row[0].equals(""+(eline1-1))) {egot = 1;}
                if (egot == 1) {break;}
                //for html generation.
                column0[count] = compare[i][0];
                column2[count] = compare[i][2];


                /*if (compare[i][0].trim().length() > 0)
                {
                    row[0] = sline1;
                    sline1++;
                }*/
                /*if (compare[i][2].trim().length() > 0)
                {
                    row[2] = sline2;
                    sline2++;
                }*/

                if (compare[i][1].equals(compare[i][3]))
                {
                    row[1] = compare[i][1];            
                    row[3] = compare[i][3];

                    column1[count] = compare[i][1];
                    column3[count] = compare[i][3];

                }
                else
                {
                    row[1] = "<html><font color='blue'>"+compare[i][1]+"</font></html>";
                    row[3] = "<html><font color='blue'>"+compare[i][3]+"</font></html>";

                    column1[count] = "<font color='blue'>"+compare[i][1]+"</font>";
                    column3[count] = "<font color='blue'>"+compare[i][3]+"</font>";

                }
                count++;  
                model.addRow(row);                
            }   
        
            //adding to the html file.
            try
            {
                BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/manualanalysis_"+cloneType+".html", true));
                
                writer.write("<div style='padding-top:20px;'>-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</div>");
                
                writer.write ("\n<table>");
                writer.write ("\n<tr><td colspan='2' style='font-weight: bold;'>"+"commit = "+currentCommit+", cloneid = "+codeid+"</td><td colspan='2' style='font-weight: bold; padding-left: 40px;'>"+"commit = "+(currentCommit+1) + ", cloneid = "+codeid+"</td></tr>");
                writer.write ("\n<tr><td colspan='2' style='font-weight: bold;'>"+file1+"</td><td colspan='2' style='font-weight: bold; padding-left: 40px;'>"+file2+"</td></tr>");
                writer.write ("\n<tr><td colspan='4'>&nbsp;</td></tr>");
                for (int i =0;i<count;i++)
                {
                    String row = "\n<tr>";
                    row += "<td>"+column0[i]+"</td>";
                    row += "<td style='padding-left:10px;'>"+column1[i]+"</td>";
                    row += "<td  style='padding-left:40px;'>"+column2[i]+"</td>";
                    row += "<td  style='padding-left:10px;'>"+column3[i]+"</td>";
                    row += "</tr>";
                    writer.write (row);
                }
                writer.write ("</table>");
                writer.close();
            }
            catch (Exception e)
            {

            }
        }
        catch(Exception e)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/manualanalysis_"+cloneType+".html", true));
                writer.write("<div style='padding-top:20px;'>-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</div>");
                writer.write ("\n<table>");
                writer.write("\n<tr><td colspan='2'>"+"commit = "+currentCommit+", cloneid = "+codeid+"</td><td colspan='2'>"+"commit = "+(currentCommit+1) + ", cloneid = "+codeid+"</td></tr>");
                writer.write ("\n<tr><td colspan='4'>&nbsp;</td></tr>");
                writer.write("\n<tr><td colspan='4'>"+"the clone is absent in commit = "+(currentCommit+1)+"</td></tr>");
                writer.write ("\n</table>");
                writer.close();
            }
            catch (Exception e2)
            {

            }            
        }                          
    }
    
    
    
    public void showChangesInTable (int offset)
    {        
        try
        {
            String [][] compare = new String[1000][4];

            cloneType = Integer.parseInt(jTextField7.getText().trim());                
            codeid = Integer.parseInt(jTextField1.getText().trim());
            codetype = jTextField2.getText().trim();
            commit = Integer.parseInt(jTextField3.getText().trim());

            if (currentCommit == -1)
            {
                currentCommit = commit;
            }
            else
            {
                currentCommit = commit + offset;
                jTextField3.setText (currentCommit+"");
            }        

            ChangeAnalysis ca = new ChangeAnalysis ();
            DatabaseAccess da = new DatabaseAccess ();

            String file1 = "";
            String file2 = "";

            int sline1=0, eline1=0, sline2=0, eline2=0;
            if (codetype.contains ("clone"))
            {
                //details of revision = currentCommit
                int r1 = currentCommit;  

                String temp = da.getData("select filepath, startline, endline from type"+cloneType+"clones where revision ='"+r1+"' and globalcloneid = '"+codeid+"'", "filepath, startline, endline")[0];
                file1 = cp.subject_system+"/repository/version-"+r1+"/"+temp.split("[,]+")[0].trim();
                sline1 = Integer.parseInt(temp.split("[,]+")[1].trim());
                eline1 = Integer.parseInt(temp.split("[,]+")[2].trim());
                String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;

                //details of revision = currentCommit + 1
                int r2 = currentCommit+1;
                file2 = cp.subject_system+"/repository/version-"+r2+"/"+temp.split("[,]+")[0].trim();
                
                jTextField5.setText (file1);
                jTextField6.setText (file2);
                
                compare = ca.compareFiles(file1, file2);
            }
            else
            {
                //details of revision = currentCommit
                int r1 = currentCommit;            
                file1 = cp.getMethodFile(codeid, r1);
                sline1 = cp.getMethodStartingLine(codeid, r1);
                eline1 = cp.getMethodEndingLine(codeid, r1);
                String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;


                //details fo revision = currentCommit + 1
                int r2 = currentCommit+1;
                file2 = cp.getMethodFile(codeid, r2);
                sline2 = cp.getMethodStartingLine(codeid, r2);
                eline2 = cp.getMethodEndingLine(codeid, r2);
                String str2 = "Revision = "+r2+", File = "+file2+", StartLine = "+sline2+", EndLine = "+eline2;


                jTextField5.setText (str1);
                jTextField6.setText (str2);
                jLabel6.setText ("N/A");
                jTextField4.setText ("N/A"); 

                //compare = ca.compareMethods(codeid, r1, codeid, r2);
            }        

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);



            String [] column0 = new String [10000];
            String [] column1 = new String [10000];
            String [] column2 = new String [10000];
            String [] column3 = new String [10000];
            int count = 0;



            int bgot = 0;
            int egot = 0;

            for (int i =0;compare[i][0] != null;i++)
            {
                String [] row = new String[4];
                row[0] = compare[i][0];
                row[2] = compare[i][2];

                if (row[0].equals(""+(sline1-1))) {bgot = 1;}
                if (bgot == 0) {continue;}
                
                
                if (row[0].equals(""+(eline1-1))) {egot = 1;}
                if (egot == 1) {break;}
                //for html generation.
                column0[count] = compare[i][0];
                column2[count] = compare[i][2];


                /*if (compare[i][0].trim().length() > 0)
                {
                    row[0] = sline1;
                    sline1++;
                }*/
                /*if (compare[i][2].trim().length() > 0)
                {
                    row[2] = sline2;
                    sline2++;
                }*/

                if (compare[i][1].equals(compare[i][3]))
                {
                    row[1] = compare[i][1];            
                    row[3] = compare[i][3];

                    column1[count] = compare[i][1];
                    column3[count] = compare[i][3];

                }
                else
                {
                    row[1] = "<html><font color='blue'>"+compare[i][1]+"</font></html>";
                    row[3] = "<html><font color='blue'>"+compare[i][3]+"</font></html>";

                    column1[count] = "<font color='blue'>"+compare[i][1]+"</font>";
                    column3[count] = "<font color='blue'>"+compare[i][3]+"</font>";

                }
                count++;  
                model.addRow(row);                
            }   
        
            //adding to the html file.
            try
            {
                BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/manualanalysis_"+cloneType+".html", true));
                
                writer.write("<div style='padding-top:20px;'>-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</div>");
                
                writer.write ("\n<table>");
                writer.write ("\n<tr><td colspan='2' style='font-weight: bold;'>"+"commit = "+currentCommit+", cloneid = "+codeid+"</td><td colspan='2' style='font-weight: bold; padding-left: 40px;'>"+"commit = "+(currentCommit+1) + ", cloneid = "+codeid+"</td></tr>");
                writer.write ("\n<tr><td colspan='2' style='font-weight: bold;'>"+file1+"</td><td colspan='2' style='font-weight: bold; padding-left: 40px;'>"+file2+"</td></tr>");
                writer.write ("\n<tr><td colspan='4'>&nbsp;</td></tr>");
                for (int i =0;i<count;i++)
                {
                    String row = "\n<tr>";
                    row += "<td>"+column0[i]+"</td>";
                    row += "<td style='padding-left:10px;'>"+column1[i]+"</td>";
                    row += "<td  style='padding-left:40px;'>"+column2[i]+"</td>";
                    row += "<td  style='padding-left:10px;'>"+column3[i]+"</td>";
                    row += "</tr>";
                    writer.write (row);
                }
                writer.write ("</table>");
                writer.close();
            }
            catch (Exception e)
            {

            }
        }
        catch(Exception e)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter (new FileWriter (cp.subject_system+"/manualanalysis_"+cloneType+".html", true));
                writer.write("<div style='padding-top:20px;'>-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</div>");
                writer.write ("\n<table>");
                writer.write("\n<tr><td colspan='2'>"+"commit = "+currentCommit+", cloneid = "+codeid+"</td><td colspan='2'>"+"commit = "+(currentCommit+1) + ", cloneid = "+codeid+"</td></tr>");
                writer.write ("\n<tr><td colspan='4'>&nbsp;</td></tr>");
                writer.write("\n<tr><td colspan='4'>"+"the clone is absent in commit = "+(currentCommit+1)+"</td></tr>");
                writer.write ("\n</table>");
                writer.close();
            }
            catch (Exception e2)
            {

            }            
        }                          
    }

    
    public int isChanged (int gcid, int clonetype, int revision)
    {      
        int offset = 0;
        try
        {
            String [][] compare = new String[1000][4];

            cloneType = clonetype;                
            codeid = gcid;
            codetype = "clone";
            commit = revision;

            currentCommit = commit;

            ChangeAnalysis ca = new ChangeAnalysis ();
            DatabaseAccess da = new DatabaseAccess ();

            String file1 = "";
            String file2 = "";

            int sline1=0, eline1=0, sline2=0, eline2=0;
            if (codetype.contains ("clone"))
            {
                //details of revision = currentCommit
                int r1 = currentCommit;  

                String temp = da.getData("select filepath, startline, endline from type"+cloneType+"clones where revision ='"+r1+"' and globalcloneid = '"+codeid+"'", "filepath, startline, endline")[0];
                file1 = cp.subject_system+"/repository/version-"+r1+"/"+temp.split("[,]+")[0].trim();
                sline1 = Integer.parseInt(temp.split("[,]+")[1].trim());
                eline1 = Integer.parseInt(temp.split("[,]+")[2].trim());
                String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;

                //details of revision = currentCommit + 1
                int r2 = currentCommit+1;
                file2 = cp.subject_system+"/repository/version-"+r2+"/"+temp.split("[,]+")[0].trim();

                compare = ca.compareFiles(file1, file2);
            }

            int bgot = 0;
            int egot = 0;

            for (int i =0;compare[i][0] != null;i++)
            {
                if (compare[i][0].equals(""+(sline1-1))) {bgot = 1;}
                if (bgot == 0) {continue;}                
                
                if (compare[i][0].equals(""+(eline1-1))) {egot = 1;}
                if (egot == 1) {break;}

                if (!compare[i][1].equals(compare[i][3]))
                {
                    return 1;
                }
            }           
        }
        catch(Exception e)
        {
            //
        }                          
        return 0;
    }
    
    
    
    public void showChangesInTableOld (int offset)
    {                        
        String [][] compare = new String[1000][4];
        
        cloneType = Integer.parseInt(jTextField7.getText().trim());                
        codeid = Integer.parseInt(jTextField1.getText().trim());
        codetype = jTextField2.getText().trim();
        commit = Integer.parseInt(jTextField3.getText().trim());
        
        if (currentCommit == -1)
        {
            currentCommit = commit;
        }
        else
        {
            currentCommit = commit + offset;
            jTextField3.setText (currentCommit+"");
        }        
        
        ChangeAnalysis ca = new ChangeAnalysis ();
        DatabaseAccess da = new DatabaseAccess ();
        
        int sline1=0, eline1=0, sline2=0, eline2=0;
        if (codetype.contains ("clone"))
        {
            //details of revision = currentCommit
            int r1 = currentCommit;            
            String temp = da.getData("select filepath, startline, endline from type"+cloneType+"clones where revision ='"+r1+"' and cloneid = '"+codeid+"'", "filepath, startline, endline")[0];
            String file1 = cp.subject_system+"/repository/version-"+r1+"/"+temp.split("[,]+")[0].trim();
            sline1 = Integer.parseInt(temp.split("[,]+")[1].trim());
            eline1 = Integer.parseInt(temp.split("[,]+")[2].trim());
            //int class1 = cp.getCloneClass(codeid, r1, cloneType);
            //String otherClones = cp.getClonesInClass(class1, r1, cloneType);
            String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;
            
            //details fo revision = currentCommit + 1
            int r2 = currentCommit+1;
            String temp2 = da.getData("select filepath, startline, endline from type"+cloneType+"clones where revision ='"+r2+"' and cloneid = '"+codeid+"'", "filepath, startline, endline")[0];
            String file2 = cp.subject_system+"/repository/version-"+r2+"/"+temp2.split("[,]+")[0].trim();
            sline2 = Integer.parseInt(temp2.split("[,]+")[1].trim());
            eline2 = Integer.parseInt(temp2.split("[,]+")[2].trim());
            //int class2 = cp.getCloneClass(codeid, r2, cloneType);
            String str2 = "Revision = "+r2+", File = "+file2+", StartLine = "+sline2+", EndLine = "+eline2;
            
            jTextField5.setText (str1);
            jTextField6.setText (str2);            
            //jLabel6.setText ("All other clones (class = "+class1 + ", revision = "+r1+") = ");
            //jTextField4.setText (otherClones);
            
            
            compare = ca.compareClones(file1, sline1, eline1, file2, sline2, eline2);
        }
        else
        {
            //details of revision = currentCommit
            int r1 = currentCommit;            
            String file1 = cp.getMethodFile(codeid, r1);
            sline1 = cp.getMethodStartingLine(codeid, r1);
            eline1 = cp.getMethodEndingLine(codeid, r1);
            String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;
            
            
            //details fo revision = currentCommit + 1
            int r2 = currentCommit+1;
            String file2 = cp.getMethodFile(codeid, r2);
            sline2 = cp.getMethodStartingLine(codeid, r2);
            eline2 = cp.getMethodEndingLine(codeid, r2);
            String str2 = "Revision = "+r2+", File = "+file2+", StartLine = "+sline2+", EndLine = "+eline2;
                        
                        
            jTextField5.setText (str1);
            jTextField6.setText (str2);
            jLabel6.setText ("N/A");
            jTextField4.setText ("N/A"); 
            
            //compare = ca.compareMethods(codeid, r1, codeid, r2);
        }        
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        for (int i =0;compare[i][0] != null;i++)
        {
            Object [] row = new Object[4];
            row[0] = compare[i][0];
            row[2] = compare[i][2];
            
            if (compare[i][0].trim().length() > 0)
            {
                row[0] = sline1;
                sline1++;
            }
            if (compare[i][2].trim().length() > 0)
            {
                row[2] = sline2;
                sline2++;
            }
            
            if (compare[i][1].equals(compare[i][3]))
            {
                row[1] = compare[i][1];            
                row[3] = compare[i][3];
            }
            else
            {
                row[1] = "<html><font color='blue'>"+compare[i][1]+"</font></html>";
                row[3] = "<html><font color='blue'>"+compare[i][3]+"</font></html>";
            }
                        
            model.addRow(row);
        }        
    }
    
    public void showChangesInTable ()
    {                        
        String [][] compare = new String[1000][4];        
        commit = currentCommit;        
        ChangeAnalysis ca = new ChangeAnalysis ();                
        int sline1=0, eline1=0, sline2=0, eline2=0;
        
        
        jTextField1.setText (codeid+"");
        jTextField2.setText (codetype);
        jTextField3.setText (""+currentCommit);
        jTextField7.setText (""+cloneType);        
        
        
        if (codetype.contains ("clone"))
        {
            //details of revision = currentCommit
            int r1 = currentCommit;
            String file1 = cp.getCloneFile(codeid, r1, cloneType);
            sline1 = cp.getCloneStartingLine(codeid, r1, cloneType);
            eline1 = cp.getCloneEndingLine(codeid, r1, cloneType);
            int class1 = cp.getCloneClass(codeid, r1, cloneType);
            String otherClones = cp.getClonesInClass(class1, r1, cloneType);
            String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1+", ClassID = "+class1;
            
            //details fo revision = currentCommit + 1
            int r2 = currentCommit+1;
            String file2 = cp.getCloneFile(codeid, r2, cloneType);
            sline2 = cp.getCloneStartingLine(codeid, r2, cloneType);
            eline2 = cp.getCloneEndingLine(codeid, r2, cloneType);
            int class2 = cp.getCloneClass(codeid, r2, cloneType);
            String str2 = "Revision = "+r2+", File = "+file2+", StartLine = "+sline2+", EndLine = "+eline2+", ClassID = "+class2;
            
            jTextField5.setText (str1);
            jTextField6.setText (str2);            
            jLabel6.setText ("All other clones (class = "+class1 + ", revision = "+r1+") = ");
            jTextField4.setText (otherClones);
            
            
            //compare = ca.compareClones(codeid, r1, codeid, r2, cloneType);            
        }
        else
        {
            //details of revision = currentCommit
            int r1 = currentCommit;
            String file1 = cp.getMethodFile(codeid, r1);
            sline1 = cp.getMethodStartingLine(codeid, r1);
            eline1 = cp.getMethodEndingLine(codeid, r1);
            String str1 = "Revision = "+r1+", File = "+file1+", StartLine = "+sline1+", EndLine = "+eline1;
            
            
            //details fo revision = currentCommit + 1
            int r2 = currentCommit+1;
            String file2 = cp.getMethodFile(codeid, r2);
            sline2 = cp.getMethodStartingLine(codeid, r2);
            eline2 = cp.getMethodEndingLine(codeid, r2);
            String str2 = "Revision = "+r2+", File = "+file2+", StartLine = "+sline2+", EndLine = "+eline2;
                        
                        
            jTextField5.setText (str1);
            jTextField6.setText (str2);
            jLabel6.setText ("N/A");
            jTextField4.setText ("N/A"); 
            
            //compare = ca.compareMethods(codeid, r1, codeid, r2);
        }        
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        for (int i =0;compare[i][0] != null;i++)
        {
            Object [] row = new Object[4];
            row[0] = compare[i][0];
            row[2] = compare[i][2];
            
            if (compare[i][0].trim().length() > 0)
            {
                row[0] = sline1;
                sline1++;
            }
            if (compare[i][2].trim().length() > 0)
            {
                row[2] = sline2;
                sline2++;
            }
            
            if (compare[i][1].equals(compare[i][3]))
            {
                row[1] = compare[i][1];            
                row[3] = compare[i][3];
            }
            else
            {
                row[1] = "<html><font color='blue'>"+compare[i][1]+"</font></html>";
                row[3] = "<html><font color='blue'>"+compare[i][3]+"</font></html>";                
            }
                        
            model.addRow(row);
        }        
    }
    
    
    public VisualizeCode() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");
        jScrollPane1.setViewportView(jLabel1);

        jLabel3.setText("Code ID = ");

        jLabel4.setText("Code Type = ");

        jLabel5.setText("Current Commit = ");

        jButton1.setText("Previous");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Next");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Current");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel6.setText("All clones in this Class  = ");

        jTextField4.setText("jTextField4");

        jTextField5.setText("jTextField5");

        jTextField6.setText("jTextField6");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jLabel2.setText("Clone Type = ");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jButton4.setText("Get Code");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel7.setText("File Path");

        jTextField8.setText("jTextField8");

        jLabel8.setText("Start Line");

        jTextField9.setText("jTextField9");

        jLabel9.setText("End Line");

        jTextField10.setText("jTextField10");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(12, 12, 12)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4))
                    .addComponent(jTextField6)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jLabel2)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        showChanges (-1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        showChanges (1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        showChanges (0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
        String filepath = jTextField8.getText();
        int startline = Integer.parseInt (jTextField9.getText());
        int endline = Integer.parseInt (jTextField10.getText());  
        
        String [] codefragment = getCode(filepath, startline, endline);
        String code = "";
        
        int i =0;
        while (codefragment[i] != null)
        {
            code += "\n"+codefragment[i];
            i++;
        }
        
        jTextArea1.setText(code);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VisualizeCode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VisualizeCode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VisualizeCode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VisualizeCode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VisualizeCode().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
