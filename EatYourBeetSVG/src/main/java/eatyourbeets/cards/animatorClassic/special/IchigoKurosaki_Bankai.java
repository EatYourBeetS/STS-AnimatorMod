package eatyourbeets.cards.animatorClassic.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IchigoKurosaki_Bankai extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(IchigoKurosaki_Bankai.class).SetAttack(-1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);

    public IchigoKurosaki_Bankai()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(3, 0);
        SetScaling(0, 3, 0);

        SetExhaust(true);
        SetMultiDamage(true);
        this.series = CardSeries.Bleach;
        SetMartialArtist();
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
        GameUtilities.UseXCostEnergy(this);

        if (damage > 0)
        {
            GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.LIGHT_GRAY));
            GameActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.LIGHT_GRAY, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75f);
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
    }
}