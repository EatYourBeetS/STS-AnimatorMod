package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Boros extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Boros.class)
            .SetPower(4, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    private static final int POWER_DD_COST = 17;
    private static final int POWER_ENERGY_GAIN = 2;
    private static final int POWER_DD_MULTI = 4;

    public Boros()
    {
        super(DATA);

        Initialize(0, 0, 4, POWER_DD_COST);
        SetCostUpgrade(-1);

        SetAffinity_Red(2);
        SetAffinity_Green(1);
        SetAffinity_Dark(2);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(POWER_ENERGY_GAIN);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new BorosPower(p, 1));
    }

    public static class BorosPower extends AnimatorClickablePower
    {
        public BorosPower(AbstractCreature owner, int amount)
        {
            super(owner, Boros.DATA, PowerTriggerConditionType.TakeDelayedDamage, POWER_DD_COST);

            this.triggerCondition.SetUses(1, false, false);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            if (!CombatStats.CanActivateLimited(Boros.DATA.ID))
            {
                this.triggerCondition.SetUses(0, false, false);
            }
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, POWER_ENERGY_GAIN, amount * POWER_DD_MULTI, amount);
        }

        @Override
        public void playApplyPowerSfx()
        {
            SFX.Play(SFX.ATTACK_MAGIC_SLOW_1, 0.65f, 0.75f, 0.85f);
            SFX.Play(SFX.ORB_LIGHTNING_EVOKE, 0.45f, 0.5f, 1.05f);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (card.type == AbstractCard.CardType.POWER && GameUtilities.CanPlayTwice(card))
            {
                GameActions.Top.PlayCopy(card, JUtils.SafeCast(action.target, AbstractMonster.class));
                if (card.costForTurn > 0)
                {
                    GameActions.Top.TakeDamageAtEndOfTurn(card.costForTurn * amount * POWER_DD_MULTI);
                }

                this.flashWithoutSound();
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.TakeDamageAtEndOfTurn(POWER_DD_COST);
            GameActions.Bottom.GainEnergy(POWER_ENERGY_GAIN);
            playApplyPowerSfx();
        }
    }
}