package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class AyaShameimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AyaShameimaru.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.Self)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();

    public AyaShameimaru()
    {
        super(DATA);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Green(1, 1, 1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.GainBlur(secondaryValue);
        GameActions.Bottom.StackPower(new AyaShameimaruPower(p));
    }

    public static class AyaShameimaruPower extends AnimatorClickablePower
    {
        public AyaShameimaruPower(AbstractCreature owner)
        {
            super(owner, AyaShameimaru.DATA, PowerTriggerConditionType.Energy, 1);

            this.triggerCondition.SetUses(1, false, false);

            Initialize(-1);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
            GameActions.Bottom.WaitRealtime(0.25f);
            RemovePower(GameActions.Last);
        }
    }
}

