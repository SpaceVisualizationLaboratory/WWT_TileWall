import processing.core.*; 
import processing.xml.*; 

import touchatag.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class touchatagWWT extends PApplet {



Touchatag rfid;
PFont font;
ArrayList targets;
// Defines the maximum number of touchatag
// readers that might be connected to the computer 
int numOfReaders = 3;

// This library affords up to three touchatag
// tags on each of the touchatag readers
String[]tags = new String[3];
XMLElement xml;
Boolean tagsPresent =false;
Boolean tagsWerePresent =false;
String currentTarget ="";
float baseTime;
float displayTime=100000.f;
String baseURL="192.168.130.234:5050";
//String baseURL="192.168.1.75:5050";

public void setup() {
  
  // Optinally, if only one touchatag reader will
  rfid = new Touchatag(this); 
  //rfid = new Touchatag(this, numOfReaders);
  targets=new ArrayList();  
  font = loadFont("AvenirNextLTPro-Bold-48.vlw");
  textFont(font);
  
  size(600, 140);
  
  rectMode(CENTER);
  textAlign(CENTER,CENTER);
  stroke(0);
  fill(125);
  xml = new XMLElement(this, "tags.xml");
  int numSites = xml.getChildCount();
  println(numSites);
  for (int i = 0; i < numSites; i++) {
    XMLElement kid = xml.getChild(i);
    String objectName = kid.getContent(); 
    String RFID=kid.getStringAttribute("RFID");
    float RA=kid.getFloatAttribute("RA");
    float dec=kid.getFloatAttribute("dec");
    float rot=kid.getFloatAttribute("rot",0.0f);
    float zoom=kid.getFloatAttribute("zoom",1.0f);
    targets.add(new WWTObj(RFID,objectName,RA,dec,zoom,rot));
  }
  baseTime=frameCount;
}

public void draw() {
   background(0);
   
   // Gets the number of touchatag readers connected
   int numReaders = rfid.On();
   
   if (numReaders != 0) {
     // Gets the tags for each of the touchatag readers
     //for (int i = 0; i < numReaders; i++) {
       tags = rfid.tagsOnReader(0);
       tagsPresent = tags.length>0;
       if (tagsPresent && !tagsWerePresent) {
         println("new tag");
         for (int i=0; i < targets.size(); i++) {
           WWTObj target = (WWTObj) targets.get(i);
           if (target.RFID.equals(tags[0])) {
             println("match " + target.RFID);
             //text(target.title,width/2,height/2);
             currentTarget=target.title;
             String urlRequest="http://"+baseURL+"/layerApi.aspx?cmd=mode&lookat=Sky&flyto="+target.dec+","+target.RA+","+target.zoom+","+target.rot+",0";
             loadStrings(urlRequest);
             println(urlRequest);
             baseTime=millis();
           } else println("no match "+tags[0]+" "+target.RFID+" "+i);
         }
       }
       if ((millis()-baseTime) < displayTime) {
         fill(lerpColor(255,0,(millis()-baseTime)/displayTime));
         text(currentTarget,width/2,height/2);
         println(millis());println(baseTime);
       }
       tagsWerePresent=tagsPresent;
    // } 
   }
}

// Draws a small square for each of
// the touchatag readers connected
public void drawReaders(int num) {
 // for (int i = 1; i <= num; i++) {
 //   rect(i * 200, 100, 50, 50);
 // }
}

// Writes the list of tag IDs 
// present on a touchatag reader
public void drawTags(String[] tagList) {
  if (tagList.length > 0)    text(tagList[0],  175,  150);
  text (PApplet.parseInt(tagsPresent),10,10);
}

class WWTObj {
  String RFID;
  String title;
  float RA;
  float dec;
  float zoom;
  float rot;
  
  WWTObj(String ID, String obj, float ra, float d,float z,float r) {
    RFID=ID;
    title=obj;
    RA=ra;
    dec=d;
    zoom=z;
    rot=r;
    //distance=Odist;
    //size=Osize;
  } 
  public void flyto(){
  }
  public boolean matchID(){
    return false;
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "touchatagWWT" });
  }
}
