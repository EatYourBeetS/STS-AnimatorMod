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
            .SetSeries(CardSeries.Katanagatari);

    public ManiwaKyouken()
    {
        super(DATA);

        Initialize(0, 1, 2);
        SetUpgrade(0, 0, 1);

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
        return super.GetBlockInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainBlock(block).SetVFX(true, true);
        }

        GameActions.Bottom.Draw(1 + CombatStats.Affinities.GetPowerAmount(Affinity.Green));

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.SelectFromHand(name, 1, false)
            .SetFilter(c -> GameUtilities.GetAffinityLevel(c, Affinity.Star, false) < 2)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.ModifyAffinityLevel(c, Affinity.Star, 2, false);
                }
            });
            this.exhaustOnUseOnce = true;
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Callback(() -> GameActions.Bottom.DiscardFromHand(name, EnergyPanel.getCurrentEnergy() * 2, false)
                                                            .SetOptions(false, false, false));
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetPowerAmount(Affinity.Dark) >= 3;
    }
}