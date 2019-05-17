package eatyourbeets.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.relics.ExquisiteBloodVial;

import java.util.ArrayList;

public class KrulTepes extends CustomMonster
{
    public static final String ID = "Animator_KrulTepes";
    public static final String NAME = "Krul Tepes";

    private final BobEffect bobEffect = new BobEffect(1);
    private final ArrayList<Move> moveset = new ArrayList<>();

    private static int GetMaxHealth()
    {
        return AbstractDungeon.ascensionLevel >= 6 ? 366 : 344;
    }

    public KrulTepes()
    {
        super(NAME, ID, GetMaxHealth(), 0.0F, 0.0F, 200.0F, 280.0F, AnimatorResources.GetMonsterImage(ID));

        this.type = EnemyType.BOSS;

        int level = AbstractDungeon.ascensionLevel;

        moveset.add(new Move_Regenerate(0, level, this));
        moveset.add(new Move_Bite(1, level, this));
        moveset.add(new Move_GuardedAttack(2, level, this));
        moveset.add(new Move_MultiSlash(3, level, this));
        moveset.add(new Move_PowerUp(4, level, this));
        moveset.add(new Move_Cripple(5, level, this));
    }

    @Override
    public void render(SpriteBatch sb)
    {
        animY = this.bobEffect.y;
        super.render(sb);
    }

    @Override
    public void update()
    {
        this.bobEffect.update();
        super.update();
    }

    @Override
    public void die()
    {
        super.die();

        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        playBossStinger();

        ExquisiteBloodVial exquisiteBloodVial = (ExquisiteBloodVial)AbstractDungeon.player.getRelic(ExquisiteBloodVial.ID);
        if (exquisiteBloodVial != null)
        {
            exquisiteBloodVial.UnlockPotential();
        }
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new IntangiblePlayerPower(this, 1), 1);
        GameActionsHelper.ApplyPower(this, this, new ArtifactPower(this, 2), 2);

//        AbstractDungeon.monsterList.removeIf(s -> s.equals(ID));

