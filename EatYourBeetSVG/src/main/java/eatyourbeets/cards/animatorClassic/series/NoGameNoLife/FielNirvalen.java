package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_GainOrBoost;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class FielNirvalen extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(FielNirvalen.class).SetPower(1, CardRarity.UNCOMMON).SetMaxCopies(3);
    public static final int SCRY_AMOUNT = 2;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public FielNirvalen()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetSeries(CardSeries.NoGameNoLife);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.StackPower(new FielNirvalenPower(p, SCRY_AMOUNT));

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_GainOrBoost(Affinity.Green, 1, true));
            choices.AddEffect(new GenericEffect_GainOrBoost(Affinity.Blue, 1, true));
            choices.AddEffect(new GenericEffect_GainOrBoost(Affinity.Red, 1, true));
        }

        choices.Select(secondaryValue, m);
    }

    public static class FielNirvalenPower extends AnimatorPower implements OnShuffleSubscriber
    {
        public FielNirvalenPower(AbstractCreature owner, int amount)
        {
            super(owner, FielNirvalen.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            CombatStats.onShuffle.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            CombatStats.onShuffle.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            enabled = true;
        }

        @Override
        public void OnShuffle(boolean triggerRelics)
        {
            if (!owner.powers.contains(this))
            {
                CombatStats.onShuffle.Unsubscribe(this);
                return;
            }

            if (enabled)
            {
                GameActions.Bottom.Scry(amount);
                enabled = false;
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}