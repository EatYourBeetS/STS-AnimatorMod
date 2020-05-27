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
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCallback;
import eatyourbeets.utilities.GenericCondition;
import patches.energyPanel.EnergyPanelPatches;

import java.util.ArrayList;
import java.util.Iterator;

public class CommandPile
{
    public class CardController
    {
        protected final AbstractCard card;
        protected GenericCondition<AbstractCard> isAvailable;
        protected GenericCallback<AbstractCard> onSelect;

        public CardController(AbstractCard card)
        {
            this.card = card;
        }

        public CardController IsAvailable(GenericCondition<AbstractCard> isAvailable)
        {
            this.isAvailable = isAvailable;

            return this;
        }

        public CardController OnSelect(GenericCallback<AbstractCard> onSelect)
        {
            this.onSelect = onSelect;

            return this;
        }

        public boolean IsAvailable()
        {
            return isAvailable == null || isAvailable.Check(card);
        }

        public void OnSelect()
        {
            if (onSelect != null)
            {
                onSelect.Complete(card);
            }
        }
    }

    private static final Texture Orb_BG = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_BG.png");
    private static final Texture Orb_FG = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_FG.png");
    private static final Texture Orb_VFX1 = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_VFX1.png");
    private static final Texture Orb_VFX2 = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_VFX2.png");

    private final Hitbox hb = new Hitbox(128f * Settings.scale, 248f * Settings.scale, 147.2f * Settings.scale, 147.2f * Settings.scale);
    private float time = 0f;

    public boolean isHidden = false;

    private final ArrayList<CardController> controllers = new ArrayList<>();
    private final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public void Initialize()
    {
        EnergyPanelPatches.Pile = this;
        if (group.isEmpty())
        {
            group.addToBottom(new QuestionMark());
            group.addToBottom(new QuestionMark());
        }
    }

    public CardController Add(AbstractCard card)
    {
        CardController controller = new CardController(card);
        controllers.add(controller);
        return controller;
    }

    public void Update(EnergyPanel panel)
    {
        isHidden = controllers.isEmpty();

        if (isHidden)
        {
            return;
        }

        time += Gdx.graphics.getRawDeltaTime();
        hb.update();

        group.clear();
        for (CardController c : controllers)
        {
            if (c.IsAvailable())
            {
                group.addToBottom(c.card);
            }
        }

        if (hb.hovered && GameUtilities.InBattle() && !AbstractDungeon.isScreenUp)
        {
            TipHelper.renderGenericTip(50f * Settings.scale, hb.y + hb.height * 2, "Test", "Test");

            if (InputHelper.justClickedLeft && AbstractDungeon.actionManager.phase == GameActionManager.Phase.WAITING_ON_USER)
            {
                GameActions.Top.SelectFromPile("Test", 1, group)
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        Iterator<CardController> i = controllers.iterator();
                        while(i.hasNext())
                        {
                            CardController c = i.next();
                            if (c.card == cards.get(0))
                            {
                                c.OnSelect();
                                i.remove();
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
        if (!isHidden)
        {
            sb.setColor(Color.WHITE);
            sb.draw(Orb_BG, hb.x, hb.y, hb.width, hb.height);
            sb.draw(Orb_VFX2, hb.x, hb.y, hb.width / 2f, hb.height / 2f, hb.width, hb.height, 1.2f, 1.2f, time * -7f % 360f, 0, 0, 128, 128, false, false);
            sb.draw(Orb_VFX1, hb.x, hb.y, hb.width / 2f, hb.height / 2f, hb.width, hb.height, 1f, 1f, time * 6f % 360f, 0, 0, 128, 128, false, false);
            sb.draw(Orb_FG, hb.x, hb.y, hb.width, hb.height);
            FontHelper.renderFontCentered(sb, FontHelper.energyNumFontBlue, String.valueOf(group.size()), 201.6f * Settings.scale, 321.6f * Settings.scale, Color.WHITE.cpy());
        }
    }
}
