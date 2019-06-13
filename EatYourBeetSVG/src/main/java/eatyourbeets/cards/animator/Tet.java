package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.DiscardFromPileAction;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class Tet extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(Tet.class.getSimpleName());

    public Tet()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0,2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new DiscardFromPileAction(p.drawPile, this.magicNumber, true));
        GameActionsHelper.DrawCard(p, 2);

        if (ProgressBoost())
        {
            GameActionsHelper.GainEnergy(1);
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

    @Override
    protected int GetBaseBoost()
    {
        return 1;
    }
}