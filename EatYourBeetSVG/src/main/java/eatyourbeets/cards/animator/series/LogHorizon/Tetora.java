package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Tetora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tetora.class).SetPower(0, CardRarity.UNCOMMON);

    public Tetora()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, 2);

        SetSpellcaster();
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && JUtils.Count(GameUtilities.GetOtherCardsInHand(this), this::HasSynergy) >= magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new TetoraPower(p, secondaryValue));
    }

    public static class TetoraPower extends AnimatorPower implements OnSynergySubscriber
    {
        public static final int BASE_SYNERGY_COUNTER = 2;

        private int synergies;

        public TetoraPower(AbstractPlayer owner, int amount)
        {
            super(owner, Tetora.DATA);

            this.amount = amount;
            this.synergies = BASE_SYNERGY_COUNTER;
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
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            this.enabled = true;
            this.synergies = BASE_SYNERGY_COUNTER;
            updateDescription();
        }

        @Override
        public void OnSynergy(AbstractCard card)
        {
            if (!enabled)
            {
                return;
            }

            if (synergies-- <= 0)
            {
                GameActions.Top.GainBlock(amount);
                enabled = false;
            }

            updateDescription();
            flash();
        }

        @Override
        public void updateDescription()
        {
            if (enabled)
            {
                description = FormatDescription(0, amount, synergies, "");
            }
            else
            {
                description = FormatDescription(1);
            }
        }
    }
}