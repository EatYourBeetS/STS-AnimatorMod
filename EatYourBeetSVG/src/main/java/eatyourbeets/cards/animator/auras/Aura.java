package eatyourbeets.cards.animator.auras;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RenderHelpers;

public abstract class Aura extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber, Hidden
{
    private static final Color RENDER_COLOR = new Color(0.8f, 0.8f, 0.8f, 1);

    public static EYBCardData RegisterAura(Class<? extends AnimatorCard> type)
    {
        return Register(type).SetSkill(-2, CardRarity.SPECIAL);
    }

    protected Aura(EYBCardData cardData)
    {
        super(cardData);

        this.cropPortrait = false;
    }

    @Override
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        RENDER_COLOR.a = this.transparency;
        switch (type)
        {
            case ATTACK:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, GR.Animator.Images.CARD_BACKGROUND_ATTACK_UR.Texture(), x, y);
                break;
            case SKILL:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, GR.Animator.Images.CARD_BACKGROUND_SKILL_UR.Texture(), x, y);
                break;
            case POWER:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, GR.Animator.Images.CARD_BACKGROUND_POWER_UR.Texture(), x, y);
                break;
            default:
                super.renderCardBg(sb, x, y);
                break;
        }
    }

    @Override
    protected String GetTypeText()
    {
        return "Aura";
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        GameActions.Instant.Callback(() ->
        {
            player.drawPile.removeCard(this);
            player.discardPile.removeCard(this);
            player.exhaustPile.removeCard(this);
            player.hand.removeCard(this);
        });
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (CanActivate(CombatStats.TurnCount(false)))
        {
            GameEffects.List.ShowCardBriefly(makeCopy(), Settings.WIDTH * 0.75f, Settings.HEIGHT * 0.75f);
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            OnUse(player, null, false);
        }
    }

    public boolean CanActivate(int currentTurn)
    {
        return currentTurn >= 3;
    }
}