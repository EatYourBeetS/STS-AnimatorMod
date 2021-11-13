package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Xiao extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Xiao.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Piercing, EYBCardTarget.ALL).SetSeriesFromClassPackage(true);

    public Xiao()
    {
        super(DATA);

        Initialize(6, 2, 2, 2);
        SetUpgrade(1, 0, 1, 0);
        SetAffinity_Green(2, 0, 2);
        SetAffinity_Dark(2, 0, 1);

        SetHitCount(3);
        SetEthereal(true);
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + magicNumber * GameUtilities.GetOrbCount(Air.ORB_ID);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        SFX.Play(SFX.ATTACK_REAPER);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE).forEach(d -> d
                .SetDamageEffect((enemy, __) ->
                {
                    float wait = GameEffects.List.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 30f * Settings.scale,
                            500f, 200f, 300f, 3f, Color.FOREST.cpy(), Color.GREEN.cpy())).duration;
                    wait += GameEffects.Queue.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 60f * Settings.scale,
                            500f, 200f, 300f, 5f, Color.FOREST.cpy(), Color.GREEN.cpy())).duration;
                }));
        GameActions.Bottom.GainBlock(block);

        if (AgilityStance.IsActive()) {
            GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), secondaryValue * player.hand.size());
            GameActions.Last.Exhaust(this);
        }
        else {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        }
    }
}

