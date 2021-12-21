package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HarukoHaruhara extends PCLCard
{
    public static final PCLCardData DATA = Register(HarukoHaruhara.class)
            .SetSkill(1, CardRarity.RARE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.FLCL);

    public HarukoHaruhara()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetAffinity_Star(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, 1, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(m, (target, cards) ->
        {
            AbstractCard discarded = cards.get(0);
            RandomizedList<AbstractCard> playable = new RandomizedList<>();
            RandomizedList<AbstractCard> unplayable = new RandomizedList<>();
            for (AbstractCard card : player.hand.group)
            {
                if (card != this && card != discarded)
                {
                    if (PCLGameUtilities.IsHindrance(card))
                    {
                        unplayable.Add(card);
                    }
                    else
                    {
                        playable.Add(card);
                    }
                }
            }

            AbstractCard card = null;
            if (playable.Size() > 0)
            {
                card = playable.Retrieve(rng);
            }
            else if (unplayable.Size() > 0)
            {
                card = unplayable.Retrieve(rng);
            }

            if (card != null)
            {
                PCLActions.Bottom.PlayCard(card, player.hand, target);
            }
        });
    }
}