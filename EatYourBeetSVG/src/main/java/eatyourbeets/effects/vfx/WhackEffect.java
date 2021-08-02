package eatyourbeets.effects.vfx;

import eatyourbeets.resources.GR;

public class WhackEffect extends GenericAnimationEffect
{
    public static final int SIZE = 512;
    public static final String IMAGE_PATH = "images/effects/Whack0.png";

    public WhackEffect(float x, float y) {

        super(GR.Common.Images.Effects.Whack.Texture(),x,y,SIZE, 0.03F);
    }

}
