package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class MeguKakizaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MeguKakizaki.class)
    		.SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
    
    public MeguKakizaki()
    {
        super(DATA);

        Initialize(0, 5, 5, 4);
        SetUpgrade(0, 3, 1, 2);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetEthereal(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.Callback(() ->
        {
            if (CombatStats.TryActivateLimited(cardID)) {
                if (!DrawSuigintou(player.drawPile))
                    DrawSuigintou(player.discardPile);
            }
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

                    GameActions.Top.MoveCard(c, group, player.hand)
                            .ShowEffect(true, true)
                            .AddCallback(GameUtilities::Retain);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ReducePower(player, player, DelayedDamagePower.POWER_ID, magicNumber).AddCallback(po -> {
            if (po != null) {
                AbstractMonster mo = GameUtilities.GetRandomEnemy(true);
                if (mo != null && po.amount > 0) {
                    GameActions.Bottom.DealDamageAtEndOfTurn(player, mo, po.amount, AttackEffects.BLUNT_HEAVY);
                }
            }
        });

        GameActions.Bottom.ExhaustFromPile(name, 1, p.discardPile, p.hand)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(c -> c.type == CardType.CURSE)
                .SetOptions(false, true)
                .AddCallback(() ->
                {
                    CombatStats.TryActivateLimited(cardID);
                    GameActions.Bottom.RecoverHP(secondaryValue);
                });
    }
}

// <LIM>. Exhaust a Curse anywhere to Heal !M! HP at the end of combat.
// <SLM>. When Discarded: Draw the bottom card of your draw pile.

