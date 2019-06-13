package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;

public class WizardHat extends AnimatorRelic
{
    public static final String ID = CreateFullID(WizardHat.class.getSimpleName());

    private static final int ENERGY_COST = 4;
    private static final int DAMAGE_AMOUNT = 42;

    public WizardHat()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);

        this.counter = ENERGY_COST;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + ENERGY_COST + DESCRIPTIONS[1] + DAMAGE_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        this.counter = ENERGY_COST;
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        this.counter = ENERGY_COST;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        this.counter = ENERGY_COST;
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (counter > 0)
        {
            int energy = EnergyPanel.getCurrentEnergy();
            if (energy <= 0)
            {
                return;
            }

            EnergyPanel.useEnergy(1);
            counter -= 1;

            this.flash();
            if (counter > 0)
            {
                GameActionsHelper.AddToBottom(new SFXAction("ANIMATOR_MEGUMIN_CHARGE", 0.1F));
            }
            else
            {
                counter = ENERGY_COST;

                GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_PASSIVE", 0.1F));
                GameActionsHelper.AddToBottom(new WaitAction(0.35f));
                GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_PASSIVE", 0.2F));
                GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.ORANGE)));
                GameActionsHelper.AddToBottom(new WaitAction(0.35f));
                GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_PASSIVE", 0.3F));
                GameActionsHelper.AddToBottom(new WaitAction(0.35f));
                GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.RED)));
                GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.5f));
                for (AbstractCreature m1 : PlayerStatistics.GetCurrentEnemies(true))
                {
                    GameActionsHelper.AddToBottom(new VFXAction(new FlameBarrierEffect(m1.hb_x, m1.hb_y)));
                    GameActionsHelper.AddToBottom(new VFXAction(new ExplosionSmallEffect(m1.hb_x, m1.hb_y)));
                }

                int[] multiDamage = DamageInfo.createDamageMatrix(DAMAGE_AMOUNT, true);
                GameActionsHelper.DamageAllEnemies(AbstractDungeon.player, multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
            }
        }
    }
}