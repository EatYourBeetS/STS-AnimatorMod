package eatyourbeets.cards.unnamed;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.actions._legacy.common.DecreaseMaxHpAction;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.effects.CataclysmEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class Devastation extends UnnamedCard
{
    public static final String ID = Register(Devastation.class.getSimpleName());

    public Devastation()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL);

        Initialize(16, 0, 8, 16);

        SetMastery(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper_Legacy.AddToBottom(new DecreaseMaxHpAction(p, secondaryValue));

        ArrayList<AbstractMonster> characters = GameUtilities.GetCurrentEnemies(true);

        GameActions.Bottom.VFX(new BorderFlashEffect(Color.ORANGE));
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.2F);
        GameActions.Bottom.VFX((new BorderLongFlashEffect(Color.RED)));
        //GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f));
        for (int i = 0; i < magicNumber; i++)
        {
            RandomizedList<AbstractMonster> enemies = new RandomizedList<>(characters);
            while (enemies.Count() > 0)
            {
                AbstractMonster c = enemies.Retrieve(AbstractDungeon.cardRandomRng);
                //ImpactEffect(c);
                float width = c.hb.width / 2f;
                float height = c.hb.height / 2f;
                float x = c.hb.cX + AbstractDungeon.cardRandomRng.random(-width, width);
                float y = c.hb.cY + AbstractDungeon.cardRandomRng.random(-height, height);

                GameActionsHelper_Legacy.VFX(new CataclysmEffect(x, y));
                GameActionsHelper_Legacy.DamageTarget(p, c, this, AbstractGameAction.AttackEffect.NONE, true);
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
}