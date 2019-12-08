package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import eatyourbeets.actions._legacy.animator.EndPlayerTurnAction;
import eatyourbeets.actions._legacy.animator.KillCharacterAction;
import eatyourbeets.blights.animator.Doomed;
import eatyourbeets.blights.common.CustomTimeMaze;
import eatyourbeets.interfaces.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.animator.*;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnApplyPowerSubscriber;
import eatyourbeets.interfaces.OnBattleStartSubscriber;

import java.util.ArrayList;

public class InfinitePower extends AnimatorPower implements OnBattleStartSubscriber, OnApplyPowerSubscriber, OnStartOfTurnPostDrawSubscriber
{
    public static final String POWER_ID = CreateFullID(InfinitePower.class.getSimpleName());

    public boolean phase2 = false;

    private final static int MAX_DAMAGE_AT_ONCE = 199;

    private final ArrayList<Integer> linesUsed = new ArrayList<>();
    private final String[] dialog;
    private final EnchantedArmorPower enchantedArmorPower;
    private final CustomTimeMaze timeMaze;
    private final int maxCardsPerTurn;

    //private boolean necronomicursed = false; Solved by not allowing deck shuffling more than twice per turn

    private boolean progressStunCounter = true;
    private int stunCounter = 0;
    private int playerIntangibleCounter = 0;
    private int maxStrengthThisTurn = 25;
    private boolean gainedIntangible = false;

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

        AbstractBlight doomed = AbstractDungeon.player.getBlight(Doomed.ID);
        if (doomed != null && doomed.counter <= 2 && !linesUsed.contains(34))
        {
            linesUsed.add(34);
            GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[34], 2f, 2f));
            GameActionsHelper.AddToBottom(new WaitRealtimeAction(1f));
        }

        if (enchantedArmorPower.amount > 0)
        {
            enchantedArmorPower.amount = Math.max(1, enchantedArmorPower.amount / 2);
            enchantedArmorPower.stackPower(0); // Update Description
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
            int stacks = Math.max(0, Math.abs(power.amount));
            if (stacks > 0)
            {
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
                        int amount;
                        if ((power instanceof StrengthPower) || (power instanceof FocusPower) || (power instanceof DexterityPower))
                        {
                            amount = stacks;
                        }
                        else
                        {
                            amount = 1;
                        }

                        if (amount > maxStrengthThisTurn)
                        {
                            amount = maxStrengthThisTurn;
                        }

                        if (amount > 0)
                        {
                            maxStrengthThisTurn -= amount;
                            GameActionsHelper.ApplyPowerSilently(owner, owner, new GainStrengthPower(owner, amount), amount);
                        }
                    }
                }
            }

            if (target.isPlayer && IntangiblePlayerPower.POWER_ID.equals(power.ID))
            {
                if (!phase2)
                {
                    playerIntangibleCounter += power.amount;

                    if (playerIntangibleCounter >= 3)
                    {
                        if (!gainedIntangible)
                        {
                            GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[33], 2f, 2f));
                            GameActionsHelper.AddToBottom(new WaitRealtimeAction(2.5f));
                            gainedIntangible = true;
                        }

                        GameActionsHelper.ApplyPowerToAllEnemies(owner, (c) -> new IntangiblePlayerPower(c, 2), 2);
                        playerIntangibleCounter = 0;
                    }
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
//        else
//        {
//            if (!necronomicursed)
//            {
//                AbstractPlayer p = AbstractDungeon.player;
//
//                int totalSize = (p.drawPile.size() + p.discardPile.size() + p.hand.size());
//                if (totalSize < 10 && PlayerStatistics.getCardsDrawnThisTurn() > (totalSize + 2))
//                {
//                    GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[31], 2f, 2f));
//                    GameActionsHelper.AddToBottom(new WaitRealtimeAction(2.5f));
//                    GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[32], 2f, 2f));
//                    GameActionsHelper.AddToBottom(new WaitRealtimeAction(2.5f));
//                    GameActionsHelper.SFX("NECRONOMICON");
//                    GameActionsHelper.MakeCardInDrawPile(new Necronomicurse(), 4, false);
//
//                    AnimatorCard_UltraRare.MarkAsSeen(Cthulhu.ID);
//                    AbstractDungeon.player.discardPile.addToTop(new Cthulhu());
//                    necronomicursed = true;
//                }
//            }
//        }
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageFinalReceive(Math.min(MAX_DAMAGE_AT_ONCE, damage), type);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        return super.onAttacked(info, Math.min(MAX_DAMAGE_AT_ONCE, damageAmount));
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
            Talk(21, 2.5f);
        }
    }

    private void Talk(int line, float duration)
    {
        if (!linesUsed.contains(line) && owner.currentHealth > 500 && !phase2)
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, dialog[line], duration, duration));

            linesUsed.add(line);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        progressStunCounter = true;
        maxStrengthThisTurn = 20;
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