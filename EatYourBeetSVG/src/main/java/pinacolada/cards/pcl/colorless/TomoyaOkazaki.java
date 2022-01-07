package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class TomoyaOkazaki extends PCLCard
{
    public static final PCLCardData DATA = Register(TomoyaOkazaki.class).SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Clannad);

    private int turns;

    public TomoyaOkazaki()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Orange(1, 0, 0);
        SetProtagonist(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        ArrayList<PCLAffinity> maxes = PCLGameUtilities.GetHighestAffinities();
        PCLActions.Bottom.SelectFromPile(name, magicNumber, player.discardPile, player.drawPile)
                .SetOptions(true, true)
                .SetFilter(c -> c instanceof PCLCard && PCLJUtils.Any(maxes, af -> ((PCLCard) c).affinities.GetLevel(af) > 0))
                .AddCallback(cards -> {
            for (AbstractCard card : cards)
            {
                PCLActions.Bottom.Motivate(card, 1);
            }
        });
    }

}