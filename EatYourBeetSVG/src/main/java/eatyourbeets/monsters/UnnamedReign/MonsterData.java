package eatyourbeets.monsters.UnnamedReign;

import com.megacrit.cardcrawl.localization.MonsterStrings;
import eatyourbeets.AnimatorResources;

public class MonsterData
{
    public String id;
    public MonsterStrings strings;
    public MonsterShape shape;
    public MonsterElement element;
    public MonsterTier tier;
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

    public MonsterData(MonsterShape shape, MonsterElement element, MonsterTier tier)
    {
        this.shape = shape;
        this.element = element;
        this.tier = tier;

        id = UnnamedMonster.CreateFullID(shape, element, tier);

        String filePath = "images/monsters/Animator_" + shape + "/" + shape + "_" + element + "_" + tier.GetId();
        atlasUrl = filePath + ".atlas";
        jsonUrl = filePath + ".json";

        imgUrl = UnnamedMonster.GetResourcePath(shape, element, tier) + ".png";
        strings = AnimatorResources.GetMonsterStrings(id);
        offsetY = 80;

        switch (shape)
        {
            case Cube:
                SetupCube(tier);
                break;

            case Crystal:
                SetupCrystal(tier);
                break;

            case Wisp:
                SetupWisp(tier);
                break;
        }
    }

    private void SetupCube(MonsterTier tier)
    {
        switch (tier)
        {
            case Small:
            {
                hb_w = 120;
                hb_h = 120;
                maxHealth = 120;
                break;
            }

            case Normal:
            {
                scale = 0.9f;
                hb_y = -30f;
                hb_w = 140;
                hb_h = 140;
                maxHealth = 160;
                break;
            }

            case Advanced:
            {
                scale = 0.5f;
                hb_y = -60f;
                hb_w = 220;
                hb_h = 220;
                maxHealth = 200;
                break;
            }

            case Ultimate:
            {
                scale = 0.4f;
                hb_y = -60f;
                hb_w = 230;
                hb_h = 230;
                maxHealth = 600;
                break;
            }
        }
    }

    private void SetupCrystal(MonsterTier tier)
    {
        switch (tier)
        {
            case Small:
            {
                hb_w = 110;
                hb_h = 110;
                maxHealth = 120;
                break;
            }

            case Normal:
            {
                scale = 1.4f;
                hb_y = -60;
                hb_w = 140;
                hb_h = 140;
                maxHealth = 160;
                break;
            }

            case Advanced:
            {
                scale = 0.5f;
                hb_y = -50;
                hb_w = 220;
                hb_h = 220;
                maxHealth = 200;
                break;
            }

            case Ultimate:
            {
                scale = 1.2f;
                hb_y = -50;
                hb_w = 220;
                hb_h = 130;
                maxHealth = 600;
                break;
            }
        }
    }

    private void SetupWisp(MonsterTier tier)
    {
        switch (tier)
        {
            case Small:
            {
                hb_w = 240;
                hb_h = 240;
                maxHealth = 120;
                break;
            }

            case Normal:
            {
                scale = 0.8f;
                hb_y = -30;
                hb_w = 140;
                hb_h = 140;
                maxHealth = 160;
                break;
            }

            case Advanced:
            {
                scale = 0.4f;
                hb_y = -60;
                hb_w = 220;
                hb_h = 220;
                maxHealth = 200;
                break;
            }

            case Ultimate:
            {
                scale = 0.4f;
                hb_y = -60f;
                hb_w = 240;
                hb_h = 240;
                maxHealth = 600;
                break;
            }
        }
    }
}
