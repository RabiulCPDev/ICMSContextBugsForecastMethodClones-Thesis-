/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package icms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 *
 * @author mani
 */
public class MethodChangeAnalysis extends javax.swing.JFrame {

    /**
     * Creates new form MethodChangeAnalysis
     */
    CommonParameters commonParameters = new CommonParameters ();
    int methodID =0;
    String methodName = "", filePath = "", commitList = "";
    String currentCommit = "";
    int currentIndex = 0;
    AttributeNames an = new AttributeNames ();
    
    public String getMethodInfo (int mid)
    {
        try
        {
            String line = "";
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream( commonParameters.subject_system+"/changedmethods.txt")));
            while ((line = br.readLine())!= null)
            {
                if (line.trim().length() ==0){continue;}

                if (Integer.parseInt(line.trim().split("[:]+")[0].trim()) == mid)
                {
                    br.close();
                    return line;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = getMethodInfo. " + e);
        }

        return "";
    }
    
    public void writeMethod (int m, int rev) throws Exception
    {
        //getting the method information.
        String str = "", methodString = "";
        BufferedReader breader = new BufferedReader (new InputStreamReader (new FileInputStream(commonParameters.subject_system+"/methods/methods_version_"+rev+".txt")));
        while ((str = breader.readLine())!= null)
        {
            if (str.trim().length() == 0){continue;}
            if (Integer.parseInt(commonParameters.getAttributeValueFromString(str, an.globalMethodID)) == m)
            {
                methodString = str;
                break;
            }
        }

        String filename = commonParameters.getAttributeValueFromString(methodString, an.filePath);
        int sline = Integer.parseInt(commonParameters.getAttributeValueFromString(methodString, an.startingLine));
        int eline = Integer.parseInt(commonParameters.getAttributeValueFromString(methodString, an.endingLine));

        filename = commonParameters.repositoryURL+ "/version-"+rev+"/" + filename;

        BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream (filename)));
        BufferedWriter br1 = new BufferedWriter (new FileWriter ("method-"+m+"_revision-"+rev+".txt"));
        
        int i =0;
        while ((str = br.readLine())!= null)
        {
            i++;
            if (i >= sline && i<= eline)
            {
                br1.write(str+"\n");
            }
        }
        br1.close();
        br.close();
        System.out.println ("file is written.");
    }

    public String [] getMethodWithLineNumber (int m, int rev, int similarity) throws Exception
    {
        String [] methodBody = new String[5000];
        int lineCount = 0;
        String str = "";

        BufferedReader br = new BufferedReader(new InputStreamReader (new FileInputStream ("method-"+m+"_revision-"+rev+".txt")));
        while ((str = br.readLine())!= null)
        {
                methodBody[lineCount] = (lineCount+1)+" "+str;
                lineCount++;
        }

        return methodBody;
    }

    public String [] getFileDiff (String file1, String file2) throws Exception
    {
        Process p = Runtime.getRuntime().exec("c:/tools/diffwin/bin/diff "+file1+" "+file2+"");
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        String [] diff = new String [5000];
        int lineCount = 0;

        while ((line = in.readLine()) != null)
        {
            diff[lineCount] = line;
            lineCount++;
        }
        in.close();

        return diff;
    }   
    
    public String compareFiles (int revision1, int revision2,  String [] diff12, String [] fileString1, String [] fileString2)
    {
        String line = "";

        //this is only for coloring.
        for (int i1 =0;diff12[i1] != null;i1++ )
        {
            line = diff12[i1];
            if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}
            int oldline1 = -1;
            int oldline2 = -1;
            int newline1 = -1;
            int newline2 = -1;

            String oldline = line.split("[a|c|d]+")[0];
            String newline = line.split("[a|c|d]+")[1];

            oldline1 = Integer.parseInt (oldline.split("[,]+")[0]);
            oldline2 = oldline1;
            if (oldline.contains(","))
            {
                oldline2 = Integer.parseInt (oldline.split("[,]+")[1]);
            }


            newline1 = Integer.parseInt (newline.split("[,]+")[0]);
            newline2 = newline1;
            if (newline.contains(","))
            {
                newline2 = Integer.parseInt (newline.split("[,]+")[1]);
            }

            if (line.contains("a") || line.contains("c"))
            {
                for (int c=newline1;c<=newline2;c++)
                {
                    String part1 = fileString2[c-1].split("[ ]+")[0];
                    int len = part1.length();
                    String part2 = fileString2[c-1].substring(len);

                    if (line.contains("a"))
                    {
                        fileString2[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                    else
                    {
                        fileString2[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                }
            }
            if (line.contains("d") || line.contains("c"))
            {
                for (int c=oldline1;c<=oldline2;c++)
                {
                    String part1 = fileString1[c-1].split("[ ]+")[0];
                    int len = part1.length();
                    String part2 = fileString1[c-1].substring(len);

                    if (line.contains("d"))
                    {
                        fileString1[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                    else
                    {
                        fileString1[c-1] = part1 + " <font color='black'>"+part2+"</font>";
                    }
                }
            }

        }

        for (int i1 =0;diff12[i1] != null;i1++ )
        {
            line = diff12[i1];
            if (line.charAt(0)=='>' || line.charAt(0)=='<' || line.charAt(0)=='-' || line.charAt(0)=='\\') {continue;}

            int oldline1 = -1;
            int oldline2 = -1;
            int newline1 = -1;
            int newline2 = -1;

            String oldline = line.split("[a|c|d]+")[0];
            String newline = line.split("[a|c|d]+")[1];

            oldline1 = Integer.parseInt (oldline.split("[,]+")[0]);
            oldline2 = oldline1;
            if (oldline.contains(","))
            {
                oldline2 = Integer.parseInt (oldline.split("[,]+")[1]);
            }


            newline1 = Integer.parseInt (newline.split("[,]+")[0]);
            newline2 = newline1;
            if (newline.contains(","))
            {
                newline2 = Integer.parseInt (newline.split("[,]+")[1]);
            }

            
            int oldlines = oldline2 - oldline1 + 1;
            int newlines = newline2 - newline1 + 1;

            if (newlines > oldlines || line.contains ("a")) // we need space in oldfile.
            {
                int cnt = newlines - oldlines;
                if (line.contains ("a")){ cnt = cnt + 1; }
                int oldln = 0;
                for (int l =0; fileString1[l] != null;l++)
                {
                    if (fileString1[l].trim().length() > 0)
                    {
                        if (Integer.parseInt(fileString1[l].split("[ ]+")[0]) == oldline2)
                        {
                            oldln = l;
                            break;
                        }
                    }
                }

                int lineCount1 = 0;
                while (fileString1[lineCount1] != null)
                {
                    lineCount1++;
                }

                for (int l = lineCount1-1;l>=oldln+1;l--)
                {
                    fileString1[l+cnt] = fileString1[l];
                    fileString1[l] = "";
                }
                lineCount1 = lineCount1 + cnt;
            }
            if (oldlines > newlines || line.contains ("d"))
            {
                int cnt = oldlines -  newlines;
                if (line.contains ("d")){ cnt = cnt + 1; }
                int newln = 0;

                for (int l =0;fileString2[l] != null;l++)
                {
                    if (fileString2[l].trim().length()>0)
                    {
                        if (Integer.parseInt(fileString2[l].split("[ ]+")[0]) == newline2)
                        {
                            newln = l;
                            break;
                        }
                    }
                }

                int lineCount2 = 0;
                while (fileString2[lineCount2] != null)
                {
                    lineCount2++;
                }

                for (int l =lineCount2-1;l>=newln+1;l--)
                {
                    fileString2[l+cnt] = fileString2[l];
                    fileString2[l] = "";
                }
                lineCount2 = lineCount2+cnt;
            }
            
        }


        String tableText  = "<table cellspacing='0' cellpadding='0' width='100%'>";

        /*tableText += "<tr>";
        tableText += "<td colspan='2'>"+"Revision = "+revision1+" </td>";
        tableText += "<td colspan='2'>"+"Revision = " + revision2+" </td>";
        tableText += "</tr>";*/

        for (int n = 0;!(fileString1[n] == null && fileString2[n] == null);n++)
        {
            String part1="", part2="", part3="", part4 ="";
            int len =0;
            if (fileString1[n] != null)
            {
                if (fileString1[n].trim().length() > 0)
                {
                    part1 = fileString1[n].split("[ ]+")[0];
                    len = part1.length();
                    part2 = fileString1[n].substring(len);
                }
            }

            if (fileString2[n] != null)
            {
                if (fileString2[n].trim().length() > 0)
                {
                    part3 = fileString2[n].split("[ ]+")[0];
                    len = part3.length();
                    part4 = fileString2[n].substring(len);
                }
            }


            tableText += "<tr>";
            tableText += "<td align='right' valign='top' style='width: 20px; background-color:#e9e8e2; padding-right: 5px; padding-bottom: 5px;padding-top: 5px;'>"+part1+"</td>";
            if (part2.trim().split("[ ]+")[0].contains("font") || part4.trim().split("[ ]+")[0].contains("font"))
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#b1cdec;padding-bottom: 5px;padding-top: 5px;'>"+part2+"</td>";
            }
            else
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#ffffff;padding-bottom: 5px;padding-top: 5px;'>"+part2+"</td>";
            }
            tableText += "<td align='right' valign='top' style='width: 20px; background-color:#e9e8e2; padding-right: 5px;padding-bottom: 5px;padding-top: 5px;'>"+part3+"</td>";
            if (part2.trim().split("[ ]+")[0].contains("font") || part4.trim().split("[ ]+")[0].contains("font"))
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#b1cdec;padding-bottom: 5px;padding-top: 5px;'>"+part4+"</td>";
            }
            else
            {
                tableText += "<td valign='top' style='width: 310px; padding-left: 5px;background-color:#ffffff;padding-bottom: 5px;padding-top: 5px;'>"+part4+"</td>";
            }
            tableText += "</tr>";
        }
        tableText += "</table>";
        tableText = "<html>" + tableText + "</html>";

        return tableText;
    }
    
    
    public void analyzeHistory (int mid, int rev) throws Exception
    {
        String [] file1String = new String[5000];
        
        String [] file2String = new String[5000];
        String [] diff12  = new String[5000];



        writeMethod(mid, rev);
        file1String = getMethodWithLineNumber(mid, rev, 0);

        writeMethod (mid, rev+1);
        file2String = getMethodWithLineNumber(mid, rev+1, 0);

        diff12 = getFileDiff("method-"+mid+"_revision-"+rev+".txt", "method-"+mid+"_revision-"+(rev+1)+".txt");
        
        String compare1 = compareFiles(rev, rev+1, diff12, file1String, file2String );
        jLabel3.setText (compare1);
        
    }
    
    
    public MethodChangeAnalysis() {
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Method Change History");

        jLabel2.setText("jLabel2");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jLabel3.setText("jLabel3");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane1.setViewportView(jLabel3);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("jLabel5");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("jLabel6");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("< Previous Commit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setText("Next Commit >");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Change History Analysis");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 232, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        try
        {
            String methodInfo = getMethodInfo(methodID);
            methodName = methodInfo.split("[:]+")[1];        
            filePath = methodInfo.split("[:]+")[2];        
            commitList = methodInfo.split("[:]+")[3];
            currentCommit = commitList.trim().split("[ ]+")[0];
            currentIndex = 0;


            String labelText = "";
            labelText += "<b>Method Name</b> = "+methodName + "(ID = "+methodID+" )";
            //labelText += "<br/><b>File Path</b> = "+filePath;
            labelText += "<br/><b>Changed in Commits</b> = "+commitList+"";

            jLabel2.setText ("<html><div style='padding-left: 20px;'>"+labelText+"</div></html>");


            int prevision = 0, nrevision=0;
            prevision = Integer.parseInt(commitList.trim().split("[ ]+")[currentIndex].trim());
            nrevision = prevision + 1;
            jLabel5.setText ("Method Instance, Revision = "+prevision);
            jLabel6.setText ("Method Instance, Revision = "+nrevision);
            jLabel4.setText ("Change History Analysis (Commit = "+prevision+")");
            analyzeHistory (methodID, Integer.parseInt(commitList.trim().split("[ ]+")[currentIndex]));
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = formWindowOpened."+e);
        }
    }//GEN-LAST:event_formWindowOpened

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try
        {
            int len = commitList.trim().split("[ ]+").length;
            if (currentIndex < len-1)
            {
                currentIndex++;
            }

            int prevision = 0, nrevision=0;
            prevision = Integer.parseInt(commitList.trim().split("[ ]+")[currentIndex].trim());
            nrevision = prevision + 1;
            jLabel5.setText ("Method Instance, Revision = "+prevision);
            jLabel6.setText ("Method Instance, Revision = "+nrevision);
            jLabel4.setText ("Change History Analysis (Commit = "+prevision+")");
            analyzeHistory (methodID, Integer.parseInt(commitList.trim().split("[ ]+")[currentIndex]));
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = jButton2ActionPerformed."+e) ;
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try
        {
            int len = commitList.trim().split("[ ]+").length;
            if (currentIndex > 0)
            {
                currentIndex--;
            }        
            int prevision = 0, nrevision=0;
            prevision = Integer.parseInt(commitList.trim().split("[ ]+")[currentIndex].trim());
            nrevision = prevision + 1;
            jLabel5.setText ("Method Instance, Revision = "+prevision);
            jLabel6.setText ("Method Instance, Revision = "+nrevision);
            jLabel4.setText ("Change History Analysis (Commit = "+prevision+")");
            analyzeHistory (methodID, Integer.parseInt(commitList.trim().split("[ ]+")[currentIndex]));
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = jButton1ActionPerformed."+e) ;
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(MethodChangeAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MethodChangeAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MethodChangeAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MethodChangeAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MethodChangeAnalysis().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
