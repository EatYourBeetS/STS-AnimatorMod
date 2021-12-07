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

    public Eve_Drone() {
        this(0);
    }

    public Eve_Drone(int form)
    {
        super(DATA);

        Initialize(3, 0, 1, 0);
        SetAffinity_Silver(2, 0, 0);
        SetRetain(true);
        SetCooldown(4, 2, this::OnCooldownCompleted);
        SetForm(form, timesUpgraded);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            LoadImage("1");
            Initialize(0, 2, 1, 0);
            SetUpgrade(1,1);
        }
        else {
            LoadImage(null);
            Initialize(3, 0, 1, 0);
            SetUpgrade(1,1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (damage > 0) {
            GameActions.Bottom.DealCardDamage(this, m, AttackEffects.PSYCHOKINESIS).forEach(d -> d.SetVFXColor(Color.CYAN));
        }
        if (block > 0) {
            GameActions.Bottom.GainBlock(block);
        }
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