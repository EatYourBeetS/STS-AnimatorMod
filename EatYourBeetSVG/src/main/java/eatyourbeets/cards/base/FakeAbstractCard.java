package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameUtilities;

public class FakeAbstractCard extends EYBCardBase
{
    private final AbstractCard source;

    public static FakeAbstractCard FromCard(AbstractCard card)
    {
        return new FakeAbstractCard(card);
    }

    @Override
    public void upgrade()
    {

    }

    protected FakeAbstractCard(AbstractCard card)
    {
        super(card.cardID, card.name, null, card.cost, card.rawDescription, card.type, card.color, card.rarity, card.target);
        this.source = card;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster)
    {

    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {

    }

    @Override
    public AbstractCard makeCopy()
    {
        return FromCard(source);
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        GameUtilities.CopyVisualProperties(source, this);
        source.render(sb, selected);
    }
}
