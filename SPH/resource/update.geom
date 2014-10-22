#version 330

layout(points) in;
layout(points) out;
layout(max_vertices = 30) out;

in uint vType[];
in vec3 vPosition[];
in vec3 vVelocity[];
in uint vAge[];

out uint gType;
out vec3 gPosition;
out vec3 gVelocity;
out uint gAge;

uniform uint uDeltaTimeMillis;
uniform uint uTime;
uniform sampler2D uRandomTexture;
uniform uint uLauncherPeriod;
uniform uint uShellLifetime;
uniform uint uSecondaryShellLifetime;

vec3 GetRandomDir(float p_texCoord)
{
	vec3 dir = texture(uRandomTexture, vec2(p_texCoord, 0.0f)).xyz;
	dir -= vec3(0.5, 0.5, 0.5);
	return dir;
}

void main() 
{
	gType = vType[0];                                                                   
	gPosition = vPosition[0];                                                           
	gVelocity = vVelocity[0];                                                           
	gAge = vAge[0];   
	
	EmitVertex();
	EndPrimitive();

	/*uint PARTICLE_TYPE_LAUNCHER = uint(0);
	uint PARTICLE_TYPE_SHELL = uint(1);
	uint PARTICLE_TYPE_SECONDARY_SHELL = uint(2);
	uint Age = vAge[0] + uDeltaTimeMillis;

	if (vType[0] == PARTICLE_TYPE_LAUNCHER) {
		if (Age >= uLauncherPeriod) {
			vec3 dir = GetRandomDir(float(uTime));
			Age = uint(0);

			gType = PARTICLE_TYPE_SHELL;
			gPosition = vPosition[0];
			gVelocity = normalize(dir) / 20.0;
			gAge = Age;

			EmitVertex();
			EndPrimitive();
        }

		gType = PARTICLE_TYPE_LAUNCHER;
		gPosition = vPosition[0];
		gVelocity = vVelocity[0];
		gAge = Age;
		
		EmitVertex();
		EndPrimitive();
	}
	else {
		float t1 = vAge[0];
		float t2 = Age;
		vec3 vel = vVelocity[0];
		vec3 acc = vec3(0.0, -9.81, 0.0);

		if (vType[0] == PARTICLE_TYPE_SHELL)  {
			if (Age < uShellLifetime) {
				gType = PARTICLE_TYPE_SHELL;
				gPosition = vPosition[0] + float(t2 - t1)/1000.0f * vel;
				gVelocity = vVelocity[0] + float(t2 - t1)/1000.0f * acc;
				gAge = Age;

				EmitVertex();
				EndPrimitive();
			}
			else {
				for (int i = 0; i < 10; i++) {
					vec3 Dir = GetRandomDir(uTime + uint(i));
					
					gType = PARTICLE_TYPE_SECONDARY_SHELL;
					gPosition = vPosition[0];
					gVelocity = normalize(Dir) / 20.0;
					gAge = uint(0);

					EmitVertex();
					EndPrimitive();
				}
			}
		}
		else {
			if (Age < uSecondaryShellLifetime) {
				gType = PARTICLE_TYPE_SECONDARY_SHELL;
				gPosition = vPosition[0] + float(t2 - t1)/1000.0f * vel;
				gVelocity = vVelocity[0] + float(t2 - t1)/1000.0f * acc;
				gAge = Age;
				
				EmitVertex();
				EndPrimitive();
			}
		}
	}*/
}