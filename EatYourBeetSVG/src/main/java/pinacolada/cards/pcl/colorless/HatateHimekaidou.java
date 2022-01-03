package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class HatateHimekaidou extends PCLCard
{
    public static final PCLCardData DATA = Register(HatateHimekaidou.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);
    protected final ArrayList<AbstractCard> imitations = new ArrayList<>();

    public HatateHimekaidou()
    {
        super(DATA);

        Initialize(0, 1, 2, 0);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Green(1, 0 ,0);
        SetAffinity_Light(1, 0 ,0);
        SetAffinity_Star(0, 0 ,2);
        SetHaste(true);
        SetEthereal(true);
        SetExhaust(true);
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

        PCLActions.Bottom.DiscardFromHand(name, player.hand.size(), true)
                .SetOptions(true, true, true)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        if (upgraded) {
                            PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.Light, 1);
                        }
                        PCLActions.Bottom.ModifyTag(c, HASTE, true);
                    }
                    if (cards.size() > 0) {
                        PCLActions.Bottom.Draw(cards.size());
                    }
                });
    }
}

