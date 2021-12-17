package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.interfaces.subscribers.OnAfterlifeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Hisako extends PCLCard implements OnAfterlifeSubscriber
{
    public static final PCLCardData DATA = Register(Hisako.class).SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Self).SetSeriesFromClassPackage();

    public Hisako()
    {
        super(DATA);

        Initialize(0, 7, 3, 1);
        SetUpgrade(0,0,1,0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.General, 3);
        SetExhaust(true);
        SetAfterlife(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
        SetAffinityRequirement(PCLAffinity.General, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback((cards) -> {
            if (cards.size() > 0) {
                PCLAffinity af = cards.get(0).Affinity;

                final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                final RandomizedList<AbstractCard> pile = new RandomizedList<>(player.drawPile.group);
                while (group.size() < magicNumber && pile.Size() > 0)
                {
                    AbstractCard c = pile.Retrieve(rng);
                    if (c instanceof PCLCard && ((PCLCard) c).affinities.GetLevel(af) > 0)
                    group.addToTop(c);
                }

                if (group.size() >= 0)
                {
                    PCLActions.Bottom.FetchFromPile(name, 1, group).SetOptions(false, true);
                }
            }
        });
    }

    @Override
    public void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard) {
        if (player.hand.contains(this)) {
            PCLActions.Bottom.GainInvocation(secondaryValue);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onAfterlife.Subscribe(this);
    }
}