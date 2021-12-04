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
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/cactus1.png", 0.07f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/cactus2.png", 0.10f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/cactus3.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/cactus4.png", 0.03f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/plant1.png", 0.20f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/plant2.png", 0.13f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/plant3.png", 0.01f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/plant4.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/plant5.png", 0.08f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/skull.png", 0.20f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/stone1.png", 0.35f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/FIELD/stone2.png", 0.01f)
                        )
                );

        setValue(SpriteType.TOWN,
                Arrays.asList(
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/bank.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/hotel1.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/store1.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/store2.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/store3.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/store4.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/store5.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/store6.png", 0.05f),
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/store7.png", 0.05f)
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
