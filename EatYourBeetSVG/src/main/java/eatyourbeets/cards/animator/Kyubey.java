package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Pride;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Kyubey extends AnimatorCard
{
    public static final String ID = CreateFullID(Kyubey.class.getSimpleName());

    public Kyubey()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.GainEnergy(this.magicNumber);
        GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(GetRandomCurse(), 1));
        GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(GetRandomCurse(), 1));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private ArrayList<AbstractCard> curses;
    private AbstractCard GetRandomCurse()
    {
        if (curses == null)
        {
            curses = new ArrayList<>();
            curses.add(new Clumsy());
            curses.add(new Decay());
            curses.add(new Doubt());
            curses.add(new Injury());
            curses.add(new Necronomicurse());
            //curses.add(new Normality());
            //curses.add(new Pain());
            curses.add(new Parasite());
            curses.add(new Pride());
            curses.add(new Regret());
            curses.add(new Shame());
            curses.add(new Writhe());
        }

        return Utilities.GetRandomElement(curses).makeCopy();
    }
}