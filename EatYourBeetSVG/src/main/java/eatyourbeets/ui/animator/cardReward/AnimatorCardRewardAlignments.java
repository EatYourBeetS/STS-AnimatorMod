package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameUtilities;

public class AnimatorCardRewardAlignments extends GUIElement
{
    private static final CommonImages.AlignmentsIcons ICONS = GR.Common.Images.Alignments;

    public final AlignmentCounter red;
    public final AlignmentCounter green;
    public final AlignmentCounter blue;
    public final AlignmentCounter light;
    public final AlignmentCounter dark;

    public AnimatorCardRewardAlignments()
    {
        red = new AlignmentCounter(ICONS.Red.Texture())
                .SetPosition(ScreenW(0.065f), ScreenH(0.65f));

        green = new AlignmentCounter(ICONS.Green.Texture())
                .SetPosition(ScreenW(0.065f), ScreenH(0.60f));

        blue = new AlignmentCounter(ICONS.Blue.Texture())
                .SetPosition(ScreenW(0.065f), ScreenH(0.55f));

        light = new AlignmentCounter(ICONS.Light.Texture())
                .SetPosition(ScreenW(0.065f), ScreenH(0.50f));

        dark = new AlignmentCounter(ICONS.Dark.Texture())
                .SetPosition(ScreenW(0.065f), ScreenH(0.45f));
    }

    public void Close()
    {
        isActive = false;
    }

    public void Open()
    {
        isActive = GameUtilities.IsPlayerClass(GR.Animator.PlayerClass);
// TODO:
//
//        if (!isActive)
//        {
//            return;
//        }
//
//        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
//        {
//
//        }
    }

    @Override
    public void Update()
    {
        red.Update();
        green.Update();
        blue.Update();
        light.Update();
        dark.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        red.Render(sb);
        green.Render(sb);
        blue.Render(sb);
        light.Render(sb);
        dark.Render(sb);
    }
}
