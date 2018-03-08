package entities;

import guis.GuiTexture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import java.security.Key;
import java.util.List;

import static com.sun.javafx.util.Utils.clamp;

public class Camera {

	private final float DEFAULT_DISTANCE_FROM_PLAYER = 14;
	private final float DEFAULT_PITCH = 30;
	private final float DEFAULT_ROLL = 0;
	private final float DEFAULT_YAW = 0;

	private final float MAX_PITCH = 80;
	private final float MIN_PITCH = 0;

	private final float MAX_DISTANCE_FROM_PLAYER = 30;
	private final float MIN_DISTANCE_FROM_PLAYER = 5;

	private final float VERTICAL_OFFSET = 2;

	private float distanceFromPlayer = DEFAULT_DISTANCE_FROM_PLAYER;
	private float angleAroundPlayer = 0;

	private Vector3f position = new Vector3f(0, 0, 0);
	//up/down
	private float pitch = DEFAULT_PITCH;
	//left/right
	private float yaw = DEFAULT_YAW;
	//tilt
	private float roll = DEFAULT_ROLL;

	private Player player;
	public boolean renderGui = false;


	
	public Camera(Player player) {
		this.player = player;
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance() + VERTICAL_OFFSET;
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = calculateYaw();
		resetCameraPosition();
		System.out.println("distanceFromPlayer: " + distanceFromPlayer + " pitch:" + pitch);
	}

    public void toggleGui(int key){
	    while(Keyboard.next()){
	        if(Keyboard.getEventKeyState()){
	            if(Keyboard.getEventKey() == key)
	                renderGui = !renderGui;
            }
        }
    }

	private void calculateCameraPosition(float horizDistance, float verticDistance){
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float)(horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float)(horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance;
	}

	private float calculateHorizontalDistance(){
		return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance(){
		return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.05f;
		distanceFromPlayer -= zoomLevel;
        distanceFromPlayer = clamp(MIN_DISTANCE_FROM_PLAYER, distanceFromPlayer, MAX_DISTANCE_FROM_PLAYER);
	}

	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
        pitch = clamp(MIN_PITCH, pitch, MAX_PITCH);
	}

	private float calculateYaw(){
		return 180 - (player.getRotY() + angleAroundPlayer);
	}

	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

	private void resetCameraPosition(){
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			angleAroundPlayer = 0;
			yaw = calculateYaw();
			distanceFromPlayer = DEFAULT_DISTANCE_FROM_PLAYER;
			pitch = DEFAULT_PITCH;
			roll = DEFAULT_ROLL;
		}
	}
	
	//Getters
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
}
