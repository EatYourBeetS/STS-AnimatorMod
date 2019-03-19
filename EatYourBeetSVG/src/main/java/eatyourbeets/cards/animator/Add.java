package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.ExhaustFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Add extends AnimatorCard
{
    public static final String ID = CreateFullID(Add.class.getSimpleName());

    public Add()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        this.isEthereal = true;

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new ExhaustFromPileAction(1, false, p.drawPile));

        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeMagicNumber(1);
        }
    }
}