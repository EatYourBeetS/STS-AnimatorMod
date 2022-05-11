package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ManiwaKyouken extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ManiwaKyouken.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeries(CardSeries.Katanagatari);

    public ManiwaKyouken()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Dark(1, 0, 2);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
        this.upgradedBlock = true;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.GainBlock(block).SetVFX(true, true);
        }

        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() ->
        {
            final int discard = EnergyPanel.getCurrentEnergy() * 2;
            if (discard > 0)
            {
                GameActions.Bottom.DiscardFromHand(name, discard, false)
                .SetOptions(false, false, false);
            }
        });

        if (CheckSpecialCondition(true))
        {
            GameActions.Last.SelectFromHand(name, 1, false)
            .SetFilter(c -> !GameUtilities.IsSealed(c))
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.SealAffinities(c, false, true);
                }
            });
            this.exhaustOnUseOnce = true;
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetPowerAmount(Affinity.Dark) >= 3;
    }
}