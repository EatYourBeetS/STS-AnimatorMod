package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;

public class Crystallize extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Crystallize.class)
            .SetStatus(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Crystallize()
    {
        super(DATA, false);

        Initialize(0, 0, 4, 3);

        SetAffinity_Silver(1);
        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.SFX(SFX.ORB_FROST_EVOKE, 1f, 1.2f);
            GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.SLASH_VERTICAL);
            GameActions.Bottom.GainMetallicize(magicNumber);
            GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.SLASH_VERTICAL);
            GameActions.Bottom.GainTechnic(1, false);
        }
    }
}