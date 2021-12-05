package ua.carcassone.game.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ua.carcassone.game.Utils;

public class PointTypeSprite extends TypeSprite {
    protected float x, y;

    public PointTypeSprite(Texture texture, float bottomStart, SpriteType spriteType, float x, float y) {
        super(texture, bottomStart, spriteType);
        this.x = x;
        this.y = y;
    }

    public Image getImage(int rotations, float relativeSize) {
        Vector2 rotatedPoint = Utils.rotatedIn1_1(new Vector2(x, y), rotations);

        Image image = super.getImage();
        image.setPosition(
                relativeSize*(rotatedPoint.x)-this.texture.getWidth()/2.0f,
                relativeSize*(rotatedPoint.y)-this.texture.getHeight()*this.bottomStart
        );
        return image;
    }

    public Image getImage(int rotations, float relativeSize, float sizeMultiplier) {
        Vector2 rotatedPoint = Utils.rotatedIn1_1(new Vector2(x, y), rotations);

        Image image = super.getImage();
        image.setSize(image.getWidth()*sizeMultiplier, image.getHeight()*sizeMultiplier);
        image.setPosition(
                relativeSize*(rotatedPoint.x)-image.getWidth()/2.0f,
                relativeSize*(rotatedPoint.y)-image.getHeight()*this.bottomStart
        );
        return image;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public Image getImage() {
        return getImage(0, 1);
    }
}
