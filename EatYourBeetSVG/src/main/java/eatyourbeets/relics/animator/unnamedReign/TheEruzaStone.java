package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class TheEruzaStone extends UnnamedReignRelic
{
    public static final String ID = AnimatorRelic.CreateFullID(TheEruzaStone.class);

    public static final int ENERGY_GAIN = 2;
    public static final int MAX_VITALITY = 3;

    public TheEruzaStone()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, ENERGY_GAIN, MAX_VITALITY);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        SetEnabled(true);
        SetCounter(MAX_VITALITY);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();
        SetCounter(-1);
    }

    @Override
    public void OnManualEquip()
    {
        player.energy.energyMaster += ENERGY_GAIN;
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        player.energy.energyMaster -= ENERGY_GAIN;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        if (IsEnabled() && c.type == AbstractCard.CardType.POWER)
        {
            GameActions.Bottom.GainVitality(1);
            SetCounter(counter - 1);
            SetEnabled(false);
        }
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        SetEnabled(counter > 0);
    }

    public String GetDoomedString()
    {
        return " NL #y" + name.replace(" ", " #y") + " protects you, increasing the turn limit by #b" + GetDoomedTurnIncrease() + ".";
    }

    public int GetDoomedTurnIncrease()
    {
        return 1;
    }
}