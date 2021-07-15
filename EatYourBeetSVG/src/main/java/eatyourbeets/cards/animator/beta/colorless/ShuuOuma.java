package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.FetchFromPile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ShuuOuma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShuuOuma.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GuiltyCrown);

    public ShuuOuma()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetExhaust(true);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        FetchFromPile fetchAction = new FetchFromPile(name, 1, player.drawPile, player.discardPile);

        GameActions.Top.StackPower(new ShuuOumaPower(player, 1, name));

        GameActions.Top.Add(fetchAction
                .SetOptions(false, false)
                .SetFilter(c -> c.type.equals(CardType.POWER)));
    }

    public static class ShuuOumaPower extends AnimatorPower
    {
        private String name;

        public ShuuOumaPower(AbstractPlayer owner, int amount, String name)
        {
            super(owner, ShuuOuma.DATA);

            this.amount = amount;
            this.name = name;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            RemovePower();
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (usedCard.type.equals(CardType.POWER))
            {
                FetchFromPile fetchAction = new FetchFromPile(name, amount, player.drawPile, player.discardPile);

                GameActions.Top.Add(fetchAction
                        .SetOptions(true, false)
                        .SetFilter(c -> c.type.equals(CardType.POWER)));
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}