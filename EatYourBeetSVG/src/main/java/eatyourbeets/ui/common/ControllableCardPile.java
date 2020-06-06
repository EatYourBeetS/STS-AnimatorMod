package eatyourbeets.ui.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.ui.controls.GUI_DynamicCardGrid;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RenderHelpers;
import patches.energyPanel.EnergyPanelPatches;

import java.util.ArrayList;
import java.util.Iterator;

// TODO: Move to a different folder
public class ControllableCardPile implements OnPhaseChangedSubscriber
{
    // Temporary Textures
    private static final Texture Orb_BG = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_BG.png");
    private static final Texture Orb_FG = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_FG.png");
    private static final Texture Orb_VFX1 = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_VFX1.png");
    private static final Texture Orb_VFX2 = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_VFX2.png");

    public static final float HOVER_TIME_OUT = 0.4F;
    private final Hitbox hb = new Hitbox(128f * Settings.scale, 248f * Settings.scale, 147.2f * Settings.scale, 147.2f * Settings.scale);
    private float timer = 0f;

    public boolean isHidden = false;

    public final ArrayList<ControllableCard> controllers = new ArrayList<>();
    private final GUI_DynamicCardGrid cardGrid;
    private final GUI_Image background;

    public ControllableCardPile()
    {
        background = RenderHelpers.ForTexture(ImageMaster.WHITE_SQUARE_IMG)
        .SetHitbox(new Hitbox(0, 0))
        .SetColor(0, 0, 0, 0.8f);

        cardGrid = new GUI_DynamicCardGrid().SetScale(0.5f)
        .SetDrawStart(Settings.WIDTH * 0.2f, Settings.HEIGHT * 0.5f)
        .SetOnCardHover(__ -> RefreshTimer())
        .SetOnCardClick(card ->
        {
            for (ControllableCard c : controllers)
            {
                if (c.card == card)
                {
                    c.Select();
                    return;
                }
            }
        });
    }

    public void Clear()
    {
        CombatStats.onPhaseChanged.Subscribe(this);
        EnergyPanelPatches.Pile = this;
        controllers.clear();
    }

    public ControllableCard Add(ControllableCard controller)
    {
        controllers.add(controller);
        return controller;
    }

    public ControllableCard Add(AbstractCard card)
    {
        return Add(new ControllableCard(card));
    }

    public boolean Contains(AbstractCard card) {
        for (ControllableCard controllableCard : controllers) {
            if (controllableCard.card == card) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        cardGrid.Clear();

        Iterator<ControllableCard> i = controllers.iterator();
        while (i.hasNext())
        {
            ControllableCard c = i.next();

            c.Update();
            //c.card.applyPowers();
            //glowCheck(c.card);
            //c.card.triggerOnGlowCheck();
            if (c.IsDeleted())
            {
                i.remove();
            }
            else if (c.IsEnabled())
            {
                cardGrid.AddCard(c.card);
            }
        }

        int size = cardGrid.cards.size();
        int rowSize;
        if (size <= 10)
        {
            rowSize = 5;

        }
        else if (size <= 16)
        {
            rowSize = 8;
        }
        else
        {
            rowSize = 10;
        }
        background.Resize(30 + 135 * Math.min(size, rowSize), - 200 * (1 + Math.floorDiv(size, rowSize)), Settings.scale);
        //We set scale to be a constant 0.5f because any smaller and the enlarged cards on hover begin to
        //cover up their neighbors, making it difficult for the player to hover some cards
        cardGrid.SetRowSize(rowSize).SetScale(0.5f);
        background.Translate(cardGrid.drawStart_x - (Settings.scale * 80), cardGrid.drawStart_y + (Settings.scale * 100));
    }

    public void Update(EnergyPanel panel)
    {
        timer = Math.max(0, timer - Gdx.graphics.getRawDeltaTime());
        isHidden = cardGrid.cards.isEmpty();

        if (isHidden)
        {
            return;
        }

        for (ControllableCard c : controllers) {
            AbstractCard card = c.card;
            if (IsHovering() && card.hb.hovered) {
                RefreshTimer();
            }
        }

        hb.update();
        cardGrid.Update();
        background.Update();
        if (hb.hovered && GameUtilities.InBattle() && !AbstractDungeon.isScreenUp)
        {
            RefreshTimer();
            // TODO: Localization
            //TipHelper.renderGenericTip(50f * Settings.scale, hb.y + hb.height * 2, "Command Pile",
            //"You may activate cards' effects from this pile by selecting them during your turn.");
        }

        if (IsHovering())
        {
            GR.UI.AddPostRender(this::PostRender);
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
        sb.draw(Orb_VFX2, hb.x, hb.y, hb.width / 2f, hb.height / 2f, hb.width, hb.height, 1.2f, 1.2f, timer * -7f % 360f, 0, 0, 128, 128, false, false);
        sb.draw(Orb_VFX1, hb.x, hb.y, hb.width / 2f, hb.height / 2f, hb.width, hb.height, 1f, 1f, timer * 6f % 360f, 0, 0, 128, 128, false, false);
        sb.draw(Orb_FG, hb.x, hb.y, hb.width, hb.height);
        FontHelper.renderFontCentered(sb, FontHelper.energyNumFontBlue, String.valueOf(cardGrid.cards.size()), 201.6f * Settings.scale, 321.6f * Settings.scale, Color.WHITE.cpy());
    }

    public void PostRender(SpriteBatch sb)
    {
        if (isHidden)
        {
            return;
        }
        background.Render(sb);
        cardGrid.Render(sb);
    }

    public void RefreshTimer()
    {
        timer = HOVER_TIME_OUT;
    }

    public boolean IsHovering()
    {
        return timer > 0;
    }

    public static void glowCheck(AbstractCard c)
    {
        if (c.canUse(AbstractDungeon.player, null) && !AbstractDungeon.isScreenUp) {
            c.beginGlowing();
        } else {
            c.stopGlowing();
        }
    }
}
