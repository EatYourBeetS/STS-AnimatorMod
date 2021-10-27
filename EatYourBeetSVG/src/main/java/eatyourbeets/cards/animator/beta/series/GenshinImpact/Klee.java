package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.JumpyDumpty;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;

public class Klee extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Klee.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new JumpyDumpty(), true));

    public Klee()
    {
        super(DATA);

        Initialize(2, 0, 15, 2);
        SetUpgrade(0, 0, 5, 0);
        SetAffinity_Red(1, 1, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(0,0,1);

        SetAffinityRequirement(Affinity.Red, 6);

        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SMALL_EXPLOSION);
        GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(magicNumber));
        //GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Burning, secondaryValue);

        int cardCount = secondaryValue;
        if (TrySpendAffinity(Affinity.Red)) {
            cardCount += 1;
        }
        for (int i = 0; i < cardCount; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new JumpyDumpty()).SetUpgrade(upgraded, false);
        }
    }
}