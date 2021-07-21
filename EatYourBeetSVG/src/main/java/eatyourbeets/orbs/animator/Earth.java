package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.orbs.EarthOrbEvokeAction;
import eatyourbeets.actions.orbs.EarthOrbPassiveAction;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

public class Earth extends AnimatorOrb implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ORB_ID = CreateFullID(Earth.class);
    public static final int HIT_COUNT = 8;

    public static Texture imgExt1;
    public static Texture imgExt2;

    private final boolean hFlip1;
    private final ArrayList<EarthParticle> particles = new ArrayList<>();
    private boolean evoked;
    private int turns;

    public Earth()
    {
        super(ORB_ID, true);

        if (imgExt1 == null || imgExt2 == null)
        {
            imgExt1 = ImageMaster.loadImage("images/orbs/animator/Earth.png");
            imgExt2 = ImageMaster.loadImage("images/orbs/animator/Earth2.png");
        }

        this.hFlip1 = MathUtils.randomBoolean();
        this.evoked = false;
        this.baseEvokeAmount = 16;
        this.evokeAmount = this.baseEvokeAmount;
        this.basePassiveAmount = 2;
        this.passiveAmount = this.basePassiveAmount;
        this.channelAnimTimer = 0.5f;
        this.turns = 3;
        this.updateDescription();

        for (int i = 0; i < HIT_COUNT; i++)
        {
            EarthParticle particle = new EarthParticle(this.scale / (2.5f + 0.5f * MathUtils.cos(i)), 0.15f * MathUtils.sin(i) , 12 * MathUtils.sin(1.5f*i + 1) - 48f, 12 * MathUtils.cos(1.5f*i) -48f, -0.25f * MathUtils.sin(1.2f*i), -0.25f * MathUtils.cos(1.7f*i + 1), i, i % 2 == 0);
            this.particles.add(particle);
        }

    }

    public void updateDescription()
    {
        final String[] desc = orbStrings.DESCRIPTION;

        this.applyFocus();
        this.description = desc[0] + this.passiveAmount + desc[1] + this.evokeAmount + desc[2] + this.turns + desc[3];
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (!AbstractDungeon.player.orbs.contains(this))
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            return;
        }

        this.turns -= 1;

        if (turns <= 0)
        {
            GameActions.Bottom.Add(new EvokeSpecificOrbAction(this));

            evoked = true;
        }
    }

    @Override
    public void onEndOfTurn()
    {
        if (evoked)
        {
            return;
        }

        if (turns > 0)
        {
            PassiveEffect();
        }

        this.updateDescription();
    }

    @Override
    public void triggerEvokeAnimation()
    {
        //CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_CHANNEL", 0.1f);
        CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", 0.1f);
        GameEffects.Queue.Add(VFX.RockBurst(this.hb, 0.5f));
    }

    @Override
    public void applyFocus()
    {
        int focus = GetFocus();
        if (focus > 0)
        {
            this.passiveAmount = Math.max(0, this.basePassiveAmount + focus);
            this.evokeAmount = Math.max(0, this.baseEvokeAmount + (focus * 2));
        }
        else
        {
            this.evokeAmount = this.baseEvokeAmount;
            this.passiveAmount = this.basePassiveAmount;
        }
    }

    @Override
    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 180f;
    }

    @Override
    public void render(SpriteBatch sb)
    {
        for (EarthParticle particle : this.particles) {
            sb.setColor(particle.GetColor());
            sb.draw(particle.useAltImg ? imgExt2 : imgExt1, particle.GetPositionX(), particle.GetPositionY(), 48f, 48f, 96f, 96f, this.scale / 2f, this.scale / 2f, particle.angle + this.angle / 12f, 0, 0, 96, 96, this.hFlip1, false);
        }

        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    protected void renderText(SpriteBatch sb)
    {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET - 4f * Settings.scale, new Color(0.2f, 1f, 1f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, new Color(0.8f, 0.7f, 0.2f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.turns), this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, this.c, this.fontScale);
    }

    @Override
    public void playChannelSFX()
    {
        CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_CHANNEL", 0.2f);
        turns = 3;
        evoked = false;
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public AbstractOrb makeCopy()
    {
        Earth copy = new Earth();
        if (!evoked)
        {
            copy.turns = turns;
        }

        return copy;
    }

    @Override
    public void PassiveEffect()
    {
        GameActions.Bottom.Add(new EarthOrbPassiveAction(passiveAmount));
        super.PassiveEffect();
    }

    @Override
    public void EvokeEffect()
    {
        if (evokeAmount > 0)
        {
            GameActions.Top.Add(new EarthOrbEvokeAction(evokeAmount, cX, cY, this.scale));
            super.EvokeEffect();
        }

        turns = 0;
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        evoked = true;
    }

    public class EarthParticle {

        public Color color;
        public boolean useAltImg;
        public float scale;
        public float tint;
        public float offsetX;
        public float offsetY;
        public float bobX;
        public float bobY;
        public float angle;

        public EarthParticle(float scale, float tint, float offsetX, float offsetY, float bobX, float bobY, float angle, boolean useAltImg) {
            this.scale = scale;
            this.tint = tint;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.bobX = bobX;
            this.bobY = bobY;
            this.angle = angle;
            this.color = c.cpy().add(this.tint, this.tint, this.tint, 0);
            this.useAltImg = useAltImg;
        }

        public Color GetColor(){
            return c.cpy().add(this.tint, this.tint, this.tint, 0);
        }

        public float GetPositionX() {
            return cX + this.offsetX + bobEffect.y * this.bobX;
        }

        public float GetPositionY() {
            return cY + this.offsetY + bobEffect.y * this.bobY;
        }

    }
}