package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RepairPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class MeguKakizaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MeguKakizaki.class)
    		.SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    
    public MeguKakizaki()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 0);

        SetHealing(true);
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Bottom.Callback(() ->
        {
            if (!DrawSuigintou(player.drawPile))
                DrawSuigintou(player.discardPile);
        });
    }

    private boolean DrawSuigintou(CardGroup group) // Copied from Chibimoth.java
    {
        for (AbstractCard c : group.group)
        {
            if (Suigintou.DATA.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());

                    if (upgraded) {
                        GameActions.Top.MoveCard(c, group, player.hand)
                                .ShowEffect(true, true)
                                .AddCallback(GameUtilities::Retain);
                    }
                    else {
                        GameActions.Top.MoveCard(c, group, player.hand)
                                .ShowEffect(true, true);
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!CombatStats.HasActivatedLimited(cardID))
        {
            GameActions.Bottom.ExhaustFromHand(name, 1, false)
                    .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                    .SetFilter(c -> c.type == CardType.CURSE)
                    .SetOptions(false, false, false)
                    .AddCallback(() ->
                    {
                        CombatStats.TryActivateLimited(cardID);
                        GameActions.Bottom.StackPower(new RepairPower(player, magicNumber));
                    });
        }
    }
}

// <LIM>. Exhaust a Curse to Heal !M! HP at the end of combat.
// <SLM>. When Discarded: Draw the bottom card of your draw pile.

