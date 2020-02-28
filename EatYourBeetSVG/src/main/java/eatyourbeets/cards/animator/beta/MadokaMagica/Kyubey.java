package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

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

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.MakeCardInDiscardPile(GameUtilities.GetRandomCurse());

        return true;
    }
}