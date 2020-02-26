package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_Greed;
import eatyourbeets.cards.animator.curse.Curse_Nutcracker;
import eatyourbeets.cards.animator.special.Asuramaru;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class SayakaMiki extends AnimatorCard {
    public static final EYBCardData DATA = Register(SayakaMiki.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public SayakaMiki()
    {
        super(DATA);

        Initialize(0, 6, 0);
        SetUpgrade(0, 1, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        GameActions.Bottom.GainBlock(block);

        if (!HasSynergy()) {
            GameActions.Bottom.MakeCardInDiscardPile(GameUtilities.GetRandomCurse());
        }

        if (GameUtilities.GetHealthPercentage(player) < 0.5f)
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Oktavia()).SetOptions(upgraded, false)
            .AddCallback(__ ->
             {
                  GameActions.Bottom.Exhaust(this);
             });
        }
    }
}
