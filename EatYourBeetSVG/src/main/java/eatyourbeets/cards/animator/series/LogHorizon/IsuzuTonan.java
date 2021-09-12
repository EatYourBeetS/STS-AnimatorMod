package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class IsuzuTonan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IsuzuTonan.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsuzuTonan()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new IsuzuTonanPower(p, magicNumber));
    }

    public static class IsuzuTonanPower extends AnimatorPower implements OnSynergySubscriber
    {
        public IsuzuTonanPower(AbstractPlayer owner, int amount)
        {
            super(owner, IsuzuTonan.DATA);

            this.amount = amount;
            this.isTurnBased = true;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void OnSynergy(AbstractCard card)
        {
            if (card.type != CardType.ATTACK || card.freeToPlay() || card.costForTurn <= 0)
            {
                return;
            }

            GameActions.Last.SelectFromPile(name, 1, player.drawPile)
            .SetOptions(true, false)
            .SetFilter(card.costForTurn, (cost, c) -> c.type == CardType.ATTACK && c.costForTurn < cost)
            .AddCallback(c ->
            {
                if (c.size() > 0)
                {
                    GameActions.Top.PlayCard(c.get(0), player.drawPile, null);
                }
            });

            flash();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.ReducePower(this, 1);
        }
    }
}