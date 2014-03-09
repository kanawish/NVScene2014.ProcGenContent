# Procedural Content Generation and Shaders

## by Etienne Caron

This GitHub project will host the demo code and slides for the talk I will be giving at NVScene 2014.

## Code roadmap

- Simplex noise generation
- GPGPU with GLSL, Renderscript(TBD?)
	- Applied to Cellular Automata
	- Applied to Simplex noise(TBD?)
- Simple Content Rendering (Voxel-ish?)
- Worldbuilding techniques?

## WorkLog

### March 3, 2014

Next up is getting some "pure calculation" shaders up and running, figuring some standard uniform names I'll be using as inputs, getting output back from shaders. 

- Game of life (important, key item)
- Create 3d random space using Simplex
	- See the simplex lava-lamp type shaders (ShaderToy)
- Sound? (FFT would be sweet, only if quick.)
- Shader: [Sierpinski Polygon](https://www.shadertoy.com/view/4djGW1)

### March 2, 2014

Still working up to the actual 3d simplex generation, but did get shader code up and running from GLSL Sandbox and ShaderToy.

- Installed some shader testing tools, ShaderMaker
- Have a couple of shader examples running on devices

### March 1, 2014: [Minecrafty-generation of cubes in 3d space](http://codeflow.org/entries/2010/dec/09/minecraft-like-rendering-experiments-in-opengl-4/)

- Quick note: [shiny rendering example](https://www.shadertoy.com/view/MdlGzn)
- [Porting? ehh...](http://stackoverflow.com/questions/7536956/opengl-es-2-0-shader-best-practices)
- [ES 2 specific references?](http://gamedev.stackexchange.com/questions/12303/opengl-es-2-0-repository-of-quality-shaders)
- [GLSL Sandbox](http://glsl.heroku.com/) 
	- [Blobs](http://glsl.heroku.com/e#14482.0)
	- [GitHub](https://github.com/mrdoob/glsl-sandbox)
- [ShaderToy explained?](http://stackoverflow.com/questions/19449590/webgl-glsl-how-does-a-shadertoy-work)
	- Explore [iquilezles.org](http://iquilezles.org/www/index.htm)
	- [TerrainMarching](http://iquilezles.org/www/articles/terrainmarching/terrainmarching.htm)
- [BadTV](http://www.airtightinteractive.com/)
- Book? Game and Graphics Programming for iOS and Android with OpenGL ES 2.0
 By Romain Marucchi-Foino
- Software ShaderMaker
- ...
- FX: [iOS & Cocos2D](http://www.raywenderlich.com/10862/how-to-create-cool-effects-with-custom-shaders-in-opengl-es-2-0-and-cocos2d-2-x)
- [Shader Practicalities (Apple Dev)](https://developer.apple.com/library/ios/documentation/3DDrawing/Conceptual/OpenGLES_ProgrammingGuide/BestPracticesforShaders/BestPracticesforShaders.html#//apple_ref/doc/uid/TP40008793-CH7-SW3)




### Overall plan (March 1, 2014)
- Started by implementing Simplex, see code notes
- Went with a more "native Android" approach for OpenGLSL mini-framework.
	- Pro: More portable, future proof, perhaps with pay-offs in "regular" apps, IDE friendly.
	- Con: Somewhat less portable compared to pure C, but... also probable perf costs/overhead. 
	- Overall: you can always optimize later, I always go with coding speed first, optimization later.
- Android Open GL "Getting started":
	- [Official Google Android dev docs](http://developer.android.com/guide/topics/graphics/opengl.html)	
	- Learn OpenGL ES 
		- [Lesson 1](http://www.learnopengles.com/android-lesson-one-getting-started/)
		- [Lesson 2](http://www.learnopengles.com/android-lesson-two-ambient-and-diffuse-lighting/)
		- [Lesson 3](http://www.learnopengles.com/android-lesson-three-moving-to-per-fragment-lighting/)
- [Minecrafty-generation of cubes in 3d space](http://codeflow.org/entries/2010/dec/09/minecraft-like-rendering-experiments-in-opengl-4/) is a great place to start, considering my overall plan.
- [World Generation](http://blog.kaelan.org/randomly-generated-world-map/) is a nice practical way to run through a ton of Procedural Generation techniques, and not limited to 3d terrain and models.
- [Map Generation](http://pcg.wikidot.com/pcg-algorithm:map-generation) also holds that promise.
- Classical Fractal (2D and 3D) and math model exploration (Think Mandelbrot) are also very much worth examining. 

**Bonus points?**

These I think I need to strongly consider if I want a "demo-sceney" final product. My impression is my explorations should land me there if all goes well...

- Live signals (video/photo stream, audio stream) in PGC. 
- Noise and visual effects are somewhat PGCish.
- Cartoony render or models for cubes. Something cute-n-shiny, like on those minecraft t-shirts and posters...
	

