package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.WisdomStance;
import pinacolada.utilities.PCLActions;

public class SabiHakuhei extends PCLCard {
    public static final PCLCardData DATA = Register(SabiHakuhei.class).SetAttack(1, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public SabiHakuhei() {
        super(DATA);

        Initialize(9, 0, 2, 2);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Blue, 4);
        SetAffinityRequirement(PCLAffinity.Green, 4);

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

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        PCLActions.Bottom.LoseBlock(player.currentBlock);
    }

    public void DoEffect() {
        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Green).AddConditionalCallback(() -> {
                PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
            });
        }
    }
}