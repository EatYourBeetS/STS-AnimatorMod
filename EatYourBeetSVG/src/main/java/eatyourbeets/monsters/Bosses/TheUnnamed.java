package eatyourbeets.monsters.Bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.Bosses.TheUnnamedMoveset.*;
import eatyourbeets.monsters.SharedMoveset.Move_Poison;
import eatyourbeets.monsters.AbstractMonsterData;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.UnnamedReign.InfinitePower;

public class TheUnnamed extends AnimatorMonster
{
    public static final String ID = CreateFullID(TheUnnamed.class.getSimpleName());
    public static final String NAME = "The Unnamed";

    private Move_Fading moveFading;
    private Move_Poison movePoison;

    public boolean appliedFading = false;
    public int minionsCount = 3;
    public final AbstractMonster[] minions = new AbstractMonster[3];

    public TheUnnamed()
    {
        super(new Data(ID), EnemyType.BOSS);

        data.SetIdleAnimation(this, 1);

        moveFading = (Move_Fading)
                moveset.AddSpecial(new Move_Fading(5));

        movePoison = (Move_Poison)
                moveset.AddSpecial(new Move_Poison(3));

        moveset.AddSpecial(new Move_SummonDoll());

        if (PlayerStatistics.GetAscensionLevel() >= 4)
        {
            moveset.AddNormal(new Move_Taunt());
            moveset.AddNormal(new Move_SingleAttack(25));
            moveset.AddNormal(new Move_MultiAttack(8, 3));
        }
        else
        {
            moveset.AddNormal(new Move_Taunt());
            moveset.AddNormal(new Move_SingleAttack(20));
            moveset.AddNormal(new Move_MultiAttack(7, 3));
        }
    }

    @Override
    public void die()
    {
        if (!AbstractDungeon.getCurrRoom().cannotLose)
        {
            super.die();
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();

            CardCrawlGame.stopClock = true;

            for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
            {
                if (m.hasPower(MinionPower.POWER_ID))
                {
                    GameActionsHelper.AddToBottom(new EscapeAction(m));
                }
            }

            AbstractDungeon.effectsQueue.add(new VictoryEffect());
        }
    }

    @Override
    public void usePreBattleAction()
    {
        if (!PlayerStatistics.SaveData.EnteredUnnamedReign)
        {
            AbstractDungeon.player.isDead = true;
            AbstractDungeon.player.currentHealth = 0;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            return;
        }

        //AbstractDungeon.aiRng.setCounter(AbstractDungeon.aiRng.counter + MathUtils.random(100));

        super.usePreBattleAction();

        AbstractDungeon.getCurrRoom().rewardAllowed = false;
        AbstractDungeon.getCurrRoom().rewards.clear();

        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");

        GameActionsHelper.ApplyPower(this, this, new InfinitePower(this));

        if (AbstractDungeon.player.maxHealth > 500)
        {
            GameActionsHelper.AddToBottom(new TalkAction(this, data.strings.DIALOG[1], 4, 4));
            moveFading.fadingTurns = 3;
            moveFading.ExecuteInternal(AbstractDungeon.player);
        }
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        Move_SummonDoll summonDoll = moveset.GetMove(Move_SummonDoll.class);
        if (summonDoll.CanUse(previousMove))
        {
            summonDoll.SetMove();
            return;
        }

        if (minionsCount <= 0 && moveFading.CanUse(previousMove))
        {
            if (moveFading.fadingTurns > 2)
            {
                moveFading.fadingTurns -= 1;
            }
            moveFading.SetMove();

            return;
        }

        if (moveset.GetMove(previousMove) instanceof Move_Taunt)
        {
            if (movePoison.poisonAmount > 6)
            {
                movePoison.poisonAmount += 2;
            }
            else
            {
                movePoison.poisonAmount += 1;
            }

            movePoison.SetMove();

            return;
        }

        super.SetNextMove(roll, historySize, previousMove);
    }

    public void OnDollDeath()
    {
        minionsCount -= 1;
        if (minionsCount <= 0)
        {
            GameActionsHelper.AddToBottom(new TalkAction(this, data.strings.DIALOG[0], 3, 3));
            GameActionsHelper.ApplyPower(this, this, new AngryPower(this, 5), 5);
            GameActionsHelper.ApplyPower(this, this, new PlatedArmorPower(this, 16), 16);
            GameActionsHelper.ApplyPower(this, this, new RegenPower(this, 100), 100);
//            moveFading.SetMove();
//            this.createIntent();

            moveset.GetMove(Move_Taunt.class).disabled = true;
            moveset.AddNormal(movePoison);
            rollMove();
            createIntent();
        }
    }

    private boolean deathNoteMessage = false;
    public void TriedUsingDeathNote()
    {
        if (!deathNoteMessage)
        {
            deathNoteMessage = true;
            GameActionsHelper.AddToBottom(new TalkAction(this, data.strings.DIALOG[2], 3, 3));
        }
    }

    protected static class Data extends AbstractMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = 1000;
            atlasUrl = "images/monsters/Animator_TheUnnamed/TheUnnamed.atlas";
            jsonUrl = "images/monsters/Animator_TheUnnamed/TheUnnamed.json";

            SetHB(0, -20, 200, 260, 0, 80);
        }
    }

    protected static class VictoryEffect extends AbstractGameEffect
    {
        public VictoryEffect()
        {
            this.startingDuration = this.duration = 3f;
        }

        @Override
        public void update()
        {
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration <= 0)
            {
                MapRoomNode cur = AbstractDungeon.currMapNode;

                AbstractDungeon.nextRoom = new MapRoomNode(cur.x, cur.y + 1);
                AbstractDungeon.nextRoom.room = new TrueVictoryRoom();
                AbstractDungeon.nextRoomTransitionStart();

                this.isDone = true;
            }
        }

        @Override
        public void render(SpriteBatch spriteBatch)
        {

        }

        @Override
        public void dispose()
        {

        }
    }
}
