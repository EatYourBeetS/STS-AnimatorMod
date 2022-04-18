package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NobleFencer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NobleFencer.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public NobleFencer()
    {
        super(DATA);

        Initialize(0, 2, 3);

        SetAffinity_Green(2);
        SetAffinity_Blue(2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        final AbstractOrb orb = GameUtilities.GetFirstOrb(Lightning.ORB_ID);
        if (orb != null)
        {
            orb.showEvokeValue();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (info.TryActivateStarter())
        {
            GameActions.Bottom.GainAgility(1);
            GameActions.Bottom.GainIntellect(1);
        }

        GameActions.Bottom.EvokeOrb(1).SetFilter(Lightning.class::isInstance);
        GameActions.Bottom.StackPower(new NobleFencerPower(p, 3));
    }

    public class NobleFencerPower extends AnimatorPower
    {
        public NobleFencerPower(AbstractCreature owner, int amount)
        {
            super(owner, NobleFencer.DATA);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}