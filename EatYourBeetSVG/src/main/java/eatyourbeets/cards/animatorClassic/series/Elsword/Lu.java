package eatyourbeets.cards.animatorClassic.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Lu extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Lu.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal).SetMaxCopies(2);

    public Lu()
    {
        super(DATA);

        Initialize(2, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);
        SetScaling(2, 0, 2);


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
        .AddCallback(stance ->
        {
            if (stance != null)
            {
                GameActions.Bottom.Flash(this);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (isMultiDamage)
        {
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE)
            .SetDamageEffect((enemy, __) -> GameEffects.List.Add(new ClawEffect(enemy.hb.cX, enemy.hb.cY, Color.VIOLET, Color.WHITE)));
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
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