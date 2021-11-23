package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.stances.DesecrationStance;
import eatyourbeets.stances.WisdomStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Lu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lu.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Ciel(), false));

    public Lu()
    {
        super(DATA);

        Initialize(2, 0, 3);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Blue(1, 0, 2);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (isMultiDamage)
        {
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE).forEach(d -> d
            .SetDamageEffect((enemy, __) -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.VIOLET, Color.WHITE))));
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).forEach(d -> d
            .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.VIOLET, Color.WHITE)).duration));
        }

        if (damage >= 20)
        {
            GameActions.Bottom.Add(new ShakeScreenAction(0.8f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }

        GameActions.Bottom.ChannelOrb(new Frost());
        GameActions.Bottom.ChannelOrb(new Dark());

        if (WisdomStance.IsActive() || DesecrationStance.IsActive()) {
            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
                    .AddCallback(info, (info2, stance) ->
                    {
                        if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID))
                        {
                            GameActions.Bottom.ModifyAllCopies(Ciel.DATA.ID)
                                    .AddCallback(c ->
                                    {
                                        GameUtilities.IncreaseBlock(c, magicNumber, false);
                                        c.flash();
                                    });
                        }
                    });
        }
    }
}