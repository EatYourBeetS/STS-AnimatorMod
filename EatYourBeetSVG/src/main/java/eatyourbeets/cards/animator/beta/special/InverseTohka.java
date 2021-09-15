package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class InverseTohka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(InverseTohka.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeries(CardSeries.DateALive);

    public InverseTohka()
    {
        super(DATA);

        Initialize(8, 0, 2, 1);
        SetUpgrade(3, 0);
        SetAffinity_Red(2, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetAutoplay(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + CombatStats.SynergiesThisCombat().size() * magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainForce(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.HIGH));
    }
}