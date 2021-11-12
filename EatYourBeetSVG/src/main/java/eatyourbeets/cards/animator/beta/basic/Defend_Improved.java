package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.utilities.GameActions;

public class Defend_Improved extends Defend {
    public static final String ID = Register(Defend_Improved.class).ID;

    public Defend_Improved() {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 6, 0);
        SetUpgrade(0, 3, 0);

        SetShapeshifter();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
    }
}