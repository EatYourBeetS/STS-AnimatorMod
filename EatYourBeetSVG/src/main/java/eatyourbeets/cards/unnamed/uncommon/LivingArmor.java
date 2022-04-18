package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.UnnamedAttachmentPower;
import eatyourbeets.utilities.GameActions;

public class LivingArmor extends UnnamedCard
{
    public static final EYBCardData DATA = Register(LivingArmor.class)
            .SetMaxCopies(2)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.Minion);

    public LivingArmor()
    {
        super(DATA);

        Initialize(0, 7, 3, 2);
        SetUpgrade(0, 4);

        SetExhaust(true);
        SetAttachment(true);
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
        GameActions.Bottom.GainBlock(block);

        if (IsSolo() || m == null)
        {
            GameActions.Bottom.GainPlatedArmor(magicNumber);
        }
        else
        {
            GameActions.Bottom.GainDexterity(player, m, secondaryValue);
            GameActions.Bottom.StackPower(new LivingArmorPower(m, this, secondaryValue));
        }
    }

    public static class LivingArmorPower extends UnnamedAttachmentPower
    {
        public LivingArmorPower(AbstractCreature owner, UnnamedCard card, int amount)
        {
            super(owner, card);

            Initialize(amount);
        }

        @Override
        protected UnnamedAttachmentPower MakeCopy()
        {
            return new LivingArmorPower(owner, originalCard, amount);
        }

        @Override
        public int onHeal(int healAmount, boolean increasingMaxHP)
        {
            if (healAmount > 0 && !increasingMaxHP)
            {
                GameActions.Bottom.ModifyDollMaxHP(owner, amount);
                flashWithoutSound();
            }

            return super.onHeal(healAmount, increasingMaxHP);
        }
    }
}