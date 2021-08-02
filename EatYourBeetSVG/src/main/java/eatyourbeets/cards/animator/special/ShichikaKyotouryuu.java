package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import eatyourbeets.actions.basic.RemoveBlock;
import eatyourbeets.cards.animator.series.Katanagatari.Shichika;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class ShichikaKyotouryuu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShichikaKyotouryuu.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(Shichika.DATA.Series);

    public ShichikaKyotouryuu()
    {
        super(DATA);

        Initialize(1, 0, 4);
        SetUpgrade(1, 0, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(2, 0, 1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Add(new RemoveBlock(m, p)).SetVFX(true, true);

        GameActions.Bottom.VFX(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.SCARLET.cpy()));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);

        GameActions.Last.Add(new RemoveBlock(m, p)).SetVFX(true, false);
    }
}