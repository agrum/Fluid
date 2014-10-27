
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class Window {
	// Entry point for the application
	public static void main(String[] args) {
		new Window();
	}
	
	// Setup variables
	private final String WINDOW_TITLE = "SPH";
	private final int WIDTH = 1000;
	private final int HEIGHT = 600;
	
	private SPH m_sph = null;
	private Eye m_eye = null;
	
	public Window() {
		// Initialize OpenGL (Display)
		this.setupOpenGL();		
		this.setupSPH();
		this.setupMatrices();
		
		while (!Display.isCloseRequested()) {
			// Do a single loop (logic/render)
			this.loopCycle();
			
			// Force a maximum FPS of about 60
			Display.sync(60);
			// Let the CPU synchronize with the GPU if GPU is tagging behind
			Display.update();
		}
		
		// Destroy OpenGL (Display)
		this.destroyOpenGL();
	}

	private void setupOpenGL() {
		// Setup an OpenGL context with API version 3.2
		try {
			PixelFormat pixelFormat = new PixelFormat();
			ContextAttribs contextAtrributes = new ContextAttribs(4, 3)
				.withForwardCompatible(true)
				.withProfileCore(true);
			
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(WINDOW_TITLE);
			Display.create(pixelFormat, contextAtrributes);
			
			GL11.glViewport(0, 0, WIDTH, HEIGHT);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Setup an XNA like background color
		GL11.glClearColor(0f, 0f, 0f, 0f);
		
		// Map the internal OpenGL coordinate system to the entire screen
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		this.exitOnGLError("setupOpenGL");
	}
	
	private void setupSPH() {
		m_sph = new SPH();
		m_sph.initialize();
		m_sph.setViewportSize(WIDTH, HEIGHT);
		
		this.exitOnGLError("setupSPH");
	}

	private void setupMatrices() {
		m_eye = m_sph.eye();
		
		m_eye.setFOVVertical(60.0f);
		m_eye.setAspectRatio((float)WIDTH / (float)HEIGHT);
		m_eye.setNearVisibility(0.1f);
		m_eye.setFarVisibility(100.0f);
		
		m_eye.lookAt(
				new Vector3f(16, 16, -30),
				new Vector3f(16, 16, 16),
				new Vector3f(0, 1, 0));
	}
	
	private void logicCycle() {
		//-- Input processing
		float rotationDelta = 0.1f;
		float posDelta = 0.1f;
		
		while(Keyboard.next()) {			
			// Only listen to events where the key was pressed (down event)
			if (!Keyboard.getEventKeyState()) continue;
			
			// Change model scale, rotation and translation values
			switch (Keyboard.getEventKey()) {
			// Move
			case Keyboard.KEY_W:
				m_eye.move(0, 0, posDelta);
				break;
			case Keyboard.KEY_S:
				m_eye.move(0, 0, -posDelta);
				break;
			// Scale
			case Keyboard.KEY_A:
				m_eye.move(posDelta, 0, 0);
				break;
			case Keyboard.KEY_D:
				m_eye.move(-posDelta, 0, 0);
				break;
			// Rotation
			case Keyboard.KEY_UP:
				m_eye.lookUp(-rotationDelta);
				break;
			case Keyboard.KEY_DOWN:
				m_eye.lookUp(rotationDelta);
				break;
			case Keyboard.KEY_LEFT:
				m_eye.lookLeft(rotationDelta);
				break;
			case Keyboard.KEY_RIGHT:
				m_eye.lookLeft(-rotationDelta);
				break;
			}
		}
		
		this.exitOnGLError("logicCycle");
	}
	
	private void renderCycle() {
		m_sph.render();
		
		this.exitOnGLError("renderCycle");
	}
	
	private void loopCycle() {
		// Update logic
		this.logicCycle();
		// Update rendered frame
		this.renderCycle();
		
		this.exitOnGLError("loopCycle");
	}
	
	private void destroyOpenGL() {	
		m_sph = null;
		m_eye = null;
		
		this.exitOnGLError("destroyOpenGL");
		
		Display.destroy();
	}
	
	private void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}
	}
}
