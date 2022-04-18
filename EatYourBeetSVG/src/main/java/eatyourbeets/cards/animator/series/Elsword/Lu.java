package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.effects.VFX;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Lu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lu.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .ModifyRewards((data, rewards) ->
            {
                if (data.GetTotalCopies(player.masterDeck) < data.MaxCopies && Ciel.DATA.GetTotalCopies(player.masterDeck) > 0)
                {
                    GR.Common.Dungeon.TryReplaceFirstCardReward(rewards, 0.075f, true, data);
                }
            });

    public Lu()
    {
        super(DATA);

        Initialize(2, 0, 3);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Dark(2);
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
                GameActions.Bottom.TakeDamageAtEndOfTurn(magicNumber, AttackEffects.CLAW);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (isMultiDamage)
        {
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE)
            .SetDamageEffect((enemy, __) -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.VIOLET, Color.WHITE)));
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.VIOLET, Color.WHITE)).duration);
        }

        if (damage >= 20)
        {
            GameActions.Bottom.Add(new ShakeScreenAction(0.8f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }

        GameActions.Bottom.ChannelOrb(new Frost());
        GameActions.Bottom.ChannelOrb(new Dark());
    }
}