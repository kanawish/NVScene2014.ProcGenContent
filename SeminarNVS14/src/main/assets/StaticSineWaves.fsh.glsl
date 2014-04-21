#ifdef GL_ES
precision mediump float;
#endif

#define PI 3.1416

// Android gadget resolution
uniform vec2 resolution;

// Time example
// uniform float time;

// Direct angle example
 uniform float angle;

void main() {
  float waveFactor = 2.0*PI*3.0;

 vec2 position = gl_FragCoord.xy / resolution.xy ;

//  float sineWaveX = (1.0+sin(position.x*waveFactor))/2.0;
  float sineWaveY = (1.0+sin(position.y*waveFactor))/2.0;

// Time
//   float sineWaveX = (1.0+sin((position.x+sin(time))*waveFactor))/2.0;
//   float sineWaveY = (1.0+sin((position.y+cos(time))*waveFactor))/2.0;

// Angle
   float sineWaveX = (1.0+sin((position.x+sin(angle))*waveFactor))/2.0;

  gl_FragColor.r = sineWaveY;
  gl_FragColor.g = 1.0-sineWaveX;
  gl_FragColor.b = sineWaveX-sineWaveY;
  gl_FragColor.a = 1.0;
}
