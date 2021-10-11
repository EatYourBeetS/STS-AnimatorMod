package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.curse.Curse_Delusion;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Tartaglia extends AnimatorCard {
    public static final EYBCardData DATA = Register(Tartaglia.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged, EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_Delusion(), false));

    public Tartaglia() {
        super(DATA);

        Initialize(8, 0, 1);
        SetUpgrade(3, 0);
        SetAffinity_Fire(1, 0, 0);
        SetAffinity_Air(1, 0, 2);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        for (AbstractCreature c : GameUtilities.GetAllCharacters(true)) {
            amount += GameUtilities.GetPowerAmount(c, BurningPower.POWER_ID) * magicNumber;
        }

        return super.ModifyDamage(enemy, amount);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_LIGHT)
                .AddCallback((targets) ->
                {
                    for (AbstractCreature t : targets) {
                        if (GameUtilities.IsDeadOrEscaped(t) && CombatStats.TryActivateLimited(cardID))
                        {
                            GameActions.Bottom.MakeCardInDrawPile(new Curse_Delusion());
                            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
                            break;
                        }
                    }
                });

        for (AbstractCreature c : GameUtilities.GetAllCharacters(true)) {
            GameActions.Bottom.RemovePower(p, c, BurningPower.POWER_ID);
        }
    }
}