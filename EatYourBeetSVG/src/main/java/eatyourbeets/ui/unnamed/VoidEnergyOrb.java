package eatyourbeets.ui.unnamed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.resources.unnamed.UnnamedStrings;
import eatyourbeets.utilities.GameUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VoidEnergyOrb
{
    public static final Logger logger = LogManager.getLogger(TextureLoader.class.getName());

    private static final Texture Orb_BG = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_BG.png");
    private static final Texture Orb_FG = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_FG.png");
    private static final Texture Orb_VFX1 = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_VFX1.png");
    private static final Texture Orb_VFX2 = UnnamedResources.GetTexture("images/characters/unnamed/energy2/Orb_VFX2.png");
    private static final UIStrings uiStrings = UnnamedStrings.EnergyPanel;

    private final Hitbox hb = new Hitbox(128f * Settings.scale, 248f * Settings.scale, 147.2f * Settings.scale, 147.2f * Settings.scale);
    private final Void source;
    private float time = 0f;
    private boolean voidScreenOpen = false;

    public int currentEnergy = 0;
    public int maxEnergy = 0;
    public boolean isHidden = false;

    public VoidEnergyOrb(Void source)
    {
        this.source = source;
    }

    public void render(EnergyPanel __instance, SpriteBatch sb)
    {
        if (!this.isHidden)
        {
            sb.setColor(Color.WHITE);
            sb.draw(Orb_BG, hb.x, hb.y, hb.width, hb.height);
            sb.draw(Orb_VFX2, hb.x, hb.y, hb.width / 2f, hb.height / 2f, hb.width, hb.height, 1.2f, 1.2f, time * -7f % 360f, 0, 0, 128, 128, false, false);
            sb.draw(Orb_VFX1, hb.x, hb.y, hb.width / 2f, hb.height / 2f, hb.width, hb.height, 1f, 1f, time * 6f % 360f, 0, 0, 128, 128, false, false);
            sb.draw(Orb_FG, hb.x, hb.y, hb.width, hb.height);
            FontHelper.renderFontCentered(sb, FontHelper.energyNumFontBlue, currentEnergy + "/" + maxEnergy, 201.6f * Settings.scale, 321.6f * Settings.scale, Color.WHITE.cpy());
        }
    }

    public void update(EnergyPanel instance)
    {
        time += Gdx.graphics.getRawDeltaTime();

        maxEnergy = source.group.size();

        boolean screenUp = AbstractDungeon.isScreenUp;
        if (!screenUp)
        {
            voidScreenOpen = false;
        }

        if (!this.isHidden)
        {
            hb.update();

            if (hb.hovered && GameUtilities.InBattle())
            {
                if (!screenUp)
                {
                    TipHelper.renderGenericTip(50f * Settings.scale, hb.y + hb.height * 2, uiStrings.TEXT[0], uiStrings.TEXT[1]);

                    if (InputHelper.justClickedLeft && !CombatStats.Void.isEmpty())
                    {
                        AbstractDungeon.gridSelectScreen.open(new CardGroup(CombatStats.Void, CardGroup.CardGroupType.UNSPECIFIED),
                                0, uiStrings.TEXT[2], false, false, true, false);

                        voidScreenOpen = true;
                    }
                }
                else if (InputHelper.justClickedLeft && voidScreenOpen && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
                {
                    AbstractDungeon.closeCurrentScreen();
                }
            }
        }
    }

    public void refill()
    {
        currentEnergy = maxEnergy;
    }

    public void useEnergy(int amount)
    {
        if (amount > 0)
        {
            currentEnergy -= amount;

            if (currentEnergy < 0)
            {
                currentEnergy = 0;
            }
        }
    }
}
