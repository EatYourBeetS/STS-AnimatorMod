package eatyourbeets.monsters.Bosses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.relics.animator.ExquisiteBloodVial;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class KrulTepes extends EYBMonster
{
    public static final String ID = CreateFullID(KrulTepes.class);

    private final BobEffect bobEffect = new BobEffect(1);

    public KrulTepes()
    {
        super(new Data(ID), EnemyType.BOSS);

        moveset.mode = EYBMoveset.Mode.Sequential;

        moveset.Normal.DefendBuff(18, PowerHelper.Thorns, 5)
        .SetCanUse((m, __) -> GameUtilities.GetPowerAmount(this, RegenPower.POWER_ID) <= 18)
        .SetMiscBonus(8, 1)
        .SetBlockScaling(0.2f);

        moveset.Normal.AttackDebuff(24, PowerHelper.Weak, 2)
        .SetOnUse((m, t) -> GameEffects.List.Add(new BiteEffect(t.hb.cX, t.hb.cY - 40f * Settings.scale, Color.SCARLET.cpy())))
        .SetDamageScaling(0.15f);

        moveset.Normal.Buff(PowerHelper.Strength, 3)
        .SetCanUse((m, __) -> GameUtilities.GetPowerAmount(this, StrengthPower.POWER_ID) <= 9)
        .AddPower(PowerHelper.Artifact, 1);

        moveset.Normal.DefendDebuff(16, PowerHelper.Weak, 2)
        .SetMiscBonus(8, 1)
        .AddPower(PowerHelper.Vulnerable, 2)
        .AddPower(PowerHelper.Frail, 2)
        .SetBlockScaling(0.15f);

        moveset.Normal.Attack(3, 4).SetMisc(12)
        .SetIntent(Intent.ATTACK_BUFF)
        .SetOnUse((m, t) -> GameActions.Bottom.Heal(this, this, m.misc.Calculate()));

        moveset.Normal.AttackDefend(18, 18)
        .SetDamageBonus(6, 3)
        .SetBlockBonus(6, 3);
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

        GameActions.Bottom.StackPower(new IntangiblePlayerPower(this, 1));
        GameActions.Bottom.StackPower(new ArtifactPower(this, 2));

        AbstractPlayer p = AbstractDungeon.player;
        p.drawPile.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.series.OwariNoSeraph.KrulTepes);
        p.discardPile.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.series.OwariNoSeraph.KrulTepes);
        p.hand.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.series.OwariNoSeraph.KrulTepes);

        AbstractDungeon.getCurrMapNode().room.playBGM("BOSS_BOTTOM");
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = AbstractDungeon.ascensionLevel >= 6 ? 377 : 344;
            imgUrl = AnimatorResources.GetMonsterImage(ID);

            SetHB(0,0,200,280);
        }
    }
}