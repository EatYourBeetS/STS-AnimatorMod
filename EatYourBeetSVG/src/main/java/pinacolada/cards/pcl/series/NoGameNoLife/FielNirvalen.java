package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class FielNirvalen extends PCLCard
{
    public static final PCLCardData DATA = Register(FielNirvalen.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (PCLCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), true);
                }
            });

    public FielNirvalen()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(secondaryValue);
        PCLActions.Bottom.StackPower(new FielNirvalenPower(p, magicNumber, magicNumber, secondaryValue));
    }

    public static class FielNirvalenPower extends PCLPower implements OnShuffleSubscriber, OnSynergySubscriber
    {
        private boolean canObtain = true;
        private final int secondaryAmount;
        private final int choices;

        public FielNirvalenPower(AbstractCreature owner, int amount, int secondaryAmount, int choices)
        {
            super(owner, FielNirvalen.DATA);

            this.amount = amount;
            this.secondaryAmount = secondaryAmount;
            this.choices = Math.min(AffinityToken.GetCards().size(), choices);

            Initialize(amount);
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            PCLCombatStats.onSynergy.Subscribe(this);
            PCLCombatStats.onShuffle.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            PCLCombatStats.onSynergy.Unsubscribe(this);
            PCLCombatStats.onShuffle.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            canObtain = true;
            ResetAmount();
        }

        @Override
        public void OnShuffle(boolean triggerRelics)
        {
            if (!owner.powers.contains(this))
            {
                PCLCombatStats.onShuffle.Unsubscribe(this);
                return;
            }

            if (canObtain)
            {
                PCLActions.Bottom.Add(AffinityToken.SelectTokenAction(name, secondaryAmount, choices)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards)
                            {
                                PCLActions.Bottom.MakeCardInDrawPile(c);
                            }
                        }));
                canObtain = false;
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, secondaryAmount);
        }

        @Override
        public void OnSynergy(AbstractCard card) {
            if (amount > 0)
            {
                PCLActions.Bottom.Scry(1);
                this.amount -= 1;
                updateDescription();
                flash();
            }
        }
    }
}