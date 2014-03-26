# NVScene 2014 Prep Notes

## Presentation plan

### First plan

* Basics (intro, examples of uses, etc)
* Ontogenetic vs Teleological generation
* Multiple working examples (Fractals, Cellular Automata, Geometry generation, etc)
* Noise generation
* Use of Shaders
	* As part of PG processes.
	* As visual effect filters (with noise as input)
* Code optimization for the Tegra 4.

### Second plan

If I present this as a working code lab type project, how do I go about it?

First, I'm thinking a GitHub hosted project, wiki actually are the talk notes / slides / materials.

#### Technical stuff 

That project is a full-fledged gradle project, avoid too many NVidia specifics unless they really bring something crazy to the table. The perf of Tegra 4 can probably be well shown off simply by using GLSL and/or Renderscript.

Renderscript support is now freshly supported in gradle (time of writing, feb 2014) so that holds some interest on it's own.

#### Theoretical stuff

Short Teleological vs Ontogenetic explanation... 

#### Starting blocks: Noise and Simulation

**Noise**: At the core, PCG techniques are there to create something out of (practically) nothing. To achieve this, at the core, we need noise generators. There exists many [noise generation](http://en.wikipedia.org/wiki/Gradient_noise) techniques, and we'll be using Kevin Perlin's classic Simplex noise.

* [Perlin/Simplex noise](http://stackoverflow.com/questions/18279456/any-simplex-noise-tutorials-or-resources) 

**GPGPU & Simulation**: Show a basic example of cellular automata as parallel process. Each particle in the system has a 'behavior', and the system evolves in a recursive manner. We feed our algorithm a dataset, and the resulting dataset is once again fed to the algorithm, etc. 

This is where we'll also introduce the small framework code we'll be using to do GPGPU over GLSL. 

* OpenGL Android Basics
	* [Official SDK doc](http://developer.android.com/guide/topics/graphics/opengl.html)
	* [Nice tutorial](http://www.learnopengles.com/android-lesson-one-getting-started/)
* [Cell automata on GPU](http://www.people.usi.ch/baludam/projects/GLCAlib/doc/)
* [OpenGL SL render to texture](http://blog.angusforbes.com/openglglsl-render-to-texture/)

**Content generation**: Explore examples of content generation. 

* [Worldbuilding](http://blog.kaelan.org/randomly-generated-world-map/) and [Map Generation](http://pcg.wikidot.com/pcg-algorithm:map-generation)
	* Terrain Generation
	* Oceans
	* Rivers
	* Climate
	* Temperature
	* Biomes
	* Forests
	
* Genetic algorithms
* Simulation


## Research notes

### DevArts specifics

* http://forum.processing.org/one/topic/thndl-shader-tutorial-in-processing-get-started-with-glsl.html
* ShaderToy
* three.js
* ?


### Tech notes

* NVidia Shield doesn't support OpenGL ES 3.0 (surprising)
* NVidia Note 7 does, [reportedly](http://liliputing.com/2013/12/nvidia-tegra-note-update-brings-android-4-3-plus-stylus-camera-improvements.html) (check that out)
* That article is **wrong-wrong**, Tegra 4 doesn't support OpenGL ES 3, as per the chip's whitepaper.

### Core Tegra Dev links

* http://docs.nvidia.com/tegra/index.html
* [GPU Gems 1](https://developer.nvidia.com/content/gpu-gems-part-i-natural-effects) 
* [GPU Gems 2](https://developer.nvidia.com/content/gpu-gems-2)
* [GPU Gems 3](https://developer.nvidia.com/content/gpu-gems-3)

### GLSL tutorials

* http://www.lighthouse3d.com/tutorials/glsl-tutorial/data-types-and-variables/
* http://aerotwist.com/tutorials/an-introduction-to-shaders-part-1/
* http://www.html5rocks.com/en/tutorials/webgl/shaders/?redirect_from_locale=cs
* http://joshbeam.com/articles/getting_started_with_glsl

### GPGPU over GLSL/RenderScript examples

GLSL

* [Google search](https://www.google.com/search?q=using+glsl+for+computation)
* [Mandelbrot](https://www.thasler.org/blog/?p=45)
* [Cellular Automaton example](http://blog.angusforbes.com/openglglsl-render-to-texture/)
* [Intro to GPGPU](http://stackoverflow.com/questions/3915001/using-shader-for-calculations)
* **[Good Cellular Automata exemple, wireworld](http://www.people.usi.ch/baludam/projects/GLCAlib/doc/)**
* [Basic calc example](http://stackoverflow.com/questions/4694608/basic-calculation-with-glsl)

Renderscript

* [Google Framework doc](http://developer.android.com/guide/topics/renderscript/index.html)
* [SurfaceTexture vs Renderscript](http://stackoverflow.com/questions/9909039/using-surfacetexture-in-combination-with-renderscript)
* [Looks like new ground...](https://groups.google.com/forum/#!msg/reaction-diffusion/galu4h9mJ2E/XpFvKVAhN2AJ)




### NVidia specific notes

* (No longer strictly true...) http://stackoverflow.com/questions/17550619/cuda-with-opencv-for-android

### List of harder problems to solve

* http://en.wikipedia.org/wiki/Wave_propagation
* http://en.wikipedia.org/wiki/FDTD

### Good source of ideas and examples (Raytracing/Raymarching/etc)

* http://iquilezles.org/www/index.htm
* http://iquilezles.org/www/material/nvscene2008/nvscene2008.htm
* http://www.geeks3d.com/20140201/glsl-menger-sponge-raymarching-code-samples-updated/
* http://www.martinchristen.ch/
* https://code.google.com/p/reaction-diffusion/
* http://golly.sourceforge.net/

### Post-proc effects 

* http://www.geeks3d.com/20140213/glsl-shader-library-fish-eye-and-dome-and-barrel-distortion-post-processing-filters/

### World gen

* http://gamedev.stackexchange.com/questions/18840/how-does-minecraft-world-generation-happen

### Talk ideas / goals

* Goal 1: GPGPU PGC
* Goal 2: Compeling visualisation
* Renderscript likely something to be explored...
	* [link](http://stackoverflow.com/questions/9909039/using-surfacetexture-in-combination-with-renderscript)
* I'd like to focus on leveraging the GPU to
	* Generate PGC.
		* [Potential tracks](https://developer.nvidia.com/content/gpu-gems-part-vi-beyond-triangles)
	* Render PGC as a second item? 
	* I had 2d filters in my whishlist at first...
		* [glow](https://developer.nvidia.com/content/gpu-gems-part-i-natural-effects)

* [Perlin Noise](http://pcg.wikidot.com/pcg-algorithm:perlin-noise) 
* [Simplex](https://github.com/ashima/webgl-noise)
	
* [Minecraft](https://minecraft.net/)/[Dwarf Fortress](http://www.bay12games.com/dwarves/) are good examples of PGC...
	* [Minecrafty terrain gen](http://codeflow.org/entries/2010/dec/09/minecraft-like-rendering-experiments-in-opengl-4/)
* [UnAngband](http://unangband.blogspot.ca/)?
* (Game) WorldGen... 
	* [Whittaker](http://pcg.wikidot.com/pcg-algorithm:whittaker-diagram)
* [Dungeon generation](http://pcg.wikidot.com/pcg-algorithm:dungeon-generation) 
* [Dungeon gen example page](http://donjon.bin.sh/fantasy/dungeon/about/)
* Cavern generation
	* Uses of cell automatons as smoothers
* Wilderness
	* [Fire propagation](http://pcg.wikidot.com/pcg-algorithm:fire-propagation)
	* [Wave propagation](http://en.wikipedia.org/wiki/Wave_propagation)
	* [FDTD](http://en.wikipedia.org/wiki/FDTD)
	* [Fluid dynamics]()
	* [L-Systems](http://pcg.wikidot.com/pcg-algorithm:l-system)
	* [IFS](http://pcg.wikidot.com/pcg-algorithm:iterated-function-system)

#### Generate


[Texture buffer approach](http://blog.angusforbes.com/openglglsl-render-to-texture/)

#### Render



### Toolchain

#### perfhud-es 

http://stackoverflow.com/questions/12414563/nvidia-perfhud-es-tegra-on-google-nexus-7

Mention pidcat, helps with filtering.

Explore the shader tools


## Procedural Content Generation and Shaders

Demosceners are no strangers to Procedural Generation. In the early days computing resources were very limited, and Procedural Generation algorithms provided ways to generate complex and entrancing graphical constructs, going beyond simple disk space or memory limitations.

With the mobile revolution upon us, we now all have extremely powerful computers that fit in our pockets. But oddly, we are now faced with new limitations, in the form of bandwith, storage space, etc.

Procedural generation allow us to take simple seeds, and with the right algorithm turn them into massive universes to explore.

This talk will focus on Procedural Content Generation techniques, and how to use them in concert with OpenGL ES Shaders to maximum effect.

Note: This talk will be using the NVidia Shield and Tegra tools suite for it's example code, but could be of interest to anyone new to Procedural Content Generation and Shaders.

## Bio 

### ETIENNE CARON (MR. KHAN / NAID)

An active member of the North American demoscene in the 90s, Etienne was a main organizer for the NAID Demoparties (95/96). Now the Android lead developer for PasswordBox.com, Etienne is also the current lead of GDG Montreal-Android. GDGs are independent, Google backed developer groups. 

