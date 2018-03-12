package enginetester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import renderengine.*;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();

        List<Entity> entities = new ArrayList<>();
        List<Terrain> terrains = new ArrayList<>();

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		ModelTexture pineAtlas = new ModelTexture(loader.loadTexture("pine"));
		TexturedModel pine = new TexturedModel(OBJLoader.loadObjModel("pine", loader), pineAtlas);
		pine.getTexture().setHasTransparency(true);
		pine.getTexture().setUseFakeLighting(true);

		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTextureAtlas);
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(true);
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("flower")));
		tree.getTexture().setHasTransparency(true);

		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f,0.4f,0.4f)));
		lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));

		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);
        entities.add(new Entity(lamp, new Vector3f(185,-4.7f,-293), 0 , 0, 0 , 1));
        entities.add(new Entity(lamp, new Vector3f(370,-4.2f,-300), 0 , 0, 0 , 1));
        entities.add(new Entity(lamp, new Vector3f(293,-6.8f,-305), 0 , 0, 0 , 1));


		Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain3 = new Terrain(0,0, loader, texturePack, blendMap, "heightmap");
		Terrain terrain4 = new Terrain(-1,0, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);

		TexturedModel person = new TexturedModel(OBJLoader.loadObjModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(person, new Vector3f(0, 0, 0), 0 ,180, 0, 0.6f);
		
		Camera camera = new Camera(player);
		
		MasterRenderer renderer = new MasterRenderer(loader);
		Random random = new Random(676452);

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(.25f, .25f));
		guis.add(gui);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

		for(int i = 0; i < 400; i++) {
			if(i % 1 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0,
						random.nextFloat() * 360, 0, 0.9f));
			}
			if(i % 5 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(pine, random.nextInt(4), new Vector3f(x, y, z), 0,
						random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0,
						random.nextFloat() * 360, 0, random.nextFloat() * 1 + 1));
			}
		}

		entities.add(player);

		while(!Display.isCloseRequested()){
			camera.move();

			player.move(Terrain.getTerrain(terrains, player.getPosition().x, player.getPosition().z));
			picker.update();
			System.out.println(picker.getCurrentRay());
			for(Terrain terrainL: terrains){
				renderer.processTerrain(terrainL);
			}
			for(Entity entity: entities) {
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);
			camera.toggleGui(Keyboard.KEY_TAB);
			if(camera.renderGui){
				guiRenderer.render(guis);
			}
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
