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
import eatyourbeets.powers.PlayerStatistics;
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

    private final Hitbox hb = new Hitbox(128.0F * Settings.scale, 248.0F * Settings.scale, 147.2F * Settings.scale, 147.2F * Settings.scale);
    private final Void source;
    private float time = 0.0F;
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
            sb.draw(Orb_VFX2, hb.x, hb.y, hb.width / 2.0F, hb.height / 2.0F, hb.width, hb.height, 1.2F, 1.2F, time * -7.0F % 360.0F, 0, 0, 128, 128, false, false);
            sb.draw(Orb_VFX1, hb.x, hb.y, hb.width / 2.0F, hb.height / 2.0F, hb.width, hb.height, 1.0F, 1.0F, time * 6.0F % 360.0F, 0, 0, 128, 128, false, false);
            sb.draw(Orb_FG, hb.x, hb.y, hb.width, hb.height);
            FontHelper.renderFontCentered(sb, FontHelper.energyNumFontBlue, currentEnergy + "/" + maxEnergy, 201.6F * Settings.scale, 321.6F * Settings.scale, Color.WHITE.cpy());
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
                    TipHelper.renderGenericTip(50.0F * Settings.scale, hb.y + hb.height * 2, uiStrings.TEXT[0], uiStrings.TEXT[1]);

                    if (InputHelper.justClickedLeft && !PlayerStatistics.Void.isEmpty())
                    {
                        AbstractDungeon.gridSelectScreen.open(new CardGroup(PlayerStatistics.Void, CardGroup.CardGroupType.UNSPECIFIED),
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
