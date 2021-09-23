package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ciel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ciel.class)
            .SetSkill(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Lu(), false));
    public static final int LOCK_ON = 2;
    public static final int BLUR = 1;

    public Ciel()
    {
        super(DATA);

        Initialize(0, 4, 6, 2);
        SetUpgrade(0, 0, 2, 0);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Dark(2);

        SetAffinityRequirement(Affinity.Green, 3);
        SetAffinityRequirement(Affinity.Blue, 2);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    protected String GetRawDescription()
    {
        return super.GetRawDescription(LOCK_ON, BLUR);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ModifyAllCopies(Lu.DATA.ID)
        .AddCallback(c ->
        {
            GameUtilities.IncreaseDamage(c, magicNumber, false);
            c.flash();
        });

        if (CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.StackPower(new LockOnPower(m, LOCK_ON));
        }

        if (CheckAffinity(Affinity.Green))
        {
            GameActions.Bottom.GainBlur(BLUR);
        }
    }
}