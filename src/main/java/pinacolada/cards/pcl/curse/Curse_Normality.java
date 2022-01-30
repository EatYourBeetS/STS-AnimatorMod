package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.interfaces.subscribers.OnCardMovedSubscriber;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.UUID;

public class Curse_Normality extends PCLCard_Curse implements OnApplyPowerSubscriber, OnStartOfTurnSubscriber, OnPurgeSubscriber, OnCardMovedSubscriber
{
    protected static final HashMap<AbstractCreature, HashMap<String, Integer>> POWERS = new HashMap<>();
    protected static UUID battleID;
    public static final PCLCardData DATA = Register(Curse_Normality.class)
            .SetCurse(-2, PCLCardTarget.None, false);

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
        PCLCombatStats.onApplyPower.Subscribe(this);
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


    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        TryActivateEffect();
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        TryActivateEffect();
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
    public void OnCardMoved(AbstractCard card, CardGroup source, CardGroup destination) {
        TryRestorePowers(false);
    }

    protected void TryActivateEffect() {
        if (GetXValue() >= magicNumber) {
            for (AbstractCreature c : PCLGameUtilities.GetAllCharacters(true)) {
                if (c.powers != null) {
                    for (AbstractPower po : c.powers) {
                        if ((PCLGameUtilities.IsCommonBuff(po) || PCLGameUtilities.IsCommonDebuff(po))) {
                            NegatePower(po, c);
                        }
                    }
                }
            }
        }
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
        if (GetXValue() >= magicNumber && PCLJUtils.Find(player.hand.group, c -> c.cardID.equals(Curse_Normality.DATA.ID)) == null) {
            for (AbstractCreature owner : POWERS.keySet()) {
                if (owner != null) {
                    HashMap<String, Integer> targetSet = POWERS.getOrDefault(owner, new HashMap<>());
                    for (String powerID : targetSet.keySet()) {
                        int amount = targetSet.getOrDefault(powerID, 0);
                        PCLPowerHelper ph = PCLPowerHelper.Get(powerID);

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
                        targetSet.put(powerID, 0);
                    }

                }
            }
            POWERS.clear();
        }
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (GetXValue() >= magicNumber && player.hand.contains(this) && (PCLGameUtilities.IsCommonBuff(power) || PCLGameUtilities.IsCommonDebuff(power))) {
            NegatePower(power, target);
        }
    }
}