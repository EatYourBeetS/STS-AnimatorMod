package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.hitboxes.FakeHitbox;
import eatyourbeets.utilities.EYBFontHelper;

public class FakeLibraryCard extends EYBCardBase
{
    @Override
    public void upgrade()
    {

    }

    public FakeLibraryCard()
    {
        super("","", null, 0, GR.Animator.Strings.Misc.NotEnoughCards, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.hb = new FakeHitbox(hb);
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
    public void initializeDescription()
    {

    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {

    }

    @Override
    public AbstractCard makeCopy() {
        return new FakeLibraryCard();
    }

    @Override
    public void update()
    {
        super.update();
        this.hovered = false;
        this.renderTip = false;
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        FontHelper.renderFontLeft(sb, EYBFontHelper.CardTitleFont_Normal, GR.Animator.Strings.Misc.NotEnoughCards, hb.cX, hb.cY + hb.height / 4, Color.WHITE);
    }
}
