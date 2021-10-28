package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SabiHakuhei extends AnimatorCard {
    public static final EYBCardData DATA = Register(SabiHakuhei.class).SetAttack(1, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public SabiHakuhei() {
        super(DATA);

        Initialize(9, 0, 8, 2);
        SetUpgrade(3, 0, 2);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(0, 0, 1);
        SetAffinity_Blue(1, 0, 1);

        SetAffinityRequirement(Affinity.Blue, 4);
        SetAffinityRequirement(Affinity.Green, 4);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + GameUtilities.GetPowerAmount(player, BlurPower.POWER_ID) * magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        DoEffect();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.RemovePower(p, p, BlurPower.POWER_ID);
    }

    public void DoEffect() {
        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Green).AddConditionalCallback(() -> {
                GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
            });
        }
    }
}