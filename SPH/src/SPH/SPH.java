package SPH;

import org.lwjgl.util.vector.Vector3f;

import SPH.pass.SPHPass;
import lemon.camera.Eye;
import lemon.window.Window;

public class SPH {
	private Window window;
	private SPHPass globalPass;
	
	public static void main(String[] args) {
		SPH surf = new SPH();
		
		surf.exec();
	}
	
	public SPH()
	{
		window = new Window();
		window.create();
		
		globalPass = new SPHPass();
		globalPass.initialize();
		globalPass.setViewportSize(1000, 600);
		
		Eye eye = globalPass.eye();
		
		eye.setFOVVertical(60.0f);
		eye.setAspectRatio((float)1000 / (float)600);
		eye.setNearVisibility(0.1f);
		eye.setFarVisibility(100.0f);
		
		eye.lookAt(
				new Vector3f(16, 16, -30),
				new Vector3f(16, 16, 16),
				new Vector3f(0, 1, 0));
	}
	
	public void exec()
	{
		window.exec(globalPass);
	}
}