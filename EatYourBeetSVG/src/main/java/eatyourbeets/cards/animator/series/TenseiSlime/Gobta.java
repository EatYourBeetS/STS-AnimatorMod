package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class Gobta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gobta.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Gobta()
    {
        super(DATA);

        Initialize(5, 0, 2, 3);
        SetUpgrade(2, 0, 1);

        SetAffinity_Fire(1);
        SetAffinity_Air(1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ApplyPoison(TargetHelper.RandomEnemy(), secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.StackPower(new GobtaPower(p, 2));
    }

    public class GobtaPower extends AnimatorPower
    {

        public GobtaPower(AbstractCreature owner, int amount)
        {
            super(owner, Gobta.DATA);

            Initialize(amount);
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(power, target, source);

            if (GameUtilities.IsPlayer(source) && GameUtilities.IsCommonDebuff(power))
            {
                power.amount += 1;

                final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
                if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
                {
                    action.amount += 1;
                }
                else
                {
                    JUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
                }

                ReducePower(1);
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}