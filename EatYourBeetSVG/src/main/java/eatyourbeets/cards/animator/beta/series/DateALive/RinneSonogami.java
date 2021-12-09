package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.SuperchargeStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class RinneSonogami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RinneSonogami.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public RinneSonogami()
    {
        super(DATA);

        Initialize(0, 8, 2, 1);
        SetUpgrade(0, 1, 0);
        SetAffinity_Light(1, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.SelectFromHand(name, secondaryValue, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> c.rarity == CardRarity.BASIC)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        GameUtilities.Retain(card);
                        AnimatorCard aC = JUtils.SafeCast(card, AnimatorCard.class);
                        if (aC != null)
                        {
                            aC.affinities.Set(Affinity.Blue, 1);
                            aC.flash();
                        }
                        if ((SuperchargeStance.IsActive() || info.IsSynergizing) && CombatStats.TryActivateSemiLimited(cardID)) {
                            if (card.baseDamage > 0) {
                                GameUtilities.IncreaseDamage(card, magicNumber, false);
                            }
                            if (card.baseBlock > 0) {
                                GameUtilities.IncreaseBlock(card, magicNumber, false);
                            }
                        }
                    }
                });
    }
}