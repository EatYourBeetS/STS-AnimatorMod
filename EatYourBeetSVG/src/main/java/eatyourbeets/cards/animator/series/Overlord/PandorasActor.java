package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class PandorasActor extends AnimatorCard
{
    public static final EYBCardData DATA = Register(PandorasActor.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public PandorasActor()
    {
        super(DATA);

        Initialize(0, 4, 4);
        SetUpgrade(0, 2, 2);

        SetAffinity_Star(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainBlock(magicNumber);
        }
    }
}