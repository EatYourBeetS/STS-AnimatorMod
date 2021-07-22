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
import patches.orbs.AbstractOrbPatches;

public class Earth extends AnimatorOrb implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ORB_ID = CreateFullID(Earth.class);

    public static Texture imgExt1;
    public static Texture imgExt2;

    private final boolean hFlip1;
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
        GameEffects.Queue.Add(VFX.RotatingRocks(this.hb, 8).SetImageParameters(this.scale / 2f, 200f, MathUtils.random(-100f,100f)));
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

        AbstractOrbPatches.ApplyAmountChangeToOrb(this);
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
        sb.setColor(GetColor(0f));
        sb.draw(imgExt1, GetPositionX(-64, -0.15f), GetPositionY(-64, -0.2f), 48f, 48f, 96f, 96f, this.scale / 3.3f, this.scale / 3.3f, 300f + this.angle / 6f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setColor(GetColor(0.1f));
        sb.draw(imgExt2, GetPositionX(-48, -0.1f), GetPositionY(-32, 0.1f), 48f, 48f, 96f, 96f, this.scale / 2.2f, this.scale / 2f, 200f + this.angle / 12f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setColor(GetColor(0.15f));
        sb.draw(imgExt1, GetPositionX(-64, -0.15f), GetPositionY(-48, 0.15f), 48f, 48f, 96f, 96f, this.scale / 3f, this.scale / 3f, 400f + this.angle / 6f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setColor(GetColor(0.05f));
        sb.draw(imgExt1, GetPositionX(-32, 0.15f), GetPositionY(-32, 0.15f), 48f, 48f, 96f, 96f, this.scale / 3.1f, this.scale / 3.1f, 500f + this.angle / 6f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setColor(GetColor(0.1f));
        sb.draw(imgExt1, GetPositionX(-64, -0.2f), GetPositionY(-32, 0.15f), 48f, 48f, 96f, 96f, this.scale / 3.5f, this.scale / 3.1f, 400f + this.angle / 6f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setColor(GetColor(0.05f));
        sb.draw(imgExt2, GetPositionX(-48, -0.15f), GetPositionY(-64, -0.2f), 48f, 48f, 96f, 96f, this.scale / 2.9f, this.scale / 2f, 800f + this.angle / 12f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setColor(GetColor(0.15f));
        sb.draw(imgExt1, GetPositionX(-32, 0.2f), GetPositionY(-48, -0.15f), 48f, 48f, 96f, 96f, this.scale / 3f, this.scale / 3f, 400f + this.angle / 6f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setColor(GetColor(0.2f));
        sb.draw(imgExt1, GetPositionX(-48, 0.1f), GetPositionY(-48, -0.1f), 48f, 48f, 96f, 96f, this.scale / 2.7f, this.scale / 3.1f, 600f + this.angle / 6f, 0, 0, 96, 96, this.hFlip1, false);

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
            GameActions.Top.Add(new EarthOrbEvokeAction(evokeAmount, cX, cY, this.scale / 2));
            super.EvokeEffect();
        }

        turns = 0;
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        evoked = true;
    }

    protected Color GetColor(float tint){
        return c.cpy().add(tint, tint, tint, 0);
    }

    protected float GetPositionX(float offsetX, float bob) {
        return cX + offsetX + bobEffect.y * bob;
    }

    protected float GetPositionY(float offsetY, float bob) {
        return cY + offsetY + bobEffect.y * bob;
    }

}