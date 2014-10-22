#version 330

in uint aType;
in vec3 aPosition; 
in vec3 aVelocity; 
in uint aAge; 

out uint vType;
out vec3 vPosition; 
out vec3 vVelocity; 
out uint vAge; 

void main() 
{ 
	vType = aType; 
	vPosition = vec3(cos(float(aAge)*0.01), sin(float(aAge)*0.01), 0);
	vPosition = aPosition; 
	gl_Position = vec4(aPosition, 1);
	vVelocity = aVelocity; 
	vAge = aAge + uint(1); 
}