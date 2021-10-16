package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Verdia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Verdia.class)
            .SetSkill(3, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Verdia()
    {
        super(DATA);

        Initialize(0, 8, 2);
        SetUpgrade(0, 13, 1);

        SetAffinity_Earth(2);
        SetAffinity_Dark(2);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        for (AbstractCard c : player.discardPile.group)
        {
            if (GameUtilities.IsLowCost(c))
            {
                amount += magicNumber;
            }
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }
}