package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kazuma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kazuma.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Kazuma()
    {
        super(DATA);

        Initialize(0, 6, 2);
        SetUpgrade(0, 3, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Draw(1)
        .AddCallback(cards ->
        {
           for (AbstractCard c : cards)
           {
               if (GameUtilities.IsSealed(c))
               {
                   GameActions.Bottom.GainTemporaryHP(magicNumber);
                   GameActions.Bottom.GainBlessing(1);
                   return;
               }
           }
        });
    }
}