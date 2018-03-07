package guis;

import guis.GuiTexture;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import renderengine.Loader;

import java.util.List;

public class GuiRenderer {

    private final RawModel quad ;
    private GuiShader shader;

    public GuiRenderer(Loader loader){
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions);
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guis){
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        for(GuiTexture gui: guis){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp(){
        shader.cleanUp();
    }

}
