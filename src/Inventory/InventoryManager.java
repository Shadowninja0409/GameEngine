package Inventory;

import entities.Player;
import font.TextMaster;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import guis.GuiRenderer;
import guis.GuiTexture;
import item.Boots;
import item.Item;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import renderengine.Loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager implements Runnable{


    private GuiTexture backGround;
    private ArrayList<Item> items;

    private Loader loader;
    private Player player;

    private Thread thread;
    private boolean running = false;
    private GUIText defaultT;
    private FontType font;

    private float invCenterX = 0.37f;
    private float invTopY = 0.364f;
    private float lineSpacing = 0.05f;

    public float invItemSpotX = 0.3735f;
    public float invItemSpotY = -0.189f;
    public float invItemScaleX = 0.08f;
    public float invItemScaleY = 0.12f;

    int selectedItem = 0;

    public List<Boolean> toggleBools;
    public List<Boolean> pressBools;
    public List<Boolean> equipBools;

    public InventoryManager(Loader loader, Player player) {
        backGround = new GuiTexture(loader.loadTexture("Inventory"), new Vector2f(0f, 0f) , new Vector2f(0.75f, 0.75f));
        items = new ArrayList<>();
        this.loader = loader;
        this.player = player;
        font = new FontType(loader.loadTexture("fonts/sans"), new File("res/fonts/sans.fnt"));
        toggleBools = new ArrayList<>();
        pressBools = new ArrayList<>();
        equipBools = new ArrayList<>();
    }

    public void buttons(){
        toggleButton(Keyboard.KEY_TAB, 0);
        open(Keyboard.KEY_O, 2);
        close(Keyboard.KEY_P, 2);
    }

    public void tick(){
        buttons();
        itemEquip(selectedItem);

        if(toggleBools.get(0)){

            if(keyPress(Keyboard.KEY_D, 0) && selectedItem < 8){
                selectedItem++;
                updateInventory();
            }
            if(keyPress(Keyboard.KEY_A, 1) && selectedItem > 0){
                selectedItem--;
                updateInventory();
            }

        }

        if(!toggleBools.get(0)){
            updateInventory();
        }

    }

    public boolean rendered = false;
    public void render(GuiRenderer renderer){
        renderer.render(getBackGround());
        renderer.render(getSelectedItem());
        for(int i = 0; i < items.size(); i++) {
            if(i == selectedItem){
                keyPress(Keyboard.KEY_G, 5);
                keyPress(Keyboard.KEY_H, 4);
                if(pressBools.get(4))
                    getBools(selectedItem).set(0, false);
                if(pressBools.get(5))
                    getBools(selectedItem).set(0, true);
            }
        }
        textRender();
    }

    public void textRender(){
        if(!rendered){
            for(int i = 0; i < items.size(); i++) {
                if (i == selectedItem) {
                    TextMaster.loadText(new GUIText( "> " + items.get(selectedItem).getName() + " <", 1, font, new Vector2f(invCenterX, invTopY + (i * lineSpacing)), .5f, false));
                } else
                    TextMaster.loadText(new GUIText(items.get(i).getName(), 1, font, new Vector2f(invCenterX, invTopY + (i * lineSpacing)), .5f, false));
            }
        }
        rendered = true;
    }
    public void prepareInventory(){
        for(int i = 0; i < 20; i++){
            toggleBools.add(false);
        }
        for(int i = 0; i < 20; i++){
            pressBools.add(false);
        }
        for(int i = 0; i < 20; i++){
            equipBools.add(false);
        }
        for(int i = 0; i < 9; i++){
            addInventoryItem(i, new Boots(loader.loadTexture("white"), new Vector2f(0f,0f), new Vector2f(0f,0f), "Empty"));
        }
    }

    public void open(int key, int index) {
        if (Keyboard.isKeyDown(key)) {
            pressBools.set(index, true);
        }
    }
    public void close(int key, int index) {
        if (Keyboard.isKeyDown(key)) {
            pressBools.set(index, false);
        }
    }

    public Item getSelectedItem() {
        return items.get(selectedItem);
    }
    public void updateInventory(){
        TextMaster.clear();
        rendered = false;
    }
    public void addInventoryItem(int index, Item item){
        items.add(index, item);
    }
    public void setInventoryItem(int index, Item item){
        items.set(index, item);
    }

    public List<Boolean> getBools(int index){ return items.get(index).getBools(); }
    public void toggleButton(int key, int index){
        while(Keyboard.next()){
            if(Keyboard.getEventKeyState()){
                if(Keyboard.getEventKey() == key)
                    toggleBools.set(index, !toggleBools.get(index));
            }
        }
    }
    public boolean keyPress(int key, int index){
        if(Keyboard.isKeyDown(key) && !pressBools.get(index)){
            pressBools.set(index, true);
            return true;
        }else
        if(!Keyboard.isKeyDown(key)){
            pressBools.set(index, false);
            return false;
        }
        return false;
    }

    public void equip(int index){
        if(!equipBools.get(index)){
            equipBools.set(index, true);
            player.setSpeed(player.getSpeed() + items.get(index).getStats().get(0));
        }
    }
    public void deEquip(int index){
        if(equipBools.get(index)){
            equipBools.set(index, false);
            player.setSpeed(player.getSpeed() - items.get(index).getStats().get(0));
        }
    }
    public void itemEquip(int index){
        if(getBools(index).get(0)){
            equip(index);
        } else deEquip(index);
    }

    @Override
    public void run() {
        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;


        while(running){
            //Creates FPS
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;
            if(delta >= 1){
                tick();
                ticks++;
                delta--;
            }
            if(timer >= 1000000000){
                System.out.println("Ticks and Frames: " + ticks);
                ticks = 0;
                timer = 0;
            }
        }
        stop();
    }

    public synchronized void start(){
        if(running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    public synchronized void stop(){
        if(!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public GuiTexture getBackGround() {
        return backGround;
    }


}