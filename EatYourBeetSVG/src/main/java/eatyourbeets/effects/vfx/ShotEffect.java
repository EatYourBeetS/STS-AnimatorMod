package eatyourbeets.effects.vfx;

public class ShotEffect extends GenericAnimationEffect
{
    public static final int SIZE = 512;
    public static final String IMAGE_PATH = "images/effects/Shot0.png";

    public ShotEffect(float x, float y) {

        super(IMAGE_PATH,x,y,SIZE, 0.03F);
    }

}
