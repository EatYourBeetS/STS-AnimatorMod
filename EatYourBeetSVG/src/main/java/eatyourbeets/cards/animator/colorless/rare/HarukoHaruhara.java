package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class HarukoHaruhara extends AnimatorCard
{
    public static final String ID = Register_Old(HarukoHaruhara.class);

    public HarukoHaruhara()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.FLCL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.DiscardFromHand(name, 1, true)
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
            if (playable.Count() > 0)
            {
                card = playable.Retrieve(AbstractDungeon.cardRandomRng);
            }
            else if (unplayable.Count() > 0)
            {
                card = unplayable.Retrieve(AbstractDungeon.cardRandomRng);
            }

            if (card != null)
            {
                GameActions.Bottom.PlayCard(card, player.hand, (AbstractMonster) target);
            }
        });
    }
}