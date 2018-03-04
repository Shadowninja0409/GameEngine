package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0,4,0);
	//up/down
	private float ptich;
	//left/right
	private float yaw;
	//tilt
	private float roll;
	
	public Camera() {}
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= 0.05f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += 0.05f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x -= 0.1f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x += 0.1f;
		}
	}
	
	//Getters
	public Vector3f getPosition() {
		return position;
	}
	public float getPtich() {
		return ptich;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
}
