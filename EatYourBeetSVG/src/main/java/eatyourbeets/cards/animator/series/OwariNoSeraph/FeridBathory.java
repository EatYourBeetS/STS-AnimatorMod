package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.FeridBathoryPower;
import eatyourbeets.utilities.GameActions;

public class FeridBathory extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FeridBathory.class).SetPower(2, CardRarity.RARE);

    public FeridBathory()
    {
        super(DATA);

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