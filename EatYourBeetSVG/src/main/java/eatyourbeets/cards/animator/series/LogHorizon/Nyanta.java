package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.EnduranceStance;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;

public class Nyanta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nyanta.class)
            .SetSkill(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Nyanta()
    {
        super(DATA);

        Initialize(0, 7, 2, 2);
        SetUpgrade(0, 2, 1, 1);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetRetainOnce(true);

        SetAffinityRequirement(Affinity.Green, 3);
        SetAffinityRequirement(Affinity.Orange, 3);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainTemporaryThorns(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Orange,Affinity.Green).AddConditionalCallback((cards) -> {
            for (AffinityChoice c : cards) {
                if (c.Affinity.equals(Affinity.Green)) {
                    GameActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
                }
                else {
                    GameActions.Bottom.ChangeStance(EnduranceStance.STANCE_ID);
                }
            }
        });
    }
}