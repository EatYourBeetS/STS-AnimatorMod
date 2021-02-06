package eatyourbeets.cards.animator.beta.Colorless;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.PhasingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NaoTomori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NaoTomori.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    protected static final Color weakenedGlowColor = Color.RED;

    public NaoTomori()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);

        SetSynergy(Synergies.Charlotte);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        if (player.hand.size() < BaseMod.MAX_HAND_SIZE)
        {
            while (group.size() < magicNumber)
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetFilter(c -> c.type.equals(CardType.POWER) || c.type.equals(CardType.CURSE))
                .SetOptions(false, false, false)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = (cards.get(0));

                        GameActions.Bottom.ReplaceCard(card.uuid, new Normality())
                                .SetUpgrade(card.upgraded);
                        GameActions.Bottom.StackPower(new PhasingPower(p, secondaryValue));
                    }
                });
    }
}