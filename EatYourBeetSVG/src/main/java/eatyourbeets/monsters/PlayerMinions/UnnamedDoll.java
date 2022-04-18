package eatyourbeets.monsters.PlayerMinions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.actions.basic.GainBlock;
import eatyourbeets.actions.damage.DealDamage;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Special;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Stunned;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.powers.unnamed.CleavingAttacksPower;
import eatyourbeets.powers.unnamed.MarkedPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;
import org.lwjgl.util.vector.Vector2f;
import patches.BobEffectPatches;

public class UnnamedDoll extends EYBMonster // TODO: PlayerMinion class
{
    private static final FieldInfo<EnemyMoveInfo> _move = JUtils.GetField("move", AbstractMonster.class);
    private static final FieldInfo<Float> _intentParticleTimer = JUtils.GetField("intentParticleTimer", AbstractMonster.class);
    private static final FieldInfo<Texture> _intentBg = JUtils.GetField("intentBg", AbstractMonster.class);
    private static final FieldInfo<Texture> _intentImg = JUtils.GetField("intentImg", AbstractMonster.class);
    private static final FieldInfo<BobEffect> _intentBobEffect = JUtils.GetField("bobEffect", AbstractMonster.class);
    private static final FieldInfo<Color> _intentColor = JUtils.GetField("intentColor", AbstractMonster.class);

    public static class SummonData
    {
        public int Slot;
        public int Health;
        public int Strength;
        public int Dexterity;
        public StartingIntent Intent;
        public Vector2f Position;
    }

    public enum StartingIntent
    {
        Attack,
        Defend,
        Random
    }

    public int Slot;
    public boolean Visible;
    private final BobEffect bobEffect = new BobEffect(1);
    private final EYBAbstractMove attack;
    private final EYBAbstractMove defend;
    private final EYBAbstractMove stunned;
    private final Random rng;
    private StartingIntent mainIntent;
    private float intentBlock;
    private float intentDamage;
    private float clickDelay;

    public UnnamedDoll(SummonData data)
    {
        super(new Data(TheUnnamed_Doll.ID), EnemyType.NORMAL, data.Position.x, data.Position.y);

        this.rng = new Random(EYBPower.rng.randomLong());
        this.data.SetIdleAnimation(this, 1);
        this.flipHorizontal = true;
        this.drawX = data.Position.x;
        this.drawY = data.Position.y;
        refreshHitboxLocation();
        refreshIntentHbLocation();

        final BobEffect intentBobEffect = _intentBobEffect.Get(this);
        intentBobEffect.dist = 0;
        intentBobEffect.speed = 0;
        BobEffectPatches.SetEnabled(intentBobEffect, false);

        moveset.mode = EYBMoveset.Mode.Repeat;

        attack = moveset.Normal.Add(new EYBMove_Special())
        .SetAttackEffect(AttackEffects.BLUNT_LIGHT, CreatureAnimation.ATTACK_SLOW)
        .SetDamage(0, 1)
        .SetIntent(Intent.ATTACK)
        .SkipNormalAction(true)
        .SetOnSelect(m -> mainIntent = StartingIntent.Attack)
        .SetOnUse((m, c) -> GameActions.Bottom.Callback(m, (t1, __) -> Attack(t1)));
        defend = moveset.Normal.Add(new EYBMove_Special())
        .SetBlock(0)
        .SetIntent(Intent.DEFEND)
        .SkipNormalAction(true)
        .SetOnSelect(m -> mainIntent = StartingIntent.Defend)
        .SetOnUse((m, c) -> GameActions.Bottom.Callback(m, (t1, __) -> Defend(t1)));
        stunned = moveset.Special.Add(new EYBMove_Stunned());

        if (data.Intent == StartingIntent.Attack || (data.Intent == StartingIntent.Random && rng.randomBoolean()))
        {
            attack.Select(true);
        }
        else
        {
            defend.Select(true);
        }

        if (data.Health > 0)
        {
            currentHealth = maxHealth = data.Health;
        }
        if (data.Strength > 0)
        {
            powers.add(new StrengthPower(this, data.Strength));
        }
        if (data.Dexterity > 0)
        {
            powers.add(new DexterityPower(this, data.Dexterity));
        }
    }

