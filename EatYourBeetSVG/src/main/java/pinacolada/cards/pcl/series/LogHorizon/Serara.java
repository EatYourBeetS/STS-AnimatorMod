package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.UUID;

public class Serara extends PCLCard
{
    public static final PCLCardData DATA = Register(Serara.class)
            .SetSkill(0, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None, true)
            .SetSeries(CardSeries.LogHorizon);
    private static HashMap<UUID, Integer> buffs;

    public Serara()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetMessage(GR.PCL.Strings.HandSelection.MoveToDrawPile)
                .AddCallback(selected ->
                {
                    for (AbstractCard c : selected)
                    {
                        PCLActions.Top.MoveCard(c, player.hand, player.drawPile).SetDestination(CardSelection.Top);
                    }
                });
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

        PCLActions.Bottom.SelectFromHand(name, 1, !upgraded)
        .SetOptions(false, false, false)
        .SetMessage(GR.PCL.Strings.HandSelection.GenericBuff)
        .SetFilter(c -> c instanceof PCLCard && !PCLGameUtilities.IsHindrance(c) && buffs.getOrDefault(c.uuid, 0) < secondaryValue && (c.baseDamage >= 0 || c.baseBlock >= 0))
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.Orange, secondaryValue);
                PCLJUtils.IncrementMapElement(buffs, c.uuid, secondaryValue);
                c.flash();
            }
        });
    }
}