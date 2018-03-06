package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 15, -10);
	//up/down
	private float ptich = 22.5f;
	//left/right
	private float yaw;
	//tilt
	private float roll;
	
	public Camera() {}
	
	public void move() {

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
