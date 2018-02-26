package enginetester;

import org.lwjgl.opengl.Display;

import renderengine.DisplayManager;
import renderengine.Loader;
import renderengine.RawModel;
import renderengine.Renderer;
import shaders.StaticShader;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		float[] verticies = {
				-0.5f, 0.5f, 0,
				-0.5f, -0.5f, 0,
				
				0.5f, -0.5f, 0,
				0.5f, 0.5f, 0,
		};
		
		int[] indices = {
				0,1,3,
				3,1,2
		};
		
		RawModel model = loader.loadToVAO(verticies, indices);
		
		while(!Display.isCloseRequested()){
			renderer.prepare();
			shader.start();
			//RawModel
			renderer.render(model);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
