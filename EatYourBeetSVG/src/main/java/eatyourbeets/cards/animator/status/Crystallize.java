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
        super(DATA, false);

        Initialize(0, 0, 4, 3);

        SetExhaust(true);
        SetAlignment(0, 0, 0, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.SFX("ORB_FROST_Evoke", 0.2f);
            GameActions.Bottom.LoseHP(secondaryValue, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            GameActions.Bottom.GainMetallicize(magicNumber);
            GameActions.Bottom.LoseHP(secondaryValue, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }
}