package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.defect.RemoveAllOrbsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Truth extends AnimatorCard_UltraRare
{
    private static final Crystallize status = new Crystallize();
    private static final int multiplier = 4;

    public static final EYBCardData DATA = Register(Truth.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.FullmetalAlchemist)
            .PostInitialize(data -> data.AddPreview(status, false));

    public Truth()
    {
        super(DATA);

        Initialize(0, 0, 8);
        SetCostUpgrade(-1);

        SetAffinity_Star(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Star(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (Affinity affinity : Affinity.Basic())
        {
            int amountToGain = GetLevelAffinity(affinity) * multiplier;
            GameActions.Bottom.StackAffinityPower(affinity, amountToGain);
        }

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Add(new RemoveAllOrbsAction());
        }
        else
        {
            ReplaceCard(p);
        }
    }

    private void ReplaceCard(AbstractPlayer p)
    {
        final CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            if (!c.cardID.equals(status.cardID) && !c.uuid.equals(uuid) && GameUtilities.CanRemoveFromDeck(c))
            {
                temp.group.add(c);
            }
        }

        if (temp.size() > 0)
        {
            GameActions.Bottom.SelectFromPile(name, 1, temp)
            .SetOptions(false, false)
            .SetMessage(GR.Common.Strings.GridSelection.TransformInto(status.name))
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    player.masterDeck.removeCard(cards.get(0));
                    player.masterDeck.addToTop(status.makeCopy());
                }
            });
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return player.hand.size() >= magicNumber;
    }
}