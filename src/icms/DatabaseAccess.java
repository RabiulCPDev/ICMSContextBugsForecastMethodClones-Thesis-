/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
/**
 *
 * @author manishankar
 */

class SingleSPCPClonePair
{
    String globalcloneid1 = "-1", globalcloneid2 = "-1", madepairsinrevisions = "", gcid1changedinrevisions = "", gcid2changedinrevisions = "";
    int clonetype = 0;
    
    String insertStatement ()
    {
        return "insert into type"+clonetype+"spcpclones (globalcloneid1, globalcloneid2, madepairsinrevisions, gcid1changedinrevisions, gcid2changedinrevisions) values ("
                + "'"+globalcloneid1+"',"
                + "'"+globalcloneid2+"',"
                + "'"+madepairsinrevisions+"',"
                + "'"+gcid1changedinrevisions+"',"
                + "'"+gcid2changedinrevisions+"')";
    }
}

class SingleLatePropagation
{
    String globalcloneid1 = "-1", globalcloneid2 = "-1", divergedafter = "-1", convergedin = "-1", isacceptable = "0";
    int clonetype=0;
    
    String insertStatement ()
    {
        return "insert into type"+clonetype+"latepropagations (globalcloneid1, globalcloneid2, divergedafter, convergedin, isacceptable) values ('"+globalcloneid1+"','"+globalcloneid2+"','"+divergedafter+"','"+convergedin+"','"+isacceptable+"')";
    }
}

class SingleClone
{
    String cloneid="-1", methodid="-1", globalcloneid="-1", changecount="0", startline="-1", endline="-1", revision="-1";
    String filepath="";
    int clonetype;
    
    String insertStatement ()
    {
        return "insert into type"+clonetype+"clones (revision, cloneid, methodid, globalcloneid, changecount, startline, endline, filepath) values ("
                + "'"+revision+"',"
                + "'"+cloneid+"',"
                + "'"+methodid+"',"
                + "'"+globalcloneid+"',"
                + "'"+changecount+"',"
                + "'"+startline+"',"
                + "'"+endline+"',"
                + "'"+filepath+"'"
                + ")";
    }
}

class SingleClonePair
{
    String cloneid1="-1", cloneid2="-1", revision = "-1", globalcloneid1 = "-1", globalcloneid2 = "-1";
    int clonetype;
    
    String insertStatement ()
    {
        return "insert into type"+clonetype+"clonepairs (revision, cloneid1, cloneid2, globalcloneid1, globalcloneid2) values ('"+revision+"','"+cloneid1+"','"+cloneid2+"','"+globalcloneid1+"','"+globalcloneid2+"')";
    }
}

class SingleMethod
{
    String methodid="-1", globalmethodid="-1", revision="-1", changecount="0", startline="-1", endline="-1", signature="", packagename="", classname="";
    String filepath="", methodname = "";
    
    String insertStatement ()
    {
        return "insert into methods (revision, methodid, globalmethodid, filepath, startline, endline, methodname, signature, packagename, classname, changecount) values ("
                + "'"+revision+"',"
                + "'"+methodid+"',"
                + "'"+globalmethodid+"',"
                + "'"+filepath+"',"
                + "'"+startline+"',"
                + "'"+endline+"',"
                + "'"+methodname+"',"
                + "'"+signature+"',"
                + "'"+packagename+"',"
                + "'"+classname+"',"
                + "'"+changecount+"'"
                + ")";               
    }
}

class SingleChange
{
    String revision="-1", startline="-1", endline="-1", filepath="-1", changetype="-1";        
}


public class DatabaseAccess {
    Connection conn ;
    Statement stmt ;
    public ResultSet result ;
    CommonParameters cp = new CommonParameters ();

    String connectionString = "jdbc:mysql://localhost:3306/carol" ;
    String userID = "root" ;
    String password = "Rabiul@34" ;


