package eatyourbeets.powers.UnnamedReign;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.HigakiRinneAction;
import eatyourbeets.actions.WaitRealtimeAction;
import eatyourbeets.cards.animator.*;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.EnchantedArmorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.subscribers.OnBattleStartSubscriber;

import java.util.ArrayList;

public class InfinitePower extends AnimatorPower implements OnBattleStartSubscriber, OnApplyPowerSubscriber
{
    public static final String POWER_ID = CreateFullID(InfinitePower.class.getSimpleName());

    private ArrayList<Integer> linesUsed = new ArrayList<>();
    private String[] dialog = null;
    private final EnchantedArmorPower enchantedArmorPower;

    public InfinitePower(TheUnnamed owner)
    {
        super(owner, POWER_ID);

        this.enchantedArmorPower = new EnchantedArmorPower(owner, 0, true);

        dialog = owner.data.strings.DIALOG;

        this.priority = 100;
        this.amount = -1;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        OnBattleStart();

        GameActionsHelper.ApplyPowerSilently(owner, owner, enchantedArmorPower, 0);
    }

    @Override
    public void onRemove()
    {
        GameActionsHelper.ApplyPowerSilently(owner, owner, this, 1);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer != owner.isPlayer)
        {
            return;
        }

        if (enchantedArmorPower.amount > 0)
        {
            enchantedArmorPower.amount = Math.max(1, enchantedArmorPower.amount / 2);
            enchantedArmorPower.updateDescription();
        }

        AbstractPower strengthPower = owner.getPower(StrengthPower.POWER_ID);
        if (strengthPower != null && strengthPower.amount > 0)
        {
            strengthPower.amount = Math.max(1, strengthPower.amount / 2);
            strengthPower.updateDescription();
        }

        AbstractPower regenPower = owner.getPower(RegenPower.POWER_ID);
        if (regenPower != null && regenPower.amount > 0)
        {
            regenPower.amount = Math.max(1, regenPower.amount / 2);
            regenPower.updateDescription();
        }
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (source != null && target != null)
        {
            int stacks = Math.abs(power.amount);
            if (source != owner && target == owner)
            {
                if (owner.isPlayer != source.isPlayer && power.type == PowerType.DEBUFF)
                {
                    GameActionsHelper.ApplyPower(owner, owner, new RegenPower(owner, stacks), stacks);
                }
            }
            else if (source != owner && target == source)
            {
                if (owner.isPlayer != source.isPlayer && power.type == PowerType.BUFF)
                {
                    GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, stacks), stacks);
                }
            }
        }

        super.onApplyPower(power, target, source);
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onBattleStart.Subscribe(this);
        PlayerStatistics.onApplyPower.Subscribe(this);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        if (cardsPlayed == 13)
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[3], 4, 4));
            AbstractDungeon.effectsQueue.add(new PowerIconShowEffect(this));
        }
        else if (cardsPlayed == 16)
        {
            this.playApplyPowerSfx();
            AbstractDungeon.actionManager.cardQueue.clear();

            for (AbstractCard c : AbstractDungeon.player.limbo.group)
            {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
            }

            AbstractDungeon.player.limbo.group.clear();
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
            AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
        }
        else if (cardsPlayed < 10)
        {
            CardMessage(card);
        }
    }

    private void CardMessage(AbstractCard card)
    {
        if (card instanceof Gilgamesh)
        {
            Talk(11, 2);
        }
        else if (card instanceof Megumin)
        {
            Talk(12, 2);
        }
        else if (card instanceof Ainz)
        {
            Talk(13, 3);
        }
        else if (card instanceof Boros)
        {
            Talk(14, 2);
        }
        else if (card instanceof Saitama)
        {
            Talk(15, 3);
        }
        else if (card instanceof Eve)
        {
            Talk(16,2);
        }
        else if (card instanceof Guren)
        {
            Talk(17, 2.5f);
        }
        else if (card instanceof Nanami)
        {
            Talk(18, 3);
        }
        else if (card instanceof GoblinSlayer)
        {
            Talk(19, 3f);
        }
        else if (card instanceof ChlammyZell)
        {
            Talk(20, 3f);
        }
        else if (card instanceof HigakiRinne)
        {
            if (!linesUsed.contains(21) && owner.currentHealth > 500 && ((TheUnnamed)owner).minionsCount > 0)
            {
                GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[21], 2.5f, 2.5f));
                GameActionsHelper.Callback(new WaitRealtimeAction(2.5f), this::Rinne, card);
                linesUsed.add(21);
            }
        }
    }

    private void Rinne(Object state, AbstractGameAction action)
    {
        if (state != null && action != null)
        {
            AbstractPlayer p = AbstractDungeon.player;
            HigakiRinne rinne = (HigakiRinne) state;
            Random rng = AbstractDungeon.miscRng;
            RandomizedList<AbstractCreature> characters = new RandomizedList<>(PlayerStatistics.GetAllCharacters(true));
            RandomizedList<AbstractMonster> enemies = new RandomizedList<>(PlayerStatistics.GetCurrentEnemies(true));

            HigakiRinneAction.PlayRandomSound();

            for (int i = 0; i < 6 ; i++)
            {
                GameActionsHelper.DamageTarget(owner, enemies.Retrieve(rng, false), 1,
                        DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY, true);
            }

            HigakiRinneAction.PlayRandomSound();

            for (int i = 0; i < 6 ; i++)
            {
                GameActionsHelper.GainBlock(characters.Retrieve(rng, false), 1);
            }

            HigakiRinneAction.PlayRandomSound();

            for (int i = 0; i < 6 ; i++)
            {
                GameActionsHelper.DamageTarget(owner, enemies.Retrieve(rng, false), 1,
                        DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON, true);
            }

            HigakiRinneAction.PlayRandomSound();
        }
    }

    private void Talk(int line, float duration)
    {
        if (!linesUsed.contains(line) && owner.currentHealth > 500 && ((TheUnnamed)owner).minionsCount > 0)
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[line], duration, duration));

            linesUsed.add(line);
        }
    }
}