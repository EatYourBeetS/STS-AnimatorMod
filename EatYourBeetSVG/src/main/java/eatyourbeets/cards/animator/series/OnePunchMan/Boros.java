package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.subscribers.OnAfterCardDrawnSubscriber;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Boros extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Boros.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 3;
    private static final int RECOVER_HP_AMOUNT = 14;

    public Boros()
    {
        super(DATA);

        Initialize(0, 0, 2, RECOVER_HP_AMOUNT);

        SetAffinity_Red(2);
        SetAffinity_Green(1);
        SetAffinity_Dark(2);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(POWER_ENERGY_COST);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new BorosPower(p, magicNumber));
    }

    public static class BorosPower extends AnimatorClickablePower implements OnAfterCardDrawnSubscriber, OnCardCreatedSubscriber
    {
        protected boolean playPowersTwice;

        public BorosPower(AbstractCreature owner, int amount)
        {
            super(owner, Boros.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);

            this.triggerCondition.SetUses(1, false, false);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAfterCardDrawn.Subscribe(this);
            CombatStats.onCardCreated.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAfterCardDrawn.Unsubscribe(this);
            CombatStats.onCardCreated.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, RECOVER_HP_AMOUNT, amount);
        }

        @Override
        public void playApplyPowerSfx()
        {
            SFX.Play(SFX.ATTACK_MAGIC_SLOW_1, 0.65f, 0.75f, 0.85f);
            SFX.Play(SFX.ORB_LIGHTNING_EVOKE, 0.45f, 0.5f, 1.05f);
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle)
        {
            Activate(card);
        }

        @Override
        public void OnAfterCardDrawn(AbstractCard card)
        {
            Activate(card);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (playPowersTwice && (card.type == AbstractCard.CardType.POWER) && GameUtilities.CanPlayTwice(card))
            {
                GameActions.Top.PlayCopy(card, JUtils.SafeCast(action.target, AbstractMonster.class));
                this.flashWithoutSound();
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            if (CombatStats.TryActivateLimited(ID))
            {
                GameActions.Bottom.RecoverHP(RECOVER_HP_AMOUNT);
                playPowersTwice = true;
                flash();
            }
        }

        protected void Activate(AbstractCard card)
        {
            if (player.hand.contains(card) && GameUtilities.IsHighCost(card))
            {
                GameActions.Bottom.GainBlock(amount).SetVFX(true, true);
                flashWithoutSound();
            }
        }
    }
}