    public void SwitchIntent(Intent targetIntent)
    {
        if (targetIntent == null)
        {
            targetIntent = (mainIntent == StartingIntent.Attack) ? Intent.DEFEND : Intent.ATTACK;
        }

        if (targetIntent == Intent.DEFEND)
        {
            defend.Select(true);
        }
        else if (targetIntent == Intent.ATTACK)
        {
            attack.Select(true);
        }
        else if (targetIntent == Intent.STUN)
        {
            stunned.Select(true);
        }
        else
        {
            throw new RuntimeException("Invalid Intent: " + targetIntent);
        }

        RefreshIntentNumber();
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
        _intentBobEffect.Get(this).y = bobEffect.y;

        super.update();
        updatePowers();

        if (GR.UI.Elapsed50())
        {
            RefreshIntentNumber();
        }

        this.hb.update();
        this.healthHb.update();
        this.intentHb.update();
        if (hb.hovered)
        {
            if (!GameUtilities.CanAcceptInput(false))
            {
                clickDelay = 0.1f;
            }
            else if (InputManager.RightClick.IsJustPressed() && clickDelay <= 0)
            {
                CombatStats.Dolls.ChangeIntent(this, null, false);
            }
        }

        if (clickDelay > 0)
        {
            clickDelay -= GR.UI.Delta();
        }
    }

    @Override
    public void die(boolean triggerRelics)
    {
        if (!this.isDying)
        {
            this.isDying = true;

            if (this.currentHealth <= 0)
            {
                this.currentHealth = 0;

                if (triggerRelics)
                {
                    for (AbstractPower power : powers)
                    {
                        power.onDeath();
                    }
                }
            }

            this.deathTimer += 1;
            CombatStats.Dolls.OnDeath(this);
        }
    }

    @Override
    protected void SetNextMove(int roll, int historySize)
    {
        super.SetNextMove(roll, historySize);

        createIntent();
    }

