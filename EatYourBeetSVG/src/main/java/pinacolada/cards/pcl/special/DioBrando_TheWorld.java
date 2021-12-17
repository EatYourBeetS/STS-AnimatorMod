package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashSet;
import java.util.UUID;

public class DioBrando_TheWorld extends PCLCard
{
    public static final PCLCardData DATA = Register(DioBrando_TheWorld.class).SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Jojo);
    public static final int COST = 3;
    protected static HashSet<UUID> buffs;

    public DioBrando_TheWorld()
    {
        super(DATA);

        Initialize(0, 0, 2, COST);
        SetUpgrade(0,0,1,0);
        SetAffinity_Star(1);
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainStrength(magicNumber);
        PCLActions.Bottom.StackPower(new TheWorldPower(p, COST));
    }

    public static class TheWorldPower extends PCLClickablePower
    {
        public TheWorldPower(AbstractPlayer owner, int amount)
        {
            super(owner, DioBrando_TheWorld.DATA, PowerTriggerConditionType.TakeDelayedDamage, amount, null, null, PCLAffinity.Star);
            buffs = CombatStats.GetCombatData(DioBrando_TheWorld.DATA.ID, null);
            if (buffs == null)
            {
                buffs = new HashSet<>();
                CombatStats.SetCombatData(DioBrando_TheWorld.DATA.ID, buffs);
            }

            this.triggerCondition.SetCheckCondition(__ -> PCLJUtils.Find(player.hand.group, c -> c instanceof PCLCard && !buffs.contains(c.uuid)) != null);
            this.triggerCondition.SetOneUsePerPower(true);
            Initialize(amount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            if (buffs != null) {
                PCLActions.Bottom.SelectFromHand(name, 1, false).SetFilter(c -> c instanceof PCLCard && !buffs.contains(c.uuid))
                        .AddCallback(cards -> {
                   for (AbstractCard c : cards) {
                       PCLGameUtilities.ModifyHitCount((PCLCard) c, ((PCLCard) c).baseHitCount * 2, false);
                       PCLGameUtilities.ModifyDamage(c, Math.max(1, c.baseDamage / 2), false);
                   }
                });
            }
        }
    }
}