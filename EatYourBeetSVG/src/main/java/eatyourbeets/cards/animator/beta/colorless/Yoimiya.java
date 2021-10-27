package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.beta.special.BlazingHeat;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Yoimiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yoimiya.class)
            .SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data -> {data.AddPreview(new BlazingHeat(), false);
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public Yoimiya()
    {
        super(DATA);

        Initialize(2, 0, 1, 5);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetExhaust(true);
        SetHitCount(4, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.DAGGER).forEach(d -> d.AddCallback(e -> {
            if (e.lastDamageTaken > 0) {
                GameActions.Bottom.CreateThrowingKnives(1).SetUpgrade(upgraded);
            }
        }));

        if (IsStarter()) {
            GameActions.Bottom.Callback(() -> {
                GameActions.Bottom.Cycle(name,magicNumber);
            });
        }

        int total = 0;
        for (AbstractPower debuff : player.powers) {
            for (PowerHelper commonDebuffHelper : GameUtilities.GetCommonDebuffs()) {
                if (commonDebuffHelper.ID.equals(debuff.ID)) {
                    int amount = GameUtilities.GetPowerAmount(player, debuff.ID);
                    GameActions.Bottom.ApplyPower(TargetHelper.Player(), commonDebuffHelper, amount);
                    total += amount * 2;
                }
            }
        }

        if (total > secondaryValue && CombatStats.TryActivateLimited(cardID))
        {
            AbstractCard c = new BlazingHeat();
            c.applyPowers();
            c.use(player, null);
        }
    }
}