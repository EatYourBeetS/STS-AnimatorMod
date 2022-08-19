package eatyourbeets.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.megacrit.cardcrawl.core.Settings;

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
    public static KeyState LeftClick = KeyState.Released;
    public static KeyState Control = KeyState.Released;
    public static KeyState Shift = KeyState.Released;

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

    public static void PostUpdate()
    {
        UpdateLeftClick();
        UpdateRightClick();
        UpdateControl();
        UpdateShift();
    }

    private static void UpdateLeftClick()
    {
        if (Gdx.input.isButtonPressed(0))
        {
            if (LeftClick.IsJustPressed())
            {
                LeftClick = KeyState.Pressed;
            }
            else if (LeftClick.IsReleased() || LeftClick.IsJustReleased())
            {
                LeftClick = KeyState.JustPressed;
            }
        }
        else
        {
            if (LeftClick.IsJustReleased())
            {
                LeftClick = KeyState.Released;
            }
            else if (LeftClick.IsPressed() || LeftClick.IsJustPressed())
            {
                LeftClick = KeyState.JustReleased;
            }
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

    private static void UpdateControl()
    {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))
        {
            if (Control.IsJustPressed())
            {
                Control = KeyState.Pressed;
            }
            else if (Control.IsReleased() || Control.IsJustReleased())
            {
                Control = KeyState.JustPressed;
            }
        }
        else
        {
            if (Control.IsJustReleased())
            {
                Control = KeyState.Released;
            }
            else if (Control.IsPressed() || Control.IsJustPressed())
            {
                Control = KeyState.JustReleased;
            }
        }
    }

    private static void UpdateShift()
    {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
        {
            if (Shift.IsJustPressed())
            {
                Shift = KeyState.Pressed;
            }
            else if (Shift.IsReleased() || Shift.IsJustReleased())
            {
                Shift = KeyState.JustPressed;
            }
        }
        else
        {
            if (Shift.IsJustReleased())
            {
                Shift = KeyState.Released;
            }
            else if (Shift.IsPressed() || Shift.IsJustPressed())
            {
                Shift = KeyState.JustReleased;
            }
        }
    }
}
