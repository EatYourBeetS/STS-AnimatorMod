package eatyourbeets.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import eatyourbeets.resources.Resources_Animator;

public abstract class AbstractMonsterData
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

    public AbstractMonsterData(String id)
    {
        this.id = id;
        this.strings = Resources_Animator.GetMonsterStrings(id);
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

    public void SetIdleAnimation(AnimatorMonster monster, float timeScale)
    {
        SetAnimation(monster, "Idle", 1);
    }

    public void SetAnimation(AnimatorMonster monster, String name)
    {
        SetAnimation(monster, name, 1);
    }

    public void SetAnimation(AnimatorMonster monster, String name, float timeScale)
    {
        monster.loadAnimation(atlasUrl, jsonUrl, scale);
        AnimationState.TrackEntry e = monster.state.setAnimation(0, name, true);
        e.setTimeScale(timeScale);
        e.setTime(e.getEndTime() * MathUtils.random());
    }
}
