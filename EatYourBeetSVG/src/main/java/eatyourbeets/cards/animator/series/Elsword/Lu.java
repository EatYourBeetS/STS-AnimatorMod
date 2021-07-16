package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Lu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lu.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Lu()
    {
        super(DATA);

        Initialize(2, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Blue(2, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAttackTarget(EYBCardTarget.ALL);
        SetMultiDamage(true);
        upgradedDamage = true;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID)
        .RequireNeutralStance(true)
        .AddCallback(changed ->
        {
            if (changed)
            {
                GameActions.Bottom.Flash(this);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (isMultiDamage)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect((enemy, __) -> GameEffects.List.Add(new ClawEffect(enemy.hb.cX, enemy.hb.cY, Color.VIOLET, Color.WHITE)));
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(enemy -> GameEffects.List.Add(new ClawEffect(enemy.hb.cX, enemy.hb.cY, Color.VIOLET, Color.WHITE)).duration);
        }

        if (damage > 20)
        {
            GameActions.Bottom.Add(new ShakeScreenAction(0.8f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }

        GameActions.Bottom.ChannelOrb(new Frost());
        GameActions.Bottom.ChannelOrb(new Dark());
    }
}