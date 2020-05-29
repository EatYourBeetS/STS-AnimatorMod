package eatyourbeets.ui.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.utilities.GameUtilities;
import patches.energyPanel.EnergyPanelPatches;

import java.util.ArrayList;
import java.util.Iterator;

import static patches.ControlPilePatches.UpdateControlPileCard.glowCheck;

// TODO: Move to a different folder
public class ControllableCardPile implements OnPhaseChangedSubscriber
{
    // Temporary Textures
    private static final Texture Orb_BG = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_BG.png");
    private static final Texture Orb_FG = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_FG.png");
    private static final Texture Orb_VFX1 = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_VFX1.png");
    private static final Texture Orb_VFX2 = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_VFX2.png");

    private final Hitbox hb = new Hitbox(128f * Settings.scale, 248f * Settings.scale, 147.2f * Settings.scale, 147.2f * Settings.scale);
    public float time = 0f;
    public static final float HOVER_TIME_OUT = 1.0F;
    public boolean isHidden = false;

    public final ArrayList<ControllableCard> controllers = new ArrayList<>();
    public final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public void Clear()
    {
        CombatStats.onPhaseChanged.Subscribe(this);
        EnergyPanelPatches.Pile = this;
        controllers.clear();
    }

    public ControllableCard Add(ControllableCard controller)
    {
        controllers.add(controller);
        setCardPositions();
        return controller;
    }

    public ControllableCard Add(AbstractCard card, CardGroup group)
    {
        return Add(new ControllableCard(card, group));
    }

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        group.clear();
        Iterator<ControllableCard> i = controllers.iterator();
        while (i.hasNext())
        {
            ControllableCard c = i.next();
            c.Update();
            if (c.IsDeleted())
            {
                i.remove();
            }
            else if (c.IsEnabled())
            {
                group.group.add(c.card);
            }
        }
    }

    public void Update(EnergyPanel panel)
    {
        time -= Gdx.graphics.getRawDeltaTime();
        isHidden = group.isEmpty();
        if (isHidden)
        {
            return;
        }

        hb.update();
        for (ControllableCard c : controllers) {
            AbstractCard card = c.card;
            card.update();
            if (card.hb.hovered) {
                resetTime();
            }
        }
        if (isHovering()) {
            //Uncomment this to render card previews
            GR.UI.AddPostRender(this::PostRender);
        }
        if (hb.hovered && GameUtilities.InBattle() && !AbstractDungeon.isScreenUp)
        {
            resetTime();
            // TODO: Localization
            TipHelper.renderGenericTip(50f * Settings.scale, hb.y + hb.height * 2, "Command Pile",
            "You may activate cards' effects from this pile by selecting them during your turn.");

//            if (InputHelper.justClickedLeft && AbstractDungeon.actionManager.phase == GameActionManager.Phase.WAITING_ON_USER)
//            {
//                GameActions.Top.SelectFromPile("Command Pile", 1, group)
//                .SetOptions(false, true)
//                .AddCallback(cards ->
//                {
//                    AbstractCard card = cards.size() > 0 ? cards.get(0) : null;
//                    if (card != null)
//                    {
//                        for (ControllableCard c : controllers)
//                        {
//                            if (c.card == card)
//                            {
//                                c.Select();
//                                return;
//                            }
//                        }
//                    }
//                });
//            }
        }
    }

    public void Render(EnergyPanel panel, SpriteBatch sb)
    {
        if (isHidden)
        {
            return;
        }

        sb.setColor(Color.WHITE);
        sb.draw(Orb_BG, hb.x, hb.y, hb.width, hb.height);
        sb.draw(Orb_VFX2, hb.x, hb.y, hb.width / 2f, hb.height / 2f, hb.width, hb.height, 1.2f, 1.2f, time * -7f % 360f, 0, 0, 128, 128, false, false);
        sb.draw(Orb_VFX1, hb.x, hb.y, hb.width / 2f, hb.height / 2f, hb.width, hb.height, 1f, 1f, time * 6f % 360f, 0, 0, 128, 128, false, false);
        sb.draw(Orb_FG, hb.x, hb.y, hb.width, hb.height);
        FontHelper.renderFontCentered(sb, FontHelper.energyNumFontBlue, String.valueOf(group.size()), 201.6f * Settings.scale, 321.6f * Settings.scale, Color.WHITE.cpy());
    }

    public void PostRender(SpriteBatch sb)
    {
        if (!AbstractDungeon.isScreenUp)
        {
            if (!isHidden) {
                for (ControllableCard c : controllers) {
                    AbstractCard card = c.card;
                    if (!card.equals(AbstractDungeon.player.hoveredCard)) {
                        card.render(sb);
                    }
                }
            }
        }
    }

    public void setCardPositions()
    {
        float RENDER_X_OFFSET = 200.0F * Settings.scale;
        float RENDER_X = 370 * Settings.scale;
        float RENDER_Y = 400 * Settings.scale;
        System.out.println("setting positions");
        int count = 0;
        for (ControllableCard controllableCard : controllers) {
            AbstractCard card = controllableCard.card;
            card.targetDrawScale = 0.5f;
            card.targetAngle = 0;
            card.target_x = RENDER_X + RENDER_X_OFFSET * count;
            card.target_y = RENDER_Y;
            glowCheck(card);
            count++;
        }
    }

    public void resetTime() {
        time = HOVER_TIME_OUT;
    }

    public boolean isHovering() {
        return time > 0;
    }
}
