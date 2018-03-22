package entities;

import Inventory.InventoryManager;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderengine.DisplayManager;
import terrains.Terrain;

import java.util.List;

public class Player extends Entity {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;

    private boolean isAirBorne = false;
    private InventoryManager inventoryManager;


    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, InventoryManager inventoryManager) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.inventoryManager = inventoryManager;
    }

    public void move(List<Terrain> terrains){
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float)(distance * Math.sin(Math.toRadians(getRotY())));
        float dz = (float)(distance * Math.cos(Math.toRadians(getRotY())));
        super.increasePosition(dx, 0, dz);
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(),0);
        for(int i = 0; i < terrains.size(); i++){
            if(getPosition().x >= terrains.get(i).getX() && getPosition().x <= terrains.get(i).getX() + Terrain.SIZE) {
                if (getPosition().z >= terrains.get(i).getZ() && getPosition().z <= terrains.get(i).getZ() + Terrain.SIZE) {
                    float terrainHeight = terrains.get(i).getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
                    if (super.getPosition().y <= terrainHeight) {
                        upwardSpeed = 0;
                        isAirBorne = false;
                        super.getPosition().y = terrainHeight;
                    }
                } else super.getPosition().y = 0;
            }
        }

    }

    private void jump(){
        if(!isAirBorne){
            this.upwardSpeed = JUMP_POWER;
            isAirBorne = true;
        }
    }

    public void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = RUN_SPEED;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED;
        } else
            this.currentSpeed = 0;

        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        } else
            this.currentTurnSpeed = 0;

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }
    }

    public boolean isPressed = false;
    public boolean openInventory(int key){
        if(Keyboard.isKeyDown(key) && !isPressed){
            isPressed = true;
            return true;
        }else
        if(!Keyboard.isKeyDown(key)){
            isPressed = false;
            return false;
        }
        return false;
    }

    public InventoryManager getInventoryManager(){
        return inventoryManager;
    }

}
