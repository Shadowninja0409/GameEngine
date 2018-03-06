package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderengine.DisplayManager;

import java.security.Key;

public class Player extends Entity {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;
    private static final float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;

    private boolean isAirBorne = false;


    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(){
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float)(distance * Math.sin(Math.toRadians(getRotY())));
        float dz = (float)(distance * Math.cos(Math.toRadians(getRotY())));
        super.increasePosition(dx, 0, dz);
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(),0);
        if(super.getPosition().y <= TERRAIN_HEIGHT){
            upwardSpeed = 0;
            isAirBorne = false;
            super.getPosition().y = TERRAIN_HEIGHT;
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

}
