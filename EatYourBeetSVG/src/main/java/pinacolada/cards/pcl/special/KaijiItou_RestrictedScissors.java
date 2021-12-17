package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.PCLPower;
import pinacolada.powers.replacement.PCLVulnerablePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KaijiItou_RestrictedScissors extends PCLCard
{
    public static final PCLCardData DATA = Register(KaijiItou_RestrictedScissors.class).SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.ALL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Kaiji);


    public KaijiItou_RestrictedScissors()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Orange(1,0,0);
        SetAffinity_Red(1, 0 ,0);

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
                if (!intent.IsDebuffing())
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
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        PCLActions.Bottom.StackPower(new KaijiItou_RestrictedScissorsPower(p, secondaryValue));
    }

    public static class KaijiItou_RestrictedScissorsPower extends PCLPower
    {
        private static final int MODIFIER = 50;

        public KaijiItou_RestrictedScissorsPower(AbstractCreature owner, int amount)
        {
            super(owner, KaijiItou_RestrictedScissors.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLVulnerablePower.AddEnemyModifier(MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLVulnerablePower.AddEnemyModifier(-MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}