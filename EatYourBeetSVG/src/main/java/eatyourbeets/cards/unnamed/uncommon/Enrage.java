package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.UnnamedAttachmentPower;
import eatyourbeets.utilities.GameActions;

public class Enrage extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Enrage.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Minion);

    public Enrage()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 2);

        SetExhaust(true);
        SetAttachment(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ModifyDollMaxHP(m, magicNumber);
        GameActions.Bottom.StackPower(new EnragePower(m, this, secondaryValue));
    }

    public static class EnragePower extends UnnamedAttachmentPower
    {
        public EnragePower(AbstractCreature owner, UnnamedCard card, int amount)
        {
            super(owner, card);

            Initialize(amount);
        }

        @Override
        protected UnnamedAttachmentPower MakeCopy()
        {
            return new EnragePower(owner, originalCard, amount);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (damageAmount > 0)
            {
                GameActions.Bottom.GainStrength(owner, owner, amount);
                flashWithoutSound();
            }
        }
    }
}