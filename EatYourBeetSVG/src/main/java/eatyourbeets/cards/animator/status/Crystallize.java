package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_Status;

public class Crystallize extends AnimatorCard_Status
{
    public static final String ID = Register(Crystallize.class.getSimpleName());

    public Crystallize()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardTarget.NONE);

        this.exhaust = true;

        Initialize(0, 0, 4, 6);
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
            GameActions.Bottom.LoseHP(secondaryValue/2, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            GameActions.Bottom.GainMetallicize(this.magicNumber);
            GameActions.Bottom.LoseHP(secondaryValue/2, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }
}