package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AkiraInoue extends PCLCard
{
    public static final PCLCardData DATA = Register(AkiraInoue.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public AkiraInoue()
    {
        super(DATA);

        Initialize(0, 4, 2, 6);
        SetUpgrade(0, 0, 2);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<AbstractCard> pile = new RandomizedList<>(JUtils.Filter(player.discardPile.group, c -> !(PCLGameUtilities.IsHindrance(c)) && PCLGameUtilities.GetPCLAffinityLevel(c, PCLAffinity.General, true) > 0));
        if (pile.Size() == 0) {
            pile.AddAll(player.discardPile.group);
        }
        while (group.size() < magicNumber && pile.Size() > 0)
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
                                PCLActions.Top.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity)
                                        .SetAffinityChoices(a.GetAffinities().toArray(new PCLAffinity[] {}))
                                        .SetOptions(false, true);
                            }
                        }
                    });
        }

        if (!player.stance.ID.equals(NeutralStance.STANCE_ID) && info.TryActivateSemiLimited()) {
            PCLGameUtilities.AddAffinityRerolls(1);
        }
    }
}