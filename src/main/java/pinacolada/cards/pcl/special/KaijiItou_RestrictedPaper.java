package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KaijiItou_RestrictedPaper extends PCLCard
{
    public static final PCLCardData DATA = Register(KaijiItou_RestrictedPaper.class).SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.AoE).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Kaiji);


    public KaijiItou_RestrictedPaper()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Orange(1,0,0);
        SetAffinity_Blue(1, 0 ,0);

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
                if (!intent.IsDefending())
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
        PCLActions.Bottom.ApplyLockOn(TargetHelper.Enemies(), magicNumber);
        PCLActions.Bottom.StackPower(new KaijiItou_RestrictedPaperPower(p, secondaryValue));
    }

    public static class KaijiItou_RestrictedPaperPower extends PCLPower
    {
        private static final int MODIFIER = 50;

        public KaijiItou_RestrictedPaperPower(AbstractCreature owner, int amount)
        {
            super(owner, KaijiItou_RestrictedPaper.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.AddEffectBonus(LockOnPower.POWER_ID, MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.AddEffectBonus(LockOnPower.POWER_ID, -MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}