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
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.utilities.GameActions;
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

    private final Hitbox hb = new Hitbox(128f * Settings.scale, 248f * Settings.scale, 147.2f * Settings.scale, 147.2f * Settings.scale);
    private float time = 0f;

    public boolean isHidden = false;

    private final ArrayList<ControllableCard> controllers = new ArrayList<>();
    private final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

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
        time += Gdx.graphics.getRawDeltaTime();
        isHidden = group.isEmpty();
        if (isHidden)
        {
            return;
        }

        hb.update();
        if (hb.hovered && GameUtilities.InBattle() && !AbstractDungeon.isScreenUp)
        {
            // TODO: Localization
            TipHelper.renderGenericTip(50f * Settings.scale, hb.y + hb.height * 2, "Command Pile",
            "You may activate cards' effects from this pile by selecting them during your turn.");

            if (InputHelper.justClickedLeft && AbstractDungeon.actionManager.phase == GameActionManager.Phase.WAITING_ON_USER)
            {
                GameActions.Top.SelectFromPile("Command Pile", 1, group)
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    AbstractCard card = cards.size() > 0 ? cards.get(0) : null;
                    if (card != null)
                    {
                        for (ControllableCard c : controllers)
                        {
                            if (c.card == card)
                            {
                                c.Select();
                                return;
                            }
                        }
                    }
                });
            }
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
}
