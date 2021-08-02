package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IchigoBankai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IchigoBankai.class)
            .SetAttack(-1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Bleach);

    public IchigoBankai()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(3, 0);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Red(2, 0, 1);

        SetExhaust(true);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameUtilities.UseXCostEnergy(this);

        if (damage > 0)
        {
            GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.LIGHT_GRAY));
            GameActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.LIGHT_GRAY, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75f);
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY);
        }
    }
}