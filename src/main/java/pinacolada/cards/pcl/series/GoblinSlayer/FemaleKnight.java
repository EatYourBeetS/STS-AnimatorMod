package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.JUtils;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;

public class FemaleKnight extends PCLCard
{
    public static final PCLCardData DATA = Register(FemaleKnight.class)
            .SetSkill(3, CardRarity.UNCOMMON, PCLCardTarget.None, true)
            .SetSeriesFromClassPackage();

    public FemaleKnight()
    {
        super(DATA);

        Initialize(0, 19, 3, 2);
        SetUpgrade(0,0,0);

        SetAffinity_Red(1);
        SetAffinity_Orange(1);
        SetAffinity_Light(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Light, 3);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.SelectFromHand(name, secondaryValue, false)
                .SetFilter(c -> c instanceof PCLCard && c.type == CardType.ATTACK && ((PCLCard) c).attackType == PCLAttackType.Normal)
                .AddCallback((cards) -> {
                    for (AbstractCard c : cards) {
                        PCLCard pC = JUtils.SafeCast(c, PCLCard.class);
                        if (pC != null) {
                            pC.attackType = PCLAttackType.Piercing;
                        }
                    }
                });

        PCLActions.Bottom.GainPlatedArmor(TrySpendAffinity(PCLAffinity.Light) ? magicNumber + 1 : magicNumber);
    }
}