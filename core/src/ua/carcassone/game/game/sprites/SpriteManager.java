package ua.carcassone.game.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.*;

public class SpriteManager {
    private Map<SpriteType, List<TypeSprite>> availableSprites;

    public SpriteManager() {
        availableSprites = new HashMap<>();
        setValue(SpriteType.FIELD,
                Arrays.asList(
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/cactus1.png", 0.07f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/cactus2.png", 0.10f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/cactus3.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/cactus4.png", 0.03f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/plant1.png", 0.20f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/plant2.png", 0.13f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/plant3.png", 0.01f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/plant4.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/plant5.png", 0.08f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/skull.png", 0.20f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/stone1.png", 0.35f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/stone2.png", 0.01f)
                        )
                );
    }

    public List<TypeSprite> getAvailableSprites(SpriteType spriteType){
        if(!availableSprites.containsKey(spriteType))
            return new ArrayList<>();
        return new ArrayList<>(availableSprites.get(spriteType));
    }

    private void setValue(SpriteType spriteType, List<AbstractMap.SimpleEntry<String, Float>> textures){
        List<TypeSprite> sprites = new ArrayList<>();

        for (AbstractMap.SimpleEntry<String, Float> pair : textures) {
            sprites.add(new TypeSprite(
                    new Texture(Gdx.files.internal(pair.getKey())),
                    pair.getValue(),
                    spriteType
            ));
        }

        availableSprites.put(spriteType, sprites);
    }
}
