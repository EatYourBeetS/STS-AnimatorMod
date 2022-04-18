package eatyourbeets.monsters.UnnamedReign.UltimateShape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.animator.special.Essence_Egnaro;
import eatyourbeets.cards.animator.special.Essence_Eruza;
import eatyourbeets.cards.animator.special.Essence_Wolley;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.EYBMove_AttackDebuff;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Buff;
import eatyourbeets.monsters.SharedMoveset.special.EYBMove_AttackDefend_ViceCrush;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.monsters.LaserDefensePower;
import eatyourbeets.powers.monsters.UltimateShapePower;
import eatyourbeets.resources.GR;
import eatyourbeets.rewards.animator.SpecialCardReward;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class UltimateShape extends EYBMonster
{
    public static final String ID = CreateFullID(UltimateShape.class);
    public static final MonsterStrings STRINGS = GR.GetMonsterStrings(ID);
    public static final String NAME = STRINGS.NAME;
    private static final float ANIMATION_EVENT = 1f;

    private int buff_counter;
    private int attack_counter;
    private int debuff_counter;
    private boolean stopAnimation;
    private boolean phase2;
    private float lastAnimationTime = ANIMATION_EVENT;
    private EYBAbstractMove special;
    private EYBAbstractMove buff;

    public static void ModifyRewards(ArrayList<RewardItem> rewards)
    {
        final ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.add(new Essence_Eruza());
        cards.add(new Essence_Egnaro());
        cards.add(new Essence_Wolley());
        rewards.clear();
        rewards.add(new SpecialCardReward(cards));
        rewards.add(new RewardItem(80));
    }

    public UltimateShape(float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        data.SetIdleAnimation(this, 0.5f);

        special = moveset.AddSpecial(new EYBMove_AttackDefend_ViceCrush(60, 60)).SetUses(1)
        .SetOnUse((m, c) -> GameActions.Bottom.Callback(() ->
        {
            final float hpRatio = GameUtilities.GetHealthPercentage(this, false, false);
            final int hpLoss = 2 + Mathf.RoundToInt(10 * ((0.6f - Mathf.Clamp(hpRatio, 0.2f, 0.6f)) / 0.4f));
            GameActions.Bottom.StackPower(new CombustPower(AbstractDungeon.player, hpLoss, hpLoss * 2));
        }));

        moveset.mode = EYBMoveset.Mode.Sequential;

        buff = moveset.Normal.Add(new EYBMove_Buff(0)
        .SetOnUse((m, t) ->
        {
            switch (buff_counter % 2)
            {
                case 0:
                {
                    GameActions.Bottom.StackPower(new StrengthPower(this, 6 + (phase2 ? 2 : 0)));
                    break;
                }
                case 1:
                {
                    GameActions.Bottom.StackPower(new PlatedArmorPower(this,  phase2 ? 16 : 10));
                    GameActions.Bottom.StackPower(new LaserDefensePower(this, 1 + (buff_counter / 2)));
                    GameActions.Bottom.StackPower(new ArtifactPower(this, 1));
                    break;
                }
            }

            buff_counter += 1;
        }));

        moveset.Normal.Attack(3, 3)
        .SetDamageBonus(4, 1)
        .SetAttackEffect(AttackEffects.SPEAR, CreatureAnimation.ATTACK_FAST)
        .SetOnUse((m, t) -> attack_counter++);

        moveset.Normal.Add(new EYBMove_AttackDebuff(6, 3))
        .SetDamageBonus(4, 2)
        .SetAttackEffect(AttackEffects.DARK, CreatureAnimation.ATTACK_SLOW)
        .SetOnUse((m, t) ->
        {
            final int amount = -(m.misc.Calculate() + (phase2 ? 2 : 0));
            final int strength = amount - Math.max(0, GameUtilities.GetPowerAmount(StrengthPower.POWER_ID) - 1);
            final int dexterity = amount - Math.max(0, GameUtilities.GetPowerAmount(DexterityPower.POWER_ID) - 1);
            final int focus = amount - Math.max(0, GameUtilities.GetPowerAmount(FocusPower.POWER_ID) - 1);
            GameActions.Bottom.GainTemporaryStats(strength, dexterity, focus);
        });
    }

    @Override
    protected void SetNextMove(int roll, int historySize)
    {
        final float hpPercentage = GameUtilities.GetHealthPercentage(this);
        final int turnMulti = Math.max(0, 3 - (CombatStats.TurnCount(true) / 3));
        if (hpPercentage <= (0.5f + (turnMulti * 0.05f)))
        {
            phase2 = true;

            if (special.uses > 0 && buff.id == previousMove)
            {
                final int playerHP = GameUtilities.GetHP(AbstractDungeon.player, true, false);
                final int amount = Math.max(60, Math.min(150, playerHP)) + (turnMulti * 25);
                special.SetBlock(amount);
                special.SetDamage(amount, 1);
                special.Select(false);
                return;
            }
        }

        super.SetNextMove(roll, historySize);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.GainBlock(this, 25).SetVFX(true, true);
        GameActions.Bottom.StackPower(new UltimateShapePower(this, 360));
        GameActions.Bottom.StackPower(new PlatedArmorPower(this, 25));
        GameActions.Bottom.StackPower(new ArtifactPower(this, 3));
    }

    @Override
    public void update()
    {
        super.update();

        final AnimationState.TrackEntry entry = state.getCurrent(0);
        if (entry == null)
        {
            return;
        }

        final float currentTime = entry.getTime() % entry.getEndTime();
        if (currentTime >= ANIMATION_EVENT && lastAnimationTime < ANIMATION_EVENT)
        {
            final float scale = state.getTimeScale();
            final float pitch = Math.max(0.3f, 0.45f - (0.05f * scale));
            final float volume = Math.max(0.6f, 0.95f - (0.1f * scale));
            SFX.Play(SFX.RELIC_DROP_ROCKY, pitch, pitch * 1.1f, volume);
        }

        lastAnimationTime = currentTime;
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);

        refreshAnimationSpeed(null);
    }

    @Override
    public void die(boolean triggerRelics)
    {
        if (!isDying)
        {
            if (stopAnimation) // already dead
            {
                return;
            }

            refreshAnimationSpeed(true);
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.intentAlphaTarget = 0;

            for (int i = 0; i < 16; i++)
            {
                GameActions.Bottom.VFX(VFX.SmallExplosion(hb, 0.4f));
            }
            GameActions.Bottom.WaitRealtime(0.3f);
            GameActions.Bottom.VFX(VFX.Hemokinesis(EYBEffect.SKY_HB_R, hb).SetColor(new Color(1f, 0.2f, 0.2f, 1)));
            GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_FAST_2, 0.95f, 1.05f);
            GameActions.Bottom.WaitRealtime(0.3f);
            GameActions.Bottom.VFX(VFX.Hemokinesis(EYBEffect.SKY_HB_L, hb).SetColor(new Color(0.2f, 1f, 0.2f, 1)));
            GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_FAST_2, 0.95f, 1.05f);
            GameActions.Bottom.WaitRealtime(0.3f);
            GameActions.Bottom.VFX(VFX.Hemokinesis(EYBEffect.SKY_HB_R, hb).SetColor(new Color(0.2f, 0.2f, 1f, 1)));
            GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_FAST_2, 0.95f, 1.05f);
            GameActions.Bottom.Heal(AbstractDungeon.player.maxHealth / 2);
            GameActions.Bottom.WaitRealtime(0.8f);
            GameActions.Bottom.Callback(triggerRelics, (trigger, __) -> super.die(trigger));
        }
        else
        {
            super.die(triggerRelics);
        }
    }

    protected void refreshAnimationSpeed(Boolean stop)
    {
        if (stop != null)
        {
            this.stopAnimation = stop;
        }

        if (stopAnimation)
        {
            state.setTimeScale(0.05f);
        }
        else
        {
            final float scale = Interpolation.smooth.apply(0.5f, 8f, (1 - GameUtilities.GetHealthPercentage(this)));
            state.setTimeScale(scale);
        }
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            scale = 0.6f;
            jsonUrl = "images/animator/monsters/UltimateShape/UltimateShape.json";
            atlasUrl = "images/animator/monsters/UltimateShape/UltimateShape.atlas";
            imgUrl = null;
            maxHealth = 1360;

            SetHB(0, -140, 300, 280, 0, 0);
        }
    }
}
