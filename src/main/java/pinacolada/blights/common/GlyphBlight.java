package pinacolada.blights.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.cards.pcl.glyphs.Glyph;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLPlayerData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class GlyphBlight extends AbstractGlyphBlight
{
    public static final String ID = CreateFullID(GlyphBlight.class);
    public static final int MAX_CHOICES = 3;
    public Glyph glyph;

    public GlyphBlight()
    {
        this(0);
    }

    public GlyphBlight(int counter)
    {
        super(ID, PCLPlayerData.ASCENSION_GLYPH1_UNLOCK, PCLPlayerData.ASCENSION_GLYPH1_LEVEL_STEP, 0, 1, counter);
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        super.renderTip(sb);

        if (glyph != null)
        {
            glyph.drawScale = glyph.targetDrawScale = 0.8f;
            glyph.current_x = glyph.target_x = InputHelper.mX + (((InputHelper.mX > (Settings.WIDTH * 0.5f)) ? -1.505f : 1.505f) * PCLCardTooltip.BOX_W);
            glyph.current_y = glyph.target_y = InputHelper.mY - (AbstractCard.IMG_HEIGHT * 0.5f);
            GR.UI.AddPostRender(glyph::render);
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLActions.Bottom.SelectFromPile(name, 1, CreateGlyphGroup())
                .AddCallback(selection -> {
                    if (selection.size() > 0) {
                        Glyph e = (Glyph) selection.get(0);
                        e.AtStartOfBattleEffect();
                        this.glyph = e;
                        flash();
                    }
                });
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (this.glyph != null) {
            this.glyph.AtStartOfTurnEffect();
        }
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (this.glyph != null) {
            this.glyph.AtEndOfTurnEffect(true);
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();

        this.glyph = null;
    }

    @Override
    public String GetUpdatedDescription() {
        return FormatDescription(0, PCLGameUtilities.InGame() ? PCLJUtils.Format(strings.DESCRIPTION[1], GetPotency()) : "");
    }

    public CardGroup CreateGlyphGroup()
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<AbstractCard> possiblePicks = new RandomizedList<>();
        possiblePicks.AddAll(PCLJUtils.Map(Glyph.GetCards(), Glyph::makeCopy));

        for (int i = 0; i < MAX_CHOICES; i++) {
            AbstractCard pick = possiblePicks.Retrieve(PCLGameUtilities.GetRNG());
            for (int j = 0; j < GetPotency(); j++) {
                pick.upgrade();
            }
            group.group.add(pick);
        }

        return group;
    }

}