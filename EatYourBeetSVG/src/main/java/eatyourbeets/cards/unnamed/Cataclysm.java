package eatyourbeets.cards.unnamed;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.actions.common.DecreaseMaxHpAction;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.effects.CataclysmEffect;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class Cataclysm extends UnnamedCard
{
    public static final String ID = CreateFullID(Cataclysm.class.getSimpleName());

    public Cataclysm()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL);

        Initialize(12, 0, 8, 8);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new DecreaseMaxHpAction(p, secondaryValue));

        ArrayList<AbstractMonster> characters = PlayerStatistics.GetCurrentEnemies(true);

        GameActionsHelper.AddToBottom(new VFXAction(new BorderLongFlashEffect(Color.RED)));
        //GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.5f));
        for (int i = 0; i < magicNumber; i++)
        {
            RandomizedList<AbstractMonster> enemies = new RandomizedList<>(characters);
            while (enemies.Count() > 0)
            {
                AbstractMonster c = enemies.Retrieve(AbstractDungeon.miscRng);
                //ImpactEffect(c);
                float width = c.hb.width / 2f;
                float height = c.hb.height / 2f;
                float x = c.hb.cX + AbstractDungeon.miscRng.random(-width, width);
                float y = c.hb.cY + AbstractDungeon.miscRng.random(-height, height);

                GameActionsHelper.VFX(new CataclysmEffect(x, y));
                GameActionsHelper.DamageTarget(p, c, this, AbstractGameAction.AttackEffect.NONE, true);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(-4);
        }
    }

//    private void ImpactEffect(AbstractCreature target)
//    {
//        float x = target.hb.cX + AbstractDungeon.miscRng.random(-40, 40);
//        float y = target.hb.cY + AbstractDungeon.miscRng.random(-40, 40);
//
//        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.5f));
//        //GameActionsHelper.AddToBottom(new VFXAction(new WeightyImpactEffect(x, y)));
//        GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(target.hb.cX + target.hb.width / 4.0F, target.hb.cY - target.hb.height / 4.0F)));
//        GameActionsHelper.AddToBottom(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
//        //GameActionsHelper.AddToBottom(new WaitAction(0.1f));
//
//        for (int i = 0; i < 3; i++)
//        {
//            ExplosionEffect(target);
//        }
//    }
//
//    private void ExplosionEffect(AbstractCreature target)
//    {
//        float x = target.hb.cX + AbstractDungeon.miscRng.random(-40, 40);
//        float y = target.hb.cY + AbstractDungeon.miscRng.random(-40, 40);
//        //GameActionsHelper.AddToBottom(new WaitAction(0.1f));
//        GameActionsHelper.AddToBottom(new VFXAction(new ExplosionSmallEffect(x, y), 0F));
//    }
}