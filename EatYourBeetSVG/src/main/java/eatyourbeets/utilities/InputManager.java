package eatyourbeets.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardText;

public class InputManager
{
    public enum KeyState
    {
        Pressed,
        JustPressed,
        JustReleased,
        Released;

        public boolean IsPressed()
        {
            return this == Pressed;
        }

        public boolean IsJustPressed()
        {
            return this == JustPressed;
        }

        public boolean IsJustReleased()
        {
            return this == JustReleased;
        }

        public boolean IsReleased()
        {
            return this == Released;
        }
    }

    public static KeyState RightClick = KeyState.Released;

    public static void OnControllerKeyPress(int keyCode)
    {
        if (keyCode == 9)
        {
            RightClick = KeyState.JustPressed;
        }
    }

    public static void OnControllerKeyRelease(int keyCode)
    {
        if (keyCode == 9)
        {
            RightClick = KeyState.JustReleased;
        }
    }

    public static void PreUpdate()
    {
        UpdateEYBCardText();
    }

    public static void PostUpdate()
    {
        UpdateRightClick();
    }

    private static void UpdateEYBCardText()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT))
        {
            EYBCardText.Toggled = !EYBCardText.Toggled;
        }
        else if (EYBCardText.ToggledOnce)
        {
            EYBCardText.NewIndex = 1;
            EYBCardText.ToggledOnce = false;
        }
        else if (EYBCardText.Toggled)
        {
            EYBCardText.NewIndex = 1;
        }
        else if (RightClick.IsPressed())
        {
            EYBCardText.NewIndex = 1;
        }
        else
        {
            EYBCardText.NewIndex = 0;
        }
    }

    private static void UpdateRightClick()
    {
        if (Gdx.input.isButtonPressed(1))
        {
            if (RightClick.IsJustPressed())
            {
                RightClick = KeyState.Pressed;
            }
            else if (RightClick.IsReleased() || RightClick.IsJustReleased())
            {
                RightClick = KeyState.JustPressed;
            }
        }
        else if (Settings.isControllerMode)
        {
            if (RightClick.IsJustPressed())
            {
                RightClick = KeyState.Pressed;
            }
            else if (RightClick.IsJustReleased())
            {
                RightClick = KeyState.Released;
            }
        }
        else
        {
            if (RightClick.IsJustReleased())
            {
                RightClick = KeyState.Released;
            }
            else if (RightClick.IsPressed() || RightClick.IsJustPressed())
            {
                RightClick = KeyState.JustReleased;
            }
        }
    }
}
