package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;


public class TomoeKashiwaba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TomoeKashiwaba.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public TomoeKashiwaba()
    {
        super(DATA);

        Initialize(0, 5, 2, 4);
        SetUpgrade(0, 3, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> !c.isEthereal)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        GameUtilities.Retain(card);
                        for (Affinity af : Affinity.Extended()) {
                            if (GameUtilities.GetAffinityLevel(card, af, true) > 0) {
                                GameActions.Bottom.StackAffinityPower(af, 1, false);
                            }
                        }
                    }
                });

        if (CheckAffinity(Affinity.General) && CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> GameActions.Bottom.TryChooseGainAffinity(name, secondaryValue, Affinity.Basic()));
        }
    }
}
