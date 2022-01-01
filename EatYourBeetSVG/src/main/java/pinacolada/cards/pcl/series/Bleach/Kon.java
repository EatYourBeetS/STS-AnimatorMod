package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Kon extends PCLCard
{
    public static final PCLCardData DATA = Register(Kon.class)
            .SetSkill(0, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage(true);

    public Kon()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Star(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetLoyal(true);
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        switch (PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity()) {
            case Red:
                PCLActions.Bottom.GainVigor(magicNumber);
            case Green:
                PCLActions.Bottom.GainEnergyNextTurn(1);
            default:
                PCLActions.Bottom.DrawNextTurn(1);
        }
    }
}