package eatyourbeets.cards.animator.colorless.uncommon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.cards.DrawPileCardPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RotatingList;

public class Zero extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zero.class)
            .SetSkill(0, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GrimoireOfZero);

    private final static RotatingList<AbstractCard> skillsCache = new RotatingList<>();
    private final DrawPileCardPreview drawPileCardPreview = new DrawPileCardPreview(this::FindNextSkill);

    public Zero()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetExhaust(true);
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

        if (GR.UI.Elapsed75() && drawPileCardPreview.GetCurrentCard() != null)
        {
            drawPileCardPreview.FindCard();
        }
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
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainIntellect(1);
        GameActions.Bottom.PlayFromPile(name, 1, m, p.drawPile)
        .SetOptions(true, false)
        .SetFilter(c -> c.type == CardType.SKILL);
    }

    protected AbstractCard FindNextSkill(AbstractMonster target)
    {
        final int previousSize = skillsCache.Count();
        final int previousIndex = skillsCache.GetIndex();

        skillsCache.Clear();
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.SKILL && c.cardPlayable(target))
            {
                skillsCache.Add(c);
            }
        }

        skillsCache.SetIndex((skillsCache.Count() == previousSize) ? previousIndex : 0);

        return (skillsCache.Count() > 0) ? skillsCache.Next(true) : null;
    }
}