package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class OrigamiTobiichi extends AnimatorCard {
    public static final EYBCardData DATA = Register(OrigamiTobiichi.class).SetPower(1, CardRarity.COMMON);

    public OrigamiTobiichi() {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetSynergy(Synergies.eatyourbeets.cards.animator.beta.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.StackPower(new OrigamiTobiichiPower(p, this.magicNumber));
    }

    public static class OrigamiTobiichiPower extends AnimatorPower {
        public OrigamiTobiichiPower(AbstractPlayer owner, int amount) {
            super(owner, OrigamiTobiichi.DATA);

            this.amount = amount;

            updateDescription();
        }
    }
}