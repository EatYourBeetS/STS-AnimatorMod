package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import eatyourbeets.actions.basic.RemoveBlock;
import eatyourbeets.cards.animator.series.Katanagatari.Shichika;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Shichika_Kyotouryuu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shichika_Kyotouryuu.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(Shichika.DATA.Series);

    public Shichika_Kyotouryuu()
    {
        super(DATA);

        Initialize(1, 0, 4);

        SetAffinity_Red(2);
        SetAffinity_Green(2, 0, 1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(new RemoveBlock(m, p)).SetVFX(true, true);

        GameActions.Bottom.VFX(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.SCARLET.cpy()));
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        GameActions.Last.Add(new RemoveBlock(m, p)).SetVFX(true, false);
    }
}