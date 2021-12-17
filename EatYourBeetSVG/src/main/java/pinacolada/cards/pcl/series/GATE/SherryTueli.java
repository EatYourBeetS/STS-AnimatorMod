package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class SherryTueli extends PCLCard
{
    public static final PCLCardData DATA = Register(SherryTueli.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public SherryTueli()
    {
        super(DATA);

        Initialize(0, 5, 7);
        SetUpgrade(0, 2, 2);

        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 5);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        CalculateHeal();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.DiscardFromHand(name, 2, false)
        .SetOptions(false, true, false)
        .SetFilter(c -> c.type == CardType.ATTACK && !c.purgeOnUse && !c.hasTag(PURGE))
        .AddCallback(cards ->
        {
            if (cards.size() >= 2)
            {
                if (CalculateHeal() > 0)
                {
                    PCLActions.Bottom.RecoverHP(heal);
                }
                if (PCLJUtils.Find(cards, c -> !PCLGameUtilities.HasOrangeAffinity(c)) != null) {
                    PCLActions.Bottom.ObtainAffinityToken(PCLAffinity.Orange, false);
                }
            }
        });

        if (TrySpendAffinity(PCLAffinity.Light)) {
            PCLActions.Bottom.DrawNextTurn(1);
        }
    }

    protected int CalculateHeal()
    {
        return heal = Math.min(magicNumber, GameActionManager.playerHpLastTurn - player.currentHealth);
    }
}