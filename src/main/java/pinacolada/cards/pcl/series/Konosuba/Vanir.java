package pinacolada.cards.pcl.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.events.shrines.Transmogrifier;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Vanir extends PCLCard
{
    public static final PCLCardData DATA = Register(Vanir.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Dark)
            .SetSeriesFromClassPackage();

    public Vanir()
    {
        super(DATA);

        Initialize(12, 0, 3);
        SetUpgrade(1, 0, -1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.SelectFromPile(name, 1, player.drawPile)
        .SetOptions(false, true)
        .SetMessage(Transmogrifier.OPTIONS[2])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Top.ReplaceCard(cards.get(0).uuid, makeCopy()).SetUpgrade(upgraded);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (damage > 8)
        {
            PCLActions.Bottom.SFX(SFX.ATTACK_DEFECT_BEAM);
            PCLActions.Bottom.VFX(VFX.SmallLaser(p.hb, m.hb, Color.RED));
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE).forEach(d -> d
            .SetDamageEffect(c -> PCLGameEffects.List.Add(VFX.SmallExplosion(c.hb)).duration * 0.1f));
        }
        else
        {
            PCLActions.Bottom.Wait(0.25f);
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SMASH);
        }
        PCLActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
    }
}