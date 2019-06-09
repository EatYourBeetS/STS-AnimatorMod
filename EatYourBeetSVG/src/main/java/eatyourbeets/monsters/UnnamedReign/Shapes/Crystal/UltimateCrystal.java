package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.AnimatorResources_Audio;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.SummonMonsterAction;
import eatyourbeets.actions.WaitRealtimeAction;
import eatyourbeets.cards.animator.Crystallize;
import eatyourbeets.effects.CallbackEffect;
import eatyourbeets.monsters.SharedMoveset.Move_AttackDefend;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrengthAndArtifactAll;
import eatyourbeets.monsters.SharedMoveset.Move_ShuffleCard;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.AntiArtifactSlowPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.UnnamedReign.UltimateCrystalPower;

public class UltimateCrystal extends Crystal
{
    public static final String ID = CreateFullID(MonsterShape.Crystal, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Crystal";

    public UltimateCrystal original = null;

    public UltimateCrystal()
    {
        this(0, 0, null);
    }

    public UltimateCrystal(float x, float y, UltimateCrystal original)
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, x, y);

        int level = AbstractDungeon.ascensionLevel;

        movesetMode = Mode.Sequential;

        this.original = original;
        if (original == null)
        {
            moveset.AddNormal(new Move_GainStrengthAndArtifactAll(3, 2));
            moveset.AddNormal(new Move_AttackDefend(1, 8));
            moveset.AddNormal(new Move_ShuffleCard(new Crystallize(), 2));
        }
        else
        {
            moveset.AddNormal(new Move_ShuffleCard(new Crystallize(), 2));
            moveset.AddNormal(new Move_GainStrengthAndArtifactAll(3, 2));
            moveset.AddNormal(new Move_AttackDefend(1, 8));
        }
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        if (original == null)
        {
            GameActionsHelper.ApplyPower(this, this, new UltimateCrystalPower(this, 6), 6);
            GameActionsHelper.ApplyPower(this, this, new AntiArtifactSlowPower(this, 1), 1);

            AbstractDungeon.effectList.add(new CallbackEffect(new WaitRealtimeAction(15),
                    (state, action)-> CardCrawlGame.music.unsilenceBGM(), this));

            CardCrawlGame.sound.play(AnimatorResources_Audio.TheUltimateCrystal);
            //CardCrawlGame.music.playTempBgmInstantly(AnimatorResources_Audio.TheUltimateCrystal, false);
        }
        else
        {
            for (AbstractPower p : original.powers)
            {
                CloneablePowerInterface cloneablePower = Utilities.SafeCast(p, CloneablePowerInterface.class);
                if (cloneablePower != null)
                {
                    AbstractPower power = cloneablePower.makeCopy();
                    if (power != null)
                    {
                        power.amount = p.amount;
                        power.owner = this;
                        this.powers.add(power);
                    }
                }
                else if (p instanceof PoisonPower) // Poison does not implement CloneablePowerInterface...
                {
                    PoisonPower poison = Utilities.SafeCast(p, PoisonPower.class);
                    if (poison != null)
                    {
                        AbstractCreature source = Utilities.<AbstractCreature>GetPrivateField("source", PoisonPower.class).Get(poison);

                        this.powers.add(new PoisonPower(this, source, poison.amount));
                    }
                }
            }

            this.currentHealth = original.currentHealth;
            this.maxHealth = original.maxHealth;
            this.healthBarUpdatedEvent();
        }
    }

    @Override
    public void die()
    {
        super.die();

        if (PlayerStatistics.GetCurrentEnemies(true).isEmpty())
        {
            AbstractDungeon.scene.fadeInAmbiance();
            CardCrawlGame.music.fadeOutTempBGM();
        }
    }

    public void SummonCopy()
    {
        GameActionsHelper.AddToBottom(new SummonMonsterAction(new UltimateCrystal(-330, -12, this), false));
    }
}