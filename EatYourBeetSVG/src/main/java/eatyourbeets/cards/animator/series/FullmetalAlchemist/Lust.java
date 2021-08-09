package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Lust extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lust.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal)
            .SetSeriesFromClassPackage();

    protected static boolean flipVfx;
    protected boolean gainTempHP = false;

    public Lust()
    {
        super(DATA);

        Initialize(3, 0, 3, 1);

        SetAffinity_Star(1, 1, 0);
        SetAffinity_Green(0, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetRetainOnce(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(secondaryValue);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return gainTempHP ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.gainTempHP = (enemy != null && enemy.hasPower(VulnerablePower.POWER_ID));
        this.secondaryValue = 1 + JUtils.Count(player.hand.group, GameUtilities::IsCurseOrStatus);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetVFX(true, false)
            .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.WHITE, Color.VIOLET)
                    .FlipX(flipVfx ^= true).SetScale(0.7f)).duration);
        }

        if (gainTempHP)
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }
    }
}