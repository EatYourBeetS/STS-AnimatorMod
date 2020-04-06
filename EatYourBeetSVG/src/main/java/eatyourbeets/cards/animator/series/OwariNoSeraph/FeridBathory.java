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
    public static final EYBCardData DATA = Register(FeridBathory.class).SetPower(2, CardRarity.RARE).SetMaxCopies(2);

    public FeridBathory()
    {
        super(DATA);

        Initialize(0,0, 2, FeridBathoryPower.FORCE_AMOUNT);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new FeridBathoryPower(p, magicNumber));
    }
}