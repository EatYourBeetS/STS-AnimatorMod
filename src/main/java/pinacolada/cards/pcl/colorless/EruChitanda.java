package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class EruChitanda extends PCLCard
{
    public static final PCLCardData DATA = Register(EruChitanda.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.Self)
            .SetColor(CardColor.COLORLESS)
            .SetMaxCopies(2)
            .SetSeries(CardSeries.Hyouka);

    public EruChitanda()
    {
        super(DATA);

        Initialize(0, 1, 3, 4);
        SetUpgrade(0, 0, 999, 0);
        SetAffinity_Light(1, 0 ,1);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        int lastTurn = CombatStats.TurnCount(true) - 1;
        if (lastTurn >= 0) {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<AbstractCard> pile = new RandomizedList<>(JUtils.Filter(CombatStats.CardsPlayedThisCombat(CombatStats.TurnCount(true) - 1),
                    c -> !(PCLGameUtilities.IsHindrance(c) && (player.discardPile.contains(c) || player.drawPile.contains(c) || player.exhaustPile.contains(c))
                    )));
            while ((upgraded || group.size() < magicNumber) && pile.Size() > 0)
            {
                group.addToTop(pile.Retrieve(rng));
            }

            if (group.size() > 0)
            {
                PCLActions.Bottom.FetchFromPile(name, 1, group)
                        .SetOptions(false, true).AddCallback(cards -> {
                            for (AbstractCard c : cards) {
                                PCLCardAffinities a = PCLGameUtilities.GetPCLAffinities(c);
                                if (a != null) {
                                    PCLActions.Bottom.TryChooseAffinity(name, a.GetAffinitiesAsArray())
                                            .AddConditionalCallback(afs -> {
                                                for (AffinityChoice af : afs ) {
                                                    PCLActions.Bottom.ObtainAffinityToken(af.Affinity, false);
                                                }
                                            });
                                }

                                if (CheckSpecialCondition(true) && info.TryActivateLimited()) {
                                    PCLActions.Bottom.Motivate(c, 1);
                                }
                            }
                        });
            }
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLGameUtilities.GetCurrentMatchCombo() >= secondaryValue;
    }
}

