package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.WisdomStance;
import eatyourbeets.utilities.GameActions;

public class SabiHakuhei extends AnimatorCard {
    public static final EYBCardData DATA = Register(SabiHakuhei.class).SetAttack(1, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public SabiHakuhei() {
        super(DATA);

        Initialize(9, 0, 2, 2);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(0, 0, 1);
        SetAffinity_Blue(1, 0, 1);

        SetAffinityRequirement(Affinity.Blue, 4);
        SetAffinityRequirement(Affinity.Green, 4);

        SetExhaust(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + player.currentBlock * magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        DoEffect();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.LoseBlock(player.currentBlock);
    }

    public void DoEffect() {
        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Green).AddConditionalCallback(() -> {
                GameActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
            });
        }
    }
}