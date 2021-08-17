package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mayuri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mayuri.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Random).SetSeriesFromClassPackage();

    public Mayuri()
    {
        super(DATA);

        Initialize(8, 4, 2, 1);
        SetUpgrade(4, 1, 0);
        SetAffinity_Light(1, 1, 1);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return other.tags.contains(CardTags.STARTER_DEFEND);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);

        GameActions.Bottom.SelectFromHand(name, secondaryValue, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> c.tags.contains(CardTags.STARTER_DEFEND))
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        GameUtilities.Retain(card);
                    }
                });

        if (isSynergizing && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Callback(cards -> {
                    for (AbstractCard card : GameUtilities.GetOtherCardsInHand(this))
                    {
                        if (card.tags.contains(CardTags.STARTER_DEFEND))
                        {
                            card.baseBlock += magicNumber;
                        }
                    }
            });
        }
    }
}