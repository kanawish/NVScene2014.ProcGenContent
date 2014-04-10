#ifdef GL_ES
precision mediump float;
#endif

#define PI 3.1416

// Varies from 0.0 to 1.0
varying vec2 position;

void main() {
  float waveFactor = 2.0*PI*10.0;

  float sineWaveX = (1.0+sin(position.x*waveFactor))/2.0;
  float sineWaveY = (1.0+sin(position.y*waveFactor))/2.0;

  gl_FragColor.r = 0.0; //sineWaveY;
  gl_FragColor.g = 1.0-sineWaveX;
  gl_FragColor.b = sineWaveX;
  gl_FragColor.a = 1.0;
}

