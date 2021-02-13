package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YukariYakumo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YukariYakumo.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.Self);

    public YukariYakumo()
    {
        super(DATA);

        Initialize(0, 0, 5, 3);
        SetUpgrade(0, 0, 1, 1);
        SetScaling(0, 0, 0);

        SetExhaust(true);
        SetSpellcaster();
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new WeakPower(player, magicNumber, false));
        GameActions.Bottom.StackPower(new FrailPower(player, magicNumber, false));
        GameActions.Bottom.StackPower(new VulnerablePower(player, magicNumber, false));
        GameActions.Bottom.StackPower(new InvertPower(player, secondaryValue));
    }

    public static class InvertPower extends AnimatorPower
    {

        public InvertPower(AbstractCreature owner, int amount)
        {
            super(owner, YukariYakumo.DATA);
            this.amount = amount;
            this.isTurnBased = true;
            this.priority = 99;
            updateDescription();
        }

        @Override
        public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
        {
            AbstractPower power = GameUtilities.GetPower(owner, VulnerablePower.POWER_ID);
            if (power != null && damage > 0)
            {
                damage /= (float)Math.pow(power.atDamageReceive(1, damageType), 2);
            }

            return damage;
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
            AbstractPower power = GameUtilities.GetPower(owner, WeakPower.POWER_ID);
            if (power != null && damage > 0)
            {
                damage /= (float)Math.pow(power.atDamageGive(1, damageType), 2);
            }
            return damage;
        }

        @Override
        public float modifyBlock(float blockAmount) {
            AbstractPower power = GameUtilities.GetPower(owner, FrailPower.POWER_ID);
            if (power != null && blockAmount > 0)
            {
                blockAmount /= (float)Math.pow(power.modifyBlock(1), 2);
            }
            return blockAmount;
        }

        @Override
        public void atEndOfRound()
        {
            super.atEndOfRound();

            GameActions.Bottom.ReducePower(this, 1);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }


}

