package eatyourbeets.effects.vfx;

public class GunshotEffect extends GenericAnimationEffect
{
    public static final int SIZE = 192;
    public static final String IMAGE_PATH = "images/effects/Gunshot.png";

    public GunshotEffect(float x, float y) {

        super(IMAGE_PATH,x,y,SIZE, 0.03F);
    }

}
