package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.FeridBathoryPower;
import eatyourbeets.utilities.GameActions;

public class FeridBathory extends AnimatorCard
{
    public static final String ID = Register(FeridBathory.class, EYBCardBadge.Special);

    public FeridBathory()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 2, FeridBathoryPower.FORCE_AMOUNT);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.StackPower(new FeridBathoryPower(p, magicNumber));
    }
}