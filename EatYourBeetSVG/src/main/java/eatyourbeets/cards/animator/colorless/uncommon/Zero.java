package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RotatingList;

public class Zero extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zero.class)
            .SetSkill(0, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GrimoireOfZero);

    public Zero()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetDrawPileCardPreview(this::FindCards);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainIntellect(1, upgraded);
        if (upgraded) {
            GameActions.Bottom.Draw(1);
        }
        GameActions.Bottom.PlayFromPile(name, 1, m, p.drawPile)
        .SetOptions(true, false)
        .SetFilter(c -> c.type == CardType.SKILL);
    }

    protected void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        cards.Clear();
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.SKILL && GameUtilities.IsPlayable(c, target) && !c.tags.contains(GR.Enums.CardTags.VOLATILE))
            {
                cards.Add(c);
            }
        }
    }
}