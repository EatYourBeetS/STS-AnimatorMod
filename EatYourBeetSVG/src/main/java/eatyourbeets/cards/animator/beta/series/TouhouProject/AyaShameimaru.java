package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class AyaShameimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AyaShameimaru.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    private int bonusBlock = 0;

    public AyaShameimaru()
    {
        super(DATA);

        Initialize(0, 4, 3, 1);
        SetUpgrade(0, 1, 0, 1);
        SetAffinity_Green(1, 1, 0);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID)
        .RequireNeutralStance(true)
        .AddCallback(stance ->
        {
            if (stance != null)
            {
                GameActions.Bottom.Flash(this);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        baseBlock += magicNumber;

        GameActions.Bottom.GainBlur(secondaryValue);
    }
}

