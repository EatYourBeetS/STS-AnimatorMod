package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KaijiItou_RestrictedRock extends PCLCard
{
    public static final PCLCardData DATA = Register(KaijiItou_RestrictedRock.class).SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.AoE).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Kaiji);


    public KaijiItou_RestrictedRock()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Orange(1,0,0);
        SetAffinity_Green(1, 0 ,0);

        SetExhaust(true);
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents())
            {
                if (!intent.IsAttacking())
                {
                    return false;
                }
            }
        }

        return true;
    }
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
        PCLActions.Bottom.StackPower(new KaijiItou_RestrictedRockPower(p, secondaryValue));
    }

    public static class KaijiItou_RestrictedRockPower extends PCLPower
    {
        private static final int MODIFIER = 25;

        public KaijiItou_RestrictedRockPower(AbstractCreature owner, int amount)
        {
            super(owner, KaijiItou_RestrictedRock.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.AddEffectBonus(WeakPower.POWER_ID, MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.AddEffectBonus(WeakPower.POWER_ID, -MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}