package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.HomuraAkemi;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

import java.util.HashSet;

public class HomuraAkemi_Homulilly extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HomuraAkemi_Homulilly.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(HomuraAkemi.DATA.Series);

    public HomuraAkemi_Homulilly()
    {
        super(DATA);

        Initialize(0, 4, 3, 2);
        SetUpgrade(0, 2, 1);

        SetAffinity_Dark(2, 0, 3);
        SetAffinity_Red(1);

        SetExhaust(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainCorruption(secondaryValue);
        GameActions.Bottom.Cycle(name, secondaryValue);
        GameActions.Bottom.Callback(() ->
        {
            final HashSet<String> cards = new HashSet<>();
            for (AbstractCard c : player.exhaustPile.group)
            {
                if (c.type == CardType.CURSE && !cards.contains(c.cardID))
                {
                    GameActions.Top.MakeCardInHand(c.makeStatEquivalentCopy());
                    cards.add(c.cardID);
                }
            }

            GameActions.Last.Callback(() ->
            {
                int tempHP = 0;
                for (AbstractCard c : player.hand.group)
                {
                    if (c.type == CardType.CURSE)
                    {
                        tempHP += magicNumber;
                    }
                }
                GameActions.Bottom.GainTemporaryHP(tempHP);
            });
        });
    }
}