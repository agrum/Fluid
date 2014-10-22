#version 330

layout(points) in;
layout(triangle_strip) out; 
layout(max_vertices = 4) out; 

uniform float uWindowWidth;
uniform float uWindowHeight;

uniform mat4 uMVP; 
uniform mat4 uMV; 
uniform mat4 uP; 

in vec3 vVelocity[]; 
in float vDensity[]; 

out vec3 gVelocity;
out float gDensity;
out float gDepth;
out vec4 gPos;
out float gRadius;

void main() 
{ 
	vec4 Pos = gl_in[0].gl_Position;

	gVelocity = vVelocity[0];
	gDensity = vDensity[0];

	float scale = 0.5;
	gPos = uMVP * Pos;
	gPos = gPos / gPos.w;
	gPos = 0.5 + (gPos * 0.5);
	gPos.x = gPos.x * uWindowWidth;
	gPos.y = gPos.y * uWindowHeight;

	vec4 worlpPos = uMV * Pos;
	gDepth = length(worlpPos / worlpPos.w);
	
	vec4 screenPos = uMVP * Pos;
	vec4 scalePos = uP * (uMV * Pos + vec4(scale, 0.0, 0.0, 1.0));
	gRadius = ((scalePos.x / scalePos.w) - (screenPos.x / screenPos.w)) * uWindowWidth * 0.5;

	gl_Position = uP * (uMV * Pos + vec4(scale, scale, 0.0, 1.0));
	EmitVertex(); 

	gl_Position = uP * (uMV * Pos + vec4(-scale, scale, 0.0, 1.0));
	EmitVertex(); 

	gl_Position = uP * (uMV * Pos + vec4(scale, -scale, 0.0, 1.0));
	EmitVertex(); 

	gl_Position = uP * (uMV * Pos + vec4(-scale, -scale, 0.0, 1.0));
	EmitVertex();

	EndPrimitive(); 
} 
