package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.JumpyDumpty;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;

public class Klee extends PCLCard
{
    public static final PCLCardData DATA = Register(Klee.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Fire, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new JumpyDumpty(), true));

    public Klee()
    {
        super(DATA);

        Initialize(2, 0, 15, 2);
        SetUpgrade(0, 0, 5, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(0,0,1);

        SetAffinityRequirement(PCLAffinity.Red, 6);
        SetAffinityRequirement(PCLAffinity.Light, 6);

        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SMALL_EXPLOSION);
        PCLActions.Bottom.Callback(() -> PCLCombatStats.AddEffectBonus(BurningPower.POWER_ID, magicNumber));

        for (int i = 0; i < secondaryValue; i++)
        {
            PCLActions.Bottom.MakeCardInDrawPile(new JumpyDumpty()).SetUpgrade(upgraded, false);
        }
        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Red, PCLAffinity.Light).AddConditionalCallback(() -> {
            PCLActions.Bottom.MakeCardInDrawPile(new JumpyDumpty()).SetUpgrade(upgraded, false);
        });
    }
}