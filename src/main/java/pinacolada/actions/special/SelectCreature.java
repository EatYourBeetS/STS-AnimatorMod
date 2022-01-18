package pinacolada.actions.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class SelectCreature extends EYBActionWithCallback<AbstractCreature>
{
    public enum Targeting
    {
        Player,
        Enemy,
        Any,
        Random,
        AoE,
        None
    }

    protected ActionT1<AbstractCreature> onHovering;
    protected AbstractCreature previous;
    protected AbstractCreature target;
    protected Targeting targeting;
    protected boolean skipConfirmation;

    private final Vector2[] points = new Vector2[20];
    private final Vector2 controlPoint = new Vector2();
    private final Vector2 origin = new Vector2();
    private float arrowScaleTimer;

    public SelectCreature(Targeting targeting, String sourceName)
    {
        super(ActionType.SPECIAL);

        this.card = null;
        this.targeting = targeting;

        Initialize(amount, sourceName);
    }

    public SelectCreature(AbstractCard card)
    {
        super(ActionType.SPECIAL);

        this.card = card;

        PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
        if (c != null && c.attackTarget != null)
        {
            switch (c.attackTarget)
            {
                case Self:
                    targeting = Targeting.Player;
                    break;
                case None:
                    targeting = Targeting.None;
                    break;
                case Normal:
                    targeting = Targeting.Enemy;
                    break;
                case ALL:
                    targeting = Targeting.AoE;
                    break;
                case Random:
                    targeting = Targeting.Random;
                    break;
            }
        }
        else
        {
            switch (card.target)
            {
                case ENEMY:
                case SELF_AND_ENEMY:
                    targeting = Targeting.Enemy;
                    break;
                case ALL:
                case ALL_ENEMY:
                    targeting = Targeting.AoE;
                    break;
                case SELF:
                    targeting = Targeting.Player;
                    break;
                case NONE:
                    targeting = Targeting.None;
                    break;
            }
        }

        Initialize(amount, card.name);
    }

    public SelectCreature SetOnHovering(ActionT1<AbstractCreature> onHovering)
    {
        this.onHovering = onHovering;

        return this;
    }

    public SelectCreature SetMessage(String message)
    {
        this.message = message;

        return this;
    }

    public SelectCreature SetMessage(String format, Object... args)
    {
        this.message = PCLJUtils.Format(format, args);

        return this;
    }

    public SelectCreature IsCancellable(boolean cancellable) {
        this.canCancel = cancellable;
        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (skipConfirmation)
        {
            switch (targeting)
            {
                case Player:
                    Complete(AbstractDungeon.player);
                    return;
                case Random:
                    Complete(PCLGameUtilities.GetRandomEnemy(true));
                    return;
                case AoE:
                case None:
                    Complete(null);
                    return;
            }
        }

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
            if (card != null)
            {
                card.applyPowers();
            }

            Complete();
            return;
        }

        switch (targeting)
        {
            case Player:
                UpdateTarget(true, false);
                break;
            case Enemy:
                UpdateTarget(false, true);
                break;
            case Any:
                UpdateTarget(true, true);
                break;
        }

        if (InputHelper.justClickedLeft || InputHelper.justReleasedClickLeft)
        {
            InputHelper.justClickedLeft = false;
            InputHelper.justReleasedClickLeft = false;
            switch (targeting)
            {
                case Random:
                    Complete(target = PCLGameUtilities.GetRandomEnemy(true));
                    return;

                case AoE:
                case None:
                    Complete(null);
                    return;

                case Player:
                case Enemy:
                case Any:
                    if (target != null)
                    {
                        Complete(target);
                        return;
                    }
            }
        }

        GR.UI.AddPostRender(this::Render);
    }

    @Override
    protected void Complete()
    {
        GameCursor.hidden = false;
        super.Complete();
    }

    protected void UpdateTarget(boolean targetPlayer, boolean targetEnemy)
    {
        if (target != null)
        {
            previous = target;
            target = null;
        }

        if (targetPlayer && (player.hb.hovered && !player.isDying))
        {
            target = player;
        }
        else if (targetEnemy)
        {
            for (AbstractMonster m : PCLGameUtilities.GetEnemies(true))
            {
                if (m.hb.hovered && !m.isDying)
                {
                    target = m;
                    break;
                }
            }
        }

        if ((card != null) && (target != null) && (target != previous))
        {
            if (target instanceof AbstractMonster)
            {
                card.calculateCardDamage((AbstractMonster) target);
            }
            else
            {
                card.applyPowers();
            }
        }

        if (onHovering != null)
        {
            onHovering.Invoke(target);
        }
    }

    protected void Render(SpriteBatch sb)
    {
        switch (targeting)
        {
            case Player:
            case Enemy:
            case Any:
                RenderArrow(sb);
                if (target != null)
                {
                    target.renderReticle(sb);
                }
                break;

            case Random:
            case AoE:
                for (AbstractCreature c : PCLGameUtilities.GetAllCharacters(true))
                {
                    c.renderReticle(sb);
                }
                break;

            case None:
                break;
        }

        String message = UpdateMessage();
        if (message.length() > 0)
        {
            FontHelper.renderDeckViewTip(sb, message, Settings.scale * 96f, Settings.CREAM_COLOR);
        }
    }

    protected void RenderArrow(SpriteBatch sb)
    {
        float x = (float) InputHelper.mX;
        float y = (float) InputHelper.mY;

        if (card != null)
        {
            origin.x = card.current_x;
            origin.y = card.current_y;

            controlPoint.x = card.current_x - ((x - card.current_x) / 4f);
            controlPoint.y = card.current_y + ((y - card.current_y - 40f * Settings.scale) / 2f);
        }
        else
        {
            origin.x = player.dialogX;
            origin.y = player.dialogY - 40f * Settings.scale;

            controlPoint.x = player.animX - (x - player.animX) / 4f;
            controlPoint.y = player.animY + (y - player.animY - 40f * Settings.scale) / 2f;
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

        Vector2 tmp = new Vector2(controlPoint.x - x, controlPoint.y - y);
        tmp.nor();
        DrawCurve(sb, origin, new Vector2(x, y), controlPoint);
        sb.draw(ImageMaster.TARGET_UI_ARROW, x - 128f, y - 128f, 128f, 128f, 256f, 256f, arrowScale, arrowScale,
        tmp.angle() + 90f, 0, 0, 256, 256, false, false);
    }

    protected void DrawCurve(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control)
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
}
