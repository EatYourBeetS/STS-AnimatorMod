package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class HarukoHaruhara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HarukoHaruhara.class).SetSkill(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);

    public HarukoHaruhara()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetSeries(CardSeries.FLCL);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, true)
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
                    if (GameUtilities.IsCurseOrStatus(card))
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
                GameActions.Bottom.PlayCard(card, player.hand, target);
            }
        });
    }
}