        AbstractPlayer p = AbstractDungeon.player;
        p.drawPile.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.KrulTepes);
        p.discardPile.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.KrulTepes);
        p.hand.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.KrulTepes);

        AbstractDungeon.getCurrMapNode().room.playBGM("BOSS_BOTTOM");
    }

    @Override
    public void takeTurn()
    {
        moveset.get(nextMove).Execute(AbstractDungeon.player);

        GameActionsHelper.AddToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i)
    {
        int size = moveHistory.size();
        if (size <= 12 && size % 3 == 0)
        {
            moveset.get(0).SetMove();
            return;
        }

        Byte previousMove = moveHistory.get(size - 1);

        ArrayList<Move> moves = new ArrayList<>();
        for (Move move : moveset)
        {
            if (move.CanUse(previousMove))
            {
                moves.add(move);
            }
        }

        moves.get(i % moves.size()).SetMove();
    }

    public static abstract class Move
    {
        public final AbstractMonster owner;
        public final int ascensionLevel;
        public final byte id;

        public Move(byte id, int ascensionLevel, AbstractMonster owner)
        {
            this.owner = owner;
            this.ascensionLevel = ascensionLevel;
            this.id = id;
        }

        public abstract void Execute(AbstractPlayer target);
        public abstract void SetMove();

        public boolean CanUse(Byte previousMove)
        {
            return previousMove != id;
        }
    }

    public static class Move_Cripple extends Move
    {
        private final int FRAIL_AMOUNT;
        private final int WEAK_AMOUNT;
        private final int BLOCK_AMOUNT;
        private final int VULNERABLE_AMOUNT;

        public Move_Cripple(int id, int ascensionLevel, AbstractMonster owner)
        {
            super((byte) id, ascensionLevel, owner);

            if (ascensionLevel >= 8)
            {
                FRAIL_AMOUNT = 2;
                WEAK_AMOUNT = 3;
                VULNERABLE_AMOUNT = 3;
                BLOCK_AMOUNT = 19;
            }
            else
            {
                FRAIL_AMOUNT = 2;
                WEAK_AMOUNT = 2;
                VULNERABLE_AMOUNT = 2;
                BLOCK_AMOUNT = 16;
            }
        }

        public void SetMove()
        {
            owner.setMove(id, Intent.DEFEND_DEBUFF);
        }

        public void Execute(AbstractPlayer target)
        {
            GameActionsHelper.GainBlock(owner, BLOCK_AMOUNT);
            GameActionsHelper.ApplyPower(owner, target, new WeakPower(target, WEAK_AMOUNT, true), WEAK_AMOUNT);
            GameActionsHelper.ApplyPower(owner, target, new VulnerablePower(target, VULNERABLE_AMOUNT, true), VULNERABLE_AMOUNT);
            GameActionsHelper.ApplyPower(owner, target, new FrailPower(target, FRAIL_AMOUNT, true), FRAIL_AMOUNT);
        }
    }

    public static class Move_PowerUp extends Move
    {
        private final int ARTIFACT_AMOUNT;
        private final int STRENGTH_AMOUNT;

        public Move_PowerUp(int id, int ascensionLevel, AbstractMonster owner)
        {
            super((byte) id, ascensionLevel, owner);

            if (ascensionLevel >= 8)
            {
                STRENGTH_AMOUNT = 4;
                ARTIFACT_AMOUNT = 1;
            }
            else
            {
                STRENGTH_AMOUNT = 3;
                ARTIFACT_AMOUNT = 1;
            }
        }

        public void SetMove()
        {
            owner.setMove(id, Intent.BUFF);
        }

        public void Execute(AbstractPlayer target)
        {
            GameActionsHelper.ApplyPower(owner, owner, new ArtifactPower(owner, ARTIFACT_AMOUNT), ARTIFACT_AMOUNT);
            GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, STRENGTH_AMOUNT), STRENGTH_AMOUNT);
        }
    }

    public static class Move_Regenerate extends Move
    {
        private final int REGEN_AMOUNT;
        private final int BLOCK_AMOUNT;
        private final int THORNS_AMOUNT;
        //private int uses;

        public Move_Regenerate(int id, int ascensionLevel, AbstractMonster owner)
        {
            super((byte) id, ascensionLevel, owner);

            if (ascensionLevel >= 8)
            {
                REGEN_AMOUNT = 6;
                BLOCK_AMOUNT = 20;
                THORNS_AMOUNT = 1;
            }
            else
            {
                REGEN_AMOUNT = 5;
                BLOCK_AMOUNT = 18;
                THORNS_AMOUNT = 1;
            }

            //uses = 4;
        }

        @Override
        public boolean CanUse(Byte previousMove)
        {
            return false; // must be used manually
        }

        public void SetMove()
        {
            owner.setMove(id, Intent.DEFEND_BUFF);
        }

        public void Execute(AbstractPlayer target)
        {
            //uses -= 1;
            GameActionsHelper.GainBlock(owner, BLOCK_AMOUNT);
            GameActionsHelper.ApplyPower(owner, owner, new RegenPower(owner, REGEN_AMOUNT), REGEN_AMOUNT);
            GameActionsHelper.ApplyPower(owner, owner, new ThornsPower(owner, THORNS_AMOUNT), THORNS_AMOUNT);
        }
    }

    public static class Move_Bite extends Move
    {
        private final DamageInfo damageInfo;
        private final int STRENGTH_AMOUNT;
        private final int WEAK_AMOUNT;

        public Move_Bite(int id, int ascensionLevel, AbstractMonster owner)
        {
            super((byte) id, ascensionLevel, owner);

            if (ascensionLevel >= 6)
            {
                damageInfo = new DamageInfo(owner, 26);
                STRENGTH_AMOUNT = 2;
                WEAK_AMOUNT = 2;
            }
            else
            {
                damageInfo = new DamageInfo(owner, 28);
                STRENGTH_AMOUNT = 3;
                WEAK_AMOUNT = 2;
            }
        }

        public void SetMove()
        {
            owner.setMove(id, Intent.ATTACK_DEBUFF, damageInfo.base);
        }

        public void Execute(AbstractPlayer target)
        {
            damageInfo.applyPowers(owner, target);
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(target.hb.cX, target.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));
            GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.NONE));
            GameActionsHelper.ApplyPower(owner, target, new WeakPower(target, WEAK_AMOUNT, true), WEAK_AMOUNT);
            GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, STRENGTH_AMOUNT), STRENGTH_AMOUNT);
        }
    }

    public static class Move_GuardedAttack extends Move
    {
        private final DamageInfo damageInfo;
        private final int BLOCK_AMOUNT;

        public Move_GuardedAttack(int id, int ascensionLevel, AbstractMonster owner)
        {
            super((byte) id, ascensionLevel, owner);

            if (ascensionLevel >= 6)
            {
                damageInfo = new DamageInfo(owner, 18);
                BLOCK_AMOUNT = 22;
            }
            else
            {
                damageInfo = new DamageInfo(owner, 20);
                BLOCK_AMOUNT = 22;
            }
        }

        public void SetMove()
        {
            owner.setMove(id, Intent.ATTACK_DEFEND, damageInfo.base);
        }

        public void Execute(AbstractPlayer target)
        {
            damageInfo.applyPowers(owner, target);
            GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            GameActionsHelper.GainBlock(owner, BLOCK_AMOUNT);
        }
    }

    public static class Move_MultiSlash extends Move
    {
        private final DamageInfo damageInfo;
        private final int TIMES;

        private int timesCounter;

        public Move_MultiSlash(int id, int ascensionLevel, AbstractMonster owner)
        {
            super((byte) id, ascensionLevel, owner);

            if (ascensionLevel >= 6)
            {
                damageInfo = new DamageInfo(owner, 2);
                TIMES = 4;
            }
            else
            {
                damageInfo = new DamageInfo(owner, 3);
                TIMES = 4;
            }
        }

        @Override
        public boolean CanUse(Byte previousMove)
        {
            if (super.CanUse(previousMove))
            {
                StrengthPower strength = Utilities.SafeCast(owner.getPower(StrengthPower.POWER_ID), StrengthPower.class);

                return strength != null && strength.amount > 3;
            }

            return false;
        }

        public void SetMove()
        {
            owner.setMove(id, Intent.ATTACK, damageInfo.base, TIMES, true);
        }

        public void Execute(AbstractPlayer target)
        {
            damageInfo.applyPowers(owner, target);
            timesCounter = TIMES;
            Attack(target);
        }

        private void Attack(AbstractPlayer target)
        {
            if (timesCounter > 0)
            {
                GameActionsHelper.Callback(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.SLASH_HEAVY), this::OnDamage, target.currentHealth);
                timesCounter -= 1;
            }
        }

        private void OnDamage(Object state, AbstractGameAction action)
        {
            Integer previousHealth = Utilities.SafeCast(state, Integer.class);
            if (previousHealth != null)
            {
                AbstractPlayer p = AbstractDungeon.player;

                int difference = previousHealth - p.currentHealth;
                if (difference > 0)
                {
                    GameActionsHelper.AddToBottom(new HealAction(owner, owner, difference));
                }

                Attack(p);
            }
        }
    }
}
