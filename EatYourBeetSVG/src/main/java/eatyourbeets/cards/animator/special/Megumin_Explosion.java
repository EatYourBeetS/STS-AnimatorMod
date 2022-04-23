package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Konosuba.Megumin;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Megumin_Explosion extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Megumin_Explosion.class)
            .SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeries(Megumin.DATA.Series);

    public Megumin_Explosion()
    {
        super(DATA);

        Initialize(11, 0);
        SetUpgrade(5, 0);

        SetAffinity_Blue(2);
        SetAffinity_Red(1);
        SetAffinity_Light(2);
        SetAffinity_Star(0, 0, 3);

        cropPortrait = false;
        isMultiUpgrade = true;
        SetExhaust(true);
        SetRetain(true);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        int bonus = 0;
        if (timesUpgraded > 2)
        {
            bonus += (timesUpgraded * (timesUpgraded - 2));

            if (timesUpgraded > 3)
            {
                LoadImage("_Alt");
            }
        }

        upgradeDamage(bonus);
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() * (GameUtilities.GetEnemies(true).size() != 1 ? 1 : 1.5f);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.7f, 0.9f, 1.2f);
        GameActions.Bottom.WaitRealtime(0.5f);
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 1.1f, 1.3f, 1.2f);
        GameActions.Bottom.BorderFlash(Color.ORANGE);
        GameActions.Bottom.WaitRealtime(0.4f);
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.7f, 0.9f, 1.2f);
        GameActions.Bottom.WaitRealtime(0.4f);
        GameActions.Bottom.BorderFlash(Color.RED);
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 1.1f, 1.3f, 1.2f);
        GameActions.Bottom.Callback(() ->
        {
            for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
            {
                GameEffects.List.Attack(player, m1, AttackEffects.LIGHTNING, 0.7f, 0.8f, Color.RED);
                GameEffects.List.Add(VFX.FlameBarrier(m1.hb));
                for (int i = 0; i < 12; i++)
                {
                    GameEffects.List.Add(VFX.SmallExplosion(m1.hb, 0.4f).PlaySFX(i == 0));
                }
            }
        });
        GameActions.Bottom.WaitRealtime(0.35f);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE).SetVFX(true, true);
    }
}