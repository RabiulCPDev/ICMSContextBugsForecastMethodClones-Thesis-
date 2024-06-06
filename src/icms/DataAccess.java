/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icms;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author mani
 */
public class DataAccess extends javax.swing.JFrame {

    /**
     * Creates new form DataAccess
     */
    
    CommonParameters cp = new CommonParameters ();
    AttributeNames an = new AttributeNames ();
    
    public DataAccess() {
        initComponents();
    }
    
    
    
    public String [][] getChangedClones (int revision, int cloneType)
    {
        String [][] result = new String[1000][3]; //0 = clone id, 1 = class id, 2 = change count.
        int n = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+"_change.txt")));
            String str = "";
                       
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int gcid = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                int clsid = getCloneClass(revision, gcid, cloneType);
                int changeCount = getChangesToClone(revision, gcid, cloneType);
                
                result[n][0] = gcid+"";
                result[n][1] = clsid+"";
                result[n][2] = changeCount+"";                
                n++;
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangedCloneFragments. "+e);
        }
        
        return result;
    }    

    
    /*public void getChangedClones1 (int revision, int [][] result, int count)
    {
        //int [][] result = new int[1000][3]; //0 = clone id, 1 = class id, 2 = change count.
        int n = 0;        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type123_clonedmethods_version_"+revision+"_change.txt")));
            String str = "";
                       
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int gcid = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                int clsid = getCloneClass(revision, gcid);
                int changeCount = getChangesToClone(revision, gcid);
                
                result[n][0] = gcid;
                result[n][1] = clsid;
                result[n][2] = changeCount;                
                n++;
            }
            br.close();
            count = n;
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangedCloneFragments. "+e);
        }
        
        //return result;
    }*/    
    
    
    public int getCloneClass (int revision, int gcid, int cloneType)
    {
        int result = -1;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (!str.contains(an.globalCloneID)){continue;}
                int id = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                int classid = Integer.parseInt (cp.getAttributeValueFromString(str, an.cloneClassID));
                
                if (gcid == id)
                {
                    result = classid;
                    break;
                }
            }
            br.close();
            br = null;
            str = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in getCloneClassID. "+e);
        }
        
        return result;
    }
    
    public int getChangesToClone (int revision, int gcid, int cloneType)
    {
        int result = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+"_change.txt")));
            String str = "";
                                    
            while ((str = br.readLine())!=null)
            {
                if (str.trim().length() == 0){continue;}
                int id = Integer.parseInt(cp.getAttributeValueFromString(str, an.globalCloneID));
                if (id == gcid)
                {
                    result = Integer.parseInt(cp.getAttributeValueFromString(str, an.cloneChangeCount));
                    break;
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangesToCloneFragment. "+e);
        }
                
        return result;
    }
    
    
    
    public String [][] getPotentialClonePairs (int revision, int cloneType)
    {
        String [][] result = new String [10000][6];
        int count = 0;
                
        String [][] changedClones = getChangedClones(revision, cloneType); // 0 = gcid, 1 = classid, 2 = change count.        
        
        for (int i = 0;changedClones[i][0] != null;i++)
        {
            int gcid = Integer.parseInt(changedClones[i][0]);
            int clsid = Integer.parseInt(changedClones[i][1]);
            int changeCount = Integer.parseInt(changedClones[i][2]);
            
            String [] clones = getClonesFromClass (revision, clsid, cloneType);
            
            for (int j=0;clones[j] != null;j++)
            {
                int gcid2 = Integer.parseInt(clones[j]);
                if (gcid2 != gcid)
                {
                    int changeCount2 = getChangesToClone(revision, gcid2, cloneType);
                    
                    result[count][0] = gcid+"";
                    result[count][1] = clsid+"";
                    result[count][2] = changeCount+"";
                    
                    result[count][3] = gcid2+"";
                    result[count][4] = clsid+"";
                    result[count][5] = changeCount2+"";
                    
                    count++;
                 
                }
            }
        }                
        return result;
    }    
    
    
    public String [] getClonesFromClass (int revision, int classid, int cloneType)
    {
        String [] result = new String [500];
        int i =0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                int id = Integer.parseInt (cp.getAttributeValueFromString(str, an.globalCloneID));
                int clsid = Integer.parseInt (cp.getAttributeValueFromString(str, an.cloneClassID));
                
                if (classid == clsid)
                {
                    result[i] = id+"";
                    i++;
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println ("error in getCloneClassID. "+e);
        }
        
        return result ;
    } 
    
    /*
    returns method ids and corresponding change count.
    */
    public String [][] getChangedMethods (int revision)
    {
        String [][] result = new String [1000][2];
        int n = 0;
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/methods/"+"methods_version_"+revision+"_change.txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                result[n][0] = cp.getAttributeValueFromString(str, an.globalMethodID);
                result[n][1] = cp.getAttributeValueFromString(str, an.methodChangeCount);
                n++;
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println ("error in getChangedMethods. "+e);
        }
        
        return result;        
    }
    
    
    public String [][] getChangedNoncloneMethods (int revision, int cloneType)
    {
        String [][] result = new String[1000][2];
        int n =0;
        
        String [][] changedMethods = getChangedMethods(revision);
        String clonedMethods = " " + getClonedMethods (revision, cloneType) + " ";
        
        for (int i =0;changedMethods[i][0] != null;i++)
        {
            String cm = changedMethods[i][0];
            if (!clonedMethods.contains(" "+cm+" "))
            {
                result[n][0] = changedMethods[i][0];
                result[n][1] = changedMethods[i][1];
                n++;
            }            
        }
                
        return result;
    }
    
    public String getClonedMethods (int revision, int cloneType)
    {
        String result = "";
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (cp.subject_system + "/clonedmethods/"+"type"+cloneType+"_clonedmethods_version_"+revision+".txt")));
            String str = "";
            
            while ((str = br.readLine())!= null)
            {
                if (str.trim().length() == 0){continue;}
                String id = cp.getAttributeValueFromString(str, an.globalMethodID);
                result = result + " " + id + " ";
            }
            br.close();
            br = null;
        }
        catch (IOException e)
        {
            System.out.println ("error in getClonedMethods. "+e);
        }
        return result;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 102, 153));

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 141, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(390, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(385, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(DataAccess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataAccess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataAccess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataAccess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataAccess().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
