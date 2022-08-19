package eatyourbeets.cards.animatorClassic.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Megumin extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Megumin.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Megumin()
    {
        super(DATA);

        Initialize(10, 0);
        SetUpgrade( 2, 0);
        SetScaling(4, 0, 0);

        SetExhaust(true);
        SetUnique(true, true);
        SetSeries(CardSeries.Konosuba);
        SetSpellcaster();
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.7f, 0.9f, 1.2f);
        GameActions.Bottom.WaitRealtime(0.5f);
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 1.1f, 1.3f, 1.2f);
        GameActions.Bottom.BorderFlash(Color.ORANGE);
        GameActions.Bottom.WaitRealtime(0.4f);
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.7f, 0.9f, 1.2f);
        GameActions.Bottom.WaitRealtime(0.4f);
        GameActions.Bottom.BorderFlash(Color.RED);
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 1.1f, 1.3f, 1.2f);
        GameActions.Bottom.Callback(() ->
        {
            for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
            {
                GameEffects.List.Attack(player, m1, AttackEffects.LIGHTNING, 0.7f, 0.8f, Color.RED);
                GameEffects.List.Add(VFX.FlameBarrier(m1.hb));
                for (int i = 0; i < 12; i++)
                {
                    GameEffects.List.Add(VFX.SmallExplosion(m1.hb, 0.4f).PlaySFX(i == 0));
                }
            }
        });
        GameActions.Bottom.WaitRealtime(0.35f);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE).SetVFX(true, true);

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
            .IncludeMasterDeck(true)
            .IsCancellable(false);
        }
    }
}