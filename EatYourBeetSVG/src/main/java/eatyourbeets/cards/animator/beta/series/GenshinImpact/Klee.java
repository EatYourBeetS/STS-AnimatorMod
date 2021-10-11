package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.JumpyDumpty;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Klee extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Klee.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new JumpyDumpty(), false));

    public Klee()
    {
        super(DATA);

        Initialize(2, 0, 2, 2);
        SetUpgrade(1, 0, 1, 0);
        SetAffinity_Red(1, 1, 0);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(Affinity.Red, 4);

        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SMALL_EXPLOSION);
        GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Vulnerable, magicNumber);

        int cardCount = secondaryValue;
        if (CheckAffinity(Affinity.Red)) {
            cardCount += 1;
        }
        if (GameUtilities.GetOrbCount(Fire.ORB_ID) >= 3) {
            cardCount += 1;
        }
        for (int i = 0; i < cardCount; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new JumpyDumpty()).SetUpgrade(upgraded, false);
        }
    }
}