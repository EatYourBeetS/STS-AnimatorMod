package eatyourbeets.monsters.UnnamedReign.Shapes;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.monsters.AbstractMonsterData;
import eatyourbeets.powers.PlayerStatistics;

public class MonsterData_Shape extends AbstractMonsterData
{
    public final MonsterShape shape;
    public final MonsterElement element;
    public final MonsterTier tier;

    public MonsterData_Shape(MonsterShape shape, MonsterElement element, MonsterTier tier)
    {
        super(UnnamedShape.CreateFullID(shape, element, tier));

        this.element = element;
        this.shape = shape;
        this.tier = tier;

        String filePath = "images/monsters/animator/" + shape + "/" + shape + "_" + element + "_" + tier.GetId();
        atlasUrl = filePath + ".atlas";
        jsonUrl = filePath + ".json";
        imgUrl = filePath + ".png";

        offsetY = 70;

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

        if (tier != MonsterTier.Ultimate)
        {
            maxHealth += AbstractDungeon.monsterHpRng.random(-4, 4);
        }

        if (PlayerStatistics.GetAscensionLevel() >= 7)
        {
            maxHealth = Math.round(maxHealth * 1.1f);
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
                offsetY = 40;
                scale = 0.9f;
                hb_y = -30f;
                hb_w = 140;
                hb_h = 140;
                maxHealth = 155;
                break;
            }

            case Advanced:
            {
                scale = 0.5f;
                hb_y = -60f;
                hb_w = 220;
                hb_h = 220;
                maxHealth = 205;
                break;
            }

            case Ultimate:
            {
                scale = 0.4f;
                hb_y = -60f;
                hb_w = 230;
                hb_h = 230;
                maxHealth = 640;
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
                offsetY = 55;
                hb_y = -60;
                hb_w = 140;
                hb_h = 140;
                maxHealth = 155;
                break;
            }

            case Advanced:
            {
                scale = 0.5f;
                hb_y = -50;
                hb_w = 220;
                hb_h = 220;
                maxHealth = 215;
                break;
            }

            case Ultimate:
            {
                scale = 1.3f;
                hb_y = -50;
                hb_w = 220;
                hb_h = 130;
                maxHealth = 680;
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
                maxHealth = 80;
                break;
            }

            case Normal:
            {
                scale = 0.8f;
                offsetY = 40;
                hb_y = -30;
                hb_w = 140;
                hb_h = 140;
                maxHealth = 110;
                break;
            }

            case Advanced:
            {
                scale = 0.4f;
                hb_y = -60;
                hb_w = 220;
                hb_h = 220;
                maxHealth = 145;
                break;
            }

            case Ultimate:
            {
                scale = 0.4f;
                hb_y = -60f;
                hb_w = 240;
                hb_h = 240;
                maxHealth = 620;
                break;
            }
        }
    }
}