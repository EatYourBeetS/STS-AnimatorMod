package eatyourbeets.monsters.PlayerMinions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import eatyourbeets.actions.basic.GainBlock;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Special;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class UnnamedDoll_SummoningRitual extends EYBMonster
{
    private final BobEffect bobEffect = new BobEffect(1);
    private final AbstractPlayer player;

    public UnnamedDoll_SummoningRitual(float x, float y)
    {
        super(new Data(TheUnnamed_Doll.ID), EnemyType.NORMAL, x, y);

        this.player = AbstractDungeon.player;
        this.data.SetIdleAnimation(this, 1);
        this.flipHorizontal = true;
        this.drawX = x;
        this.drawY = y;
        refreshHitboxLocation();
        refreshIntentHbLocation();

        moveset.mode = EYBMoveset.Mode.Random;

        moveset.Normal.Add(new EYBMove_Special())
        .SetIntent(Intent.DEFEND_BUFF)
        .SetOnUse((move, __) ->
        {
            for (int i = 0; i < 3; i++)
            {
                GameActions.Bottom.Add(new GainBlock(player, this, 7)).SetVFX(false, i > 0);
            }
            GameActions.Bottom.GainArtifact(1);
        });

        moveset.Normal.Add(new EYBMove_Special())
        .SetIntent(Intent.BUFF)
        .SetOnUse((move, __) ->
        {
            int alternativeBuff = 3;
            if (GameUtilities.GetPowerAmount(StrengthPower.POWER_ID) < 8)
            {
                GameActions.Bottom.GainStrength(2);
                alternativeBuff -= 1;
            }
            if (GameUtilities.GetPowerAmount(DexterityPower.POWER_ID) < 8)
            {
                GameActions.Bottom.GainDexterity(2);
                alternativeBuff -= 1;
            }
            if (GameUtilities.GetPowerAmount(FocusPower.POWER_ID) < 8)
            {
                GameActions.Bottom.GainFocus(2);
                alternativeBuff -= 1;
            }
            if (alternativeBuff > 0)
            {
                GameActions.Bottom.GainEnergyNextTurn(alternativeBuff);
            }
        });

        moveset.Normal.Add(new EYBMove_Special())
        .SetIntent(Intent.STRONG_DEBUFF)
        .SetOnUse((move, __) ->
        {
            GameActions.Bottom.VFX(new ScreenOnFireEffect(), 0.2f);
            for (int i = 0; i < 3; i++)
            {
                GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(this), 8);
            }
            move.UseAnimation(CreatureAnimation.ATTACK_SLOW);
        });
    }

    @Override
    public void render(SpriteBatch sb)
    {
        animY = this.bobEffect.y;
        super.render(sb);
    }

    @Override
    public void update()
    {
        this.bobEffect.update();
        super.update();
    }

    @Override
    protected void SetNextMove(int roll, int historySize)
    {
        super.SetNextMove(roll, historySize);

        createIntent();
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = 1;
            atlasUrl = "images/animator/monsters/TheUnnamed/TheUnnamedMinion.atlas";
            jsonUrl = "images/animator/monsters/TheUnnamed/TheUnnamedMinion.json";
            scale = 2;

            SetHB(0,-20,120,140, 0, 60);
        }
    }
}
