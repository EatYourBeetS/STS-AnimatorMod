package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.InputManager;

public class PlotArmor extends AnimatorRelic
{
    public static final String ID = CreateFullID(PlotArmor.class);
    public static final int HP_THRESHOLD = 50;
    public static final int BASE_DODGE_CHANCE = 10;

    public PlotArmor()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);

        SetCounter(20);
        updateDescription();
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, counter, HP_THRESHOLD);
    }

    @Override
    public void update()
    {
        super.update();

        if (hb.hovered && (!GameUtilities.InBattle() || GameUtilities.CanAcceptInput(false)) && InputManager.RightClick.IsJustPressed())
        {
            if (AddCounter(10) > 50)
            {
                SetCounter(BASE_DODGE_CHANCE);
            }
            updateDescription();
        }
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        this.pulse = player.isBloodied;
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();

        this.pulse = false;
    }

    @Override
    public void onBloodied()
    {
        super.onBloodied();

        this.flash();
        this.pulse = true;
    }

    @Override
    public void onNotBloodied()
    {
        super.onNotBloodied();

        this.pulse = false;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        if (player.isBloodied && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && rng.randomBoolean(counter / 100f))
        {
            damageAmount = 0;
            flash();
        }

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        GR.Common.Dungeon.SetCheating();
    }

    //    @Override
//    public void renderCounter(SpriteBatch sb, boolean inTopPanel)
//    {
//        if (this.counter >= 0)
//        {
//            final String text = GetCounterString();
//            final float offset_y = -(hb.height * 0.5f) - (7.0F * Settings.scale);
//            final float offset_x = -(hb.width * 0.1f);
//            if (inTopPanel)
//            {
//                FontHelper.renderFontCenteredTopAligned(sb, FontHelper.topPanelInfoFont, text,
//                        _offsetX.Get(null) + this.currentX + offset_x, this.currentY + offset_y, Color.WHITE);
//            }
//            else
//            {
//                FontHelper.renderFontCenteredTopAligned(sb, FontHelper.topPanelInfoFont, text,
//                        this.currentX + offset_x, this.currentY + offset_y, Color.WHITE);
//            }
//        }
//    }
}