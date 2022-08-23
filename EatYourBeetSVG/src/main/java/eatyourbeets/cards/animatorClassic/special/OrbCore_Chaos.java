package eatyourbeets.cards.animatorClassic.special;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrbCore_Chaos extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Chaos.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 1;

    public OrbCore_Chaos()
    {
        super(DATA, null, 1);

        Initialize(0, 0, VALUE, 1);
    }

    @Override
    protected AnimatorPower GetPower()
    {
        return new OrbCore_ChaosPower(player, 1);
    }

    public static class OrbCore_ChaosPower extends OrbCorePower
    {
        public OrbCore_ChaosPower(AbstractCreature owner, int amount)
        {
            super(DATA, owner, amount, VALUE);

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
}