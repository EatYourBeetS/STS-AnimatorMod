package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.animator.beta.special.JumpyDumpty;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Klee extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Klee.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    static
    {
        DATA.AddPreview(new JumpyDumpty(), true);
    }

    public Klee()
    {
        super(DATA);

        Initialize(3, 0, 2, 2);
        SetUpgrade(1, 0, 0, 0);
        SetAffinity_Red(1, 1, 0);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m1.hb.cX, m1.hb.cY), 0.1F);
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Burning, magicNumber);

        int additionalCount = (CheckTeamwork(AffinityType.Red, 3) ? 1 : 0);
        for (int i = 0; i < secondaryValue + additionalCount; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new JumpyDumpty()).SetUpgrade(upgraded, false);
        }
    }
}