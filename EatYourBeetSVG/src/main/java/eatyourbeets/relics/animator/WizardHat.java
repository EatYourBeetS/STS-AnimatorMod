package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class WizardHat extends AnimatorRelic
{
    public static final String ID = CreateFullID(WizardHat.class);
    public static final int INTELLECT_AMOUNT = 2;
    public static final int DAMAGE_AMOUNT = 32;
    public static final int ENERGY_COST = 4;

    public WizardHat()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(INTELLECT_AMOUNT, ENERGY_COST, DAMAGE_AMOUNT);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        SetEnabled(true);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetEnabled(true);
        GameActions.Bottom.GainIntellect(INTELLECT_AMOUNT);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (IsEnabled())
        {
            int energy = EnergyPanel.getCurrentEnergy();
            if (energy >= 4)
            {
                SetEnabled(false);

                GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.1f);
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.2f);
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.ORANGE));
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.3f);
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
                GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f);

                for (AbstractCreature m : GameUtilities.GetEnemies(true))
                {
                    GameActions.Bottom.VFX((new FlameBarrierEffect(m.hb_x, m.hb_y)));
                    GameActions.Bottom.VFX((new ExplosionSmallEffect(m.hb_x, m.hb_y)));
                }

                int[] multiDamage = DamageInfo.createDamageMatrix(DAMAGE_AMOUNT, true);
                GameActions.Bottom.DealDamageToAll(multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
            }
        }
    }
}