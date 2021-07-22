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
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class Earth extends AnimatorOrb implements OnStartOfTurnPostDrawSubscriber, OnChannelOrbSubscriber
{
    public static final String ORB_ID = CreateFullID(Earth.class);
    public static final int PROJECTILES = 8;

    public static Texture PROJECTILE_LARGE;
    public static Texture PROJECTILE_SMALL;

    private final ArrayList<AdvancedTexture> projectiles = new ArrayList<>();
    private float timer;
    private boolean evoked;
    private int turns;

    public Earth()
    {
        super(ORB_ID, true);

        if (PROJECTILE_LARGE == null || PROJECTILE_SMALL == null)
        {
            PROJECTILE_LARGE = ImageMaster.loadImage("images/orbs/animator/Earth1.png");
            PROJECTILE_SMALL = ImageMaster.loadImage("images/orbs/animator/Earth2.png");
        }

        this.evoked = false;
        this.evokeAmount = this.baseEvokeAmount = 16;
        this.passiveAmount = this.basePassiveAmount = 2;
        this.channelAnimTimer = 0.5f;
        this.turns = 3;

        for (int i = 0; i < PROJECTILES; i++)
        {
            projectiles.add(new AdvancedTexture((i % 3 == 0) ? PROJECTILE_LARGE : PROJECTILE_SMALL, 48f, 48f)
            .SetColor(Mathf.RandomColor(0f, 0.15f, true))
            .SetScale(MathUtils.random(0.8f, 1f))
            .SetFlip(i % 2 == 0, null));
        }

        this.updateDescription();
    }

    @Override
    public void OnChannelOrb(AbstractOrb orb)
    {
        if (orb == this)
        {
            for (AdvancedTexture t : projectiles)
            {
                t.SetPosition(cX, cY)
                .SetOffset(0f, 0f, MathUtils.random(0f, 360f))
                .SetSpeedMulti(0.2f, 0.2f, MathUtils.random(0.8f, 1f));
            }
        }
    }

    @Override
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
        GameEffects.Queue.Add(VFX.RotatingRocks(this.hb, 8).SetImageParameters(this.scale / 2f, 200f, MathUtils.random(-100f, 100f)));
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

        final float delta = Gdx.graphics.getRawDeltaTime();
        for (AdvancedTexture texture : projectiles)
        {
            texture.SetPosition(cX, cY).SetTargetRotation(angle).Update(delta);
        }

        if ((timer -= delta) <= 0)
        {
            final float w = hb.width * 0.5f;
            final float h = hb.height * 0.5f;
            for (AdvancedTexture texture : projectiles)
            {
                texture.SetTargetOffset((w * 0.5f) - MathUtils.random(w), (h * 0.5f) - MathUtils.random(h), null);
            }
            timer = 1.5f;
        }

        this.angle += delta * 180f;
    }

    @Override
    public void render(SpriteBatch sb)
    {
        for (AdvancedTexture projectile : projectiles)
        {
            projectile.Render(sb, Mathf.SubtractColor(c.cpy(), projectile.color, false));
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
            GameActions.Top.Add(new EarthOrbEvokeAction(evokeAmount, cX, cY, this.scale / 2));
            super.EvokeEffect();
        }

        turns = 0;
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        evoked = true;
    }
}