#ifdef GL_ES
precision mediump float;
#endif

// modified by @hintz

// uniform vec2 mouse; // not used.
uniform float time; // Should be in there now.
uniform vec2 resolution;
uniform vec4 vColor;

#define PI 3.14159
#define TWO_PI (PI*2.0)
#define N 6.0

void main(void)
{
	vec2 center = (gl_FragCoord.xy);
	center.x=-10.12*sin(time/200.0);
	center.y=-10.12*cos(time/200.0);

	vec2 v = (gl_FragCoord.xy - resolution/20.0) / min(resolution.y,resolution.x) * 5.0;
	v.x=v.x-10.0;
	v.y=v.y-200.0;
	float col = 0.0;

	for(float i = 0.0; i < N; i++)
	{
	  	float a = i * (TWO_PI/N) * 61.95;
		col += cos(TWO_PI*(v.y * cos(a) + v.x * sin(a) /*+ mouse.y +i*mouse.x*/ + sin(time*0.004)*100.0 ));
	}

	col /= 3.0;

	gl_FragColor = vec4(col*1.0, -col*1.0,-col*4.0, 1.0);
}