package com.monyetmabuk.rajawali.tutorials.examples.general;

import javax.microedition.khronos.opengles.GL10;

import rajawali.materials.Material;
import rajawali.materials.textures.ATexture;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.primitives.ScreenQuad;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.monyetmabuk.rajawali.tutorials.examples.AExampleFragment;

public class ThreeSixtyImagesFragment extends AExampleFragment {

	@Override
	protected AExampleRenderer createRenderer() {
		return new ThreeSixtyImagesRenderer(getActivity());
	}

	private final class ThreeSixtyImagesRenderer extends AExampleRenderer {
		private ATexture[] mTextures;
		private ScreenQuad mScreenQuad;
		private int mFrameCount;
		private Material mMaterial;
		private final static int NUM_TEXTURES = 80;

		public ThreeSixtyImagesRenderer(Context context) {
			super(context);
		}

		protected void initScene() {
			if (mTextureManager != null)
				mTextureManager.reset();
			if (mMaterial != null)
				mMaterial.getTextureList().clear();

			getCurrentScene().setBackgroundColor(0xffffff);

			mMaterial = new Material();

			mScreenQuad = new ScreenQuad();
			mScreenQuad.setMaterial(mMaterial);
			addChild(mScreenQuad);

			if (mTextures == null) {
				// -- create an array that will contain all TextureInfo objects
				mTextures = new ATexture[NUM_TEXTURES];
			}
			mFrameCount = 0;

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inInputShareable = true;

			for (int i = 1; i <= NUM_TEXTURES; ++i) {
				// -- load all the textures from the drawable folder
				int resourceId = mContext.getResources().getIdentifier(
						i < 10 ? "m0" + i : "m" + i, "drawable",
						"com.monyetmabuk.rajawali.tutorials");

				Bitmap bitmap = BitmapFactory.decodeResource(
						mContext.getResources(), resourceId, options);

				ATexture texture = new Texture("bm" + i, bitmap);
				texture.setMipmap(false);
				texture.shouldRecycle(true);
				mTextures[i - 1] = mTextureManager.addTexture(texture);
			}
			try {
				mMaterial.addTexture(mTextures[0]);
				mMaterial.setColorInfluence(0);
			} catch (TextureException e) {
				e.printStackTrace();
			}
		}

		public void onDrawFrame(GL10 glUnused) {
			super.onDrawFrame(glUnused);
			// -- get the texture info list and remove the previous TextureInfo object
			mMaterial.getTextureList().remove(
					mTextures[mFrameCount++ % NUM_TEXTURES]);
			// -- add a new TextureInfo object
			mMaterial.getTextureList().add(
					mTextures[mFrameCount % NUM_TEXTURES]);
		}

	}

}
