package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.*;

public class Lust extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lust.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    protected static boolean flipVfx;
    protected boolean gainTempHP = false;

    public Lust()
    {
        super(DATA);

        Initialize(5, 0, 1, 3);
        SetUpgrade(1, 0, 0);

        SetAffinity_Dark(1, 1, 1);
        SetAffinity_Orange(1, 1, 0);
        SetAffinity_Green(0, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return magicNumber > 1 ? super.GetDamageInfo().AddMultiplier(magicNumber) : super.GetDamageInfo();
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return gainTempHP ? TempHPAttribute.Instance.SetCard(this, false).SetText(secondaryValue, Colors.Cream(1f)) : null;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.gainTempHP = (enemy != null && enemy.hasPower(VulnerablePower.POWER_ID));
        this.magicNumber = (JUtils.Any(player.hand.group, GameUtilities::IsHindrance) ? 2 : 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetVFX(true, false)
            .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.WHITE, Color.VIOLET)
                                      .FlipX(flipVfx ^= true).SetScale(0.7f)).duration);
        }

        if (gainTempHP)
        {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }
}