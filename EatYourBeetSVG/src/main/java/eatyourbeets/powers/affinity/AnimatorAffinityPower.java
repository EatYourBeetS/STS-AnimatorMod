package eatyourbeets.powers.affinity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.animator.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

public abstract class AnimatorAffinityPower extends AbstractAffinityPower
{
    protected AnimatorAffinityPower(Affinity affinity, String powerID, String symbol)
    {
        super(affinity, powerID, symbol);
    }

    @Override
    public void Initialize(AbstractCreature owner)
    {
        this.owner = owner;
        this.enabled = true;
        this.boost = 0;
        this.thresholdIndex = 0;
        this.minimumAmount = 0;
        this.maxAmount = 5;

        Initialize(1, PowerType.BUFF, false);
    }

    @Override
    protected String GetUpdatedDescription()
    {
        switch (affinity)
        {
            case Red: return GR.Tooltips.RedPower.description;
            case Green: return GR.Tooltips.GreenPower.description;
            case Blue: return GR.Tooltips.BluePower.description;
            case Light: return GR.Tooltips.LightPower.description;
            case Dark: return GR.Tooltips.DarkPower.description;
        }

        throw new RuntimeException("Affinity not supported: " + affinity);
    }

    public static AnimatorAffinityPower CreatePower(Affinity affinity)
    {
        switch (affinity)
        {
            case Red: return new ForcePower();
            case Green: return new AgilityPower();
            case Blue: return new IntellectPower();
            case Light: return new BlessingPower();
            case Dark: return new CorruptionPower();

            default: throw new RuntimeException("Invalid enum value: " + affinity.name());
        }
    }

    public float ApplyScaling(float multi)
    {
        return GetThresholdLevel() * multi;
    }

    public float ApplyScaling(EYBCard card, float base)
    {
        return base + ApplyScaling(card.affinities.GetScaling(affinity, true));
    }

    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.Animator.PlayerClass;
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        final float scale = Settings.scale;
        final float w = hb.width;
        final float h = hb.height;
        final float x = hb.x + (5 * scale);
        final float y = hb.y + (9 * scale);
        final float cX = hb.cX + (5 * scale);
        final float cY = hb.cY;
        final int amount = GetThresholdLevel();

        Color amountColor;
        RenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
        amountColor = (amount > minimumAmount ? Colors.Blue(1) : Colors.Cream(minimumAmount > 0 ? 1 : 0.6f)).cpy();

        final Color imgColor = Colors.White((enabled && (boost + amount) > 0) ? 1 : 0.5f);
        RenderHelpers.DrawCentered(sb, imgColor, img, x + 20 * scale, cY + (3f * scale), 32, 32, 1, 0);

        final float textX = x + ((amount > 9 ? 56 : 50) * scale);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), textX, y, fontScale, amountColor);

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x + w + (5 * scale), cY + (5f * scale));
        }
    }

    public boolean CanUpgrade()
    {
        return GetThresholdLevel() < maxAmount;
    }

    public void Upgrade(int amount)
    {
        final int current = GetThresholdLevel();
        amount = Mathf.Min(maxAmount - current, amount);
        if (amount > 0)
        {
            boost = 0;

            for (int i = 0; i < amount; i++)
            {
                thresholdIndex += 1;
                OnThresholdReached();
                CombatStats.OnAffinityThresholdReached(this, thresholdIndex);
            }
        }
    }

    public void Downgrade(int amount)
    {
        final int current = GetThresholdLevel();
        amount = Mathf.Min(current, amount);
        if (amount > 0)
        {
            GameActions.Bottom.ReducePower(owner, GetThresholdBonusPower().ID, amount);
            SetThresholdLevel(current - amount);
        }
    }

    public int GetUpgradeCost()
    {
        final int level = GetThresholdLevel();
        return Mathf.Max(0, level + 3 - boost);
    }

    @Override
    public void Stack(int amount, boolean retain)
    {
        if (retain)
        {
            RetainOnce();
        }

        if (amount > 0)
        {
            CombatStats.Affinities.AddAffinity(affinity, amount);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        amount = 1;
        boost = 0;
    }
}