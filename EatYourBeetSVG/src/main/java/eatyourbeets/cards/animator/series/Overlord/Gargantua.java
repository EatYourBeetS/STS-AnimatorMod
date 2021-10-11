package eatyourbeets.cards.animator.series.Overlord;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.orbs.EarthOrbPassiveAction;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Gargantua extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gargantua.class)
            .SetSkill(3, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Gargantua()
    {
        super(DATA);

        Initialize(0, 10, 1);
        SetUpgrade(0, 3, 1);

        SetAffinity_Earth(2);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        final Earth next = JUtils.SafeCast(GameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
        if (next != null) {
            GameActions.Bottom.GainTemporaryThorns(next.projectilesCount / 2);
            GameActions.Bottom.Add(new EarthOrbPassiveAction(next, -next.projectilesCount / 2));
            GameActions.Bottom.StackPower(new GargantuaPower(p, magicNumber));
        }

    }

    public static class GargantuaPower extends AnimatorPower implements OnTryApplyPowerListener
    {
        private boolean shouldNegate = false;
        public GargantuaPower(AbstractPlayer owner, int amount)
        {
            super(owner, Gargantua.DATA);

            this.priority = -99;

            Initialize(amount);
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
        {
            if (damageAmount > 0)
            {
                final Earth next = JUtils.SafeCast(GameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
                if (next != null) {
                    final int temp = damageAmount / Earth.PROJECTILE_DAMAGE;

                    damageAmount = Math.max(0, damageAmount - next.projectilesCount * Earth.PROJECTILE_DAMAGE);
                    GameActions.Top.Add(new EarthOrbPassiveAction(next, -temp));
                    if (next.projectilesCount <= 0)
                    {
                        next.projectilesCount = 0;
                        GameActions.Top.Add(new EvokeSpecificOrbAction(next));
                    }
                }
            }

            if (info.owner != null && info.owner.isPlayer != owner.isPlayer && damageAmount == 0)
            {
                shouldNegate = true;
                flashWithoutSound();
            }

            return super.onAttackedToChangeDamage(info, damageAmount);
        }

        @Override
        public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action)
        {
            if (target == owner && power.type == PowerType.DEBUFF && shouldNegate)
            {
                final ApplyPower applyPowerAction = JUtils.SafeCast(action, ApplyPower.class);
                if (applyPowerAction == null || !applyPowerAction.ignoreArtifact)
                {
                    GameActions.Top.SFX("NULLIFY_SFX");
                    flashWithoutSound();
                    shouldNegate = false;
                    return false;
                }
            }

            return true;
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            RemovePower();
        }
    }
}