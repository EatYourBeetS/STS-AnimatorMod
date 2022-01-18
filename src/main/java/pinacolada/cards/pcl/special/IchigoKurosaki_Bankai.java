package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.Bleach.IchigoKurosaki;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class IchigoKurosaki_Bankai extends PCLCard
{
    public static final PCLCardData DATA = Register(IchigoKurosaki_Bankai.class)
            .SetAttack(-1, CardRarity.SPECIAL, PCLAttackType.Ranged, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeries(IchigoKurosaki.DATA.Series);

    public IchigoKurosaki_Bankai()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(3, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1, 0, 1);

        SetExhaust(true);
        SetMultiDamage(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse > 0)
        {
            effect = energyOnUse;
        }

        if (player.hasRelic(ChemicalX.ID))
        {
            effect += ChemicalX.BOOST;
        }

        return effect * amount;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLGameUtilities.UseXCostEnergy(this);

        if (damage > 0)
        {
            PCLActions.Bottom.VFX(new BorderLongFlashEffect(Color.LIGHT_GRAY));
            PCLActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.LIGHT_GRAY, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75f);
            PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);
        }
    }
}