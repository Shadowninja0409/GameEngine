package item;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class Boots extends Item {

    public Boots(int texture, Vector2f position, Vector2f scale, String name) {
        super(texture, position, scale, name);
        stats.add(1.0);
        bools.add(true);
    }
}
