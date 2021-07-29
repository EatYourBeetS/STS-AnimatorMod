package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.curse.Curse_Delusion;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Tartaglia extends AnimatorCard {
    public static final EYBCardData DATA = Register(Tartaglia.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Curse_Delusion(), false);
    }

    public Tartaglia() {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(2, 0);
        SetAffinity_Green(2, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            amount += GameUtilities.GetPowerAmount(enemy, BurningPower.POWER_ID) / (2.0);
        }

        return super.ModifyDamage(enemy, amount);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
            .AddCallback(m.currentBlock, (initialBlock, target) ->
            {
                if (GameUtilities.IsDeadOrEscaped(target) && CombatStats.TryActivateLimited(cardID))
                {
                    GameActions.Bottom.MakeCardInDrawPile(new Curse_Delusion());
                    GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
                }

            });
        }

        GameActions.Bottom.RemovePower(p, m, BurningPower.POWER_ID);
    }
}