package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class KaijiItou_RestrictedScissors extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaijiItou_RestrictedScissors.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Kaiji);


    public KaijiItou_RestrictedScissors()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Earth(2,0,0);
        SetAffinity_Fire(1,0,0);

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
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        GameActions.Bottom.StackPower(new KaijiItou_RestrictedScissorsPower(p, secondaryValue));
    }

    public static class KaijiItou_RestrictedScissorsPower extends AnimatorPower
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

            AnimatorVulnerablePower.AddEnemyModifier(MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            AnimatorVulnerablePower.AddEnemyModifier(-MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}