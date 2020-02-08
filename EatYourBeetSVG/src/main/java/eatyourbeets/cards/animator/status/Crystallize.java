package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Crystallize extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Crystallize.class).SetStatus(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Crystallize()
    {
        super(DATA);

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