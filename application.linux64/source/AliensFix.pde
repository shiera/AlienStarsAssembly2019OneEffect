import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

/**
 * Bounce. 
 * 
 * When the shape hits the edge of the window, it reverses its direction. 
 */
 
int rad = 60;        // Width of the shape
float xpos, ypos;    // Starting position of shape    

float xspeed = 2.8;  // Speed of the shape
float yspeed = 2.2;  // Speed of the shape

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
float warp = 35.5;
float end = 42;

Minim minim;

void setup() {
  randomSeed(0);
  
  //size(640, 360);
  startMillis = millis();
  fullScreen(P3D);
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
  scale = height/768.0;
  //-------------------------------------
  // alien code
  alienPosX = -100;
  alienPosY = height/10.0;
  alienSpeedX = 5;
  alienSpeedY = random(-5, 20);

  
  
  //-------------------------------------
 
}

void draw() 
{
  background(0, 25-min(timeFromStart(), 10), 70-min(timeFromStart(), 15));
  //background(100,100,100);
  float starAlpha = min(1, (timeFromStart()-1)/10.0);
  drawStars(starAlpha);
  
  

  // Draw the moon
  xpos = (width * 0.7f)+ ((width/4)-((width/4)*min(1, timeFromStart()/10.0)));
  ypos = (height * 0.2f)+(height-(height*min(1,timeFromStart()/10.0)))/1.5;

  for (float i = 1; i < 1.3; i += 0.01){  
      color hazeColor = color(255, 255, 150+min(80, 10*timeFromStart()), 0.03*255);
      fill(hazeColor);
      ellipse(xpos, ypos, rad*i, rad*i);
  }
  randomSeed(0);
  fill(color(250, 255, 150+min(80, 10*timeFromStart())));
  ellipse(xpos, ypos, rad, rad);
  for (int i = 0; i < 100; i++){
      float s=  rad/random(5, 10);
      float an= random(0, 360);
      fill(color(150, 155, 050+min(80, 10*timeFromStart())), 0.05*255);
      ellipse(xpos + radialCoordX(random(10, rad/1.1), an),ypos+ radialCoordY(random(0, rad/1.1), an), s,s);
    
  }
  
  // Draw ground
  
  fill(color(10, 55-min(15, timeFromStart()*2), 10));
  ellipse(width/2, height*5.7, height*5, height*5);
  fill(color(0, 0, 0, 0.1*255));
  ellipse(width/2, height*5.7, height*5.05, height*5.05);
  //Draw trees
  
  
    float  startHeight = height/1.62;
  float endHeight = height+10;
  float stepsize = (endHeight-startHeight)/treeAmount;
  for (float j = startHeight; j < endHeight; j+= stepsize){
    float crand = random(20,30);
    fill(color(0,crand,5));
    float x= random(0, width);
    //float y = random(height/1.62, height/1.35)+ scale* 60*(max(x, width-x)/(width/2));
    float y = j+scale* 60*(max(x, width-x)/(width/2));
    float size = 10.0*scale+random(0,3);
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
      alienSpeedboost -= 1.2;
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
      alienPosX += alienSpeedX*scale/10.0;
      alienPosY += alienSpeedY*scale/10.0;
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
    if (alienPosX > width/4.0*3){ 
      
      alienSpeedX = -5 ; 
    }  
  }
  //---------------------------------------
  
  
  //end
  if (timeFromStart() > end){
    exit();
  }  
}

void drawAlien(float xpos, float ypos, boolean beam,float alpha)
{
    int size = width / 20;
  if(beam){
    fill(color(255,255,100+random(-30, 30)));
    triangle(xpos, ypos, xpos-size/5, height, xpos+size/5, height);
    fill(color(255,255,100+random(-30, 30)), 255*0.4);
    triangle(xpos, ypos, xpos-size/4, height, xpos+size/4, height);
    fill(color(255,255,100+random(-30, 30)), 255*0.1);
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
    fill(color(230+20*sin(millis()/100.0-i),230+20*sin(millis()/500.0+i),0));
    ellipse(xpos+((size)/3*i),ypos+size/20*sin(millis()/100.0+i), size/10, size/10);
  }

  
}  

float timeFromStart(){
  return (millis()-startMillis)/1000.0;
}  
