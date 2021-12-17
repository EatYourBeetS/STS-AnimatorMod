package pinacolada.cards.pcl.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Hibiki extends PCLCard
{
    public static final PCLCardData DATA = Register(Hibiki.class)
            .SetAttack(1, CardRarity.RARE, PCLAttackType.Ranged, eatyourbeets.cards.base.EYBCardTarget.Random)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Kancolle);

    public Hibiki()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Star(0, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Silver(1);

        SetHitCount(3);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.GUNSHOT).forEach(d -> d
                .SetOptions(true, false));

        PCLActions.Bottom.ModifyAllInstances(uuid, c -> PCLGameUtilities.IncreaseHitCount((PCLCard) c, secondaryValue, false));

        if (info.IsSynergizing) {
            PCLActions.Bottom.AddAffinity(PCLAffinity.Star, magicNumber);
        }
    }
}