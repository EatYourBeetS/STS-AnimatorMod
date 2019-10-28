package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.StephanieAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class DolaStephanie extends AnimatorCard
{
    public static final String ID = Register(DolaStephanie.class.getSimpleName());

    public DolaStephanie()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0);

        this.exhaust = true;

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToTop(new StephanieAction(p, 1));
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