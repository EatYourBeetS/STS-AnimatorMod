package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class Viivi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Viivi.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();
    static
    {
        for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
        {
            DATA.AddPreview(knife, false);
        }
    }

    public Viivi()
    {
        super(DATA);

        Initialize(3, 0, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1, 1, 1);
        SetAffinity_Light(2);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.CreateThrowingKnives(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.VFX(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0f);
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.NONE);
        }

        if (IsStarter())
        {
            GameActions.Bottom.GainAgility(1);
            GameActions.Bottom.Draw(1);
        }
    }
}