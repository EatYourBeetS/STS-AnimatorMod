package pinacolada.cards.pcl.series.Konosuba;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
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
            .SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None, true)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public Iris()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0,0,1, 1);

        SetAffinity_Light(1, 0, 1);

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

        PCLActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetFilter(c -> c.type == CardType.ATTACK)
        .SetOptions(false, false, false)
        .AddCallback((cards) ->
        { //
            for (AbstractCard c : cards) {
                PCLActions.Bottom.MakeCardInHand(PCLGameUtilities.Imitate(c));
                PCLActions.Bottom.MakeCardInHand(PCLGameUtilities.Imitate(c));
                PCLActions.Last.Callback(() -> {
                    int amount = Math.min(secondaryValue,c.costForTurn + 1);
                    PCLActions.Bottom.IncreaseScaling(p.hand, BaseMod.MAX_HAND_SIZE, PCLAffinity.Light, amount)
                            .SetFilter(ca ->  buffs.getOrDefault(c.uuid, 0) < magicNumber)
                            .AddCallback(cards2 ->
                            {
                                for (AbstractCard c2 : cards2)
                                {
                                    PCLJUtils.IncrementMapElement(buffs, c2.uuid, amount);
                                }
                            });
                });
            }
        });
    }
}