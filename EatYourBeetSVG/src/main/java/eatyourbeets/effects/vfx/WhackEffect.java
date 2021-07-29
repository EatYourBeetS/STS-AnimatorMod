package eatyourbeets.effects.vfx;

public class WhackEffect extends GenericAnimationEffect
{
    public static final int SIZE = 512;
    public static final String IMAGE_PATH = "images/effects/Whack0.png";

    public WhackEffect(float x, float y) {

        super(IMAGE_PATH,x,y,SIZE, 0.03F);
    }

}
