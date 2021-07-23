package eatyourbeets.effects.vfx;

public abstract class LightShaftEffect extends GenericAnimationEffect
{
    public static final int SIZE = 192;
    public static final String IMAGE_PATH = "images/effects/LightShaft.png";

    public LightShaftEffect(float x, float y) {

        super(IMAGE_PATH,x,y,SIZE);
    }

}
