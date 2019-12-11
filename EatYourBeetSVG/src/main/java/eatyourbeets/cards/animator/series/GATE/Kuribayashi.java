package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Kuribayashi extends AnimatorCard
{
    public static final String ID = Register(Kuribayashi.class.getSimpleName(), EYBCardBadge.Synergy);

    private static final int STRENGTH_DOWN = 4;

    public Kuribayashi()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(8,0,1, 2);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        GameActions.Bottom.ApplyPower(p, m, new ChokePower(m, secondaryValue), secondaryValue);

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ReduceStrength(m, STRENGTH_DOWN, true);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeSecondaryValue(2);
        }
    }
}