package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.ShuffleRandomGoblinAction;
import eatyourbeets.cards.AnimatorCard_Status;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;

public class GoblinChampion extends AnimatorCard_Status
{
    public static final String ID = Register(GoblinChampion.class.getSimpleName());

    public GoblinChampion()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0,0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Add(new ShuffleRandomGoblinAction(1));
        }
    }
}