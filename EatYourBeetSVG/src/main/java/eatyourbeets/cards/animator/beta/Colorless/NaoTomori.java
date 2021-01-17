package eatyourbeets.cards.animator.beta.Colorless;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NaoTomori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NaoTomori.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    protected static final Color weakenedGlowColor = Color.RED;

    public NaoTomori()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 1);
        SetRetain(true);

        SetSynergy(Synergies.Charlotte);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        if (player.hand.size() < BaseMod.MAX_HAND_SIZE)
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
                    GameActions.Bottom.MakeCardInHand(cards.get(0));
                    GameUtilities.RefreshHandLayout();
                }
            });
        }
    }

    @Override
    public void triggerOnGlowCheck()
    {
        super.triggerOnGlowCheck();

        if (AbstractDungeon.id.equals(TheUnnamedReign.ID) || AbstractDungeon.actNum > secondaryValue)
        {
            this.glowColor = weakenedGlowColor;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //UnnamedReign has an Act number of 4, despite being Act 5.
        if (!AbstractDungeon.id.equals(TheUnnamedReign.ID) && AbstractDungeon.actNum <= secondaryValue)
        {
            GameActions.Bottom.ExhaustFromHand(name, 1, false)
                    .SetFilter(c -> c.type.equals(CardType.POWER) || c instanceof AnimatorCard_UltraRare)
                    .SetOptions(false, false, false)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            GameActions.Bottom.StackPower(new BufferPower(p, 1));
                        }
                    });
        }
    }
}