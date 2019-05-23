package eatyourbeets.monsters.Bosses;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.Bosses.KrulTepesMoveset.*;
import eatyourbeets.relics.ExquisiteBloodVial;

import java.util.ArrayList;

public class KrulTepes extends CustomMonster
{
    public static final String ID = "Animator_KrulTepes";
    public static final String NAME = "Krul Tepes";

    private final BobEffect bobEffect = new BobEffect(1);
    private final ArrayList<AbstractMove> moveset = new ArrayList<>();

    private static int GetMaxHealth()
    {
        return AbstractDungeon.ascensionLevel >= 6 ? 366 : 344;
    }

    public KrulTepes()
    {
        super(NAME, ID, GetMaxHealth(), 0.0F, 0.0F, 200.0F, 280.0F, AnimatorResources.GetMonsterImage(ID));

        this.type = EnemyType.BOSS;

        int level = AbstractDungeon.ascensionLevel;

        moveset.add(new Move_Regenerate(0, level, this));
        moveset.add(new Move_Bite(1, level, this));
        moveset.add(new Move_GuardedAttack(2, level, this));
        moveset.add(new Move_MultiSlash(3, level, this));
        moveset.add(new Move_PowerUp(4, level, this));
        moveset.add(new Move_Cripple(5, level, this));
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
    public void die()
    {
        super.die();

        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        playBossStinger();

        ExquisiteBloodVial exquisiteBloodVial = (ExquisiteBloodVial)AbstractDungeon.player.getRelic(ExquisiteBloodVial.ID);
        if (exquisiteBloodVial != null)
        {
            exquisiteBloodVial.UnlockPotential();
        }
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new IntangiblePlayerPower(this, 1), 1);
        GameActionsHelper.ApplyPower(this, this, new ArtifactPower(this, 2), 2);

        AbstractPlayer p = AbstractDungeon.player;
        p.drawPile.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.KrulTepes);
        p.discardPile.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.KrulTepes);
        p.hand.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.KrulTepes);

        AbstractDungeon.getCurrMapNode().room.playBGM("BOSS_BOTTOM");
    }

    @Override
    public void takeTurn()
    {
        moveset.get(nextMove).Execute(AbstractDungeon.player);

        GameActionsHelper.AddToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i)
    {
        int size = moveHistory.size();
        if (size <= 12 && size % 3 == 0)
        {
            moveset.get(0).SetMove();
            return;
        }

        Byte previousMove = moveHistory.get(size - 1);

        ArrayList<AbstractMove> moves = new ArrayList<>();
        for (AbstractMove move : moveset)
        {
            if (move.CanUse(previousMove))
            {
                moves.add(move);
            }
        }

        moves.get(i % moves.size()).SetMove();
    }
}