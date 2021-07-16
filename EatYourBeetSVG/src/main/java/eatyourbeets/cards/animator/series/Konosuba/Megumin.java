package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Megumin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Megumin.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Megumin()
    {
        super(DATA);

        Initialize(10, 0);
        SetUpgrade( 2, 0);

        SetAffinity_Blue(2, 0, 4);

        SetExhaust(true);
        SetUnique(true, true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 2 == 0)
        {
            upgradeDamage(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Add(SFX.LightningPassive(0.1f, true));
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.Add(SFX.LightningPassive(0.2f, true));
        GameActions.Bottom.BorderFlash(Color.ORANGE);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.Add(SFX.LightningPassive(0.3f, true));
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.BorderFlash(Color.RED);
        GameActions.Bottom.Add(SFX.LightningEvoke(0.5f, true));

        for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(VFX.FlameBarrier(m1.hb));
            GameActions.Bottom.VFX(VFX.SmallExplosion(m1.hb));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
            .IncludeMasterDeck(true)
            .IsCancellable(false);
        }
    }
}