package eatyourbeets.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import eatyourbeets.resources.animator.AnimatorResources;

public abstract class EYBMonsterData
{
    public final String id;
    public final MonsterStrings strings;
    public int maxHealth;
    public float scale = 1f;
    public float hb_x;
    public float hb_y;
    public float hb_w;
    public float hb_h;
    public float offsetX;
    public float offsetY;
    public String atlasUrl;
    public String jsonUrl;
    public String imgUrl;

    public EYBMonsterData(String id)
    {
        this.id = id;
        this.strings = AnimatorResources.GetMonsterStrings(id);
    }

    protected void SetHB(float hb_x, float hb_y, float hb_w, float hb_h, float x, float y)
    {
        this.hb_x = hb_x;
        this.hb_y = hb_y;
        this.hb_w = hb_w;
        this.hb_h = hb_h;
        this.offsetX = x;
        this.offsetY = y;
    }

    protected void SetHB(float hb_x, float hb_y, float hb_w, float hb_h)
    {
        this.hb_x = hb_x;
        this.hb_y = hb_y;
        this.hb_w = hb_w;
        this.hb_h = hb_h;
    }

    public void SetIdleAnimation(EYBMonster monster, float timeScale)
    {
        SetAnimation(monster, "Idle", timeScale);
    }

    public void SetAnimation(EYBMonster monster, String name)
    {
        SetAnimation(monster, name, 1);
    }

    public void SetAnimation(EYBMonster monster, String name, float timeScale)
    {
        monster.loadAnimation(atlasUrl, jsonUrl, scale);
        AnimationState.TrackEntry e = monster.state.setAnimation(0, name, true);
        e.setTimeScale(timeScale);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void SetMaxHP(int minHP, int maxHP)
    {
        maxHealth = AbstractDungeon.monsterHpRng.random(minHP, maxHP);
    }
}
