package eatyourbeets.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.resources.Resources_Animator_Images;
import eatyourbeets.cards.animator.*;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.utilities.RenderHelpers;
import patches.AbstractEnums;

import java.util.HashMap;

public abstract class AnimatorCard_UltraRare extends AnimatorCard implements Hidden
{
    private static final float A = 0.4f;
    public static final Color RENDER_COLOR = new Color(A, A, A, 1);

    private static final float B = 0.4f;
    public static final Color RENDER_COLOR2 = new Color(B, B, B, 1);

    private static final byte[] whatever = {0x61, 0x6e, 0x69, 0x6d, 0x61, 0x74, 0x6f, 0x72, 0x3a, 0x75, 0x72};
    private static final String idPrefix = new String(whatever);

    protected AnimatorCard_UltraRare(String id, int cost, CardType type, CardTarget target)
    {
        super(id, cost, type, CardColor.COLORLESS, CardRarity.SPECIAL, target);

        // Do not use SetUnique()
        tags.add(AbstractEnums.CardTags.UNIQUE);

        setOrbTexture(Resources_Animator_Images.ORB_A_PNG, Resources_Animator_Images.ORB_B_PNG);
    }

    private static HashMap<String, AnimatorCard_UltraRare> Cards = null;

    public static HashMap<String, AnimatorCard_UltraRare> GetCards()
    {
        if (Cards == null)
        {
            Cards = new HashMap<>();
            Cards.put(Chomusuke.ID, new Chomusuke());
            Cards.put(Giselle.ID, new Giselle());
            Cards.put(Veldora.ID, new Veldora());
            Cards.put(Rose.ID, new Rose());
            Cards.put(Truth.ID, new Truth());
            Cards.put(Azriel.ID, new Azriel());
            Cards.put(Hero.ID, new Hero());
            Cards.put(SirTouchMe.ID, new SirTouchMe());
            Cards.put(ShikizakiKiki.ID, new ShikizakiKiki());
            Cards.put(HiiragiTenri.ID, new HiiragiTenri());
            Cards.put(JeanneDArc.ID, new JeanneDArc());
            Cards.put(NivaLada.ID, new NivaLada());
            Cards.put(SeriousSaitama.ID, new SeriousSaitama());
            //Cards.put(Cthulhu.ID, new Cthulhu());
            //Cards.put(InfinitePower.ID, new InfinitePower());
        }

        return Cards;
    }

    public static void MarkAsSeen(String cardID)
    {
        if (!IsSeen(cardID))
        {
            UnlockTracker.seenPref.putInteger(cardID, 2);
            UnlockTracker.seenPref.flush();
        }
    }

    public static boolean IsSeen(String cardID)
    {
        return UnlockTracker.seenPref.getInteger(cardID, 0) >= 1;
    }

    @SpireOverride
    protected void renderAttackBg(SpriteBatch sb, float x, float y)
    {
        RenderHelpers.RenderOnCardCentered(sb, this, RENDER_COLOR, ImageMaster.CARD_ATTACK_BG_GRAY, x, y);
    }

    @SpireOverride
    protected void renderSkillBg(SpriteBatch sb, float x, float y)
    {
        RenderHelpers.RenderOnCardCentered(sb, this, RENDER_COLOR, ImageMaster.CARD_SKILL_BG_GRAY, x, y);
    }

    @SpireOverride
    protected void renderPowerBg(SpriteBatch sb, float x, float y)
    {
        RenderHelpers.RenderOnCardCentered(sb, this, RENDER_COLOR, ImageMaster.CARD_POWER_BG_GRAY, x, y);
    }
}