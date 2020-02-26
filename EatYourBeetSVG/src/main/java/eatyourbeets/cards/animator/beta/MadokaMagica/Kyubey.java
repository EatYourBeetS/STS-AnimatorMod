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
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class Kyubey extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(Kyubey.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None);

    private static ArrayList<AbstractCard> curses;

    public Kyubey()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.GainEnergy(2);
    }

    private AbstractCard GetRandomCurse()
    {
        if (curses == null)
        {
            curses = new ArrayList<>();
            curses.add(new Clumsy());
            curses.add(new Decay());
            curses.add(new Doubt());
            curses.add(new Injury());
            curses.add(new Normality());
            curses.add(new Pain());
            curses.add(new Parasite());
            curses.add(new Regret());
            curses.add(new Shame());
            curses.add(new Writhe());
            curses.add(new Curse_Greed());
            curses.add(new Curse_Nutcracker());
            //curses.add(new Pride());
            //curses.add(new Necronomicurse());
        }

        return JavaUtilities.GetRandomElement(curses).makeCopy();
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.MakeCardInDiscardPile(GetRandomCurse());

        return true;
    }
}