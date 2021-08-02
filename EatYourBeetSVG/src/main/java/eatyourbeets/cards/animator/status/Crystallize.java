package eatyourbeets.cards.animator.status;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
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

        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.SFX(SFX.ORB_FROST_EVOKE, 1f, 1.2f);
            GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.SLASH_VERTICAL);
            GameActions.Bottom.GainMetallicize(magicNumber);
            GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.SLASH_VERTICAL);
            GameActions.Bottom.GainCorruption(1, false);
        }
    }
}