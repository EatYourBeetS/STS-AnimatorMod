package pinacolada.orbs.pcl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.ui.TextureCache;
import pinacolada.effects.SFX;
import pinacolada.orbs.PCLOrb;
import pinacolada.powers.temporary.TemporaryEnchantedArmorPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class Metal extends PCLOrb
{
    public static final String ORB_ID = CreateFullID(Metal.class);

    public static TextureCache img1 = IMAGES.Metal;

    private final boolean hFlip1;

    public Metal()
    {
        super(ORB_ID, Timing.EndOfTurn, false);

        this.hFlip1 = MathUtils.randomBoolean();
        this.baseEvokeAmount = this.evokeAmount = 2;
        this.basePassiveAmount = this.passiveAmount = 10;

        this.updateDescription();
        this.channelAnimTimer = 0.5f;
    }

    public String GetUpdatedDescription()
    {
        return FormatDescription(0, this.passiveAmount, this.evokeAmount);
    }

    @Override
    public void triggerEvokeAnimation() {
        super.triggerEvokeAnimation();

        playChannelSFX(0.93f);
    }

    @Override
    public void updateAnimation()
    {
        super.updateAnimation();

        this.angle += GR.UI.Delta(13f);
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);

        float by = bobEffect.y;
        sb.draw(img1.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale, scale, angle, 0, 0, 96, 96, hFlip1, false);
        this.shineColor.a = Interpolation.sine.apply(0.12f,0.62f, angle / 165);
        sb.setColor(this.shineColor);
        sb.draw(img1.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale, this.scale, -2 * angle, 0, 0, 96, 96, !this.hFlip1, false);
        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    public void playChannelSFX()
    {
        playChannelSFX(0.93f);
    }

    public void playChannelSFX(float volume)
    {
        SFX.Play(SFX.POWER_METALLICIZE, 0.3f, 1.3f, volume);
    }

    @Override
    public void Evoke()
    {
        PCLActions.Bottom.GainMetallicize(evokeAmount);
    }

    @Override
    public void Passive()
    {
        PCLActions.Bottom.StackPower(AbstractDungeon.player, new TemporaryEnchantedArmorPower(AbstractDungeon.player, passiveAmount));

        super.Passive();
    }

    @Override
    protected Color GetColor1()
    {
        return Color.GRAY;
    }

    @Override
    protected Color GetColor2()
    {
        return Color.GRAY;
    }
}