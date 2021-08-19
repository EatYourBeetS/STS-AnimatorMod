package eatyourbeets.cards.animator.beta.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class AbyssalVoid extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(AbyssalVoid.class).SetStatus(2, CardRarity.SPECIAL, EYBCardTarget.None);

    public AbyssalVoid()
    {
        super(DATA, false);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Dark(2);

        SetAutoplay(true);
        SetPurge(true);
        SetEthereal(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainCorruption(magicNumber, true);
        GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.DARKNESS);
    }
}