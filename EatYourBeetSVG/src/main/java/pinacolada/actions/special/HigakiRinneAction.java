package pinacolada.actions.special;

import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.combatOnly.ShuffleEnemiesEffect;
import eatyourbeets.effects.combatOnly.TalkEffect;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.TargetHelper;
import eatyourbeets.utilities.WeightedList;
import pinacolada.actions.cardManipulation.RandomCardUpgrade;
import pinacolada.cards.pcl.series.Katanagatari.HigakiRinne;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.cards.pcl.status.Status_Slimed;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.common.EnchantedArmorPower;
import pinacolada.powers.deprecated.MarkOfPoisonPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

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
                PCLActions.Bottom.StackPower(new EnchantedArmorPower(player, 2));
                Complete();
                return;
            }
        }

        for (AbstractGameEffect effect : AbstractDungeon.effectsQueue)
        {
            if (effect instanceof ShuffleEnemiesEffect)
            {
                PCLActions.Bottom.StackPower(new EnchantedArmorPower(player, 2));
                Complete();
                return;
            }
        }

        PCLGameEffects.Queue.Add(new ShuffleEnemiesEffect());
    }

    private void EnqueueNewActions()
    {
        PCLActions.Bottom.Add(new HigakiRinneAction(higakiRinne, 3));
    }

    private void ApplyMarkOfPoison()
    {
        AbstractMonster m = PCLGameUtilities.GetRandomEnemy(true);
        if (m != null)
        {
            PCLActions.Bottom.StackPower(player, new MarkOfPoisonPower(m, player, 2));
        }
    }

    private void Motivate()
    {
        PCLActions.Bottom.Motivate(1);
    }

    private void GainRandomBuff()
    {
        switch (rng.random(5))
        {
            case 0: PCLActions.Bottom.GainPlatedArmor(1); break;
            case 1: PCLActions.Bottom.GainMetallicize(1); break;
            case 2: PCLActions.Bottom.GainInspiration(1); break;
            case 3: PCLActions.Bottom.GainBlur(1); break;
            case 4: PCLActions.Bottom.GainMalleable(2); break;
            case 5: PCLActions.Bottom.GainTemporaryThorns(9); break;
        }
    }

    private void ApplyRandomDebuff()
    {
        switch (rng.random(5))
        {
            case 0: PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PCLPowerHelper.Poison, 3); break;
            case 1: PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PCLPowerHelper.Weak, 1); break;
            case 2: PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PCLPowerHelper.Vulnerable, 1); break;
            case 3: PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PCLPowerHelper.LockOn, 2); break;
            case 4: PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PCLPowerHelper.Burning, 3); break;
            case 5: PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PCLPowerHelper.Constricted, 2); break;
        }
    }

    private void GainOrbSlot()
    {
        PCLActions.Bottom.GainOrbSlots(1);
    }

    private void GainBlock3Times()
    {
        PCLActions.Bottom.GainBlock(1);
        PCLActions.Bottom.GainBlock(1);
        PCLActions.Bottom.GainBlock(1);
    }

    private void ObtainRandomCardOfAnyColor()
    {
        final AbstractCard card = PCLGameUtilities.GetAnyColorCardFiltered(null, null, false);
        if (card != null)
        {
            PCLActions.Bottom.MakeCardInHand(card);
        }
    }

    private void SilentTalk()
    {
        PCLGameEffects.Queue.Add(new TalkEffect(player.hb.cX + player.dialogX, player.hb.cY + player.dialogY, "", true));
    }

    private void PlayRandomSounds()
    {
        PCLActions.Bottom.SFX(GetRandomSFX(), 0.5f, 1.5f);
        PCLActions.Bottom.SFX(GetRandomSFX(), 0.5f, 1.5f);
        PCLActions.Bottom.SFX(GetRandomSFX(), 0.5f, 1.5f);
    }

    private void ObtainRandomCard()
    {
        RandomizedList<AbstractCard> cards = PCLGameUtilities.GetCardPoolInCombat(null);
        if (cards.Size() > 0)
        {
            PCLActions.Bottom.MakeCardInHand(cards.Retrieve(rng).makeCopy());
        }
    }

    private void ObtainStatusCard()
    {
        PCLActions.Bottom.MakeCardInHand(new Status_Slimed());
    }

    private void ObtainHigakiRinne()
    {
        PCLActions.Bottom.MakeCardInHand(new HigakiRinne());
    }

    private void ObtainThrowingKnife()
    {
        PCLActions.Bottom.MakeCardInHand(ThrowingKnife.GetRandomCard());
    }

    private void GainTempHP()
    {
        PCLActions.Bottom.GainTemporaryHP(3);
    }

    private void GainArtifact()
    {
        PCLActions.Bottom.GainArtifact(1);
    }

    private void GainEnergy()
    {
        PCLActions.Bottom.GainEnergy(1);
    }

    private void BouncingFlask()
    {
        AbstractMonster m = PCLGameUtilities.GetRandomEnemy(true);
        if (m != null)
        {
            PCLActions.Bottom.Add(new BouncingFlaskAction(m, 2, 2));
        }
    }

    private void GainRandomStat()
    {
        PCLActions.Bottom.GainRandomAffinityPower(1, false);
        PCLActions.Bottom.GainRandomAffinityPower(1, false);
    }

    private void UpgradeRandomCard()
    {
        PCLActions.Bottom.Add(new RandomCardUpgrade());
    }

    private void Draw()
    {
        PCLActions.Bottom.Draw(1);
    }

    private void ChannelRandomOrb()
    {
        PCLActions.Bottom.ChannelRandomOrbs(1);
    }

    private void DamageRandomEnemy()
    {
        for (int i = 0; i < 3; i++)
        {
            PCLActions.Bottom.Add(new DamageRandomEnemyAction(new DamageInfo(player, 3, DamageInfo.DamageType.THORNS), AttackEffects.POISON));
        }
    }

    private void GainBlock()
    {
        for (int i = 0; i < 3; i++)
        {
            PCLActions.Bottom.GainBlock(2);
        }
    }

    private void SelectCard()
    {
        PCLActions.Bottom.SelectFromPile(higakiRinne.name, 1, player.hand)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                final AbstractMonster m = PCLGameUtilities.GetRandomEnemy(true);
                switch (c.type)
                {
                    case ATTACK:
                        PCLActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(), 1);
                        break;

                    case SKILL:
                        PCLActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 1);
                        break;

                    case POWER:
                        PCLActions.Bottom.GainRandomAffinityPower(1, true);
                        PCLActions.Bottom.GainRandomAffinityPower(1, true);
                        PCLActions.Bottom.GainRandomAffinityPower(1, true);
                        break;

                    case STATUS:
                        PCLActions.Bottom.ApplyBurning(TargetHelper.RandomEnemy(), 3);
                        break;

                    case CURSE:
                        PCLActions.Bottom.ApplyConstricted(TargetHelper.RandomEnemy(), 3);
                        break;
                }
            }
        });
    }
}
