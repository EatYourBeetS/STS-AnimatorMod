package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_Greed;
import eatyourbeets.cards.animator.curse.Curse_Nutcracker;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class Kyubey extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(Kyubey.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None);

    public Kyubey()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);

        SetExhaust(true);
        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(this.magicNumber);
        GameActions.Bottom.GainEnergy(this.magicNumber);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.MakeCardInDiscardPile(GameUtilities.GetRandomCurse());

        return true;
    }
}