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

import java.util.Comparator;

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

        if (GR.UI.Elapsed80() && drawPileCardPreview.GetCurrentCard() != null)
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
        boolean refresh = false;
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.SKILL && !skillsCache.Contains(c))
            {
                refresh = true;
                break;
            }
        }

        if (refresh)
        {
            skillsCache.Clear();
            for (AbstractCard c : player.drawPile.group)
            {
                if (c.type == CardType.SKILL)
                {
                    skillsCache.Add(c);
                }
            }
            skillsCache.GetInnerList().sort(Comparator.comparing(a -> a.name));
        }

        return skillsCache.Next(true);
    }
}