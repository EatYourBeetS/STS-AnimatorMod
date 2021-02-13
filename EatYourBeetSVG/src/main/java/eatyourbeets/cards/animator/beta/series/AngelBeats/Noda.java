package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.utilities.GameActions;

public class Noda extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noda.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public Noda()
    {
        super(DATA);

        Initialize(7, 0, 1, 1);
        SetUpgrade(1, 0, 0, 0);

        SetSynergy(Synergies.AngelBeats);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Exhaust(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        if (HasSynergy())
        {
            GameActions.Bottom.GainForce(secondaryValue, upgraded);
        }
    }
}