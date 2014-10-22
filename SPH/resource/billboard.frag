#version 400

in vec3 gVelocity; 
in float gDensity; 
in float gDepth;
in vec4 gPos;
in float gRadius;

out vec4 FragColor; 

void main() 
{
	float densityColor = gDensity - 1.0;
	float distanceColor = 1.0 - gDepth / 60.0;
	if(length(gl_FragCoord.xy - gPos.xy) < gRadius)
		//FragColor = vec4(distanceColor-densityColor, distanceColor, distanceColor+densityColor, 1);
		FragColor = vec4(densityColor, 1, densityColor, 1);
	else
		discard;
}