package item;

import entities.Entity;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Item extends Entity {

    private String name;

    public Item(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, String name) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.name = name;
    }

    public Item(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale, String name) {
        super(model, index, position, rotX, rotY, rotZ, scale);
        this.name = name;
    }

}
