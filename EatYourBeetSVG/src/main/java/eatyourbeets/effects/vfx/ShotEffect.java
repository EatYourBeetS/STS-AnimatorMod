package eatyourbeets.effects.vfx;

import eatyourbeets.resources.GR;

public class ShotEffect extends GenericAnimationEffect
{
    public static final int SIZE = 512;
    public static final String IMAGE_PATH = "images/effects/Shot0.png";

    public ShotEffect(float x, float y) {

        super(GR.Common.Images.Effects.Shot1.Texture(),x,y,SIZE, 0.03F);
    }

}
