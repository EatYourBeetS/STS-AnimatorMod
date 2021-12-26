package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.UUID;

public class Curse_Normality extends PCLCard_Curse implements OnTryApplyPowerListener, OnStartOfTurnSubscriber, OnPurgeSubscriber
{
    protected static final HashMap<AbstractCreature, HashMap<String, Integer>> POWERS = new HashMap<>();
    protected static UUID battleID;
    public static final PCLCardData DATA = Register(Curse_Normality.class)
            .SetCurse(-2, EYBCardTarget.None, false);

    protected static void CheckForNewBattle() {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            POWERS.clear();
        }
    }

    public Curse_Normality()
    {
        super(DATA, true);

        Initialize(0, 0, 3, 50);

        SetUnplayable(true);
    }

    public void OnUpgrade() {
        SetRetain(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onStartOfTurn.Subscribe(this);
        PCLCombatStats.onPurge.Subscribe(this);
    }

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription(PCLGameUtilities.InBattle() ? DATA.Strings.EXTENDED_DESCRIPTION[0] : "");
    }

    @Override
    public int GetXValue() {
        return AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
    }

    public boolean canPlay(AbstractCard card) {
        if (GetXValue() >= 3) {
            card.cantUseMessage = DATA.Strings.EXTENDED_DESCRIPTION[1];
            return false;
        } else {
            return true;
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (AbstractCreature c : PCLGameUtilities.GetAllCharacters(true)) {
            if (c.powers != null) {
                for (AbstractPower po : c.powers) {
                    if ((PCLGameUtilities.IsCommonBuff(po) || (PCLGameUtilities.IsCommonDebuff(po) && PCLGameUtilities.IsPlayer(c)))) {
                        NegatePower(po, c);
                    }
                }
            }
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        TryRestorePowers(false);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();
        TryRestorePowers(false);
    }

    @Override
    public void OnStartOfTurn()
    {
        super.OnStartOfTurn();
        TryRestorePowers(true);
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        TryRestorePowers(false);
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
        if (player.hand.contains(this) && (PCLGameUtilities.IsCommonBuff(power) || (PCLGameUtilities.IsCommonDebuff(power) && PCLGameUtilities.IsPlayer(target)))) {
            NegatePower(power, target);
            return false;
        }
        return true;
    }

    protected void NegatePower(AbstractPower power, AbstractCreature owner) {
        CheckForNewBattle();
        HashMap<String, Integer> targetSet = POWERS.getOrDefault(owner, new HashMap<>());
        targetSet.merge(power.ID, power.amount, Integer::sum);
        POWERS.put(owner, targetSet);
        PCLActions.Bottom.RemovePower(owner, power);
    }

    protected void TryRestorePowers(boolean shouldProgress) {
        CheckForNewBattle();
        if (PCLJUtils.Find(player.hand.group, c -> c.cardID.equals(Curse_Normality.DATA.ID)) == null) {
            for (AbstractCreature owner : POWERS.keySet()) {
                if (owner != null) {
                    HashMap<String, Integer> targetSet = POWERS.getOrDefault(owner, new HashMap<>());
                    for (String powerID : targetSet.keySet()) {
                        int amount = targetSet.getOrDefault(powerID, 0);
                        PCLPowerHelper ph = PCLPowerHelper.ALL.get(powerID);

                        if (shouldProgress) {
                            if (ph.EndTurnBehavior == PCLPowerHelper.Behavior.TurnBased) {
                                amount -= 1;
                            }
                            else if (ph.EndTurnBehavior == PCLPowerHelper.Behavior.SingleTurn || ph.EndTurnBehavior == PCLPowerHelper.Behavior.Temporary) {
                                amount = 0;
                            }
                        }

                        if (ph != null && amount != 0) {
                            PCLActions.Bottom.ApplyPower(TargetHelper.Normal(owner), ph, amount);
                        }
                    }

                }
            }
            POWERS.clear();
        }
    }
}