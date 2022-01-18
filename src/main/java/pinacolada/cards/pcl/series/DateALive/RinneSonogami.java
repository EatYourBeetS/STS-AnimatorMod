package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.stances.InvocationStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class RinneSonogami extends PCLCard
{
    public static final PCLCardData DATA = Register(RinneSonogami.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public RinneSonogami()
    {
        super(DATA);

        Initialize(0, 9, 4, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0, 2);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.SelectFromHand(name, secondaryValue, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> c.rarity == CardRarity.BASIC)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        PCLGameUtilities.Retain(card);
                        PCLCard aC = PCLJUtils.SafeCast(card, PCLCard.class);
                        PCLAffinity next = PCLCombatStats.MatchingSystem.AffinityMeter.GetNextAffinity();
                        if (aC != null && PCLGameUtilities.GetPCLAffinityLevel(aC, PCLCombatStats.MatchingSystem.AffinityMeter.GetNextAffinity(), true) < 2)
                        {
                            aC.affinities.Add(next, 1);
                            aC.flash();
                        }
                        if ((InvocationStance.IsActive() || info.IsSynergizing) && CombatStats.TryActivateSemiLimited(cardID)) {
                            if (card.baseDamage > 0) {
                                PCLGameUtilities.IncreaseDamage(card, magicNumber, false);
                            }
                            if (card.baseBlock > 0) {
                                PCLGameUtilities.IncreaseBlock(card, magicNumber, false);
                            }
                        }
                    }
                });
    }
}