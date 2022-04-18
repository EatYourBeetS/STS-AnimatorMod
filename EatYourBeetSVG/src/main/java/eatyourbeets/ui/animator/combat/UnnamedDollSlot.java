package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;
import org.lwjgl.util.vector.Vector2f;

public class UnnamedDollSlot extends GUIElement
{
    public int Index;
    public Vector2f Offset;
    public UnnamedDoll Doll;

    public UnnamedDollSlot(int slot)
    {
        Index = slot;
        Doll = null;

        switch (Index)
        { //@Formatter: off
            case  0: Offset = new Vector2f(260, 210); break;
            case  1: Offset = new Vector2f(100, 355); break;
            case  2: Offset = new Vector2f(-95, 375); break;
            case  3: Offset = new Vector2f(-255, 245); break;
            default: Offset = new Vector2f(0, 0); break;
        } //@Formatter: on
    }

    public float GetX()
    {
        return AbstractDungeon.player.drawX + (Offset.x * Settings.scale);
    }

    public float GetY()
    {
        return AbstractDungeon.player.drawY + (Offset.y * Settings.scale);
    }

    public Vector2f GetPosition()
    {
        if (AbstractDungeon.player == null)
        {
            JUtils.LogError(this, "AbstractDungeon.player was null");
            return new Vector2f(0, 0);
        }

        return new Vector2f(GetX(), GetY());
    }

    public boolean CanAct()
    {
        return GameUtilities.IsValidTarget(Doll);
    }

    public boolean IsAvailable()
    {
        return !CanAct();
    }

    public void Clear()
    {
        Doll = null;
    }

    public void Update()
    {
        if (CanAct())
        {
            Doll.update();
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (CanAct() && Doll.Visible)
        {
            Doll.render(sb);
        }
        else
        {
            final float cX = GetX();
            final float cY = GetY();
            final Color color = Colors.Purple(0.5f);
            RenderHelpers.DrawCentered(sb, color, ImageMaster.ORB_SLOT_2, cX, cY, 96f, 96f, 1, 0);
            RenderHelpers.DrawCentered(sb, color, ImageMaster.ORB_SLOT_1, cX, cY, 96f, 96f, 1, GR.UI.Time_Multi(20));
        }
    }
}