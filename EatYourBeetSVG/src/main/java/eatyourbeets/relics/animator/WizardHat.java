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
import eatyourbeets.utilities.JavaUtilities;

public class WizardHat extends AnimatorRelic
{
    public static final String ID = CreateFullID(WizardHat.class.getSimpleName());

    private static final int INTELLECT_AMOUNT = 2;
    private static final int ENERGY_COST = 4;
    private static final int DAMAGE_AMOUNT = 32;

    public WizardHat()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], INTELLECT_AMOUNT, ENERGY_COST, DAMAGE_AMOUNT);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        this.counter = 1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        this.counter = 1;
        GameActions.Bottom.GainIntellect(INTELLECT_AMOUNT);
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (counter > 0)
        {
            int energy = EnergyPanel.getCurrentEnergy();
            if (energy >= 4)
            {
                setCounter(counter - 1);

                GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.1F);
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.2F);
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.ORANGE));
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.3F);
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
                GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f);

                for (AbstractCreature m1 : GameUtilities.GetAllEnemies(true))
                {
                    GameActions.Bottom.VFX((new FlameBarrierEffect(m1.hb_x, m1.hb_y)));
                    GameActions.Bottom.VFX((new ExplosionSmallEffect(m1.hb_x, m1.hb_y)));
                }

                int[] multiDamage = DamageInfo.createDamageMatrix(DAMAGE_AMOUNT, true);
                GameActions.Bottom.DealDamageToAll(multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
            }
        }
    }
}