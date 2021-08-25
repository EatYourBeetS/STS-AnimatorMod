package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import eatyourbeets.actions.player.EndPlayerTurn;
import eatyourbeets.actions.special.KillCharacterAction;
import eatyourbeets.blights.animator.Doomed;
import eatyourbeets.blights.common.CustomTimeMaze;
import eatyourbeets.cards.animator.series.Elsword.Eve;
import eatyourbeets.cards.animator.series.Fate.Gilgamesh;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.cards.animator.series.Katanagatari.Togame;
import eatyourbeets.cards.animator.series.Konosuba.Megumin;
import eatyourbeets.cards.animator.series.NoGameNoLife.ChlammyZell;
import eatyourbeets.cards.animator.series.OnePunchMan.Boros;
import eatyourbeets.cards.animator.series.Overlord.Ainz;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Guren;
import eatyourbeets.cards.animator.ultrarare.Cthulhu;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class TheUnnamedPower extends AnimatorPower implements OnBattleStartSubscriber, OnApplyPowerSubscriber, OnStartOfTurnPostDrawSubscriber
{
    public static final String POWER_ID = CreateFullID(TheUnnamedPower.class);
    public static final int PHASE2_POWER_ASCENSION = 19;
    public static final int PRIORITY = -2900;

    public boolean phase2 = false;

    private final static int MAX_DAMAGE_AT_ONCE = 199;

    private final ArrayList<Integer> linesUsed = new ArrayList<>();
    private final String[] dialog;
    private final EnchantedArmorPower enchantedArmorPower;
    private final CustomTimeMaze timeMaze;
    private final int maxCardsPerTurn;

    private boolean progressStunCounter = true;
    private int stunCounter = 0;
    private int playerIntangibleCounter = 0;
    private int intangibleThreshold = 2;
    private int maxStrengthThisTurn = 25;

    public TheUnnamedPower(TheUnnamed owner)
    {
        super(owner, POWER_ID);

        this.maxCardsPerTurn = 16;
        this.timeMaze = new CustomTimeMaze(maxCardsPerTurn);
        this.enchantedArmorPower = new EnchantedArmorPower(owner, 0, true);
        this.dialog = owner.data.strings.DIALOG;
        this.priority = PRIORITY;

        Initialize(-1);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        OnBattleStart();

        GameActions.Bottom.ApplyPower(owner, enchantedArmorPower)
        .ShowEffect(false, true);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        GameActions.Bottom.ApplyPower(owner, this)
        .ShowEffect(false, true);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0);
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
        if (doomed != null && doomed.counter <= 1 && !linesUsed.contains(34))
        {
            linesUsed.add(34);
            GameActions.Bottom.Talk(owner, dialog[34], 1.5f, 2f);
        }

        GameActions.Bottom.Callback(() ->
        {
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
                    break;
                }
            }

            if (!found)
            {
                GameActions.Bottom.RemovePower(owner, owner, EnchantedArmorPower.POWER_ID);
                GameActions.Bottom.StackPower(owner, enchantedArmorPower)
                .ShowEffect(false, true);
            }

            final AbstractPower strengthPower = owner.getPower(StrengthPower.POWER_ID);
            if (strengthPower != null && strengthPower.amount > 0)
            {
                strengthPower.amount = Math.max(1, strengthPower.amount / 2);
                strengthPower.updateDescription();
            }

            final AbstractPower regenPower = owner.getPower(RegenPower.POWER_ID);
            if (regenPower != null && regenPower.amount > 0)
            {
                regenPower.amount = Math.max(1, regenPower.amount / 2);
                regenPower.updateDescription();
            }
        });
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
                        GameActions.Bottom.StackPower(new RegenPower(owner, stacks));
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
                            GameActions.Bottom.StackPower(new GainStrengthPower(owner, amount))
                            .ShowEffect(false, true)
                            .IgnoreArtifact(true);
                        }
                    }
                }
            }

            if (target.isPlayer && IntangiblePlayerPower.POWER_ID.equals(power.ID))
            {
                if (!phase2)
                {
                    playerIntangibleCounter += power.amount;

                    if (playerIntangibleCounter >= intangibleThreshold)
                    {
                        if (intangibleThreshold < 4)
                        {
                            GameActions.Bottom.Talk(owner, dialog[33], 2f, 2f);
                        }
                        else
                        {
                            GameActions.Bottom.Talk(owner, dialog[6], 2f, 2f);
                        }

                        intangibleThreshold *= 2;

                        for (AbstractMonster m : GameUtilities.GetEnemies(true))
                        {
                            GameActions.Bottom.StackPower(owner, new IntangiblePlayerPower(m, 2));
                        }

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
        CombatStats.onBattleStart.Subscribe(this);
        CombatStats.onApplyPower.Subscribe(this);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();

        if (cardsPlayed < (maxCardsPerTurn - 3))
        {
            CardMessage(card, action);
        }

        if (cardsPlayed == (maxCardsPerTurn - 2))
        {
            if (!timeMaze.isObtained)
            {
                timeMaze.counter = cardsPlayed;

                GameActions.Bottom.Talk(owner, dialog[3], 2.5f, 4);
                GameEffects.Queue.Add(new PowerIconShowEffect(this));
                GameUtilities.ObtainBlight(owner.hb.cX, owner.hb.cY, timeMaze);
            }
            else
            {
                GameActions.Bottom.Talk(owner, dialog[22], 2, 2);
            }
        }
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
        else if (card instanceof SummoningRitual || card instanceof Cthulhu)
        {
            Talk(35, 2.5f);
        }
        else if (card instanceof AnimatorCard_UltraRare)
        {
            Talk(36, 2.5f);
        }
    }

    private void Talk(int line, float duration)
    {
        if (!linesUsed.contains(line) && owner.currentHealth > 500 && !phase2)
        {
            GameActions.Bottom.Talk(owner, dialog[line], 1f, duration);

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
            GameActions.Top.Talk(owner, dialog[23], 4, 4);
            stunCounter = 1;
        }
        else if (stunCounter == 1)
        {
            GameActions.Top.Talk(owner, dialog[24], 4, 4);
            stunCounter = 2;
        }
        else if (stunCounter == 2)
        {
            GameActions.Top.Add(new EndPlayerTurn());
            GameActions.Top.Talk(owner, dialog[25], 4, 4);

            stunCounter = 3;
        }
        else if (stunCounter == 3)
        {
            GameActions.Top.Add(new EndPlayerTurn());
            GameActions.Top.Talk(owner, dialog[26], 3, 3);

            stunCounter = 4;
        }
        else
        {
            GameActions.Top.Add(new KillCharacterAction(owner, player));
            GameActions.Top.Talk(owner, dialog[27], 3, 3);
        }
    }

    public void BeginPhase2()
    {
        this.phase2 = true;

        if (GameUtilities.GetAscensionLevel() >= PHASE2_POWER_ASCENSION)
        {
            GameActions.Bottom.ApplyPower(owner, new Phase2Power(owner)).ShowEffect(false, true);
        }
    }

    public static class Phase2Power extends AnimatorPower
    {
        private AbstractCard lastCard;
        private boolean reduceDamage;

        public Phase2Power(AbstractCreature owner)
        {
            super(owner, POWER_ID);

            this.ID += "Phase2";
            this.name = FormatDescription(1, PHASE2_POWER_ASCENSION);
            this.priority = PRIORITY + 1;

            Initialize(-1);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(2);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            reduceDamage = false;
            lastCard = null;

            GameActions.Bottom.Callback(() ->
            {
                int debuffs = 0;
                for (AbstractPower p : owner.powers)
                {
                    if (p.type == PowerType.DEBUFF)
                    {
                        GameActions.Bottom.ReducePower(p, 1);
                        debuffs += 1;
                    }
                }

                if (debuffs > 0)
                {
                    flashWithoutSound();
                }
            });
        }

        @Override
        public float atDamageReceive(float damage, DamageInfo.DamageType damageType, AbstractCard card)
        {
            if (damageType == DamageInfo.DamageType.NORMAL && card == lastCard)
            {
                if (reduceDamage)
                {
                    return super.atDamageReceive(damage / 2f, damageType, card);
                }
                else
                {
                    reduceDamage = true;
                }
            }

            return super.atDamageReceive(damage, damageType, card);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            reduceDamage = false;
            lastCard = card;
        }
    }
}