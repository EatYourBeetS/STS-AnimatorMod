package eatyourbeets.actions.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class SelectCreature extends EYBActionWithCallback<AbstractCreature>
{
    protected AbstractCreature target;
    protected Vector2 controlPoint;
    protected boolean includePlayer;

    private float arrowScaleTimer;
    private Vector2[] points = new Vector2[20];

    public SelectCreature(boolean includePlayer)
    {
        super(ActionType.SPECIAL);

        Initialize(amount);
    }

    public SelectCreature SetControlPoint(Vector2 point)
    {
        controlPoint = point;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        for (int i = 0; i < this.points.length; ++i)
        {
            this.points[i] = new Vector2();
        }

        super.FirstUpdate();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        GameCursor.hidden = true;

        if (InputHelper.justClickedRight && canCancel)
        {
            Complete();
            return;
        }

        target = null;

        if (includePlayer && player.hb.hovered && !player.isDying)
        {
            target = player;
        }
        else
        {
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                if (m.hb.hovered && !m.isDying)
                {
                    target = m;
                    break;
                }
            }
        }

        if (InputHelper.justClickedLeft)
        {
            InputHelper.justClickedLeft = false;
            if (target != null)
            {
                Complete(target);
                return;
            }
        }

        GR.UI.AddPostRender(this::Render);
    }

    public void Render(SpriteBatch sb)
    {
        float x = (float) InputHelper.mX;
        float y = (float) InputHelper.mY;

        Vector2 origin = controlPoint;

        if (origin == null)
        {
            origin = new Vector2(player.animX - (x - player.animX) / 4f, player.animY + (y - player.animY - 40f * Settings.scale) / 2f);
        }

        float arrowScale;
        if (target == null)
        {
            arrowScale = Settings.scale;
            arrowScaleTimer = 0f;
            sb.setColor(new Color(1f, 1f, 1f, 1f));
        }
        else
        {
            arrowScaleTimer += Gdx.graphics.getDeltaTime();
            if (arrowScaleTimer > 1f)
            {
                arrowScaleTimer = 1f;
            }

            arrowScale = Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, arrowScaleTimer);
            sb.setColor(new Color(1f, 0.2F, 0.3F, 1f));
        }

        Vector2 tmp = new Vector2(origin.x - x, origin.y - y);
        tmp.nor();
        DrawCurve(sb, new Vector2(player.dialogX, player.dialogY - 40f * Settings.scale), new Vector2(x, y), origin);
        sb.draw(ImageMaster.TARGET_UI_ARROW, x - 128f, y - 128f, 128f, 128f, 256f, 256f, arrowScale, arrowScale, tmp.angle() + 90f, 0, 0, 256, 256, false, false);

        if (target != null)
        {
            target.renderReticle(sb);
        }
    }

    private void DrawCurve(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control)
    {
        float radius = 7f * Settings.scale;

        for (int i = 0; i < points.length - 1; ++i)
        {
            points[i] = Bezier.quadratic(points[i], (float) i / 20f, start, control, end, new Vector2());
            radius += 0.4F * Settings.scale;
            float angle;
            Vector2 tmp;
            if (i != 0)
            {
                tmp = new Vector2(points[i - 1].x - points[i].x, points[i - 1].y - points[i].y);
                angle = tmp.nor().angle() + 90f;
            }
            else
            {
                tmp = new Vector2(control.x - points[i].x, control.y - points[i].y);
                angle = tmp.nor().angle() + 270f;
            }

            sb.draw(ImageMaster.TARGET_UI_CIRCLE, points[i].x - 64f, points[i].y - 64f, 64f, 64f, 128f, 128f, radius / 18f, radius / 18f, angle, 0, 0, 128, 128, false, false);
        }
    }

    @Override
    protected void Complete()
    {
        GameCursor.hidden = false;
        super.Complete();
    }
}
