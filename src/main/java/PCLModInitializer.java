import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.ui.controls.GUI_TextBox;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLInputManager;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

@SpireInitializer //
public class PCLModInitializer implements OnStartBattleSubscriber, PostBattleSubscriber, PostDeathSubscriber,
                                          PreStartGameSubscriber, PostUpdateSubscriber, PostRenderSubscriber
{
    private static final PCLModInitializer instance = new PCLModInitializer();
    private static GUI_TextBox testModeLabel;

    public static void initialize()
    {
        ArrayList<OnStartBattleSubscriber> battleStart = PCLJUtils.<ArrayList<OnStartBattleSubscriber>>
        GetField("startBattleSubscribers", BaseMod.class).Get(null);

        BaseMod.subscribe(instance);
        battleStart.remove(instance);
        battleStart.add(0, instance);
        GR.Initialize();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom)
    {
        PCLCombatStats.OnBattleStart();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom)
    {
        PCLCombatStats.OnBattleEnd();
    }

    @Override
    public void receivePreStartGame()
    {
        PCLCombatStats.OnGameStart();
    }

    @Override
    public void receivePostDeath()
    {
        PCLCombatStats.OnAfterDeath();
    }

    @Override
    public void receivePostUpdate()
    {
        PCLInputManager.PostUpdate();
    }

    @Override
    public void receivePostRender(SpriteBatch sb)
    {
        if (GR.TEST_MODE)
        {
            if (testModeLabel == null)
            {
                testModeLabel = new GUI_TextBox(GR.PCL.Images.Panel.Texture(),
                new AdvancedHitbox(Settings.WIDTH * 0.16f, Settings.HEIGHT * 0.12f))
                .SetPosition(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.85f)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.buttonLabelFont, 1.5f)
                .SetText("TEST MODE");
            }

            if (GR.UI.Elapsed(40))
            {
                testModeLabel.SetActive(true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_7))
            {
                testModeLabel.SetActive(false);
            }

            testModeLabel.TryRender(sb);
        }
    }
}