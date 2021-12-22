package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class HatateHimekaidou extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(HatateHimekaidou.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);
    protected final ArrayList<AbstractCard> imitations = new ArrayList<>();

    public HatateHimekaidou()
    {
        super(DATA);

        Initialize(0, 2, 2, 0);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Green(1, 0 ,0);
        SetAffinity_Light(1, 0 ,0);
        SetAffinity_Star(0, 0 ,1);
        SetHaste(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
                .SetOptions(true, true, true)
                .AddCallback(cards -> {
                    HatateHimekaidou other = (HatateHimekaidou) makeStatEquivalentCopy();
                    PCLCombatStats.onStartOfTurnPostDraw.Subscribe(other);
                    for (AbstractCard c : cards) {
                        if (upgraded) {
                            PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.Light, 1);
                        }
                        PCLActions.Bottom.ModifyTag(c, HASTE, true);
                        PCLActions.Bottom.SelectFromPile(name, 1, player.discardPile)
                                .SetFilter(c2 -> c2.costForTurn == c.costForTurn)
                                .SetOptions(false, true)
                                .AddCallback(cards2 -> {
                                    if (cards2.size() > 0) {
                                        for (AbstractCard c2 : cards2) {
                                            other.imitations.add(PCLGameUtilities.Imitate(c2));
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        super.OnStartOfTurnPostDraw();
        if (imitations.size() > 0) {
            PCLGameEffects.Queue.ShowCardBriefly(this);
            for (AbstractCard c : imitations) {
                PCLActions.Bottom.MakeCardInHand(c);
            }
        }
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}

