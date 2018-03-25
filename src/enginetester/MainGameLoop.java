package enginetester;

import Inventory.InventoryManager;
import entities.*;
import font.TextMaster;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import guis.GuiRenderer;
import guis.GuiTexture;
import item.Boots;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderengine.*;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterShader;
import water.WaterTile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		TextMaster.init(loader);

		FontType font = new FontType(loader.loadTexture("fonts/sans"), new File("res/fonts/sans.fnt"));
		GUIText text = new GUIText("Empty", 1, font, new Vector2f(0.5f,0.5f), 0.5f, true);
		text.remove();

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
		
		MasterRenderer renderer = new MasterRenderer(loader);
		Random random = new Random(676452);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
		List<WaterTile> waterTiles = new ArrayList<>();
		waterTiles.add(new WaterTile(90, -30, 30));

		WaterFrameBuffers fbos = new WaterFrameBuffers();

		List<GuiTexture> guis = new ArrayList<>();
		GuiTexture lampPlacer = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(.25f, .25f));
		GuiTexture reflectionTest = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f) , new Vector2f(0.25f, 0.25f));
		guis.add(lampPlacer);
		guis.add(reflectionTest);

		Player player = new Player(person, new Vector3f(0, 40, 0), 0 ,180, 0, 0.6f);
		InventoryManager inventoryManager = new InventoryManager(loader, player);
		Camera camera = new Camera(player);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrains);

		Boots boots = new Boots(loader.loadTexture("boots"), new Vector2f(inventoryManager.invItemSpotX, inventoryManager.invItemSpotY), new Vector2f(inventoryManager.invItemScaleX, inventoryManager.invItemScaleY), "boots");

		for(int i = 0; i < 400; i++) {
			if(i % 1 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.placeEntity(terrains, x, z);
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0,
						random.nextFloat() * 360, 0, 0.9f));
			}
			if(i % 5 == 0){
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.placeEntity(terrains, x, z);
				entities.add(new Entity(pine, random.nextInt(4), new Vector3f(x, y, z), 0,
						random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.placeEntity(terrains, x, z);
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0,
						random.nextFloat() * 360, 0, random.nextFloat() * 1 + 1));
			}
		}

		entities.add(player);

		inventoryManager.prepareInventory();
		inventoryManager.setInventoryItem(4, boots);



		while(!Display.isCloseRequested()) {
			inventoryManager.start();
			if(!inventoryManager.toggleBools.get(0)){
				camera.move();
				player.move(terrains);
			}
			picker.update();

			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			fbos.bindReflectionFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera);
			fbos.unbindCurrentFrameBuffer();

			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			//inventoryManager.toggleButton(Keyboard.KEY_T, 1);
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if (inventoryManager.toggleBools.get(1)) {
				if (terrainPoint != null) {
					streetLamp.setPosition(terrainPoint);
					lampLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 15, terrainPoint.z));
					if (player.openInventory(Keyboard.KEY_E)) {
						entities.add(new Entity(lamp, terrainPoint, 0, 0, 0, 1));
						lights.add(new Light(new Vector3f(terrainPoint.x, terrainPoint.y + 15, terrainPoint.z), new Vector3f(1, 0, 0)));
					}
				}
			}
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

			renderer.renderScene(entities, terrains, lights, camera);
			waterRenderer.render(waterTiles, camera);

			guiRenderer.render(guis);
			if(inventoryManager.toggleBools.get(0)){
				inventoryManager.render(guiRenderer);
			}

			TextMaster.render();
			DisplayManager.updateDisplay();

		}

		TextMaster.cleanUp();
		fbos.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}


}
