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
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.ui.controls.GUI_DynamicCardGrid;
import eatyourbeets.utilities.GameUtilities;
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

    public static final float HOVER_TIME_OUT = 1.0F;
    private final Hitbox hb = new Hitbox(128f * Settings.scale, 248f * Settings.scale, 147.2f * Settings.scale, 147.2f * Settings.scale);
    private float timer = 0f;

    public boolean isHidden = false;

    private final ArrayList<ControllableCard> controllers = new ArrayList<>();
//    private final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private final GUI_DynamicCardGrid cardGrid = new GUI_DynamicCardGrid();

    public ControllableCardPile()
    {
        cardGrid.SetScale(0.5f);

        cardGrid.SetOnCardHover(__ -> RefreshTimer());
        cardGrid.SetOnCardClick(card ->
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

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        cardGrid.Clear();

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
                cardGrid.AddCard(c.card);
            }
        }
    }

    public void Update(EnergyPanel panel)
    {
        timer = Math.max(0, timer - Gdx.graphics.getRawDeltaTime());
        isHidden = cardGrid.cards.isEmpty();

        if (isHidden)
        {
            return;
        }

        cardGrid.Update();

        for (ControllableCard c : controllers)
        {
            c.Update(this);
        }

        hb.update();
        if (hb.hovered && GameUtilities.InBattle() && !AbstractDungeon.isScreenUp)
        {
            RefreshTimer();

            // TODO: Localization
            TipHelper.renderGenericTip(50f * Settings.scale, hb.y + hb.height * 2, "Command Pile",
            "You may activate cards' effects from this pile by selecting them during your turn.");
        }

        if (timer > 0)
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
}
