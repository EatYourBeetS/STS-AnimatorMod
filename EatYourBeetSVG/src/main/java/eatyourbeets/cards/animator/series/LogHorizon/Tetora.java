package eatyourbeets.cards.animator.series.LogHorizon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class Tetora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tetora.class)
            .SetPower(0, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static final int POWER_CARD_COST = 4;
    private static final int USE_COST = 7;
    private static final int SYNERGY_TIMES = 2;

    public Tetora()
    {
        super(DATA);

        Initialize(0, 0, 3, POWER_CARD_COST);

        SetAffinity_Star(1);

        SetHarmonic(true);

        SetAffinityRequirement(Affinity.General, USE_COST);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
        SetAffinityRequirement(Affinity.General, 9);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && CheckAffinity(Affinity.General);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            GameActions.Bottom.StackPower(new TetoraPower(p, magicNumber, SYNERGY_TIMES));
        });
    }

    public static class TetoraPower extends AnimatorClickablePower implements OnSynergySubscriber
    {
        private final int baseSecondaryValue;
        private int secondaryValue;
        private boolean active;

        public TetoraPower(AbstractPlayer owner, int amount, int secondaryValue)
        {
            super(owner, Tetora.DATA, PowerTriggerConditionType.Affinity, POWER_CARD_COST);

            this.triggerCondition.SetUses(1,true, false);

            this.amount = amount;
            this.baseSecondaryValue = this.secondaryValue = secondaryValue;

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
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);
            GameActions.Bottom.Draw(1).AddCallback(() -> {
                GameActions.Bottom.SelectFromHand(name, 1, false)
                        .SetOptions(true,true,true)
                        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
                        .AddCallback(cards ->
                        {
                            for (int i = cards.size() - 1; i >= 0; i--)
                            {
                                GameActions.Top.MoveCard(cards.get(i), player.hand, player.drawPile)
                                        .SetDestination(CardSelection.Top);
                            }
                        });
            });
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            secondaryValue = baseSecondaryValue;
        }


        @Override
        public void OnSynergy(AbstractCard card)
        {
            if (secondaryValue > 0)
            {
                secondaryValue -= 1;
                for (int i = 0; i < amount; i++)
                {
                    GameActions.Bottom.GainRandomAffinityPower(1, false);
                }

                flash();
            }
        }


        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(secondaryValue, Settings.BLUE_TEXT_COLOR);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, secondaryValue, amount, POWER_CARD_COST);
        }
    }
}