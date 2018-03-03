package enginetester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderengine.DisplayManager;
import renderengine.Loader;
import renderengine.MasterRenderer;
import renderengine.OBJLoader;
import renderengine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white"))); 
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-15),0,0,0,1);
		Light light = new Light(new Vector3f(0,0,-10), new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			camera.move();
			entity.increaseRotation(0, 1, 0);
			renderer.processEntity(entity);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
