package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.powers.ApplyPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Gobta extends PCLCard
{
    public static final PCLCardData DATA = Register(Gobta.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Gobta()
    {
        super(DATA);

        Initialize(5, 0, 1, 3);
        SetUpgrade(3, 0, 0, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.ApplyPoison(TargetHelper.RandomEnemy(), secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        if (info.TryActivateSemiLimited()) {
            PCLActions.Bottom.StackPower(new GobtaPower(p, 1));
        }
    }

    public static class GobtaPower extends PCLPower
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

            if (PCLGameUtilities.IsPlayer(source) && PCLGameUtilities.IsCommonDebuff(power))
            {
                power.amount += 1;

                final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
                if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
                {
                    action.amount += 1;
                }
                else
                {
                    PCLJUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
                }
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}