package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Pride extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Pride.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.Normal)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Pride()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);

        SetAffinity_Star(1, 1, 0);

        SetEvokeOrbCount(magicNumber);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyConstricted(p, m, secondaryValue);
        GameActions.Bottom.ChannelOrbs(Dark::new, magicNumber);
        GameActions.Bottom.StackPower(new PridePower(p));
    }

    public static class PridePower extends AnimatorPower
    {
        public PridePower(AbstractPlayer owner)
        {
            super(owner, Pride.DATA);

            this.priority = -99;

            Initialize(1);
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
        {
            final ArrayList<AbstractOrb> darkOrbs = new ArrayList<>();
            for (AbstractOrb orb : player.orbs)
            {
                if (Dark.ORB_ID.equals(orb.ID) && orb.evokeAmount > 0)
                {
                    darkOrbs.add(orb);
                }
            }

            if (darkOrbs.size() > 0)
            {
                if (damageAmount > 0)
                {
                    damageAmount = AbsorbDamage(damageAmount, darkOrbs);
                    flashWithoutSound();
                }
            }

            return super.onAttackedToChangeDamage(info, damageAmount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            RemovePower();
        }

        private int AbsorbDamage(int damage, ArrayList<AbstractOrb> darkOrbs)
        {
            final AbstractOrb next = darkOrbs.get(0);
            final float temp = damage;

            damage = Math.max(0, damage - next.evokeAmount);
            next.evokeAmount -= temp;

            if (next.evokeAmount <= 0)
            {
                darkOrbs.remove(next);
                next.evokeAmount = 0;
                GameActions.Top.Add(new EvokeSpecificOrbAction(next));
            }

            return (damage > 0 && darkOrbs.size() > 0) ? AbsorbDamage(damage, darkOrbs) : damage;
        }
    }
}