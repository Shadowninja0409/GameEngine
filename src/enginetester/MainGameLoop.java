package enginetester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderengine.*;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();

        List<Entity> entities = new ArrayList<>();

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

		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);
        Entity streetLamp = new Entity(lamp, new Vector3f(0,0,0), 0 , 0, 0 , 1);
        List<Light> lights = new ArrayList<>();
        Light sunLight = new Light(new Vector3f(0, 10000, -7000), new Vector3f(0.4f,0.4f,0.4f));
        Light lampLight = new Light(new Vector3f(0, 0, 0), new Vector3f(0,0,0));
        entities.add(streetLamp);
        lights.add(sunLight);
        lights.add(lampLight);


        List<Terrain> terrains = new ArrayList<>();
		Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);
		terrains.add(terrain2);

		TexturedModel person = new TexturedModel(OBJLoader.loadObjModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(person, new Vector3f(0, 0, 0), 0 ,180, 0, 0.6f);
		
		Camera camera = new Camera(player);
		
		MasterRenderer renderer = new MasterRenderer(loader);
		Random random = new Random(676452);

		List<GuiTexture> guis = new ArrayList<>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(.25f, .25f));
		guis.add(gui);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrains);

		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
		List<WaterTile> waterTiles = new ArrayList<>();
		waterTiles.add(new WaterTile(-75, -75, 0));


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
			player.move(terrains);
			picker.update();
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if (camera.renderGui) {
				if (terrainPoint != null) {
					streetLamp.setPosition(terrainPoint);
					lampLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 15, terrainPoint.z));
					if (player.isPlacing(Keyboard.KEY_E)) {
						entities.add(new Entity(lamp, terrainPoint, 0, 0, 0, 1));
						lights.add(new Light(new Vector3f(terrainPoint.x, terrainPoint.y + 15, terrainPoint.z), new Vector3f(1, 0, 0)));
					}
				}
			}
			renderer.renderScene(entities, terrains, lights, camera);
			waterRenderer.render(waterTiles, camera);
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
