package eatyourbeets.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import eatyourbeets.cards.animatorClassic.basic.Defend;
import eatyourbeets.cards.animatorClassic.basic.ImprovedDefend;
import eatyourbeets.cards.animatorClassic.basic.ImprovedStrike;
import eatyourbeets.cards.animatorClassic.basic.Strike;
import eatyourbeets.effects.SFX;
import eatyourbeets.relics.animatorClassic.LivingPicture;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class AnimatorClassicCharacter extends EYBPlayerCharacter
{
    public static final CharacterStrings characterStrings = GR.GetCharacterStrings("AnimatorClassic");
    public static final String[] NAMES = characterStrings.NAMES;
    public static final String[] TEXT = characterStrings.TEXT;
    public static final String ORIGINAL_NAME = NAMES[0];
    public static final String OVERRIDE_NAME = NAMES.length > 1 ? NAMES[1] : ORIGINAL_NAME; // Support for Beta/Alt

    public static final int BASE_HP = 50;
    public static final int BASE_GOLD = 99;

    public AnimatorClassicCharacter()
    {
        super(ORIGINAL_NAME, GR.AnimatorClassic.PlayerClass, GR.AnimatorClassic.Images.ORB_TEXTURES, GR.AnimatorClassic.Images.ORB_VFX_PNG);

        initializeClass(null, GR.AnimatorClassic.Images.SHOULDER2_PNG, GR.AnimatorClassic.Images.SHOULDER1_PNG, GR.AnimatorClassic.Images.CORPSE_PNG,
        getLoadout(), 0f, -5f, 240f, 244f, new EnergyManager(3));

        reloadAnimation();
    }

    public void reloadAnimation()
    {
        super.reloadAnimation(GR.AnimatorClassic.Images.SKELETON_ATLAS, GR.AnimatorClassic.Images.SKELETON_JSON);
    }

    @Override
    public String getLocalizedCharacterName()
    {
        return ORIGINAL_NAME;
    }

    @Override
    public String getTitle(PlayerClass playerClass) // Top panel title
    {
        return OVERRIDE_NAME;
    }

    @Override
    public AbstractPlayer newInstance()
    {
        return new AnimatorClassicCharacter();
    }

    @Override
    public Color getSlashAttackColor()
    {
        return Color.SKY;
    }

    @Override
    public Color getCardTrailColor()
    {
        return GR.AnimatorClassic.RenderColor.cpy();
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
        final ArrayList<String> cards = new ArrayList<>();
        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(ImprovedStrike.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(ImprovedDefend.DATA.ID);
        return cards;
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        final ArrayList<String> relics = new ArrayList<>();
        relics.add(LivingPicture.ID);
        return relics;
    }

    @Override
    public AbstractCard getStartCardForEvent()
    {
        return new Strike();
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return new CharSelectInfo(NAMES[0], TEXT[0], BASE_HP, BASE_HP, 0, BASE_GOLD, 5, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return GR.AnimatorClassic.CardColor;
    }

    @Override
    public Color getCardRenderColor()
    {
        return GR.AnimatorClassic.RenderColor.cpy();
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