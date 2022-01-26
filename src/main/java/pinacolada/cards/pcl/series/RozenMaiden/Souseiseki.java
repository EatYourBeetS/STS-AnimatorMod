package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Souseiseki extends PCLCard
{
    public static final PCLCardData DATA = Register(Souseiseki.class)
    		.SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal, PCLCardTarget.Normal).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Suiseiseki(), false));

    public Souseiseki()
    {
        super(DATA);

        Initialize(7, 2, 0, 0);
        SetUpgrade(2, 1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(1, 0 ,1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Draw(1)
                .ShuffleIfEmpty(false)
                .SetFilter(c -> Suiseiseki.DATA.ID.equals(c.cardID), false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.ExhaustFromHand(name, 1, false)
                .SetOptions(false, false, false)
                .AddCallback(cards -> {
                    if (cards.size() > 0 && PCLGameUtilities.IsHindrance(cards.get(0)))
                        PCLActions.Bottom.Draw(1);
                });

        if (info.IsSynergizing) {
            PCLActions.Bottom.Draw(1)
                    .ShuffleIfEmpty(false)
                    .SetFilter(c -> Suiseiseki.DATA.ID.equals(c.cardID), false);
        }
    }
}
