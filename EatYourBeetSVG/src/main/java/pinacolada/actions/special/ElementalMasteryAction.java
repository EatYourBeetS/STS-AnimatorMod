package pinacolada.actions.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.actions.EYBAction;
import pinacolada.effects.SFX;
import pinacolada.effects.vfx.ShootingStarsEffect;
import pinacolada.powers.special.ElementalExposurePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class ElementalMasteryAction extends EYBAction
{
    private int amount;

    public ElementalMasteryAction(int amount)
    {
        super(ActionType.DAMAGE);

        this.amount = amount;
    }

    @Override
    protected void FirstUpdate()
    {
        PCLActions.Top.VFX(new ShootingStarsEffect(-100 * Settings.scale, player.hb.cY).SetSpread(0,180));
        PCLActions.Top.SFX(SFX.PCL_STAR, 0.9f, 1.1f);
        PCLGameEffects.List.Add(new BorderFlashEffect(Color.CORAL));

        ArrayList<AbstractMonster> enemies = PCLGameUtilities.GetEnemies(true);
        for (AbstractMonster m : enemies)
        {
            PCLActions.Bottom.StackPower(new ElementalExposurePower(m, amount)).IgnoreArtifact(true);
        }

        Complete();
    }

}
