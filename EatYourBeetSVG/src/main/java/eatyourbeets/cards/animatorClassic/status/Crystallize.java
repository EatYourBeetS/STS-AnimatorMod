package eatyourbeets.cards.animatorClassic.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Crystallize extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Crystallize.class).SetStatus(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Crystallize()
    {
        super(DATA);

        Initialize(0, 0, 4, 3);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.SFX("ORB_FROST_Evoke", 0.8f, 1f);
            GameActions.Bottom.LoseHP(secondaryValue, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            GameActions.Bottom.GainMetallicize(magicNumber);
            GameActions.Bottom.LoseHP(secondaryValue, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }
}