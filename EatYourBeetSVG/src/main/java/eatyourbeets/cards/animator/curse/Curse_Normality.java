package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

import java.util.HashMap;
import java.util.UUID;

public class Curse_Normality extends AnimatorCard_Curse implements OnTryApplyPowerListener, OnStartOfTurnSubscriber, OnPurgeSubscriber
{
    protected static final HashMap<AbstractCreature, HashMap<String, Integer>> POWERS = new HashMap<>();
    protected static UUID battleID;

    public static final EYBCardData DATA = Register(Curse_Normality.class)
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
        CombatStats.onStartOfTurn.Subscribe(this);
        CombatStats.onPurge.Subscribe(this);
    }

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription(GameUtilities.InBattle() ? DATA.Strings.EXTENDED_DESCRIPTION[0] : "");
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

        for (AbstractCreature c : GameUtilities.GetAllCharacters(true)) {
            if (c.powers != null) {
                for (AbstractPower po : c.powers) {
                    if ((GameUtilities.IsCommonBuff(po) || (GameUtilities.IsCommonDebuff(po) && GameUtilities.IsPlayer(c)))) {
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
        TryRestorePowers();
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();
        TryRestorePowers();
    }

    @Override
    public void OnStartOfTurn()
    {
        super.OnStartOfTurn();
        TryRestorePowers();
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        TryRestorePowers();
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
        if (player.hand.contains(this) && (GameUtilities.IsCommonBuff(power) || (GameUtilities.IsCommonDebuff(power) && GameUtilities.IsPlayer(target)))) {
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
        GameActions.Bottom.RemovePower(owner, power);
    }

    protected void TryRestorePowers() {
        CheckForNewBattle();
        if (JUtils.Find(player.hand.group, c -> c.cardID.equals(Curse_Normality.DATA.ID)) == null) {
            for (AbstractCreature owner : POWERS.keySet()) {
                if (owner != null) {
                    HashMap<String, Integer> targetSet = POWERS.getOrDefault(owner, new HashMap<>());
                    for (String powerID : targetSet.keySet()) {
                        int amount = targetSet.getOrDefault(powerID, 0);
                        PowerHelper ph = PowerHelper.ALL.get(powerID);
                        if (ph != null && amount != 0) {
                            GameActions.Bottom.ApplyPower(TargetHelper.Normal(owner), ph, amount);
                        }
                    }

                }
            }
            POWERS.clear();
        }
    }
}