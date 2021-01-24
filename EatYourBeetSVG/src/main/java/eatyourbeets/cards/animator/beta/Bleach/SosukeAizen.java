package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.PhasingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SosukeAizen extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SosukeAizen.class).SetSkill(-1, CardRarity.SPECIAL, EYBCardTarget.None);

    public SosukeAizen()
    {
        super(DATA);

        Initialize(0, 0, 30);
        SetUpgrade(0, 0, 10);

        SetMultiDamage(true);
        SetExhaust(true);
        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int energy = GameUtilities.UseXCostEnergy(this);

        if (energy > 0)
        {
            GameActions.Bottom.StackPower(new PhasingPower(p, energy));
        }

        GameActions.Bottom.StackPower(new SosukeAizenPower(p, magicNumber));
    }

    public static class SosukeAizenPower extends AnimatorPower
    {
        public SosukeAizenPower(AbstractPlayer owner, int amount)
        {
            super(owner, SosukeAizen.DATA);

            this.amount = amount;
            this.priority = 100; //This should be higher (as in later) than PhasingPower's priority.

            updateDescription();
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
        {
            if (info.output == 0 && info.owner instanceof AbstractMonster)
            {
                GameActions.Bottom.DealDamage(player, info.owner, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            }

            return super.onAttackedToChangeDamage(info, damageAmount);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();
            RemovePower();
        }
    }
}