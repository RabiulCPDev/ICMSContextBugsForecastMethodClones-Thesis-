
﻿using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Text;
namespace Exif {
public sealed class ExifTagCollection : IEnumerable<ExifTag> {
private Dictionary<ID, ExifTag> _tags;
#region Constructors
public ExifTagCollection(string fileName)
: this(fileName, true, false) { }
public ExifTagCollection(string fileName, bool useEmbeddedColorManagement, bool validateImageData) {
using (FileStream stream = new FileStream(fileName, FileMode.Open, FileAccess.Read)) {
Image image = System.Drawing.Image.FromStream(stream,
useEmbeddedColorManagement,
validateImageData);
ReadTags(image.PropertyItems); } }
public ExifTagCollection(Image image) {
ReadTags(image.PropertyItems); }
#endregion
#region Private Methods
private void ReadTags(PropertyItem[] pitems) {
Encoding ascii = Encoding.ASCII;
SupportedTags supportedTags = new SupportedTags();
_tags = new Dictionary<ID, ExifTag>();
foreach (PropertyItem pitem in pitems) {
ExifTag tag = (ExifTag) supportedTags[(ID)pitem.Id];
if (tag == null) continue;
string value = "";
if (pitem.Type == 0x1) {
#region BYTE (8-bit unsigned int)
if (pitem.Value.Length == 4)
value = "Version " + pitem.Value[0].ToString() + "." + pitem.Value[1].ToString();
else if (pitem.Id == 0x5 && pitem.Value[0] == 0)
value = "Sea level";
else
value = pitem.Value[0].ToString();
#endregion }
else if (pitem.Type == 0x2) {
#region ASCII (8 bit ASCII code)
value = ascii.GetString(pitem.Value).Trim('\0');
if (pitem.Id == 0x1 || pitem.Id == 0x13)
if (value == "N") value = "North latitude";
else if (value == "S") value = "South latitude";
else value = "reserved";
if (pitem.Id == 0x3 || pitem.Id == 0x15)
if (value == "E") value = "East longitude";
else if (value == "W") value = "West longitude";
else value = "reserved";
if (pitem.Id == 0x9)
if (value == "A") value = "Measurement in progress";
else if (value == "V") value = "Measurement Interoperability";
else value = "reserved";
if (pitem.Id == 0xA)
if (value == "2") value = "2-dimensional measurement";
else if (value == "3") value = "3-dimensional measurement";
else value = "reserved";
if (pitem.Id == 0xC || pitem.Id == 0x19)
if (value == "K") value = "Kilometers per hour";
else if (value == "M") value = "Miles per hour";
else if (value == "N") value = "Knots";
else value = "reserved";
if (pitem.Id == 0xE || pitem.Id == 0x10 || pitem.Id == 0x17)
if (value == "T") value = "True direction";
else if (value == "M") value = "Magnetic direction";
else value = "reserved";
#endregion }
else if (pitem.Type == 0x3) {
#region 3 = SHORT (16-bit unsigned int)
UInt16 uintval = BitConverter.ToUInt16(pitem.Value, 0);
// orientation // lookup table
switch (pitem.Id) {
case 0x8827: // ISO speed rating
value = "ISO-" + uintval.ToString();
break;
case 0xA217: // sensing method {
switch (uintval) {
case 1: value = "Not defined"; break;
case 2: value = "One-chip color area sensor"; break;
case 3: value = "Two-chip color area sensor"; break;
case 4: value = "Three-chip color area sensor"; break;
case 5: value = "Color sequential area sensor"; break;
case 7: value = "Trilinear sensor"; break;
case 8: value = "Color sequential linear sensor"; break;
default: value = " reserved"; break; } }
break;
case 0x8822: // Exposure program
switch (uintval) {
case 0: value = "Not defined"; break;
case 1: value = "Manual"; break;
case 2: value = "Normal program"; break;
case 3: value = "Aperture priority"; break;
case 4: value = "Shutter priority"; break;
case 5: value = "Creative program (biased toward depth of field)"; break;
case 6: value = "Action program (biased toward fast shutter speed)"; break;
case 7: value = "Portrait mode (for closeup photos with the background out of focus)"; break;
case 8: value = "Landscape mode (for landscape photos with the background in focus)"; break;
default: value = "reserved"; break; }
break;
case 0x9207: // metering mode
switch (uintval) {
case 0: value = "unknown"; break;
case 1: value = "Average"; break;
case 2: value = "Center Weighted Average"; break;
case 3: value = "Spot"; break;
case 4: value = "MultiSpot"; break;
case 5: value = "Pattern"; break;
case 6: value = "Partial"; break;
case 255: value = "Other"; break;
default: value = "reserved"; break; }
break;
case 0x9208: // Light source {
switch (uintval) {
case 0: value = "unknown"; break;
case 1: value = "Daylight"; break;
case 2: value = "Fluorescent"; break;
case 3: value = "Tungsten (incandescent light)"; break;
case 4: value = "Flash"; break;
case 9: value = "Fine weather"; break;
case 10: value = "Cloudy weather"; break;
case 11: value = "Shade"; break;
case 12: value = "Daylight fluorescent (D 5700 – 7100K)"; break;
case 13: value = "Day white fluorescent (N 4600 – 5400K)"; break;
case 14: value = "Cool white fluorescent (W 3900 – 4500K)"; break;
case 15: value = "White fluorescent (WW 3200 – 3700K)"; break;
case 17: value = "Standard light A"; break;
case 18: value = "Standard light B"; break;
case 19: value = "Standard light C"; break;
case 20: value = "D55"; break;
case 21: value = "D65"; break;
case 22: value = "D75"; break;
case 23: value = "D50"; break;
case 24: value = "ISO studio tungsten"; break;
case 255: value = "ISO studio tungsten"; break;
default: value = "other light source"; break; } }
break;
case 0x9209: // Flash {
switch (uintval) {
case 0x0: value = "Flash did not fire"; break;
case 0x1: value = "Flash fired"; break;
case 0x5: value = "Strobe return light not detected"; break;
case 0x7: value = "Strobe return light detected"; break;
case 0x9: value = "Flash fired, compulsory flash mode"; break;
case 0xD: value = "Flash fired, compulsory flash mode, return light not detected"; break;
case 0xF: value = "Flash fired, compulsory flash mode, return light detected"; break;
case 0x10: value = "Flash did not fire, compulsory flash mode"; break;
case 0x18: value = "Flash did not fire, auto mode"; break;
case 0x19: value = "Flash fired, auto mode"; break;
case 0x1D: value = "Flash fired, auto mode, return light not detected"; break;
case 0x1F: value = "Flash fired, auto mode, return light detected"; break;
case 0x20: value = "No flash function"; break;
case 0x41: value = "Flash fired, red-eye reduction mode"; break;
case 0x45: value = "Flash fired, red-eye reduction mode, return light not detected"; break;
case 0x47: value = "Flash fired, red-eye reduction mode, return light detected"; break;
case 0x49: value = "Flash fired, compulsory flash mode, red-eye reduction mode"; break;
case 0x4D: value = "Flash fired, compulsory flash mode, red-eye reduction mode, return light not detected"; break;
case 0x4F: value = "Flash fired, compulsory flash mode, red-eye reduction mode, return light detected"; break;
case 0x59: value = "Flash fired, auto mode, red-eye reduction mode"; break;
case 0x5D: value = "Flash fired, auto mode, return light not detected, red-eye reduction mode"; break;
case 0x5F: value = "Flash fired, auto mode, return light detected, red-eye reduction mode"; break;
default: value = "reserved"; break; } }
break;
case 0x0128: //ResolutionUnit {
switch (uintval) {
case 2: value = "Inch"; break;
case 3: value = "Centimeter"; break;
default: value = "No Unit"; break; } }
break;
case 0xA409: // Saturation {
switch (uintval) {
case 0: value = "Normal"; break;
case 1: value = "Low saturation"; break;
case 2: value = "High saturation"; break;
default: value = "Reserved"; break; } }
break;
case 0xA40A: // Sharpness {
switch (uintval) {
case 0: value = "Normal"; break;
case 1: value = "Soft"; break;
case 2: value = "Hard"; break;
default: value = "Reserved"; break; } }
break;
case 0xA408: // Contrast {
switch (uintval) {
case 0: value = "Normal"; break;
case 1: value = "Soft"; break;
case 2: value = "Hard"; break;
default: value = "Reserved"; break; } }
break;
case 0x103: // Compression {
switch (uintval) {
case 1: value = "Uncompressed"; break;
case 6: value = "JPEG compression (thumbnails only)"; break;
default: value = "Reserved"; break; } }
break;
case 0x106: // PhotometricInterpretation {
switch (uintval) {
case 2: value = "RGB"; break;
case 6: value = "YCbCr"; break;
default: value = "Reserved"; break; } }
break;
case 0x112: // Orientation {
switch (uintval) {
case 1: value = "The 0th row is at the visual top of the image, and the 0th column is the visual left-hand side."; break;
case 2: value = "The 0th row is at the visual top of the image, and the 0th column is the visual right-hand side."; break;
case 3: value = "The 0th row is at the visual bottom of the image, and the 0th column is the visual right-hand side."; break;
case 4: value = "The 0th row is at the visual bottom of the image, and the 0th column is the visual left-hand side."; break;
case 5: value = "The 0th row is the visual left-hand side of the image, and the 0th column is the visual top."; break;
case 6: value = "The 0th row is the visual right-hand side of the image, and the 0th column is the visual top."; break;
case 7: value = "The 0th row is the visual right-hand side of the image, and the 0th column is the visual bottom."; break;
case 8: value = "The 0th row is the visual left-hand side of the image, and the 0th column is the visual bottom."; break;
default: value = "Reserved"; break; } }
break;
case 0x213: // YCbCrPositioning {
switch (uintval) {
case 1: value = "centered"; break;
case 6: value = "co-sited"; break;
default: value = "Reserved"; break; } }
break;
case 0xA001: // ColorSpace {
switch (uintval) {
case 1: value = "sRGB"; break;
case 0xFFFF: value = "Uncalibrated"; break;
default: value = "Reserved"; break; } }
break;
case 0xA401: // CustomRendered {
switch (uintval) {
case 0: value = "Normal process"; break;
case 1: value = "Custom process"; break;
default: value = "Reserved"; break; } }
break;
case 0xA402: // ExposureMode {
switch (uintval) {
case 0: value = "Auto exposure"; break;
case 1: value = "Manual exposure"; break;
case 2: value = "Auto bracket"; break;
default: value = "Reserved"; break; } }
break;
case 0xA403: // WhiteBalance {
switch (uintval) {
case 0: value = "Auto white balance"; break;
case 1: value = "Manual white balance"; break;
default: value = "Reserved"; break; } }
break;
case 0xA406: // SceneCaptureType {
switch (uintval) {
case 0: value = "Standard"; break;
case 1: value = "Landscape"; break;
case 2: value = "Portrait"; break;
case 3: value = "Night scene"; break;
default: value = "Reserved"; break; } }
break;
case 0xA40C: // SubjectDistanceRange {
switch (uintval) {
case 0: value = "unknown"; break;
case 1: value = "Macro"; break;
case 2: value = "Close view"; break;
case 3: value = "Distant view"; break;
default: value = "Reserved"; break; } }
break;
case 0x1E: // GPSDifferential {
switch (uintval) {
case 0: value = "Measurement without differential correction"; break;
case 1: value = "Differential correction applied"; break;
default: value = "Reserved"; break; } }
break;
case 0xA405: // FocalLengthIn35mmFilm
value = uintval.ToString() + " mm";
break;
default://
value = uintval.ToString();
break; }
#endregion }
else if (pitem.Type == 0x4) {
#region 4 = LONG (32-bit unsigned int)
value = BitConverter.ToUInt32(pitem.Value, 0).ToString();
#endregion }
else if (pitem.Type == 0x5) {
#region 5 = RATIONAL (Two LONGs, unsigned)
URational rat = new URational(pitem.Value);
switch (pitem.Id) {
case 0x9202: // ApertureValue
value = Math.Round(Math.Pow(Math.Sqrt(2), rat.ToDouble()), 2).ToString();
break;
case 0x9205: // MaxApertureValue
value = Math.Round(Math.Pow(Math.Sqrt(2), rat.ToDouble()), 2).ToString();
break;
case 0x920A: // FocalLength
value = rat.ToDouble().ToString();
break;
case 0x829D: // F-number
value = rat.ToDouble().ToString();
break;
case 0x11A: // Xresolution
value = rat.ToDouble().ToString();
break;
case 0x11B: // Yresolution
value = rat.ToDouble().ToString();
break;
case 0x829A: // ExposureTime
value = rat.ToString();
// special case: e.g. 10/10 = 1"
string[] array = value.Split('/');
if (array.Length >= 2 && array[0].Equals(array[1])) {
value = "1\""; }
break;
case 0x2: // GPSLatitude
value = new GPSRational(pitem.Value).ToString();
break;
case 0x4: // GPSLongitude
value = new GPSRational(pitem.Value).ToString();
break;
case 0x6: // GPSAltitude
value = rat.ToDouble() + " meters";
break;
case 0xA404: // Digital Zoom Ratio
value = rat.ToDouble().ToString();
if (value == "0") value = "none";
break;
case 0xB: // GPSDOP
value = rat.ToDouble().ToString();
break;
case 0xD: // GPSSpeed
value = rat.ToDouble().ToString();
break;
case 0xF: // GPSTrack
value = rat.ToDouble().ToString();
break;
case 0x11: // GPSImgDir
value = rat.ToDouble().ToString();
break;
case 0x14: // GPSDestLatitude
value = new GPSRational(pitem.Value).ToString();
break;
case 0x16: // GPSDestLongitude
value = new GPSRational(pitem.Value).ToString();
break;
case 0x18: // GPSDestBearing
value = rat.ToDouble().ToString();
break;
case 0x1A: // GPSDestDistance
value = rat.ToDouble().ToString();
break;
case 0x7: // GPSTimeStamp
value = new GPSRational(pitem.Value).ToString(":");
break;
default:
value = rat.ToString();
break; }
#endregion }
else if (pitem.Type == 0x7) {
#region UNDEFINED (8-bit)
switch (pitem.Id) {
case 0xA300: //FileSource {
if (pitem.Value[0] == 3)
value = "DSC";
else
value = "reserved";
break; }
case 0xA301: //SceneType
if (pitem.Value[0] == 1)
value = "A directly photographed image";
else
value = "reserved";
break;
case 0x9000:// Exif Version
value = ascii.GetString(pitem.Value).Trim('\0');
break;
case 0xA000: // Flashpix Version
value = ascii.GetString(pitem.Value).Trim('\0');
if (value == "0100")
value = "Flashpix Format Version 1.0";
else value = "reserved";
break;
case 0x9101: //ComponentsConfiguration
value = GetComponentsConfig(pitem.Value);
break;
case 0x927C: //MakerNote
value = ascii.GetString(pitem.Value).Trim('\0');
break;
case 0x9286: //UserComment
value = ascii.GetString(pitem.Value).Trim('\0');
break;
case 0x1B: //GPS Processing Method
value = ascii.GetString(pitem.Value).Trim('\0');
break;
case 0x1C: //GPS Area Info
value = ascii.GetString(pitem.Value).Trim('\0');
break;
default:
value = "-";
break; }
#endregion }
else if (pitem.Type == 0x9) {
#region 9 = SLONG (32-bit int)
value = BitConverter.ToInt32(pitem.Value, 0).ToString();
#endregion }
else if (pitem.Type == 0xA) {
#region 10 = SRATIONAL (Two SLONGs, signed)
Rational rat = new Rational(pitem.Value);
switch (pitem.Id) {
case 0x9201: // ShutterSpeedValue
value = "1/" + Math.Round(Math.Pow(2, rat.ToDouble()), 2).ToString();
break;
case 0x9203: // BrightnessValue
value = Math.Round(rat.ToDouble(), 4).ToString();
break;
case 0x9204: // ExposureBiasValue
value = Math.Round(rat.ToDouble(), 2).ToString() + " eV";
break;
default:
value = rat.ToString();
break; }
#endregion }
tag.Value = value;
_tags.Add(tag.Id, tag); } }
private static string GetComponentsConfig(byte[] bytes) {
string s = "";
string[] vals = new string[] { "", "Y", "Cb", "Cr", "R", "G", "B" };
foreach (byte b in bytes)
s += vals[b];
return s; }
#endregion
#region IEnumerable<ExifTag> Members
public IEnumerator<ExifTag> GetEnumerator() {
return _tags.Values.GetEnumerator(); }
#endregion
#region IEnumerable Members
IEnumerator IEnumerable.GetEnumerator() {
return _tags.Values.GetEnumerator(); }
#endregion
#region Indexers
public ExifTag this[ID id] {
get {
return _tags[id]; } }
#endregion }
public enum ID {
IMAGEWIDTH                  = 0x100,
IMAGEHEIGHT                 = 0x101,
GPSVERSIONID                = 0x0,
GPSALTITUDEREF              = 0x5,
STRIPOFFSETS                = 0x111,
ROWSPERSTRIP                = 0x116,
STRIPBYTECOUNTS             = 0x117,
PIXELXDIMENSION             = 0xA002,
PIXELYDIMENSION             = 0xA003,
BITSPERSAMPLE               = 0x102,
COMPRESSION                 = 0x103,
PHOTOMETRICINTERPRETATION   = 0x106,
ORIENTATION                 = 0x112,
SAMPLESPERPIXEL             = 0x115,
PLANARCONFIGURATION         = 0x11C,
YCBCRSUBSAMPLING            = 0x212,
YCBCRPOSITIONING            = 0x213,
RESOLUTIONUNIT              = 0x128,
TRANSFERFUNCTION            = 0x12D,
COLORSPACE                  = 0xA001,
EXPOSUREPROGRAM             = 0x8822,
ISOSPEEDRATINGS             = 0x8827,
METERINGMODE                = 0x9207,
LIGHTSOURCE                 = 0x9208,
FLASH                       = 0x9209,
SUBJECTAREA                 = 0x9214,
FOCALPLANERESOLUTIONUNIT    = 0xA210,
SUBJECTLOCATION             = 0xA214,
SENSINGMETHOD               = 0xA217,
CUSTOMRENDERED              = 0xA401,
EXPOSUREMODE                = 0xA402,
WHITEBALANCE                = 0xA403,
FOCALLENGTHIN35MMFILM       = 0xA405,
SCENECAPTURETYPE            = 0xA406,
CONTRAST                    = 0xA408,
SATURATION                  = 0xA409,
SHARPNESS                   = 0xA40A,
SUBJECTDISTANCERANGE        = 0xA40C,
GPSDIFFERENTIAL             = 0x1E,
SHUTTERSPEEDVALUE           = 0x9201,
BRIGHTNESSVALUE             = 0x9203,
EXPOSUREBIASVALUE           = 0x9204,
JPEGINTERCHANGEFORMAT       = 0x201,
JPEGINTERCHANGEFORMATLENGTH = 0x202,
XRESOLUTION                 = 0x11A,
YRESOLUTION                 = 0x11B,
WHITEPOINT                  = 0x13E,
PRIMARYCHROMATICITIES       = 0x13F,
YCBCRCOEFFICIENTS           = 0x211,
REFERENCEBLACKWHITE         = 0x214,
COMPRESSEDBITSPERPIXEL      = 0x9102,
EXPOSURETIME                = 0x829A,
FNUMBER                     = 0x829D,
APERTUREVALUE               = 0x9202,
MAXAPERTUREVALUE            = 0x9205,
SUBJECTDISTANCE             = 0x9206,
FOCALLENGTH                 = 0x920A,
FLASHENERGY                 = 0xA20B,
FOCALPLANEXRESOLUTION       = 0xA20E,
FOCALPLANEYRESOLUTION       = 0xA20F,
EXPOSUREINDEX               = 0xA215,
DIGITALZOOMRATIO            = 0xA404,
GAINCONTROL                 = 0xA407,
GPSLATITUDE                 = 0x2,
GPSLONGITUDE                = 0x4,
GPSALTITUDE                 = 0x6,
GPSTIMESTAMP                = 0x7,
GPSDOP                      = 0xB,
GPSSPEED                    = 0xD,
GPSTRACK                    = 0xF,
GPSIMGDIRECTION             = 0x11,
GPSDESTLATITUDE             = 0x14,
GPSDESTLONGITUDE            = 0x16,
GPSDESTBEARING              = 0x18,
GPSDESTDISTANCE             = 0x1A,
DATETIME                    = 0x132,
IMAGEDESCRIPTION            = 0x10E,
MAKE                        = 0x10F,
MODEL                       = 0x110,
SOFTWARE                    = 0x131,
ARTIST                      = 0x13B,
COPYRIGHT                   = 0x8298,
RELATEDSOUNDFILE            = 0xA004,
DATETIMEORIGINAL            = 0x9003,
DATETIMEDIGITIZED           = 0x9004,
SUBSECTIME                  = 0x9290,
SUBSECTIMEORIGINAL          = 0x9291,
SUBSECTIMEDIGITIZED         = 0x9292,
IMAGEUNIQUEID               = 0xA420,
SPECTRALSENSITIVITY         = 0x8824,
GPSLATITUDEREF              = 0x1,
GPSLONGITUDEREF             = 0x3,
GPSSATELLITES               = 0x8,
GPSSTATUS                   = 0x9,
GPSMEASUREMODE              = 0xA,
GPSSPEEDREF                 = 0xC,
GPSTRACKREF                 = 0xE,
GPSIMGDIRECTIONREF          = 0x10,
GPSMAPDATUM                 = 0x12,
GPSDESTLATITUDEREF          = 0x13,
GPSDESTLONGITUDEREF         = 0x15,
GPSDESTBEARINGREF           = 0x17,
GPSDESTDISTANCEREF          = 0x19,
GPSDATESTAMP                = 0x1D,
OECF                        = 0x8828,
SPATIALFREQUENCYRESPONSE    = 0xA20C,
FILESOURCE                  = 0xA300,
SCENETYPE                   = 0xA301,
CFAPATTERN                  = 0xA302,
DEVICESETTINGDESCRIPTION    = 0xA40B,
EXIFVERSION                 = 0x9000,
FLASHPIXVERSION             = 0xA000,
COMPONENTSCONFIGURATION     = 0x9101,
MAKERNOTE                   = 0x927C,
USERCOMMENT                 = 0x9286,
GPSPROCESSINGMETHOD         = 0x1B,
GPSAREAINFORMATION          = 0x1C
};
internal sealed class SupportedTags : Hashtable {
public SupportedTags() {
this.Add(ID.IMAGEWIDTH, new ExifTag(ID.IMAGEWIDTH, "ImageWidth", "Image width"));
this.Add(ID.IMAGEHEIGHT, new ExifTag(ID.IMAGEHEIGHT, "ImageHeight", "Image height"));
this.Add(ID.GPSVERSIONID, new ExifTag(ID.GPSVERSIONID, "GPSVersionID", "GPS tag version"));
this.Add(ID.GPSALTITUDEREF, new ExifTag(ID.GPSALTITUDEREF, "GPSAltitudeRef", "Altitude reference"));
this.Add(ID.STRIPOFFSETS, new ExifTag(ID.STRIPOFFSETS, "StripOffsets", "Image data location"));
this.Add(ID.ROWSPERSTRIP, new ExifTag(ID.ROWSPERSTRIP, "RowsPerStrip", "Number of rows per strip"));
this.Add(ID.STRIPBYTECOUNTS, new ExifTag(ID.STRIPBYTECOUNTS, "StripByteCounts", "Bytes per compressed strip"));
this.Add(ID.PIXELXDIMENSION, new ExifTag(ID.PIXELXDIMENSION, "PixelXDimension", "Valid image width"));
this.Add(ID.PIXELYDIMENSION, new ExifTag(ID.PIXELYDIMENSION, "PixelYDimension", "Valid image height"));
this.Add(ID.BITSPERSAMPLE, new ExifTag(ID.BITSPERSAMPLE, "BitsPerSample", "Number of bits per component"));
this.Add(ID.COMPRESSION, new ExifTag(ID.COMPRESSION, "Compression", "Compression scheme"));
this.Add(ID.PHOTOMETRICINTERPRETATION, new ExifTag(ID.PHOTOMETRICINTERPRETATION, "PhotometricInterpretation", "Pixel composition"));
this.Add(ID.ORIENTATION, new ExifTag(ID.ORIENTATION, "Orientation", "Orientation of image"));
this.Add(ID.SAMPLESPERPIXEL, new ExifTag(ID.SAMPLESPERPIXEL, "SamplesPerPixel", "Number of components"));
this.Add(ID.PLANARCONFIGURATION, new ExifTag(ID.PLANARCONFIGURATION, "PlanarConfiguration", "Image data arrangement"));
this.Add(ID.YCBCRSUBSAMPLING, new ExifTag(ID.YCBCRSUBSAMPLING, "YCbCrSubSampling", "Subsampling ratio of Y to C"));
this.Add(ID.YCBCRPOSITIONING, new ExifTag(ID.YCBCRPOSITIONING, "YCbCrPositioning", "Y and C positioning"));
this.Add(ID.RESOLUTIONUNIT, new ExifTag(ID.RESOLUTIONUNIT, "ResolutionUnit", "Unit of X and Y resolution"));
this.Add(ID.TRANSFERFUNCTION, new ExifTag(ID.TRANSFERFUNCTION, "TransferFunction", "Transfer function"));
this.Add(ID.COLORSPACE, new ExifTag(ID.COLORSPACE, "ColorSpace", "Color space information"));
this.Add(ID.EXPOSUREPROGRAM, new ExifTag(ID.EXPOSUREPROGRAM, "ExposureProgram", "Exposure program"));
this.Add(ID.ISOSPEEDRATINGS, new ExifTag(ID.ISOSPEEDRATINGS, "ISOSpeedRatings", "ISO speed rating"));
this.Add(ID.METERINGMODE, new ExifTag(ID.METERINGMODE, "MeteringMode", "Metering mode"));
this.Add(ID.LIGHTSOURCE, new ExifTag(ID.LIGHTSOURCE, "LightSource", "Light source"));
this.Add(ID.FLASH, new ExifTag(ID.FLASH, "Flash", "Flash"));
this.Add(ID.SUBJECTAREA, new ExifTag(ID.SUBJECTAREA, "SubjectArea", "Subject area"));
this.Add(ID.FOCALPLANERESOLUTIONUNIT, new ExifTag(ID.FOCALPLANERESOLUTIONUNIT, "FocalPlaneResolutionUnit", "Focal plane resolution unit"));
this.Add(ID.SUBJECTLOCATION, new ExifTag(ID.SUBJECTLOCATION, "SubjectLocation", "Subject location"));
this.Add(ID.SENSINGMETHOD, new ExifTag(ID.SENSINGMETHOD, "SensingMethod", "Sensing method"));
this.Add(ID.CUSTOMRENDERED, new ExifTag(ID.CUSTOMRENDERED, "CustomRendered", "Custom image processing"));
this.Add(ID.EXPOSUREMODE, new ExifTag(ID.EXPOSUREMODE, "ExposureMode", "Exposure mode"));
this.Add(ID.WHITEBALANCE, new ExifTag(ID.WHITEBALANCE, "WhiteBalance", "White balance"));
this.Add(ID.FOCALLENGTHIN35MMFILM, new ExifTag(ID.FOCALLENGTHIN35MMFILM, "FocalLengthIn35mmFilm", "Focal length in 35 mm film"));
this.Add(ID.SCENECAPTURETYPE, new ExifTag(ID.SCENECAPTURETYPE, "SceneCaptureType", "Scene capture type"));
this.Add(ID.CONTRAST, new ExifTag(ID.CONTRAST, "Contrast", "Contrast"));
this.Add(ID.SATURATION, new ExifTag(ID.SATURATION, "Saturation", "Saturation"));
this.Add(ID.SHARPNESS, new ExifTag(ID.SHARPNESS, "Sharpness", "Sharpness"));
this.Add(ID.SUBJECTDISTANCERANGE, new ExifTag(ID.SUBJECTDISTANCERANGE, "SubjectDistanceRange", "Subject distance range"));
this.Add(ID.GPSDIFFERENTIAL, new ExifTag(ID.GPSDIFFERENTIAL, "GPSDifferential", "GPS differential correction"));
this.Add(ID.SHUTTERSPEEDVALUE, new ExifTag(ID.SHUTTERSPEEDVALUE, "ShutterSpeedValue", "Shutter speed"));
this.Add(ID.BRIGHTNESSVALUE, new ExifTag(ID.BRIGHTNESSVALUE, "BrightnessValue", "Brightness"));
this.Add(ID.EXPOSUREBIASVALUE, new ExifTag(ID.EXPOSUREBIASVALUE, "ExposureBiasValue", "Exposure bias"));
this.Add(ID.JPEGINTERCHANGEFORMAT, new ExifTag(ID.JPEGINTERCHANGEFORMAT, "JPEGInterchangeFormat", "Offset to JPEG SOI"));
this.Add(ID.JPEGINTERCHANGEFORMATLENGTH, new ExifTag(ID.JPEGINTERCHANGEFORMATLENGTH, "JPEGInterchangeFormatLength", "Bytes of JPEG data"));
this.Add(ID.XRESOLUTION, new ExifTag(ID.XRESOLUTION, "XResolution", "Image resolution in width direction"));
this.Add(ID.YRESOLUTION, new ExifTag(ID.YRESOLUTION, "YResolution", "Image resolution in height direction"));
this.Add(ID.WHITEPOINT, new ExifTag(ID.WHITEPOINT, "WhitePoint", "White point chromaticity"));
this.Add(ID.PRIMARYCHROMATICITIES, new ExifTag(ID.PRIMARYCHROMATICITIES, "PrimaryChromaticities", "Chromaticities of primaries"));
this.Add(ID.YCBCRCOEFFICIENTS, new ExifTag(ID.YCBCRCOEFFICIENTS, "YCbCrCoefficients", "Color space transformation matrix coefficients"));
this.Add(ID.REFERENCEBLACKWHITE, new ExifTag(ID.REFERENCEBLACKWHITE, "ReferenceBlackWhite", "Pair of black and white reference values"));
this.Add(ID.COMPRESSEDBITSPERPIXEL, new ExifTag(ID.COMPRESSEDBITSPERPIXEL, "CompressedBitsPerPixel", "Image compression mode"));
this.Add(ID.EXPOSURETIME, new ExifTag(ID.EXPOSURETIME, "ExposureTime", "Exposure time"));
this.Add(ID.FNUMBER, new ExifTag(ID.FNUMBER, "FNumber", "F number"));
this.Add(ID.APERTUREVALUE, new ExifTag(ID.APERTUREVALUE, "ApertureValue", "Aperture"));
this.Add(ID.MAXAPERTUREVALUE, new ExifTag(ID.MAXAPERTUREVALUE, "MaxApertureValue", "Maximum lens aperture"));
this.Add(ID.SUBJECTDISTANCE, new ExifTag(ID.SUBJECTDISTANCE, "SubjectDistance", "Subject distance"));
this.Add(ID.FOCALLENGTH, new ExifTag(ID.FOCALLENGTH, "FocalLength", "Lens focal length"));
this.Add(ID.FLASHENERGY, new ExifTag(ID.FLASHENERGY, "FlashEnergy", "Flash energy"));
this.Add(ID.FOCALPLANEXRESOLUTION, new ExifTag(ID.FOCALPLANEXRESOLUTION, "FocalPlaneXResolution", "Focal plane X resolution"));
this.Add(ID.FOCALPLANEYRESOLUTION, new ExifTag(ID.FOCALPLANEYRESOLUTION, "FocalPlaneYResolution", "Focal plane Y resolution"));
this.Add(ID.EXPOSUREINDEX, new ExifTag(ID.EXPOSUREINDEX, "ExposureIndex", "Exposure index"));
this.Add(ID.DIGITALZOOMRATIO, new ExifTag(ID.DIGITALZOOMRATIO, "DigitalZoomRatio", "Digital zoom ratio"));
this.Add(ID.GAINCONTROL, new ExifTag(ID.GAINCONTROL, "GainControl", "Gain control"));
this.Add(ID.GPSLATITUDE, new ExifTag(ID.GPSLATITUDE, "GPSLatitude", "Latitude"));
this.Add(ID.GPSLONGITUDE, new ExifTag(ID.GPSLONGITUDE, "GPSLongitude", "Longitude"));
this.Add(ID.GPSALTITUDE, new ExifTag(ID.GPSALTITUDE, "GPSAltitude", "Altitude"));
this.Add(ID.GPSTIMESTAMP, new ExifTag(ID.GPSTIMESTAMP, "GPSTimeStamp", "GPS time (atomic clock)"));
this.Add(ID.GPSDOP, new ExifTag(ID.GPSDOP, "GPSDOP", "Measurement precision"));
this.Add(ID.GPSSPEED, new ExifTag(ID.GPSSPEED, "GPSSpeed", "Speed of GPS receiver"));
this.Add(ID.GPSTRACK, new ExifTag(ID.GPSTRACK, "GPSTrack", "Direction of movement"));
this.Add(ID.GPSIMGDIRECTION, new ExifTag(ID.GPSIMGDIRECTION, "GPSImgDirection", "Direction of image"));
this.Add(ID.GPSDESTLATITUDE, new ExifTag(ID.GPSDESTLATITUDE, "GPSDestLatitude", "Latitude of destination"));
this.Add(ID.GPSDESTLONGITUDE, new ExifTag(ID.GPSDESTLONGITUDE, "GPSDestLongitude", "Longitude of destination"));
this.Add(ID.GPSDESTBEARING, new ExifTag(ID.GPSDESTBEARING, "GPSDestBearing", "Bearing of destination"));
this.Add(ID.GPSDESTDISTANCE, new ExifTag(ID.GPSDESTDISTANCE, "GPSDestDistance", "Distance to destination"));
this.Add(ID.DATETIME, new ExifTag(ID.DATETIME, "DateTime", "File change date and time"));
this.Add(ID.IMAGEDESCRIPTION, new ExifTag(ID.IMAGEDESCRIPTION, "ImageDescription", "Image title"));
this.Add(ID.MAKE, new ExifTag(ID.MAKE, "Make", "Image input equipment manufacturer"));
this.Add(ID.MODEL, new ExifTag(ID.MODEL, "Model", "Image input equipment model"));
this.Add(ID.SOFTWARE, new ExifTag(ID.SOFTWARE, "Software", "Software used"));
this.Add(ID.ARTIST, new ExifTag(ID.ARTIST, "Artist", "Person who created the image"));
this.Add(ID.COPYRIGHT, new ExifTag(ID.COPYRIGHT, "Copyright", "Copyright holder"));
this.Add(ID.RELATEDSOUNDFILE, new ExifTag(ID.RELATEDSOUNDFILE, "RelatedSoundFile", "Related audio file"));
this.Add(ID.DATETIMEORIGINAL, new ExifTag(ID.DATETIMEORIGINAL, "DateTimeOriginal", "Date and time of original data generation"));
this.Add(ID.DATETIMEDIGITIZED, new ExifTag(ID.DATETIMEDIGITIZED, "DateTimeDigitized", "Date and time of digital data generation"));
this.Add(ID.SUBSECTIME, new ExifTag(ID.SUBSECTIME, "SubSecTime", "DateTime subseconds"));
this.Add(ID.SUBSECTIMEORIGINAL, new ExifTag(ID.SUBSECTIMEORIGINAL, "SubSecTimeOriginal", "DateTimeOriginal subseconds"));
this.Add(ID.SUBSECTIMEDIGITIZED, new ExifTag(ID.SUBSECTIMEDIGITIZED, "SubSecTimeDigitized", "DateTimeDigitized subseconds"));
this.Add(ID.IMAGEUNIQUEID, new ExifTag(ID.IMAGEUNIQUEID, "ImageUniqueID", "Unique image ID"));
this.Add(ID.SPECTRALSENSITIVITY, new ExifTag(ID.SPECTRALSENSITIVITY, "SpectralSensitivity", "Spectral sensitivity"));
this.Add(ID.GPSLATITUDEREF, new ExifTag(ID.GPSLATITUDEREF, "GPSLatitudeRef", "North or South Latitude"));
this.Add(ID.GPSLONGITUDEREF, new ExifTag(ID.GPSLONGITUDEREF, "GPSLongitudeRef", "East or West Longitude"));
this.Add(ID.GPSSATELLITES, new ExifTag(ID.GPSSATELLITES, "GPSSatellites", "GPS satellites used for measurement"));
this.Add(ID.GPSSTATUS, new ExifTag(ID.GPSSTATUS, "GPSStatus", "GPS receiver status"));
this.Add(ID.GPSMEASUREMODE, new ExifTag(ID.GPSMEASUREMODE, "GPSMeasureMode", "GPS measurement mode"));
this.Add(ID.GPSSPEEDREF, new ExifTag(ID.GPSSPEEDREF, "GPSSpeedRef", "Speed unit"));
this.Add(ID.GPSTRACKREF, new ExifTag(ID.GPSTRACKREF, "GPSTrackRef", "Reference for direction of movement"));
this.Add(ID.GPSIMGDIRECTIONREF, new ExifTag(ID.GPSIMGDIRECTIONREF, "GPSImgDirectionRef", "Reference for direction of image"));
this.Add(ID.GPSMAPDATUM, new ExifTag(ID.GPSMAPDATUM, "GPSMapDatum", "Geodetic survey data used"));
this.Add(ID.GPSDESTLATITUDEREF, new ExifTag(ID.GPSDESTLATITUDEREF, "GPSDestLatitudeRef", "Reference for latitude of destination"));
this.Add(ID.GPSDESTLONGITUDEREF, new ExifTag(ID.GPSDESTLONGITUDEREF, "GPSDestLongitudeRef", "Reference for longitude of destination"));
this.Add(ID.GPSDESTBEARINGREF, new ExifTag(ID.GPSDESTBEARINGREF, "GPSDestBearingRef", "Reference for bearing of destination"));
this.Add(ID.GPSDESTDISTANCEREF, new ExifTag(ID.GPSDESTDISTANCEREF, "GPSDestDistanceRef", "Reference for distance to destination"));
this.Add(ID.GPSDATESTAMP, new ExifTag(ID.GPSDATESTAMP, "GPSDateStamp", "GPS date"));
this.Add(ID.OECF, new ExifTag(ID.OECF, "OECF", "Optoelectric conversion factor"));
this.Add(ID.SPATIALFREQUENCYRESPONSE, new ExifTag(ID.SPATIALFREQUENCYRESPONSE, "SpatialFrequencyResponse", "Spatial frequency response"));
this.Add(ID.FILESOURCE, new ExifTag(ID.FILESOURCE, "FileSource", "File source"));
this.Add(ID.SCENETYPE, new ExifTag(ID.SCENETYPE, "SceneType", "Scene type"));
this.Add(ID.CFAPATTERN, new ExifTag(ID.CFAPATTERN, "CFAPattern", "CFA pattern"));
this.Add(ID.DEVICESETTINGDESCRIPTION, new ExifTag(ID.DEVICESETTINGDESCRIPTION, "DeviceSettingDescription", "Device settings description"));
this.Add(ID.EXIFVERSION, new ExifTag(ID.EXIFVERSION, "ExifVersion", "Exif version"));
this.Add(ID.FLASHPIXVERSION, new ExifTag(ID.FLASHPIXVERSION, "FlashpixVersion", "Supported Flashpix version"));
this.Add(ID.COMPONENTSCONFIGURATION, new ExifTag(ID.COMPONENTSCONFIGURATION, "ComponentsConfiguration", "Meaning of each component"));
this.Add(ID.MAKERNOTE, new ExifTag(ID.MAKERNOTE, "MakerNote", "Manufacturer notes"));
this.Add(ID.USERCOMMENT, new ExifTag(ID.USERCOMMENT, "UserComment", "User comments"));
this.Add(ID.GPSPROCESSINGMETHOD, new ExifTag(ID.GPSPROCESSINGMETHOD, "GPSProcessingMethod", "Name of GPS processing method"));
this.Add(ID.GPSAREAINFORMATION, new ExifTag(ID.GPSAREAINFORMATION, "GPSAreaInformation", "Name of GPS area")); } } }