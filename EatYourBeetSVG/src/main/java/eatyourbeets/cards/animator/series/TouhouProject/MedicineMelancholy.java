package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class MedicineMelancholy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MedicineMelancholy.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    public static final int POWER_COST = 2;

    public MedicineMelancholy()
    {
        super(DATA);

        Initialize(0, 0, 2, POWER_COST);

        SetAffinity_Green(1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new MedicineMelancholyPower(p, magicNumber));
    }

    public static class MedicineMelancholyPower extends AnimatorClickablePower
    {
        protected AbstractCreature lastEnemy = null;

        public MedicineMelancholyPower(AbstractCreature owner, int amount)
        {
            super(owner, MedicineMelancholy.DATA, PowerTriggerConditionType.Special, POWER_COST, MedicineMelancholyPower::CheckCondition, __ -> {});

            triggerCondition.SetUses(1, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount);
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null)
            {
                final PoisonPower poison = GameUtilities.GetPower(info.owner, PoisonPower.class);
                if (poison != null && lastEnemy != info.owner)
                {
                    GameActions.Top.Add(new PoisonLoseHpAction(info.owner, owner, poison.amount, AttackEffects.POISON));
                    lastEnemy = info.owner;
                }
            }

            return super.onAttacked(info, damageAmount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            lastEnemy = null;
        }

        private static boolean CheckCondition(int cost)
        {
            return GameUtilities.GetPowers(TargetHelper.Enemies(), PoisonPower.POWER_ID).size() > 0;
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            for (AbstractPower p : GameUtilities.GetPowers(TargetHelper.Enemies(), PoisonPower.POWER_ID))
            {
                GameActions.Bottom.ApplyPoison(owner, p.owner, triggerCondition.requiredAmount);
                GameActions.Bottom.ApplyWeak(owner, p.owner, 1);
            }
        }

        @Override
        public void update(int slot)
        {
            super.update(slot);

            if (clickable && enabled && hb.hovered)
            {
                for (AbstractPower p : GameUtilities.GetPowers(TargetHelper.Enemies(), PoisonPower.POWER_ID))
                {
                    GameUtilities.GetIntent((AbstractMonster) p.owner).AddWeak();
                }
            }
        }
    }
}