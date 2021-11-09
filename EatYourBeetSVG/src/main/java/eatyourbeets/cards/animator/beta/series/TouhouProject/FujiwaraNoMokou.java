package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class FujiwaraNoMokou extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(FujiwaraNoMokou.class).SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public FujiwaraNoMokou()
    {
        super(DATA);

        Initialize(9, 0, 3, 0);
        SetUpgrade(3, 0, 1, 0);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Red(1, 0, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.FetchFromPile(name, 1, player.exhaustPile).SetOptions(true, false).SetFilter(c -> !GameUtilities.IsHindrance(c));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.TriggerOrbPassive(magicNumber, false, true).SetFilter(o -> Fire.ORB_ID.equals(o.ID)).AddCallback(orbs -> {
            if (orbs.size() > 0) {
                GameActions.Bottom.StackPower(new VigorPower(player, orbs.size()));
            }
        });
    }
}

