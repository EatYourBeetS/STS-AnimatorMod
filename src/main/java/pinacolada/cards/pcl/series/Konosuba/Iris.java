package pinacolada.cards.pcl.series.Konosuba;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.UUID;

public class Iris extends PCLCard
{
    public static final PCLCardData DATA = Register(Iris.class)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.None, true)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public Iris()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0,0,1, 1);

        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 7);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        buffs = CombatStats.GetCombatData(cardID, null);
        if (buffs == null)
        {
            buffs = new HashMap<>();
            CombatStats.SetCombatData(cardID, buffs);
        }

        DoAction();

        PCLActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(true, true, true)
        .AddCallback((cards) ->
        { //
            if (cards.size() > 0) {
                DoAction();
            }
            if (TrySpendAffinity(PCLAffinity.Light)) {
                PCLActions.Last.Callback(() -> {

                    PCLActions.Bottom.SelectFromHand(name, BaseMod.MAX_HAND_SIZE, true)
                            .SetOptions(true, true, true)
                            .SetFilter(ca -> buffs.getOrDefault(ca.uuid, 0) < secondaryValue)
                            .AddCallback(cards2 ->
                            {
                                for (AbstractCard c2 : cards2) {
                                    int amount = c2.costForTurn + 1;
                                    if (amount + buffs.getOrDefault(c2.uuid, 0) > secondaryValue) {
                                        amount = secondaryValue - buffs.getOrDefault(c2.uuid, 0);
                                    }
                                    PCLActions.Bottom.IncreaseScaling(c2, PCLAffinity.Light, c2.costForTurn);
                                    PCLJUtils.IncrementMapElement(buffs, c2.uuid, amount);
                                }
                            });
                });
            }
        });
    }

    protected void DoAction() {
        PCLActions.Bottom.SelectFromHand(name, 1, false).SetFilter(c -> c instanceof PCLCard)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        PCLGameUtilities.ModifyDamage(c, c.baseDamage + secondaryValue, false);
                    }
                });
    }
}