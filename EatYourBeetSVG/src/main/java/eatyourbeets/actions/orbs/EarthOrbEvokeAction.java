package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.actions.animator.AnimatorAction;

import java.util.ArrayList;

public class EarthOrbEvokeAction extends AnimatorAction
{
    private final AbstractPlayer p;
    private int step;
    private final int orbDamage;
    private int damageDealt;

    public EarthOrbEvokeAction(int orbDamage)
    {
        this.p = AbstractDungeon.player;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.duration = 0.1F;
        this.orbDamage = orbDamage;
        this.damageDealt = 0;
        this.step = (int)Math.ceil(orbDamage / 8.0);
        this.isDone = step <= 0;
    }

    public void update()
    {
        if (damageDealt < orbDamage)
        {
            if (damageDealt == 0)
            {
                CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", 0.1F);
            }

            if ((damageDealt + step) > orbDamage)
            {
                step = orbDamage - damageDealt;
            }

            if (step > 0)
            {
                GameActionsHelper.AddToTop(new EarthDamageAction(step));

                damageDealt += step;
            }
            else
            {
                this.isDone = true;
            }
        }
        else
        {
            this.isDone = true;
        }
    }


    private class EarthDamageAction extends AnimatorAction
    {
        private final int baseDamage;

        private EarthDamageAction(int baseDamage)
        {
            this.baseDamage = baseDamage;
        }

        @Override
        public void update()
        {
            ArrayList<AbstractMonster> enemies = GameUtilities.GetCurrentEnemies(true);
            AbstractMonster m = JavaUtilities.GetRandomElement(enemies);
            if (m != null && baseDamage > 0)
            {
                int actualDamage = AbstractOrb.applyLockOn(m, baseDamage);

                //CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", AbstractDungeon.miscRng.random(0.1f, 0.3f));
                //CardCrawlGame.sound.play("ATTACK_HEAVY", AbstractDungeon.miscRng.random(0.1f, 0.3f));

                AttackEffect effect;
                switch (AbstractDungeon.cardRandomRng.random(2))
                {
                    case 0:
                        effect = AttackEffect.SMASH;
                        break;
                    case 1:
                        effect = AttackEffect.BLUNT_LIGHT;
                        break;
//                    case 2: effect = AttackEffect.BLUNT_HEAVY; break;
//                    case 3: effect = AttackEffect.SLASH_VERTICAL; break;
                    default:
                        effect = AttackEffect.NONE;
                        break;
                }

                GameActionsHelper.AddToTop(new DamageAction(m, new DamageInfo(p, actualDamage, DamageInfo.DamageType.THORNS), effect, true));
            }

            this.isDone = true;
        }
    }
}
