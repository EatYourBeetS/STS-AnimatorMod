package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import pinacolada.actions.basic.RemoveBlock;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.pcl.series.Katanagatari.Shichika;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Shichika_Kyotouryuu extends PCLCard
{
    public static final PCLCardData DATA = Register(Shichika_Kyotouryuu.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(Shichika.DATA.Series);

    public Shichika_Kyotouryuu()
    {
        super(DATA);

        Initialize(1, 0, 4);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 1);

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
        PCLActions.Bottom.Add(new RemoveBlock(m, p)).SetVFX(true, true);

        PCLActions.Bottom.VFX(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.SCARLET.cpy()));
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        PCLActions.Last.Add(new RemoveBlock(m, p)).SetVFX(true, false);
    }
}