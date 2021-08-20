package eatyourbeets.actions.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.vfx.ShootingStarsEffect;
import eatyourbeets.powers.animator.ElementalExposurePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

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
        GameActions.Top.VFX(new ShootingStarsEffect(-170, player.hb.cY).SetSpread(0,110));
        GameActions.Top.SFX(SFX.ANIMATOR_STAR, 0.9f, 1.1f);
        GameEffects.List.Add(new BorderFlashEffect(Color.CORAL));

        ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
        for (AbstractMonster m : enemies)
        {
            GameActions.Bottom.StackPower(new ElementalExposurePower(m, amount)).IgnoreArtifact(true);
        }

        Complete();
    }

}
