package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class Destroyer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Destroyer.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int BURNING_AMOUNT = 1;
    public static final int BASE_DAMAGE = 14;

    public Destroyer()
    {
        super(DATA);

        Initialize(0, 0, 2, BASE_DAMAGE);

        SetAffinity_Blue(1);
        SetAffinity_Dark(2);
        SetAffinity_Red(2);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return GetSpecialVariableString(BURNING_AMOUNT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new DestroyerPower(p, magicNumber, upgraded ? 2 : 1, BURNING_AMOUNT));
    }

    public static class DestroyerPower extends AnimatorClickablePower
    {
        private int burning;

        public DestroyerPower(AbstractCreature owner, int amount, int uses, int burning)
        {
            super(owner, Destroyer.DATA, PowerTriggerConditionType.Energy, 1);

            this.triggerCondition.SetUses(uses, true, true);
            this.burning = burning;

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, BASE_DAMAGE, burning);
        }

        @Override
        protected void OnSamePowerApplied(AbstractPower power)
        {
            super.OnSamePowerApplied(power);

            burning += ((DestroyerPower)power).burning;
        }

        @Override
        public void playApplyPowerSfx()
        {
            SFX.Play(SFX.ORB_PLASMA_CHANNEL, 0.7f);
        }

        @Override
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer)
        {
            super.atEndOfTurnPreEndTurnCards(isPlayer);

            final int[] damage = DamageInfo.createDamageMatrix(BASE_DAMAGE, true, false);
            GameActions.Bottom.SFX(SFX.ATTACK_DEFECT_BEAM, 0.65f, 0.7f);
            GameActions.Bottom.VFX(VFX.SweepingBeam(owner.hb, VFX.FlipHorizontally(), new Color(1f, 0, 0f, 1f)), 0.3f);
            GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.FIRE)
            .SetDamageEffect((c, __) -> GameActions.Top.ApplyBurning(owner, c, burning));

            flashWithoutSound();
            ReducePower(1);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            playApplyPowerSfx();
            IncreasePower(1);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(burning, Colors.Gold(c.a));
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new DestroyerPower(owner, amount, triggerCondition.baseUses, burning);
        }
    }
}