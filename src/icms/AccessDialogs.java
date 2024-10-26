/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icms;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Manishankar
 */
public class AccessDialogs extends javax.swing.JFrame {

    /**
     * Creates new form AccessDialogs
     */
    public AccessDialogs() {
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

        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("ICMS (Integrated Clone Management System)");

        jButton1.setText("Show Clone Analysis Dialog");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Show Method Analysis Dialog");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Subject System Path");

        jTextField1.setText("jTextField1");

        jLabel4.setText("Programming Language");

        jTextField2.setText("jTextField2");

        jLabel5.setText("Last Revision Number");

        jTextField3.setText("jTextField3");

        jButton10.setText("Save Parameters");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel1.setText("Database Connection String");

        jTextField4.setText("jTextField4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5))
                                .addGap(51, 51, 51))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(29, 29, 29)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .addComponent(jTextField3)
                            .addComponent(jTextField2)
                            .addComponent(jTextField1))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jButton10)
                .addContainerGap())
        );

        jButton3.setText("Visualize Code");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Investigating Bugproneness");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Store Changes to Database");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Investigating Bug Prediction");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Investigating Bug Propagation");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Consistent And Inconsistent");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Investigating Context Bugs in Code Clones");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton11.setText("Forecasting Context Bugs in Code Clones");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Granularity-based comparison of the bug-proneness of code clones");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Investigating Bug-replication in SPCP micro-clones ");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(153, 153, 153))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:

        SelectParameters sp = new SelectParameters();
        sp.saveSystemInformation(jTextField1.getText().trim(), jTextField2.getText().trim(), 1, Integer.parseInt(jTextField3.getText().trim()), jTextField4.getText().trim());
        setVisible(false);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        CloneAnalysisDialog cdd = new CloneAnalysisDialog();
        cdd.setVisible(true);
        cdd.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocation(100, 100);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        String subject_system = "";
        String repositoryURL = "";
        String language = "";
        int revisionCount = 0;
        String connectionstring = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("common_parameters.txt")));
            subject_system = br.readLine().split("[=]+")[1].trim();
            repositoryURL = br.readLine().split("[=]+")[1].trim();
            language = br.readLine().split("[=]+")[1].trim();
            revisionCount = Integer.parseInt(br.readLine().split("[=]+")[1].trim());
            connectionstring = br.readLine().split("[=]+")[1].trim();
        } catch (Exception e) {
        }

        jTextField1.setText(subject_system);
        jTextField2.setText(language);
        jTextField3.setText(revisionCount + "");
        jTextField4.setText(connectionstring);

        setLocation(100, 100);
    }//GEN-LAST:event_formWindowOpened

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        MethodAnalysisDialog mad = new MethodAnalysisDialog();
        mad.setVisible(true);
        mad.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        VisualizeCode vc = new VisualizeCode();
        vc.setVisible(true);
        vc.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        InvestigatingBugproneness ib = new InvestigatingBugproneness();
        showFrame(ib);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        ModificationManagement mm = new ModificationManagement();
        mm.extractModifications();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:

        InvestigatingBugPrediction ibp = new InvestigatingBugPrediction();
        showFrame(ibp);

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        InvestigatingBugPropagation ibp = new InvestigatingBugPropagation();
        showFrame(ibp);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:

        ConsistentAndInconsistentChangePatterns cai = new ConsistentAndInconsistentChangePatterns();
        showFrame(cai);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        ContextBugs instance = new ContextBugs();
        instance.investigation();
        //showFrame (instance);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:

        ContextBugs instance = new ContextBugs();
        instance.investigation2();


    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:

