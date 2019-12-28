package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class GoblinKing extends AnimatorCard_Status
{
    public static final String ID = Register(GoblinKing.class);

    public GoblinKing()
    {
        super(ID, 1, CardRarity.RARE, CardTarget.NONE);

        Initialize(0, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Add(new CreateRandomGoblins(3));
        }
    }
}