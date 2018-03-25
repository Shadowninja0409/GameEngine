package item;

import entities.Entity;
import guis.GuiTexture;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class Item extends GuiTexture {

    protected String name;
    protected List<Float> stats;
    protected List<Boolean> bools;

    public Item(int texture, Vector2f position, Vector2f scale, String name) {
        super(texture, position, scale);
        this.name = name;
        stats = new ArrayList<>(9);
        bools = new ArrayList<>(9);
        for(int i = 0; i < 9; i++){
            stats.add(i, 0.0f);
        }
        for(int i = 0; i < 9; i++){
            bools.add(i, false);
        }
    }

    public String getName(){
        return name;
    }

    public List<Float> getStats(){
        return stats;
    }

    public List<Boolean> getBools(){
        return bools;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStats(List<Float> stats) {
        this.stats = stats;
    }

    public void setBools(List<Boolean> bools) {
        this.bools = bools;
    }
}
