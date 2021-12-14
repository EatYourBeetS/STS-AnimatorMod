package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class ManiwaKoumori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ManiwaKoumori.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.Katanagatari).PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public ManiwaKoumori()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(2);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainBlock(block).SetVFX(true, true);
        }

        GameActions.Bottom.Draw(1 + CombatStats.Affinities.GetAffinityLevel(Affinity.Green, true));

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.CreateThrowingKnives(secondaryValue);
            this.exhaustOnUseOnce = true;
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() -> GameActions.Bottom.DiscardFromHand(name, EnergyPanel.getCurrentEnergy() * 2, false)
                                                            .SetOptions(false, false, false));
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetPowerAmount(Affinity.Dark) >= 10;
    }
}