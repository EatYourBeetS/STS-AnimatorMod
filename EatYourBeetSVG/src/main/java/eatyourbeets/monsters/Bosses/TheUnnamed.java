package eatyourbeets.monsters.Bosses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.actions.monsters.TheUnnamed_SummonDollAction;
import eatyourbeets.actions.special.PlayTempBgmAction;
import eatyourbeets.actions.special.SendMinionsAway;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.blights.animator.Doomed;
import eatyourbeets.cards.animator.special.Respite;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Special;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Unknown;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.powers.common.PoisonPlayerPower;
import eatyourbeets.powers.monsters.TheUnnamedPower;
import eatyourbeets.relics.animator.unnamedReign.Ynitaph;
import eatyourbeets.resources.GR;
import eatyourbeets.scenes.TheUnnamedReignScene;
import eatyourbeets.utilities.*;

public class TheUnnamed extends EYBMonster
{
    public static final String ID = CreateFullID(TheUnnamed.class);
    public static final String NAME = "The Unnamed";

    public final AbstractMonster[] minions = new AbstractMonster[3];
    public boolean appliedFading = false;
    public int minionsCount = 3;

    private final RandomizedList<String> taunt = new RandomizedList<>();
    private final EYBAbstractMove moveFading;
    private final EYBAbstractMove movePoison;
    private final EYBAbstractMove moveSummon;
    private final EYBAbstractMove moveTaunt;
    private final TheUnnamedPower infinitePower;
    private boolean triedUsingDeathNote;

    public TheUnnamed()
    {
        super(new Data(ID), EnemyType.BOSS);

        data.SetIdleAnimation(this, 1);
        infinitePower = new TheUnnamedPower(this);

        moveFading = moveset.Special.Add(new EYBMove_Special()).SetMisc(4)
        .SetCanUse((m, __) -> !AbstractDungeon.player.hasBlight(Doomed.ID))
        .SetOnUse((m, t) ->
        {
            final int turns = m.misc.Calculate();
            GameActions.Bottom.SFX(SFX.MONSTER_COLLECTOR_DEBUFF);
            GameActions.Bottom.VFX(new CollectorCurseEffect(t.hb.cX, t.hb.cY), 2f);
            GameActions.Bottom.Callback(turns, (i, __) ->GameUtilities.ObtainBlight(hb.cX, hb.cY, new Doomed(i)));
            GameActions.Bottom.Add(new PlayTempBgmAction("MINDBLOOM", 1));
            AbstractDungeon.player.drawPile.addToTop(new Respite());
        });

        movePoison = moveset.Special.Add(new EYBMove_Unknown())
        .SetMisc(3).SetData(1)
        .SetMiscBonus(4, 1)
        .SetIntent(Intent.STRONG_DEBUFF)
        .SetOnUse((m, t) ->
        {
            final int poisonAmount = (int)m.data;
            final int times = m.misc.Calculate();
            for (int i = 0; i < times; i++)
            {
                GameActions.Bottom.VFX(new PotionBounceEffect(t.hb.cX + MathUtils.random(-5, 5),
                t.hb.cY + MathUtils.random(-5, 5), t.hb.cX, t.hb.cY), 0.4f);
                GameActions.Bottom.StackPower(this, new PoisonPlayerPower(t, this, poisonAmount));
                GameActions.Bottom.WaitRealtime(0.1f);
            }

            m.data = (poisonAmount + 1);
        });

        moveSummon = moveset.Special.Add(new EYBMove_Unknown())
        .SetOnUse((m, t) -> GameActions.Bottom.Add(new TheUnnamed_SummonDollAction(this)))
        .SetCanUse((m, b) -> m.uses > 0)
        .SetUses(3);

        //Rotation:
        moveset.Normal.Attack(26)
        .SetDamageScaling(0.25f);

        moveset.Normal.Attack(6, 3);

        moveTaunt = moveset.Normal.Add(new EYBMove_Unknown())
        .SetOnUse((m, t) -> RandomTaunt());
    }

