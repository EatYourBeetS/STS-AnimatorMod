package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
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
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.effects.ShuffleEnemiesEffect;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.animator.MarkOfPoisonPower;

import java.util.ArrayList;

public class HigakiRinneAction extends EYBAction
{
    private final HigakiRinne higakiRinne;
    private int roll;

    public HigakiRinneAction(HigakiRinne higakiRinne)
    {
        super(ActionType.SPECIAL, Settings.ACTION_DUR_XFAST);

        this.higakiRinne = higakiRinne;

        Initialize(1);
    }

    private boolean tryActivate(int chances)
    {
        roll -= chances;

        return roll <= 0;
    }

    @Override
    protected void FirstUpdate()
    {
        roll = AbstractDungeon.cardRandomRng.random(188);
        
        AbstractPlayer p = AbstractDungeon.player;
        if (tryActivate(3))
        {
            GameActions.Bottom.SelectFromPile(higakiRinne.name, 1, p.hand)
            .SetOptions(false, false)
            .SetMessage("???");
        }
        else if (tryActivate(6)) // 6
        {
            for (int i = 0; i < 3; i++)
            {
                GameActions.Bottom.GainBlock(2);
            }
        }
        else if (tryActivate(6)) // 12
        {
            for (int i = 0; i < 3; i++)
            {
                GameActions.Bottom.Add(new DamageRandomEnemyAction(new DamageInfo(p, 3, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
            }
        }
        else if (tryActivate(6)) // 18
        {
            GameActions.Bottom.ChannelRandomOrb(true);
        }
        else if (tryActivate(6)) // 24
        {
            GameActions.Bottom.Draw(1);
        }
        else if (tryActivate(8)) // 32
        {
            GameActions.Bottom.Add(new RandomCardUpgrade());
        }
        else if (tryActivate(8)) // 40
        {
            GameActions.Bottom.GainIntellect(1);
        }
        else if (tryActivate(6)) // 46
        {
            AbstractMonster m = GameUtilities.GetRandomEnemy(true);
            GameActions.Bottom.Add(new BouncingFlaskAction(m, 2, 2));
        }
        else if (tryActivate(6)) // 52
        {
            GameActions.Bottom.GainEnergy(1);
        }
        else if (tryActivate(6)) // 58
        {
            GameActions.Bottom.GainAgility(1);
        }
        else if (tryActivate(6)) // 64
        {
            GameActions.Bottom.GainForce(1);
        }
        else if (tryActivate(4)) // 68
        {
            GameActions.Bottom.GainArtifact(1);
        }
        else if (tryActivate(6)) // 74
        {
            GameActions.Bottom.GainTemporaryHP(2);
        }
        else if (tryActivate(8)) // 82
        {
            AbstractMonster m = GameUtilities.GetRandomEnemy(true);
            if (m != null)
            {
                GameActions.Bottom.ApplyVulnerable(p, m, 1);
            }
        }
        else if (tryActivate(8)) // 90
        {
            AbstractMonster m = GameUtilities.GetRandomEnemy(true);
            if (m != null)
            {
                GameActions.Bottom.ApplyWeak(p, m, 1);
            }
        }
        else if (tryActivate(8)) // 98
        {
            GameActions.Bottom.MakeCardInHand(new Shiv());
        }
        else if (tryActivate(4)) // 102
        {
            GameActions.Bottom.MakeCardInHand(new Madness());
        }
        else if (tryActivate(6)) // 108
        {
            GameActions.Bottom.MakeCardInHand(new Slimed());
        }
        else if (tryActivate(3)) // 111
        {
            AbstractCard card = CardLibrary.getRandomColorSpecificCard(higakiRinne.color, AbstractDungeon.cardRandomRng);
            if (!card.tags.contains(AbstractCard.CardTags.HEALING))
            {
                GameActions.Bottom.MakeCardInHand(card);
            }
        }
        else if (tryActivate(7)) // 118
        {
            GameActions.Bottom.SFX(JavaUtilities.GetRandomElement(sounds));
        }
        else if (tryActivate(6)) // 124
        {
            GameActions.Bottom.Talk(p, "???", 1.0F, 2.0F);
        }
        else if (tryActivate(2)) // 126
        {
            ArrayList<String> keys = new ArrayList<>(CardLibrary.cards.keySet());
            String key = JavaUtilities.GetRandomElement(keys);
            AbstractCard card = CardLibrary.cards.get(key).makeCopy();
            if (!card.tags.contains(AbstractCard.CardTags.HEALING))
            {
                GameActions.Bottom.MakeCardInHand(card);
            }
        }
        else if (tryActivate(6)) // 132
        {
            for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.DealDamage(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.BLUNT_HEAVY);
            }
        }
        else if (tryActivate(6)) // 138
        {
            for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.DealDamage(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HEAVY);
            }
        }
        else if (tryActivate(6)) // 144
        {
            for (AbstractCreature m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.DealDamage(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffect.POISON);
            }
        }
        else if (tryActivate(6)) // 150
        {
            GameActions.Bottom.GainBlock(1);
            GameActions.Bottom.GainBlock(1);
            GameActions.Bottom.GainBlock(1);
        }
        else if (tryActivate(6)) // 156
        {
            GameActions.Bottom.GainOrbSlots(1);
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
        }
        else if (tryActivate(4)) // 160
        {
            GameActions.Bottom.GainTemporaryHP(5);
        }
        else if (tryActivate(3)) // 163
        {
            AbstractMonster m = GameUtilities.GetRandomEnemy(true);
            if (m != null)
            {
                GameActions.Bottom.ApplyConstricted(p, m, 3);
            }
        }
        else if (tryActivate(3)) // 166
        {
            AbstractMonster m = GameUtilities.GetRandomEnemy(true);
            if (m != null)
            {
                GameActions.Bottom.ApplyBurning(p, m, 3);
            }
        }
        else if (tryActivate(3)) // 169
        {
            GameActions.Bottom.GainPlatedArmor(1);
        }
        else if (tryActivate(3)) // 172
        {
            GameActions.Bottom.Motivate(1);
        }
        else if (tryActivate(3)) // 175
        {
            AbstractMonster m = GameUtilities.GetRandomEnemy(true);
            if (m != null)
            {
                GameActions.Bottom.ApplyPower(p, m, new MarkOfPoisonPower(m, p, 2), 2);
            }
        }
        else if (tryActivate(3)) // 178
        {
            GameActions.Bottom.Draw(3);
        }
        else if (tryActivate(2)) // 180
        {
            GameActions.Bottom.Add(new HigakiRinneAction(higakiRinne));
            GameActions.Bottom.Add(new HigakiRinneAction(higakiRinne));
            GameActions.Bottom.Add(new HigakiRinneAction(higakiRinne));
        }
        else if (tryActivate(5)) // 185
        {
            for (AbstractGameEffect effect : AbstractDungeon.effectList)
            {
                if (effect instanceof ShuffleEnemiesEffect)
                {
                    GameActions.Bottom.StackPower(new EnchantedArmorPower(p, 1));
                    Complete();
                    return;
                }
            }

            for (AbstractGameEffect effect : AbstractDungeon.effectsQueue)
            {
                if (effect instanceof ShuffleEnemiesEffect)
                {
                    GameActions.Bottom.StackPower(new EnchantedArmorPower(p, 1));
                    Complete();
                    return;
                }
            }

            GameEffects.Queue.Add(new ShuffleEnemiesEffect());
        }
    }

    public static void PlayRandomSound()
    {
        GameActions.Bottom.SFX(JavaUtilities.GetRandomElement(sounds));
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
