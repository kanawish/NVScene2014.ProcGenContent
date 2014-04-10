# 14-04-05 Talk Notes and References

## Goals and synopsis

Show people how they can leverage OpenGL Shaders for installation type work. The theory shown here should directly be useful on Mobile (Android), Embedded (Raspberry Pi) and Web Chromebook/Chrome. It should also transfer well to other embedded/mobile platforms (iOS, etc.)

Part 1, Goal is to explain the subject, and end with a concrete "hello world" shader example (colored screen gradient). 

Part 2, present some more interesting shaders, make distinction between texture generators and post-processing types of shaders. Perhaps touch briefly on Procedural generation as well (just quickly show GoL example.)

Part 3, Interactivity, could show Android touch interactivity, hopefully demo Web interactivity, and demo of sound reactivity.


## Link pool [To be sorted]

- OpenGL (ES) Basics
	- [Official Android SDK doc](http://developer.android.com/guide/topics/graphics/opengl.html)
	- [OpenGL official site](http://www.khronos.org/)
	- [Nice OpenGL ES Android tutorial](http://www.learnopengles.com/android-lesson-one-getting-started/)
	- [GLSL Shader Basics](http://pixelshaders.com/sample/)
	- [Get started with GLSL / Processing](http://forum.processing.org/one/topic/thndl-shader-tutorial-in-processing-get-started-with-glsl.html)
	- [WebGL Intro](http://www.html5rocks.com/en/tutorials/webgl/shaders/)
- [Airtight Interactive](http://www.airtightinteractive.com/)
	- [Vertex fireball example](http://www.clicktorelease.com/code/perlin/explosion.html)
	- [Android Plasma](http://www.bidouille.org/prog/plasma)
- [ThreeJS](http://threejs.org/)
	- [Create a scene](http://threejs.org/docs/#Manual/Introduction/Creating_a_scene)
- Shader examples
	- [ShaderToy](https://www.shadertoy.com/)
	- [GLSL Sandbox](http://glsl.heroku.com/)
- Advanced subjects
	- [iquilezles.org](http://iquilezles.org/www/index.htm)
- Interactivity
	- [Interactive Art](http://en.wikipedia.org/wiki/Interactive_art) 
	
## Example code

Static Sine Wave Pattern

	precision mediump float;

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