    @Override
    public void die()
    {
        if (!infinitePower.phase2)
        {
            CardCrawlGame.startOver();
        }
        else if (!AbstractDungeon.getCurrRoom().cannotLose)
        {
            AbstractDungeon.getCurrRoom().cannotLose = true;
            GameEffects.List.Talk(this, data.strings.DIALOG[28], 1.2f);

            ChangeOverlayColor(new Color(1f, 1f, 1f, 0.3f));

            super.die();
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();

            if (Ynitaph.CanSpawn())
            {
                GameUtilities.ObtainRelic(hb.cX, hb.cY, new Ynitaph(1, true));
            }

            CardCrawlGame.stopClock = true;
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

        super.usePreBattleAction();

        GameEffects.List.Talk(this, data.strings.DIALOG[30]);

        AbstractDungeon.getCurrRoom().rewardAllowed = false;
        AbstractDungeon.getCurrRoom().rewards.clear();

        if (AbstractDungeon.player.maxHealth > 400)
        {
            GameActions.Bottom.Talk(this, data.strings.DIALOG[1], 3, 4);
            moveFading.SetMisc(4);
            moveFading.QueueActions(AbstractDungeon.player);
        }
        else
        {
            CardCrawlGame.music.silenceTempBgmInstantly();
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
    protected void SetNextMove(int roll, int historySize)
    {
        if (!hasPower(TheUnnamedPower.POWER_ID))
        {
            GameActions.Bottom.ApplyPower(this, infinitePower)
            .ShowEffect(false, true);
        }

        if (infinitePower.phase2 && moveFading.CanUse(previousMove))
        {
            moveFading.Select();

            return;
        }

        if (!infinitePower.phase2)
        {
            if (moveSummon.CanUse(previousMove))
            {
                moveSummon.Select();
                return;
            }
        }

        if (moveTaunt.id == previousMove)
        {
            movePoison.Select();

            return;
        }

        super.SetNextMove(roll, historySize);
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

        infinitePower.BeginPhase2();
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.BLACK, false));
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        AbstractDungeon.scene.fadeInAmbiance();

        ChangeOverlayColor(new Color(0.3f, 0.3f, 0.3f, 0.2f));

        if (!this.isDeadOrEscaped())
        {
            GameActions.Bottom.Add(new SendMinionsAway())
            .AddCallback(minions ->
            {
                int regen  = (minions.size() >= 1) ? 110 : 80;
                int plated = (minions.size() >= 2) ? 24 : 18;
                int angry  = (minions.size() >= 3) ? 6 : 4;

                GameActions.Bottom.Talk(this, data.strings.DIALOG[0], 1.5f, 3);
                GameActions.Bottom.StackPower(new RegenPower(this, regen));
                GameActions.Bottom.StackPower(new AngryPower(this, angry));
                GameActions.Bottom.StackPower(new PlatedArmorPower(this, plated));
                GameActions.Bottom.StackPower(new EarthenThornsPower(this, 6));
            });

            moveTaunt.disabled = true;
            moveset.Normal.Add(movePoison);

            rollMove();
            createIntent();
        }
    }

    private static void ChangeOverlayColor(Color color)
    {
        TheUnnamedReignScene scene = JUtils.SafeCast(AbstractDungeon.scene, TheUnnamedReignScene.class);
        if (scene != null)
        {
            scene.overlayColor = color;
        }
    }

    public void TriedUsingDeathNote()
    {
        if (!triedUsingDeathNote)
        {
            triedUsingDeathNote = true;
            GameActions.Bottom.Talk(this, data.strings.DIALOG[2], 2, 3);
        }
    }

    private void RandomTaunt()
    {
        if (taunt.Size() == 0)
        {
            taunt.Add(data.strings.DIALOG[4]);
            taunt.Add(data.strings.DIALOG[5]);
            taunt.Add(data.strings.DIALOG[6]);
            taunt.Add(data.strings.DIALOG[7]);
            taunt.Add(data.strings.DIALOG[8]);
            taunt.Add(data.strings.DIALOG[9]);
            taunt.Add(data.strings.DIALOG[10]);
        }

        GameActions.Bottom.Talk(this, taunt.Retrieve(AbstractDungeon.aiRng), 0.8f, 2f);
    }

    public static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = 1000 + (GameUtilities.GetAscensionLevel() * 5);
            atlasUrl = "images/animator/monsters/TheUnnamed/TheUnnamed.atlas";
            jsonUrl = "images/animator/monsters/TheUnnamed/TheUnnamed.json";

            SetHB(0, -20, 200, 260, 0, 80);
        }
    }

    protected static class VictoryEffect extends AbstractGameEffect
    {
        private final WaitRealtimeAction wait;

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
                MapRoomNode node = AbstractDungeon.currMapNode;
                node.getRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

                AbstractDungeon.nextRoom = new MapRoomNode(node.x, node.y + 1);
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
