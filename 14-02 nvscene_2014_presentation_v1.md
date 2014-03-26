# NVScene 2014 Presentation - Research

## Presentation Goals

We'll explore a gammut of procedural content generation techniques, and see how to use GPGPU techniques to turbocharge their performance. 

## The plan

**Bio / Talk intro** [5m]




**Presentation**

(use star to denote content done, go back to - for images-complete)

- Theory: PCG History, concepts and applications
	* Use in games 
		* Conway's Game of Life. (1970)
		* Elite, Seven Cities of Gold, The Sentinel.
		* from Rogue to Diab.. Minecraft and Dwarf Fortress. (1980s->2010s)
	* High level concepts Ontogenetic and Teleological.
		* Perlin and Simplex noise.
		* Fractals, L-Systems
		* Cell Automata, Genetic algorithms
	- ~~Applications~~ Dupes the worldGen example at the end.
		- ~~Creating Landscapes, Simulating Ecosystems.~~
		- ~~Dungeoneering, maze creation.~~
		- ~~Assisted Content Creation, Mixed methods.~~
- Approach: a Small Mobile GPGPU Prototyping Framework
	- Mobile technology landscape
		- OpenGL ES 2.0, 3.0 and 3.1
		- Renderscript
		- Tegra 4, Tegra K1
	- Github project
		- Modern Android App toolchain
			- Android Studio / IntelliJ
			- Gradle
			- Genymotion
		- Framwork architecture
			- Cooperating with the OS: using GLSurfaceView
			- Loading and running simple Shader gadgets
			- GPGPU gadgets
		- TODOs
			- Browsing the shader repository
			- Raspberry Pie support
			- OpenGL ES 3.1 
	- Quick demonstration
		- Simplex (?Might not have time to port to C99)
		- Shader (Plasma/Fractal)
		- REST repo
		- Cell Automaton
- Hello world: Worldgen examples
	- Terrain and rivers
	- Temperature + Precipitation = Biomes
	- Forests, Dungeons and Politics

**Closing** [5m]

## The research

### [History](http://en.wikipedia.org/wiki/Procedural_generation)

- [Conway's](http://en.wikipedia.org/wiki/Conway_game_of_life)
- [Elite](http://en.wikipedia.org/wiki/Elite_\(video_game\))
- [The Seven Cities of Gold]()
- [The Sentinel](http://en.wikipedia.org/wiki/The_Sentinel_\(computer_game\)) with [Gameplay](http://www.youtube.com/watch?v=9V_pgo3vgiI)
	
### High level concepts: [Ontogenetic and Teleological](http://pcg.wikidot.com/pcg-algorithm:teleological-vs-ontogenetic) or [Simulations](http://en.wikipedia.org/wiki/Simulation) and [Heuristics](http://en.wikipedia.org/wiki/Heuristic)

- [Perlin](http://pcg.wikidot.com/pcg-algorithm:perlin-noise) and [Simplex](https://github.com/ashima/webgl-noise) [noise](http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf), also see [Kevin Perlin's](http://mrl.nyu.edu/~perlin/) site.
- [Fractals](http://en.wikipedia.org/wiki/Fractal), [L-Systems](http://en.wikipedia.org/wiki/L-system)
	- [Menger](http://en.wikipedia.org/wiki/Menger_sponge) 
- [Cell Automata](http://www.gamasutra.com/view/feature/134736/an_intro_to_cellular_automation.php), [Genetic algorithms](http://pcg.wikidot.com/pcg-algorithm:genetic-algorithm), also Core Wars + GA = [Core Life](http://corewar.co.uk/perry.htm)



### Framework: Technology

[OpenGL ES 2.0](http://www.khronos.org/registry/gles/#specs2) includes OpenGL **ES** Shading Language 1.0.17, [not to be confused](http://stackoverflow.com/questions/8872105/what-versions-of-glsl-can-i-use-in-opengl-es-2-0) with it's non-ES counterpart, OpenGL Shading Language 1.10.  

[OpenGL ES 3.0](http://www.khronos.org/registry/gles/#specs3) includes OpenGL ES Shading Language 3.00.4.

[OpenGL ES 3.1](http://www.khronos.org/registry/gles/#specs31) upgrades OpenGL ES Shading Language to version 3.10. More relevant to today's talk, a great deal of the new feature set relates to general computing tasks.

The [Tegra 4](http://www.nvidia.com/docs/IO/116757/Tegra_4_GPU_Whitepaper_FINALv2.pdf) does not support OpenGL ES 3.0, since it is missing a few features. (FP32 pixel shaders, EAC/ETC2 texture compression format, etc.) 

The [Tegra K1](http://www.nvidia.com/content/PDF/tegra_white_papers/Tegra_K1_whitepaper_v1.0.pdf) will reportedly support both OpenGL ES 3.0 and the full OpenGL 4.4 feature set, CUDA 6.0, as well as Tessellation and Compute shaders. 

*Note: This is interesting. How will this be made available to Android developpers? Special libraries and drivers seem to be in order. Big problem then becomes writing very NVidia specific code, developpers for now would have little incentive to do this for the large scale public. Might be interest for application specific, custom-made business projects? A K1 Ouya could be intringing...*

One focus of this talk is on General-purpose computing on GPU, a.k.a. [GPGPU](http://en.wikipedia.org/wiki/GPGPU). As per Wikipedia: *"OpenCL is the currently dominant open general-purpose GPU computing language. The dominant proprietary framework is Nvidia's CUDA."* 

Sadly, neither of these are standardly available to the Android developper. There are technical reasons and compatibility reasons [behind this](http://stackoverflow.com/questions/14385843/why-did-google-choose-renderscript-instead-of-opencl), perhaps some historical ones as well.

All in all, we do have two frameworks to our disposal. OpenGL ES Shading Language and RenderScript. Of the two, GLSL will result in code that will be more easily re-usable on other platforms. Renderscript, being specifically designed for GPGPU has interesting features that could make our job easier, but will result in less portable code in some ways. 

If you're aiming to do cross-platform projects (iOS/Android), you might be better served by the GLSL approach. If you're hoping to support a significant portion of the Android landscape (4000+ types of devices configurations at time of writing), you should probably give consideration to Renderscript.

[Renderscript](http://developer.android.com/guide/topics/renderscript/compute.html) is now very much dedicated to computing tasks on large buffers of data, and should bring speed improvements on a wide variety of Android hardware. [Wikipedia](http://en.wikipedia.org/wiki/Renderscript) has a streamlined history. 


### Framework: The github project 

- The github [repo](https://github.com/kanawish/SeminarNVScene2014)
- Modern Android toolchain
	- [IntelliJ and Android Studio](http://http://www.jetbrains.com)
	- [Gradle](http://tools.android.com/tech-docs/new-build-system)
	- [Genymotion](http://www.genymotion.com/)

### Framework: Architecture 

- [Android's OpenGL ES docs](http://developer.android.com/guide/topics/graphics/opengl.html)
- [GLSurfaceView](http://developer.android.com/reference/android/opengl/GLSurfaceView.html)

### Hello world: Terrain and rivers

### Hello world: Temperature + Precipitation = Biomes

### Hello world: Forests, Dungeons and Politics

[Random scattering for landscapes](http://www.gamasutra.com/view/feature/130071/random_scattering_creating_.php)
