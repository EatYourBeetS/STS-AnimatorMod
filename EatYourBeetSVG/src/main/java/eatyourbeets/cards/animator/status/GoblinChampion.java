package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class GoblinChampion extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(GoblinChampion.class).SetStatus(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public GoblinChampion()
    {
        super(DATA, true);

        Initialize(0, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Add(new CreateRandomGoblins(1));
        }
    }
}