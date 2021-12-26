package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class KanadeTachibana extends PCLCard
{
    public static final PCLCardData DATA = Register(KanadeTachibana.class).SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage();
    public static final int MATCH_COMBO = 3;

    public KanadeTachibana()
    {
        super(DATA);

        Initialize(0, 2, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAfterlife(true);
        SetPurge(true);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(2, 0, 2);
        SetAffinity_Green(0,0,1);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(MATCH_COMBO);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainInvocation(secondaryValue);
        PCLActions.Bottom.FetchFromPile(name, magicNumber, p.discardPile)
        .SetOptions(false, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0]).AddCallback(
                cards -> {
                    boolean canGiveAfterlife = PCLGameUtilities.GetCurrentMatchCombo() >= MATCH_COMBO && CombatStats.TryActivateLimited(cardID);

                    for (AbstractCard c : cards) {
                        PCLCard card = PCLJUtils.SafeCast(c, PCLCard.class);
                        if (card != null && PCLGameUtilities.GetPCLAffinityLevel(card, PCLAffinity.Light, true) < 2)
                        {
                            card.affinities.Add(PCLAffinity.Light, 1);
                            card.flash();

                            if (canGiveAfterlife) {
                                card.SetAfterlife(true);
                            }
                        }
                    }
                }
        );
    }
}