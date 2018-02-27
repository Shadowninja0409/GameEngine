package enginetester;

import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TexturedModel;
import renderengine.DisplayManager;
import renderengine.Loader;
import renderengine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		float[] verticies = {
				-0.5f, 0.5f, 0,  //V0
				-0.5f, -0.5f, 0, //V1
				0.5f, -0.5f, 0,  //V2
				0.5f, 0.5f, 0,   //V3
		};
		
		int[] indices = {
				0,1,3, // top left triangle
				3,1,2  // bottom right triangle
		};
		
		float[] textureCoords = {
				0,0, //V0
				0,1, //V1
				1,1, //V2
				1,0  //V3
		};
		
		RawModel model = loader.loadToVAO(verticies, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("3004391"));
		TexturedModel texturedModel = new TexturedModel(model, texture); 
		
		while(!Display.isCloseRequested()){
			renderer.prepare();
			shader.start();
			//RawModel
			renderer.render(texturedModel);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
