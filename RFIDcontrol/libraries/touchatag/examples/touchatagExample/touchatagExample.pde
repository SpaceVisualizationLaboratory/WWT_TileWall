import touchatag.*;

Touchatag rfid;
PFont font;

// Defines the maximum number of touchatag
// readers that might be connected to the computer 
int numOfReaders = 3;

// This library affords up to three touchatag
// tags on each of the touchatag readers
String[][] tags = new String[numOfReaders][3];

void setup() {
  
  // Optinally, if only one touchatag reader will
  // be used: rfid = new Touchatag(this); 
  rfid = new Touchatag(this, numOfReaders);
    
  font = loadFont("Verdana-10.vlw");
  textFont(font);
  
  size(800, 400);
  
  rectMode(CENTER);
  stroke(0);
  fill(125);
}

void draw() {
   background(255);
   
   // Gets the number of touchatag readers connected
   int numReaders = rfid.On();
   
   if (numReaders != 0) {
     drawReaders(numReaders);
   
     // Gets the tags for each of the touchatag readers
     for (int i = 0; i < numReaders; i++) {
       tags[i] = rfid.tagsOnReader(i);
       drawTags(tags[i], i);
     }  
   }
}

// Draws a small square for each of
// the touchatag readers connected
void drawReaders(int num) {
  for (int i = 1; i <= num; i++) {
    rect(i * 200, 100, 50, 50);
  }
}

// Writes the list of tag IDs 
// present on a touchatag reader
void drawTags(String[] tagList, int pos) {
  for (int i = 0; i < tagList.length; i++) {
    text(tagList[i], (pos + 1) * 175, (i * 10) + 150);
  }
}
