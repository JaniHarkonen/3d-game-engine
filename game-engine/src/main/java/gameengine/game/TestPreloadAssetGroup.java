package gameengine.game;

import gameengine.engine.asset.AssetManager;
import gameengine.engine.asset.texture.Texture;
import gameengine.engine.asset.texture.TextureLoadTask;
import gameengine.util.FileUtils;

public class TestPreloadAssetGroup extends AssetManager.AGroup {

	protected TestPreloadAssetGroup() {
		super("preload");
	}

	
	@Override
	public void load() {
		this.loadAsset(new TextureLoadTask("tex-default", FileUtils.getResourcePath("texture/texture.png")));
	}
	
	public Texture getTexture(String name) {
		return (Texture) this.findAsset(name);
	}
}
