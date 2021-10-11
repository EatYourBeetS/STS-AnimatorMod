package eatyourbeets.cards.animator.beta.ultrarare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.PetalEffect;
import eatyourbeets.actions.special.KillCharacterAction;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class YuyukoSaigyouji extends AnimatorCard_UltraRare implements StartupCard
{
    public static final EYBCardData DATA = Register(YuyukoSaigyouji.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.TouhouProject);

    public YuyukoSaigyouji()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Water(1, 0, 0);
        SetAffinity_Dark(2, 0, 0);
        SetCostUpgrade(-1);
        GraveField.grave.set(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(new VFXAction(new CherryBlossomEffect(), 0.7F));
        GameActions.Bottom.ApplyPower(new DeathTouch(p));
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (GameUtilities.InBossRoom())
        {
            GameActions.Bottom.Purge(this);

            return true;
        }

        return false;
    }

    public static class DeathTouch extends AnimatorPower
    {
        public DeathTouch(AbstractCreature owner)
        {
            super(owner, YuyukoSaigyouji.DATA);
            updateDescription();
        }

        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
        {
            if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
            {
                AbstractMonster mo = JUtils.SafeCast(target, AbstractMonster.class);
                if (mo != null && mo.type != AbstractMonster.EnemyType.BOSS)
                {
                    this.flash();
                    GameActions.Bottom.Add(new KillCharacterAction(mo, mo));
                }

            }
        }
    }

    public static class CherryBlossomEffect extends AbstractGameEffect
    {
        private float timer = 0.1F;

        public CherryBlossomEffect()
        {
            this.duration = 2.0F;
        }

        @Override
        public void update()
        {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.timer -= Gdx.graphics.getDeltaTime();
            if (this.timer < 0.0F)
            {
                this.timer += 0.1F;
                AbstractDungeon.effectsQueue.add(new PetalEffect());
                AbstractDungeon.effectsQueue.add(new PetalEffect());
            }

            if (this.duration < 0.0F)
            {
                this.isDone = true;
            }
        }

        public void render(SpriteBatch sb)
        {
        }

        public void dispose()
        {
        }
    }
}