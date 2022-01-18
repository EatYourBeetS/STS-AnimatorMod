package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.basic.ImprovedDefend;
import pinacolada.cards.pcl.basic.ImprovedStrike;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class DolaRiku extends PCLCard
{
    public static final PCLCardData DATA = Register(DolaRiku.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public DolaRiku()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Orange(1);

        SetAffinityRequirement(PCLAffinity.Orange, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (TrySpendAffinity(PCLAffinity.Orange) && info.TryActivateSemiLimited())
        {
            PCLActions.Bottom.Draw(1)
            .SetFilter(c -> c.costForTurn == 0 && !PCLGameUtilities.IsHindrance(c), false);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards -> {
            for (AbstractCard card : cards) {
                final RandomizedList<AbstractCard> pool = PCLGameUtilities.GetCardPoolInCombat(card.rarity);
                if (pool.Size() == 0) {
                    if (card.rarity == CardRarity.BASIC) {
                        for (PCLCardData strike : ImprovedStrike.GetCards()) {
                            pool.Add(strike.MakeCopy(false));
                        }
                        for (PCLCardData defend : ImprovedDefend.GetCards()) {
                            pool.Add(defend.MakeCopy(false));
                        }
                    }
                    else {
                        pool.AddAll(PCLGameUtilities.GetCardPoolInCombat(CardRarity.COMMON).GetInnerList());
                        pool.AddAll(PCLGameUtilities.GetCardPoolInCombat(CardRarity.UNCOMMON).GetInnerList());
                        pool.AddAll(PCLGameUtilities.GetCardPoolInCombat(CardRarity.RARE).GetInnerList());
                    }
                }
                final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while (choice.size() < magicNumber && pool.Size() > 0)
                {
                    AbstractCard temp = pool.Retrieve(rng);
                    if (!temp.cardID.equals(card.cardID)) {
                        AbstractCard temp2 = temp.makeCopy();
                        PCLGameUtilities.ModifyCostForCombat(temp2, -1, true);
                        if (upgraded) {
                            temp2.upgrade();
                        }
                        choice.addToTop(temp2);
                    }
                }

                PCLActions.Bottom.SelectFromPile(name, 1, choice)
                        .SetOptions(false, false)
                        .AddCallback(cards2 ->
                        {
                            PCLActions.Bottom.MakeCardInHand(cards2.get(0));
                        });
            }
        });
    }
}