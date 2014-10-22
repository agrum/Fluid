#version 330

in vec3 aPosition; 
in vec3 aVelocity; 
in float aDensity; 

out vec3 vVelocity;
out float vDensity;

void main() 
{ 
	gl_Position = vec4(aPosition, 1);

	vVelocity = aVelocity;
	vDensity = aDensity;
} 
