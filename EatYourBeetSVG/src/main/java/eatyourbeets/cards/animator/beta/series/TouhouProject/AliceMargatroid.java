package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnTrySpendAffinitySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class AliceMargatroid extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AliceMargatroid.class).SetPower(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public AliceMargatroid()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1,0,0);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new AlicePower(p, magicNumber, secondaryValue));
    }

    public static class AlicePower extends AnimatorPower implements OnTrySpendAffinitySubscriber
    {
        private int secondaryValue;

        public AlicePower(AbstractCreature owner, int amount, int secondaryValue)
        {
            super(owner, AliceMargatroid.DATA);
            this.amount = amount;
            this.secondaryValue = secondaryValue;
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            CombatStats.onTrySpendAffinity.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            CombatStats.onTrySpendAffinity.Unsubscribe(this);
        }


        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurn();

            GameActions.Bottom.Callback(() -> {
                GameActions.Bottom.Draw(amount);
                GameActions.Bottom.SelectFromHand(name, 1, false)
                        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards)
                            {
                                GameActions.Top.MoveCard(c, AbstractDungeon.player.hand, AbstractDungeon.player.drawPile).SetDestination(CardSelection.Top);
                                if (c instanceof EYBCard) {
                                    CombatStats.Affinities.AddAffinities(((EYBCard) c).affinities);
                                }
                            }
                            GameActions.Bottom.Add(new RefreshHandLayout());
                        });
            });
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, secondaryValue);
        }

        @Override
        public int OnTrySpendAffinity(Affinity affinity, int amount, boolean isActuallySpending) {
            if (isActuallySpending) {
                GameActions.Bottom.GainBlock(secondaryValue);
            }
            return amount;
        }
    }
}

