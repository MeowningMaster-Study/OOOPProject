package ua.carcassone.game.game.sprites;

import com.badlogic.gdx.graphics.Texture;

public class TypeSprite extends Sprite{
    protected final SpriteType spriteType;

    public TypeSprite(Texture texture, float bottomStart, SpriteType spriteType) {
        super(texture, bottomStart);
        this.spriteType = spriteType;
    }

}
