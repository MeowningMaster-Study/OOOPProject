package ua.carcassone.game.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ua.carcassone.game.Utils;
import ua.carcassone.game.game.TileType;
import ua.carcassone.game.game.TileTypes;

import java.util.*;

public class SpriteManager {
    private final Map<SpriteType, List<TypeSprite>> availableSprites;
    private final Map<SpriteType, List<TypeSprite>> mandatorySpritesUnfinished;
    private final Map<SpriteType, List<TypeSprite>> mandatorySpritesFinished;

    public SpriteManager() {
        availableSprites = new HashMap<>();
        mandatorySpritesUnfinished = new HashMap<>();
        mandatorySpritesFinished = new HashMap<>();
        setValue(
                availableSprites,
                SpriteType.FIELD,
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

        setValue(
                availableSprites,
                SpriteType.TOWN,
                Arrays.asList(
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

        setValue(
                mandatorySpritesUnfinished,
                SpriteType.MONASTERY,
                Arrays.asList(
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/Mandatory/ranch1.png", 0.03f)
                )
        );

        setValue(
                mandatorySpritesFinished,
                SpriteType.MONASTERY,
                Arrays.asList(
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/Mandatory/ranch2.png", 0.03f)
                )
        );

        setValue(
                mandatorySpritesUnfinished,
                SpriteType.SHIELD,
                Arrays.asList(
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/bank.png", 0.05f)
                )
        );

        setValue(
                mandatorySpritesFinished,
                SpriteType.SHIELD,
                Arrays.asList(
                        new AbstractMap.SimpleEntry<>("skins/decorations/trimmed/TOWN/bank.png", 0.05f)
                )
        );



    }

    public List<TypeSprite> getAvailableSprites(SpriteType spriteType){
        if(!availableSprites.containsKey(spriteType))
            return new ArrayList<>();
        return new ArrayList<>(availableSprites.get(spriteType));
    }

    private void setValue(Map<SpriteType, List<TypeSprite>> map, SpriteType spriteType, List<AbstractMap.SimpleEntry<String, Float>> textures){
        List<TypeSprite> sprites = new ArrayList<>();

        for (AbstractMap.SimpleEntry<String, Float> pair : textures) {
            sprites.add(new TypeSprite(
                    new Texture(Gdx.files.internal(pair.getKey())),
                    pair.getValue(),
                    spriteType
            ));
        }

        map.put(spriteType, sprites);
    }

    public List<TypeSprite> getMandatorySprites(SpriteType spriteType, boolean finished){
        if(finished){
            if(!mandatorySpritesFinished.containsKey(spriteType))
                return new ArrayList<>();
            return new ArrayList<>(mandatorySpritesFinished.get(spriteType));
        } else {
            if(!mandatorySpritesUnfinished.containsKey(spriteType))
                return new ArrayList<>();
            return new ArrayList<>(mandatorySpritesUnfinished.get(spriteType));
        }
    }

    public Vector2 getMandatorySpritePosition(TileType tileType, int rotation){
        if(tileType == TileTypes.get(1))
            return new Vector2(0.5f, 0.5f);
        if(tileType == TileTypes.get(2))
            return new Vector2(0.5f, 0.5f);
        if (tileType == TileTypes.get(3))
            return new Vector2(0.5f, 0.5f);
        if (tileType == TileTypes.get(5))
            return new Vector2(0.5f, 0.6f);
        if (tileType == TileTypes.get(7))
            return new Vector2(0.5f, 0.6f);
        if (tileType == TileTypes.get(9))
            return new Vector2(0.25f, 0.75f);
        if (tileType == TileTypes.get(11))
            return new Vector2(0.25f, 0.75f);
        if (tileType == TileTypes.get(13))
            return new Vector2(0.5f, 0.5f);
        else
            return Utils.rotatedIn1_1(new Vector2(0.5f, 0.5f), rotation);
    }

}
