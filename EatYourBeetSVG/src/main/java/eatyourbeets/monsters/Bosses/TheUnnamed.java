package eatyourbeets.monsters.Bosses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.monsters.AbstractMonsterData;
import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.Bosses.TheUnnamedMoveset.*;
import eatyourbeets.powers.UnnamedReign.InfinitePower;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.resources.GR;
import eatyourbeets.scenes.TheUnnamedReignScene;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class TheUnnamed extends AnimatorMonster
{
    public static final String ID = CreateFullID(TheUnnamed.class);
    public static final String NAME = "The Unnamed";

    private final Move_Fading moveFading;
    private final Move_ScalingPoison movePoison;

    private final InfinitePower infinitePower;

    public boolean appliedFading = false;
    public int minionsCount = 3;
    public final AbstractMonster[] minions = new AbstractMonster[3];

    public TheUnnamed()
    {
        super(new Data(ID), EnemyType.BOSS);

        data.SetIdleAnimation(this, 1);

        int poisonScaling;
        int singleAttackDamage;
        int multiAttackDamage;
        if (GameUtilities.GetAscensionLevel() >= 4)
        {
            poisonScaling = 4;
            singleAttackDamage = 34;
            multiAttackDamage = 6;
        }
        else
        {
            poisonScaling = 3;
            singleAttackDamage = 26;
            multiAttackDamage = 6;
        }

        moveFading = moveset.AddSpecial(new Move_Fading(4));
        movePoison = moveset.AddSpecial(new Move_ScalingPoison(1, poisonScaling));

        moveset.AddSpecial(new Move_SummonDoll());

        moveset.AddNormal(new Move_Taunt());
        moveset.AddNormal(new Move_SingleAttack(singleAttackDamage));
        moveset.AddNormal(new Move_MultiAttack(multiAttackDamage, 3));

        infinitePower = new InfinitePower(this);
    }

    @Override
    public void die()
    {
        if (!AbstractDungeon.getCurrRoom().cannotLose)
        {
            AbstractDungeon.getCurrRoom().cannotLose = true;

            GameEffects.List.Talk(this, data.strings.DIALOG[28], 1.2f);

            ChangeOverlayColor(new Color(1f, 1f, 1f, 0.3f));

            super.die();
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();

            CardCrawlGame.stopClock = true;

            RemoveMinions();

            GameEffects.Queue.Add(new VictoryEffect());
        }
    }

    @Override
    public void usePreBattleAction()
    {
        if (!GR.Common.Dungeon.IsUnnamedReign())
        {
            AbstractDungeon.player.isDead = true;
            AbstractDungeon.player.currentHealth = 0;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            return;
        }

        GameEffects.List.Talk(this, data.strings.DIALOG[30]);

//      AbstractDungeon.aiRng.setCounter(AbstractDungeon.aiRng.counter + MathUtils.random(100));

        super.usePreBattleAction();

        AbstractDungeon.getCurrRoom().rewardAllowed = false;
        AbstractDungeon.getCurrRoom().rewards.clear();

        if (AbstractDungeon.player.maxHealth > 400)
        {
            GameActions.Bottom.Talk(this, data.strings.DIALOG[1], 3, 4);
            moveFading.fadingTurns = 3;
            moveFading.ExecuteInternal(AbstractDungeon.player);
        }
        else
        {
            CardCrawlGame.music.silenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            CardCrawlGame.music.playTempBgmInstantly("BOSS_ENDING", true);
            CardCrawlGame.music.updateVolume();
        }

        GameActions.Bottom.ApplyPower(this, this, infinitePower);
    }

    @Override
    public void createIntent()
    {
        super.createIntent();

        if (intent == Intent.STUN || intent == Intent.SLEEP)
        {
            infinitePower.onSleepOrStun();
        }
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (!hasPower(InfinitePower.POWER_ID))
        {
            GameActions.Bottom.ApplyPowerSilently(this, this, infinitePower, 0);
        }

        if (infinitePower.phase2 && moveFading.CanUse(previousMove))
        {
            moveFading.SetMove();

            return;
        }

        if (!infinitePower.phase2)
        {
            Move_SummonDoll summonDoll = moveset.GetMove(Move_SummonDoll.class);
            if (summonDoll.CanUse(previousMove))
            {
                summonDoll.SetMove();
                return;
            }
        }

        if (moveset.GetMove(previousMove) instanceof Move_Taunt)
        {
            movePoison.SetMove();

            return;
        }

        super.SetNextMove(roll, historySize, previousMove);
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);

        if (this.currentHealth <= 400)
        {
            StartPhase2();
        }
    }

    public void OnDollDeath()
    {
        minionsCount -= 1;
        if (minionsCount <= 0)
        {
            StartPhase2();
        }
    }

    private void StartPhase2()
    {
        if (infinitePower.phase2)
        {
            return;
        }

        infinitePower.phase2 = true;

        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.BLACK, false));
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        AbstractDungeon.scene.fadeInAmbiance();

        ChangeOverlayColor(new Color(0.3f, 0.3f, 0.3f, 0.2f));

        if (!this.isDeadOrEscaped())
        {
            int minions = RemoveMinions();
            int regen = (minions >= 1) ? 110 : 80;
            int plated = (minions >= 2) ? 24 : 18;
            int angry = (minions >= 3) ? 6 : 4;

            GameActions.Bottom.Talk(this, data.strings.DIALOG[0], 3, 3);
            GameActions.Bottom.StackPower(new RegenPower(this, regen));
            GameActions.Bottom.StackPower(new AngryPower(this, angry));
            GameActions.Bottom.StackPower(new PlatedArmorPower(this, plated));
            GameActions.Bottom.StackPower(new EarthenThornsPower(this, 6));

//            moveFading.SetMove();
//            this.createIntent();

            moveset.GetMove(Move_Taunt.class).disabled = true;
            moveset.AddNormal(movePoison);
            rollMove();
            createIntent();
        }
    }

    private static void ChangeOverlayColor(Color color)
    {
        TheUnnamedReignScene scene = JavaUtilities.SafeCast(AbstractDungeon.scene, TheUnnamedReignScene.class);
        if (scene != null)
        {
            scene.overlayColor = color;
        }
    }

    private boolean deathNoteMessage = false;
    public void TriedUsingDeathNote()
    {
        if (!deathNoteMessage)
        {
            deathNoteMessage = true;
            GameActions.Bottom.Talk(this, data.strings.DIALOG[2], 3, 3);
        }
    }

    private int RemoveMinions()
    {
        int removed = 0;
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            if (m.hasPower(MinionPower.POWER_ID))
            {
                GameActions.Bottom.Add(new EscapeAction(m));
                removed += 1;
            }
        }

        return removed;
    }

    public static class Data extends AbstractMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = 1000;
            atlasUrl = "images/monsters/animator/TheUnnamed/TheUnnamed.atlas";
            jsonUrl = "images/monsters/animator/TheUnnamed/TheUnnamed.json";

            int level = GameUtilities.GetActualAscensionLevel();
            if (level > 0)
            {
                maxHealth += level * 5;
            }

            SetHB(0, -20, 200, 260, 0, 80);
        }
    }

    protected static class VictoryEffect extends AbstractGameEffect
    {
        boolean fastMode;
        WaitRealtimeAction wait;

        public VictoryEffect()
        {
            this.startingDuration = this.duration = 2.5f;
            wait = new WaitRealtimeAction(this.duration);
        }

        @Override
        public void update()
        {
            wait.update();
            if (wait.isDone)
            {
                MapRoomNode cur = AbstractDungeon.currMapNode;

                cur.getRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

                AbstractDungeon.nextRoom = new MapRoomNode(cur.x, cur.y + 1);
                AbstractDungeon.nextRoom.room = new TrueVictoryRoom();
                AbstractDungeon.nextRoomTransitionStart();

                Settings.FAST_MODE = fastMode;
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
