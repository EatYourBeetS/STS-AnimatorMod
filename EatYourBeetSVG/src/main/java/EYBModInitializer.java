import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.InputManager;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

@SpireInitializer //
public class EYBModInitializer implements OnStartBattleSubscriber, PostBattleSubscriber, PostDrawSubscriber, PostDeathSubscriber,
                                          PreStartGameSubscriber, PostUpdateSubscriber, PostRenderSubscriber
{
    private static final EYBModInitializer instance = new EYBModInitializer();
    private static GUI_TextBox testModeLabel;

    public static void initialize()
    {
        ArrayList<OnStartBattleSubscriber> battleStart = JUtils.<ArrayList<OnStartBattleSubscriber>>
        GetField("startBattleSubscribers", BaseMod.class).Get(null);

        BaseMod.subscribe(instance);
        battleStart.remove(instance);
        battleStart.add(0, instance);
        GR.Initialize();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom)
    {
        CombatStats.OnBattleStart();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom)
    {
        CombatStats.OnBattleEnd();
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard)
    {
        CombatStats.OnAfterDraw(abstractCard);
    }

    @Override
    public void receivePreStartGame()
    {
        CombatStats.OnGameStart();
    }

    @Override
    public void receivePostDeath()
    {
        CombatStats.OnAfterDeath();
    }

    @Override
    public void receivePostUpdate()
    {
        InputManager.PostUpdate();
    }

    @Override
    public void receivePostRender(SpriteBatch sb)
    {
        if (GR.TEST_MODE)
        {
            if (testModeLabel == null)
            {
                testModeLabel = new GUI_TextBox(GR.Common.Images.Panel.Texture(),
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