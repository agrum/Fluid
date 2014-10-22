#version 330

layout(points) in;
layout(triangle_strip) out; 
layout(max_vertices = 4) out;  

in vec4 v_position[];

void main() 
{ 
	gl_Position = v_position[0] + vec4(0.05, 0.05, 0.0, 1.0);
	EmitVertex(); 

	gl_Position = v_position[0] + vec4(-0.05, 0.05, 0.0, 1.0);
	EmitVertex(); 

	gl_Position = v_position[0] + vec4(0.05, -0.05, 0.0, 1.0);
	EmitVertex(); 

	gl_Position = v_position[0] + vec4(-0.05, -0.05, 0.0, 1.0);
	EmitVertex();

	EndPrimitive(); 
} 
