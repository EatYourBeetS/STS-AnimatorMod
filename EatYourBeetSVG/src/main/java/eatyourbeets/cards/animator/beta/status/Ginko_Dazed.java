package eatyourbeets.cards.animator.beta.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Ginko_Dazed extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Ginko_Dazed.class).SetStatus(-2, CardRarity.COMMON, EYBCardTarget.ALL);

    public Ginko_Dazed()
    {
        super(DATA, false);

        Initialize(0, 0, 3);
        SetEthereal(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard)
        {
            this.performAction();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        this.performAction();
    }

    private void performAction() {
        GameActions.Bottom.GainBlock(magicNumber);
    }
}