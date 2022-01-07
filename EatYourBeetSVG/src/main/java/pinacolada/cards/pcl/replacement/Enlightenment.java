package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;

public class Enlightenment extends PCLCard
{
    public static final PCLCardData DATA = Register(Enlightenment.class)
            .SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public Enlightenment()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ModifyCost(player.hand, player.hand.size(), 1, upgraded, false)
                .SetFilter(c -> c.costForTurn > 1)
                .AddCallback(c -> {
                    if (c.size() == 0) {
                        PCLActions.Bottom.GainInspiration(magicNumber);
                    }
                });
    }
}