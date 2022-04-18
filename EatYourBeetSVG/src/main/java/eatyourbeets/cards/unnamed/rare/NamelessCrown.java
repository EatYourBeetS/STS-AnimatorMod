package eatyourbeets.cards.unnamed.rare;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.unnamed.LoseSummonSlot;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.interfaces.subscribers.OnPlayerMinionActionSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.UnnamedAttachmentPower;
import eatyourbeets.utilities.GameActions;

public class NamelessCrown extends UnnamedCard
{
    public static final EYBCardData DATA = Register(NamelessCrown.class)
            .SetMaxCopies(1)
            .SetSkill(3, CardRarity.RARE, EYBCardTarget.None);

    public NamelessCrown()
    {
        super(DATA);

        Initialize(0, 0, 3, 9);

        SetAttachment(true);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(IsSolo() ? EYBCardTarget.None : EYBCardTarget.Minion);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        if (IsSolo() || m == null)
        {
            GameActions.Bottom.Add(new LoseSummonSlot(CombatStats.Dolls.MaxSlots));
            GameActions.Bottom.GainStrength(magicNumber);
            GameActions.Bottom.GainDexterity(magicNumber);
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
        else
        {
            GameActions.Bottom.StackPower(new NamelessCrownPower(m, this, 1));
        }
    }

    public static class NamelessCrownPower extends UnnamedAttachmentPower implements OnPlayerMinionActionSubscriber
    {
        public NamelessCrownPower(AbstractCreature owner, UnnamedCard card, int amount)
        {
            super(owner, card);

            canBeZero = true;

            Initialize(amount);
        }

        @Override
        protected UnnamedAttachmentPower MakeCopy()
        {
            return new NamelessCrownPower(owner, originalCard, amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onPlayerMinionAction.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onPlayerMinionAction.Unsubscribe(this);
        }

        @Override
        public float atDamageFinalGive(float damage, DamageInfo.DamageType type)
        {
            damage = super.atDamageFinalGive(damage, type);
            return (enabled && type == DamageInfo.DamageType.NORMAL) ? damage * 2 : damage;
        }

        @Override
        public float modifyBlockLast(float blockAmount)
        {
            blockAmount = super.modifyBlockLast(blockAmount);
            return enabled ? blockAmount * 2 : blockAmount;
        }

        @Override
        public void OnMinionActivation(AbstractCreature minion, boolean endOfTurn)
        {
            if (minion == owner)
            {
                GameActions.Delayed.Callback(() ->
                {
                    reducePower(1, false);
                    SetEnabled(amount > 0);
                });
            }
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
            SetEnabled(amount > 0);
        }
    }
}