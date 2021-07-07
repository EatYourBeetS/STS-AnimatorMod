package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.effects.vfx.SnowballEffect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SheerColdPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SheerColdPower.class);

    public SheerColdPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        AbstractCreature target;

        for (AbstractOrb orb : player.orbs) {
            if (Frost.ORB_ID.equals(orb.ID)) {
                if (owner.isPlayer) {
                    target = GameUtilities.GetRandomEnemy(true);
                }
                else {
                    target = player;
                }
                this.applyPower(target, orb, orb.passiveAmount * this.amount);
            }
        }
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Frost.ORB_ID.equals(orb.ID)) {
            if (owner.isPlayer)
            {
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
                {
                    this.applyPower(enemy, orb, orb.evokeAmount * this.amount);
                }
            }
            else {
                this.applyPower(player, orb, orb.evokeAmount * this.amount);
            }
        }
    }

    private void applyPower(AbstractCreature target, AbstractOrb orb, int applyAmount) {
        if (target != null) {
            GameActions.Bottom.Add(new ApplyPowerAction(target, owner, new ChilledPower(target, applyAmount), applyAmount, true));
            GameActions.Top.Wait(0.15f);
            GameActions.Top.VFX(new SnowballEffect(orb.hb.cX, orb.hb.cY, target.hb.cX, target.hb.cY)
                    .SetColor(Color.SKY, Color.CYAN).SetRealtime(true));
        }
    }
}
