package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class InverseTohka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(InverseTohka.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeries(CardSeries.DateALive);

    public InverseTohka()
    {
        super(DATA);

        Initialize(10, 0, 10);
        SetAffinity_Red(2, 0, 0);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.SpendEnergy(1, false)
        .AddCallback(() ->
        {
            if (GameUtilities.InStance(ForceStance.STANCE_ID))
            {
                player.stance.onEnterStance();
            }
            else
            {
                GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
            }
        });
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Reload(name, cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.DealDamageToAll(this, AttackEffects.SLASH_DIAGONAL)
                .SetVFX(false, true);
            }
        })
        .SetFilter(c -> c.costForTurn == 1);

        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.HIGH));
    }
}