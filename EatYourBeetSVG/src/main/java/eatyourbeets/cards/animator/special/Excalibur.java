package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.animator.series.Fate.Saber;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Excalibur extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Excalibur.class)
            .SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeries(Saber.DATA.Series);

    public Excalibur()
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

        for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(new VerticalImpactEffect(m1.hb_x, m1.hb_y));
        }

        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY);
    }
}