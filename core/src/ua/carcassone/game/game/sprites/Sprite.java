package ua.carcassone.game.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Sprite {
    protected final Texture texture;
    protected final float bottomStart;

    public Sprite(Texture texture, float bottomStart){
        this.texture = texture;
        this.bottomStart = bottomStart;
    }

    public Image getImage(){
        Image image = new Image(this.texture);
        image.setHeight(this.texture.getHeight());
        image.setWidth(this.texture.getWidth());
        return image;
    }

    public float getBottomStart() {
        return bottomStart;
    }
}
