package eatyourbeets.monsters.UnnamedReign.Crystal;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.UnnamedReign.Cube.Moveset.Move_GuardedAttack;
import eatyourbeets.monsters.UnnamedReign.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.MonsterTier;
import eatyourbeets.powers.UnnamedReign.UltimateCrystalPower;

public class UltimateCrystal extends Crystal
{
    public static final String ID = CreateFullID(MonsterShape.Crystal, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Crystal";

    public UltimateCrystal()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        int level = AbstractDungeon.ascensionLevel;

        moveset.add(new Move_GuardedAttack(0, 18, 16, this));
    }

    @Override
    protected void getMove(int i)
    {
        moveset.get(0).SetMove();
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new UltimateCrystalPower(this));

//        CardCrawlGame.music.unsilenceBGM();
//        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrMapNode().room.playBGM("ANIMATOR_THE_ULTIMATE_CRYSTAL.ogg");
        //AbstractDungeon.getCurrRoom().playBgmInstantly("ANIMATOR_THE_ULTIMATE_CRYSTAL.ogg");
    }
}
