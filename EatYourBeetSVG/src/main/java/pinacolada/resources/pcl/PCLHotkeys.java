package pinacolada.resources.pcl;

import com.badlogic.gdx.Input;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;

import java.util.HashMap;

public class PCLHotkeys
{

    public static final HashMap<Integer, Integer> EQUIVALENT_KEYS = new HashMap<>();

    private static final String KEYMAP_CONTROL_PILE_CHANGE = PCLConfig.CreateFullID("ControlPileChange");
    private static final String KEYMAP_CONTROL_PILE_SELECT = PCLConfig.CreateFullID("ControlPileSelect");
    private static final String KEYMAP_CYCLE = PCLConfig.CreateFullID("Cycle");
    private static final String KEYMAP_REROLL_CURRENT = PCLConfig.CreateFullID("RerollCurrent");
    private static final String KEYMAP_REROLL_NEXT = PCLConfig.CreateFullID("RerollNext");

    static {
        EQUIVALENT_KEYS.put(Input.Keys.ALT_LEFT, Input.Keys.ALT_RIGHT);
        EQUIVALENT_KEYS.put(Input.Keys.ALT_RIGHT, Input.Keys.ALT_LEFT);
        EQUIVALENT_KEYS.put(Input.Keys.CONTROL_LEFT, Input.Keys.CONTROL_RIGHT);
        EQUIVALENT_KEYS.put(Input.Keys.CONTROL_RIGHT, Input.Keys.CONTROL_LEFT);
        EQUIVALENT_KEYS.put(Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT);
        EQUIVALENT_KEYS.put(Input.Keys.SHIFT_RIGHT, Input.Keys.SHIFT_LEFT);
    }

    public static InputAction controlPileChange;
    public static InputAction controlPileSelect;
    public static InputAction cycle;
    public static InputAction rerollCurrent;
    public static InputAction rerollNext;

    public static void load()
    {
        controlPileChange = new InputAction(InputActionSet.prefs.getInteger(KEYMAP_CONTROL_PILE_CHANGE, Input.Keys.T));
        controlPileSelect = new InputAction(InputActionSet.prefs.getInteger(KEYMAP_CONTROL_PILE_SELECT, Input.Keys.Y));
        cycle = new InputAction(InputActionSet.prefs.getInteger(KEYMAP_CYCLE, Input.Keys.CONTROL_LEFT));
        rerollCurrent = new InputAction(InputActionSet.prefs.getInteger(KEYMAP_REROLL_CURRENT, Input.Keys.Q));
        rerollNext = new InputAction(InputActionSet.prefs.getInteger(KEYMAP_REROLL_NEXT, Input.Keys.W));
    }

    public static void save()
    {
        InputActionSet.prefs.putInteger(KEYMAP_CONTROL_PILE_CHANGE, controlPileChange.getKey());
        InputActionSet.prefs.putInteger(KEYMAP_CONTROL_PILE_SELECT, controlPileSelect.getKey());
        InputActionSet.prefs.putInteger(KEYMAP_CYCLE, cycle.getKey());
        InputActionSet.prefs.putInteger(KEYMAP_REROLL_CURRENT, rerollCurrent.getKey());
        InputActionSet.prefs.putInteger(KEYMAP_REROLL_NEXT, rerollNext.getKey());
    }

    public static void resetToDefaults()
    {
        controlPileChange.remap(Input.Keys.T);
        controlPileSelect.remap(Input.Keys.Y);
        cycle.remap(Input.Keys.CONTROL_LEFT);
        rerollCurrent.remap(Input.Keys.Q);
        rerollNext.remap(Input.Keys.W);
    }
}