package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class DolaSchwi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaSchwi.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();
    public static final int COOLDOWN_DAMAGE_BONUS = 14;

    private static final Color VFX_COLOR = new Color(0.8f, 0.4f, 0.8f, 1f);

    public DolaSchwi()
    {
        super(DATA);

        Initialize(2, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);

        SetCooldown(2, 0, null);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return new ColoredString(COOLDOWN_DAMAGE_BONUS, Colors.Cream(1f));
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (cooldown.IsReady() ? COOLDOWN_DAMAGE_BONUS : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (cooldown.ProgressCooldown(true))
        {
            GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.5f, 0.6f);
            GameActions.Bottom.BorderFlash(VFX_COLOR);
            GameActions.Bottom.SFX(SFX.ATTACK_HEAVY);
            GameActions.Bottom.VFX(VFX.Mindblast(player.dialogX, player.dialogY).SetColor(VFX_COLOR), 0.1f);
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetDamageEffect(c ->
            {
                SFX.Play(SFX.ATTACK_FIRE, 1.25f, 1.3f);
                return GameEffects.List.Add(VFX.SmallLaser(player.hb, c.hb, VFX_COLOR, 0.1f)).duration * 0.5f;
            })
            .SetSoundPitch(1.7f, 1.75f)
            .SetVFXColor(VFX_COLOR);
        }

        GameActions.Bottom.StackPower(p, new LockOnPower(m, magicNumber));
        GameActions.Bottom.ChannelOrb(new Lightning());
    }
}