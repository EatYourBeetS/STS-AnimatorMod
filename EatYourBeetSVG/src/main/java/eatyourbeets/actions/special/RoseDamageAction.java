package eatyourbeets.actions.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions._legacy.common.PiercingDamageAction;
import eatyourbeets.actions.damage.DealDamage;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.animator.QuestionMark;
import eatyourbeets.cards.animator.Rose;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class RoseDamageAction extends EYBAction
{
    private final int damage;
    private final int times;
    private final Rose rose;

    private AbstractGameAction action;

    public RoseDamageAction(AbstractMonster enemy, Rose rose, int times, int damage)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_FAST);

        this.damage = damage;
        this.rose = rose;
        this.times = times;

        Initialize(player, enemy, damage);
    }

    @Override
    public void update()
    {
        if (action != null)
        {
            action.update();

            if (action.isDone)
            {
                if (GameUtilities.IsDeadOrEscaped(target))
                {
                    int[] damageMatrix = DamageInfo.createDamageMatrix(rose.secondaryValue, true);

                    for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
                    {
                        Explosion(m.hb);
                    }

                    GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffect.NONE);
                }
                else if (times > 1)
                {
                    GameActions.Bottom.Add(new RoseDamageAction((AbstractMonster) target, rose, times - 1, damage));
                }

                Complete();
            }
        }
        else
        {
            Explosion(target.hb);

            action = new DealDamage(target, new DamageInfo(player, damage, rose.damageTypeForTurn))
            .SetOptions(true, true).SetOptions2(true, false, 0);
        }
    }

    private void Explosion(Hitbox hb)
    {
        GameActions.Top.VFX(new ExplosionSmallEffect(hb.cX + MathUtils.random(-0.05F, 0.05F),
                                                     hb.cY + MathUtils.random(-0.05F, 0.05F)),0.1F);
    }
}