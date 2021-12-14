package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.orbs.RemoveOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Chung extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chung.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Chung()
    {
        super(DATA);

        Initialize(7, 4, 5, 2);
        SetUpgrade(2, 0, 2, 0);

        SetAffinity_Red(1);
        SetAffinity_Orange(1);
        SetAffinity_Blue(0,0,2);

        SetExhaust(true);
    }


    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        return super.ModifyDamage(enemy, damage + magicNumber * GameUtilities.GetOrbCount(Frost.ORB_ID));
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ChannelOrb(new Frost());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.ICE);
        GameActions.Bottom.GainBlock(block);

        boolean shouldEvoke = upgraded && auxiliaryData.form == 1;
        for (AbstractOrb orb : player.orbs) {
            if (orb != null && Frost.ORB_ID.equals(orb.ID)) {
                if (shouldEvoke) {
                    GameActions.Bottom.EvokeOrb(1, orb);
                }
                else {
                    GameActions.Bottom.Add(new RemoveOrb(orb));
                }

            }
        }
    }
}