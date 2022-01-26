package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.curse.Curse_Delusion;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.BurningPower;
import pinacolada.stances.pcl.MightStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Tartaglia extends PCLCard {
    public static final PCLCardData DATA = Register(Tartaglia.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Ranged, PCLCardTarget.AoE).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_Delusion(), false));

    public Tartaglia() {
        super(DATA);

        Initialize(12, 0, 5, 1);
        SetUpgrade(3, 0, 1);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    protected float GetInitialDamage()
    {
        int amount = 0;
        for (AbstractCreature c : PCLGameUtilities.GetAllCharacters(true)) {
            if (PCLGameUtilities.GetPowerAmount(c, BurningPower.POWER_ID) > 0) {
                amount += magicNumber;
            }
        }
        return baseDamage + amount;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.BLUNT_LIGHT).forEach(d -> d
                .AddCallback((targets) ->
                {
                    for (AbstractCreature t : targets) {
                        if (PCLGameUtilities.IsDeadOrEscaped(t) && CombatStats.TryActivateLimited(cardID))
                        {
                            PCLActions.Bottom.MakeCardInDrawPile(new Curse_Delusion());
                            PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
                            break;
                        }
                    }
                }));

        for (AbstractCreature c : PCLGameUtilities.GetAllCharacters(true)) {
            PCLActions.Bottom.RemovePower(p, c, BurningPower.POWER_ID);
        }

        PCLActions.Bottom.ApplyRippled(TargetHelper.Enemies(), secondaryValue);
    }
}