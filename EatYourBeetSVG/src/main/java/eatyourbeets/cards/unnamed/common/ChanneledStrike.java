package eatyourbeets.cards.unnamed.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.unnamed.MarkedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class ChanneledStrike extends UnnamedCard
{
    public static final EYBCardData DATA = Register(ChanneledStrike.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged);

    public ChanneledStrike()
    {
        super(DATA);

        Initialize(3, 0, 9);
        SetUpgrade(0, 0, 4);

        SetEthereal(true);
        SetRecast(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (!hasTag(RECAST))
        {
            amount += magicNumber;
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        if (hasTag(RECAST))
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowDagger(c.hb, 0.15f).PlaySFX(true).SetColor(Color.SKY)).duration * 0.5f);
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR);
        }

        GameActions.Bottom.ApplyPower(new MarkedPower(m)).IgnoreArtifact(true);
    }
}