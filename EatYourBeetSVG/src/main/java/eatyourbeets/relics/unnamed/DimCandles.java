package eatyourbeets.relics.unnamed;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.unnamed.SummoningSicknessPower;
import eatyourbeets.relics.UnnamedRelic;
import eatyourbeets.utilities.GameActions;

public class DimCandles extends UnnamedRelic
{
    public static final int POWER_AMOUNT = 4;
    public static final String ID = CreateFullID(DimCandles.class);

    public DimCandles()
    {
        super(ID, RelicTier.SHOP, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, POWER_AMOUNT);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        if (IsEnabled() && c.hasTag(UnnamedCard.SUMMON))
        {
            GameActions.Delayed.Callback(() ->
            {
                GameActions.Bottom.GainEnergy(1);
                GameActions.Bottom.StackPower(new SummoningSicknessPower(player, POWER_AMOUNT));
                SetEnabled(false);
            });
            flash();
        }
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        SetEnabled(true);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        SetEnabled(true);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();

        SetEnabled(true);
    }
}