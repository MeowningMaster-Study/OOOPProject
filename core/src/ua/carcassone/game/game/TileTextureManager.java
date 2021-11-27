package ua.carcassone.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import ua.carcassone.game.Utils;

import java.util.LinkedList;
import java.util.List;

public class TileTextureManager {
    private final List<List<Texture>> textures = new LinkedList<>();
    private int minTileSize = new Texture(Gdx.files.internal("skin/classic-tiles/1-0.png")).getWidth();

    public TileTextureManager() {
        for (int i = 0; i <= 24; i++){
            List<Texture> tileTextures = new LinkedList<>();
            for (int j = 0; j <= 3; j++){
                Texture texture = new Texture(Gdx.files.internal("skin/classic-tiles/"+i+"-"+j+".png"));
                if (texture.getHeight() < minTileSize) minTileSize = texture.getHeight();
                if  (texture.getWidth() < minTileSize) minTileSize = texture.getWidth();
                tileTextures.add(j, texture);
            }
            textures.add(i, tileTextures);
        }
    }

    public Texture getTexture(int tileTypeId, int rotation){
        return textures.get(tileTypeId).get(rotation);
    }

    public Texture getTexture(TileType tileType, int rotation){
        return getTexture(Utils.getTileTypeId(tileType), rotation);
    }

    public Texture getTexture(Tile tile){
        return getTexture(tile.type, tile.rotation);
    }

    public int getMinTileSize() {
        return minTileSize;
    }
}
