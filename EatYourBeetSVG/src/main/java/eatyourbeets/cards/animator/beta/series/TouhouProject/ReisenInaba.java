package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class ReisenInaba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ReisenInaba.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage(true);

    public ReisenInaba()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(new CreateRandomCurses(1, p.hand)).AddCallback(card -> {
            GameActions.Bottom.ApplyPower(new ReisenInabaPower(player, card));
        });
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ExhaustFromPile(name, secondaryValue, player.hand, player.discardPile, player.drawPile).SetOptions(false, true);
    }

    public static class ReisenInabaPower extends AnimatorPower
    {
        public AbstractCard card;

        public ReisenInabaPower(AbstractPlayer owner, AbstractCard card)
        {
            super(owner, ReisenInaba.DATA);

            this.card = card;
            updateDescription();
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            if (this.card == card) {
                flashWithoutSound();
                final RandomizedList<AbstractCard> pool = GameUtilities.GetCardPoolInCombat(CardRarity.COMMON);
                pool.AddAll(GameUtilities.GetCardPoolInCombat(CardRarity.UNCOMMON).GetInnerList());
                pool.AddAll(GameUtilities.GetCardPoolInCombat(CardRarity.RARE).GetInnerList());
                final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while (choice.size() < 3 && pool.Size() > 0)
                {
                    AbstractCard temp = pool.Retrieve(rng);
                    if (!(temp.cardID.equals(card.cardID) || temp.tags.contains(AbstractCard.CardTags.HEALING) || temp.tags.contains(GR.Enums.CardTags.VOLATILE))) {
                        choice.addToTop(temp.makeCopy());
                    }
                }

                GameActions.Bottom.SelectFromPile(name, 1, choice)
                        .SetOptions(false, false)
                        .AddCallback(cards ->
                        {
                            GameActions.Bottom.MakeCardInHand(cards.get(0));
                            GameActions.Bottom.Add(new RefreshHandLayout());
                        });
                RemovePower();
            }

        }


    }
}

