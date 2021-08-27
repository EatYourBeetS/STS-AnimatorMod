package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.cards.animator.series.Fate.Saber;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Saber_Excalibur extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Saber_Excalibur.class)
            .SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeries(Saber.DATA.Series);

    public Saber_Excalibur()
    {
        super(DATA);

        Initialize(80, 0);
        SetUpgrade(19, 0);

        SetAffinity_Red(2);
        SetAffinity_Light(2, 0, 4);

        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, 1));
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));
        GameActions.Bottom.VFX(VFX.ShockWave(p.hb, Color.GOLD), 0.4f).SetRealtime(true);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE)
        .SetDamageEffect((c, __) -> GameEffects.List.Add(VFX.VerticalImpact(c.hb).SetColor(Color.GOLD)));
    }
}