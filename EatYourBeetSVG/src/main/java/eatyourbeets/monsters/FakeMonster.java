package eatyourbeets.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FakeMonster extends EYBMonster
{
    public static final String ID = CreateFullID(FakeMonster.class);

    public FakeMonster(float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        this.data.SetIdleAnimation(this, 1);

        moveset.Normal.Attack(0);
        moveset.Normal.Defend(0);
    }

    @Override
    public void createIntent()
    {

    }

    @Override
    public void update()
    {

    }

    @Override
    public void render(SpriteBatch sb)
    {

    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = 999;
            atlasUrl = "images/animator/monsters/TheUnnamed/Hat.atlas";
            jsonUrl = "images/animator/monsters/TheUnnamed/Hat.json";
            scale = 1.6f;

            SetHB(0,0,100,140, 0, 50);
        }
    }
}
