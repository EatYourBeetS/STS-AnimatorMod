package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Ciel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ciel.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Lu(), false));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Ciel()
    {
        super(DATA);

        Initialize(0, 12, 1, 8);
        SetUpgrade(0, 0, 2, 7);

        SetAffinity_Dark(2);
        SetAffinity_Water();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        boolean luExists = false;

        for (AbstractCard card : player.masterDeck.group)
        {
            if (card.cardID.equals(Lu.DATA.ID))
            {
                luExists = true;
                break;
            }
        }

        if (luExists)
        {
            GameActions.Bottom.ApplyLockOn(p,m,secondaryValue);
        }
        else
        {
            GameActions.Bottom.ApplyLockOn(p,m,magicNumber);
        }
    }
}