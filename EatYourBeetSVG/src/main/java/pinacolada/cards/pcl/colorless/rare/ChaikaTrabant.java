package pinacolada.cards.pcl.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.ChaikaBohdan;
import pinacolada.cards.pcl.special.Layla;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.special.PoisonAffinityPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class ChaikaTrabant extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final CardGroup upgradedChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public static final PCLCardData DATA = Register(ChaikaTrabant.class)
            .SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                cardChoices.addToBottom(new ChaikaBohdan());
                cardChoices.addToBottom(new Layla());
                for (AbstractCard c : cardChoices.group)
                {
                    AbstractCard copy = c.makeStatEquivalentCopy();
                    c.upgrade();
                    upgradedChoices.addToBottom(c);
                    data.AddPreview(c, true);
                }
            });

    private AbstractMonster enemy;

    public ChaikaTrabant()
    {
        super(DATA);

        Initialize(0, 0, 6, 2);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.Green, 8);

        SetProtagonist(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromPile(name, player.hand.size(), player.hand)
                .SetOptions(true, false)
                .SetFilter(c -> c.costForTurn == 0)
                .AddCallback(cards -> {
                   for (AbstractCard c : cards) {
                       c.flash();
                       PCLGameUtilities.IncreaseDamage(c, c.baseDamage, false);
                   }
        });
        ChaikaTrabant other = (ChaikaTrabant) makeStatEquivalentCopy();
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(other);

        if (info.CanActivateLimited && TrySpendAffinity(PCLAffinity.Green) && info.TryActivateLimited()) {
            PCLActions.Bottom.StackPower(new PoisonAffinityPower(p, secondaryValue));
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        PCLGameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);

        PCLActions.Bottom.Callback(() ->
                PCLActions.Bottom.SelectFromPile(name, 1, upgraded ? upgradedChoices : cardChoices)
                        .SetOptions(false, false)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards)
                            {
                                PCLActions.Bottom.MakeCardInHand(c);
                            }
                        }));
    }
}