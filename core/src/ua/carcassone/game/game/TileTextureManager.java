package ua.carcassone.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import ua.carcassone.game.Utils;

import java.util.LinkedList;
import java.util.List;

public class TileTextureManager {
    private final List<List<Texture>> textures = new LinkedList<>();
    private int minTileSize = new Texture(Gdx.files.internal("skins/classic-tiles/1-0.png")).getWidth();
    private Texture borderTexture, innerBorderTexture, rotateClockwiseTexture, rotateAnticlockwiseTexture;

    public TileTextureManager() {
        for (int i = 0; i <= 24; i++){
            List<Texture> tileTextures = new LinkedList<>();
            for (int j = 0; j <= 3; j++){
                Texture texture = new Texture(Gdx.files.internal("skins/classic-tiles/"+i+"-"+j+".png"));
                if (texture.getHeight() < minTileSize) minTileSize = texture.getHeight();
                if  (texture.getWidth() < minTileSize) minTileSize = texture.getWidth();
                tileTextures.add(j, texture);
            }
            textures.add(i, tileTextures);
        }
        this.borderTexture = new Texture(Gdx.files.internal("skins/icons/tile-border.png"));
        this.innerBorderTexture = new Texture(Gdx.files.internal("skins/icons/tile-inner-border.png"));
        this.rotateClockwiseTexture = new Texture(Gdx.files.internal("skins/icons/clockwiseRotate.png"));
        this.rotateAnticlockwiseTexture = new Texture(Gdx.files.internal("skins/icons/left.png"));
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

    public Texture getInnerBorderTexture() {
        return innerBorderTexture;
    }

    public Texture getBorderTexture() {
        return borderTexture;
    }

    public Texture getRotateAnticlockwiseTexture() {
        return rotateAnticlockwiseTexture;
    }

    public Texture getRotateClockwiseTexture() {
        return rotateClockwiseTexture;
    }
}
