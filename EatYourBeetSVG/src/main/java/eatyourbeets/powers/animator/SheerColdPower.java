package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.effects.vfx.SnowballEffect;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class SheerColdPower extends AnimatorPower implements OnOrbPassiveEffectSubscriber
{
    public static final String POWER_ID = CreateFullID(SheerColdPower.class);

    public SheerColdPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onOrbPassiveEffect.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onOrbPassiveEffect.Unsubscribe(this);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Frost.ORB_ID.equals(orb.ID)) {
            if (owner.isPlayer)
            {
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
                {
                    this.applyPower(enemy, orb, orb.evokeAmount  / 2);
                }
            }
            else {
                this.applyPower(player, orb, orb.evokeAmount / 2);
            }
        }
    }

    @Override
    public void OnOrbPassiveEffect(AbstractOrb orb) {
        if (Frost.ORB_ID.equals(orb.ID)) {

            AbstractCreature target = player;
            ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
            if (owner.isPlayer && enemies.size() > 0) {
                int highestAttack = Integer.MIN_VALUE;
                float chance = (float) (1/(Math.max(enemies.size(),1.0)));

                for (AbstractMonster m : enemies)
                {
                    int damage = GameUtilities.GetIntent(m).GetDamage(true);
                    if (damage > highestAttack)
                    {
                        highestAttack = damage;
                        target = m;
                    }
                    else if (damage == highestAttack && rng.randomBoolean(chance)) {
                        target = m;
                    }
                }
            }
            this.applyPower(target, orb, orb.passiveAmount / 2);
        }
    }

    private void applyPower(AbstractCreature target, AbstractOrb orb, int applyAmount) {
        if (target != null) {
            GameActions.Top.Wait(.15f);
            GameActions.Top.VFX(new SnowballEffect(orb.hb.cX, orb.hb.cY, target.hb.cX, target.hb.cY)
                    .SetColor(Color.SKY, Color.CYAN).SetRealtime(true));
            GameActions.Bottom.ApplyFreezing(owner, target, applyAmount).CanStack(true);
        }
    }
}
