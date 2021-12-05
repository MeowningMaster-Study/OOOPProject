package ua.carcassone.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import ua.carcassone.game.Settings;
import ua.carcassone.game.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TileTextureManager {
    private final List<List<PreparableTexture>> textures = new LinkedList<>();
    private final PreparableTexture borderTexture, innerBorderTexture, borderSilverTexture, borderWhiteTexture, pointTexture;
    private final PreparableTexture pointDarkerTexture, pointDarkTexture, nullTileTexture;
    private final List<PreparableTexture> rotateClockwiseTextures = new LinkedList<>();
    private final java.util.Map<MeeplePosition.INSTANCE, PreparableTexture> meepleTextures = new HashMap<>();

    private final PreparableTexture transparentTexture;

    public TileTextureManager() {
        for (int i = 0; i <= 24; i++){
            List<PreparableTexture> tileTextures = new LinkedList<>();
            for (int j = 0; j <= 3; j++){
                PreparableTexture texture = new PreparableTexture("skins/wildwest-tiles/"+i+"-"+j+".png");
                tileTextures.add(j, texture);
            }
            textures.add(i, tileTextures);
        }
        this.borderTexture = new PreparableTexture("skins/icons/tile-border.png");
        this.innerBorderTexture = new PreparableTexture("skins/icons/tile-inner-border.png");
        this.borderSilverTexture = new PreparableTexture("skins/icons/tile-border-silver.png");
        this.borderWhiteTexture = new PreparableTexture("skins/icons/tile-border-white.png");
        this.transparentTexture = new PreparableTexture("skins/icons/transparent.png");
        this.pointTexture = new PreparableTexture("skins/icons/blue-point.png");
        this.pointDarkerTexture = new PreparableTexture("skins/icons/blue-point-darker.png");
        this.pointDarkTexture = new PreparableTexture("skins/icons/blue-point-dark.png");
        this.nullTileTexture = new PreparableTexture("skins/wildwest-tiles/Null.png");

        for (int i = 0; i < 4; i++) {
            this.rotateClockwiseTextures.add(new PreparableTexture("skins/icons/clockwiseRotate-"+i+".png"));
        }

        meepleTextures.put(MeeplePosition.INSTANCE.NONE, new PreparableTexture("skins/meeples/meeple-greenscreen.png"));
        meepleTextures.put(MeeplePosition.INSTANCE.FIELD, new PreparableTexture("skins/meeples/meeple-greenscreen-FIELD.png"));
        meepleTextures.put(MeeplePosition.INSTANCE.TOWN, new PreparableTexture("skins/meeples/meeple-greenscreen-TOWN.png"));
        meepleTextures.put(MeeplePosition.INSTANCE.MONASTERY, new PreparableTexture("skins/meeples/meeple-greenscreen-MONASTERY.png"));
        meepleTextures.put(MeeplePosition.INSTANCE.ROAD, new PreparableTexture("skins/meeples/meeple-greenscreen-ROAD.png"));

    }

    public Texture getTexture(int tileTypeId, int rotation){
        return textures.get(tileTypeId).get(rotation).getTexture();
    }

    public Texture getTexture(TileType tileType, int rotation){
        return getTexture(Utils.getTileTypeId(tileType), rotation);
    }

    public Texture getTexture(Tile tile){
        return getTexture(tile.type, tile.rotation);
    }

    public int getMinTileSize() {
        int minTileSize = Settings.minTileResolution;
        return minTileSize;
    }

    public Texture getInnerBorderTexture() {
        return innerBorderTexture.getTexture();
    }

    public Texture getBorderTexture() {
        return borderTexture.getTexture();
    }

    public Texture getBorderSilverTexture() {
        return borderSilverTexture.getTexture();
    }

    public Texture getBorderWhiteTexture() {
        return borderWhiteTexture.getTexture();
    }

    public Texture getTransparentTexture() {
        return transparentTexture.getTexture();
    }

    public Texture getPointTexture() {
        return pointTexture.getTexture();
    }

    public Texture getPointDarkerTexture() {
        return pointDarkerTexture.getTexture();
    }

    public Texture getPointDarkTexture() {
        return pointDarkTexture.getTexture();
    }

    public Texture getRotateClockwiseTexture(int rotation) {
        return rotateClockwiseTextures.get(rotation%4).getTexture();
    }

    public Texture getMeepleTexture() {
        return meepleTextures.get(MeeplePosition.INSTANCE.NONE).getTexture();
    }

    public Texture getMeepleTexture(int instance) {
        return meepleTextures.get(MeeplePosition.getInstance(instance)).getTexture();
    }

    public Texture getMeepleTexture(Color color, int instance) {
        return TileTextureManager.fillTexture(
                meepleTextures.get(MeeplePosition.getInstance(instance)).getTexture(),
                Settings.meepleGreenscreenColor, color
        );
    }

    public Texture getNullTileTexture() {
        return nullTileTexture.getTexture();
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

    private static class PreparableTexture{
        private final String path;
        private Texture texture;

        public PreparableTexture(String path) {
            this.path = path;
        }

        public Texture getTexture() {
            if (texture == null) {
                this.texture = new Texture(Gdx.files.internal(path));

            }
            return this.texture;
        }
    }


}
