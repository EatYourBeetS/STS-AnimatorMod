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
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

public class Megumin extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Megumin.class.getSimpleName(), EYBCardBadge.Synergy);

    public Megumin()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(12, 0);

        SetMultiDamage(true);
        SetExhaust(true);
        SetUnique(true);
        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (Spellcaster.GetScaling() * 4));
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

        if (HasActiveSynergy() && PlayerStatistics.TryActivateLimited(cardID))
        {
            for (AbstractCard c : GetAllInstances())
            {
                Megumin megumin = Utilities.SafeCast(c, Megumin.class);
                if (megumin != null)
                {
                    megumin.upgrade();
                }
            }
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }

    @Override
    public void upgrade()
    {
        this.timesUpgraded += 1;

        upgradeDamage(2);

        this.upgraded = true;
        this.name = cardData.strings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }
}