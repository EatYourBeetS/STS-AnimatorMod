package eatyourbeets.monsters.UnnamedReign.Cultist;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import eatyourbeets.AnimatorResources;

public abstract class TheUnnamed_Cultist extends CustomMonster
{
    private static final String MODEL_ATLAS = "images/monsters/Animator_TheUnnamed/TheUnnamedCultist.atlas";
    private static final String MODEL_JSON = "images/monsters/Animator_TheUnnamed/TheUnnamedCultist.json";

    public static final String ID = "Animator_TheUnnamedCultist";
    public static final String NAME = "Cultist";

    private static int GetBaseMaxHealth()
    {
        return 180;
    }

    public TheUnnamed_Cultist(float x, float y)
    {
        super(NAME, ID, GetBaseMaxHealth(), 0.0F, -30.0F, 140.0F, 210.0f, null, x, y + 80.0F);

        loadAnimation(MODEL_ATLAS, MODEL_JSON, 2);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.strings = AnimatorResources.GetMonsterStrings(ID);
        this.type = EnemyType.NORMAL;
    }

    public MonsterStrings strings;
}
