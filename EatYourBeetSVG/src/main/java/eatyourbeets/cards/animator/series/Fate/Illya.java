package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.cards.DrawPileCardPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Illya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Illya.class).SetSkill(1, CardRarity.COMMON);
    static
    {
        DATA.AddPreview(new Berserker(), false);
    }

    private final DrawPileCardPreview drawPileCardPreview = new DrawPileCardPreview(Illya::FindBestCard);

    public Illya()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, -2);

        SetSynergy(Synergies.Fate);
        SetAlignment(0, 0, 1, 0, 0);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        drawPileCardPreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        drawPileCardPreview.Update();
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        super.Render(sb, hovered, selected, library);

        if (!library)
        {
            drawPileCardPreview.Render(sb);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(() ->
        {
            if (!DrawBerserker(player.drawPile))
            {
                if (!DrawBerserker(player.discardPile))
                {
                    if (!DrawBerserker(player.exhaustPile))
                    {
                        DrawBerserker(player.hand);
                    }
                }
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        AbstractCard card = FindBestCard(m);
        if (card != null)
        {
            GameActions.Bottom.StackPower(new SelfDamagePower(p, magicNumber));
            GameActions.Bottom.PlayCard(card, p.drawPile, m);
        }
    }

    private boolean DrawBerserker(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Berserker.DATA.ID.equals(c.cardID))
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

    private static AbstractCard FindBestCard(AbstractMonster target)
    {
        AbstractCard bestCard = null;
        int maxDamage = Integer.MIN_VALUE;
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.ATTACK && c.cardPlayable(target) && !c.tags.contains(GR.Enums.CardTags.TEMPORARY))
            {
                c.calculateCardDamage(target);
                if (c.damage > maxDamage)
                {
                    maxDamage = c.damage;
                    bestCard = c;
                }
                c.resetAttributes();
            }
        }

        return bestCard;
    }
}