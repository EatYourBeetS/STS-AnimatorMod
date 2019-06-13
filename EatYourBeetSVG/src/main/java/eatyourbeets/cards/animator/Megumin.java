package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Megumin extends AnimatorCard//_SavableInteger implements CustomSavable<Integer>
{
    public static final String ID = CreateFullID(Megumin.class.getSimpleName());
    private static final int ORIGINAL_DAMAGE = 14;

    public Megumin()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(ORIGINAL_DAMAGE, 0);

        this.isMultiDamage = true;
        this.exhaust = true;
        
        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_PASSIVE", 0.1F));
        GameActionsHelper.AddToBottom(new WaitAction(0.35f));
        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_PASSIVE", 0.2F));
        GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.ORANGE)));
        GameActionsHelper.AddToBottom(new WaitAction(0.35f));
        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_PASSIVE", 0.3F));
        GameActionsHelper.AddToBottom(new WaitAction(0.35f));
        GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.RED)));
        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.5f));
        for (AbstractCreature m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.AddToBottom(new VFXAction(new FlameBarrierEffect(m1.hb_x, m1.hb_y)));
            GameActionsHelper.AddToBottom(new VFXAction(new ExplosionSmallEffect(m1.hb_x, m1.hb_y)));
        }
        GameActionsHelper.AddToBottom(new DamageAllEnemiesAction(p, multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));

        if (HasActiveSynergy())
        {
            for (AbstractCard c : GetAllInstances())
            {
                Megumin megumin = Utilities.SafeCast(c, Megumin.class);
                if (megumin != null)
                {
                    megumin.upgrade();
//                    megumin.secondaryValue += this.magicNumber;
//                    megumin.baseSecondaryValue = megumin.secondaryValue;
//                    megumin.applyPowers();
                }
            }
        }
    }

//    public void applyPowers()
//    {
//        this.baseDamage = ORIGINAL_DAMAGE + this.secondaryValue;
//        super.applyPowers();
//        initializeDescription();
//    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }

    @Override
    public void upgrade()
    {
        this.timesUpgraded += 1;

//        int damageBonus = 2;
//        if (timesUpgraded % 2 == 0)
//        {
//            damageBonus += 1;
//        }
//        upgradeDamage(damageBonus);
        upgradeDamage(2);

        this.upgraded = true;
        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }

//    @Override
//    protected void SetValue(Integer integer)
//    {
//        super.SetValue(integer);
//        this.baseDamage = ORIGINAL_DAMAGE + this.secondaryValue;
//    }
}