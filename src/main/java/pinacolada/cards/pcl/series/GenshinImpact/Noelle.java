package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.orbs.pcl.Earth;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Noelle extends PCLCard
{
    public static final PCLCardData DATA = Register(Noelle.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.Self).SetSeriesFromClassPackage();

    public Noelle()
    {
        super(DATA);

        Initialize(0, 5, 1);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Red, 3);
        SetAffinityRequirement(PCLAffinity.Orange, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            PCLActions.Bottom.TriggerOrbPassive(1)
                    .SetFilter(o -> Earth.ORB_ID.equals(o.ID))
                    .AddCallback(orbs ->
                    {
                        if (orbs.size() <= 0)
                        {
                            PCLActions.Bottom.ChannelOrb(new Earth());
                        }
                    });
        }

        if (player.hand.size() > 0) {
            PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Red, PCLAffinity.Orange)
                    .CancellableFromPlayer(true)
                    .AddConditionalCallback(() ->
            {
                PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
                        .SetOptions(true, true, true)
                        .SetMessage(RetainCardsAction.TEXT[0])
                        .AddCallback(cards ->
                        {
                            if (cards.size() > 0)
                            {
                                AbstractCard card = cards.get(0);
                                PCLGameUtilities.Retain(card);
                            }
                        });
            });
        }
    }
}