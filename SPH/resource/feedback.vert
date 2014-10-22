#version 400 core

#define POSITION	0
#define VELOCITY	1

layout(location = POSITION) in vec4 p_position;

out vec4 v_position;

void main()
{	
	v_position = p_position;
}

