package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.animator.HigakiRinne;
import eatyourbeets.effects.ShuffleEnemiesEffect;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.animator.MarkOfPoisonPower;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class HigakiRinneAction extends AnimatorAction
{
    private final HigakiRinne higakiRinne;
    private int roll;

    public HigakiRinneAction(HigakiRinne higakiRinne)
    {
        this.higakiRinne = higakiRinne;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    private boolean tryActivate(int chances)
    {
        roll -= chances;

        return roll <= 0;
    }

    public void update()
    {
        roll = AbstractDungeon.cardRandomRng.random(185);
        
        AbstractPlayer p = AbstractDungeon.player;
        if (tryActivate(6)) // 6
        {
            for (int i = 0; i < 3; i++)
            {
                GameActionsHelper.GainBlock(p, 2);
            }
        }
        else if (tryActivate(6)) // 12
        {
            for (int i = 0; i < 3; i++)
            {
                GameActionsHelper.AddToBottom(new DamageRandomEnemyAction(new DamageInfo(p, 3, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
            }
        }
        else if (tryActivate(6)) // 18
        {
            GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
        }
        else if (tryActivate(6)) // 24
        {
            GameActionsHelper.DrawCard(p, 1);
        }
        else if (tryActivate(8)) // 32
        {
            GameActionsHelper.AddToBottom(new UpgradeRandomCardAction());
        }
        else if (tryActivate(8)) // 40
        {
            GameActionsHelper.ApplyPower(p, p, new FocusPower(p, 1), 1);
        }
        else if (tryActivate(6)) // 46
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            GameActionsHelper.AddToBottom(new BouncingFlaskAction(m, 2, 2));
        }
        else if (tryActivate(6)) // 52
        {
            GameActionsHelper.GainEnergy(1);
        }
        else if (tryActivate(6)) // 58
        {
            GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, 1), 1);
        }
        else if (tryActivate(6)) // 64
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
        }
        else if (tryActivate(4)) // 68
        {
            GameActionsHelper.ApplyPower(p, p, new IntangiblePlayerPower(p, 1), 1);
        }
        else if (tryActivate(6)) // 74
        {
            GameActionsHelper.AddToBottom(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
        }
        else if (tryActivate(8)) // 82
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper.AddToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, 2, false), 2));
            }
        }
        else if (tryActivate(8)) // 90
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper.AddToBottom(new ApplyPowerAction(m, p, new WeakPower(m, 2, false), 2));
            }
        }
        else if (tryActivate(8)) // 98
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new Shiv()));
        }
        else if (tryActivate(4)) // 102
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new Madness()));
        }
        else if (tryActivate(6)) // 108
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new Slimed()));
        }
        else if (tryActivate(3)) // 111
        {
            AbstractCard card = CardLibrary.getRandomColorSpecificCard(higakiRinne.color, AbstractDungeon.cardRandomRng);
            if (!card.tags.contains(AbstractCard.CardTags.HEALING))
            {
                GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(card));
            }
        }
        else if (tryActivate(7)) // 118
        {
            GameActionsHelper.AddToBottom(new SFXAction(Utilities.GetRandomElement(sounds)));
        }
        else if (tryActivate(6)) // 124
        {
            GameActionsHelper.AddToBottom(new TalkAction(true, "???", 1.0F, 2.0F));
        }
        else if (tryActivate(2)) // 126
        {
            ArrayList<String> keys = new ArrayList<>(CardLibrary.cards.keySet());
            String key = Utilities.GetRandomElement(keys);
            AbstractCard card = CardLibrary.cards.get(key).makeCopy();
            if (!card.tags.contains(AbstractCard.CardTags.HEALING))
            {
                GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(card));
            }
        }
        else if (tryActivate(6)) // 132
        {
            for (AbstractCreature m : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.DamageTarget(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.BLUNT_HEAVY);
            }
        }
        else if (tryActivate(6)) // 138
        {
            for (AbstractCreature m : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.DamageTarget(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HEAVY);
            }
        }
        else if (tryActivate(6)) // 144
        {
            for (AbstractCreature m : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.DamageTarget(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.POISON);
            }
        }
        else if (tryActivate(6)) // 150
        {
            GameActionsHelper.GainBlock(p, 1);
            GameActionsHelper.GainBlock(p, 1);
            GameActionsHelper.GainBlock(p, 1);
        }
        else if (tryActivate(6)) // 156
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
            GameActionsHelper.ChannelOrb(new Lightning(), true);
        }
        else if (tryActivate(4)) // 160
        {
            GameActionsHelper.GainTemporaryHP(p, p, 5);
        }
        else if (tryActivate(3)) // 163
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper.AddToBottom(new ApplyPowerAction(m, p, new ConstrictedPower(m, p, 3), 3));
            }
        }
        else if (tryActivate(3)) // 166
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper.AddToBottom(new ApplyPowerAction(m, p, new BurningPower(m, p, 2), 2));
            }
        }
        else if (tryActivate(3)) // 169
        {
            GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, 1), 1);
        }
        else if (tryActivate(3)) // 172
        {
            if (p.hand.size() > 0)
            {
                AbstractCard c = p.hand.getRandomCard(true);
                if (c.costForTurn > 0)
                {
                    c.setCostForTurn(c.costForTurn - 1);
                    c.flash();
                }
            }
        }
        else if (tryActivate(3)) // 175
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            if (m != null)
            {
                GameActionsHelper.AddToBottom(new ApplyPowerAction(m, p, new MarkOfPoisonPower(m, p, 2), 2));
            }
        }
        else if (tryActivate(3)) // 178
        {
            GameActionsHelper.DrawCard(p, 3);
        }
        else if (tryActivate(2)) // 180
        {
            GameActionsHelper.AddToBottom(new HigakiRinneAction(higakiRinne));
            GameActionsHelper.AddToBottom(new HigakiRinneAction(higakiRinne));
            GameActionsHelper.AddToBottom(new HigakiRinneAction(higakiRinne));
        }
        else if (tryActivate(5)) // 185
        {
            for (AbstractGameEffect effect : AbstractDungeon.effectList)
            {
                if (effect instanceof ShuffleEnemiesEffect)
                {
                    GameActionsHelper.ApplyPower(p, p, new EnchantedArmorPower(p, 1), 1);
                    this.isDone = true;
                    return;
                }
            }

            for (AbstractGameEffect effect : AbstractDungeon.effectsQueue)
            {
                if (effect instanceof ShuffleEnemiesEffect)
                {
                    GameActionsHelper.ApplyPower(p, p, new EnchantedArmorPower(p, 1), 1);
                    this.isDone = true;
                    return;
                }
            }

            AbstractDungeon.effectsQueue.add(new ShuffleEnemiesEffect());
        }

        this.isDone = true;
    }

    public static void PlayRandomSound()
    {
        GameActionsHelper.AddToBottom(new SFXAction(Utilities.GetRandomElement(sounds)));
    }

    private static final ArrayList<String> sounds = new ArrayList<>();

    static
    {
        sounds.add("VO_AWAKENEDONE_3");
        sounds.add("VO_GIANTHEAD_1B");
        sounds.add("VO_GREMLINANGRY_1A");
        sounds.add("VO_GREMLINCALM_2A");
        sounds.add("VO_GREMLINFAT_2A");
        sounds.add("VO_GREMLINNOB_1B");
        sounds.add("VO_HEALER_1A");
        sounds.add("VO_MERCENARY_1B");
        sounds.add("VO_MERCHANT_MB");
        sounds.add("VO_SLAVERBLUE_2A");
        sounds.add("THUNDERCLAP");
        sounds.add("BELL");
        sounds.add("BELL");
        sounds.add("BELL");
        sounds.add("NECRONOMICON");
        sounds.add("INTIMIDATE");
    }
}
