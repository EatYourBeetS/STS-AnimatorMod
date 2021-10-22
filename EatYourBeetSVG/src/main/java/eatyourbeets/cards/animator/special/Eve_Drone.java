package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Elsword.Eve;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Eve_Drone extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve_Drone.class)
            .SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Ranged)
            .SetSeries(Eve.DATA.Series);

    public Eve_Drone()
    {
        super(DATA);

        Initialize(4, 0, 1, 0);
        SetAffinity_Silver(2, 0, 0);
        SetRetain(true);
        SetCooldown(4, 2, this::OnCooldownCompleted);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.PSYCHOKINESIS).forEach(d -> d.SetVFXColor(Color.CYAN));
        GameActions.Last.MoveCard(this,player.hand).AddCallback(() -> {
            GameUtilities.SetUnplayableThisTurn(this);
            cooldown.ProgressCooldownAndTrigger(null);
        });
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();
        GameActions.Bottom.GainSupportDamage(magicNumber);
        cooldown.ProgressCooldownAndTrigger(null);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Last.Exhaust(this);
    }
}