package eatyourbeets.powers.unnamed;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.interfaces.markers.DontCopy;
import eatyourbeets.powers.UnnamedAttachmentPower;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class AttachmentsPower extends UnnamedPower implements DontCopy
{
    public static final String POWER_ID = CreateFullID(AttachmentsPower.class);

    private static final float DISPLAY_TIMER_THRESHOLD = 0.25f;
    private static final float CARD_SCALE = 0.75f;
    private float displayTimer = 0;

    public AttachmentsPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.BUFF, true);
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        if (GameUtilities.CanAcceptInput(false) && (owner.hb.hovered || owner.healthHb.hovered))
        {
            if ((displayTimer += GR.UI.Delta()) > DISPLAY_TIMER_THRESHOLD)
            {
                GR.UI.AddPostRender(this::RenderCards);
            }
        }
        else
        {
            displayTimer = 0;
        }
    }

    private void RenderCards(SpriteBatch sb)
    {
        final float x_step = (AbstractCard.IMG_WIDTH * CARD_SCALE * 1.075f);
        final float y_step = -(AbstractCard.IMG_HEIGHT * CARD_SCALE * 0.15f);
        final float x = owner.drawX + x_step;
        final float y = Settings.HEIGHT * 0.75f;
        final ArrayList<ArrayList<UnnamedCard>> cardGroups = GetAttachments();
        for (int col = 0; col < cardGroups.size(); col++)
        {
            final ArrayList<UnnamedCard> list = cardGroups.get(col);
            for (int row = 0; row < list.size(); row++)
            {
                final UnnamedCard card = list.get(row);
                RenderCard(card, x + (col * x_step), y + (row * y_step), sb);
            }
        }
    }

    private void RenderCard(AbstractCard card, float x, float y, SpriteBatch sb)
    {
        card.untip();
        card.unhover();
        card.unfadeOut();
        card.lighten(true);
        card.setAngle(0, true);
        card.drawScale = card.targetDrawScale = CARD_SCALE;
        card.current_x = card.target_x = x;
        card.current_y = card.target_y = y;

        sb.setColor(Color.WHITE);
        card.render(sb);
    }

    private ArrayList<ArrayList<UnnamedCard>> GetAttachments()
    {
        final ArrayList<ArrayList<UnnamedCard>> cards = new ArrayList<>();
        for (AbstractPower power : owner.powers)
        {
            final UnnamedAttachmentPower p = JUtils.SafeCast(power, UnnamedAttachmentPower.class);
            if (p != null)
            {
                cards.add(p.cards);
            }
        }

        return cards;
    }
}
