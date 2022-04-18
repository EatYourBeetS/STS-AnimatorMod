package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.FlandreScarlet;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.CorruptionStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;

public class RemiliaScarlet extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RemiliaScarlet.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.TouhouProject_Remilia(FlandreScarlet.DATA, 3));
                data.AddPreview(new FlandreScarlet(), true);
            });
    public static final int RECOVER_AMOUNT = 6;

    public RemiliaScarlet()
    {
        super(DATA);

        Initialize(0, 0, RECOVER_AMOUNT);
        SetCostUpgrade(-1);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new RemiliaScarletPower(p, 1));
    }

    public static class RemiliaScarletPower extends AnimatorClickablePower
    {
        public RemiliaScarletPower(AbstractCreature owner, int amount)
        {
            super(owner, RemiliaScarlet.DATA, PowerTriggerConditionType.Exhaust, 1);

            triggerCondition.SetUses(1, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, RECOVER_AMOUNT, amount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.RecoverHP(RECOVER_AMOUNT);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (AgilityStance.IsActive())
            {
                GameActions.Bottom.GainAgility(1, true);
            }
            else if (ForceStance.IsActive())
            {
                GameActions.Bottom.GainForce(1, true);
            }
            else if (IntellectStance.IsActive())
            {
                GameActions.Bottom.GainIntellect(1, true);
            }
            else if (CorruptionStance.IsActive())
            {
                GameActions.Bottom.GainCorruption(1, true);
            }
            else
            {
                GameActions.Bottom.GainRandomAffinityPower(1, true);
            }

            flash();
        }
    }
}