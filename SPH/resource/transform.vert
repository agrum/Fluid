#version 400 core

#define POSITION	0
#define VELOCITY	1
#define COLOR		1

layout(location = POSITION) in vec3 p_position;
layout(location = VELOCITY) in vec3 p_velocity;

uniform float count;

out vec3 v_position;
out vec3 v_velocity;

void main()
{	
	v_position = p_position + p_velocity;
	v_velocity = vec3(0.001*cos(count), 0.001*sin(count), 0.0) + 0.5*p_velocity;
}