//        investigation1 ();
        investigation2();

    }//GEN-LAST:event_jButton12ActionPerformed

    // Investigating Bug Replication in SPCP micro-clones

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        investigation_Bug_replication_clones_SPCP_Clone();
    }//GEN-LAST:event_jButton13ActionPerformed

    public void investigation_Bug_replication_clones_SPCP_Clone() {
        DatabaseAccess da = new DatabaseAccess();
        CommonParameters cp = new CommonParameters();
        InvestigatingBugproneness ib = new InvestigatingBugproneness();
        String bugfixcommits = ib.getBugFixCommits();
        Clone_SPCP clone_spcp = new Clone_SPCP();
        // Connect with database
        da.connect();

        String ctype = JOptionPane.showInputDialog("Clone Type ?");
        int clonetype = Integer.parseInt(ctype);

        // Get SPCP clones
        SingleSPCPClonePair[] spcpClones = da.getSPCPCloness(clonetype);

        // show the revision because of bug-fix
        System.out.println("Revision Created because of bug-fix:::::" + bugfixcommits);
        int sameFile=0,difFile=0;
        for (int i = 477; i < 478; i++) {
            System.out.println("Working on revison no : " + i);

            SingleClone[] clones = da.getClones(i - 1, clonetype);
            SingleChange[] changes = da.getChangess(i - 1);
            SingleClonePair[] clonePairs = da.getClonePairs(i-1, clonetype);
            Clone_SPCP checkClass = new Clone_SPCP();
            
            
            if(!bugfixcommits.contains(" "+i+" ")) continue;
            
            int clone1ind=-1,clone2ind=-1;
            int ind=0;
            while(clones[ind]!=null){
                int in=0;
                while(clonePairs[in]!=null){
                    if(clonePairs[in].cloneid1.equals(clones[ind].cloneid)){
                        clone1ind=ind;
                        break;
                    }
                    in++;
                }
                ind++;
                if(clone1ind!=-1) break;
                
            }
            ind =0;
             while(clones[ind]!=null){
                int in=0;
                while(clonePairs[in]!=null){
                    if(clonePairs[in].cloneid2.equals(clones[ind].cloneid)){
                        clone2ind=ind;
                        break;
                    }
                    in++;
                }
                ind++;
                if(clone2ind!=-1) break;
                
            }
            
            boolean c1 = checkClass.checkBugReplication(clones, changes, clone1ind);
            boolean c2 = checkClass.checkBugReplication(clones, changes, clone2ind);
            // lets check
            
            System.out.println(clones[clone1ind].cloneid + " "+clones[clone1ind].startline+" "+clones[clone1ind].endline);
            System.out.println(clones[clone2ind].cloneid + " "+clones[clone2ind].startline+" "+clones[clone2ind].endline);
            
            
            
            if(c1 || c2){
                if(clones[clone1ind].filepath.equals(clones[clone2ind].filepath)){
                    System.out.println(clones[clone1ind].filepath+" matches "+clones[clone2ind].filepath);
                    sameFile++; 
                }else{
                    difFile++;
                }
            }
            
        }

        System.out.println("Total Same File = "+sameFile);
        System.out.println("Total not same File ="+difFile);

        // Disconnect Database
        da.disconnect();
    }

    public void investigation2() {
        DatabaseAccess da = new DatabaseAccess();
        CommonParameters cp = new CommonParameters();
        InvestigatingBugproneness ib = new InvestigatingBugproneness();
        String bugfixcommits = ib.getBugFixCommits();

        //opening the connection to the database.
        da.connect();

        String ctype = JOptionPane.showInputDialog("Clone Type ?");
        int clonetype = Integer.parseInt(ctype);

        int mclones = 0, bclones = 0, mclones_buggy = 0, bclones_buggy = 0, mclones_changed = 0;
        String mclones_buggy_string = "", bclones_buggy_string = "", mclones_string = "", bclones_string = "";
        int mclones_changes = 0, mclones_changes_buggy = 0, bclones_changes = 0, bclones_changes_buggy = 0;
        int bfcommits = 0;
        String whochanged = "";

        for (int i = 1; i < cp.revisionCount; i++) {
            System.out.println("working on revision " + i);

            //get clones from revision i.                                   
            SingleClone[] clones = da.getClones(i - 1, clonetype);
            int l = clones.length;

            int bugfixflag = 0;

            for (int j = 0; clones[j] != null; j++) {
                mclones++;
                int changecount = Integer.parseInt(clones[j].changecount);
                mclones_changes += changecount;

                if (changecount > 0) {

                    mclones_changed++;
                    String committer = ib.getCommitter(i);
                    if (!whochanged.contains(" " + committer + " ")) {
                        whochanged += " " + committer + " ";
                    }
                }

                //checking if this revision was created for a bug-fix commit.
                if (!bugfixcommits.contains(" " + i + " ")) {
                    continue;
                }
                bugfixflag = 1;
                System.out.println("This is revision no " + i + " commiter :" + ib.getCommitter(i));
                int bugfix = 0;
                if (changecount > 0) {
                    bugfix = 1;
                }

                if (bugfix == 1) {

                    mclones_buggy++;
                    mclones_changes_buggy += changecount;
                }
            }
            if (bugfixflag == 1) {
                bfcommits++;
            }

            System.out.println("method clones = " + mclones + ", buggy method clone = " + mclones_buggy + ", all changes = " + mclones_changes + ", buggy changes in method clones = " + mclones_changes_buggy + " method clones changed = " + mclones_changed);
            System.out.println("no of committers = " + whochanged.trim().split("[ ]+").length + "\n");
        }

        float r1 = (float) mclones_changes_buggy * 100 * cp.revisionCount / mclones;
        float r2 = (float) mclones_changes_buggy * 100 / mclones_changes;
        float r3 = (float) mclones_buggy * 100 * bfcommits / mclones;
        float r4 = (float) mclones_changes * cp.revisionCount / mclones;
        float r5 = (float) mclones_changed * 100 / mclones;

        System.out.println("bug-fix changes per 100 method clones per revision = " + r1);
        System.out.println("percentage of bug-fix changes w.r.t all changes = " + r2);
        System.out.println("percentage of code clones that experienced bug-fix changes w.r.t all clones per bug-fix commit = " + r3);
        System.out.println("changes per method clone per revision = " + r4);
        System.out.println("percentage of changed method clones = " + r5);

        //disconnecting database.
        da.disconnect();

    }

    public void investigation1() {
        DatabaseAccess da = new DatabaseAccess();
        CommonParameters cp = new CommonParameters();
        InvestigatingBugproneness ib = new InvestigatingBugproneness();
        String bugfixcommits = ib.getBugFixCommits();

        String ctype = JOptionPane.showInputDialog("Clone Type ?");
        int clonetype = Integer.parseInt(ctype);

        int mclones = 0, bclones = 0, mclones_buggy = 0, bclones_buggy = 0;
        String mclones_buggy_string = "", bclones_buggy_string = "", mclones_string = "", bclones_string = "";
        int mclones_changes = 0, mclones_changes_buggy = 0, bclones_changes = 0, bclones_changes_buggy = 0;

        for (int i = 1; i < cp.revisionCount; i++) {
            System.out.println("working on revision " + i);

            //get clones from revision i.                                   
            SingleClone[] clones = da.getClones(i - 1, clonetype);
            int l = clones.length;

            for (int j = 0; clones[j] != null; j++) {
                int gcid = Integer.parseInt(clones[j].globalcloneid);
                if (gcid <= -1) {
                    continue;
                }
                int methodclone = 1;
                //methodclone = ib.isMethodClone(gcid, clonetype, i-1);

                int changecount = Integer.parseInt(clones[j].changecount);
                if (methodclone == 1) {
                    mclones_changes += changecount;
                } else {
                    bclones_changes += changecount;
                }

                //checking if this revision was created for a bug-fix commit.
                if (!bugfixcommits.contains(" " + i + " ")) {
                    continue;
                }

                //int gcid = Integer.parseInt(clones[j].globalcloneid);                
                //if (gcid <= -1) { continue; }                
                //int methodclone = 0;
                //methodclone = ib.isMethodClone(gcid, clonetype, i-1);
                //int changecount = Integer.parseInt(clones[j].changecount);
                if (methodclone == 1) {
                    if (!mclones_string.contains(" " + gcid + " ")) {
                        mclones_string += " " + gcid + " ";
                    }
                } else {
                    if (!bclones_string.contains(" " + gcid + " ")) {
                        bclones_string += " " + gcid + " ";
                    }
                }

                int bugfix = 0;
                //int changecount = 0;
                if (clones[j].changecount != null) {
                    changecount = Integer.parseInt(clones[j].changecount);
                }
                if (changecount > 0) {
                    bugfix = 1;
                }

                if (methodclone == 1) {
                    mclones++;
                    if (bugfix == 1) {
                        if (!mclones_buggy_string.contains(" " + gcid + " ")) {
                            mclones_buggy_string += " " + gcid + " ";
                        }

                        mclones_buggy++;
                        mclones_changes_buggy += changecount;
                        //if (!mclones_buggy_string.contains (""+clones[j].globalcloneid+"")) { mclones_buggy_string += ""+clones[j].globalcloneid+""; }
                    }
                } else {
                    bclones++;
                    if (bugfix == 1) {
                        if (!bclones_buggy_string.contains(" " + gcid + " ")) {
                            bclones_buggy_string += " " + gcid + " ";
                        }
                        bclones_buggy++;
                        bclones_changes_buggy += changecount;
                        //if (!bclones_buggy_string.contains (""+clones[j].globalcloneid+"")) { bclones_buggy_string += ""+clones[j].globalcloneid+""; }                        
                    }
                }

            }

            String mbuggy = mclones_buggy_string;
            String bbuggy = bclones_buggy_string;

            System.out.println("method clones = " + mclones_string.trim().split("[ ]+").length
                    + ", method clones buggy = " + mbuggy.trim().split("[ ]+").length + ", method clones buggy changes = " + mclones_changes_buggy);
            //System.out.println ("block clones buggy = "+bbuggy.trim().split("[ ]+").length+", block clones buggy changes = "+bclones_changes_buggy);

        }

        mclones_buggy = mclones_buggy_string.trim().split("[ ]+").length;
        mclones = mclones_string.trim().split("[ ]+").length;

        bclones_buggy = bclones_buggy_string.trim().split("[ ]+").length;
        bclones = bclones_string.trim().split("[ ]+").length;

        float mclones_buggy_percentage = (float) mclones_buggy / mclones;
        float bclones_buggy_percentage = (float) bclones_buggy / bclones;

        System.out.println("\n\nmethod clone count = " + mclones + ", buggy method clones count = " + mclones_buggy);
        System.out.println("method clones buggy percentage = " + mclones_buggy_percentage);
        //System.out.println ("block clone count = "+bclones+", buggy block clones count = "+bclones_buggy);
        //System.out.println ("block clones buggy percentage = "+bclones_buggy_percentage);

        System.out.println("\n");

        System.out.println("changes in method clones = " + mclones_changes + ", buggy changes in method clones = " + mclones_changes_buggy);
        System.out.println("percentage of buggy changes in method clones = " + (float) (mclones_changes_buggy * 100 / mclones_changes));
        //System.out.println ("changes in block clones = "+bclones_changes+", buggy changes in block clones = "+bclones_changes_buggy);
        //System.out.println ("percentage of buggy changes in block clones = "+ (float)(bclones_changes_buggy*100/bclones_changes));

        System.out.println("\n");

        System.out.println("bug-fix changes per 100 method clones = " + ((float) mclones_changes_buggy * 100 / mclones));
        //System.out.println ("bug-fix changes per 100 fragment clones = "+((float)bclones_changes_buggy*100/bclones));

    }

    public void showFrame(JFrame instance) {
        instance.setVisible(true);
        instance.setLocation(100, 100);
        instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

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
            java.util.logging.Logger.getLogger(AccessDialogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AccessDialogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AccessDialogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AccessDialogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AccessDialogs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
