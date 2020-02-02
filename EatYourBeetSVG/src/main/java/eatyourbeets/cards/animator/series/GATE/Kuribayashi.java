package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Kuribayashi extends AnimatorCard
{
    public static final String ID = Register(Kuribayashi.class);

    private static final int STRENGTH_DOWN = 4;

    public Kuribayashi()
    {
        super(ID, 2, CardRarity.COMMON, EYBAttackType.Ranged);

        Initialize(7, 0, 2, 2);
        SetUpgrade(1, 0, 0, 2);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        GameActions.Bottom.ApplyPower(p, m, new ChokePower(m, secondaryValue), secondaryValue);

        if (HasSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ReduceStrength(m, STRENGTH_DOWN, true);
        }
    }
}