package eatyourbeets.ui.screens.animator.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnimatorCharacterSelectScreen
{
    protected static final Logger logger = LogManager.getLogger(AnimatorCharacterSelectScreen.class.getName());

    public static final AnimatorLoadoutRenderer LoadoutRenderer = new AnimatorLoadoutRenderer();
    public static final AnimatorTrophiesRenderer TrophiesRenderer = new AnimatorTrophiesRenderer();
    public static final AnimatorSpecialTrophiesRenderer SpecialTrophiesRenderer = new AnimatorSpecialTrophiesRenderer();
    public static CharacterOption selectedOption;

    public static void Initialize(CharacterSelectScreen selectScreen)
    {
        GameUtilities.UnlockAllKeys();
        selectedOption = null;
    }

    public static void Update(CharacterSelectScreen selectScreen)
    {
        UpdateSelectedCharacter(selectScreen);
        if (selectedOption != null)
        {
            LoadoutRenderer.Update();
            TrophiesRenderer.Update();
            SpecialTrophiesRenderer.Update();
        }
    }

    public static void Render(CharacterSelectScreen selectScreen, SpriteBatch sb)
    {
        if (selectedOption != null)
        {
            LoadoutRenderer.Render(sb);
            TrophiesRenderer.Render(sb);
            SpecialTrophiesRenderer.Render(sb);
        }
    }

    private static void UpdateSelectedCharacter(CharacterSelectScreen selectScreen)
    {
        CharacterOption current = selectedOption;

        selectedOption = null;

        for (CharacterOption o : selectScreen.options)
        {
            if (o.selected)
            {
                if (o.c.chosenClass == GR.Enums.Characters.THE_ANIMATOR)
                {
                    selectedOption = o;

                    if (current != o)
                    {
                        LoadoutRenderer.Refresh(selectScreen, o);
                    }
                }
                else if (o.c.chosenClass == GR.Enums.Characters.THE_UNNAMED)
                {
                    selectScreen.confirmButton.isDisabled = true;
                }

                return;
            }
        }
    }
}
