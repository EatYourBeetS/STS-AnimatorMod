package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashSet;
import java.util.UUID;

public class DioBrando_TheWorld extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DioBrando_TheWorld.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.Jojo);
    public static final int COST = 3;
    protected static HashSet<UUID> buffs;

    public DioBrando_TheWorld()
    {
        super(DATA);

        Initialize(0, 0, 2, COST);
        SetUpgrade(0,0,1,0);
        SetAffinity_Star(2);
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainStrength(magicNumber);
        GameActions.Bottom.StackPower(new TheWorldPower(p, COST));
    }

    public static class TheWorldPower extends AnimatorClickablePower
    {
        public TheWorldPower(AbstractPlayer owner, int amount)
        {
            super(owner, DioBrando_TheWorld.DATA, PowerTriggerConditionType.TakeDelayedDamage, amount, null, null, Affinity.Star);
            buffs = CombatStats.GetCombatData(DioBrando_TheWorld.DATA.ID, null);
            if (buffs == null)
            {
                buffs = new HashSet<>();
                CombatStats.SetCombatData(DioBrando_TheWorld.DATA.ID, buffs);
            }

            this.triggerCondition.SetCheckCondition(__ -> JUtils.Find(player.hand.group, c -> c instanceof EYBCard && !buffs.contains(c.uuid)) != null);
            this.triggerCondition.SetOneUsePerPower(true);
            Initialize(amount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            if (buffs != null) {
                GameActions.Bottom.SelectFromHand(name, 1, false).SetFilter(c -> c instanceof EYBCard && !buffs.contains(c.uuid))
                        .AddCallback(cards -> {
                   for (AbstractCard c : cards) {
                       GameUtilities.ModifyHitCount((EYBCard) c, ((EYBCard) c).baseHitCount * 2, false);
                       GameUtilities.ModifyDamage(c, Math.max(1, c.baseDamage / 2), false);
                   }
                });
            }
        }
    }
}