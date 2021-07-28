package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ciel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ciel.class)
            .SetSkill(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Lu(), true);
    }

    public Ciel()
    {
        super(DATA);

        Initialize(0, 4, 6, 2);
        SetUpgrade(0, 0, 2, 0);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Dark(1, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ModifyAllCopies(Lu.DATA.ID)
        .AddCallback(c ->
        {
            GameUtilities.IncreaseDamage(c, magicNumber, false);
            c.flash();
        });

        if (CheckAffinity(AffinityType.Blue, secondaryValue))
        {
            GameActions.Bottom.StackPower(new LockOnPower(m, 2));
        }

        if (CheckAffinity(AffinityType.Green, secondaryValue))
        {
            GameActions.Bottom.GainBlur(1);
        }
    }
}