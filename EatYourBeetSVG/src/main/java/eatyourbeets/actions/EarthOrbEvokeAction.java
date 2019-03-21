package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class EarthOrbEvokeAction extends AnimatorAction
{
    private AbstractPlayer p;
    private int step;
    private int orbDamage;
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
            if ((damageDealt + step) > orbDamage)
            {
                step = orbDamage - damageDealt;
            }

            ArrayList<AbstractMonster> enemies = PlayerStatistics.GetCurrentEnemies(true);
            AbstractMonster m = Utilities.GetRandomElement(enemies);
            if (m != null && step > 0)
            {
                int actualDamage = AbstractOrb.applyLockOn(m, step);

                CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", AbstractDungeon.miscRng.random(0.1f, 0.3f));

                GameActionsHelper.AddToTop(new DamageAction(m, new DamageInfo(p, actualDamage, DamageInfo.DamageType.THORNS), AttackEffect.NONE, true));
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
}
