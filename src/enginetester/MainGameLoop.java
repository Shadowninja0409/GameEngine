package enginetester;

import org.lwjgl.opengl.Display;

import renderengine.DisplayManager;
import renderengine.Loader;
import renderengine.RawModel;
import renderengine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		float[] verticies = {
				-0.5f, 0.5f, 0,
				-0.5f, -0.5f, 0,
				0.5f, -0.5f, 0,
				
				0.5f, -0.5f, 0,
				0.5f, 0.5f, 0,
				-0.5f, 0.5f, 0,
		};
		
		RawModel model = loader.loadToVAO(verticies);
		
		while(!Display.isCloseRequested()){
			renderer.prepare();

			//RawModel
			
			renderer.render(model);
			DisplayManager.updateDisplay();
		}

		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
