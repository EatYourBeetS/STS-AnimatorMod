package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.orbs.Aether;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.Collections;

public class AirOrbPassiveAction extends AbstractGameAction
{
    private final Aether wind;

    public AirOrbPassiveAction(Aether wind)
    {
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.wind = wind;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        GameActionsHelper_Legacy.SetOrder(GameActionsHelper_Legacy.Order.Top);

        ArrayList<AbstractMonster> enemies = GameUtilities.GetCurrentEnemies(true);
        Collections.reverse(enemies);

        for (AbstractMonster m : enemies)
        {
            int actualDamage = AbstractOrb.applyLockOn(m, wind.passiveAmount);
            if (actualDamage > 0)
            {
                GameActionsHelper_Legacy.DamageTargetPiercing(p, m, actualDamage, DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HORIZONTAL);
            }
        }

        GameActionsHelper_Legacy.SFX("ATTACK_WHIRLWIND");
        GameActionsHelper_Legacy.VFX(new WhirlwindEffect(), 0);

        GameActionsHelper_Legacy.ResetOrder();

        this.isDone = true;
    }
}