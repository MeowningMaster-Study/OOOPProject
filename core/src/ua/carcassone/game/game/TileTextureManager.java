package ua.carcassone.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import ua.carcassone.game.Settings;
import ua.carcassone.game.Utils;

import java.util.LinkedList;
import java.util.List;

public class TileTextureManager {
    private final List<List<Texture>> textures = new LinkedList<>();
    private int minTileSize = new Texture(Gdx.files.internal("skins/wildwest-tiles/1-0.png")).getWidth();
    private final Texture borderTexture, innerBorderTexture, borderSilverTexture, borderWhiteTexture, pointTexture;
    private final Texture pointDarkerTexture, pointDarkTexture, meepleTexture;
    private final List<Texture> rotateClockwiseTextures = new LinkedList<>();
    private final Texture transparentTexture;

    public TileTextureManager() {
        for (int i = 0; i <= 24; i++){
            List<Texture> tileTextures = new LinkedList<>();
            for (int j = 0; j <= 3; j++){
                Texture texture = new Texture(Gdx.files.internal("skins/wildwest-tiles/"+i+"-"+j+".png"));
                if (texture.getHeight() < minTileSize) minTileSize = texture.getHeight();
                if  (texture.getWidth() < minTileSize) minTileSize = texture.getWidth();
                tileTextures.add(j, texture);
            }
            textures.add(i, tileTextures);
        }
        this.borderTexture = new Texture(Gdx.files.internal("skins/icons/tile-border.png"));
        this.innerBorderTexture = new Texture(Gdx.files.internal("skins/icons/tile-inner-border.png"));
        this.borderSilverTexture = new Texture(Gdx.files.internal("skins/icons/tile-border-silver.png"));
        this.borderWhiteTexture = new Texture(Gdx.files.internal("skins/icons/tile-border-white.png"));
        this.transparentTexture = new Texture(Gdx.files.internal("skins/icons/transparent.png"));
        this.pointTexture = new Texture(Gdx.files.internal("skins/icons/blue-point.png"));
        this.pointDarkerTexture = new Texture(Gdx.files.internal("skins/icons/blue-point-darker.png"));
        this.pointDarkTexture = new Texture(Gdx.files.internal("skins/icons/blue-point-dark.png"));
        this.meepleTexture = new Texture(Gdx.files.internal("skins/meeples/meeple-greenscreen.png"));

        for (int i = 0; i < 4; i++) {
            this.rotateClockwiseTextures.add(new Texture(Gdx.files.internal("skins/icons/clockwiseRotate-"+i+".png")));
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

    public Texture getInnerBorderTexture() {
        return innerBorderTexture;
    }

    public Texture getBorderTexture() {
        return borderTexture;
    }

    public Texture getBorderSilverTexture() {
        return borderSilverTexture;
    }

    public Texture getBorderWhiteTexture() {
        return borderWhiteTexture;
    }

    public Texture getTransparentTexture() {
        return transparentTexture;
    }

    public Texture getPointTexture() {
        return pointTexture;
    }

    public Texture getPointDarkerTexture() {
        return pointDarkerTexture;
    }

    public Texture getPointDarkTexture() {
        return pointDarkTexture;
    }

    public Texture getRotateClockwiseTexture(int rotation) {
        return rotateClockwiseTextures.get(rotation%4);
    }

    public Texture getMeepleTexture() {
        return meepleTexture;
    }

    public Texture getMeepleTexture(Color color) {
        return TileTextureManager.fillTexture(meepleTexture, Settings.meepleGreenscreenColor, color);
    }

    public static Texture fillTexture(Texture source, Color colorReplaced, Color newColor){
        TextureData textureData = source.getTextureData();
        textureData.prepare();
        Pixmap pixmap = textureData.consumePixmap();

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {

                Color color = new Color();
                Color.rgba8888ToColor(color, pixmap.getPixel(x, y));



                if (color.r == colorReplaced.r && color.g == colorReplaced.g && color.b == colorReplaced.b) {
                    Color replaceWith = new Color(newColor);
                    replaceWith.a = color.a;
                    pixmap.setColor(replaceWith);
                    pixmap.fillRectangle(x, y, 1, 1);
                }
            }
        }

        Texture res = new Texture(pixmap);
        pixmap.dispose();
        return res;
    }
}
