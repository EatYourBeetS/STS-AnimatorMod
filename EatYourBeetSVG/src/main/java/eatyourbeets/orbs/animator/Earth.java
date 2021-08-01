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
import eatyourbeets.effects.vfx.OrbFlareEffect2;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.effects.Projectile;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class Earth extends AnimatorOrb implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ORB_ID = CreateFullID(Earth.class);
    public static final int PROJECTILES = 8;

    public static Texture PROJECTILE_LARGE;
    public static Texture PROJECTILE_SMALL;

    public final ArrayList<Projectile> projectiles = new ArrayList<>();
    public boolean evoked;
    public int turns;

    private float timer;

    public Earth()
    {
        super(ORB_ID, Timing.EndOfTurn);

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

        this.updateDescription();
    }

    public void GenerateProjectiles()
    {
        projectiles.clear();
        for (int i = 0; i < PROJECTILES; i++)
        {
            projectiles.add(new Projectile((i % 3 == 0) ? PROJECTILE_LARGE : PROJECTILE_SMALL, 48f, 48f)
            .SetPosition(cX, cY)
            .SetColor(Mathf.RandomColor(0f, 0.15f, true))
            .SetScale(MathUtils.random(0.8f, 1f))
            .SetFlip(i % 2 == 0, null)
            .SetOffset(0f, 0f, MathUtils.random(0f, 360f))
            .SetSpeed(2f, 2f, MathUtils.random(18f, 24f)));
        }
    }

    @Override
    public void onChannel()
    {
        turns = 3;
        evoked = false;
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        GenerateProjectiles();
    }

    @Override
    public void updateDescription()
    {
        this.applyFocus();
        this.description = JUtils.Format(orbStrings.DESCRIPTION[0], this.passiveAmount, this.evokeAmount, this.turns);
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
        if (!evoked && turns > 0)
        {
            Passive();
        }

        this.updateDescription();
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
        for (Projectile texture : projectiles)
        {
            texture.SetPosition(cX, cY).SetTargetRotation(angle).Update(delta);
        }

        if (!evoked && (timer -= delta) <= 0)
        {
            final float w = hb.width * 0.5f;
            final float h = hb.height * 0.5f;
            for (Projectile texture : projectiles)
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
        for (Projectile projectile : projectiles)
        {
            projectile.Render(sb, Mathf.Subtract(c.cpy(), projectile.color, false));
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
    public void Passive()
    {
        GameActions.Bottom.Add(new EarthOrbPassiveAction(passiveAmount));
        super.Passive();
    }

    @Override
    public void Evoke()
    {
        if (evokeAmount > 0)
        {
            if (projectiles.isEmpty())
            {
                GenerateProjectiles();
            }

            GameActions.Bottom.Add(new EarthOrbEvokeAction(this, evokeAmount));
            super.Evoke();
        }

        turns = 0;
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        evoked = true;
    }

    @Override
    protected OrbFlareEffect2 GetOrbFlareEffect()
    {
        return super.GetOrbFlareEffect().SetColors(Color.BROWN, Color.DARK_GRAY);
    }
}