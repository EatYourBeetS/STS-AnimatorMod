package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Entoma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Entoma.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Entoma()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(1, 0, 0);

        SetAffinity_Dark(1);

        SetUnique(true, true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 0)
        {
            upgradeMagicNumber(1);
        }

        upgradedMagicNumber = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.POISON)
        .SetDamageEffect(e -> GameEffects.List.Add(VFX.Bite(e.hb, Color.GREEN)).duration)
        .AddCallback(enemy ->
        {
            GameActions.Bottom.ApplyPoison(player, enemy, magicNumber).ShowEffect(false, true);
            if (GameUtilities.IsFatal(enemy, true) && CombatStats.TryActivateLimited(cardID))
            {
                player.increaseMaxHp(2, false);

                GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                .IncludeMasterDeck(true)
                .IsCancellable(false);
            }
        });
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (CombatStats.TurnCount(true) > 0 && baseDamage > 0)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - 1));
        }
    }
}