package eatyourbeets.cards.animatorClassic.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Hitei extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Hitei.class).SetSeriesFromClassPackage().SetPower(1, CardRarity.UNCOMMON);

    public Hitei()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 1, 0);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HiteiPower(p, magicNumber, secondaryValue));
    }

    public static class HiteiPower extends AnimatorPower
    {
        private int draw;

        public HiteiPower(AbstractPlayer owner, int amount, int draw)
        {
            super(owner, Hitei.DATA);

            Initialize(amount);
            this.draw = draw;
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, draw);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Bottom.Callback(() ->
            {
                final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                final RandomizedList<AbstractCard> pile;
                if (player.drawPile.size() < amount)
                {
                    group.group.addAll(player.drawPile.group);
                    pile = new RandomizedList<>(player.discardPile.group);
                }
                else
                {
                    pile = new RandomizedList<>(player.drawPile.group);
                }

                while (group.size() < amount && pile.Size() > 0)
                {
                    group.addToTop(pile.Retrieve(rng));
                }

                if (group.size() >= 0)
                {
                    GameActions.Top.ExhaustFromPile(name, 1, group)
                            .ShowEffect(true, true)
                            .SetOptions(false, false);
                }

                GameActions.Bottom.Draw(draw);
            });

            this.flash();
        }
    }
}