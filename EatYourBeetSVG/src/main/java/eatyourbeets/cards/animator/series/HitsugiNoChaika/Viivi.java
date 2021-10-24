package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Viivi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Viivi.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public Viivi()
    {
        super(DATA);

        Initialize(4, 0, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Air();
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.VFX(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0f);
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.NONE);
        }

        GameActions.Bottom.CreateThrowingKnives(1);
        GameActions.Bottom.GainSupportDamage(secondaryValue);
    }
}