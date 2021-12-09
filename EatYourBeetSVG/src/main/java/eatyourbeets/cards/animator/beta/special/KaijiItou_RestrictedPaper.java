package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.replacement.AnimatorLockOnPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class KaijiItou_RestrictedPaper extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaijiItou_RestrictedPaper.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Kaiji);


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
            for (EnemyIntent intent : GameUtilities.GetIntents())
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
        GameActions.Bottom.ApplyLockOn(TargetHelper.Enemies(), magicNumber);
        GameActions.Bottom.StackPower(new KaijiItou_RestrictedPaperPower(p, secondaryValue));
    }

    public static class KaijiItou_RestrictedPaperPower extends AnimatorPower
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

            AnimatorLockOnPower.AddEnemyModifier(MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            AnimatorLockOnPower.AddEnemyModifier(-MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}