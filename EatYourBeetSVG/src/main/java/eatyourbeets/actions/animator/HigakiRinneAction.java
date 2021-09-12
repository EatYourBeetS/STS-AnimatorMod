package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.combatOnly.ShuffleEnemiesEffect;
import eatyourbeets.effects.combatOnly.TalkEffect;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.deprecated.MarkOfPoisonPower;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public class HigakiRinneAction extends EYBAction
{
    private static final RandomizedList<String> sounds = new RandomizedList<>();
    private final WeightedList<ActionT0> actions = new WeightedList<>();
    private final HigakiRinne higakiRinne;

    public static String GetRandomSFX()
    {
        if (sounds.Size() < 6)
        {
            sounds.Add(SFX.VO_AWAKENEDONE_3);
            sounds.Add(SFX.VO_GIANTHEAD_1B);
            sounds.Add(SFX.VO_GREMLINANGRY_1A);
            sounds.Add(SFX.VO_GREMLINCALM_2A);
            sounds.Add(SFX.VO_GREMLINFAT_2A);
            sounds.Add(SFX.VO_GREMLINNOB_1B);
            sounds.Add(SFX.VO_HEALER_1A);
            sounds.Add(SFX.VO_MERCENARY_1B);
            sounds.Add(SFX.VO_MERCHANT_MB);
            sounds.Add(SFX.VO_SLAVERBLUE_2A);
            sounds.Add(SFX.THUNDERCLAP);
            sounds.Add(SFX.BELL);
            sounds.Add(SFX.ENEMY_TURN);
            sounds.Add(SFX.DEATH_STINGER);
            sounds.Add(SFX.BOSS_VICTORY_STINGER);
            sounds.Add(SFX.TINGSHA);
            sounds.Add(SFX.NECRONOMICON);
            sounds.Add(SFX.INTIMIDATE);
        }

        return sounds.RetrieveUnseeded(true);
    }

    public HigakiRinneAction(HigakiRinne higakiRinne, int amount)
    {
        super(ActionType.SPECIAL, Settings.ACTION_DUR_XFAST);

        this.higakiRinne = higakiRinne;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        CreateActions();

        int i = 0;
        while (actions.Size() > 0 && i++ < amount)
        {
            actions.Retrieve(rng).Invoke();
        }

        Complete();
    }

    private void CreateActions()
    {
        // Buffs
        actions.Add(this::GainBlock, 5);
        actions.Add(this::GainBlock3Times, 5);
        actions.Add(this::GainRandomStat, 3);
        actions.Add(this::GainRandomBuff, 2);
        actions.Add(this::GainTempHP, 2);
        actions.Add(this::GainArtifact, 1);
        actions.Add(this::GainEnergy, 1);

        // Debuffs
        actions.Add(this::DamageRandomEnemy, 3);
        actions.Add(this::ApplyRandomDebuff, 3);
        actions.Add(this::ApplyMarkOfPoison, 2);
        actions.Add(this::BouncingFlask, 2);

        // Orbs
        actions.Add(this::ChannelRandomOrb, 3);
        actions.Add(this::GainOrbSlot, 3);

        // Cards
        actions.Add(this::ObtainStatusCard, 4);
        actions.Add(this::UpgradeRandomCard, 3);
        actions.Add(this::ObtainThrowingKnife, 3);
        actions.Add(this::Motivate, 2);
        actions.Add(this::Draw, 2);
        actions.Add(this::ObtainHigakiRinne, 1);
        actions.Add(this::ObtainRandomCard, 1);
        actions.Add(this::ObtainRandomCardOfAnyColor, 1);

        // Special
        actions.Add(this::SelectCard, 3);
        actions.Add(this::PlayRandomSounds, 3);
        actions.Add(this::SilentTalk, 3);
        actions.Add(this::ShuffleEnemies, 2);
        actions.Add(this::EnqueueNewActions, 1);
    }

    private void ShuffleEnemies()
    {
        for (AbstractGameEffect effect : AbstractDungeon.effectList)
        {
            if (effect instanceof ShuffleEnemiesEffect)
            {
                GameActions.Bottom.StackPower(new EnchantedArmorPower(player, 2));
                Complete();
                return;
            }
        }

        for (AbstractGameEffect effect : AbstractDungeon.effectsQueue)
        {
            if (effect instanceof ShuffleEnemiesEffect)
            {
                GameActions.Bottom.StackPower(new EnchantedArmorPower(player, 2));
                Complete();
                return;
            }
        }

        GameEffects.Queue.Add(new ShuffleEnemiesEffect());
    }

    private void EnqueueNewActions()
    {
        GameActions.Bottom.Add(new HigakiRinneAction(higakiRinne, 3));
    }

    private void ApplyMarkOfPoison()
    {
        AbstractMonster m = GameUtilities.GetRandomEnemy(true);
        if (m != null)
        {
            GameActions.Bottom.StackPower(player, new MarkOfPoisonPower(m, player, 2));
        }
    }

    private void Motivate()
    {
        GameActions.Bottom.Motivate(1);
    }

    private void GainRandomBuff()
    {
        switch (rng.random(5))
        {
            case 0: GameActions.Bottom.GainPlatedArmor(1); break;
            case 1: GameActions.Bottom.GainMetallicize(1); break;
            case 2: GameActions.Bottom.GainInspiration(1); break;
            case 3: GameActions.Bottom.GainBlur(1); break;
            case 4: GameActions.Bottom.GainMalleable(2); break;
            case 5: GameActions.Bottom.GainTemporaryThorns(9); break;
        }
    }

    private void ApplyRandomDebuff()
    {
        switch (rng.random(5))
        {
            case 0: GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Poison, 3); break;
            case 1: GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Weak, 1); break;
            case 2: GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Vulnerable, 1); break;
            case 3: GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.LockOn, 2); break;
            case 4: GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Burning, 3); break;
            case 5: GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Constricted, 2); break;
        }
    }

    private void GainOrbSlot()
    {
        GameActions.Bottom.GainOrbSlots(1);
    }

    private void GainBlock3Times()
    {
        GameActions.Bottom.GainBlock(1);
        GameActions.Bottom.GainBlock(1);
        GameActions.Bottom.GainBlock(1);
    }

    private void ObtainRandomCardOfAnyColor()
    {
        final ArrayList<String> keys = new ArrayList<>(CardLibrary.cards.keySet());
        final String key = GameUtilities.GetRandomElement(keys);
        final AbstractCard card = CardLibrary.cards.get(key).makeCopy();
        if (GameUtilities.IsObtainableInCombat(card))
        {
            GameActions.Bottom.MakeCardInHand(card);
        }
    }

    private void SilentTalk()
    {
        GameEffects.Queue.Add(new TalkEffect(player.hb.cX + player.dialogX, player.hb.cY + player.dialogY, "", true));
    }

    private void PlayRandomSounds()
    {
        GameActions.Bottom.SFX(GetRandomSFX(), 0.5f, 1.5f);
        GameActions.Bottom.SFX(GetRandomSFX(), 0.5f, 1.5f);
        GameActions.Bottom.SFX(GetRandomSFX(), 0.5f, 1.5f);
    }

    private void ObtainRandomCard()
    {
        RandomizedList<AbstractCard> cards = GameUtilities.GetCardPoolInCombat(null);
        if (cards.Size() > 0)
        {
            GameActions.Bottom.MakeCardInHand(cards.Retrieve(rng).makeCopy());
        }
    }

    private void ObtainStatusCard()
    {
        GameActions.Bottom.MakeCardInHand(new Status_Slimed());
    }

    private void ObtainHigakiRinne()
    {
        GameActions.Bottom.MakeCardInHand(new HigakiRinne());
    }

    private void ObtainThrowingKnife()
    {
        GameActions.Bottom.MakeCardInHand(ThrowingKnife.GetRandomCard());
    }

    private void GainTempHP()
    {
        GameActions.Bottom.GainTemporaryHP(3);
    }

    private void GainArtifact()
    {
        GameActions.Bottom.GainArtifact(1);
    }

    private void GainEnergy()
    {
        GameActions.Bottom.GainEnergy(1);
    }

    private void BouncingFlask()
    {
        AbstractMonster m = GameUtilities.GetRandomEnemy(true);
        if (m != null)
        {
            GameActions.Bottom.Add(new BouncingFlaskAction(m, 2, 2));
        }
    }

    private void GainRandomStat()
    {
        GameActions.Bottom.GainRandomAffinityPower(1, false);
        GameActions.Bottom.GainRandomAffinityPower(1, false);
    }

    private void UpgradeRandomCard()
    {
        GameActions.Bottom.Add(new RandomCardUpgrade());
    }

    private void Draw()
    {
        GameActions.Bottom.Draw(1);
    }

    private void ChannelRandomOrb()
    {
        GameActions.Bottom.ChannelRandomOrbs(1);
    }

    private void DamageRandomEnemy()
    {
        for (int i = 0; i < 3; i++)
        {
            GameActions.Bottom.Add(new DamageRandomEnemyAction(new DamageInfo(player, 3, DamageInfo.DamageType.THORNS), AttackEffects.POISON));
        }
    }

    private void GainBlock()
    {
        for (int i = 0; i < 3; i++)
        {
            GameActions.Bottom.GainBlock(2);
        }
    }

    private void SelectCard()
    {
        GameActions.Bottom.SelectFromPile(higakiRinne.name, 1, player.hand)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                final AbstractMonster m = GameUtilities.GetRandomEnemy(true);
                switch (c.type)
                {
                    case ATTACK:
                        GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(), 1);
                        break;

                    case SKILL:
                        GameActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 1);
                        break;

                    case POWER:
                        GameActions.Bottom.GainRandomAffinityPower(1, true);
                        GameActions.Bottom.GainRandomAffinityPower(1, true);
                        GameActions.Bottom.GainRandomAffinityPower(1, true);
                        break;

                    case STATUS:
                        GameActions.Bottom.ApplyBurning(TargetHelper.RandomEnemy(), 3);
                        break;

                    case CURSE:
                        GameActions.Bottom.ApplyConstricted(TargetHelper.RandomEnemy(), 3);
                        break;
                }
            }
        });
    }
}