    DatabaseAccess ()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(cp.connectionString, userID, password);        
        }
        catch (Exception e)
        {
            
        }
    }    
    
    
    //connection is made here.
    public void connect ()
    {
        try
        {
            if (conn.isClosed())
            {            
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(cp.connectionString, userID, password);
            }
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = connect."+e);
        }
    }

    //connection is closed here.
    public void disconnect ()
    {
        try
        {
            conn.close();
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = disconnect.") ;
        }
    }

    //query is made here.
    public void executeQuery (String query)
    {
        try
        {
            stmt = conn.createStatement();	   //creating the statement.
            result = stmt.executeQuery(query); //executing the query.
        }
        catch (Exception e)
        {
            System.out.println ("\ncan not connect"  + e) ;
        }
    }


    //this is for inserting.
    public void executeUpdate (String query)
    {
        try
        {
            connect () ;

            stmt = conn.createStatement();	   //creating the statement.
            stmt.executeUpdate(query); //executing the query.

            disconnect () ;
        }
        catch (Exception e)
        {
            System.out.println ("\ncan not connect"  + e) ;
        }
    }
    
    /*public void writeDataToFile (String filepath, String [] data, int count)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter (new FileWriter(filepath, true));
            for (int i =0;i<count;i++)
            {
                writer.write ("\n"+data[i]);
            }
            writer.close();            
        }
        catch (Exception e)
        {
            System.out.println ("error. writeDataToFile. " + e);
        }        
    }*/
    
    /*public String [] readDataFromFile (String filepath, int revision)
    {
        String [] data = new String [500000];
        
        try
        {
            BufferedReader br = new BufferedReader (new InputStreamReader (new FileInputStream (filepath)));
            String str = "";
            
            while ((str = br.readLine ())!= null)
            {
                if ()
            }
        }
        catch (Exception e)
        {
            
        }
        
        return data;    
    }*/
    
    public boolean hasData (String tablename)
    {
        boolean hasdata = false;
        try
        {
            connect ();
            executeQuery ("select count(*) cnt from "+tablename);
            result.next();
            int cnt = Integer.parseInt (result.getString("cnt"));
            if (cnt > 0)
            {
                hasdata = true;
            }
            disconnect ();
        }
        catch (Exception e)
        {
            
        }                
        return hasdata;
    }
    
    
    public void insertData (String [] queries, int count)
    {
        if (count == 0) {return;}
        
        String firstpart = queries[0].split("values")[0];
        String query = queries[0].split("values")[0];
        String data = "";
        
        
        System.out.println ("inserting data begins, count = "+count);
        connect ();
        int i =0;
        while (i<count)
        {
            data = "";
            for (int j =0;j<100;j++)
            {
                if (i < count)
                {
                    data = data + ","+ queries[i].split(" values ")[1];
                    i++;
                }
                else
                {
                    break;
                }
            }  
            if (data.length() > 0)
            {
                data = data.substring(1);
                
                executeUpdate (firstpart + " values " + data + ";");
            }
        }
        disconnect ();
        System.out.println ("inserting data ends.");
    }
    
    public void insertSPCPClones (SingleSPCPClonePair [] spcppairs)
    {
        String [] queries = new String[100000];
        int i = 0;
        for (i=0;spcppairs[i] != null;i++)
        {
            queries[i] = spcppairs[i].insertStatement();
        }
        insertData (queries, i);                
    }
    
    public void insertMethods (SingleMethod [] methods)
    {
        String [] queries = new String[100000];
        int i = 0;
        for (i=0;methods[i] != null;i++)
        {
            queries[i] = methods[i].insertStatement();
        }
        insertData (queries, i);        
    }
    
    public void insertClones (SingleClone [] clones, int clonetype)
    {
        String [] queries = new String[100000];
        int i = 0;
        for (i=0;clones[i] != null;i++)
        {
            clones[i].clonetype = clonetype;
            queries[i] = clones[i].insertStatement();
        }
        insertData (queries, i);
    }
    
    public void insertClonePairs (SingleClonePair [] clonepairs, int clonetype)
    {
        String [] queries = new String[100000];
        int i =0;
        for (i=0;clonepairs[i] != null;i++)
        {
            clonepairs[i].clonetype = clonetype;
            queries[i] = clonepairs[i].insertStatement();
        }
        insertData (queries, i);
    }
    
    public void insertLatePropagations (SingleLatePropagation [] lps, int clonetype)
    {
        String [] queries = new String[100000];
        int i =0;
        for (i=0;lps[i] != null;i++)
        {
            lps[i].clonetype = clonetype;
            queries[i] = lps[i].insertStatement();
        }
        insertData (queries, i);        
    }
    
    public SingleSPCPClonePair [] getSPCPClones (int clonetype)
    {
        SingleSPCPClonePair [] pairs = new SingleSPCPClonePair[10000000];
        int i = 0;
        try
        {
            connect();
            executeQuery ("select * from type"+clonetype+"spcpclones");
            while (result.next())
            {
                pairs[i] = new SingleSPCPClonePair ();
                pairs[i].globalcloneid1 = result.getString ("globalcloneid1");
                pairs[i].globalcloneid2 = result.getString ("globalcloneid2");
                pairs[i].madepairsinrevisions = result.getString ("madepairsinrevisions");
                pairs[i].gcid1changedinrevisions = result.getString ("gcid1changedinrevisions");
                pairs[i].gcid2changedinrevisions = result.getString ("gcid2changedinrevisions");
                i++;
            }
            disconnect ();
        }
        catch (Exception e)
        {
            System.out.println ("error. "+e);
        }
        return pairs;
    }
    
    public SingleClone getCloneInfo (int gcid, int clonetype, int revision)
    {
        SingleClone clone = new SingleClone ();
        try
        {
            connect ();
            executeQuery ("select filepath, startline, endline, changecount from type"+clonetype+"clones where revision = "+revision+" and globalcloneid =  "+gcid);
            if (result.next())
            {
                clone.revision = revision+"";
                clone.filepath = result.getString ("filepath");
                clone.startline = result.getString ("startline");
                clone.endline = result.getString ("endline");
                clone.changecount = result.getString ("changecount");
            }
            else
            {
                clone = null;
            }
            disconnect ();
        }
        catch (Exception e)
        {
            clone = null;
        }
        return clone;
    }
    
    public int isClonePair (int gcid1, int gcid2, int clonetype, int revision)
    {
        int pair = 0;
        try
        {
            connect();
            executeQuery ("select count(*) cnt from type"+clonetype+"clonepairs where revision = "+revision+" and ((globalcloneid1 = "+gcid1 + " and globalcloneid2 = "+gcid2+") or (globalcloneid1 = "+gcid2 + " and globalcloneid2 = "+gcid1+"))");
            if (result.next())
            {
                pair = Integer.parseInt(result.getString("cnt"));
            }
            disconnect ();                            
        }
        catch (Exception e)
        {
            System.out.println ("error. code = 4.");
        }
        return pair;
    }
    
    public int isClone (int startline, int endline, String filepath, int clonetype, int revision)
    {
        int clone =0;
        try
        {
            connect();
            executeQuery ("select count(*) cnt from type"+clonetype+"clones where revision = "+revision+" and startline = "+startline+" and endline = "+endline+" and filepath = '"+filepath+"' ");
            if (result.next())
            {
                clone = Integer.parseInt(result.getString("cnt"));
            }
            disconnect ();                            
        }
        catch (Exception e)
        {
            System.out.println ("error. code = 4.");
        }
        
        return clone;
    }
    
    public SingleClone [] getClones (int revision, int clonetype)
    {
        SingleClone [] clones = new SingleClone[10000];
        int i = 0;
        try
        {
            //connect();
            executeQuery ("select * from type"+clonetype+"clones where revision = "+revision);
            while (result.next())
            {
                clones[i] = new SingleClone ();
                clones[i].revision = result.getString("revision");
                clones[i].cloneid = result.getString("cloneid");
                clones[i].methodid = result.getString("methodid");
                clones[i].globalcloneid = result.getString("globalcloneid");
                clones[i].changecount = result.getString("changecount");
                clones[i].filepath = result.getString("filepath");
                clones[i].startline = result.getString("startline");
                clones[i].endline = result.getString("endline");
                i++;
            }
            
            //disconnect ();
        }
        catch (Exception e)
        {
            System.out.println ("error. "+e);
        }
        return clones;
    }
    
    public SingleLatePropagation [] getLatePropagations (int clonetype)
    {
        SingleLatePropagation [] lps = new SingleLatePropagation [100000];
        int i = 0;
        
        try
        {
            connect ();
            executeQuery ("select * from type"+clonetype+"latepropagations");
            while (result.next())
            {
                lps[i] = new SingleLatePropagation ();
                lps[i].clonetype = clonetype;
                lps[i].globalcloneid1 = result.getString ("globalcloneid1");
                lps[i].globalcloneid2 = result.getString ("globalcloneid2");
                lps[i].divergedafter = result.getString ("divergedafter");
                lps[i].convergedin = result.getString ("convergedin");
                lps[i].isacceptable = result.getString ("isacceptable");
                i++;
            }
            disconnect ();
        }
        catch (Exception e)
        {
            System.out.println ("error. "+e);
        }
        
        return lps;
    }
    
    public void deleteClones (int revision, int clonetype)
    {
        connect ();
        executeUpdate ("delete from type"+clonetype+"clones where revision = "+revision);
        disconnect ();
        
        System.out.println ("clones are deleted.");
    }
    
    public SingleClonePair [] getClonePairs (int revision, int clonetype)
    {
        SingleClonePair [] clonepairs = new SingleClonePair[10000];
        int i =0;
        try
        {
            connect();
            executeQuery ("select * from type"+clonetype+"clonepairs where revision = "+revision);
            while (result.next ())
            {
                clonepairs[i] = new SingleClonePair ();
                clonepairs[i].revision = result.getString ("revision");
                clonepairs[i].cloneid1 = result.getString ("cloneid1");
                clonepairs[i].cloneid2 = result.getString ("cloneid2");
                clonepairs[i].globalcloneid1 = result.getString ("globalcloneid1");
                clonepairs[i].globalcloneid2 = result.getString ("globalcloneid2");
                clonepairs[i].clonetype = clonetype;
                i++;
            }
            disconnect ();
        }
        catch (Exception e)
        {
            System.out.println ("error. "+e);
        }
        
        return clonepairs;
    }
    
    public void deleteClonePairs (int revision, int clonetype)
    {
        connect ();
        executeUpdate ("delete from type"+clonetype+"clonepairs where revision = "+revision);
        disconnect ();
        
        System.out.println ("clone-pairs are deleted.");
    }    
    
    public SingleChange [] getChanges (int revision)
    {
        SingleChange [] changes = new SingleChange [10000];
        int i =0;
        
        try
        {
            connect ();
            executeQuery ("select * from changes where revision = "+revision);
            while (result.next ())
            {
                changes[i] = new SingleChange ();
                changes[i].revision = result.getString ("revision");
                changes[i].filepath = result.getString ("filepath");
                changes[i].startline = result.getString ("startline");
                changes[i].endline = result.getString ("endline");
                changes[i].changetype = result.getString ("changetype");
                i++;
            }
            disconnect ();
        }
        catch (Exception e)
        {
            System.out.println ("error. "+e);
        }
        
        return changes;     
    }
    
    
    public SingleMethod [] getMethods (int revision)
    {
        SingleMethod [] methods = new SingleMethod[100000];
        int i =0;
        
        try
        {
            connect ();
            executeQuery ("select * from methods where revision = "+revision);
            while (result.next ())
            {
                methods[i] = new SingleMethod ();
                methods[i].revision = revision+"";
                methods[i].filepath = result.getString("filepath");
                methods[i].startline = result.getString("startline");
                methods[i].endline = result.getString("endline");
                methods[i].signature = result.getString("signature");
                methods[i].packagename = result.getString("packagename");
                methods[i].classname = result.getString("classname");
                methods[i].changecount = result.getString("changecount");
                methods[i].globalmethodid = result.getString("globalmethodid");
                methods[i].methodid = result.getString("methodid");
                
                i++;
            }
            disconnect ();
        }
        catch (Exception e)
        {
            System.out.println ("error. "+e);
        }
        
        return methods;
    }
    
    public String [] getData (String tablename, String fields, String condition)
    {
        String [] dataset = new String[500000];
        int count = 0;
        
        try
        {
            connect();
            executeQuery ("select * from "+tablename+ " " + condition);
            while (result.next())
            {
                String data = "";
                int l = fields.trim().split("[,]+").length;
                for (int i =0;i<l-1;i++)
                {
                    data += result.getString(fields.trim().split("[,]+")[i].trim()) + ", ";
                }
                data += result.getString(fields.trim().split("[,]+")[l-1].trim());
                dataset[count] = data;
                count++;
            }
            disconnect();
        }
        catch(Exception e)
        {
            System.out.println ("error 111. "+e);
        }
        return dataset;
    }
    
    String [] getData (String query, String fields)
    {
        String [] dataset = new String[500000];
        int count = 0;
        
        try
        {
            connect();
            executeQuery (query);
            while (result.next())
            {
                String data = "";
                int l = fields.trim().split("[,]+").length;
                for (int i =0;i<l-1;i++)
                {
                    data += result.getString(fields.trim().split("[,]+")[i].trim()) + ", ";
                }
                data += result.getString(fields.trim().split("[,]+")[l-1].trim());
                dataset[count] = data;
                count++;
            }
            disconnect();
        }
        catch(Exception e)
        {
            System.out.println ("error 111. "+e);
        }
        return dataset;        
    }
    
    
    
    
    public void fixDatabase ()
    {
        try
        {
            int [] mids = new int[1000000];
            int [] gmids = new int[1000000];
            int cnt=0;
            
            int [] cmids = new int[10000];
            int cnt1 = 0;

            for (int i =1;i<1699;i++)
            {
                System.out.println ("version = "+i);
                cnt = 0;
                connect();
                executeQuery ("select globalmethodid, methodid from method where version = "+i+" ");
                while (result.next ())
                {
                    mids[cnt] = Integer.parseInt(result.getString("methodid"));
                    gmids[cnt] = Integer.parseInt(result.getString("globalmethodid"));
                    cnt++;
                }
                disconnect ();
                
                
                //this is for clonedmethod1.
                for (int t = 1;t<=3;t++)
                {
                    cnt1 = 0;
                    connect ();
                    executeQuery ("select methodid from clonedmethod"+t+" where version = "+i);
                    while (result.next())
                    {
                        cmids[cnt1] = Integer.parseInt(result.getString("methodid"));
                        cnt1++;
                    }
                    disconnect ();
                    int j=0, k=0;
                    for (j=0;j<cnt1;j++)
                    {
                        for (k=0;k<cnt;k++)
                        {
                            if (cmids[j] == mids[k])
                            {
                                connect ();
                                executeUpdate("update clonedmethod"+t+" set globalmethodid="+gmids[k]+" where methodid = "+mids[k] +" and version = "+i);
                                disconnect ();                            
                                System.out.println ("clonedmethod"+t+" updating: version = "+i+", method id = "+mids[k]);
                                break;
                            }
                        }
                    }
                }                             
            }
        }
        catch (Exception e)
        {
            System.out.println ("error. method name = fixDatabase");
        }
    }
}

