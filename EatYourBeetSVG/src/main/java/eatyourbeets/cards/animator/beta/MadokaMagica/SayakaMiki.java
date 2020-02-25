package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_Greed;
import eatyourbeets.cards.animator.curse.Curse_Nutcracker;
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

        Initialize(0, 5, 5);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        GameActions.Bottom.GainBlock(magicNumber);
        GameActions.Bottom.Heal(magicNumber);

        if (!HasSynergy()) {
            GameActions.Bottom.MakeCardInDiscardPile(GameUtilities.GetRandomCurse());
        }
    }
}
