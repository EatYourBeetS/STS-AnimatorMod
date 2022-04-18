package eatyourbeets.cards.animator.special;

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
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Special_Refrain extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Special_Refrain.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    private static final ArrayList<AbstractCard> selectableCards = new ArrayList<>();
    private static ArrayList<String> cardIDs;

    public Special_Refrain()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetPlayable(UpdateSelectableCards());
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyWeak(p, p, 1);
        GameActions.Bottom.LoseHP(magicNumber, AttackEffects.POISON).IgnoreTempHP(false).CanKill(true);

        if (UpdateSelectableCards())
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : selectableCards)
            {
                group.group.add(GameUtilities.Imitate(c));
            }

            if (group.size() > 0)
            {
                GameActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards2 ->
                {
                   UpdateCardIDs();

                   for (AbstractCard c2 : cards2)
                   {
                       cardIDs.add(c2.cardID);
                       GameActions.Bottom.MakeCardInHand(c2);
                   }
                });
            }
        }
    }

    protected boolean UpdateSelectableCards()
    {
        selectableCards.clear();

        final int lastTurn = CombatStats.TurnCount(true) - 1;
        if (lastTurn >= 0)
        {
            UpdateCardIDs();

            for (AbstractCard c : CombatStats.CardsPlayedThisCombat(lastTurn))
            {
                if (GameUtilities.IsLowCost(c) && !cardIDs.contains(c.cardID))
                {
                    selectableCards.add(c);
                }
            }
        }

        return selectableCards.size() > 0;
    }

    protected void UpdateCardIDs()
    {
        cardIDs = CombatStats.GetCombatData(cardID, null);
        if (cardIDs == null)
        {
            cardIDs = CombatStats.SetCombatData(cardID, new ArrayList<>());
        }
    }
}