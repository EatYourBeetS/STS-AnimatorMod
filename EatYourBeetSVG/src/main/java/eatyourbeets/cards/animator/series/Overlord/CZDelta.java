package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class CZDelta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CZDelta.class)
            .SetAttack(0, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    private static final Color VFX_COLOR = new Color(0.6f, 1f, 0.6f, 1f);

    public CZDelta()
    {
        super(DATA);

        Initialize(9, 0, 3);
        SetUpgrade(0, 0, -1);

        SetAffinity_Green(2);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAttackTarget(EYBCardTarget.Normal);
        this.upgradedDamage = true;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(player.currentBlock < magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ModifyAllCopies(cardID)
            .AddCallback(c -> ((CZDelta) c).SetHaste(true));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.LoseBlock(magicNumber);

        if (upgraded)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT)
            .SetDamageEffect(c ->
            {
                SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f, 0.95f);
                return GameEffects.List.Add(VFX.SmallLaser(player.hb, c.hb, VFX_COLOR, 0.1f)).duration * 0.7f;
            })
            .SetSoundPitch(1.5f, 1.55f)
            .SetVFXColor(VFX_COLOR);
        }
        else
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.GUNSHOT)
            .SetDamageEffect(c ->
            {
                SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f, 0.95f);
                return GameEffects.List.Add(VFX.SmallLaser(player.hb, c.hb, VFX_COLOR, 0.1f)).duration * 0.7f;
            })
            .SetSoundPitch(1.5f, 1.55f)
            .SetVFXColor(VFX_COLOR);
        }
    }
}