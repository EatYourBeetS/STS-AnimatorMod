package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.ChaaPower;
import eatyourbeets.utilities.GameActions;

public class Chaa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chaa.class).SetPower(2, CardRarity.UNCOMMON);
    private boolean fromSynergy = false;

    public Chaa()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);
        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new ChaaPower(p, magicNumber));
    }

    @Override
    public void Refresh(AbstractMonster enemy) {
        super.Refresh(enemy);
        if (HasSynergy()) {
            setCostForTurn(0);
            fromSynergy = true;
        } else {
            if (fromSynergy) {
                setCostForTurn(this.cost);
                this.isCostModifiedForTurn = false;
                fromSynergy = false;
            }
        }
    }
}