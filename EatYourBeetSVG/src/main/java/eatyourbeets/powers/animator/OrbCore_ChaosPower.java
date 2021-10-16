package eatyourbeets.powers.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrbCore_ChaosPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_ChaosPower.class);

    public OrbCore_ChaosPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        //this.value = OrbCore_Chaos.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        if (p.hand.size() < BaseMod.MAX_HAND_SIZE)
        {
            while (group.size() < 3)
            {
                AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat();
                if (!card.hasTag(AbstractCard.CardTags.HEALING) && group.findCardById(card.cardID) == null)
                {
                    group.addToBottom(card.makeCopy());
                }
            }

            GameActions.Bottom.SelectFromPile(name, 1, group)
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    GameActions.Bottom.MakeCardInHand(cards.get(0)).AddCallback(c -> c.modifyCostForCombat(-1));
                    GameUtilities.RefreshHandLayout();
                }
            });
        }
    }
}