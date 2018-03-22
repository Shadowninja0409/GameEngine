package Inventory;

import guis.GuiRenderer;
import guis.GuiTexture;
import item.Item;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import renderengine.Loader;

import java.util.ArrayList;

public class InventoryManager implements Runnable{


    private GuiTexture backGround;
    private ArrayList<Item> items;

    private GuiRenderer renderer;
    private Loader loader;
    private Thread thread;
    private boolean running = false;

    public InventoryManager(Loader loader, GuiRenderer renderer) {
        backGround = new GuiTexture(loader.loadTexture("Inventory"), new Vector2f(0f, 0f) , new Vector2f(0.75f, 0.75f));
        items = new ArrayList<>();
        this.renderer = renderer;
        this.loader = loader;
    }


    int selectedItem = 0;

    public boolean isOpened = false;
    public void toggleInventory(int key){
        while(Keyboard.next()){
            if(Keyboard.getEventKeyState()){
                if(Keyboard.getEventKey() == key)
                    isOpened = !isOpened;
            }
        }
    }

    public GuiTexture getBackGround() {
        return backGround;
    }

    public void tick(){

        if(isRight(Keyboard.KEY_A)){
            selectedItem++;
            System.out.println(selectedItem);
        }

        if(isLeft(Keyboard.KEY_D)){
            selectedItem--;
            System.out.println(selectedItem);
        }

        int len = items.size();

        if(items.size() == 0){
            renderer.render(new GuiTexture(loader.loadTexture(""), new Vector2f(0, 0), new Vector2f(.25f, .25f)));
            return;
        }

        for(int i = -4; i < 4; i++){

        }


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

    public boolean right = false;
    public boolean isRight(int key){
        if(Keyboard.isKeyDown(key) && !right){
            right = true;
            return true;
        }else
        if(!Keyboard.isKeyDown(key)){
            right = false;
            return false;
        }
        return false;
    }

    public boolean left = false;
    public boolean isLeft(int key){
        if(Keyboard.isKeyDown(key) && !left){
            left = true;
            return true;
        }else
        if(!Keyboard.isKeyDown(key)){
            left = false;
            return false;
        }
        return false;
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


}