    @Override
    protected void renderDamageRange(SpriteBatch sb)
    {
        String intentText;
        switch (intent)
        {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                intentText = Integer.toString((int) intentDamage);

                if (hasPower(CleavingAttacksPower.POWER_ID))
                {
                    intentText += " all";
                }
                break;

            case DEFEND:
            case DEFEND_DEBUFF:
            case DEFEND_BUFF:
                intentText = Integer.toString((int) intentBlock);
                break;

            default:
                return;
        }

        FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, intentText, this.intentHb.cX - 30.0F * Settings.scale,
                this.intentHb.cY + _intentBobEffect.Get(this).y - 12.0F * Settings.scale, _intentColor.Get(this));
    }

    @SpireOverride
    protected void renderRedHealthBar(SpriteBatch sb, float x, float y)
    {
        if (GameUtilities.IsDefending(intent))
        {
            currentBlock += 10;
            SpireSuper.call(sb, x, y);
            currentBlock -= 10;
        }
        else
        {
            SpireSuper.call(sb, x, y);
        }
    }

    @SpireOverride
    protected void updateIntentTip()
    {
        SpireSuper.call();
    }

    @SpireOverride
    protected Texture getIntentImg()
    {
        return SpireSuper.call();
    }

    @SpireOverride
    protected Texture getIntentBg()
    {
        return SpireSuper.call();
    }

    @Override
    public void applyPowers()
    {

    }

    @Override
    public void updateAnimations()
    {
        this.isPlayer = true;
        super.updateAnimations();
        this.isPlayer = false;
        this.refreshIntentHbLocation();
    }

    @Override
    public void createIntent()
    {
        final EnemyMoveInfo move = _move.Get(this);

        this.intent = move.intent;
        this.nextMove = move.nextMove;

        RefreshIntentNumber();

        _intentParticleTimer.Set(this, 0.5F);
        _intentImg.Set(this, getIntentImg());
        _intentBg.Set(this, getIntentBg());

        this.tipIntent = this.intent;
        this.intentAlpha = 0.0F;
        this.intentAlphaTarget = 1.0F;
        this.updateIntentTip();
    }

    @Override
    public void addPower(AbstractPower powerToApply)
    {
        if (powerToApply instanceof StunMonsterPower)
        {
            SwitchIntent(Intent.STUN);
        }
        else
        {
            super.addPower(powerToApply);
        }
    }

    protected int RefreshIntentNumber()
    {
        switch (intent)
        {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                return RefreshIntentDamage();

            case DEFEND:
            case DEFEND_DEBUFF:
            case DEFEND_BUFF:
                return RefreshIntentBlock();

            default:
                return 0;
        }
    }

    protected int RefreshIntentBlock()
    {
        intentBlock = 0;
        for (AbstractPower power : powers)
        {
            intentBlock = power.modifyBlock(intentBlock);
        }
        for (AbstractPower power : powers)
        {
            intentBlock = power.modifyBlockLast(intentBlock);
        }

        return (int) intentBlock;
    }

    protected int RefreshIntentDamage()
    {
        intentDamage = 0;
        for (AbstractPower power : powers)
        {
            intentDamage = power.atDamageGive(intentDamage, DamageInfo.DamageType.NORMAL);
        }
        for (AbstractPower power : powers)
        {
            intentDamage = power.atDamageFinalGive(intentDamage, DamageInfo.DamageType.NORMAL);
        }

        return (int) intentDamage;
    }

    protected void Attack(EYBAbstractMove move)
    {
        AbstractCreature target = null;
        final RandomizedList<AbstractMonster> targets1 = new RandomizedList<>();
        final RandomizedList<AbstractMonster> targets2 = new RandomizedList<>();
        for (AbstractMonster m : GameUtilities.GetEnemies(true))
        {
            if (GameUtilities.GetDebuffsCount(m) > 0)
            {
                if (m.hasPower(MarkedPower.POWER_ID))
                {
                    target = m;
                    targets1.Clear();
                    targets2.Clear();
                    break;
                }

                targets1.Add(m);
            }
            else
            {
                targets2.Add(m);
            }
        }

        if (target == null)
        {
            if (targets1.Size() > 0)
            {
                target = targets1.Retrieve(rng);
            }
            else if (targets2.Size() > 0)
            {
                target = targets2.Retrieve(rng);
            }
            else
            {
                return;
            }
        }

        if (hasPower(CleavingAttacksPower.POWER_ID))
        {
            GameActions.Top.DealDamageToAll(DamageInfo.createDamageMatrix(RefreshIntentDamage(), true, false), DamageInfo.DamageType.THORNS, move.attackEffect)
            .SetDamageEffect((c, mute) ->
            {
                if (!mute)
                {
                    this.useFastAttackAnimation();
                }
            });
        }
        else
        {
            move.damageInfo = new DamageInfo(this, RefreshIntentDamage(), DamageInfo.DamageType.THORNS);
            move.damageInfo.applyPowers(this, target);
            GameActions.Top.Add(new DealDamage(target, move.damageInfo, move.attackEffect)).SetDamageEffect(c ->
            {
                this.useFastAttackAnimation();
                return 0f;
            });
        }
    }

    protected void Defend(EYBAbstractMove move)
    {
        float block = move.block.Calculate();
        for (AbstractPower power : powers)
        {
            block = power.modifyBlock(block);
        }
        for (AbstractPower power : powers)
        {
            block = power.modifyBlockLast(block);
        }

        GameActions.Top.Add(new GainBlock(AbstractDungeon.player, this, (int) block)).ShowVFX(false);
        GameActions.Top.Callback(() ->
        {
            AttackEffects.PlaySound(AttackEffects.SHIELD, 1.15f, 1.2f, 0.95f);
            GameEffects.Queue.AttackWithoutSound(this, this, AttackEffects.SHIELD, null, 0);
        });
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = 1;
            atlasUrl = "images/animator/monsters/TheUnnamed/TheUnnamedMinion.atlas";
            jsonUrl = "images/animator/monsters/TheUnnamed/TheUnnamedMinion.json";
            scale = 2;

            SetHB(0, -20, 120, 140, 0, 60);
        }
    }
}
