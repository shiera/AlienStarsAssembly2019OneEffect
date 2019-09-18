import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AliensFix extends PApplet {








/**
 * Bounce. 
 * 
 * When the shape hits the edge of the window, it reverses its direction. 
 */
 
int rad = 60;        // Width of the shape
float xpos, ypos;    // Starting position of shape    

float xspeed = 2.8f;  // Speed of the shape
float yspeed = 2.2f;  // Speed of the shape

int xdirection = 1;  // Left or Right
int ydirection = 1;  // Top to Bottom

int treeAmount = 2000;
float scale;
//--------------------------------
// alien code 
float alienPosX, alienPosY, alienSpeedX, alienSpeedY;
boolean beamMeUp = false;
int beamOniter = 42;
int beamOffIter = 102;
int iter = 0;
float alienSpeedboost = 0;
//--------------------------------
long startMillis;


//----------
//timing
float alienComes = 12;
float warp = 35.5f;
float end = 42;

Minim minim;

public void setup() {
  randomSeed(0);
  
  //size(640, 360);
  startMillis = millis();
  
  noCursor();
  noStroke();
  frameRate(60);
  ellipseMode(RADIUS);
  // Set the starting position of the shape
  xpos = width * 0.7f;
  ypos = height * 0.2f;
  rad = (int)( width * 0.06f);
  
  minim = new Minim(this);
  minim.loadFile("alienNight.mp3").play();
  
  setupStars();
  scale = height/768.0f;
  //-------------------------------------
  // alien code
  alienPosX = -100;
  alienPosY = height/10.0f;
  alienSpeedX = 5;
  alienSpeedY = random(-5, 20);

  
  
  //-------------------------------------
 
}

public void draw() 
{
  background(0, 25-min(timeFromStart(), 10), 70-min(timeFromStart(), 15));
  //background(100,100,100);
  float starAlpha = min(1, (timeFromStart()-1)/10.0f);
  drawStars(starAlpha);
  
  

  // Draw the moon
  xpos = (width * 0.7f)+ ((width/4)-((width/4)*min(1, timeFromStart()/10.0f)));
  ypos = (height * 0.2f)+(height-(height*min(1,timeFromStart()/10.0f)))/1.5f;

  for (float i = 1; i < 1.3f; i += 0.01f){  
      int hazeColor = color(255, 255, 150+min(80, 10*timeFromStart()), 0.03f*255);
      fill(hazeColor);
      ellipse(xpos, ypos, rad*i, rad*i);
  }
  randomSeed(0);
  fill(color(250, 255, 150+min(80, 10*timeFromStart())));
  ellipse(xpos, ypos, rad, rad);
  for (int i = 0; i < 100; i++){
      float s=  rad/random(5, 10);
      float an= random(0, 360);
      fill(color(150, 155, 050+min(80, 10*timeFromStart())), 0.05f*255);
      ellipse(xpos + radialCoordX(random(10, rad/1.1f), an),ypos+ radialCoordY(random(0, rad/1.1f), an), s,s);
    
  }
  
  // Draw ground
  
  fill(color(10, 55-min(15, timeFromStart()*2), 10));
  ellipse(width/2, height*5.7f, height*5, height*5);
  fill(color(0, 0, 0, 0.1f*255));
  ellipse(width/2, height*5.7f, height*5.05f, height*5.05f);
  //Draw trees
  
  
    float  startHeight = height/1.62f;
  float endHeight = height+10;
  float stepsize = (endHeight-startHeight)/treeAmount;
  for (float j = startHeight; j < endHeight; j+= stepsize){
    float crand = random(20,30);
    fill(color(0,crand,5));
    float x= random(0, width);
    //float y = random(height/1.62, height/1.35)+ scale* 60*(max(x, width-x)/(width/2));
    float y = j+scale* 60*(max(x, width-x)/(width/2));
    float size = 10.0f*scale+random(0,3);
    beginShape();
    for(int i = 90; i < 360; i+=120) {
      vertex(x + radialCoordX(size, i),y+ radialCoordY(size, i));   
    }
    endShape(CLOSE);
    fill(color(0,crand+1,0));
    beginShape();
    for(int i = 90; i < 360; i+=120) {
      vertex(x + radialCoordX(size-1, i),y-size+ radialCoordY(size-1, i));
    }
    endShape(CLOSE);
    
  
  }
  //---------------------------------------
  //alien
  
  if (timeFromStart() > warp){
      alienSpeedboost -= 1.2f;
  }  
  
  if (timeFromStart() > alienComes){
    iter ++;
    //alien code
    alienSpeedX += random(-1, 1)+alienSpeedboost;
    alienSpeedY += random(-1, 1);
    if (alienSpeedX > 20) alienSpeedX = 20+alienSpeedboost;
    if (alienSpeedX < -20) alienSpeedX = -20+alienSpeedboost; 
    if (alienSpeedY > 10) alienSpeedY = 10;
    if (alienSpeedY < -10) alienSpeedY = -10;

    if (beamMeUp){
      alienPosX += alienSpeedX*scale/10.0f;
      alienPosY += alienSpeedY*scale/10.0f;
      drawAlien(alienPosX, alienPosY, true, 1);
      if (iter >= beamOniter){
        iter = 0;
        beamMeUp = false;
        //println("beamtime end", timeFromStart());
        //beamOniter += random(-20,20);
        if (beamOniter < 30 || beamOniter > 150) beamOniter = 50;
      }  
    }
    else{
      alienPosX += alienSpeedX;
      alienPosY += alienSpeedY;
      drawAlien(alienPosX, alienPosY, false,1);
      if (iter >= beamOffIter){
        iter = 0;
        beamMeUp = true; 
        //println("beamtime start", timeFromStart());
        //beamOffIter += random(-50,50);
        if (beamOffIter < 60 || beamOffIter > 450) beamOffIter = 150;
      }
    }
    if (alienPosY < 0){ 
      float amountOut = abs(alienPosY)/height;
      alienSpeedY += 2; 
    }
    if (alienPosX < 0){ 
      
      alienSpeedX += 5; 
    }
    if (alienPosY > height/4){ 
      
      alienSpeedY -= 2; 
    }
    if (alienPosX > width/4.0f*3){ 
      
      alienSpeedX = -5 ; 
    }  
  }
  //---------------------------------------
  
  
  //end
  if (timeFromStart() > end){
    exit();
  }  
}

public void drawAlien(float xpos, float ypos, boolean beam,float alpha)
{
    int size = width / 20;
  if(beam){
    fill(color(255,255,100+random(-30, 30)));
    triangle(xpos, ypos, xpos-size/5, height, xpos+size/5, height);
    fill(color(255,255,100+random(-30, 30)), 255*0.4f);
    triangle(xpos, ypos, xpos-size/4, height, xpos+size/4, height);
    fill(color(255,255,100+random(-30, 30)), 255*0.1f);
    triangle(xpos-1, ypos, xpos-size/3, height, xpos+size/5, height);
    triangle(xpos+1, ypos, xpos-size/5, height, xpos+size/3, height);
    fill(color(0,0,0));
    float osuus = ((float)beamOniter-iter)/beamOniter;
    float matka = height-ypos;
    ellipse(xpos, ypos+osuus*matka, size/15, size/15);
  }  
  
  fill(color(0,0,0), alpha*255);
  ellipse(xpos, ypos, size, size/2);
  ellipse(xpos, ypos, size*2, size/4);
  
  for (int i = -3; i < 4; i++) {
    fill(color(230+20*sin(millis()/100.0f-i),230+20*sin(millis()/500.0f+i),0));
    ellipse(xpos+((size)/3*i),ypos+size/20*sin(millis()/100.0f+i), size/10, size/10);
  }

  
}  

public float timeFromStart(){
  return (millis()-startMillis)/1000.0f;
}  
ArrayList<Float> starXs = new ArrayList<Float>();
ArrayList<Float> starYs = new ArrayList<Float>();
ArrayList<Float> starSizes = new ArrayList<Float>();
ArrayList<Float> starHues = new ArrayList<Float>();

int starCount = 1500;

public void setupStars() {
    for(int i = 0; i < starCount/10*9; i++) {
    starXs.add(random(-20, width));
    starYs.add(random(0, height));
    starSizes.add(max(1,randomGaussian()*3));
    starHues.add(random(10, 25));
    
  }

  for(int i = 0; i < starCount/10.0f; i++) {
    starXs.add(random(-20, width));
    starYs.add(random(0, height));
    starSizes.add(max(1,randomGaussian()*5));
    starHues.add(random(10, 25));
    
  }
  
}


public void drawStars (float alphaMult) {
  for(int i = 0; i < starCount; i++) {
    float x = starXs.get(i);
    float y = starYs.get(i);
    float size = starSizes.get(i);
    float hue = starHues.get(i);
    drawStar(x, y, size, hue, alphaMult);
    
    x += size * 0.05f;
    
    if (x > width + 5) {
      x = -20+random(0,5);
      
    }
    
   
    starXs.set(i, x);
    
  }
  
}

public void drawStar(float x, float y, float size, float hue, float alphaMult) {
  
  colorMode(HSB, 100, 100, 100, 1);
  
  int starColor = color(hue, 50, 100 * size, 1*alphaMult);
  noStroke();
  
  //ellipse(x, y, 5 * size, 5 * size);
  if (size > 1){
    for (float i = 0; i < 1.2f; i += 0.1f){  
      int hazeColor = color(0, 0, 100, max(0.0f, 0.01f)*alphaMult);
      fill(hazeColor);
      ellipse(x, y, size +size*i, size+size*i);
    }
  }  
  fill(starColor);
  beginShape();
  for(int i = 0; i < 360; i+=60) {
    vertex(x + radialCoordX(size, i),y+ radialCoordY(size, i));
    vertex(x + radialCoordX(size/2, i), y+ radialCoordY(size/2, i));
  }
  endShape(CLOSE);
  
  colorMode(RGB, 255, 255, 255, 255);
  
}

public float radialCoordX(float radius, float angle) { 
  return radius * cos(angle / 360 * 2*PI );
}

public float radialCoordY(float radius, float angle) { 
  return radius * -sin(angle / 360 * 2*PI );
}
  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AliensFix" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
