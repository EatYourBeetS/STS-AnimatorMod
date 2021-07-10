package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class AnimatorCardRewardAffinities extends GUIElement
{
    public static final float ICON_SIZE = Scale(40);

    private static final CommonImages.AffinityIcons ICONS = GR.Common.Images.Affinities;

    private final AdvancedHitbox hb;
    private final ArrayList<CardAffinityCounter> counters = new ArrayList<>();
    private final CardAffinityCounter starCounter;
    private final GUI_Image header1;
    private final GUI_Image header2;
    private long lastFrame;

    public AnimatorCardRewardAffinities()
    {
        hb = new DraggableHitbox(ScreenW(0.025f), ScreenH(0.65f), Scale(140), Scale(50), false);

        header1 = RenderHelpers.ForTexture(ICONS.Border_Weak.Texture())
        .SetHitbox(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.15f, 1f, true));
        header2 = RenderHelpers.ForTexture(ICONS.Border.Texture())
        .SetBackgroundTexture(ICONS.BorderBG.Texture())
        .SetForegroundTexture(ICONS.BorderFG.Texture())
        .SetHitbox(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.45f, 1f, true));

        counters.add(new CardAffinityCounter(hb, AffinityType.Red));
        counters.add(new CardAffinityCounter(hb, AffinityType.Green));
        counters.add(new CardAffinityCounter(hb, AffinityType.Blue));
        counters.add(new CardAffinityCounter(hb, AffinityType.Light));
        counters.add(new CardAffinityCounter(hb, AffinityType.Dark));
        starCounter = new CardAffinityCounter(hb, AffinityType.Star);
        counters.add(starCounter);
    }

    public void Close()
    {
        SetActive(false);
    }

    public void Open(ArrayList<AbstractCard> cards)
    {
        isActive = GR.Animator.IsSelected();

        if (!isActive)
        {
            return;
        }

        for (CardAffinityCounter c : counters)
        {
            c.Percentage = c.AffinityLV1.level = c.AffinityLV2.level = 0;
        }

        for (AbstractCard c : cards)
        {
            EYBCard card = JUtils.SafeCast(c, EYBCard.class);
            if (card == null)
            {
                continue;
            }

            int star = card.affinities.GetLevel(AffinityType.Star);
            if (star > 0)
            {
                if (star > 1)
                {
                    starCounter.AffinityLV2.level += 1;
                }
                else
                {
                    starCounter.AffinityLV1.level += 1;
                }
                continue;
            }

            for (EYBCardAffinity alignment : card.affinities.List)
            {
                for (CardAffinityCounter counter : counters)
                {
                    if (counter.Type == alignment.Type)
                    {
                        if (alignment.level > 1)
                        {
                            counter.AffinityLV2.level += 1;
                        }
                        else
                        {
                            counter.AffinityLV1.level += 1;
                        }
                        break;
                    }
                }
            }
        }

        for (CardAffinityCounter c : counters)
        {
            int value = c.AffinityLV1.level + c.AffinityLV2.level;
            if (c.SetActive(value > 0).isActive)
            {
                c.Percentage = value / (float)cards.size();
            }
        }

        counters.sort((a, b) -> (int)(1000 * (b.Percentage - a.Percentage)));

        int index = 0;
        for (CardAffinityCounter c : counters)
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
        for (CardAffinityCounter c : counters)
        {
            c.TryUpdate();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        long frame = Gdx.graphics.getFrameId();
        if (frame == lastFrame)
        {
            return;
        }

        lastFrame = frame;
        header1.TryRender(sb);
        header2.TryRender(sb);
        for (CardAffinityCounter c : counters)
        {
            c.TryRender(sb);
        }
        hb.render(sb);
    }
}
