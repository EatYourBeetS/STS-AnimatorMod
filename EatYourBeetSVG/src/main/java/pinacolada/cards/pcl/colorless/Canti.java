package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Canti extends PCLCard
{
    public static final PCLCardData DATA = Register(Canti.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.FLCL);

    public Canti()
    {
        super(DATA);

        Initialize(2, 3, 2);

        SetAffinity_Orange(1);
        SetAffinity_Light(1);
        SetAffinity_Silver(1);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetHaste(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            amount += PCLGameUtilities.GetPCLIntent(enemy).GetDamage(false);
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        if (m != null && !PCLGameUtilities.IsAttacking(m.intent) && info.TryActivateSemiLimited()) {
            PCLActions.Delayed.GainTechnic(magicNumber, true);
        }

        if (damage >= 20)
        {
            //GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY));
            //GameActions.Bottom.Wait(0.8f);
            PCLActions.Bottom.VFX(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.SKY));
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE);
        }
        else
        {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        }
    }
}