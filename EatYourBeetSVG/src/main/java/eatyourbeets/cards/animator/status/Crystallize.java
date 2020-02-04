package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_Status;

public class Crystallize extends AnimatorCard_Status
{
    public static final String ID = Register_Old(Crystallize.class);

    public Crystallize()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0, 0, 4, 3);

        SetExhaust(true);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        // Do not autoplay
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.SFX("ORB_FROST_Evoke", 0.2F);
            GameActions.Bottom.LoseHP(secondaryValue, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            GameActions.Bottom.GainMetallicize(this.magicNumber);
            GameActions.Bottom.LoseHP(secondaryValue, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }
}