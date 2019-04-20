package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ChooseFromPileAction;
import eatyourbeets.actions.ExhaustFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.OrbCore_AbstractPower;

import java.util.ArrayList;

public class Add extends AnimatorCard
{
    public static final String ID = CreateFullID(Add.class.getSimpleName());

    public Add()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0);

        this.exhaust = true;
        this.isEthereal = true;

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (p.drawPile.size() > 0)
        {
            GameActionsHelper.AddToBottom(new ExhaustFromPileAction(1, false, p.drawPile));

            CardGroup cores = OrbCore_AbstractPower.CreateCoresGroup(3);
            GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false, cores, this::OrbChosen, this, ""));
        }
    }

    private void OrbChosen(Object state, ArrayList<AbstractCard> chosen)
    {
        if (state == this && chosen != null && chosen.size() == 1)
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(chosen.get(0), 1, true, true));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            this.exhaust = false;
        }
    }
}