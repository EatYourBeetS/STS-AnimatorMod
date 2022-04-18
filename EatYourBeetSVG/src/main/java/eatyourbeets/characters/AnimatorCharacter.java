package eatyourbeets.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.effects.SFX;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class AnimatorCharacter extends EYBPlayerCharacter
{
    public static final CharacterStrings characterStrings = AnimatorResources.GetCharacterStrings("Animator");
    public static final String[] NAMES = characterStrings.NAMES;
    public static final String[] TEXT = characterStrings.TEXT;
    public static final String ORIGINAL_NAME = NAMES[0];
    public static final String OVERRIDE_NAME = NAMES.length > 1 ? NAMES[1] : ORIGINAL_NAME; // Support for Beta/Alt

    public AnimatorCharacter()
    {
        super(ORIGINAL_NAME, GR.Animator.PlayerClass, GR.Animator.Images.ORB_TEXTURES, GR.Animator.Images.ORB_VFX_PNG);

        initializeClass(null, GR.Animator.Images.SHOULDER2_PNG, GR.Animator.Images.SHOULDER1_PNG, GR.Animator.Images.CORPSE_PNG,
        getLoadout(), 0f, -5f, 240f, 244f, new EnergyManager(3));

        reloadAnimation();
    }

    public void reloadAnimation()
    {
        super.reloadAnimation(GR.Animator.Images.SKELETON_ATLAS, GR.Animator.Images.SKELETON_JSON);
    }

    @Override
    public String getLocalizedCharacterName()
    {
        return ORIGINAL_NAME;
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) // Top panel title
    {
        return OVERRIDE_NAME;
    }

    @Override
    public AbstractPlayer newInstance()
    {
        return new AnimatorCharacter();
    }

    @Override
    public Color getSlashAttackColor()
    {
        return Color.SKY;
    }

    @Override
    public Color getCardTrailColor()
    {
        return GR.Animator.RenderColor.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss()
    {
        return AnimatorLoadout.BASE_HP / 10;
    }

    @Override
    public BitmapFont getEnergyNumFont()
    {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey()
    {
        return SFX.TINGSHA;
    }

    @Override
    public String getPortraitImageName()
    {
        return null; // Updated in AnimatorCharacterSelectScreen
    }

    @Override
    public ArrayList<String> getStartingDeck()
    {
        return PrepareLoadout().GetStartingDeck();
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        return PrepareLoadout().GetStartingRelics();
    }

    @Override
    public AbstractCard getStartCardForEvent()
    {
        return new Strike();
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return PrepareLoadout().GetLoadout(NAMES[0], TEXT[0], this);
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return GR.Animator.CardColor;
    }

    @Override
    public Color getCardRenderColor()
    {
        return GR.Animator.RenderColor.cpy();
    }

    protected AnimatorLoadout PrepareLoadout()
    {
        int unlockLevel = GR.Animator.GetUnlockLevel();
        if (unlockLevel < GR.Animator.Data.SelectedLoadout.UnlockLevel)
        {
            final RandomizedList<AnimatorLoadout> list = new RandomizedList<>();
            for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
            {
                if (unlockLevel >= loadout.UnlockLevel)
                {
                    list.Add(loadout);
                }
            }

            GR.Animator.Data.SelectedLoadout = list.Retrieve(new com.megacrit.cardcrawl.random.Random());
        }

        return GR.Animator.Data.SelectedLoadout;
    }
}