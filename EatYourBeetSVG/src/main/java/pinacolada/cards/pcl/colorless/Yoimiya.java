package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.BlazingHeat;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Yoimiya extends PCLCard
{
    public static final PCLCardData DATA = Register(Yoimiya.class)
            .SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Ranged, eatyourbeets.cards.base.EYBCardTarget.Normal)
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

        Initialize(2, 0, 1, 8);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetExhaust(true);
        SetHitCount(4, 0);
    }

    @Override
    public void OnUpgrade() {
        AddScaling(PCLAffinity.Red, 1);
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.DAGGER).forEach(d ->
                d.SetVFXColor(Color.FIREBRICK, Color.SCARLET)
                        .AddCallback(e -> {
                    if (IsStarter() && e.lastDamageTaken > 0) {
                        PCLActions.Bottom.CreateThrowingKnives(1).SetUpgrade(upgraded);
                    }
        }));

        int total = 0;
        for (AbstractPower debuff : player.powers) {
            for (PCLPowerHelper commonDebuffHelper : PCLGameUtilities.GetPCLCommonDebuffs()) {
                if (commonDebuffHelper.ID.equals(debuff.ID)) {
                    int amount = PCLGameUtilities.GetPowerAmount(player, debuff.ID);
                    if (IsStarter()) {
                        PCLActions.Bottom.ApplyPower(TargetHelper.Player(), commonDebuffHelper, amount);
                        total += amount;
                    }

                    total += amount;
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