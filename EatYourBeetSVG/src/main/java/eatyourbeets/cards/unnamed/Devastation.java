package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Devastation extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Devastation.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Devastation()
    {
        super(DATA);

        Initialize(16, 0, 8, 16);

        SetMastery(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
//        GameActionsHelper_Legacy.AddToBottom(new DecreaseMaxHpAction(p, secondaryValue));
//
//        ArrayList<AbstractMonster> characters = GameUtilities.GetCurrentEnemies(true);
//
//        GameActions.Bottom.VFX(new BorderFlashEffect(Color.ORANGE));
//        GameActions.Bottom.Wait(0.35f);
//        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.2F);
//        GameActions.Bottom.VFX((new BorderLongFlashEffect(Color.RED)));
//        //GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f));
//        for (int i = 0; i < magicNumber; i++)
//        {
//            RandomizedList<AbstractMonster> enemies = new RandomizedList<>(characters);
//            while (enemies.Count() > 0)
//            {
//                AbstractMonster c = enemies.Retrieve(AbstractDungeon.cardRandomRng);
//                //ImpactEffect(c);
//                float width = c.hb.width / 2f;
//                float height = c.hb.height / 2f;
//                float x = c.hb.cX + AbstractDungeon.cardRandomRng.random(-width, width);
//                float y = c.hb.cY + AbstractDungeon.cardRandomRng.random(-height, height);
//
//                GameActionsHelper_Legacy.VFX(new CataclysmEffect(x, y));
//                GameActionsHelper_Legacy.DamageTarget(p, c, this, AbstractGameAction.AttackEffect.NONE, true);
//            }
//        }
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