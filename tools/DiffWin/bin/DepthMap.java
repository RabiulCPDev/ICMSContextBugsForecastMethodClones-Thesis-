
package com.threecam.geometry;
import java.util.*;
import com.threecam.math.Vector3D;
import com.threecam.util.ProgressListener;
public class DepthMap {
public double[][] depth;
public Mesh object;
double[][] points = null;
double[][] map = null;
double[][][] pointmap = null;
ProgressListener pl;
int width;
int height;
public DepthMap(Mesh object, int width, int height) {
this.object = object;
this.width = width;
this.height = height; }
public DepthMap(Mesh object, int width, int height, ProgressListener pl) {
this.object = object;
this.width = width;
this.height = height;
this.pl = pl; }
public void computeDepthMap() {
int segres = (int)Math.round(Math.sqrt(object.countTriangles()));
System.out.println("Generating " + segres + "x" + segres + " segmented mesh...");
SegmentedMesh3ax segmesh = new SegmentedMesh3ax(object,segres,segres,1,pl);
XYZBounds bounds = object.getCartesianBoundingBox();
System.out.println("Generating depth map...");
System.out.println("STL Bounds: " + bounds);
points = new double[width*height][3];
map = new double[width][height];
pointmap = new double[width][height][3];
double xa=(bounds.Xmax-bounds.Xmin)/((double)width);
double xb=bounds.Xmin;
double ya=(bounds.Ymax-bounds.Ymin)/((double)height);
double yb=bounds.Ymin;
System.out.println("Scanning...");
int percent = 0;
for(int i=0;i<width;i++) {
double x=xa*((double)i)+xb;
for(int j=0;j<height;j++) {
double y=ya*((double)j)+yb;
double zm = segmesh.findMinZToolDepthQuick(x, y, null);
points[i*height+j][0] = x;
points[i*height+j][1] = y;
points[i*height+j][2] = zm;
map[i][j] = zm;
pointmap[i][j][0] = x;
pointmap[i][j][1] = y;
pointmap[i][j][2] = zm; }
if(pl!=null) {
percent = (int)(100*((double)i/(double)width));
pl.updateProgress(percent); } }
if(pl!=null) {
pl.updateProgress(100); } }
public double getDepth(int i, int j) {
return map[i][j]; }
public double[][] getPoints() {
return points; }
public double[][] getMap() {
return map; } }