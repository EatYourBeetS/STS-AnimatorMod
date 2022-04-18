package eatyourbeets.cards.unnamed.basic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.common.DeEnergizedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class PowerStrike extends UnnamedCard
{
    public static final EYBCardData DATA = Register(PowerStrike.class)
            .SetAttack(2, CardRarity.BASIC, EYBAttackType.Elemental);

    public PowerStrike()
    {
        super(DATA);

        Initialize(28, 0, 2);
        SetUpgrade(8, 0, 0);

        SetDelayed(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DARK)
        .SetSoundPitch(0.6f, 0.65f)
        .SetDamageEffect(enemy ->
        {
            GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.PURPLE));
            return GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.VIOLET)).duration * 0.1f;
        });
        GameActions.Bottom.StackPower(new DeEnergizedPower(p, magicNumber)).ShowEffect(false, false);
    }
}