package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import eatyourbeets.actions.animator.EndPlayerTurnAction;
import eatyourbeets.actions.animator.KillCharacterAction;
import eatyourbeets.blights.CustomTimeMaze;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.interfaces.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.actions.common.WaitRealtimeAction;
import eatyourbeets.cards.animator.*;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.animator.AnimatorPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnApplyPowerSubscriber;
import eatyourbeets.interfaces.OnBattleStartSubscriber;

import java.util.ArrayList;

public class InfinitePower extends AnimatorPower implements OnBattleStartSubscriber, OnApplyPowerSubscriber, OnStartOfTurnPostDrawSubscriber
{
    public static final String POWER_ID = CreateFullID(InfinitePower.class.getSimpleName());

    private final ArrayList<Integer> linesUsed = new ArrayList<>();
    private final String[] dialog;
    private final EnchantedArmorPower enchantedArmorPower;
    private final CustomTimeMaze timeMaze;
    private final int maxCardsPerTurn;

    private boolean necronomicursed = false;
    private boolean progressStunCounter = true;
    private int stunCounter = 0;

    public InfinitePower(TheUnnamed owner)
    {
        super(owner, POWER_ID);

        this.maxCardsPerTurn = 16;
        this.timeMaze = new CustomTimeMaze(maxCardsPerTurn);
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
        GameActionsHelper.ApplyPowerSilently(owner, owner, this, 0);
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

        boolean found = false;
        for (AbstractPower p : owner.powers)
        {
            if (p == enchantedArmorPower)
            {
                found = true;
            }
        }

        if (!found)
        {
            GameActionsHelper.ApplyPowerSilently(owner, owner, enchantedArmorPower, enchantedArmorPower.amount);
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
            int stacks = Math.max(1, Math.abs(power.amount));
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
        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();

        if (cardsPlayed < (maxCardsPerTurn / 2))
        {
            CardMessage(card, action);
        }

        if (cardsPlayed == (maxCardsPerTurn - 2))
        {
            if (!timeMaze.isObtained)
            {
                GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[3], 4, 4));
                AbstractDungeon.effectsQueue.add(new PowerIconShowEffect(this));

                timeMaze.counter = cardsPlayed;
                AbstractDungeon.getCurrRoom().spawnBlightAndObtain(owner.hb.cX, owner.hb.cY, timeMaze);
            }
            else
            {
                GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[22], 4, 4));
            }
        }
        else
        {
            if (!necronomicursed)
            {
                AbstractPlayer p = AbstractDungeon.player;

                int totalSize = (p.drawPile.size() + p.discardPile.size() + p.hand.size());
                if (totalSize < 10 && PlayerStatistics.getCardsDrawnThisTurn() > (totalSize + 2))
                {
                    GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[31], 2f, 2f));
                    GameActionsHelper.AddToBottom(new WaitRealtimeAction(2.5f));
                    GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[32], 2f, 2f));
                    GameActionsHelper.AddToBottom(new WaitRealtimeAction(2.5f));
                    GameActionsHelper.SFX("NECRONOMICON");
                    GameActionsHelper.MakeCardInDrawPile(new Necronomicurse(), 4, false);

                    AnimatorCard_UltraRare.MarkAsSeen(Cthulhu.ID);
                    AbstractDungeon.player.discardPile.addToTop(new Cthulhu());
                    necronomicursed = true;
                }
            }
        }
    }

    private void CardMessage(AbstractCard card, UseCardAction action)
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
//        else if (card instanceof Saitama && owner.equals(action.target) && owner.currentHealth > 800)
//        {
//            Talk(15, 3);
//        }
        else if (card instanceof Eve)
        {
            Talk(16,2.5f);
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
        else if (card instanceof Togame)
        {
            Talk(29, 2.2f);
        }
        else if (card instanceof HigakiRinne)
        {
            if (!linesUsed.contains(21) && owner.currentHealth > 500 && !((TheUnnamed)owner).phase2)
            {
                GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[21], 1.2f, 1.2f));
                GameActionsHelper.Callback(new WaitRealtimeAction(1.2f), this::Rinne, card);
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
            Random rng = AbstractDungeon.cardRandomRng;
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
        if (!linesUsed.contains(line) && owner.currentHealth > 500 && !((TheUnnamed)owner).phase2)
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[line], duration, duration));

            linesUsed.add(line);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        progressStunCounter = true;
    }

    public void onSleepOrStun()
    {
        if (progressStunCounter)
        {
            progressStunCounter = false;
        }
        else
        {
            return;
        }

        if (stunCounter <= 0)
        {
            GameActionsHelper.AddToTop(new TalkAction(owner, dialog[23], 4, 4));
            stunCounter = 1;
        }
        else if (stunCounter == 1)
        {
            GameActionsHelper.AddToTop(new TalkAction(owner, dialog[24], 4, 4));
            stunCounter = 2;
        }
        else if (stunCounter == 2)
        {
            GameActionsHelper.AddToTop(new EndPlayerTurnAction());
            GameActionsHelper.AddToTop(new TalkAction(owner, dialog[25], 4, 4));

            stunCounter = 3;
        }
        else if (stunCounter == 3)
        {
            GameActionsHelper.AddToTop(new EndPlayerTurnAction());
            GameActionsHelper.AddToTop(new TalkAction(owner, dialog[26], 3, 3));

            stunCounter = 4;
        }
        else
        {
            GameActionsHelper.AddToTop(new KillCharacterAction(owner, AbstractDungeon.player));
            GameActionsHelper.AddToTop(new TalkAction(owner, dialog[27], 3, 3));
        }
    }
}