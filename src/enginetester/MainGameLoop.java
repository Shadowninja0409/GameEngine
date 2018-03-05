package enginetester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import renderengine.DisplayManager;
import renderengine.Loader;
import renderengine.MasterRenderer;
import renderengine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture"))); 
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(true);
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
		tree.getTexture().setHasTransparency(true);
		
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain3 = new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain4 = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();

		for(int i = 0; i < 500; i++) {
			entities.add(new Entity(tree, new Vector3f(random.nextFloat() * 800 - 400, 0, 
					random.nextFloat() * -600), 0, 0, 0, 3));
			entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, 
					random.nextFloat() * -600), 0, 0, 0, 1));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, 
					random.nextFloat() * -600), 0, 0, 0, 0.6f));
		}
		
		while(!Display.isCloseRequested()){
			camera.move();
			renderer.ProcessTerrain(terrain);
			renderer.ProcessTerrain(terrain2);
			renderer.ProcessTerrain(terrain3);
			renderer.ProcessTerrain(terrain4);
			for(Entity entity: entities) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
