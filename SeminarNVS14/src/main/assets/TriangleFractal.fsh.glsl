#ifdef GL_ES
precision mediump float;
#endif

uniform float time; // Should be in there now.
uniform vec2 resolution;
uniform vec4 vColor;

#define MAXDEPTH 20
#define SPEED 0.5
#define ROTSPEED 0.05
#define ONE_OVER_SQRT_3 0.5773502691896


/*
vec2 sidea = vec2(0,2*ONE_OVER_SQRT_3) - vec2(1,-1*ONE_OVER_SQRT_3);
vec2 sideb = vec2(-1,-1*ONE_OVER_SQRT_3) - vec2(0,2*ONE_OVER_SQRT_3);
vec2 sidec = vec2(1,-1*ONE_OVER_SQRT_3)- vec2(-1,-1*ONE_OVER_SQRT_3);
*/

float getLevel( vec2 pos ){
  float distance = length(pos);
	return floor(log2(distance) - time*SPEED);
}

float getLevelSize( float level){
	return exp2(level + time*SPEED);
}

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec3 getTriangleColor( float level, float offs, vec3 back){
	float alpha = pow(0.57, offs);
	float t = level/5.4264;//getLevelSize(level)/length(iResolution.xy);
	//vec3 drawcol = hsv2rgb(vec3(t, 1.0, 1.0));
	//vec3 drawcol = hsv2rgb(vec3(t, exp(-offs*0.8), 1.0));
	//vec3 drawcol = hsv2rgb(vec3(t, 1.0, exp(-offs*0.8)));
	//vec3 drawcol = hsv2rgb(vec3(t, exp(-offs*mouse.x), exp(-offs*mouse.y)));
	vec3 drawcol = hsv2rgb(vec3(t, exp(-offs*0.1), exp(-offs*.01)));
	return drawcol*alpha + back*(1.0-alpha);
}

void main( void ) {

	vec2 pta = vec2(-1.0,-1.0*ONE_OVER_SQRT_3);
	vec2 ptb = vec2(1.0,-1.0*ONE_OVER_SQRT_3);
	vec2 ptc = vec2(0.0,2.0*ONE_OVER_SQRT_3);

	vec2 btoa = ptb-pta;
	vec2 ctob = ptc-ptb;
	vec2 atoc = pta-ptc;

	vec2 position =  (gl_FragCoord.xy - resolution.xy * vec2(0.5,0.5));
	float r = length(position);
	float theta = atan(position.y, position.x) + time*ROTSPEED;
	position = r*vec2(cos(theta),sin(theta));

	float level = getLevel(position);
	float levelsize = getLevelSize(level);
	vec2 relpos = position / levelsize * ONE_OVER_SQRT_3;
	if(mod(level,2.0) == 1.0) relpos = -relpos;
	float relunitsize = 1.0 * levelsize / ONE_OVER_SQRT_3;
	vec3 color;

	float offs = 0.0;

	for(int i=0;i<MAXDEPTH;i++){
		vec2 reltoa = relpos - pta;
		vec2 reltob = relpos - ptb;
		vec2 reltoc = relpos - ptc;
		bool flipped=false;
		if(btoa.x*reltoa.y - btoa.y*reltoa.x < 0.0){ //outside!
			reltoa = 2.0*dot(reltoa,btoa)/dot(btoa,btoa) * btoa - reltoa;
			relpos = reltoa + pta;
			flipped=true;
		}else if(ctob.x*reltob.y - ctob.y*reltob.x < 0.0){ //outside!
			reltob = 2.0*dot(reltob,ctob)/dot(ctob,ctob) * ctob - reltob;
			relpos = reltob + ptb;
			flipped=true;
		}else if(atoc.x*reltoc.y - atoc.y*reltoc.x < 0.0){ //outside!
			reltoc = 2.0*dot(reltoc,atoc)/dot(atoc,atoc) * atoc - reltoc;
			relpos = reltoc + ptc;
			flipped=true;
		}
		relpos = -relpos;
		relpos *= 2.0;
		relunitsize *= 0.5;

		if(flipped){
			color = getTriangleColor(level, offs, color);
			offs += 1.0;

		}else{
			level -= 1.0;
			color = getTriangleColor(level, offs, color);
		}
		if(relunitsize < 0.5) break;
	}


	gl_FragColor = vec4( color, 1.0 );

}