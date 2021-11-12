package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ChaikaBohdan;
import eatyourbeets.cards.animator.special.Layla;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.PoisonAffinityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaTrabant extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public static final EYBCardData DATA = Register(ChaikaTrabant.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                cardChoices.addToBottom(new ChaikaBohdan());
                cardChoices.addToBottom(new Layla());
                for (AbstractCard c : cardChoices.group)
                {
                    data.AddPreview(c, true);
                }
            });

    private AbstractMonster enemy;

    public ChaikaTrabant()
    {
        super(DATA);

        Initialize(0, 0, 6, 2);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Blue(2);
        SetAffinity_Light(2);

        SetAffinityRequirement(Affinity.Green, 8);

        SetProtagonist(true);
        SetHarmonic(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, 9999, false).AddCallback(cards -> {
           for (AbstractCard c : cards) {
               GameUtilities.ModifyDamage(c, c.baseDamage * 2, !upgraded);
           }
        });
        ChaikaTrabant other = (ChaikaTrabant) makeStatEquivalentCopy();
        CombatStats.onStartOfTurnPostDraw.Subscribe(other);

        if (info.CanActivateLimited && TrySpendAffinity(Affinity.Green) && info.TryActivateLimited()) {
            GameActions.Bottom.StackPower(new PoisonAffinityPower(p, secondaryValue));
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);

        GameActions.Bottom.Callback(() ->
                GameActions.Bottom.SelectFromPile(name, 1, cardChoices)
                        .SetOptions(false, false)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards)
                            {
                                GameActions.Bottom.MakeCardInHand(c);
                            }
                        }));
    }
}