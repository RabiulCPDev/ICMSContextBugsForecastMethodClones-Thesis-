
package com.threecam.cycles;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.tree.*;
import com.threecam.*;
import com.threecam.geometry.*;
import com.threecam.math.*;
import com.threecam.toolpath.*;
import com.threecam.util.*;
public class SurfaceScan implements Cycle {
public Mesh object;
public MachineConfig machine;
public MutableTreeNode treeNode;
double xres=0;
double yres=0;
public static final int MODE_XTHENY=0;
public static final int MODE_YTHENX=1;
int scan_mode;
boolean enabled=false;
public SurfaceScan(Mesh object, MachineConfig machine) {
this.object = object;
this.machine = machine;
treeNode = new DefaultMutableTreeNode(this); }
public void setXRes(double xres) {
this.xres = xres; }
public double getXRes() {
return xres; }
public void setYRes(double yres) {
this.yres = yres; }
public double getYRes() {
return yres; }
public void setEnabled(boolean value) {
enabled = value; }
public boolean getEnabled() {
return enabled; }
public void setScanMode(int scan_mode) {
this.scan_mode = scan_mode; }
public int getScanMode() {
return scan_mode; }
public SurfaceScanConfigFrame getConfigFrame() {
SurfaceScanConfigFrame configFrame = new SurfaceScanConfigFrame(this);
return configFrame; }
public MutableTreeNode getGUITreeNode() {
return treeNode; }
public String getDescription() {
return "SurfaceScan : " + xres + "X" + yres; }
public Program getProgram() {
if(!enabled)
return new Program();
Program ncprogram = new Program();
ncprogram.addSegment(new RapidLinear(new MachineCoordinate(machine,null,null,new Double(0),null,null,null)));
ncprogram.addSegment(new RapidLinear(new MachineCoordinate(machine,new Double(0),new Double(0),null,null,null,null)));
int segres = (int)Math.round(Math.sqrt(object.countTriangles()));
System.out.println("Generating " + segres + "x" + segres + " segmented mesh...");
SegmentedMesh3ax segmesh = new SegmentedMesh3ax(object,segres,segres,1,new ProgressListener() {
public void updateProgress(int percent) { }
});
XYZBounds bounds = object.getCartesianBoundingBox();
double xa=(bounds.Xmax-bounds.Xmin)/((double)xres);
double xb=bounds.Xmin;
double ya=(bounds.Ymax-bounds.Ymin)/((double)yres);
double yb=bounds.Ymin;
System.out.println("Scanning...");
int percent = 0;
for(int i=0;i<xres;i++) {
double x=xa*((double)i)+xb;
ncprogram.addSegment(new RapidLinear(new MachineCoordinate(machine, x, 0d, null)));
for(int j=0;j<yres;j++) {
double y=ya*((double)j)+yb;
double zm=segmesh.findMinZToolDepthQuick(x, y, machine.tools.getTool(0));
MachineCoordinate moveto = new MachineCoordinate(machine);
moveto.x = x;
moveto.y = y;
moveto.z = zm;
ncprogram.addSegment(new FeedLinear(22,moveto)); }
ncprogram.addSegment(new RapidLinear(new MachineCoordinate(machine, null,null,new Double(0)))); }
System.out.println("DONE");
return ncprogram; }
public String toString() {
return "Surface Scan"; } }