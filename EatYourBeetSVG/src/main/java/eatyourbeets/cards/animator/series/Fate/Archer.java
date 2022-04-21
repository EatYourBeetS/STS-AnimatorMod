package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Archer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Archer.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Archer()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Motivate();
        }

        GameActions.Bottom.StackPower(new ArcherPower(p, magicNumber));
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        for (AbstractMonster m : GameUtilities.GetEnemies(true))
        {
            if (GameUtilities.GetDebuffsCount(m.powers) < secondaryValue)
            {
                return false;
            }
        }

        return true;
    }

    public class ArcherPower extends AnimatorPower
    {
        public ArcherPower(AbstractCreature owner, int amount)
        {
            super(owner, Archer.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            SetEnabled(true);
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                final int debuffs = GameUtilities.GetDebuffsCount(m.powers);
                for (int i = 0; i < debuffs; i++)
                {
                    GameActions.Bottom.VFX(VFX.ThrowDagger(m.hb, 0.2f));
                    GameActions.Bottom.DealDamage(owner, m, amount, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                    .SetVFX(true, true);
                }
            }

            this.flash();
        }
    }
}