package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAlignment;
import eatyourbeets.cards.base.EYBCardAlignmentType;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class AnimatorCardRewardAlignments extends GUIElement
{
    public static final float ICON_SIZE = Scale(40);

    private static final CommonImages.AlignmentsIcons ICONS = GR.Common.Images.Alignments;

    private final AdvancedHitbox hb;
    private final ArrayList<CardAlignmentCounter> counters = new ArrayList<>();
    private final CardAlignmentCounter starCounter;
    private final GUI_Image header1;
    private final GUI_Image header2;

    public AnimatorCardRewardAlignments()
    {
        hb = new DraggableHitbox(ScreenW(0.025f), ScreenH(0.65f), Scale(140), Scale(50), false);

        header1 = RenderHelpers.ForTexture(ICONS.Border_Weak.Texture())
        .SetHitbox(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.15f, 1f, true));
        header2 = RenderHelpers.ForTexture(ICONS.Border.Texture())
        .SetHitbox(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.45f, 1f, true));

        counters.add(new CardAlignmentCounter(hb, EYBCardAlignmentType.Red));
        counters.add(new CardAlignmentCounter(hb, EYBCardAlignmentType.Green));
        counters.add(new CardAlignmentCounter(hb, EYBCardAlignmentType.Blue));
        counters.add(new CardAlignmentCounter(hb, EYBCardAlignmentType.Light));
        counters.add(new CardAlignmentCounter(hb, EYBCardAlignmentType.Dark));
        starCounter = new CardAlignmentCounter(hb, EYBCardAlignmentType.Star);
        counters.add(starCounter);
    }

    public void Close()
    {
        isActive = false;
    }

    public void Open(ArrayList<AbstractCard> cards)
    {
        isActive = GameUtilities.IsPlayerClass(GR.Animator.PlayerClass);

        if (!isActive)
        {
            return;
        }

        for (CardAlignmentCounter c : counters)
        {
            c.Percentage = c.AlignmentLV1.level = c.AlignmentLV2.level = 0;
        }

        for (AbstractCard c : cards)
        {
            EYBCard card = JUtils.SafeCast(c, EYBCard.class);
            if (card != null)
            {
                if (card.alignments.HasStar())
                {
                    starCounter.AlignmentLV1.level += 1;
                }
                else
                {
                    for (EYBCardAlignment alignment : card.alignments.List)
                    {
                        for (CardAlignmentCounter counter : counters)
                        {
                            if (counter.Type == alignment.Type)
                            {
                                if (alignment.level > 1)
                                {
                                    counter.AlignmentLV2.level += 1;
                                }
                                else
                                {
                                    counter.AlignmentLV1.level += 1;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (CardAlignmentCounter c : counters)
        {
            int value = c.AlignmentLV1.level + c.AlignmentLV2.level;
            if (c.SetActive(value > 0).isActive)
            {
                c.Percentage = value / (float)cards.size();
            }
        }

        counters.sort((a, b) -> (int)(1000 * (b.Percentage - a.Percentage)));

        int index = 0;
        for (CardAlignmentCounter c : counters)
        {
            if (c.isActive)
            {
                c.SetIndex(index);
                index += 1;
            }
        }
    }

    @Override
    public void Update()
    {
        hb.update();
        header1.TryUpdate();
        header2.TryUpdate();
        for (CardAlignmentCounter c : counters)
        {
            c.TryUpdate();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        header1.TryRender(sb);
        header2.TryRender(sb);
        for (CardAlignmentCounter c : counters)
        {
            c.TryRender(sb);
        }
        hb.render(sb);
    }
}
