package eatyourbeets.relics.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.JUtils;

public class OddDevice_1 extends OddDevice
{
    public static final String ID = CreateFullID(OddDevice_1.class);
    public static final int[] DAMAGE_MODIFIERS = { 2, 2, 4 };

    public OddDevice_1()
    {
        super(ID, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    protected String GetMainDescription(int effectIndex)
    {
        return FormatDescription(effectIndex, DAMAGE_MODIFIERS[effectIndex - 1]);
    }

    protected float ModifyDamage(EYBAttackType attackType, float damage)
    {
        final int mod = DAMAGE_MODIFIERS[counter - 1];
        switch (attackType)
        {
            case Ranged: return (counter == 1) ? (damage + mod) : damage;
            case Elemental: return (counter == 2) ? (damage + mod) : damage;
            case Piercing: return (counter == 3) ? (damage + mod) : damage;
        }

        return damage;
    }

    public static class OddDevice_1Power extends AnimatorPower implements Hidden, InvisiblePower
    {
        private OddDevice_1 relic;

        public OddDevice_1Power(AbstractCreature owner, OddDevice_1 relic)
        {
            super(owner, relic);

            this.canBeZero = true;
            this.relic = relic;
        }

        @Override
        public void updateDescription()
        {
            this.description = "";
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card)
        {
            damage = super.atDamageGive(damage, type, card);

            final EYBCard c = JUtils.SafeCast(card, EYBCard.class);
            if (c != null && c.type == AbstractCard.CardType.ATTACK)
            {
                return relic.ModifyDamage(c.attackType, damage);
            }

            return damage;
        }
    }
}