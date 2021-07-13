package eatyourbeets.monsters.UnnamedReign.UnnamedDoll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import eatyourbeets.actions.basic.GainBlock;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Special;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.TargetHelper;

public class TheUnnamed_Doll_Player extends EYBMonster
{
    private final BobEffect bobEffect = new BobEffect(1);
    private final AbstractPlayer player;

    public TheUnnamed_Doll_Player(float x, float y)
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
            GameActions.Bottom.Add(new GainBlock(player, this, 24));
            GameActions.Bottom.GainArtifact(1);
        });

        moveset.Normal.Add(new EYBMove_Special())
        .SetIntent(Intent.BUFF)
        .SetOnUse((move, __) ->
        {
            GameActions.Bottom.GainIntellect(12);
            GameActions.Bottom.GainAgility(12);
            GameActions.Bottom.GainForce(12);
            GameActions.Bottom.GainBlessing(12);
            GameActions.Bottom.GainCorruption(12);
        });

        moveset.Normal.Add(new EYBMove_Special())
        .SetIntent(Intent.STRONG_DEBUFF)
        .SetOnUse((move, __) ->
        {
            move.UseAnimation(CreatureAnimation.ATTACK_SLOW);
            GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(this), 24)
            .AddCallback(___ -> GameEffects.List.Add(new ScreenOnFireEffect()));
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
            atlasUrl = "images/monsters/animator/TheUnnamed/TheUnnamedMinion.atlas";
            jsonUrl = "images/monsters/animator/TheUnnamed/TheUnnamedMinion.json";
            scale = 2;

            SetHB(0,-20,120,140, 0, 60);
        }
    }
}
