package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class IsuzuTonan extends PCLCard
{
    public static final PCLCardData DATA = Register(IsuzuTonan.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsuzuTonan()
    {
        super(DATA);

        Initialize(0, 2, 1, 1);

        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 1);
        SetCostUpgrade(-1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Cycle(name, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.StackPower(new IsuzuTonanPower(p, magicNumber));
    }

    public static class IsuzuTonanPower extends PCLPower implements OnSynergySubscriber
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

            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void OnSynergy(AbstractCard card)
        {
            if (card.type != CardType.ATTACK || card.freeToPlay() || card.costForTurn <= 0)
            {
                return;
            }

            PCLActions.Last.SelectFromPile(name, 1, player.drawPile)
            .SetOptions(true, false)
            .SetFilter(card.costForTurn, (cost, c) -> c.type == CardType.ATTACK && c.costForTurn < cost)
            .AddCallback(c ->
            {
                if (c.size() > 0)
                {
                    PCLActions.Top.PlayCard(c.get(0), player.drawPile, null);
                }
            });

            flash();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            PCLActions.Bottom.ReducePower(this, 1);
        }
    }
}