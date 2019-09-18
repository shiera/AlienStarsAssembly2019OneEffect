ArrayList<Float> starXs = new ArrayList<Float>();
ArrayList<Float> starYs = new ArrayList<Float>();
ArrayList<Float> starSizes = new ArrayList<Float>();
ArrayList<Float> starHues = new ArrayList<Float>();

int starCount = 1500;

void setupStars() {
    for(int i = 0; i < starCount/10*9; i++) {
    starXs.add(random(-20, width));
    starYs.add(random(0, height));
    starSizes.add(max(1,randomGaussian()*3));
    starHues.add(random(10, 25));
    
  }

  for(int i = 0; i < starCount/10.0; i++) {
    starXs.add(random(-20, width));
    starYs.add(random(0, height));
    starSizes.add(max(1,randomGaussian()*5));
    starHues.add(random(10, 25));
    
  }
  
}


void drawStars (float alphaMult) {
  for(int i = 0; i < starCount; i++) {
    float x = starXs.get(i);
    float y = starYs.get(i);
    float size = starSizes.get(i);
    float hue = starHues.get(i);
    drawStar(x, y, size, hue, alphaMult);
    
    x += size * 0.05;
    
    if (x > width + 5) {
      x = -20+random(0,5);
      
    }
    
   
    starXs.set(i, x);
    
  }
  
}

void drawStar(float x, float y, float size, float hue, float alphaMult) {
  
  colorMode(HSB, 100, 100, 100, 1);
  
  color starColor = color(hue, 50, 100 * size, 1*alphaMult);
  noStroke();
  
  //ellipse(x, y, 5 * size, 5 * size);
  if (size > 1){
    for (float i = 0; i < 1.2; i += 0.1){  
      color hazeColor = color(0, 0, 100, max(0.0, 0.01)*alphaMult);
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

float radialCoordX(float radius, float angle) { 
  return radius * cos(angle / 360 * 2*PI );
}

float radialCoordY(float radius, float angle) { 
  return radius * -sin(angle / 360 * 2*PI );
}
