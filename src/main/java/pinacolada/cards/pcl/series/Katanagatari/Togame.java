package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;

public class Togame extends PCLCard
{
    public static final PCLCardData DATA = Register(Togame.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Togame()
    {
        super(DATA);

        Initialize(0, 2, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Green, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup cGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        PCLActions.Bottom.Draw(magicNumber).AddCallback((cards) -> {
            cGroup.group.addAll(cards);
            PCLActions.Bottom.ExhaustFromHand(name, 1, false)
                    .SetOptions(false, false, false)
                    .AddCallback(() ->
                    {
                        PCLActions.Bottom.Draw(1).AddCallback((cards2) -> {
                            cGroup.group.addAll(cards2);
                            if (info.CanActivateSemiLimited && CheckAffinity(PCLAffinity.Green) && info.TryActivateSemiLimited()) {
                                PCLActions.Bottom.Motivate(cGroup, 1);
                            }
                        });
                    });
        });
    }
}