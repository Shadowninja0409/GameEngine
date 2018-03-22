package font;

public class FontRenderer  {

    private FontShader shader;

    public FontRenderer(){
        shader = new FontShader();
    }

    public void cleanUp(){
        shader.cleanUp();
    }

    public void prepare(){}

    public void render(GUIText text){

    }

    public void endRenderering(){

    }
}
