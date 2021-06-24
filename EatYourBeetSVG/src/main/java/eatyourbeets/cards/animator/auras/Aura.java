package eatyourbeets.cards.animator.auras;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import eatyourbeets.blights.common.CardEffectBlight;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RenderHelpers;

public abstract class Aura extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber, OnBattleEndSubscriber
{
    private static final Color RENDER_COLOR = new Color(0.8f, 0.8f, 0.8f, 1);

    protected AbstractBlight blight;
    protected int maxTurn;

    public static EYBCardData RegisterAura(Class<? extends AnimatorCard> type)
    {
        return Register(type).SetSkill(-2, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);
    }

    protected Aura(EYBCardData cardData)
    {
        super(cardData);

        this.cropPortrait = false;
        this.maxTurn = 3;
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(GR.Tooltips.Aura);
        }
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
        CombatStats.onBattleEnd.Subscribe(this);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        GameActions.Instant.Callback(() ->
        {
            blight = new CardEffectBlight(makeStatEquivalentCopy());
            blight.instantObtain(player, player.blights.size(), false);
            blight.setCounter(GetCountdown());

            player.drawPile.removeCard(this);
            player.discardPile.removeCard(this);
            player.exhaustPile.removeCard(this);
            player.hand.removeCard(this);
        });
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        int countdown = GetCountdown();
        if (blight != null)
        {
            blight.setCounter(countdown);
        }
        if (countdown == 0)
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            OnUse(player, null, false);
            blight.flash();

            GameActions.Last.Callback(this::OnBattleEnd);
        }
    }

    @Override
    public void OnBattleEnd()
    {
        if (blight != null)
        {
            player.blights.remove(blight);
        }
    }

    public int GetCountdown()
    {
        return Math.max(0, maxTurn - CombatStats.TurnCount(false));
    }
}